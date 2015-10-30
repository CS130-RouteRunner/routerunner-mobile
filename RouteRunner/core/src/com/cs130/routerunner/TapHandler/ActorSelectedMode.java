package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Actor;
import com.cs130.routerunner.Routes.Route;

import java.util.ArrayList;

/**
 * Created by julianyang on 10/27/15.
 */
public class ActorSelectedMode implements TapMode {
    TapHandler tapHandler_;
    Actor selectedActor_;

    public ActorSelectedMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
    }

    public void Init() {}
    public void SetSelectedActor(Actor a) {
        selectedActor_ = a;
    }

    public void Tap(float x, float y, int count) {
        // TODO(Evan): check if user tapped on the route edit button
        Gdx.app.log("ASTag", "Tapped Truck inside AS\n");
        if(selectedActor_.isEditRoute()) {
            Gdx.app.log("ASTag", "Entering Route Edit Mode\n");
            tapHandler_.routeEditMode_.SetSelectedActor(this.selectedActor_);

            this.selectedActor_.route_.clearWaypoints();
            tapHandler_.curMode_ = tapHandler_.routeEditMode_;
            tapHandler_.curMode_.Init();

        } else if (selectedActor_.isCancelEdit()) {
            // user tapped out of ActorSelectMode so go back to normal mode
            // clean up any display stuff (ie "EditRouteButton")
            tapHandler_.curMode_ = tapHandler_.normalMode_;
            tapHandler_.gameMaster_.clearWaypoints();
            //selectedActor_.hideInfo();
        }



    }
}
