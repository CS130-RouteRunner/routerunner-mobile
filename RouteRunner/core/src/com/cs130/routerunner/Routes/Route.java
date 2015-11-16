package com.cs130.routerunner.Routes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Actor;
import com.cs130.routerunner.GameMaster;
import com.cs130.routerunner.Settings;
import com.cs130.routerunner.Player;
import com.cs130.routerunner.TapHandler.TapHandler;

import java.util.ArrayList;

/**
 * Created by Roger on 10/22/2015.
 */
public class Route {
    //member variables
    public ArrayList<Vector3> wayPoints_;
    private int currWayPointIndex_;

    //constructor
    public Route(){
        currWayPointIndex_ = 0;
        wayPoints_ = new ArrayList<Vector3>();
    }

    //methods
    //updates what the given truck's next way point should be
    //if so, then update to the next waypoint/reset/initialize appropriately
    public boolean updateWaypoint(Actor truck){
        if (wayPoints_ == null || wayPoints_.size() == 0) {
//            Gdx.app.debug("Route", "waypoints is null or empty");
            return false;
        }
//        Gdx.app.debug("Route", "Cur waypoint index is: " + currWayPointIndex_);
        Vector3 currWayPoint = wayPoints_.get(currWayPointIndex_);

        if (Math.abs(truck.getX() - currWayPoint.x) < Settings.EPSILON && Math.abs(truck.getY() - currWayPoint.y) < Settings.EPSILON && currWayPointIndex_ == wayPoints_.size()-1){
            //WE JUST FINISHED THE WHOLE ROUTE, RESET
            currWayPointIndex_ = 0;
            truck.setX(0f);
            truck.setY(0f);
            truck.setMovementVectorToNextWaypoint(wayPoints_.get(currWayPointIndex_).x, wayPoints_.get(currWayPointIndex_).y);
            return true;
        }
        else if (Math.abs(truck.getX()- currWayPoint.x) < Settings.EPSILON && Math.abs(truck.getY() - currWayPoint.y) < Settings.EPSILON && currWayPointIndex_ < (wayPoints_.size()-1)) {
            //WE JUST GOT TO A WAYPT, NOW SET NEXT ONE
            currWayPointIndex_++;
            currWayPoint = wayPoints_.get(currWayPointIndex_);
            truck.setMovementVectorToNextWaypoint(currWayPoint.x, currWayPoint.y);
            //Gdx.app.log("RETag", "HIT WAYPOINT, MOVING ON!");
            //Add money to player
            TapHandler h = truck.getTapHandler();
            GameMaster m = h.getGameMaster();
            Player l = m.getLocalPlayer();
            l.addMoney(truck.getAmount());
            return true;
        }
        else if (!truck.hasStartedNewRoute()){
            //WE HAVENT STARTED MOVING YET, set the first waypoint
            truck.setMovementVectorToNextWaypoint(currWayPoint.x, currWayPoint.y);
            return true;
        }
        return false;
    }

    public void updateSelf(Route newRoute) {
        //create a copy of the vector of waypoints and reset the index.
        wayPoints_ = new ArrayList<Vector3>(newRoute.wayPoints_);
        currWayPointIndex_ = 0;
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
    public int getCurrWayPointIndex(){
        return this.currWayPointIndex_;
    }
}
