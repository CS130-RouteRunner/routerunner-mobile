package com.cs130.routerunner.Actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Actors.Box.RandomEvent;
import com.cs130.routerunner.Player;
import com.cs130.routerunner.Settings;
import com.cs130.routerunner.TapHandler.TapHandler;

/**
 * Created by Roger on 11/16/2015.
 */
public class Truck extends Actor {
    private int amountCarrying_;
    private Player player_;
    private boolean tombStoned_;

    public Truck(){
        super();
    }

    public Truck (Sprite sprite, Stage stage, TapHandler tapHandler, Player player){
        this(sprite, stage, tapHandler, 0, player);
        tombStoned_ = false;
    }

    public Truck (Sprite sprite, Stage stage, TapHandler tapHandler, int initialMoney, Player player){
        super(sprite, stage, tapHandler);
        setSpeed(Settings.DEFAULT_MOVEMENT);
        actorInfo_ = new ActorInfo(stage, tapHandler);
        amountCarrying_ = initialMoney;
        player_ = player;
        tombStoned_ = false;
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

    public void resetToSpawn() {
        this.setX(player_.getSpawnPoint().getX());
        this.setY(player_.getSpawnPoint().getY());
    }

    //check if we are intersecting our player's base
    //if yes, then add money to the player
    //if no, then do not
    public void checkIntersectingBase(){
        if (player_.getDeliveryPoint().overlaps(this.getBoundingRectangle()))
            player_.addMoney(this.getAmount());
    }

    public boolean checkIntersectingRandomEvent(RandomEvent randomEvent){
        if(randomEvent.overlaps(this.getBoundingRectangle())) {
            player_.addMoney(Settings.RANDOM_EVENT_VAL);
            return true;
        }
        return false;
    }

    public int getAmount(){
        return amountCarrying_;
    }

    public boolean getTombStoned() { return tombStoned_; }

    public void setTombStoned_(boolean tombStoned_) { this.tombStoned_ = tombStoned_; }
}
