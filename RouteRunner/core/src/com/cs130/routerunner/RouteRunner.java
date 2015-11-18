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
	private int playerNum_;

	public RouteRunner(MessageCenter messageCenter, int playerNum) {
		this.messageCenter_ = messageCenter;
		this.playerNum_ = playerNum;
    }
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameMaster(this, this.messageCenter_, this.playerNum_));
	}

	@Override
	public void render () {
		super.render();
	}
}
