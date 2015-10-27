package com.cs130.routerunner.android.Test;

import android.test.InstrumentationTestCase;

import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverter;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverterAdapter;
import com.cs130.routerunner.CoordinateConverter.LatLngPoint;

import java.util.ArrayList;

/**
 * Created by julianyang on 10/26/15.
 */
public class CordinateConverterAdapterTest extends InstrumentationTestCase {
    private final static int pixelTolerance = 5;
    private final static float coordTolerance = 0.001f;
    private boolean close(LatLngPoint a, LatLngPoint b) {
        float lat_actual = Math.abs(a.lat - b.lat);
        float lng_actual = Math.abs(a.lng - b.lng);
        assertTrue(lat_actual < coordTolerance);
        assertTrue(lng_actual < coordTolerance);
        return true;
    }

    public void test() throws Exception {
        // some test points.
        final float worldCenterLat = 34.0536f;
        final float worldCenterLng = -118.44139f;
        final int zoom = 16;
        final int mapWidth = 1529;
        final int mapHeight = 1589;

        CoordinateConverter cc = new CoordinateConverterAdapter
                (worldCenterLat, worldCenterLng, zoom, mapWidth, mapHeight);

        ArrayList<CoordinateTestPoint> data = new
                ArrayList<CoordinateTestPoint>();
        data.add(new CoordinateTestPoint(16, 34.05879f, -118.4439f, 520, 1403));

        for (CoordinateTestPoint testPoint : data) {
            Vector3 pixel = cc.ll2px(testPoint.lat, testPoint.lng);
            String TAG= "PixelTolerance";
            android.util.Log.e(TAG, "outputX: " + pixel.x + " actual: " +
                    testPoint.x);
            android.util.Log.e(TAG, "outputY: " + pixel.y + " actual: " +
                    testPoint.y);

            assertTrue(Math.abs(pixel.x - testPoint.x) < pixelTolerance);
            assertTrue(Math.abs(pixel.y - testPoint.y) < pixelTolerance);

            pixel.x = testPoint.x;
            pixel.y = testPoint.y;

            String TAG2 = "CoordTolerance";
            LatLngPoint output = cc.px2ll(pixel);
            android.util.Log.e(TAG2, "outputLat: " + output.lat + " actual: "
                    + testPoint.lat);
            android.util.Log.e(TAG2, "outputLng: " + output.lng + " actual: "
                    + testPoint.lng);
            assertTrue(close(cc.px2ll(pixel),
                    new LatLngPoint(testPoint.lat, testPoint.lng)));
        }
    }
}
