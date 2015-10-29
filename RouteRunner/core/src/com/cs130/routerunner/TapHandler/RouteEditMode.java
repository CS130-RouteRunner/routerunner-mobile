package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Actor;
import com.cs130.routerunner.Routes.*;

/**
 * Created by julianyang on 10/22/15.
 */
public class RouteEditMode implements TapMode {
    Actor selectedActor_;
    TapHandler tapHandler_;
    RouteFactory routeFactory_;
    public RouteEditMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
    }
    public void SetSelectedActor(Actor a) {
        selectedActor_ = a;
    }

    public void Tap(float x, float y, int count) {
        Gdx.app.log("RETag", "Inside Route Edit Mode\n");
        // we have entered setting a waypoint mode, so push this waypoint to
        // the actor.
        Vector3 touchPos = new Vector3();
        touchPos.set(x, y, 0);
        tapHandler_.gameMaster_.getCamera().unproject(touchPos);

        // TODO(Julian/Patrick): collect all the points into an array. And
        routeFactory_ = tapHandler_.gameMaster_.getRouteFactory();
        //if we want to edit routes, get old truck route if exists and populate array
        routeFactory_.addWayPoint(touchPos.x, touchPos.y);

        // TODO(Julian/Patrick): change this to a confirm route button or something
        if(tapHandler_.gameMaster_.baseContains(touchPos.x, touchPos.y)){
            //when we have touched the base, then set the route and re-enter normal mode

            //convertToLatLng(); //TODO: need to figure out how to convert to latitude + longitude
            //convertToSnappedPoints(); //TODO: convert points to snapped points
            Route createdRoute = routeFactory_.getRoute(); //pair route with truck
            //routeFactory_.clearRoute(); //clear route for next creation

            tapHandler_.gameMaster_.setRoute(); //change after Roger implements truck routes
            tapHandler_.curMode_ = tapHandler_.normalMode_;
        }
    }
}