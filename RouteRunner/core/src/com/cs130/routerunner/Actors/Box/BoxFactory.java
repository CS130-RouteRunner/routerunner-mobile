package com.cs130.routerunner.Actors.Box;

/**
 * Created by julianyang on 11/17/15.
 */
public abstract class BoxFactory {
    abstract public Box createBox(BoxType type, int playerNum);
}
