package com.cs130.routerunner.android.Test;

import android.test.InstrumentationTestCase;

/**
 * Created by julianyang on 10/25/15.
 */
public class ExampleTest extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
    }
}