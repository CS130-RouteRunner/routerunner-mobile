package com.cs130.routerunner.android;

import com.cs130.routerunner.MessageCenter;
import com.cs130.routerunner.Settings;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by graceychin on 11/1/15.
 */
public class PubnubHelper implements MessageCenter {
    private Pubnub pubnub_;
    private String channel_;

    /**
     * Instantiate PubNub object with username as UUID
     *   Then subscribe to the current channel with presence.
     *   Finally, populate the listview with past messages from history
     */
    public PubnubHelper(String username, String channel) {
        this.pubnub_ = new Pubnub(Settings.PUBNUB_PUBLISH_KEY, Settings.PUBNUB_SUBSCRIBE_KEY);
        this.pubnub_.setUUID(username);
        if (channel != null) {
            this.channel_ = channel;
            subscribeChannel(channel);
        }
    }

    /**
     * Subscribes to a PubNub channel.
     * @param channel - lobby id to join
     */
    public void subscribeChannel(String channel) {
        this.channel_ = channel;
        Callback subscribeCallback = new Callback() {
            @Override
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
//                if (message instanceof JSONObject) {
//                    try {
//                        // Will need this for chat, multiplayer msgs
//                        JSONObject jmessage = (JSONObject) message;
//                        String messageType = jmessage.getString(Message.TYPE);
//                        String username = jmessage.getString(Message.USER);
//                        String messageData = jmessage.getString(Message.MESSAGE);
//                        long time = jmessage.getLong(Message.TIME);
//                        if (username.equals(pubnub_.getUUID())) return; // ignore own msgs
//                        if (messageType.equals("chat")) {
//                            Message msg = new Message(username, messageData, time);
//
//                            // Do application-logic
//                        }
//                    } catch (Exception e) {
//                        System.out.println(e.toString());
//                    }
//                }
            }

            //@Override
            //public void connectCallback(String channel, Object message) {
            //    getPlayers();
            //}

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
     * Unsubscribe a PubNub channel.
     */
    public void unsubscribeChannel(String channel) {
        pubnub_.unsubscribe(channel);
    }

    public void hereNow(String channel, Callback hereCallback) {
        pubnub_.hereNow(channel, hereCallback);
    }

    public void presence(String channel, Callback presenceCallback) {
        try {
            pubnub_.presence(channel, presenceCallback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
    }

    public String getUUID() {
        return pubnub_.getUUID();
    }

    public void sendMessage(JSONObject message) {
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

    public List<JSONObject> getMessages(long timeToken) {
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

        return new ArrayList<JSONObject>();
    }

    /**
     * Publishes a message to a PubNub channel.
     */
    public void publishMessage(String channel) {
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
        pubnub_.publish(channel, json, callback);
    }

}
