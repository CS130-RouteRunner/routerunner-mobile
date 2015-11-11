package com.cs130.routerunner.android;

import com.cs130.routerunner.Message;
import com.cs130.routerunner.MessageCenter;
import com.cs130.routerunner.Settings;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by graceychin on 11/1/15.
 */
public class PubnubHelper implements MessageCenter {
    private Pubnub pubnub_;
    private String channel_;
    private long lastSyncTime_;
    private List<Message> l;
    private CountDownLatch countDownLatch = new CountDownLatch(1);


    /**
     * Instantiate PubNub object with username as UUID
     *   Then subscribe to the current channel with presence.
     *   Finally, populate the listview with past messages from history
     */
    public PubnubHelper(String username, String channel) {
        this.pubnub_ = new Pubnub(Settings.PUBNUB_PUBLISH_KEY, Settings.PUBNUB_SUBSCRIBE_KEY);
        this.pubnub_.setUUID(username);
        l = new ArrayList<Message>();
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

    public void sendMessage(Message message) {
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };

        JSONObject json = message.toJSON();
        pubnub_.publish(this.channel_, json, callback);
    }


    public List<Message> getMessages(long timeToken) {
        System.out.println(timeToken);
        final List<Message> messages = new ArrayList<>();
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                try {
                    System.out.println("This is the response: " + response.toString());
                    JSONArray jarr = (JSONArray) response;

                    JSONArray data = (JSONArray) jarr.get(0);
                    long oldestTimeToken = Long.parseLong(jarr.getString(1));
                    long newestTimeToken = Long.parseLong(jarr.getString(2));
                    lastSyncTime_ = newestTimeToken;
                    for (int idx = 0; idx < data.length(); idx++) {
                        JSONObject m = (JSONObject) data.get(idx);
                        System.out.println("------Curr message--------");
                        System.out.println(m.toString());
//                        if (!m.getString("uid").equals(getUUID())) {
                            messages.add(new Message(m));
                            System.out.println("Size: " + String.valueOf(messages.size()));
//                        }
                    }
                    l.addAll(messages);
                    System.out.println("Size of l: " + String.valueOf(l.size()));
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                countDownLatch.countDown();
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
                countDownLatch.countDown();
            }
        };
        // Return 100 messages newer than this timetoken
        pubnub_.history(this.channel_, timeToken, 100, true, callback);
        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // re-initialize countdown latch for each call to getMessages
        countDownLatch = new CountDownLatch(1);

        System.out.println("-----------------");
        for(Message m: l)
        {
            System.out.println(m.toString());
        }
        return l;
    }

    public long getLastSyncTime() {
        return lastSyncTime_;
    }

    public String getUUID() {
        return pubnub_.getUUID();
    }
}
