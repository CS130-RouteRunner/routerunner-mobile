package com.cs130.routerunner;

import com.cs130.routerunner.Settings;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;
import com.pubnub.api.PubnubError;

import java.util.Date;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by roger on 11/8/15.
 */
public class PubnubGameHelper {
    private Pubnub pubnub_;
    private String channel_;

    public PubnubGameHelper(String username, String channel) {
        this.pubnub_ = new Pubnub(Settings.PUBNUB_PUBLISH_KEY, Settings.PUBNUB_SUBSCRIBE_KEY);
        this.pubnub_.setUUID(username);
        this.channel_ = channel;
        subscribeChannel(channel);
    }

    public void subscribeChannel(String channel) {
        Callback subscribeCallback = new Callback() {
            @Override
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                System.out.println("ERROR on channel " + channel + ": " + error.toString());
            }
        };

        try {
            pubnub_.subscribe(channel, subscribeCallback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
    }

    public void unsubscribeChannel(String channel) {
        pubnub_.unsubscribe(channel);
    }

    public void publishMessage(String message) {
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
        pubnub_.publish(this.channel_, json, callback);
    }

    public String getUUID() {
        return pubnub_.getUUID();
    }

    public void history(long timeToken) {
        System.out.println(timeToken);
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
                System.out.println("Successful call!");
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
        // Return 100 messages newer than this timetoken
        pubnub_.history(this.channel_, timeToken, 100, true, callback);
    }

}
