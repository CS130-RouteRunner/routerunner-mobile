package com.cs130.routerunner.Routes;

import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Actor;

import java.util.ArrayList;

/**
 * Created by Roger on 10/23/2015.
 */
public class RouteFactory {
    public ArrayList<Vector3> wayPoints;

    public void startCreatingRoute(){
        wayPoints = new ArrayList<Vector3>();
    }

    public void addWayPoint(float x, float y){
        Vector3 v = new Vector3(x, y, 0f);
        wayPoints.add(v);
    }

    public Route getRoute(){
        Route r = new Route();
        r.setWayPoints(wayPoints);
        return r;
    }

    public void clearRoute(){
        wayPoints.clear();
    }
}
