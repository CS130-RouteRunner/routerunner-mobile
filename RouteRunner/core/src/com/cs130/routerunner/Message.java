package com.cs130.routerunner;

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
    private List<LatLngPoint> coords_;

    public Message(JSONObject obj) {
        try {
            type_ = obj.getString("type");
            uid_ = obj.getString("uid");
            JSONObject data = obj.getJSONObject("data");
            if (type_.equals("purchase")) {
                item_ = data.getString("item");
                coords_ = null;
            } else {
                item_ = null;
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

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("uid", uid_);
        json.put("type", type_);

        JSONObject data = new JSONObject();
        if (type_.equals("purchase")) {
            data.put("item", item_);
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

    public String getType() { return type_; }
    public String getUid() { return uid_; }
    public String getItem() { return item_; }
    public List<LatLngPoint> getCoords() { return coords_; }
    public String toString() {
        String s = "uid:" + uid_ + ";type:" + type_ + ";item:" + item_;
        return s;
    }
}
