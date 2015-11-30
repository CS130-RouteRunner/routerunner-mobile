package com.cs130.routerunner;

import com.badlogic.gdx.Gdx;
import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by christinayang on 11/3/15.
 */
public class Message {
    private String type_;
    private String item_;
    private String uid_;
    private String status_;
    private List<LatLngPoint> coords_;
    private Integer itemId_;

    /**
     * Constructs a Message object based on the JSONObject passed in
     * @param obj - data to construct the Message with
     */
    public Message(JSONObject obj) {
        try {
            type_ = obj.getString("type");
            uid_ = obj.getString("uid");
            JSONObject data = obj.getJSONObject("data");
//            Gdx.app.log("MessageConstructorTag", data.toString());
            itemId_ = data.getInt("id");
            if (type_.equals(Settings.PURCHASE_TYPE) || type_.equals(Settings.UPDATE_TYPE)) {
                item_ = data.getString("item");
                if (type_.equals(Settings.UPDATE_TYPE)) {
                    status_ = obj.getString("status");
                }
                coords_ = null;
            }
            else {
                item_ = null;
                status_ = null;
                coords_ = new ArrayList<LatLngPoint>();
                String coordsString = data.getString("coords");
                List<String> coordPairs = Arrays.asList(coordsString.split(";"));
                for (String pair : coordPairs) {
                    coords_.add(new LatLngPoint(pair));
                }

            /*for (JsonValue point : data.iterator()) {
                //TODO: ask julian about serializing coordinates into json

            }*/
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * JSONifies a Message object.
     * @return
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("uid", uid_);
        json.put("type", type_);

        JSONObject data = new JSONObject();
        data.put("id", itemId_);
        if (type_.equals(Settings.PURCHASE_TYPE) || type_.equals(Settings.UPDATE_TYPE)) {
            data.put("item", item_);
            if (type_.equals(Settings.UPDATE_TYPE)) {
                data.put("status", Settings.PAUSE_STATUS);
            }
        }
        else {
            String points = "";
            for (int i = 0; i < coords_.size() - 1; i++) {
                points += coords_.get(i).toString() + ";";
            }
            points += coords_.get(coords_.size() - 1).toString();
            data.put("coords", points);
        }
        json.put("data", data);
        return json;
    }

    /**
     * Gets type of Message.
     * @return
     */
    public String getType() { return type_; }

    /**
     * Gets uid of Message.
     * @return
     */
    public String getUid() { return uid_; }

    /**
     * Gets item of Message.
     * @return
     */
    public String getItem() { return item_; }

    /**
     * Gets coordinates of Message.
     * @return
     */
    public List<LatLngPoint> getCoords() { return coords_; }

    /**
     * Gets the item ID
     * @return
     */
    public Integer getItemId() { return itemId_; }

    public String getStatus() {return status_; }

    /**
     * Stringifies a Message object
     * @return
     */
    public String toString() {
        String data;
        if (type_.equals(Settings.PURCHASE_TYPE)) {
            data = "{item:" + item_ + "}";
        }
        else if (type_.equals(Settings.UPDATE_TYPE)) {
            data = "{item:" + item_ + ";status:" + status_ + "}";
        }
        else {
            String coords = "";
            for (int i = 0; i < coords_.size() - 1; i++) {
                coords += (coords_.get(i).toString()) + ";";
            }
            coords += coords_.get(coords_.size() - 1).toString();
            data = "{coords:" + coords + "}";
        }
        return "uid:" + uid_ + ";type:" + type_ + ";data:" + data;
    }
}
