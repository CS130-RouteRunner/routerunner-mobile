package com.cs130.routerunner.android.Test;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Actor;
import com.cs130.routerunner.Routes.Route;
import com.cs130.routerunner.TapHandler.TapHandler;

import org.mockito.Mockito;
/**
 * Created by Roger on 11/9/2015.
 */
public class RouteTruckTest extends InstrumentationTestCase {
    public void setUp() throws Exception {
        System.setProperty(
                "dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
    }
    public void test() throws Exception {

        Route route = new Route();
        Actor truck = Mockito.mock(Actor.class);

        Mockito.when(truck.hasStartedNewRoute()).thenReturn(false);
        Mockito.when(truck.getX()).thenReturn(5f);
        Mockito.when(truck.getY()).thenReturn(5f);

        //should return false because no waypoints yet
        assertTrue(route.updateWaypoint(truck) == false);

        route.addWayPoint(5, 5);
        route.addWayPoint(10, 10);
        route.addWayPoint(15, 15);

        //should return true because will hit the first waypoint
        //index should be one because since we're "at" the first waypt, index increments by 1
        assertTrue(route.updateWaypoint(truck));
        assertTrue(route.getCurrWayPointIndex() == 1);

        Mockito.when(truck.getX()).thenReturn(10f);
        Mockito.when(truck.getY()).thenReturn(10f);

        //should return true because will hit the second waypoint
        //index should be two because since we're "at" the first waypt, index increments by 1
        assertTrue(route.updateWaypoint(truck));
        assertTrue(route.getCurrWayPointIndex() == 2);

        Mockito.when(truck.getX()).thenReturn(15f);
        Mockito.when(truck.getY()).thenReturn(15f);

        //should return true because will hit the third/last waypoint
        //index should be zero because it resets the index after hitting last waypoint
        assertTrue(route.updateWaypoint(truck));
        assertTrue(route.getCurrWayPointIndex() == 0);

        Mockito.when(truck.getX()).thenReturn(-1f);
        Mockito.when(truck.getY()).thenReturn(-1f);

        //should return true because even though no waypoints hit, the truck has not started moving yet
        //index should be zero because the route has just been "initialized"
        assertTrue(route.updateWaypoint(truck));
        assertTrue(route.getCurrWayPointIndex() == 0);
    }
}