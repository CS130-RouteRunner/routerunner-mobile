package com.cs130.routerunner.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cs130.routerunner.Settings;
import com.cs130.routerunner.android.ApiServer.ApiServerGetTask;
import com.cs130.routerunner.android.ApiServer.ApiServerPostTask;
import com.pubnub.api.*;
import org.json.*;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends Activity {
    private Pubnub pubnub_;
    private ListView listView_;
    private ArrayAdapter<String> lobbyAdapter_;
    //private ChatAdapter chatAdapter_;

    private String username_;
    private String channel_ = "routerunner-global";
    private ArrayList<String> channelList_;

//    /* Client for accessing Google APIs (logout stuff for later) */
//    private GoogleApiClient mGoogleApiClient;

    private String randomString() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.username_ = randomString();   // Change this when login is merged
        System.out.println(this.username_);
        this.listView_ = (ListView) findViewById(R.id.listView);

        // Connect to PubNub
        initPubNub();

        // Populate lobby list
        this.channelList_ = new ArrayList<String>();
        this.channelList_ = getLobbies();
        this.lobbyAdapter_ = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.channelList_);
        this.listView_.setAdapter(this.lobbyAdapter_);

        // Add listener to lobby list
        this.listView_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lobbyId = (String)((TextView) view).getText();
                joinLobby(lobbyId);
                Toast.makeText(getBaseContext(), "Joined " + lobbyId, Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Creates a lobby.
     * @param view
     */
    public void createLobby(View view) {
        ApiServerPostTask apiServerPostTask = new ApiServerPostTask();
        String endpoint = Settings.CREATE_LOBBY_URL;
        try {
            JSONObject params = new JSONObject();
            String userId = this.username_;
            String lobbyId = Settings.LOBBY_PREFIX + userId;
            params.put("uid", userId);
            params.put("lid", lobbyId);
            JSONObject response = apiServerPostTask.execute(endpoint, params.toString()).get();
            System.out.println(response.toString());
            // Data persisted, okay to connect to PubNub
            subscribeChannel(lobbyId);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Join an existing lobby.
     */
    public void joinLobby(String lobbyId) {
        ApiServerPostTask apiServerPostTask = new ApiServerPostTask();
        String endpoint = Settings.ROUTERUNNER_BASE + Settings.MATCHMAKING_URL + "join";
        try {
            JSONObject params = new JSONObject();
            String userId = this.username_;
            params.put("uid", userId);
            params.put("lid", lobbyId);
            JSONObject response = apiServerPostTask.execute(endpoint, params.toString()).get();
            System.out.println(response.toString());
            // Data persisted, okay to join channel on PubNub
            subscribeChannel(lobbyId);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Retrieves a list of open lobbies.
     * @return
     */
    private ArrayList<String> getLobbies() {
        ApiServerGetTask apiServerGetTask = new ApiServerGetTask();
        String endpoint = Settings.ROUTERUNNER_BASE + Settings.MATCHMAKING_URL + "join";
        try {
            JSONObject response = apiServerGetTask.execute(endpoint).get();
            System.out.println(response.toString());
            JSONArray jlobbies = response.getJSONArray("lobbies");
            System.out.println(jlobbies);
            ArrayList<String> lobbies = new ArrayList<String>();
            for (int i = 0; i < jlobbies.length(); i++) {
                System.out.println(jlobbies.getString(i));
                lobbies.add(jlobbies.getString(i));
            }

            return lobbies;
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return new ArrayList<String>();
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
            public void errorCallback(String channel, PubnubError error) {
                System.out.println("ERROR on channel " + channel + " : " + error.toString());
            }
        };

        try {
            pubnub_.subscribe(channel, subscribeCallback);
            Intent lobby = new Intent(this, LobbyActivity.class);
            lobby.putExtra("uid", username_);
            lobby.putExtra("channel-id", channel);
            startActivity(lobby);
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

    /*

    private void onSignOutClicked() {
        // Clear the default account so that GoogleApiClient will not automatically
        // connect in the future.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }

        // return to sign in page
        Intent signIn = new Intent(this, SignInPage.class);
        startActivity(signIn);
    }

    */
}
