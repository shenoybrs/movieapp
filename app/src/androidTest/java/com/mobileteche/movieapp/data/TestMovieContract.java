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

import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Students: This is NOT a complete test for the MovieContract --- just for the functions
    that we expect you to write.
 */
public class TestMovieContract extends AndroidTestCase {

    private static final int TEST_MOVIE_ID  = 1234;

    /*
        Students: Uncomment this out to test your Movie location function.
     */
public void testBuildMovieLocation() {
Uri locationUri = MovieContract.MovieEntry.buildMovieUri(TEST_MOVIE_ID);
    assertNotNull("Error: Null Uri returned.  You must fill-in buildMovieLocation in " +
"MovieContract.",
locationUri);
assertEquals("Error: Movie location not properly appended to the end of the Uri",
        String.valueOf(TEST_MOVIE_ID), locationUri.getLastPathSegment());
assertEquals("Error: Movie location Uri doesn't match our expected result",
locationUri.toString(),
"content://com.mobileteche.movieapp/movie/1234");
}
}
