package com.cs130.routerunner.android;

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

    private Pubnub pubnub_;
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
        initPubNub();
        subscribeChannel(channel);
        subscribePresence(channel);

        // Get players
        this.playerList_ = new ArrayList<String>();
        this.playerAdapter_ = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.playerList_);
        this.listView_.setAdapter(this.playerAdapter_);
    }

    /**
     * Subscribes to a PubNub channel.
     * @param channel - lobby id to join
     */
    private void subscribeChannel(String channel) {
        Callback subscribeCallback = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                if (message instanceof JSONObject) {
                    try {
                        // Will need this for chat, multiplayer msgs
                        JSONObject jmessage = (JSONObject) message;
                        String messageType = jmessage.getString(Message.TYPE);
                        String username = jmessage.getString(Message.USER);
                        String messageData = jmessage.getString(Message.MESSAGE);
                        long time = jmessage.getLong(Message.TIME);
                        if (username.equals(pubnub_.getUUID())) return; // ignore own msgs
                        if (messageType.equals("chat")) {
                            Message msg = new Message(username, messageData, time);

                            // Do application-logic
                        }
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }

            @Override
            public void connectCallback(String channel, Object message) {
                getPlayers();
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                System.out.println("ERROR on channel " + channel + " : " + error.toString());
            }
        };

        try {
            pubnub_.subscribe(channel, subscribeCallback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }

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

        try {
            pubnub_.presence(channel, presenceCallback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
    }


    /**
     * Instantiate PubNub object with username as UUID
     *   Then subscribe to the current channel with presence.
     *   Finally, populate the listview with past messages from history
     */
    private void initPubNub() {
        this.pubnub_ = new Pubnub(Settings.PUBNUB_PUBLISH_KEY, Settings.PUBNUB_SUBSCRIBE_KEY);
        this.pubnub_.setUUID(this.username_);
        //subscribeWithPresence();
        //history();
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
            pubnub_.hereNow(channel_, hereCallback);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Starts the Routerunner game, e.g. opens up LibGDX engine
     */
    public void startGame(View view) {
        // Do something in response to button
        Intent routeRunner = new Intent(this, AndroidLauncher.class);
        startActivity(routeRunner);
    }

}