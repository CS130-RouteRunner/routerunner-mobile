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
    TapMode routeEditMode_;
    TapMode normalMode_;
    TapMode actorSelectedMode_;
    TapMode routeConfirmMode_;
    TapMode missileMode_;
    TapMode curMode_;

    public TapHandler(GameMaster gameMaster) {
        this.gameMaster_ = gameMaster;
        routeEditMode_ = new RouteEditMode(this, this.gameMaster_.snapToRoads_);
        normalMode_ = new NormalMode(this);
        actorSelectedMode_ = new ActorSelectedMode(this);
        routeConfirmMode_ = new RouteConfirmMode(this);
        missileMode_ = new MissileMode(this);
        curMode_ = normalMode_;
    }

    public void Tap(float x, float y, int count) {
        curMode_.Tap(x, y, count);
    }

    public GameMaster getGameMaster(){
        return gameMaster_;
    }
}
