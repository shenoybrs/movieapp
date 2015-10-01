package com.mobileteche.movieapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileteche.movieapp.data.MovieContract;
import com.mobileteche.movieapp.data.MovieContract.ReviewEntry;
import com.mobileteche.movieapp.data.MovieContract.TrailerEntry;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "detailfragmen";
    private static final int DETAIL_LOADER = 2;
    private static final int TRAIL_LOADER = 3;
    private static final int REVIEW_LOADER = 4;
    private CheckBox favorite;
    private Uri mUri;
    private TextView teaserName, teaserShortDesc, releasedate, rating;

    public MovieDetailActivityFragment() {
    }

    MovieModel movieModel = null;
    List<TrailerModel> trailerModel = null;
    List<ReviewModel> reviewsModel = null;
    private RecyclerView mTrailerRecyclerView, mReviewRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager, mLayoutManagerReviews;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Uri movieUri = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            movieUri = arguments.getParcelable(MovieDetailActivityFragment.DETAIL_URI);
        }

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        if (movieUri != null) {

            getLoaderManager().initLoader(TRAIL_LOADER, null, this);
            getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        }
        Log.v(LOG_TAG, "In onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MovieDetailActivityFragment.DETAIL_URI);
        }

        if ( mUri == null) {
            if (movieModel.id != 0)
                mUri = MovieContract.MovieEntry.buildMovieUri(movieModel.id);
            else
                return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        if (id == TRAIL_LOADER) {
            mUri = MovieContract.TrailerEntry.buildTrailerUri(Long.valueOf(MovieContract.MovieEntry.getMovieIdFromUri(mUri)));
        } else if (id == REVIEW_LOADER) {
            mUri = MovieContract.ReviewEntry.buildReviewUri(Long.valueOf(MovieContract.MovieEntry.getMovieIdFromUri(mUri)));
        }
        return new CursorLoader(
                getActivity(),
                mUri,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");

        if (!data.moveToFirst()) {
            return;
        }
        /*MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_ADULT + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH+ " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VIDEO + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL);";*/

        if (loader.getId() == DETAIL_LOADER) {

            movieModel.id = data.getInt(1);
            movieModel.adult = data.getInt(2) == 1;
            movieModel.backdrop_path = data.getString(3);
            movieModel.original_language = data.getString(4);
            movieModel.original_title = data.getString(5);
            movieModel.overview = data.getString(6);
            movieModel.popularity = data.getFloat(7);
            movieModel.poster_path = data.getString(8);
            movieModel.release_date = data.getString(9);
            movieModel.title = data.getString(10);
            movieModel.video = data.getInt(11) == 1;
            movieModel.vote_average = data.getFloat(12);
            movieModel.vote_count = data.getLong(13);
            movieModel.favorite = true;

            loadData(getView());
        } else if (loader.getId() == TRAIL_LOADER) {
            Log.d(LOG_TAG, "TRAIL_LOADER inside" + data.getCount());

            /*TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TrailerEntry.COLUMN_MOVIE_ID+ " INTEGER NOT NULL, " +

                TrailerEntry.COLUMN_TRAILER_ID + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_TRAILER_ISO+ " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_TRAILER_KEY+ " TEXT NOT NULL," +
                TrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_TRAILER_SITE + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_TRAILER_SIZE + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_TRAILER_TYPE+ " TEXT NOT NULL,  " +*/

            if (data.moveToFirst()) {
                do {
                    TrailerModel singleTrailerModel = new TrailerModel();
                    singleTrailerModel.id = data.getString(2);
                    singleTrailerModel.iso_639_1 = data.getString(3);
                    singleTrailerModel.key = data.getString(4);
                    singleTrailerModel.name = data.getString(5);
                    singleTrailerModel.site = data.getString(6);
                    singleTrailerModel.size = data.getInt(7);
                    singleTrailerModel.type = data.getString(8);

                    mTrailerAdapter.add(singleTrailerModel);
                } while (data.moveToNext());

            }
            mTrailerRecyclerView.setAdapter(mTrailerAdapter);
          /*  mTrailerRecyclerView.swapAdapter(mTrailerAdapter,true);*/
        } else if (loader.getId() == REVIEW_LOADER) {
            Log.d(LOG_TAG, "REVIEW_LOADER inside" + data.getCount());

/*
            ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    ReviewEntry.COLUMN_REVIEW_ID + " INTEGER NOT NULL," +
                    ReviewEntry.COLUMN_REVIEW_AUTHOR+ " TEXT NOT NULL, " +
                    ReviewEntry.COLUMN_REVIEW_CONTENT+ " TEXT NOT NULL, " +
                    ReviewEntry.COLUMN_REVIEW_URL + " TEXT NOT NULL, " +
*/
            if (data.moveToFirst()) {
                do {
                    ReviewModel singleReviewModel = new ReviewModel();

                    singleReviewModel.id = data.getString(2);
                    singleReviewModel.author = data.getString(3);
                    singleReviewModel.content = data.getString(4);
                    singleReviewModel.url = data.getString(5);

                    mReviewsAdapter.add(singleReviewModel);
                } while (data.moveToNext());
            }

            mReviewRecyclerView.setAdapter(mReviewsAdapter);
           /* mReviewRecyclerView.swapAdapter(mReviewsAdapter,true);*/
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    void onMovieChanged( String movieId ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {

            Uri updatedUri = MovieContract.MovieEntry.buildMovieUri(Long.valueOf(movieId));
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String json = null;
        Bundle bundle=getArguments();
        if (bundle!=null) {
             json = bundle.getString("key");
            if (json != null)
                Log.d(LOG_TAG, json);
        }
        String jsonMovieInfo = getActivity().getIntent().getStringExtra("key");
        movieModel = new MovieModel();
        if (json != null ) {

            trailerModel = new ArrayList<TrailerModel>();
            reviewsModel = new ArrayList<ReviewModel>();

            GsonParser<MovieModel> parser = new GsonParser<MovieModel>(
                    MovieModel.class);
            try {
                movieModel = parser.getGson().fromJson(json, MovieModel.class);
                //movieModelResponse = parser.parse(movieJsonStr.toString());
            } catch (Exception e) {

            }
            new RequestTrailerTask(getActivity()).execute(BuildConfig.THE_MOVIE_DB_API_KEY, String.valueOf(movieModel.id));
            new RequestReviewsTask(getActivity()).execute(BuildConfig.THE_MOVIE_DB_API_KEY, String.valueOf(movieModel.id));
        }


    }

    View mRootView;
    ImageButton back_arrow;
    ImageView bannerImageview;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewsAdapter;
    static final String DETAIL_URI = "URI";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MovieDetailActivityFragment.DETAIL_URI);
        }
        mRootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        teaserName = ((TextView) mRootView.findViewById(R.id.teaserName));
        teaserShortDesc = ((TextView) mRootView.findViewById(R.id.teaserShortDesc));
        releasedate = ((TextView) mRootView.findViewById(R.id.releasedate));
        rating = ((TextView) mRootView.findViewById(R.id.rating));

        bannerImageview = (ImageView) mRootView.findViewById(R.id.iconTeaserBanner);
        mTrailerRecyclerView = (RecyclerView) mRootView.findViewById(R.id.trailer_recycler_view);
        mReviewRecyclerView = (RecyclerView) mRootView.findViewById(R.id.review_recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(mLayoutManager);


        mLayoutManagerReviews = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(mLayoutManagerReviews);

        mTrailerAdapter = new TrailerAdapter(getActivity()/*, null, 0*/);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mReviewsAdapter = new ReviewAdapter(getActivity()/*, null, 0*/);
        mReviewRecyclerView.setAdapter(mReviewsAdapter);


        if (movieModel.id != 0) {
            loadData(mRootView);
        }
        return mRootView;
    }

    private void loadData(View mRootView) {
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("MMM dd, yyyy");
        java.util.Date date = null;
        try {
            date = format.parse(movieModel.release_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //back_arrow = (ImageButton) mRootView.findViewById(R.id.back_arrow);

        /*back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });*/
        teaserName.setText(movieModel.title);
        teaserShortDesc.setText(movieModel.overview);
        releasedate.setText(DesiredFormat.format(date));
        rating.setText(String.valueOf(movieModel.vote_average) + "/10");
        favorite = (CheckBox) mRootView.findViewById(R.id.favorite);
        favorite.setChecked(movieModel.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    long movieId = addMovie(movieModel);
                    addTrailer(Long.valueOf(movieModel.id), trailerModel);
                    addReviews(Long.valueOf(movieModel.id), reviewsModel);
                } else {

                    removeTrailer(movieModel.id);
                    removeReview(movieModel.id);
                    removeMovie(movieModel.id);
                }


            }
        });
        String url = "http://image.tmdb.org/t/p/w185/" + movieModel.backdrop_path;
        bannerImageview.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(getActivity()).load(url).into(bannerImageview);
    }

    private void addReviews(Long movieId, List<ReviewModel> reviewsModel) {
        // Insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(reviewsModel.size());

        for (ReviewModel singleReviewModel : reviewsModel) {
            ContentValues reviewValues = new ContentValues();
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, movieId);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, singleReviewModel.id);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, singleReviewModel.author);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, singleReviewModel.content);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_URL, singleReviewModel.url);
            cVVector.add(reviewValues);
        }

        int inserted = 0;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = getActivity().getContentResolver().bulkInsert(ReviewEntry.buildReviewMovie(movieId.toString()), cvArray);
            Log.d(LOG_TAG, "The num is " + inserted);
        }
    }

    private void removeMovie(long movieId) {
        //delete movie
        getActivity().getContentResolver().
                delete(MovieContract.MovieEntry.buildMovieUri(movieId), null, null);
    }

    private void removeTrailer(long movieId) {
        //delete trailer for the movie
        getActivity().getContentResolver().
                delete(MovieContract.TrailerEntry.buildTrailerMovie(String.valueOf(movieId)), null, null);
    }

    private void removeReview(long movieId) {
        //delete review for the movie
        getActivity().getContentResolver().
                delete(MovieContract.ReviewEntry.buildReviewMovie(String.valueOf(movieId)), null, null);
    }

    private void addTrailer(Long movieId, List<TrailerModel> trailerModel) {
        // Insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(trailerModel.size());

        for (TrailerModel singleTrailerModel : trailerModel) {
            ContentValues trailerValues = new ContentValues();
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, movieId);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY, singleTrailerModel.key);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, singleTrailerModel.id);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ISO, singleTrailerModel.iso_639_1);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME,
                    singleTrailerModel.name);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_SITE, singleTrailerModel.site);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_SIZE, singleTrailerModel.size);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_TYPE, singleTrailerModel.type);
            cVVector.add(trailerValues);
        }

        int inserted = 0;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = getActivity().getContentResolver().bulkInsert(TrailerEntry.buildTrailerMovie(movieId.toString()), cvArray);
            Log.d(LOG_TAG, "The num is " + inserted);
        }

    }


    private long addMovie(MovieModel movieModel) {
// Finally, insert location data into the database.
        long movieId;
        Uri insertedUri = getActivity().getContentResolver().insert(
                MovieContract.MovieEntry.CONTENT_URI,
                createMovieValues(movieModel)
        );
        // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
        movieId = ContentUris.parseId(insertedUri);
        return movieId;
    }


    private ContentValues createMovieValues(MovieModel movieModel) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_ADULT, movieModel.adult);
        testValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movieModel.backdrop_path);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieModel.id);
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movieModel.original_language);
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieModel.original_title);
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieModel.overview);
        testValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, Float.valueOf(movieModel.popularity));
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movieModel.poster_path);
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieModel.release_date);
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieModel.title);
        testValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, movieModel.video);
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieModel.vote_average);
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movieModel.vote_count);


        return testValues;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_movie_detail, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class RequestTrailerTask extends AsyncTask<String, Void, List<TrailerModel>> {

        private final String LOG_TAG = RequestTrailerTask.class.getSimpleName();

        private final Context mContext;

        public RequestTrailerTask(Context context) {
            mContext = context;
        }

        private boolean DEBUG = true;
        String movieJsonStr = null;

        @Override
        protected List<TrailerModel> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                String restURL = "http://api.themoviedb.org/3/movie/" + params[1] + "/videos?api_key=" + params[0];

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
            TrailerModelResponse trailerModelResponse = new TrailerModelResponse();
            trailerModelResponse.results = new ArrayList<TrailerModel>();

            GsonParser<TrailerModelResponse> parser = new GsonParser<TrailerModelResponse>(
                    TrailerModelResponse.class);
            try {
                trailerModelResponse = parser.parse(movieJsonStr.toString());
            } catch (Exception e) {
                return null;
            }


            List<TrailerModel> data = trailerModelResponse.results;

            return data;
        }

        @Override
        protected void onPostExecute(List<TrailerModel> trailerModels) {

            if (trailerModels != null && !trailerModels.isEmpty()) {
                for (int count = 0; count < trailerModels.size(); count++) {
                    mTrailerAdapter.add(trailerModels.get(count));
                }
                trailerModel.addAll(trailerModels);
                mTrailerRecyclerView.setAdapter(mTrailerAdapter);
            }

        }
    }

    public class RequestReviewsTask extends AsyncTask<String, Void, List<ReviewModel>> {

        private final String LOG_TAG = RequestReviewsTask.class.getSimpleName();

        private final Context mContext;

        public RequestReviewsTask(Context context) {
            mContext = context;
        }

        private boolean DEBUG = true;
        String movieJsonStr = null;

        @Override
        protected List<ReviewModel> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                String restURL = "http://api.themoviedb.org/3/movie/" + params[1] + "/reviews?api_key=" + params[0];

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
            ReviewsModelResponse reviewsModelResponse = new ReviewsModelResponse();
            reviewsModelResponse.results = new ArrayList<ReviewModel>();

            GsonParser<ReviewsModelResponse> parser = new GsonParser<ReviewsModelResponse>(
                    ReviewsModelResponse.class);
            try {
                reviewsModelResponse = parser.parse(movieJsonStr.toString());
            } catch (Exception e) {
                return null;
            }


            List<ReviewModel> data = reviewsModelResponse.results;

            return data;
        }

        @Override
        protected void onPostExecute(List<ReviewModel> reviewsModels) {

            if (reviewsModels != null && !reviewsModels.isEmpty()) {
                for (int count = 0; count < reviewsModels.size(); count++) {
                    mReviewsAdapter.add(reviewsModels.get(count));
                }
                reviewsModel.addAll(reviewsModels);
                mReviewRecyclerView.setAdapter(mReviewsAdapter);
            }

        }
    }
}
