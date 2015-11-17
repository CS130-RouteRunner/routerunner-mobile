package com.cs130.routerunner.android.Test;

import android.test.InstrumentationTestCase;

import com.cs130.routerunner.Actors.Actor;
import com.cs130.routerunner.Actors.Truck;

/**
 * Created by Roger on 11/11/2015.
 */
public class ActorTest extends InstrumentationTestCase {

    public void test() {

        Truck truck = new Truck();
        truck.setX(0f);
        truck.setY(0f);
        //should move towards 0,1 at a speed of 0.5, so move delts should be 0,0.5
        truck.setMovementVectorToNextWaypoint(0, 1);
        assertTrue(truck.getMoveXDelta() == 0 && truck.getMoveYDelta() == 0.5f);

        //should move towards 0,0, which it's alrdy at. so move deltas should be 0,0
        truck.setMovementVectorToNextWaypoint(0, 0);
        assertTrue(truck.getMoveXDelta() == 0 && truck.getMoveYDelta() == 0);

        //set move vector here to see if stuff gets added correctly
        truck.setMovementVectorToNextWaypoint(0, 1);
        truck.move();
        assertTrue(truck.getX() == 0 && truck.getY() == 0.5f);
    }
}
