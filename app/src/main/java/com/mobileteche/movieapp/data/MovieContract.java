package com.mobileteche.movieapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Santosh.Shenoy on 09-09-2015.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.mobileteche.movieapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_TRAILER = "trailer";



    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_BACKDROP_PATH =  "backdrop_path";
        public static final String COLUMN_MOVIE_ID =  "id";
        public static final String COLUMN_ORIGINAL_LANGUAGE =  "original_language";
        public static final String COLUMN_ORIGINAL_TITLE =  "original_title";
        public static final String COLUMN_OVERVIEW =  "overview";
        public static final String COLUMN_RELEASE_DATE =  "release_date";
        public static final String COLUMN_POSTER_PATH =  "poster_path";
        public static final String COLUMN_POPULARITY =  "popularity";
        public static final String COLUMN_TITLE =  "title";
        public static final String COLUMN_VIDEO =  "video";
        public static final String COLUMN_VOTE_AVERAGE =  "vote_average";
        public static final String COLUMN_VOTE_COUNT=  "vote_count";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {return uri.getLastPathSegment();
        }
    }
    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String TABLE_NAME = "review";
        public static final String COLUMN_MOVIE_ID =  "movie_id";
        public static final String COLUMN_REVIEW_ID =  "id";
        public static final String COLUMN_REVIEW_AUTHOR =  "author";
        public static final String COLUMN_REVIEW_CONTENT =  "content";
        public static final String COLUMN_REVIEW_URL =  "url";
        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        public static Uri buildReviewMovie(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }
    }
    public static final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_MOVIE_ID =  "movie_id";
        public static final String COLUMN_TRAILER_ID =  "id";
        public static final String COLUMN_TRAILER_ISO =  "iso_639_1";
        public static final String COLUMN_TRAILER_KEY =  "key";
        public static final String COLUMN_TRAILER_NAME =  "name";
        public static final String COLUMN_TRAILER_SITE =  "site";
        public static final String COLUMN_TRAILER_SIZE =  "size";
        public static final String COLUMN_TRAILER_TYPE =  "type";
        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildTrailerMovie(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}
