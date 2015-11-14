package com.cs130.routerunner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RouteRunner extends Game {
    //note that Evan's test map is 800x800

	public SpriteBatch batch;
	Texture img;
    private MessageCenter messageCenter_;

	public RouteRunner(MessageCenter messageCenter) {
		this.messageCenter_ = messageCenter;
    }
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameMaster(this, this.messageCenter_));
	}

	@Override
	public void render () {
		super.render();
	}
}
