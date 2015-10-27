package com.cs130.routerunner.android.Test;

import android.test.InstrumentationTestCase;

import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.CoordinateConverter.LegacyCoordinateConverter;
import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import com.cs130.routerunner.CoordinateConverter.XYPoint;

import java.util.ArrayList;

/**
 * Created by julianyang on 10/25/15.
 */
public class LegacyCoordinateConverterTest extends InstrumentationTestCase {
    private boolean close(LatLngPoint a, LatLngPoint b) {
        // compare two sets of floats
        float lat_actual = Math.abs(a.lat - b.lat);
        float lng_actual = Math.abs(a.lng - b.lng);
        assertTrue(lat_actual < 1);
        assertTrue(lng_actual < 1);
        return true;
    }

    public void test() throws Exception {
        ArrayList<CoordinateTestPoint> data = new
                ArrayList<CoordinateTestPoint> ();
        data.add(new CoordinateTestPoint(3, 39.81447f, -98.564388f, 463, 777));
        data.add(new CoordinateTestPoint(3, 40.609538f, -80.224528f, 568, 771));
        data.add(new CoordinateTestPoint(0, -90, 180, 256, 330));
        data.add(new CoordinateTestPoint(0, -90, -180, 0, 330));
        data.add(new CoordinateTestPoint(0, 90, 180, 256, -74));
        data.add(new CoordinateTestPoint(0, 90, -180, 0, -74));

        data.add(new CoordinateTestPoint(1, -90, 180, 512, 660));
        data.add(new CoordinateTestPoint(1, -90, -180, 0, 660));
        data.add(new CoordinateTestPoint(1, 90, 180, 512, -148));
        data.add(new CoordinateTestPoint(1, 90, -180, 0, -148));

        data.add(new CoordinateTestPoint(2, -90, 180, 1024, 1319));
        data.add(new CoordinateTestPoint(2, -90, -180, 0, 1319));
        data.add(new CoordinateTestPoint(2, 90, 180, 1024, -295));
        data.add(new CoordinateTestPoint(2, 90, -180, 0, -295));

        for (CoordinateTestPoint testPoint : data) {
            XYPoint pixel = LegacyCoordinateConverter.ll2px(
                    testPoint.lat, testPoint.lng, testPoint.zoom);
            assertEquals(pixel.x, testPoint.x);
            assertEquals(pixel.y, testPoint.y);


            assertTrue(close(LegacyCoordinateConverter.px2ll(pixel, testPoint.zoom)
                    , new LatLngPoint(testPoint.lat, testPoint.lng)));
        }
    }
}