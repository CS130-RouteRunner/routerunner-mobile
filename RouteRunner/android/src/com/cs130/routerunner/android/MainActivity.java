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
    private PubnubHelper pubnubHelper_;
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
        pubnubHelper_ = new PubnubHelper(this.username_);

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
            pubnubHelper_.subscribeChannel(lobbyId);
            Intent lobby = new Intent(this, LobbyActivity.class);
            lobby.putExtra("uid", username_);
            lobby.putExtra("channel-id", lobbyId);
            startActivity(lobby);
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
            pubnubHelper_.subscribeChannel(lobbyId);
            Intent lobby = new Intent(this, LobbyActivity.class);
            lobby.putExtra("uid", username_);
            lobby.putExtra("channel-id", lobbyId);
            startActivity(lobby);
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
