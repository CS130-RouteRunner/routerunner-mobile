package com.cs130.routerunner.Actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Settings;
import com.cs130.routerunner.TapHandler.TapHandler;

/**
 * Created by Roger on 11/16/2015.
 */
public class Truck extends Actor {
    private int amountCarrying_;

    public Truck(){
        super();
    }

    public Truck (Sprite sprite, Stage stage, TapHandler tapHandler){
        this(sprite, stage, tapHandler, 0);
    }

    public Truck (Sprite sprite, Stage stage, TapHandler tapHandler, int initialMoney){
        super(sprite, stage, tapHandler);
        setSpeed(Settings.DEFAULT_MOVEMENT);
        actorInfo_ = new ActorInfo(stage, tapHandler);
        amountCarrying_ = initialMoney;
    }

    public void move() {
        if (!isPaused() && route_ != null) {
            route_.updateWaypoint(this);
            this.setX(this.getX() + getMoveXDelta());
            this.setY(this.getY() + getMoveYDelta());

            //once we get within some epsilon, we stop trying to move there
            if (Math.abs(this.getY() - movingTowardsY_) < .5f)
                this.setY(movingTowardsY_);
            if (Math.abs(this.getX() - movingTowardsX_) < .5f)
                this.setX(movingTowardsX_);
        } else {
            // actor not moving
        }
    }


    public int getAmount(){
        return amountCarrying_;
    }
}
