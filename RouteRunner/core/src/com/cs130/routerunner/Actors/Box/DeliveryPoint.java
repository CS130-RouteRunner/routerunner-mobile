package com.cs130.routerunner.Actors.Box;

import com.cs130.routerunner.Actors.Box.Box;
import com.cs130.routerunner.Settings;

/**
 * Created by julianyang on 11/17/15.
 */
public class DeliveryPoint extends Box {
    public DeliveryPoint(int playerNum) {

        super(Settings.DELIVERY_POINT[playerNum][0],
                Settings.DELIVERY_POINT[playerNum][1],
                Settings.DELIVERY_PNG[playerNum], 500, 500);
    }
}
