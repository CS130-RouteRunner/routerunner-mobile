package com.cs130.routerunner.android.Test;

/**
 * Created by julianyang on 10/25/15.
 */
public class CoordinateTestPoint {
    public int zoom;
    public float lat;
    public float lng;
    public long x;
    public long y;
    public CoordinateTestPoint(int zoom, float lat, float lng, long x,
                               long y) {
        this.zoom = zoom;
        this.lat = lat;
        this.lng = lng;
        this.x = x;
        this.y = y;
    }
}