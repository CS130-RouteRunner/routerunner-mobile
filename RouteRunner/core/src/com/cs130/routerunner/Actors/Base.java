package com.cs130.routerunner.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Roger on 11/16/2015.
 */
public class Base {
    //create base sprite and logical box
    private Sprite baseSprite_;
    private Rectangle base_;
    public Base(int x, int y){
        //TODO: make a base 1 and base 2
        baseSprite_ = new Sprite(new Texture("base.png"));
        base_ = new Rectangle(x, y, 500, 500);
    }
    public float getX(){
        return base_.getX();
    }
    public float getY(){
        return base_.getY();
    }
    public Sprite getSprite(){
        return baseSprite_;
    }
    public boolean overlaps(Rectangle r){
        return base_.overlaps(r);
    }
}
