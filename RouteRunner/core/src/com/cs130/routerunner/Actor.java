package com.cs130.routerunner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Routes.Route;
import com.cs130.routerunner.TapHandler.TapHandler;


import sun.rmi.runtime.Log;

/**
 * Created by julianyang on 10/22/15.
 */
public class Actor extends Sprite {
    private float movingTowardsX_;
    private float movingTowardsY_;
    private float moveXDelta_ = 0;
    private float moveYDelta_ = 0;

    public Route route_;
    private Stage stage_;
    private TapHandler tapHandler_;
    private ActorInfo actorInfo_;

    public Actor(Sprite sprite, Stage stage, TapHandler tapHandler){
        super(sprite);
        stage_ = stage;
        tapHandler_ = tapHandler;
        actorInfo_ = new ActorInfo(stage_, tapHandler_);
        route_ = new Route();
    }

    @Override
    public void draw(Batch batch) {
        update();
        super.draw(batch);
    }
    public void setRoute(Route r){
        route_ = r;
    }
    public void moveAlongRoute(){
        if (route_ == null)
            return;

        route_.updateTruckPosition(this);
    }

    // TODO(Kailin): return whether actor was tapped on
    public boolean tryToTap(float x, float y) {
        if (x >= this.getX() && x <= (this.getX()+this.getWidth()) &&
                y >= this.getY() && y <= (this.getY()+this.getHeight()))
            return true;
        else
            return false;
    }

    // TODO(Kailin/Evan): fill this method out
    public void displayInfo() {
        // display "edit route" button
        actorInfo_.display();
    }

    // TODO(Kailin/Evan): fill this method out
    public void hideInfo() {
        // clean up any displayed buttons / info
        // namely, hide or destroy "edit route" button
        actorInfo_.hide();
    }

    public void update(){
        this.setX(this.getX() + moveXDelta_);
        this.setY(this.getY() + moveYDelta_);

        //once we get within some epsilon, we stop trying to move there
        if (Math.abs(this.getY() - movingTowardsY_) < .5f)
            this.setY(movingTowardsY_);
        if (Math.abs(this.getX() - movingTowardsX_) < .5f)
            this.setX(movingTowardsX_);
    }

    public void setMoveTo(float x, float y) {

        //set moving
        movingTowardsX_ = x;
        movingTowardsY_ = y;
/*
        //TODO: check when slope = infinity
        //calculate the "moving vector"
        if (Math.abs(x - this.getX()) < Settings.EPSILON){
            moveXDelta_ = 0f;
            moveYDelta_ = Settings.DEFAULT_MOVEMENT;
        }

        float slope = (y - this.getY())/(x - this.getX());
        moveXDelta_ = (float) Math.sqrt(Math.pow(Settings.DEFAULT_MOVEMENT, 2)/ (slope + 1));
        moveYDelta_ = slope * moveXDelta_;
 */
        moveXDelta_ = x - this.getX();
        moveYDelta_ = y - this.getY();
        double length = Math.sqrt(moveXDelta_ * moveXDelta_ + moveYDelta_ * moveYDelta_);
        moveXDelta_ /= length;
        moveYDelta_ /= length;

        moveXDelta_ *= Settings.DEFAULT_MOVEMENT;
        moveYDelta_ *= Settings.DEFAULT_MOVEMENT;

    }

    // return true if Edit Route Button is tapped
    public boolean isEditRoute() { return actorInfo_.isEditRoute(); }
    public boolean isSaveRoute() { return actorInfo_.isSaveRoute(); }
    public boolean isCancelEdit() { return actorInfo_.isCancelEdit(); }

}
