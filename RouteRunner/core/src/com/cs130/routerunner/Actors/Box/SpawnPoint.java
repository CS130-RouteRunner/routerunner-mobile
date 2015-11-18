package com.cs130.routerunner.Actors.Box;

import com.cs130.routerunner.Actors.Box.Box;
import com.cs130.routerunner.Settings;

/**
 * Created by julianyang on 11/17/15.
 */
public class SpawnPoint extends Box {
    public SpawnPoint(int playerNum) {
        super(Settings.SPAWN_POINT[playerNum][0],
                Settings.SPAWN_POINT[playerNum][1],
                Settings.SPAWN_PNG[playerNum], 500, 500);
    }
}
