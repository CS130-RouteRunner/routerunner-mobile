package com.cs130.routerunner.android.Test;
import android.test.InstrumentationTestCase;
import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import com.cs130.routerunner.SnapToRoads.SnapToRoads;
import java.util.ArrayList;
import android.util.Log;

/**
 * Created by christinayang on 10/26/15.
 */
public class SnapToRoadsTest extends InstrumentationTestCase {
    public void test() {

        ArrayList<LatLngPoint> TestPoints = new ArrayList<LatLngPoint>();
        TestPoints.add(new LatLngPoint(60.170880f,24.942795f));
        TestPoints.add(new LatLngPoint(60.170879f,24.942796f));
        TestPoints.add(new LatLngPoint(60.170877f,24.942796f));

        SnapToRoads SR = new SnapToRoads();
        ArrayList<LatLngPoint> SnappedTestPoints = SR.GetSnappedPoints(TestPoints);

        float LatDiff0 = Math.abs(SnappedTestPoints.get(0).lat - 60.17087791867259f);
        float LngDiff0 = Math.abs(SnappedTestPoints.get(0).lng - 24.94269982192242f);
        float LatDiff1 = Math.abs(SnappedTestPoints.get(1).lat - 60.170876898776406f);
        float LngDiff1 = Math.abs(SnappedTestPoints.get(1).lng - 24.942699912064775f);
        float LatDiff2 = Math.abs(SnappedTestPoints.get(2).lat - 60.170874902634374f);
        float LngDiff3 = Math.abs(SnappedTestPoints.get(2).lng - 24.942700088491474f);

        assertTrue(LatDiff0 < 0.0001);
        assertTrue(LngDiff0 < 0.0001);
        assertTrue(LatDiff1 < 0.0001);
        assertTrue(LngDiff1 < 0.0001);
        assertTrue(LatDiff2 < 0.0001);
        assertTrue(LngDiff3 < 0.0001);
    }
}
