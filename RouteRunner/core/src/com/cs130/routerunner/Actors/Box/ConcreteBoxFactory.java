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
                newBox = new DeliveryPoint(playerNum);
                break;
            case SpawnPoint:
                newBox = new SpawnPoint(playerNum);
                break;
            default:
                newBox = new SpawnPoint(playerNum);
        }
        return newBox;
    }
}
