package com.cs130.routerunner;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.TapHandler.TapHandler;

/**
 * Created by graceychin on 11/12/15.
 */
public class Missile extends Actor{
    public Missile() {
        super();
    }

    public Missile(Sprite sprite, Stage stage, TapHandler tapHandler) {
        super(sprite, stage, tapHandler);
    }
}
