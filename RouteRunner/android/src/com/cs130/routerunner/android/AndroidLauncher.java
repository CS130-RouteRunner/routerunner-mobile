package com.cs130.routerunner.android;

import android.os.Bundle;
import android.content.Intent;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.cs130.routerunner.RouteRunner;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String channel = intent.getStringExtra("lobby-id");
        System.out.println("Initialized: " + username + " " + channel);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new RouteRunner(username, channel), config);
	}

// prevents back from returning to menu (using some exit button instead would be better)
//	@Override
//	public void onBackPressed() {
//	}
}
