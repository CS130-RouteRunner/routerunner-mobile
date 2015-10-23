package com.cs130.routerunner.TapHandler;

import com.cs130.routerunner.GameMaster;

/**
 * Created by julianyang on 10/22/15.
 */
public class TapHandler {
    GameMaster gameMaster_;
    TapMode routeEditMode_ = new RouteEditMode(this);
    TapMode normalMode_ = new NormalMode(this);
    TapMode curMode_ = normalMode_;

    public TapHandler(GameMaster gameMaster) {
        this.gameMaster_ = gameMaster;
    }

    public void Tap(float x, float y, int count) {
        curMode_.Tap(x, y, count);
    }
}
