package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.Gdx;
import com.cs130.routerunner.Actors.Actor;
import com.cs130.routerunner.Actors.Truck;
import com.cs130.routerunner.Message;
import com.cs130.routerunner.MessageCenter;
import com.cs130.routerunner.Routes.Route;
import com.cs130.routerunner.Settings;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by julianyang on 10/27/15.
 */
public class ActorSelectedMode implements TapMode {
    TapHandler tapHandler_;
    Actor selectedActor_;
    MessageCenter messageCenter_;

    public ActorSelectedMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
    }

    public void Init() {}
    public void SetSelectedActor(Actor a) {
        selectedActor_ = a;
    }
    public void SetRoute(Route r) {}
    public void Tap(float x, float y, int count) {
        // TODO(Evan): check if user tapped on the route edit button
        if (this.messageCenter_ == null) {
            this.messageCenter_ = this.tapHandler_.gameMaster_.getMessageCenter();
        }

        Gdx.app.log("ASTag", "Tapped Truck inside AS\n");
        if(selectedActor_.isEditRoute()) {
            Gdx.app.log("ASTag", "Entering Route Edit Mode\n");
            tapHandler_.routeEditMode_.SetSelectedActor(this.selectedActor_);

            this.selectedActor_.setPaused(true);

            // Prepare Truck pause Message
            JSONObject data = new JSONObject();
            ArrayList<Truck> trucks = tapHandler_.gameMaster_.getLocalPlayer().getTruckList();
            int truckID = trucks.indexOf(this.selectedActor_);
            Gdx.app.log("TruckIDTag", String.valueOf(truckID));
            data.put("id", truckID);
            data.put("item", Settings.TRUCK_ITEM);
            data.put("status", Settings.PAUSE_STATUS);


            // Send Message
            Message msgToSend = messageCenter_.createUpdateMessage(messageCenter_.getUUID(), data);
            messageCenter_.sendMessage(msgToSend);

            tapHandler_.gameMaster_.clearWaypoints();
            tapHandler_.curMode_ = tapHandler_.routeEditMode_;
            tapHandler_.curMode_.Init();

        } else/* if (selectedActor_.isCancelEdit()) */{
            // user tapped out of ActorSelectMode so go back to normal mode
            // clean up any display stuff (ie "EditRouteButton")
            tapHandler_.curMode_ = tapHandler_.normalMode_;
            tapHandler_.gameMaster_.clearWaypoints();
            tapHandler_.gameMaster_.getLocalPlayerButtonInfo().display();
            //selectedActor_.hideInfo();
        }



    }
}
