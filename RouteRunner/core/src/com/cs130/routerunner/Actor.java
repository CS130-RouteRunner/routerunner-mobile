package com.cs130.routerunner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Routes.Route;


import sun.rmi.runtime.Log;

/**
 * Created by julianyang on 10/22/15.
 */
public class Actor extends Sprite {

    private float movingTowardsX_;
    private float movingTowardsY_;
    private Route route_;
    private Stage stage_;
    private ActorInfo actorInfo_;

    public Actor(Sprite sprite, Stage stage){
        super(sprite);
        stage_ = stage;
        actorInfo_ = new ActorInfo(stage_);
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
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
        update(Settings.DEFAULT_MOVEMENT);
    }
    public void update(float delta){
        if (movingTowardsX_ > this.getX())
            this.setX(this.getX() + delta);
        else if (movingTowardsX_ < this.getX())
            this.setX(this.getX() - delta);
        else if (movingTowardsY_ > this.getY())
            this.setY(this.getY() + delta);
        else
            this.setY(this.getY() - delta);

        //once we get within some epsilon, we stop trying to move there
        if (Math.abs(this.getY() - movingTowardsY_) < .5f)
            this.setY(movingTowardsY_);
        if (Math.abs(this.getX() - movingTowardsX_) < .5f)
            this.setX(movingTowardsX_);
    }
    //set the move to with the greatest difference (since you can only move one dir at a time)
    public void setMoveTo(float x, float y) {
        //only take the direction with the greatest movement
        if (Math.abs(this.getX() - x) > (Math.abs(this.getY() - y))){
            movingTowardsX_ = x;
            movingTowardsY_ = this.getY(); //reset the last "movement"
        }
        else {
            movingTowardsY_ = y;
            movingTowardsX_ = this.getX();
        }
    }

}
