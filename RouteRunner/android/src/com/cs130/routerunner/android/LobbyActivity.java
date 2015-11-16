package com.cs130.routerunner.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pubnub.api.*;
import org.json.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class LobbyActivity extends Activity {

    private ListView listView_;
    private ArrayAdapter<String> playerAdapter_;

    private PubnubHelper pubnubHelper_;
    private String username_;
    private String channel_;

    private ArrayList<String> playerList_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        this.listView_ = (ListView) findViewById(R.id.playerListView);
        Intent intent = getIntent();

        // Get extra information
        String user = intent.getStringExtra("uid");
        this.username_ = user;
        String channel = intent.getStringExtra("channel-id");
        this.channel_ = channel;

        // Dynamically update the text in TextView
        TextView channel_title = (TextView) findViewById(R.id.channelName);
        channel_title.setText(channel);

        // Connect to PubNub
        pubnubHelper_ = new PubnubHelper(this.username_, this.channel_);
        subscribePresence(channel);

        // Get players
        getPlayers();
        this.playerList_ = new ArrayList<String>();
        this.playerAdapter_ = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.playerList_);
        this.listView_.setAdapter(this.playerAdapter_);

        //Set Return Intent
        Intent returnIntent = new Intent();
        returnIntent.putExtra("lobby-id",channel);
        setResult(RESULT_OK,returnIntent);
    }

    /**
     * Subscribe to Presence events on this channel.
     * @param channel - lobby id to subscribe
     */
    private void subscribePresence(String channel) {
        Callback presenceCallback = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
               // System.out.println(channel + " : " + message.getClass() + " : " + message.toString());
                if (message instanceof JSONObject) {
                    JSONObject jmessage = (JSONObject) message;
                    System.out.println(jmessage.toString());
                    try {
                        final int occupants = jmessage.getInt("occupancy");
                        final String user = jmessage.getString("uuid");
                        final String action = jmessage.getString("action");
                        LobbyActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (action.equals("join")) {
                                    if (!playerList_.contains(user)) {
                                        playerList_.add(user);
                                        playerAdapter_.notifyDataSetChanged();
                                    }
                                }

                                if (action.equals("state-change")) {
                                    System.out.println("state is changed!");
                                    enterGame();
                                }

                            }
                        });
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                System.out.println("ERROR on channel " + channel + " : " + error.toString());
            }
        };
        pubnubHelper_.presence(channel, presenceCallback);
    }

    /**
     * Retrieves a list of players currently in lobby.
     * @return
     */
    private void getPlayers() {
        Callback hereCallback = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                try {
                    JSONObject jmessage = (JSONObject) message;
                    int occupants = jmessage.getInt("occupancy");

                    // Get users in the lobby
                    JSONArray hereNowJSON = jmessage.getJSONArray("uuids");
                    System.out.println(hereNowJSON);
                    final Set<String> players = new HashSet<String>();
                    players.add(username_);
                    for (int i = 0; i < hereNowJSON.length(); i++) {
                        players.add(hereNowJSON.getString(i));
                        System.out.println(hereNowJSON.getString(i));
                    }
                    LobbyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        // TODO: Dunno why this doesn't work, look at it later
                        public void run() {
                            //playerList_ = new ArrayList<String>(players);
                            //playerAdapter_.notifyDataSetChanged();
                            //System.out.println(playerList_);
                        }

                    });
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
         };
        try {
            pubnubHelper_.hereNow(channel_, hereCallback);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Starts the Routerunner game, e.g. opens up LibGDX engine
     */
    public void startGame(View view) {
        ProgressDialog progress;
        pubnubHelper_.setState();
        progress = ProgressDialog.show(this, "Waiting for other players", "Please wait", true, true);
    }

    public void enterGame() {
        Intent routeRunner = new Intent(this, AndroidLauncher.class);
        routeRunner.putExtra("username", pubnubHelper_.getUUID());
        routeRunner.putExtra("playerNum", 0);
        routeRunner.putExtra("lobby-id", channel_);
        startActivity(routeRunner);
    }

}
