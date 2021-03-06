package com.cs130.routerunner.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cs130.routerunner.Settings;
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

    private boolean opponentStart_;
    private boolean selfStart_;


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
        getPlayers(channel);
        if(this.playerList_ == null)
            this.playerList_ = new ArrayList<String>();
        this.playerAdapter_ = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.playerList_);
        this.listView_.setAdapter(this.playerAdapter_);

        //Set Return Intent
        Intent returnIntent = new Intent();
        returnIntent.putExtra("lobby-id",channel);
        setResult(RESULT_OK,returnIntent);

        // Set default start to false
        this.opponentStart_ = false;
        this.selfStart_ = false;
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
                                    if (!user.equals(pubnubHelper_.getUUID())) {
                                        opponentStart_ = true;
                                    }

                                    if (selfStart_ && opponentStart_) {
                                        System.out.println("state is changed!");
                                        pubnubHelper_.setState();
                                        enterGame();
                                    }
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
    private void getPlayers(String channel) {
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
                        public void run() {
                            for(String player: players) {
                                if(!playerList_.contains(player)) {
                                    playerList_.add(player);
                                    playerAdapter_.notifyDataSetChanged();
                                }
                            }
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
            pubnubHelper_.hereNow(channel, hereCallback);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Starts the Routerunner game, e.g. opens up LibGDX engine
     */
    public void startGame(View view) {
        if (Settings.WAIT_FOR_PLAYERS) {
            selfStart_ = true;
            pubnubHelper_.setState();
            ProgressDialog progress = ProgressDialog.show(this, "Waiting for other players", "Please wait", true, true);
        }
        else
            enterGame();

    }

    public void enterGame() {
        Intent routeRunner = new Intent(this, AndroidLauncher.class);
        routeRunner.putExtra("username", pubnubHelper_.getUUID());
        routeRunner.putExtra("playerNum", getPlayerID());
        routeRunner.putExtra("lobby-id", channel_);
        startActivity(routeRunner);
        finish();
    }

    private int getPlayerID() {
        if (playerList_.size() != 2) {
            return 0;
        }
        // just assume opponent entered lobby first, fix it if it isn't the case
        String opponentName = playerList_.get(0);
        if(opponentName.equals(username_)) {
            opponentName = playerList_.get(1);
        }
        int comparison = username_.compareTo(opponentName);
        System.out.println("username: " + username_);
        System.out.println("opponent: " + opponentName);
        System.out.println("comparison: " + comparison);
        int playerID = comparison < 0 ? 0 : 1;
        System.out.println("playerID: " + playerID);
        return playerID;
    }

}
