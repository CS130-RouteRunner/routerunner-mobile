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
    private String username_;
    private PubnubGameHelper pubnub_;
    private String channel_;

	public RouteRunner(String username, String channel) {
        this.username_ = username;
        this.channel_ = channel;
        this.pubnub_ = new PubnubGameHelper(username, channel);
    }
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameMaster(this, this.pubnub_));
	}

	@Override
	public void render () {
		super.render();
	}
}
