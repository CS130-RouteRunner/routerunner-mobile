package com.cs130.routerunner.CoordinateConverter;
import java.util.Arrays;
import java.util.List;
/**
 * Created by julianyang on 10/25/15.
 */
public class LatLngPoint {
    public float lat;
    public float lng;

    public LatLngPoint(float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLngPoint(String pair) {
        List<String> coords = Arrays.asList(pair.split(","));
        lat = Float.parseFloat(coords.get(0));
        lng = Float.parseFloat(coords.get(1));
    }

    public String toString() {
        return Float.toString(lat) + "," + Float.toString(lng);
    }
}

