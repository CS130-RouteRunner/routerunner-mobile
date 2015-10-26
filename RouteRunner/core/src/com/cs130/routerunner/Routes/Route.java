package com.cs130.routerunner.Routes;

import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Actor;

import java.util.ArrayList;

/**
 * Created by Roger on 10/22/2015.
 */
public class Route {
    public ArrayList<Vector3> wayPoints;
    public Actor truck;
    private int currWayPointIndex;

    public Route(Actor t){
        truck = t;
        currWayPointIndex = 0;
    }
    public void updateTruckPosition(){
        if (wayPoints == null || wayPoints.size() == 0)
            return;

        Vector3 currWayPoint = wayPoints.get(currWayPointIndex);
        //if reached waypoint, then update waypoint
        if (truck.getX() == currWayPoint.x && truck.getY() == currWayPoint.y && currWayPointIndex < (wayPoints.size()-1)) {
            //WE JUST GOT TO A WAYPT, NOW SET NEXT ONE
            currWayPointIndex++;
            currWayPoint = wayPoints.get(currWayPointIndex);
            truck.setMoveTo(currWayPoint.x, currWayPoint.y);
        }
        else if (truck.getX() == currWayPoint.x && truck.getY() == currWayPoint.y){
            //WE JUST FINISHED THE WHOLE ROUTE, RESET
            currWayPointIndex = 0;
            truck.setX(0f);
            truck.setY(0f);
            truck.setMoveTo(wayPoints.get(currWayPointIndex).x, wayPoints.get(currWayPointIndex).y);
        }
        else{
            truck.setMoveTo(currWayPoint.x, currWayPoint.y);
        }

        truck.update();
    }
    public void setWayPoints(ArrayList<Vector3> wayPoints){
        this.wayPoints = new ArrayList<Vector3>(wayPoints);
    }
}
