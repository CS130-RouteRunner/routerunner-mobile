package com.cs130.routerunner.TapHandler;

import com.cs130.routerunner.Routes.*;

/**
 * Created by julianyang on 10/22/15.
 */
public class NormalMode implements TapMode {
    TapHandler tapHandler_;

    public NormalMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
    }
    public void Tap(float x, float y, int count) {
        // check if the tap collides with any buttons
        // check if the tap collides with any actors
        // else do nothing

        //temporarily all we do is when there is a tap, we start creating route and enter route creation mode
        RouteFactory routeFactory = tapHandler_.gameMaster_.getRouteFactory();
        routeFactory.startCreatingRoute();

        tapHandler_.curMode_ = tapHandler_.routeEditMode_;
    }
}
