package com.mobileteche.movieapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.mobileteche.movieapp.data.MovieContract;
import com.mobileteche.movieapp.data.MovieContract.MovieEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieActivityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MovieActivityFragment.class.toString();
    private static final int MOVIE_LOADER = 0;
    String movieJsonStr = null;
    public ImageAdapter imageAdapter = null;
    public MovieCursorAdaptor imageCursorAdapter = null;
    public GridView gridView;
    String sortOption = "popularity";
    boolean settingChanged = false;
    SharedPreferences preference;
    private String apiKey = null;


    public static final String[] MOVIE_COLUMNS = {

            MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_ADULT,
            MovieEntry.COLUMN_BACKDROP_PATH,
            MovieEntry.COLUMN_ORIGINAL_LANGUAGE,
            MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry.COLUMN_POPULARITY,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_VIDEO,
            MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieEntry.COLUMN_VOTE_COUNT
    };

    static final int COL_MOVIE_ID = 1;
    static final int COL_ADULT = 2;
    static final int COL_BACKDROP_PATH = 3;
    static final int COL_ORIGINAL_LANGUAGE = 4;
    static final int COL_ORIGINAL_TITLE = 5;
    static final int COL_OVERVIEW = 6;
    static final int COL_POPULARITY = 7;
    static final int COL_POSTER_PATH = 8;
    static final int COL_RELEASE_DATE = 9;
    static final int COL_TITLE = 10;
    static final int COL_VIDEO = 11;
    static final int COL_VOTE_AVERAGE = 12;
    static final int COL_VOTE_COUNT = 13;


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Intent movieIntent ,MovieModel movieModel);
    }



    public MovieActivityFragment() {
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Sort order:  Ascending, by date.

        Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                movieUri,
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (pbHeaderProgress != null)
            pbHeaderProgress.setVisibility(View.INVISIBLE);
        imageCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imageCursorAdapter.swapCursor(null);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortOption = preference.getString(getString(R.string.sortoption), getString(R.string.pref_sort_default));
        sortOption = findSortOption(sortOption);

        preference.registerOnSharedPreferenceChangeListener(this);
        apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;
        if (apiKey != "") {

            setHasOptionsMenu(true);
            if (savedInstanceState != null && savedInstanceState.containsKey("key")) {
                if (pbHeaderProgress != null)
                pbHeaderProgress.setVisibility(View.INVISIBLE);
                movieJsonStr = savedInstanceState.getString("key");
                if (movieJsonStr != null) {
                    MovieModelResponse movieModelResponse = new MovieModelResponse();
                    movieModelResponse.results = new ArrayList<MovieModel>();
                    GsonParser<MovieModelResponse> parser = new GsonParser<MovieModelResponse>(
                            MovieModelResponse.class);
                    try {
                        movieModelResponse = parser.getGson().fromJson(movieJsonStr, MovieModelResponse.class);
                        //movieModelResponse = parser.parse(movieJsonStr.toString());
                    } catch (Exception e) {

                    }
                    imageAdapter = new ImageAdapter(getActivity());
                    for (int count = 0; count < movieModelResponse.results.size(); count++) {

                        imageAdapter.add(movieModelResponse.results.get(count));
                    }
                } else {
                    if (sortOption != "favourite") {
                        if (isNetworkAvailable()) {
                            imageAdapter = new ImageAdapter(getActivity());
                            new RequestMovieDataTask().execute(apiKey, sortOption);
                        } else {
                            new AlertDialog.Builder(getActivity()).
                                    setTitle(getString(R.string.no_network_title)).
                                    setMessage(getString(R.string.no_network_message)).
                                    setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    }).
                                    show();
                        }
                    } else {
                        //loader manager can be called to get the favorited item


                        imageCursorAdapter = new MovieCursorAdaptor(getActivity(), null, 0);
                        getLoaderManager().initLoader(MOVIE_LOADER, null, this);

                    }
                }

            } else {

                if (sortOption != "favourite") {
                    if (isNetworkAvailable()) {
                        imageAdapter = new ImageAdapter(getActivity());
                        new RequestMovieDataTask().execute(apiKey, sortOption);
                    } else {
                        new AlertDialog.Builder(getActivity()).
                                setTitle(getString(R.string.no_network_title)).
                                setMessage(getString(R.string.no_network_message)).
                                setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                }).
                                show();
                    }
                } else {
                    //loader manager can be called to get the favorited item
                    imageCursorAdapter = new MovieCursorAdaptor(getActivity(), null, 0);
                    //gridView.setAdapter(imageCursorAdapter);
                    getLoaderManager().initLoader(MOVIE_LOADER, null, this);

                }
            }
        } else {
            new AlertDialog.Builder(getActivity()).
                    setTitle(getActivity().getString(R.string.api_error_title)).
                    setMessage(getActivity().getString(R.string.api_error_message)).
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    }).
                    show();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("key", movieJsonStr);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_movieactivity, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {

            startActivity(new Intent(getActivity(), SettingsActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    LinearLayout pbHeaderProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movieactivity, container, false);

        gridView = (GridView) rootView.findViewById(R.id.moviegrid);
        pbHeaderProgress = (LinearLayout) rootView.findViewById(R.id.linlaHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        if (sortOption != "favourite") {
            gridView.setAdapter(imageAdapter);
        } else {
            gridView.setAdapter(imageCursorAdapter);
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                MovieModel moviedata = null;
                if (sortOption == "favourite") {
                    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                    if (cursor != null) {

                        intent.setData(MovieContract.MovieEntry.buildMovieUri(
                                cursor.getLong(COL_MOVIE_ID)));


                    }
                } else {
                    moviedata = (MovieModel) imageAdapter.getItem(position);

                    GsonParser<MovieModel> parser = new GsonParser<MovieModel>(
                            MovieModel.class);
                    String jsonMovieData = parser.getGson().toJson(moviedata);

                    intent.putExtra("key", jsonMovieData);

                }
                ((Callback) getActivity())
                        .onItemSelected(intent, moviedata);

            }
        });
        String userChoice = null;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (settingChanged) {
            if (pbHeaderProgress != null)
                pbHeaderProgress.setVisibility(View.VISIBLE);
            if (sortOption == "favourite") {
                //loader manager can be called to get the favorited item
                if (imageAdapter != null)
                    imageAdapter.removeAll();
                imageCursorAdapter = new MovieCursorAdaptor(getActivity(), null, 0);
                gridView.setAdapter(imageCursorAdapter);
                getLoaderManager().initLoader(MOVIE_LOADER, null, this);
            } else {
                if (isNetworkAvailable()) {
                    if (imageAdapter != null)
                        imageAdapter.removeAll();
                    else
                        imageAdapter = new ImageAdapter(getActivity());
                    new RequestMovieDataTask().execute(apiKey, sortOption);
                    settingChanged = false;
                } else {
                    new AlertDialog.Builder(getActivity()).
                            setTitle(getString(R.string.no_network_title)).
                            setMessage(getString(R.string.no_network_message)).
                            setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }
                            }).
                            show();
                }
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged");
        if (isAdded()) {
            if (key.contains(getString(R.string.sortoption))) {
                sortOption = sharedPreferences.getString(key, getString(R.string.pref_sort_default));
                sortOption = findSortOption(sortOption);
                settingChanged = true;
            }
        }
    }

    private String findSortOption(String sortOption) {
        String sortString;
        switch (sortOption) {
            case "most popular":
                sortString = "popularity";
                break;
            case "highest rated":
                sortString = "vote_count";
                break;
            case "favourite":
                sortString = "favourite";
                break;
            default:
                sortString = "popularity";
        }

        return sortString;
    }

    public class RequestMovieDataTask extends AsyncTask<String, Void, List<MovieModel>> {


        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


        }

        @Override
        protected List<MovieModel> doInBackground(String... params) {
            {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;


                try {

                    String restURL = "http://api.themoviedb.org/3/discover/movie?sort_by=" + params[1] + ".desc&api_key=" + params[0];

                    URL url = new URL(restURL);

                    // Create the request to OpenWeatherMap, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    movieJsonStr = buffer.toString();


                    Log.v("Data", movieJsonStr);
                } catch (IOException e) {
                    Log.e("PlaceholderFragment", "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("PlaceholderFragment", "Error closing stream", e);
                        }
                    }
                }

                MovieModelResponse movieModelResponse = new MovieModelResponse();
                movieModelResponse.results = new ArrayList<MovieModel>();

                GsonParser<MovieModelResponse> parser = new GsonParser<MovieModelResponse>(
                        MovieModelResponse.class);
                try {
                    movieModelResponse = parser.parse(movieJsonStr.toString());
                } catch (Exception e) {
                    return null;
                }


                List<MovieModel> data = movieModelResponse.results;

                return data;
            }
        }

        @Override
        protected void onPostExecute(List<MovieModel> movieModels) {
            if (pbHeaderProgress != null)
                pbHeaderProgress.setVisibility(View.GONE);
            if (movieModels != null && !movieModels.isEmpty()) {
                for (int count = 0; count < movieModels.size(); count++) {
                    imageAdapter.add(movieModels.get(count));
                }
                gridView.setAdapter(imageAdapter);
            }

        }

    }
}
