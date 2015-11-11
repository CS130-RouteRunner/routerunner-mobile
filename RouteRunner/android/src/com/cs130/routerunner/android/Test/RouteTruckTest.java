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
public class RouteTruckTest extends AndroidTestCase {
    public void test() throws Exception {

//        Route route = new Route();
          //Actor truck = PowerMock.createMock(Actor.class);
        Actor truck = Mockito.mock(Actor.class);

//        Mockito.when(truck.getX()).thenReturn(5f);
//        Mockito.when(truck.getY()).thenReturn(5f);
//
//        assertTrue(route.updateWaypoint(truck) == false);
//
//        route.addWayPoint(5, 5);
//        route.addWayPoint(10, 10);
//        route.updateWaypoint(truck);
//
//        assertTrue(route.getCurrWayPointIndex() == 1);

    }
}