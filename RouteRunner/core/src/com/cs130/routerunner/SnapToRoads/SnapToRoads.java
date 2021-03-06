package com.cs130.routerunner.SnapToRoads;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverter;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverterAdapter;
import com.cs130.routerunner.Settings;
import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.maps.model.SnappedPoint;
import com.google.maps.model.LatLng;
import com.google.maps.RoadsApi;
import com.google.maps.GeoApiContext;

/**
 * Created by christinayang on 10/26/15.
 */
public class SnapToRoads {
    private GeoApiContext Context;
    private CoordinateConverter coordinateConverter_;

    public SnapToRoads() {
        this(new CoordinateConverterAdapter());
    }

    public SnapToRoads(CoordinateConverter coordinateConverter) {
        Context = new GeoApiContext().setApiKey(Settings.SNAP_ROADS_KEY);
        coordinateConverter_ = coordinateConverter;
    }

    /* TODO: the api limits the number of points in a path to 100, so we should implement
       a check that splits the path into groups of 100 and make separate api calls
    */
    public ArrayList<LatLngPoint> GetSnappedPoints(ArrayList<LatLngPoint> UserPoints) {
        LatLng[] DataPoints = new LatLng[UserPoints.size()];

        // Convert LatlngPoints to LatLngs that are used by the Google API
        for (int i = 0; i < UserPoints.size(); i++) {
            LatLng point = new LatLng(UserPoints.get(i).lat, UserPoints.get(i).lng);
            DataPoints[i] = point;
        }

        // Make the API call to get the snapped points
        SnappedPoint[] SPoints = null;
        while (SPoints == null) {
            try {
                SPoints = RoadsApi.snapToRoads(Context, true, DataPoints).await();
            } catch (Exception e) {
                // do some error handling
                Gdx.app.error("SnapToRoads", e.getMessage());
            }
        }
        List<SnappedPoint> Points = Arrays.asList(SPoints);

        // Convert LatLngs to LatLngPoints
        ArrayList<LatLngPoint> ConvertedSnappedPoints = new ArrayList<LatLngPoint>();
        for (SnappedPoint p : Points) {
            ConvertedSnappedPoints.add(new LatLngPoint((float) p.location.lat,(float) p.location.lng));
        }

        return ConvertedSnappedPoints;
    }

    public List<Vector3> snapPoints(List<Vector3> points) {
        ArrayList<LatLngPoint> convertedPoints = new ArrayList<LatLngPoint>();
        for (Vector3 point : points) {
            convertedPoints.add(coordinateConverter_.px2ll(point));
        }
        convertedPoints = GetSnappedPoints(convertedPoints);
        points.clear();
        for (LatLngPoint coord : convertedPoints) {
            Gdx.app.log("SNAP", "Adding point at: " + coord.lat + " " + coord
                    .lng);
            points.add(coordinateConverter_.ll2px(coord.lat, coord.lng));
        }
        return points;
    }
}