package com.cs130.routerunner.android;

/**
 * Created by roger on 10/27/15.
 *
 * Message is used to hold information that is transmitted using PubNub.
 * A message in this app has a username, message, and a timestamp.
 */
public class Message {
    public static final String TYPE = "type";   // Type of message, e.g. chat, battle, etc
    public static final String USER = "username";
    public static final String MESSAGE = "msg";
    public static final String TIME = "time";

    private String username_;
    private String message_;
    private long timeStamp_;

    public Message(String username, String message, long timeStamp) {
        this.username_ = username;
        this.message_ = message;
        this.timeStamp_ = timeStamp;
    }

    public String getUserName() {
        return username_;
    }

    public String getMessage() {
        return message_;
    }

    public long getTimeStamp() {
        return timeStamp_;
    }
}
