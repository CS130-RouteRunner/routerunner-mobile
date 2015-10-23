package com.cs130.routerunner;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by julianyang on 10/22/15.
 */
public class TouchHandler implements GestureDetector.GestureListener {
    private float PAN_SPEED = 0.3f;
    private GameMaster gameMaster;

    public TouchHandler(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
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
        gameMaster.moveCamera(PAN_SPEED * deltaX, PAN_SPEED * deltaY);
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){
        return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer,
                          Vector2 initialSecondPointer,
                          Vector2 firstPointer,
                          Vector2 secondPointer){
        return false;
    }
}
