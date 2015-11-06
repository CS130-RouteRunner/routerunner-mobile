package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.Gdx;
import com.cs130.routerunner.Actor;
import com.cs130.routerunner.Routes.Route;

/**
 * Created by julianyang on 11/5/15.
 */
public class RouteConfirmMode implements TapMode {
    TapHandler tapHandler_;
    Actor selectedActor_;
    Route snappedRoute_;

    public RouteConfirmMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
    }

    public void Init() {}
    public void SetRoute(Route r) {
        this.snappedRoute_ = r;
    }
    public void SetSelectedActor(Actor a) { selectedActor_ = a; }

    public void Tap(float x, float y, int count) {
        Gdx.app.log("RCTag", "Inside Route Confirm Mode");
        if(selectedActor_.isSaveRoute()) {
            Gdx.app.log("RCTag", "Save");
            // TODO(julian): see if we keep creating Route objects will cause
            // game to slow down.
            selectedActor_.setRoute(snappedRoute_);
        } else if (selectedActor_.isCancelSave()) {
            // do cleanup if needed
        }

        selectedActor_.setPaused(false);
        Gdx.app.log("RCTag", "unpaused truck");
        tapHandler_.curMode_ = tapHandler_.normalMode_;
        tapHandler_.gameMaster_.clearWaypoints();

    }
}

