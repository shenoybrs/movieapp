package com.mobileteche.movieapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;


public class MovieDetailActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar
                .setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.hide();
        setContentView(R.layout.activity_movie_detail);
        MovieDetailActivityFragment movieDetailActivityFragment =new MovieDetailActivityFragment();
        movieDetailActivityFragment.setArguments(getIntent().getExtras());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()

                    .add(R.id.container_detail,movieDetailActivityFragment )
                    .commit();
        }
    }



}
