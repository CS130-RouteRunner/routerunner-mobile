package com.cs130.routerunner.android;

import android.os.Bundle;
import android.content.Intent;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.cs130.routerunner.RouteRunner;

public class AndroidLauncher extends AndroidApplication {
	private String username_;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		username_ = intent.getStringExtra("username");
        String channel = intent.getStringExtra("lobby-id");
        System.out.println("Initialized: " + username_ + " " + channel);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new RouteRunner(new PubnubHelper(username_, channel),
						intent.getIntExtra("playerNum", 0)),
				config);
	}

 //prevents back from returning to menu (using some exit button instead would be better)
	@Override
	public void onBackPressed() {
		Intent main = new Intent(this, MainActivity.class);
		main.putExtra("username", username_);
		// TODO: Kailin Make a POST request to CREATE_USER_URL
		try {
			startActivity(main);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
