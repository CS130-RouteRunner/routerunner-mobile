package com.cs130.routerunner;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by julianyang on 10/22/15.
 */
public class GestureHandler implements GestureDetector.GestureListener {
    private GameMaster gameMaster_;

    public GestureHandler(GameMaster gameMaster) {
        this.gameMaster_ = gameMaster;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // Input x,y coordinates are relative to the screen.  Unwrap the

        // camera to grab the x,y coordinates relative to the map.
        gameMaster_.handleTap(x, y, count);
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        gameMaster_.getCamera().moveCamera(
                Settings.PAN_SPEED * deltaX,
                Settings.PAN_SPEED * deltaY);
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){
        gameMaster_.getCamera().zoomCamera(
                (originalDistance - currentDistance) * Settings.ZOOM_SPEED);
        return true;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer,
                          Vector2 initialSecondPointer,
                          Vector2 firstPointer,
                          Vector2 secondPointer){
        return false;
    }
}
