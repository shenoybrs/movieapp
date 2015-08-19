package com.mobileteche.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
public class MovieActivityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MovieActivityFragment.class.toString();
    String movieJsonStr = null;
    public ImageAdapter imageAdapter = null;
    public GridView gridView;
    String sortOption = "popularity";
    boolean settingChanged = false;
    SharedPreferences preference;

    public MovieActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortOption = getResources().getString(R.string.pref_sort_default);
        preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preference.registerOnSharedPreferenceChangeListener(this);
        setHasOptionsMenu(true);
        if (savedInstanceState != null && savedInstanceState.containsKey("key")) {

            movieJsonStr = savedInstanceState.getString("key");
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

            imageAdapter = new ImageAdapter(getActivity());
            new RequestMovieDataTask().execute(sortOption, null);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movieactivity, container, false);
        gridView = (GridView) rootView.findViewById(R.id.moviegrid);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieModel moviedata = (MovieModel) imageAdapter.getItem(position);
                GsonParser<MovieModel> parser = new GsonParser<MovieModel>(
                        MovieModel.class);
                String jsonMovieData = parser.getGson().toJson(moviedata);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("key", jsonMovieData);
                startActivity(intent);
            }
        });
        String userChoice = null;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (settingChanged) {
            imageAdapter.removeAll();
            new RequestMovieDataTask().execute(sortOption, null);
            settingChanged = false;
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
                switch (sortOption) {
                    case "most popular":
                        sortOption = "popularity";
                        break;
                    case "highest rated":
                        sortOption = "vote_count";
                        break;
                    default:
                        sortOption = "popularity";
                }
                settingChanged = true;
            }
        }
    }

    public class RequestMovieDataTask extends AsyncTask<String, Void, List<MovieModel>> {

        @Override
        protected List<MovieModel> doInBackground(String... params) {
            {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;


                try {

                    URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=" + params[0] + ".desc&api_key=77e87c5300254bcd2ba52c23aa38a693");

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

            if (movieModels!=null&&!movieModels.isEmpty()) {
                for (int count = 0; count < movieModels.size(); count++) {
                    imageAdapter.add(movieModels.get(count));
                }
                gridView.setAdapter(imageAdapter);
            }

        }

    }
}
