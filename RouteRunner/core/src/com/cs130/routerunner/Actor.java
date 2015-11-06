package com.cs130.routerunner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Routes.Route;
import com.cs130.routerunner.TapHandler.TapHandler;

/**
 * Created by julianyang on 10/22/15.
 */
public class Actor extends Sprite {
    private float movingTowardsX_ = -1;
    private float movingTowardsY_ = -1;
    private boolean hasStartedNewRoute_ = false;
    private float moveXDelta_ = 0;
    private float moveYDelta_ = 0;
    private boolean paused_ = false;

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
        hasStartedNewRoute_ = false;
    }

    public void move(){
        if (!paused_ && route_ != null) {
            route_.updateTruckPosition(this);
            this.setX(this.getX() + moveXDelta_);
            this.setY(this.getY() + moveYDelta_);

            //once we get within some epsilon, we stop trying to move there
            if (Math.abs(this.getY() - movingTowardsY_) < .5f)
                this.setY(movingTowardsY_);
            if (Math.abs(this.getX() - movingTowardsX_) < .5f)
                this.setX(movingTowardsX_);

            //Gdx.app.log("ATag", "DELTAXY: " + moveXDelta_ + "," +
            // moveYDelta_);
            //Gdx.app.log("ATag", "XY: " + this.getX() + "," + this.getY());
        } else {
            Gdx.app.log("ActorMove", "truck is not moving; pause: " + paused_
                    + " route_ is : " + route_ );
        }
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
        move();
    }

    public void setMoveTo(float x, float y) {

        //set moving
        movingTowardsX_ = x;
        movingTowardsY_ = y;

        //uses vector logic:
        //creates a unit vector in the desired direction
        //then multiplies by the default move
        moveXDelta_ = x - this.getX();
        moveYDelta_ = y - this.getY();
        double length = Math.sqrt(moveXDelta_ * moveXDelta_ + moveYDelta_ * moveYDelta_);
        if (length != 0 ) {
            moveXDelta_ /= length;
            moveYDelta_ /= length;
        }

        Gdx.app.log("RTag", "DELTAXY IN CALC: " + moveXDelta_ + "," +
               moveYDelta_ + " LEN: " + length);
        moveXDelta_ *= Settings.DEFAULT_MOVEMENT;
        moveYDelta_ *= Settings.DEFAULT_MOVEMENT;
    }

    public void setPaused(boolean paused) {
        paused_ = paused;
    }
    // return true if Edit Route Button is tapped
    public boolean isSaveRoute() { return actorInfo_.isSaveRoute(); }
    public boolean isEditRoute() { return actorInfo_.isEditRoute(); }
    public boolean isSnapRoute() { return actorInfo_.isSnapRoute(); }
    public boolean isCancelEdit() { return actorInfo_.isCancelEdit(); }
    public boolean isCancelSave() { return actorInfo_.isCancelSave(); }
    public boolean hasStartedNewRoute() { return this.hasStartedNewRoute_; }

}
