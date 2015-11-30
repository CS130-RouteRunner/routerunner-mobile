package com.cs130.routerunner.android.Test;

import android.test.InstrumentationTestCase;

import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import com.cs130.routerunner.Message;
import com.cs130.routerunner.android.PubnubHelper;
import com.pubnub.api.Pubnub;

import org.json.JSONObject;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roger on 11/12/15.
 */
public class PubnubHelperTest extends InstrumentationTestCase {
    public void setUp() throws Exception {
        System.setProperty(
                "dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    public void test() throws Exception {
        Pubnub mockPubnub = Mockito.mock(Pubnub.class);
        String expecteduuid = "cs130";
        String expectedChannel = "routerunner";
        PubnubHelper pubnubHelper = new PubnubHelper(expecteduuid, expectedChannel);

        // Check that PubnubHelper has been instantiated correctly
        assertEquals(expecteduuid, pubnubHelper.getUUID());
        assertEquals(expectedChannel, pubnubHelper.getChannel());

        // Check that a 'Purchase' message is created successfully
        JSONObject expectedPurchase = new JSONObject();
        String expectedPurchaseKey = "item";
        String expectedPurchaseValue = "truck";
        String expectedPurchaseType = "purchase";
        expectedPurchase.put(expectedPurchaseKey, expectedPurchaseValue);
        expectedPurchase.put("id", 1);
        Message purchaseMessage = pubnubHelper.createPurchaseMessage(expecteduuid, expectedPurchase);
        assertEquals(expectedPurchaseType, purchaseMessage.getType());
        assertEquals(expecteduuid, purchaseMessage.getUid());
        assertEquals(expectedPurchaseValue, purchaseMessage.getItem());
        assertNull(purchaseMessage.getCoords());

        // Check that a 'Route' message is created successfully
        JSONObject expectedRoute = new JSONObject();
        String expectedRouteKey = "coords";
        String point1 = "1.2345,2.3456";
        String point2 = "3.4567,4.5678";
        String point3 = "5.6789,6.7890";
        String expectedRouteString = point1 + ";" + point2 + ";" + point3;
        List<LatLngPoint> expectedRouteList = new ArrayList<LatLngPoint>();
        expectedRouteList.add(new LatLngPoint(point1));
        expectedRouteList.add(new LatLngPoint(point2));
        expectedRouteList.add(new LatLngPoint(point3));
        String expectedRouteType = "route";
        expectedRoute.put(expectedRouteKey, expectedRouteString);
        expectedRoute.put("id", 1);
        Message routeMessage = pubnubHelper.createRouteMessage(expecteduuid, expectedRoute);
        assertEquals(expectedRouteType, routeMessage.getType());
        assertEquals(expecteduuid, routeMessage.getUid());
        assertNull(routeMessage.getItem());
        List<LatLngPoint> resultRoute = routeMessage.getCoords();
        assertEquals(expectedRouteList.size(), resultRoute.size());
        for (int i = 0; i < expectedRouteList.size(); i++) {
            assertEquals(expectedRouteList.get(i).toString(), resultRoute.get(i).toString());
        }

        // TODO: Finish PubnubHelper tests
    }
}
