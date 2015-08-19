package com.mobileteche.movieapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MovieActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieactivity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieActivityFragment())
                    .commit();
        }
    }


}
