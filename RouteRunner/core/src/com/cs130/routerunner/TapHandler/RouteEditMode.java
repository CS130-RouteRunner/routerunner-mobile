package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Actors.Actor;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverter;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverterAdapter;
import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import com.cs130.routerunner.Routes.*;
import com.cs130.routerunner.Settings;
import com.cs130.routerunner.SnapToRoads.SnapToRoads;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julianyang on 10/22/15.
 */
public class RouteEditMode implements TapMode {
    Actor selectedActor_;
    TapHandler tapHandler_;
    Route newRoute_;
    SnapToRoads snapToRoads_;
    ArrayList<Vector3> oldPartialWaypoints_;
    int oldPartialWaypointIndex;

    public RouteEditMode(TapHandler tapHandler, SnapToRoads snapToRoads) {
        this.tapHandler_ = tapHandler;
        newRoute_ = new Route();
        snapToRoads_ = snapToRoads;
    }
    public void Init() {
        newRoute_ = new Route();
    }
    public void SetSelectedActor(Actor a) {
        selectedActor_ = a;
    }
    public void SetRoute(Route r) {
    }

    public void Tap(float x, float y, int count) {
        Gdx.app.log("RETag", "Inside Route Edit Mode\n");
        // we have entered setting a waypoint mode, so push this waypoint to
        // the actor.

        //fill the route with all the waypoints the truck has already passed
        if (newRoute_.wayPoints_.size() == 0) {
            oldPartialWaypoints_ = new ArrayList<Vector3>();
            if (selectedActor_.route_.getCurrWayPointIndex() > 0) {
                for (int i = 0; i < selectedActor_.route_.getCurrWayPointIndex(); i++) {
                    oldPartialWaypoints_.add(selectedActor_.route_.wayPoints_.get(i));
                }
            }
            //make the curr waypoint be the truck's current position
            Vector3 touchPos = new Vector3();
            touchPos.set(selectedActor_.getX(), selectedActor_.getY(), 0);
            newRoute_.addWayPoint(selectedActor_.getX(), selectedActor_.getY());
            tapHandler_.gameMaster_.addWaypoint(touchPos);
        }

        Gdx.app.log("RETag", "saveRoute is " + selectedActor_.isSnapRoute());
        if(selectedActor_.isSnapRoute()){
            //when we have touched the base, then set the route and re-enter normal mode

            Gdx.app.debug("RETag", "old route: ");
            selectedActor_.route_.printWaypoints();

            // Run our input points through Google Snap to Roads
            List<Vector3> convertedPoints = snapToRoads_.snapPoints
                    (newRoute_.wayPoints_);
            for (Vector3 point: convertedPoints) {
                Gdx.app.debug("CP", point.x + " " + point.y);
            }

            newRoute_.wayPoints_.addAll(0, oldPartialWaypoints_);
            newRoute_.setCurrWayPointIndex(selectedActor_.route_.getCurrWayPointIndex());

            Gdx.app.debug("RETag", "new route: ");
            newRoute_.printWaypoints();
            // update the waypoint sprites
            tapHandler_.gameMaster_.setWaypoints(newRoute_.wayPoints_);
            // switch to Route Confirm Mode
            tapHandler_.curMode_ = tapHandler_.routeConfirmMode_;
            tapHandler_.routeConfirmMode_.SetSelectedActor(selectedActor_);
            tapHandler_.routeConfirmMode_.SetRoute(newRoute_);
        }else {
            Vector3 touchPos = new Vector3();
            touchPos.set(x, y, 0);

            Vector3 previous = new Vector3();
            if(!newRoute_.wayPoints_.isEmpty())
                previous = newRoute_.wayPoints_.get(newRoute_.wayPoints_.size() - 1);
            else
                previous = new Vector3(selectedActor_.getX(), selectedActor_.getY(), 0);
            double distance = Math.sqrt(Math.pow(x-previous.x,2)+Math.pow(y-previous.y,2));
            //check if the point is inside the range
            if(distance > Settings.NEXT_POINT_RADIUS){
                Gdx.app.log("ReTag", "Tap outside valid range: " + touchPos.x + ", " +
                        touchPos.y);
                Gdx.app.log("ReTag", "Radius: "+Settings.NEXT_POINT_RADIUS);
                Gdx.app.log("ReTag", "Distance: "+distance);
                selectedActor_.showAlert("Please choose a point inside the green range");
                return;
            }

            //if we want to edit routes, get old truck route if exists and populate array
            Gdx.app.log("ReTag", "Received tap at: " + touchPos.x + ", " +
                    touchPos.y);
            newRoute_.addWayPoint(touchPos.x, touchPos.y);
            tapHandler_.gameMaster_.addWaypoint(touchPos);
        }
    }
}