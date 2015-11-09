package com.cs130.routerunner;

import com.pubnub.api.Callback;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by roger on 11/8/15.
 */
public interface MessageCenter {

    /**
     * Subscribes to a channel.
     * @param channel - lobby id to join
     */
    void subscribeChannel(String channel);

    /**
     * Unsubscribes to a channel.
     * @param channel - lobby id to leave
     */
    void unsubscribeChannel(String channel);

    /**
     * Retrieves who's currently in the channel.
     * @param channel - lobby id
     * @param hereCallback - callback
     */
    void hereNow(String channel, Callback hereCallback);

    /**
     * Subscribe to presence events for channel.
     * @param channel - lobby id
     * @param presenceCallback - callback
     */
    void presence(String channel, Callback presenceCallback);

    /**
     * Send message to channel.
     * @param message - JSONObject to send
     */
    void sendMessage(JSONObject message);

    /**
     * Returns a list of messages newer than timeToken.
     * @param timeToken - timeToken to chec against
     * @return list of JSONObjects
     */
    List<JSONObject> getMessages(long timeToken);

    /**
     * Returns the uuid associated with this Pubnub instance.
     * @return uuid
     */
    String getUUID();

}
