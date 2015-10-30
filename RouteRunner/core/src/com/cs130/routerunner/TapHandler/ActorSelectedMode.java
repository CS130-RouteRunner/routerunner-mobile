package com.cs130.routerunner.TapHandler;

import com.badlogic.gdx.Gdx;
import com.cs130.routerunner.Actor;

/**
 * Created by julianyang on 10/27/15.
 */
public class ActorSelectedMode implements TapMode {
    TapHandler tapHandler_;
    Actor selectedActor_;

    public ActorSelectedMode(TapHandler tapHandler) {
        this.tapHandler_ = tapHandler;
    }

    public void SetSelectedActor(Actor a) {
        selectedActor_ = a;
    }

    // TODO(): write a setButton method

    public void Tap(float x, float y, int count) {
        // TODO(Evan): check if user tapped on the route edit button
        Gdx.app.log("ASTag", "Tapped Truck inside AS\n");
        if(selectedActor_.isEditRoute()) {
            Gdx.app.log("ASTag", "Entering Route Edit Mode\n");
            tapHandler_.routeEditMode_.SetSelectedActor(this.selectedActor_);
            tapHandler_.curMode_ = tapHandler_.routeEditMode_;
        } else if(selectedActor_.isCancelEdit())
        {
            // user tapped out of ActorSelectMode so go back to normal mode
            // clean up any display stuff (ie "EditRouteButton")
            tapHandler_.curMode_ = tapHandler_.normalMode_;
            //selectedActor_.hideInfo();
        }



    }
}
