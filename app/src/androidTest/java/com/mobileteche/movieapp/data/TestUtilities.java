package com.mobileteche.movieapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.mobileteche.movieapp.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {
    static final String TEST_MOVIE = "76341";

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default weather values for your database tests.
     */
    static ContentValues createTrailerValues(long movieRowId) {
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, movieRowId);
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY, "eisKxhjBnZ0");
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, "54badb64c3a3684046001c73");
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ISO, "en");
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, "Minions Official Trailer #1 (2015) - Despicable Me Prequel HD");
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_SITE, "YouTube");
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_SIZE, 720);
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_TYPE, "Trailer");


        return trailerValues;
    }

    
    static ContentValues createMovieValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_ADULT, 0);
        testValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, "/tbhdm8UJAb4ViCTsulYFL3lxMCd.jpg");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 76341);
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, "en");
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,"Mad Max: Fury Road");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,
                "An apocalyptic story set in the furthest reaches of our planet, " +
                        "in a stark desert landscape where humanity is broken, " +
                        "and most everyone is crazed fighting for the necessities of life. " +
                        "Within this world exist two rebels on the run who just might be able to restore order. " +
                        "There's Max, a man of action and a man of few words, who seeks peace of mind following the loss " +
                        "of his wife and child in the aftermath of the chaos. And Furiosa, " +
                        "a woman of action and a woman who believes her path to survival may " +
                        "be achieved if she can make it across the " +
                        "desert back to her childhood homeland.release_date: 2015-05-15");
        testValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 47.2187
        );
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2015-05-15");
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE,"Mad Max: Fury Road");
        testValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, 0);
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,7.6);
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, 2283);



        return testValues;
    }

    static long insertMovieValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieValues();

        long locationRowId;
        locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);

        return locationRowId;
    }

    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
