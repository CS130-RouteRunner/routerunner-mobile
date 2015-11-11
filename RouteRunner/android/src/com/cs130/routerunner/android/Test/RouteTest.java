package com.cs130.routerunner.android.Test;

import android.test.InstrumentationTestCase;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.cs130.routerunner.Actor;
import com.cs130.routerunner.Routes.Route;

/**
 * Created by Roger on 11/9/2015.
 */
public class RouteTest extends InstrumentationTestCase {
    public void test() throws Exception {
        Route route = new Route();
        assertTrue(route.wayPoints_ != null);
        assertTrue(route.wayPoints_.size() == 0);

        route.addWayPoint(1, 2);
        assertTrue(route.wayPoints_.size() == 1);

        route.addWayPoint(3, 4);
        assertTrue(route.wayPoints_.size() == 2);
        assertTrue(route.wayPoints_.get(0).x == 1);
        assertTrue(route.wayPoints_.get(1).y == 4);

        route.clearWaypoints();
        assertTrue(route.wayPoints_.size() == 0);
    }
}
