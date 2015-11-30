package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Actors.Actor;
import com.cs130.routerunner.Actors.Truck;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverter;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverterAdapter;
import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import com.cs130.routerunner.Message;
import com.cs130.routerunner.MessageCenter;
import com.cs130.routerunner.Routes.Route;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julianyang on 11/5/15.
 */
public class RouteConfirmMode implements TapMode {
    TapHandler tapHandler_;
    Actor selectedActor_;
    Route snappedRoute_;
    MessageCenter messageCenter_;
    CoordinateConverter coordinateConverter_;

    public RouteConfirmMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
        this.coordinateConverter_ = new CoordinateConverterAdapter();
    }

    public void Init() {}
    public void SetRoute(Route r) {
        this.snappedRoute_ = r;
    }
    public void SetSelectedActor(Actor a) { selectedActor_ = a; }

    public void Tap(float x, float y, int count) {
        Gdx.app.log("RCTag", "Inside Route Confirm Mode");
        if (this.messageCenter_ == null) {
            this.messageCenter_ = this.tapHandler_.gameMaster_.getMessageCenter();
        }
        if(selectedActor_.isSaveRoute()) {
            Gdx.app.log("RCTag", "Save");
            // TODO(julian): see if we keep creating Route objects will cause
            // game to slow down.
            selectedActor_.setRoute(snappedRoute_);

            ArrayList<Vector3> waypoints = tapHandler_.gameMaster_.getWayPoints();

            // Prepare Route message
            JSONObject data = new JSONObject();
            String coords = "";

            for (int i = 0; i < waypoints.size() - 1; i++) {
                LatLngPoint convertedPoint = coordinateConverter_.px2ll(waypoints.get(i));
                //Gdx.app.log("RCTag", convertedPoint.toString());
                coords += convertedPoint + ";";
            }
            coords += coordinateConverter_.px2ll(waypoints.get(waypoints.size()-1));
            data.put("coords", coords);
            data.put("waypointIndex", snappedRoute_.getCurrWayPointIndex());
            ArrayList<Truck> trucks = tapHandler_.gameMaster_.getLocalPlayer().getTruckList();
            int truckId = trucks.indexOf(selectedActor_);
            data.put("id", truckId);
            data.put("truckX", selectedActor_.getX());
            data.put("truckY", selectedActor_.getY());
            Gdx.app.log("RCTag", "selected truck id :" + Integer.toString(truckId));

            // Send message
            Message toSend = messageCenter_.createRouteMessage(messageCenter_.getUUID(), data);
            messageCenter_.sendMessage(toSend);
        } else if (selectedActor_.isCancelSave()) {
            // do cleanup if needed
        }

        selectedActor_.setPaused(false);
        Gdx.app.log("RCTag", "unpaused truck");
        tapHandler_.curMode_ = tapHandler_.normalMode_;
        tapHandler_.gameMaster_.getLocalPlayerButtonInfo().display();
        tapHandler_.gameMaster_.clearWaypoints();

    }
}

