package com.cs130.routerunner;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by julianyang on 10/22/15.
 */
public class GestureHandler implements GestureDetector.GestureListener {
    private GameMaster gameMaster;

    public GestureHandler(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        gameMaster.handleTap(x, y, count);
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
        gameMaster.moveCamera(
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
        gameMaster.zoomCamera(
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
