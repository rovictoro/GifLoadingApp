package com.rovictoro.giphy.utils;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

public class EspressoTestingIdlingResource {

    /**
     * A counter for network delay testing purpose
     */

        private static final String RESOURCE = "GLOBAL";

        private static CountingIdlingResource mCountingIdlingResource =
                new CountingIdlingResource(RESOURCE);

        public static void increment() {
            mCountingIdlingResource.increment();
        }

        public static void decrement() {
            mCountingIdlingResource.decrement();
        }

        public static IdlingResource getIdlingResource() {
            return mCountingIdlingResource;
        }



}
