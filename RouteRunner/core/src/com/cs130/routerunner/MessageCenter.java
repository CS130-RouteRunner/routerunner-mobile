package com.cs130.routerunner;

import com.cs130.routerunner.Message;
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
     * @param message - Message to send
     */
    void sendMessage(Message message);

    /**
     * Returns a list of messages newer than timeToken.
     * @param timeToken - timeToken to check against
     * @return list of Message
     */
    List<Message> getMessages(long timeToken);

    /**
     * Returns the uuid associated with this Pubnub instance.
     * @return uuid
     */
    String getUUID();

    /**
     * Returns the channel this Pubnub instance is subscribed to.
     * @return channel
     */
    String getChannel();

    /**
     * Returns the newest timestamp of the last messages that we have received.
     * @return timestamp
     */
    long getLastSyncTime();

    /**
     * Sets lastSyncTime to be now.
     */
    void setLastSyncTime();

    /**
     * Creates a Message of type 'purchase'
     * @param uuid - uuid associated with Pubnub instance
     * @param data - payload
     * @return
     */
    Message createPurchaseMessage(String uuid, JSONObject data);

    /**
     * Creates a Message of type 'purchase'
     * @param uuid - uuid associated with Pubnub instance
     * @param data
     * @return
     */
    Message createRouteMessage(String uuid, JSONObject data);

    /**
     * Creates a Message of type 'update'
     * @param uuid
     * @param data
     * @return
     */
    Message createUpdateMessage(String uuid, JSONObject data);

    /**
     * Creates a Message of type 'event'
     * @param uuid
     * @param data
     * @return
     */
    Message createEventMessage(String uuid, JSONObject data);
}
