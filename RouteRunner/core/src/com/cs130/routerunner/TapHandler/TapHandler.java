package com.cs130.routerunner.TapHandler;

import com.cs130.routerunner.GameMaster;

/**
 * Created by julianyang on 10/22/15.
 */

/**
 * TapHandler basicaly handles all taps by the user depending on what the
 * user has already done. IE if he clicks on a truck (in normal mode), it
 * should go to
 * SelectedActorMode (and display actor info onto the screen).
 * TODO(RogerLau): Document what's already implemented
 *
 */
public class TapHandler {
    GameMaster gameMaster_;
    TapMode routeEditMode_ = new RouteEditMode(this);
    TapMode normalMode_ = new NormalMode(this);
    TapMode actorSelectedMode_ = new ActorSelectedMode(this);
    TapMode routeConfirmMode_ = new RouteConfirmMode(this);

    TapMode curMode_ = normalMode_;

    public TapHandler(GameMaster gameMaster) {
        this.gameMaster_ = gameMaster;
    }

    public void Tap(float x, float y, int count) {
        curMode_.Tap(x, y, count);
    }
}
