/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobileteche.movieapp.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;


public class TestUriMatcher extends AndroidTestCase {
    private static final String MOVIEID = "76341";


    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_TRAILER_MOVIE_DIR = MovieContract.TrailerEntry.buildTrailerMovie(MOVIEID);
    private static final Uri TEST_REVIEW_MOVIE_DIR = MovieContract.ReviewEntry.buildReviewMovie(MOVIEID);

    private static final Uri TEST_TRAILER_DIR = MovieContract.TrailerEntry.CONTENT_URI;
    private static final Uri TEST_REVIEW_DIR = MovieContract.ReviewEntry.CONTENT_URI;


public void testUriMatcher() {
UriMatcher testMatcher = MovieContentProvider.buildUriMatcher();

assertEquals("Error: The WEATHER URI was matched incorrectly.",
testMatcher.match(TEST_MOVIE_DIR), MovieContentProvider.MOVIE);
assertEquals("Error: The WEATHER WITH LOCATION URI was matched incorrectly.",
testMatcher.match(TEST_TRAILER_MOVIE_DIR), MovieContentProvider.TRAILER_WITH_MOVIE);
assertEquals("Error: The WEATHER WITH LOCATION AND DATE URI was matched incorrectly.",
testMatcher.match(TEST_REVIEW_MOVIE_DIR), MovieContentProvider.REVIEW_WITH_MOVIE);
assertEquals("Error: The LOCATION URI was matched incorrectly.",
testMatcher.match(TEST_TRAILER_DIR), MovieContentProvider.TRAILER);
}
}
