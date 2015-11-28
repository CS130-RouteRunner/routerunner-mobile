package com.cs130.routerunner.Actors.Box;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.cs130.routerunner.Settings;

/**
 * Created by julianyang on 11/26/15.
 */
public class RandomEvent extends Box {
    public RandomEvent(int x, int y) {
        //pick a random location

        super(x, y, Settings.RANDOM_EVENT_PNG, 10, 10);
    }

    @Override
    public boolean overlaps(Rectangle r){
        r.setWidth(10);
        r.setHeight(10);
        return super.overlaps(r);
    }
}
