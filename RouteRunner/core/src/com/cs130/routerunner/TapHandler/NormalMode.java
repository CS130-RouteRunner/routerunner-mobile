package com.cs130.routerunner.TapHandler;

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
    }
}
