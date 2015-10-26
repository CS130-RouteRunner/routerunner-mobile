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
        // TODO(Kailin): if tap collides with the truck, switch to
        // SelectedActorMode (note that we also need to implement that too)
        // 1) SelectedActor should change state to RouteEditMode when the user
        // clicks on SelectedActorMode
        // 2) Before changing to SelectedActor, we need to display an
        // EditRoute Button.
        // else do nothing

        //temporarily all we do is when there is a tap, we start creating route and enter route creation mode
        RouteFactory routeFactory = tapHandler_.gameMaster_.getRouteFactory();
        routeFactory.startCreatingRoute();

        tapHandler_.curMode_ = tapHandler_.routeEditMode_;
    }
}
