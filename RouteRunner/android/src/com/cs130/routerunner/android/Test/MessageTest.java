package com.cs130.routerunner.android.Test;

import android.test.InstrumentationTestCase;

import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import com.cs130.routerunner.Message;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roger on 11/13/15.
 */
public class MessageTest extends InstrumentationTestCase {
    public void checkPurchaseMessage() throws Exception {
        String expecteduuid = "cs130";

        // Check that a 'Purchase' message is constructed correctly
        JSONObject expectedPurchase = new JSONObject();
        String expectedPurchaseKey = "item";
        String expectedPurchaseValue = "truck";
        String expectedPurchaseType = "purchase";
        JSONObject payload = new JSONObject();
        payload.put(expectedPurchaseKey, expectedPurchaseValue);
        expectedPurchase.put("type", expectedPurchaseType);
        expectedPurchase.put("uid", expecteduuid);
        expectedPurchase.put("data", payload);
        Message resultPurchase = new Message(expectedPurchase);
        assertEquals(expectedPurchaseType, resultPurchase.getType());
        assertEquals(expecteduuid, resultPurchase.getUid());
        assertEquals(expectedPurchaseValue, resultPurchase.getItem());
        assertNull(resultPurchase.getCoords());
        String expectedPurchaseString = "uid:" + expecteduuid + ";type:" + expectedPurchaseType +
                ";data:" + "{" + expectedPurchaseKey + ":" + expectedPurchaseValue + "}";
        assertEquals(expectedPurchaseString, resultPurchase.toString());
    }

    public void checkRouteMessage() throws Exception {
        String expecteduuid = "cs130";

        // Check that a 'Route' message is constructed correctly
        JSONObject expectedRoute = new JSONObject();
        String expectedRouteKey = "coords";
        String point1 = "1.2345,2.3456";
        String point2 = "3.4567,4.5678";
        String point3 = "5.6789,6.789";
        String expectedRouteString = point1 + ";" + point2 + ";" + point3;
        List<LatLngPoint> expectedList = new ArrayList<LatLngPoint>();
        expectedList.add(new LatLngPoint(point1));
        expectedList.add(new LatLngPoint(point2));
        expectedList.add(new LatLngPoint(point3));
        String expectedRouteType = "route";
        JSONObject payload = new JSONObject();
        payload.put(expectedRouteKey, expectedRouteString);
        expectedRoute.put("type", expectedRouteType);
        expectedRoute.put("uid", expecteduuid);
        expectedRoute.put("data", payload);
        Message resultRoute = new Message(expectedRoute);
        assertEquals(expectedRouteType, resultRoute.getType());
        assertEquals(expecteduuid, resultRoute.getUid());
        assertNull(resultRoute.getItem());
        List<LatLngPoint> routeList = resultRoute.getCoords();
        assertEquals(expectedList.size(), routeList.size());
        for (int i = 0; i < routeList.size(); i++) {
            assertEquals(expectedList.get(i).toString(), routeList.get(i).toString());
        }
        String expectedString = "uid:" + expecteduuid + ";type:" + expectedRouteType +
                ";data:" + "{" + expectedRouteKey + ":" + expectedRouteString + "}";
        assertEquals(expectedString, resultRoute.toString());
    }

    public void test() throws Exception {
        checkPurchaseMessage();
        checkRouteMessage();
    }
}
