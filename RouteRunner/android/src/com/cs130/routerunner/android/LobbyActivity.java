package com.cs130.routerunner.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cs130.routerunner.Settings;
import com.pubnub.api.*;
import org.json.*;

import java.util.Random;


public class LobbyActivity extends Activity {
    private Pubnub pubnub_;
    private ListView listView_;
    private Button channelView_;
    private EditText messageET_;
    //private ChatAdapter chatAdapter_;

    private String username_;
    private String channel_ = "routerunner-global";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        this.username_ = sb.toString();   // Change this when login is merged
        //this.listView_ = getListView();

        initPubNub();
    }

    /**
     * Starts the Routerunner game, e.g. opens up LibGDX engine
     */
    public void startGame(View view) {
        // Do something in response to button
        Intent routeRunner = new Intent(this, AndroidLauncher.class);
        startActivity(routeRunner);
    }


    /**
     * Subscribes to a PubNub channel.
     */
    public void subscribeChannel(View view) {
        Callback subscribeCallback = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                if (message instanceof JSONObject) {
                    try {
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
            public void errorCallback(String channel, PubnubError error) {
                System.out.println("ERROR on channel " + channel + " : " + error.toString());
            }
        };

        try {
            pubnub_.subscribe(this.channel_, subscribeCallback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Publishes a message to a PubNub channel.
     */
    public void publishMessage(View view) {
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
        JSONObject json = new JSONObject();
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        try {
            json.put("data", sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pubnub_.publish(channel_, json , callback);
    }

    /**
     * Unsubscribe a PubNub channel.
     */
    public void unsubscribeChannel(View view) {
        pubnub_.unsubscribe(channel_);
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

}
