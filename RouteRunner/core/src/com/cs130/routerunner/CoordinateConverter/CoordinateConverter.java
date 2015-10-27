package com.cs130.routerunner.CoordinateConverter;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by julianyang on 10/26/15.
 */
public interface CoordinateConverter {
     Vector3 ll2px(double lat, double lng);
     LatLngPoint px2ll(Vector3 pixel);
}
