package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.Gdx;
import com.cs130.routerunner.Actor;
import com.cs130.routerunner.Missile;
import com.cs130.routerunner.Routes.Route;

import java.util.ArrayList;

/**
 * Created by graceychin on 11/12/15.
 */
public class MissileMode implements TapMode {
    Missile selectedActor_;
    TapHandler tapHandler_;
    Route newRoute_;

    public MissileMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
    }

    public void Init() {
        newRoute_ = new Route();
    }

    public void Tap(float x, float y, int count) {
        Gdx.app.log("TapTag", "Tapped Location " + x + " " + y + "\n");
        if (tappedTruck(x, y)) {
            Gdx.app.log("TapTag", "Tapped Truck To Set Target" + x + " " + y + "\n");
            tapHandler_.curMode_ = tapHandler_.normalMode_;
            tapHandler_.gameMaster_.getLocalPlayerButtonInfo().display();
            //font_.dispose();
        }
    }
    public void SetSelectedActor(Actor a) {
        if (a instanceof Missile) {
            selectedActor_ = (Missile) a;
        }
    }

    public void SetRoute(Route r) {}

    /*
    * Loop on all trucks, return true if tap collides with truck location
    * Change actorSelected to tapped truck coordinates
    */
    private boolean tappedTruck(float x, float y) {
        ArrayList<Actor> trucks_ = tapHandler_.gameMaster_.getTrucks();

        for (Actor truck: trucks_) {
            if (truck.tryToTap(x, y)) {
                Gdx.app.log("TruckTag", "Tapped Truck");
                Gdx.app.log("TruckTag", truck.getX() + " " + truck.getY() + " " + x + " " + y + "\n");
                selectedActor_.setTargetTruck(truck);
                return true;
            }
        }
        return false;
    }
}
