package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.Gdx;
import com.cs130.routerunner.Actors.*;
import com.cs130.routerunner.Actors.Missile;
import com.cs130.routerunner.Routes.*;
import com.cs130.routerunner.Settings;

import java.util.ArrayList;

/**
 * Created by julianyang on 10/22/15.
 */
public class NormalMode implements TapMode {
    TapHandler tapHandler_;
    ArrayList<Truck> trucks_ = null;
    Actor actorSelected = null;

    public NormalMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
    }
    public void Init() {}
    public void SetRoute(Route r) {}
    public void SetSelectedActor(Actor a) {
        // do nothing, normal mode doesn't have a selected actor
    }
    /*
     check if the tap collides with any buttons
     check if the tap collides with any actors
     SelectedActorMode

     1) SelectedActor should change state to RouteEditMode when the user
     clicks on SelectedActorMode
     2) Before changing to SelectedActor, we need to display an
     EditRoute Button.
     else do nothing

     should probably just loop on all actors in the game or something?
     */
    public void Tap(float x, float y, int count) {
        Gdx.app.log("TapTag", x + " " + y + "\n");
        // check if tapped on truck
        if (tappedTruck(x, y)) {
            Gdx.app.log("TapTag", "Tapped Truck " + x + " " + y + "\n");
            actorSelected.displayInfo();
            tapHandler_.actorSelectedMode_.SetSelectedActor(actorSelected);
            tapHandler_.curMode_ = tapHandler_.actorSelectedMode_;
            tapHandler_.gameMaster_.setWaypoints(actorSelected.route_.wayPoints_);
            tapHandler_.gameMaster_.getLocalPlayerButtonInfo().hide();
            // Display Edit Route Button
            //tapHandler_.curMode_.Tap(x, y, count);
        }
        else if (tappedBuyTruck(x, y)) { //check if we tapped the "buy truck" button
            Gdx.app.log("TapTag", "Tapped to Buy a Truck " + x + " " + y + "\n");
            tapHandler_.gameMaster_.buyTruck();
        }
        else if (tappedBuyMissile(x, y)) {
            Gdx.app.log("TapTag", "Tapped to Buy a Missile " + x + " " + y + "\n");
            //Missile m = tapHandler_.gameMaster_.buyMissile();
            if (!tapHandler_.gameMaster_.getOpponentTrucks().isEmpty()) {
                if (tapHandler_.gameMaster_.getLocalPlayer().getMoney() >= Settings.BUY_MISSILE_COST) {
                    tapHandler_.gameMaster_.getLocalPlayer().subtractMoney(Settings.BUY_MISSILE_COST);
                    Missile m = tapHandler_.gameMaster_.buyMissile();
                    tapHandler_.gameMaster_.getLocalPlayerButtonInfo().hide();
                    tapHandler_.curMode_ = tapHandler_.missileMode_;
                    tapHandler_.missileMode_.Init();
                    tapHandler_.missileMode_.SetSelectedActor(m);
                } else {
                    tapHandler_.gameMaster_.showAlert("Insufficient Funds");
                }
            } else {
                tapHandler_.gameMaster_.showAlert("Opponent does not have any trucks to attack");
            }
        }
        else {
            // do nothing for now.
            // TODO():need to implement other buttons (ie buy truck) later
            tapHandler_.gameMaster_.clearWaypoints();
        }

        // We will need to refactor this somewhere else later.

    }

    /*
    * Loop on all trucks, return true if tap collides with truck location
    * Change actorSelected to tapped truck coordinates
    */
    private boolean tappedTruck(float x, float y) {
        trucks_ = tapHandler_.gameMaster_.getTrucks();

        for (Truck truck: trucks_) {
            if (truck.tryToTap(x, y)) {
                Gdx.app.log("TruckTag", "Tapped Truck");
                Gdx.app.log("TruckTag", truck.getX() + " " + truck.getY() + " " + x + " " + y + "\n");
                actorSelected = truck;
                return true;
            }
        }
        return false;
    }
    private boolean tappedBuyTruck(float x, float y) {
        return tapHandler_.gameMaster_.getLocalPlayerButtonInfo().isBuyTruck();
    }

    private boolean tappedBuyMissile(float x, float y) {
        return tapHandler_.gameMaster_.getLocalPlayerButtonInfo()
                .isBuyMissile();
    }
}
