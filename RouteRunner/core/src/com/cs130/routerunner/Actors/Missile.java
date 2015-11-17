package com.cs130.routerunner.Actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Settings;
import com.cs130.routerunner.TapHandler.TapHandler;

/**
 * Created by graceychin on 11/12/15.
 */
public class Missile extends Actor {
    private Actor targetTruck_;

    public Missile() {super();}

    public Missile(Sprite sprite, Stage stage, TapHandler tapHandler) {
        super(sprite, stage, tapHandler);
        setSpeed(Settings.MISSILE_MOVEMENT);
    }

    @Override
    public void move() {
        if (!isPaused() && targetTruck_!= null) {
            setMovementVectorToNextWaypoint(targetTruck_.getX(), targetTruck_.getY());
            this.setX(this.getX() + getMoveXDelta());
            this.setY(this.getY() + getMoveYDelta());
        }
    }

    public void setTargetTruck(Actor t) {targetTruck_ = t;}
    public Actor getTargetTruck() {return targetTruck_;}
}
