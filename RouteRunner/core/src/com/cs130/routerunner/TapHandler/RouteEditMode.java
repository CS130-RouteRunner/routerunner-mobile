package com.cs130.routerunner.TapHandler;

import com.cs130.routerunner.Actor;

/**
 * Created by julianyang on 10/22/15.
 */
public class RouteEditMode implements TapMode {
    Actor selectedActor_;
    TapHandler tapHandler_;
    public RouteEditMode(TapHandler tapHandler) {
       this.tapHandler_ = tapHandler;
    }

    public void Tap(float x, float y, int count) {
        // we have entered setting a waypoint mode, so push this waypoint to
        // the actor.

    }
}