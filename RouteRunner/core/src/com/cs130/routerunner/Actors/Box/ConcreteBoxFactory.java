package com.cs130.routerunner.Actors.Box;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverter;
import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import com.cs130.routerunner.Settings;
import com.cs130.routerunner.SnapToRoads.SnapToRoads;

/**
 * Created by julianyang on 11/17/15.
 */
public class ConcreteBoxFactory extends BoxFactory {
    private int randomEventAreaHalfSideLength_;
    private SnapToRoads snapToRoads_;
    public ConcreteBoxFactory(SnapToRoads snapToRoads) {
        randomEventAreaHalfSideLength_ =
                (int) (Settings.RANDOM_EVENT_AREA_PROPORTION *
                Math.min(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT)) / 2;
        snapToRoads_ = snapToRoads;
    }
     public Box createBox(BoxType type, int playerNum) {
        // switch statement to create boxes.
        Box newBox;
        switch(type) {
            case DeliveryPoint:
                newBox = new DeliveryPoint(playerNum);
                break;
            case SpawnPoint:
                newBox = new SpawnPoint(playerNum);
                break;
            case RandomEvent:
                int x = Settings.WORLD_WIDTH/2 +
                    MathUtils.random(randomEventAreaHalfSideLength_  * -1, randomEventAreaHalfSideLength_ );
                int y = Settings.WORLD_HEIGHT/2 +
                MathUtils.random(randomEventAreaHalfSideLength_  * -1, randomEventAreaHalfSideLength_ );
                List<Vector3> points = new ArrayList<Vector3>();
                Vector3 point = new Vector3();
                point.x = x;
                point.y = y;
                points.add(point);
                points = snapToRoads_.snapPoints(points);
                newBox = new RandomEvent((int) points.get(0).x, (int) points
                        .get(0).y);
                break;
            default:
                newBox = new SpawnPoint(playerNum);
        }
        return newBox;
    }
}
