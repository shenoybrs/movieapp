package com.mobileteche.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Santosh.Shenoy on 21-09-2015.
 */
public class TrailerModel implements Parcelable {

    public static final Parcelable.Creator<TrailerModel> CREATOR = new Parcelable.Creator<TrailerModel>() {

        public TrailerModel createFromParcel(Parcel parcel) {
            return new TrailerModel(parcel);
        }

        public TrailerModel[] newArray(int size) {
            return new TrailerModel[size];
        }
    };

    public String id;
    public String iso_639_1;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;

    public TrailerModel() {

    }

    public TrailerModel(Parcel parcel) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        id = ParcelUtils.readString(dest);
        iso_639_1 = ParcelUtils.readString(dest);
        key = ParcelUtils.readString(dest);
        name = ParcelUtils.readString(dest);
        site = ParcelUtils.readString(dest);
        size = dest.readInt();
        type =ParcelUtils.readString(dest);

    }


    @Override
    public int describeContents() {
        return 0;
    }


}
