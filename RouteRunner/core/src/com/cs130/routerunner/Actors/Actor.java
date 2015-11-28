package com.cs130.routerunner.Actors;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Routes.Route;
import com.cs130.routerunner.Settings;
import com.cs130.routerunner.TapHandler.TapHandler;

/**
 * Created by julianyang on 10/22/15.
 */
public abstract class Actor extends Sprite {
    protected float movingTowardsX_ = -1;
    protected float movingTowardsY_ = -1;
    private boolean hasStartedNewRoute_ = false;
    private float moveXDelta_ = 0;
    private float moveYDelta_ = 0;
    private Vector2 destination_ = new Vector2(-1,-1);
    private boolean paused_ = false;
    private float speed_ = Settings.DEFAULT_MOVEMENT;
    protected ActorInfo actorInfo_;
    private boolean isUpgraded_ = false;

    public Route route_;
    private Stage stage_;
    private TapHandler tapHandler_;

    public Actor() {
        route_ = new Route();
    }

    public Actor(Sprite sprite, Stage stage, TapHandler tapHandler){
        super(sprite);
        stage_ = stage;
        tapHandler_ = tapHandler;
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

    //moves the truck
    public abstract void move();

    public boolean tryToTap(float x, float y) {
        if (x >= (this.getX()-this.getWidth()/2) && x <= (this.getX()+this.getWidth()/2) &&
                y >= (this.getY()-this.getHeight()/2) && y <= (this.getY()+this.getHeight()/2))
            return true;
        else
            return false;
    }

    public void update(){
        move();
    }

    //takes in the next waypoint x,y
    //creates a unit vector with start point as the current truck's location
    //pointing towards the inputted waypoint
    //sets the x,y movements to be in the direction of this vector
    public void setMovementVectorToNextWaypoint(float x, float y) {

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

/*        Gdx.app.log("RTag", "DELTAXY IN CALC: " + moveXDelta_ + "," +
               moveYDelta_ + " LEN: " + length);*/
        moveXDelta_ *= speed_;
        moveYDelta_ *= speed_;
    }

    protected void setSpeed(float speed) {speed_ = speed;}
    public float getSpeed(){return speed_;}
    protected boolean isPaused() {return paused_;}

    public void setPaused(boolean paused) {
        paused_ = paused;
    }
    public boolean hasStartedNewRoute() { return this.hasStartedNewRoute_; }
    public float getMoveXDelta() {return moveXDelta_;}
    public float getMoveYDelta() {return moveYDelta_;}

    // return true if Edit Route Button is tapped
    public boolean isSaveRoute() { return actorInfo_.isSaveRoute(); }
    public boolean isEditRoute() { return actorInfo_.isEditRoute(); }
    public boolean isSnapRoute() { return actorInfo_.isSnapRoute(); }
    public boolean isCancelEdit() { return actorInfo_.isCancelEdit(); }
    public boolean isCancelSave() { return actorInfo_.isCancelSave(); }
    public boolean isUpgrade() { return actorInfo_.isUpdateTruck(); }

    public TapHandler getTapHandler(){
        return tapHandler_;
    }

    public void displayInfo() {
        // display "edit route" button
        actorInfo_.display();
    }

    public void hideInfo() {
        // clean up any displayed buttons / info
        // namely, hide or destroy "edit route" button
        actorInfo_.hide();
    }

    public void showAlert(String string){
        tapHandler_.getGameMaster().showAlert(string);
    }

    public void upgrade(){
        setUpgraded();
        setSpeed(this.getSpeed() * 1.5f);
    }

    private void setUpgraded(){
        isUpgraded_ = true;
    }

    public boolean getUpgraded(){return isUpgraded_;}
}
