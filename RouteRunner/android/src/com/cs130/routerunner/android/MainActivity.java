package com.cs130.routerunner.android;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cs130.routerunner.Settings;
import com.cs130.routerunner.android.ApiServer.ApiServerGetTask;
import com.cs130.routerunner.android.ApiServer.ApiServerPostTask;

import org.json.*;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends Activity {
    private PubnubHelper pubnubHelper_;
    private ListView listView_;
    private ArrayAdapter<String> lobbyAdapter_;
    private ImageButton profileImage_;

    private String username_;
    private ArrayList<String> channelList_ = new ArrayList<String>();

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

        final Context context = this;

        Intent intent = getIntent();
        final String username_ = intent.getStringExtra("username");
        this.username_ = username_;
        System.out.println(this.username_);
        this.listView_ = (ListView) findViewById(R.id.listView);
        this.listView_.setEmptyView(findViewById(R.id.emptyLobbyItem));
        this.profileImage_ = (ImageButton) findViewById(R.id.profileImage);

        // Connect to PubNub
        pubnubHelper_ = new PubnubHelper(this.username_, null);

        // Populate lobby list
        populateLobbies();

        // Add listener to lobby list
        this.listView_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lobbyId = (String) ((TextView) view).getText();
                try {
                    joinLobby(lobbyId);
                    Toast.makeText(getBaseContext(), "Joined " + lobbyId, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Add listener to profile picture
        this.profileImage_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Kailin navigate to User Profile Activity
                // Toast.makeText(getApplicationContext(), "Go to " + username_ + "'s profile", Toast.LENGTH_SHORT).show();

                ArrayList<Integer> data = getUserInfo(username_);

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_fragment);
                dialog.setTitle(username_ + "'s Stats");

                // set the custom dialog components - text, image and button
                TextView text1 = (TextView) dialog.findViewById(R.id.textView6);
                text1.setText("Number of games won: " + data.get(0));

                TextView text2 = (TextView) dialog.findViewById(R.id.textView7);
                text2.setText("Number of games lost: " + data.get(1));

                Button dialogButton = (Button) dialog.findViewById(R.id.button3);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
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
            String lobbyId = Settings.LOBBY_PREFIX + randomString();
            params.put("uid", userId);
            params.put("lid", lobbyId);
            JSONObject response = apiServerPostTask.execute(endpoint, params.toString()).get();
            System.out.println(response.toString());
            // Data persisted, okay to connect to PubNub
            pubnubHelper_.subscribeChannel(lobbyId);
            Intent lobby = new Intent(this, LobbyActivity.class);
            lobby.putExtra("uid", username_);
            lobby.putExtra("channel-id", lobbyId);
            startActivityForResult(lobby, 1);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Refreshes Lobby List.
     * @param view
     */
    public void refreshLobbyList(View view) {
        populateLobbies();
    }

    public void joinLobby(String lobbyId) throws Exception {
        ApiServerPostTask apiServerPostTask = new ApiServerPostTask();
        JSONObject params = new JSONObject();
        String userId = this.username_;
        params.put("uid", userId);
        params.put("lid", lobbyId);
        JSONObject response = apiServerPostTask.execute(Settings.JOIN_LOBBY_URL, params.toString()).get();
        System.out.println("Response: " + response.toString());
        String status = response.getString("status");
        if (status.equals("error")) {
            throw new Exception(response.getString("msg"));
        }

        // Data persisted, okay to join channel on PubNub
        pubnubHelper_.subscribeChannel(lobbyId);
        Intent lobby = new Intent(this, LobbyActivity.class);
        lobby.putExtra("uid", username_);
        lobby.putExtra("channel-id", lobbyId);
        startActivityForResult(lobby, 1);
    }

    /**
     * Handle exiting an existing lobby on back click.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            String lobbyId = data.getStringExtra("lobby-id");
            try {
                exitLobby(lobbyId);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            //pubnubHelper_.unsubscribeChannel(lobbyId);
            //Toast.makeText(getBaseContext(), "Exited " + lobbyId, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Update Server and Pubnub when user exists existing lobby.
     */
    private void exitLobby(String lobbyId) throws Exception{
        // Notify server that user is leaving lobby
        ApiServerPostTask apiServerPostTask = new ApiServerPostTask();
        String endpoint = Settings.ROUTERUNNER_BASE + Settings.MATCHMAKING_URL + "end";
        JSONObject params = new JSONObject();
        params.put("uid", this.username_);
        params.put("lid", lobbyId);
        JSONObject response = apiServerPostTask.execute(endpoint, params.toString()).get();
        System.out.println("Response: " + response.toString());

        // Notify Pubnub that user is leaving lobby
        pubnubHelper_.unsubscribeChannel(lobbyId);
    }

    /**
     * Populates the list of lobbies and updates listView.
     * @return
     */
    private void populateLobbies() {
        ApiServerGetTask apiServerGetTask = new ApiServerGetTask();
        String endpoint = Settings.ROUTERUNNER_BASE + Settings.MATCHMAKING_URL + "join";
        try {
            JSONObject response = apiServerGetTask.execute(endpoint).get();
            System.out.println(response.toString());
            JSONArray jlobbies = response.getJSONObject("data").getJSONArray("lobbies");
            System.out.println(jlobbies);
            ArrayList<String> lobbies = new ArrayList<String>();
            for (int i = 0; i < jlobbies.length(); i++) {
                lobbies.add(jlobbies.getString(i));
            }

            this.channelList_ = lobbies;
            this.lobbyAdapter_ = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.channelList_);
            this.listView_.setAdapter(this.lobbyAdapter_);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * @param username
     * @return ArrayList: (# wins, # losses)
     */
    private ArrayList<Integer> getUserInfo(String username) {
        ApiServerGetTask apiServerGetTask = new ApiServerGetTask();
        String endpoint = Settings.USER_STATS_URL + username;
        ArrayList<Integer> data = new ArrayList<Integer>();

        try {
            JSONObject response = apiServerGetTask.execute(endpoint).get();
            System.out.println(response.toString());
            String jstatus = response.getString("status");
            int jwin = response.getJSONObject("data").getInt("win");
            int jloss = response.getJSONObject("data").getInt("loss");

            if (jstatus.equals("success")) {
                data.add(jwin);
                data.add(jloss);
            }
            else {
                Toast.makeText(getBaseContext(), "User not found!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return data;
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
