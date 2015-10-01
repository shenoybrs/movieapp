package com.mobileteche.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Santosh.Shenoy on 21-09-2015.
 */
public class ReviewModel implements Parcelable {

    public static final Creator<ReviewModel> CREATOR = new Creator<ReviewModel>() {

        public ReviewModel createFromParcel(Parcel parcel) {
            return new ReviewModel(parcel);
        }

        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };

    public String id;
    public String author;
    public String content;
    public String url;
    public ReviewModel() {

    }

    public ReviewModel(Parcel parcel) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        id = ParcelUtils.readString(dest);
        author = ParcelUtils.readString(dest);
        content = ParcelUtils.readString(dest);
        url = ParcelUtils.readString(dest);


    }


    @Override
    public int describeContents() {
        return 0;
    }


}
