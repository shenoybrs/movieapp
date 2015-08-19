package com.mobileteche.movieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }
    MovieModel movieModel = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jsonMovieInfo = getActivity().getIntent().getStringExtra("key");
         movieModel = new MovieModel();

        GsonParser<MovieModel> parser = new GsonParser<MovieModel>(
                MovieModel.class);
        try {
            movieModel = parser.getGson().fromJson(jsonMovieInfo, MovieModel.class);
            //movieModelResponse = parser.parse(movieJsonStr.toString());
        } catch (Exception e) {

        }
    }
    View mRootView;
    ImageButton back_arrow;
    ImageView bannerImageview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("MMM dd, yyyy");
        java.util.Date date = null;
        try {
            date = format.parse(movieModel.release_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
        mRootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        back_arrow = (ImageButton) mRootView.findViewById(R.id.back_arrow);
        bannerImageview = (ImageView)mRootView.findViewById(R.id.iconTeaserBanner);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        ((TextView) mRootView.findViewById(R.id.teaserName)).setText(movieModel.title);
        ((TextView) mRootView.findViewById(R.id.teaserShortDesc)).setText(movieModel.overview);
        ((TextView) mRootView.findViewById(R.id.releasedate)).setText(DesiredFormat.format(date));
        ((TextView) mRootView.findViewById(R.id.rating)).setText(String.valueOf(movieModel.vote_average)+"/10");
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/original/"+movieModel.backdrop_path).into(bannerImageview);
        return mRootView;
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

}
