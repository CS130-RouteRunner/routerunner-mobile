package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Actor;
import com.cs130.routerunner.Routes.*;

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
        Vector3 touchPos = new Vector3();
        touchPos.set(x, y, 0);
        tapHandler_.gameMaster_.unproject(touchPos);

        RouteFactory routeFactory = tapHandler_.gameMaster_.getRouteFactory();

        routeFactory.addWayPoint(touchPos.x, touchPos.y);
        if(tapHandler_.gameMaster_.baseContains(touchPos.x, touchPos.y)){
            //when we have touched the base, then set the route and re-enter normal mode
            tapHandler_.gameMaster_.setRoute();
            tapHandler_.curMode_ = tapHandler_.normalMode_;
        }
    }
}