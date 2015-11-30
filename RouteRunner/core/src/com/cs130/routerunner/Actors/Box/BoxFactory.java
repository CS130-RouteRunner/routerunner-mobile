package com.cs130.routerunner.Actors.Box;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by julianyang on 11/17/15.
 */
public abstract class BoxFactory {
    abstract public Box createBox(BoxType type, int playerNum);
    abstract public Box createBox(BoxType type, int playerNum, Vector3 v);
}
