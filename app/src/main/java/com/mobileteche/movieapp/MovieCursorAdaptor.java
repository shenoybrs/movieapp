package com.mobileteche.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Santosh.Shenoy on 24-09-2015.
 */
public class MovieCursorAdaptor extends CursorAdapter {

    public MovieCursorAdaptor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ImageView imageView;

            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);

        return imageView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //imageView.setImageResource(mThumbIds[position]);
        String url = "http://image.tmdb.org/t/p/w500/"+convertCursorRowToUXFormat(cursor);
        //Log.d(TAG,"The URL hit is :"+url);
        Picasso.with(context).load(url).into((ImageView)view);
    }

    private String convertCursorRowToUXFormat(Cursor cursor) {


        return
                cursor.getString(MovieActivityFragment.COL_POSTER_PATH);

    }
}
