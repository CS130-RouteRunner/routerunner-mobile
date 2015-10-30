package com.cs130.routerunner.Routes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Actor;

import java.util.ArrayList;

/**
 * Created by Roger on 10/22/2015.
 */
public class Route {
    public ArrayList<Vector3> wayPoints_;
    public Actor truck_;
    private int currWayPointIndex_;

    public Route(){
        currWayPointIndex_ = 0;
        wayPoints_ = new ArrayList<Vector3>();
    }
    public void updateTruckPosition(Actor truck){
        if (wayPoints_ == null || wayPoints_.size() == 0)
            return;

        Vector3 currWayPoint = wayPoints_.get(currWayPointIndex_);
        //if reached waypoint, then update waypoint
        if (truck.getX() == currWayPoint.x && truck.getY() == currWayPoint.y && currWayPointIndex_ < (wayPoints_.size()-1)) {
            //WE JUST GOT TO A WAYPT, NOW SET NEXT ONE
            currWayPointIndex_++;
            currWayPoint = wayPoints_.get(currWayPointIndex_);
            truck.setMoveTo(currWayPoint.x, currWayPoint.y);
        }
        else if (truck.getX() == currWayPoint.x && truck.getY() == currWayPoint.y){
            //WE JUST FINISHED THE WHOLE ROUTE, RESET
            currWayPointIndex_ = 0;
            truck.setX(0f);
            truck.setY(0f);
            truck.setMoveTo(wayPoints_.get(currWayPointIndex_).x, wayPoints_.get(currWayPointIndex_).y);
        }
        else{
            truck.setMoveTo(currWayPoint.x, currWayPoint.y);
        }

        truck.update();
    }

    public void addWayPoint(float x, float y){
        Vector3 v = new Vector3(x, y, 0f);
        wayPoints_.add(v);
    }

    public void clearWaypoints() {
        wayPoints_.clear();
        currWayPointIndex_ = 0;
    }

    public void printWaypoints() {
        Gdx.app.log("RETag", ">>>>waypoints:");
        for (Vector3 v : wayPoints_) {
            Gdx.app.log("RETag", v.x + ", " + v.y);
        }
        Gdx.app.log("RETag", "<<<<end waypoints");
    }
    public void setWayPoints(ArrayList<Vector3> wayPoints){
        this.wayPoints_ = wayPoints;

    }
}
