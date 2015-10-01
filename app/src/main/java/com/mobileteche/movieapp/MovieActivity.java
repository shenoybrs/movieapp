package com.mobileteche.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mobileteche.movieapp.data.MovieContract;


public class MovieActivity extends ActionBarActivity implements MovieActivityFragment.Callback {
    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieactivity);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onItemSelected(Intent movieIntent,MovieModel movieModel) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            //args.putParcelable(MovieDetailActivityFragment.DETAIL_URI, contentUri);
            //

            Intent intent = new Intent(movieIntent);

            Bundle args = new Bundle();
            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            String json = intent.getStringExtra("key");
            if (json != null) {

                args.putString("key", json);


                fragment.setArguments(args);
            }
            else
            {

                Uri uri = intent.getData();

                if (uri != null) {
                        uri = MovieContract.MovieEntry.buildMovieUri(Long.parseLong(MovieContract.MovieEntry.getMovieIdFromUri(uri)));
                        args.putParcelable(MovieDetailActivityFragment.DETAIL_URI, uri);
                        fragment.setArguments(args);
                }

            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {

            Intent intent = new Intent(movieIntent);
            Uri uri = intent.getData();
            if (uri != null) {
                uri = MovieContract.MovieEntry.buildMovieUri(Long.parseLong(MovieContract.MovieEntry.getMovieIdFromUri(uri)));
                intent.putExtra(MovieDetailActivityFragment.DETAIL_URI, uri);

            }

            startActivity(intent);
        }
    }
}
