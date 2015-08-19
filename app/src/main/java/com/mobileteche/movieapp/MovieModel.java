package com.mobileteche.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Santosh.Shenoy on 13-08-2015.
 */
public class MovieModel implements Parcelable {
    public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>() {

        public MovieModel createFromParcel(Parcel parcel) {
            return new MovieModel(parcel);
        }

        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public boolean adult;
    public String backdrop_path;
    public int id;
    public String original_language;
    public String original_title;
    public String overview;
    public String release_date;
    public String poster_path;
    public float popularity;
    public String title;
    public boolean video;
    public float vote_average;
    public Long vote_count;


    public MovieModel(Parcel parcel) {
        adult = ParcelUtils.readString(parcel) == "true";
        backdrop_path = ParcelUtils.readString(parcel);
        id = parcel.readInt();
        original_language = ParcelUtils.readString(parcel);
        original_title = ParcelUtils.readString(parcel);
        overview = ParcelUtils.readString(parcel);
        release_date = ParcelUtils.readString(parcel);
        poster_path = ParcelUtils.readString(parcel);
        popularity = parcel.readFloat();
        title = ParcelUtils.readString(parcel);
        video = ParcelUtils.readString(parcel) == "true";
        vote_average = parcel.readFloat();
        vote_count = parcel.readLong();
    }

    public MovieModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        adult = ParcelUtils.readString(dest) == "true";
        backdrop_path = ParcelUtils.readString(dest);
        id = dest.readInt();
        original_language = ParcelUtils.readString(dest);
        original_title = ParcelUtils.readString(dest);
        overview = ParcelUtils.readString(dest);
        release_date = ParcelUtils.readString(dest);
        poster_path = ParcelUtils.readString(dest);
        popularity = dest.readFloat();
        title = ParcelUtils.readString(dest);
        video = ParcelUtils.readString(dest) == "true";
        vote_average = dest.readFloat();
        vote_count = dest.readLong();
    }
}
