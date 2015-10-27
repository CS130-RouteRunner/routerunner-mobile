package com.cs130.routerunner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import sun.rmi.runtime.Log;

/**
 * Created by julianyang on 10/22/15.
 */
public class Actor extends Sprite {

    public float movingTowardsX;
    public float movingTowardsY;
    private final float DEFAULT_MOVEMENT = 0.5f;

    public Actor(Sprite sprite){
        super(sprite);
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    // TODO(Kailin/Evan): fill this method out
    public boolean tryToTap(float x, float y) {
        // return whether actor was tapped on.
        return false;
    }

    // TODO(Kailin/Evan): fill this method out
    public void displayInfo() {
        // display "edit route" button
    }

    // TODO(Kailin/Evan): fill this method out
    public void hideInfo() {
        // clean up any displayed buttons / info
        // namely, hide or destroy "edit route" button
    }

    public void update(){
        update(DEFAULT_MOVEMENT);
    }
    public void update(float delta){
        if (movingTowardsX > this.getX())
            this.setX(this.getX() + delta);
        else if (movingTowardsX < this.getX())
            this.setX(this.getX() - delta);
        else if (movingTowardsY > this.getY())
            this.setY(this.getY() + delta);
        else
            this.setY(this.getY() - delta);

        //once we get within some epsilon, we stop trying to move there
        if (Math.abs(this.getY() - movingTowardsY) < .5f)
            this.setY(movingTowardsY);
        if (Math.abs(this.getX() - movingTowardsX) < .5f)
            this.setX(movingTowardsX);
    }
    //set the move to with the greatest difference (since you can only move one dir at a time)
    public void setMoveTo(float x, float y) {
        //only take the direction with the greatest movement
        if (Math.abs(this.getX() - x) > (Math.abs(this.getY() - y))){
            movingTowardsX = x;
            movingTowardsY = this.getY(); //reset the last "movement"
        }
        else {
            movingTowardsY = y;
            movingTowardsX = this.getX();
        }
    }

}
