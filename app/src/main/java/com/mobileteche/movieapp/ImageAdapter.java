package com.mobileteche.movieapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Santosh.Shenoy on 12-08-2015.
 */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = ImageAdapter.class.toString() ;
    private Context mContext;

    public void removeAll()
    {
        imageList.clear();
    }

    public boolean add(MovieModel object) {

        imageList.add(object);
        return true;
    }


    private List<MovieModel> imageList = new ArrayList<MovieModel>();


    public ImageAdapter(Context c) {
        mContext = c;


    }

    public int getCount() {
        return imageList.size();
    }

    public Object getItem(int position) {
        return imageList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);
        String url = "http://image.tmdb.org/t/p/w500/"+imageList.get(position).poster_path;
        //Log.d(TAG,"The URL hit is :"+url);
        Picasso.with(mContext).load(url).into(imageView);
        return imageView;
    }


}
