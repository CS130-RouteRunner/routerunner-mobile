package com.cs130.routerunner.Actors.Box;

import com.cs130.routerunner.Settings;

/**
 * Created by julianyang on 11/17/15.
 */
public class ConcreteBoxFactory extends BoxFactory {
     public Box createBox(BoxType type, int playerNum) {
        // switch statement to create boxes.
        Box newBox;
        switch(type) {
            case DeliveryPoint:
                newBox = new DeliveryPoint(
                        Settings.DELIVERY_POINT[playerNum][0],
                        Settings.DELIVERY_POINT[playerNum][1]);
                break;
            case SpawnPoint:
                newBox = new SpawnPoint(
                        Settings.SPAWN_POINT[playerNum][0],
                        Settings.SPAWN_POINT[playerNum][1]);
                break;
            default:
                newBox = new SpawnPoint(
                        Settings.SPAWN_POINT[playerNum][0],
                        Settings.SPAWN_POINT[playerNum][1]);
        }
        return newBox;
    }
}
