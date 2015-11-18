package com.cs130.routerunner.Actors.Box;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Roger on 11/16/2015.
 */
public abstract class Box {
    //create base sprite and logical box
    private Sprite boxSprite_;
    private Rectangle hitBox_;
    public Box(int x, int y, String textureName, int width, int height){
        //TODO: make a base 1 and base 2
        boxSprite_ = new Sprite(new Texture(textureName));
        hitBox_ = new Rectangle(x, y, width, height);
    }
    public float getX(){
        return hitBox_.getX();
    }
    public float getY(){
        return hitBox_.getY();
    }
    public Sprite getSprite(){
        return boxSprite_;
    }
    public boolean overlaps(Rectangle r){
        return hitBox_.overlaps(r);
    }
}
