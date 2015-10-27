package com.cs130.routerunner.TapHandler;

import com.cs130.routerunner.Actor;
import com.cs130.routerunner.Routes.*;

/**
 * Created by julianyang on 10/22/15.
 */
public class NormalMode implements TapMode {
    TapHandler tapHandler_;

    public NormalMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
    }
    public void SetSelectedActor(Actor a) {
        // do nothing, normal mode doesn't have a selected actor
    }

    public void Tap(float x, float y, int count) {
        // check if the tap collides with any buttons
        // check if the tap collides with any actors
        // SelectedActorMode

        // 1) SelectedActor should change state to RouteEditMode when the user
        // clicks on SelectedActorMode
        // 2) Before changing to SelectedActor, we need to display an
        // EditRoute Button.
        // else do nothing

        // should probably just loop on all actors in the game or something?
        if (/** tapped on truck **/ false) {
            // temporarily placeholder for truck
            //tapHandler_.actorSelectedMode_.SetSelectedActor(actorSelected);
            //display Edit Route button
            tapHandler_.curMode_ = tapHandler_.actorSelectedMode_;
        } else {
            // do nothing for now.
            // TODO():need to implement other buttons (ie buy truck) later
        }

        // We will need to refactor this somewhere else later.
        // TODO(Roger Lau): change route / actor relationship so that actor
        // owns a route.
        //temporarily all we do is when there is a tap, we start creating route and enter route creation mode
        RouteFactory routeFactory = tapHandler_.gameMaster_.getRouteFactory();
        routeFactory.startCreatingRoute();

        tapHandler_.curMode_ = tapHandler_.routeEditMode_;
    }
}
