package com.cs130.routerunner.android;

import com.cs130.routerunner.Message;
import com.cs130.routerunner.MessageCenter;
import com.cs130.routerunner.Settings;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by graceychin on 11/1/15.
 */
public class PubnubHelper implements MessageCenter {
    private Pubnub pubnub_;
    private String channel_;
    private long lastSyncTime_;
    private List<Message> messageList;
    private CountDownLatch countDownLatch = new CountDownLatch(1);


    /**
     * Constructor
     * @param username
     * @param channel - channel to subscribe to if exists
     */
    public PubnubHelper(String username, String channel) {
        this.pubnub_ = new Pubnub(Settings.PUBNUB_PUBLISH_KEY, Settings.PUBNUB_SUBSCRIBE_KEY);
        this.pubnub_.setUUID(username);
        messageList = new ArrayList<Message>();
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
                // System.out.println(response.toString());
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
     * Unsubscribe from the channel.
     * @param channel - lobby id to leave
     */
    public void unsubscribeChannel(String channel) {
        pubnub_.unsubscribe(channel);
    }

    /**
     * Retrieves who's currently in the channel.
     * @param channel - lobby id
     * @param hereCallback - callback
     */
    public void hereNow(String channel, Callback hereCallback) {
        pubnub_.hereNow(channel, hereCallback);
    }

    /**
     * Subscribe to presence events for channel.
     * @param channel - lobby id
     * @param presenceCallback - callback
     */
    public void presence(String channel, Callback presenceCallback) {
        try {
            pubnub_.presence(channel, presenceCallback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Send message to channel.
     * @param message - Message to send
     */
    public void sendMessage(Message message) {
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                //System.out.println(response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };

        JSONObject json = message.toJSON();
        pubnub_.publish(this.channel_, json, callback);
    }


    /**
     * Returns a list of messages newer than timeToken.
     * @param timeToken - timeToken to check against
     * @return list of Message
     */
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
                    lastSyncTime_ = Long.parseLong(jarr.getString(2));
                    for (int idx = 0; idx < data.length(); idx++) {
                        JSONObject m = (JSONObject) data.get(idx);
                        System.out.println("------Curr message--------");
                        System.out.println(m.toString());
                        if (!m.getString("uid").equals(getUUID())) {
                            messages.add(new Message(m));
                            System.out.println("Size: " + String.valueOf(messages.size()));
                        }
                    }
                    // Clear the messageList before adding new ones
                    messageList.clear();
                    messageList.addAll(messages);
                    System.out.println("Size of messageList: " + String.valueOf(messageList.size()));
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

        return messageList;
    }

    /**
     * Returns the newest timestamp of the last messages that we have received.
     * @return timestamp
     */
    public long getLastSyncTime() {
        return lastSyncTime_;
    }

    /**
     * Returns the uuid associated with this Pubnub instance.
     * @return uuid
     */
    public String getUUID() {
        return pubnub_.getUUID();
    }

    /**
     * Creates a Message of type 'purchase'
     * @param uuid - uuid associated with Pubnub instance
     * @param data - payload
     * @return
     */
    public Message createPurchaseMessage(String uuid, JSONObject data) {
        JSONObject msg = new JSONObject();
        try {
            msg.put("type", "purchase");
            msg.put("uid", uuid);
            msg.put("data", data);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new Message(msg);
    }

    /**
     * Creates a Message of type 'purchase'
     * @param uuid - uuid associated with Pubnub instance
     * @param data
     * @return
     */
    public Message createRouteMessage(String uuid, JSONObject data) {
        JSONObject msg = new JSONObject();
        try {
            msg.put("type", "route");
            msg.put("uid", uuid);
            msg.put("data", data);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new Message(msg);
    }
}
