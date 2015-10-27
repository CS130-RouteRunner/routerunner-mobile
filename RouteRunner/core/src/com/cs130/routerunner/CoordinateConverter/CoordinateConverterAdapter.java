package com.cs130.routerunner.CoordinateConverter;

import com.badlogic.gdx.math.Vector3;
import com.cs130.routerunner.Settings;

/**
 * Created by julianyang on 10/26/15.
 */
public class CoordinateConverterAdapter implements CoordinateConverter {
    private float MapCenterWorldCoordPixelX_;
    private float MapCenterWorldCoordPixelY_;
    private float MapCenterPixelX_;
    private float MapCenterPixelY_;


    public CoordinateConverterAdapter() {
        this(Settings.WORLD_CENTER_LAT, Settings.WORLD_CENTER_LNG,
                Settings.WORLD_MAP_ZOOM, Settings.WORLD_WIDTH,
                Settings.WORLD_HEIGHT);
    }

    // Use this constructor for testing
    public CoordinateConverterAdapter(float worldCenterLat, float
            worldCenterLng, int worldZoom, int mapWidth, int mapHeight) {
        Vector3 worldCenterPx = LegacyCoordinateConverter.ll2px(
                worldCenterLat, worldCenterLng, worldZoom);
        MapCenterWorldCoordPixelX_ = worldCenterPx.x;
        MapCenterWorldCoordPixelY_ = worldCenterPx.y;
        MapCenterPixelX_ = mapWidth / 2;
        MapCenterPixelY_ = mapHeight / 2;
    }

    public Vector3 ll2px(double lat, double lng) {
        // pass into legacy version and then convert the Vector3 from world
        // coordinate into map coordinate
        Vector3 worldCoordPx = LegacyCoordinateConverter.ll2px(lat, lng,
                Settings.WORLD_MAP_ZOOM);

        float deltaX = (worldCoordPx.x - MapCenterWorldCoordPixelX_) *
                Settings.MAP_TO_WEB_DPI_RATIO;
        float deltaY = (worldCoordPx.y - MapCenterWorldCoordPixelY_) *
                Settings.MAP_TO_WEB_DPI_RATIO;

        Vector3 worldPx = new Vector3();
        worldPx.x = MapCenterPixelX_ + deltaX;
        // we use '-' because origin for WorldCoord is actually top left,
        // while our screen origin is bottom left.
        worldPx.y = MapCenterPixelY_ - deltaY;
        return worldPx;

    }
    public LatLngPoint px2ll(Vector3 pixel) {
        // change map pixel to world pixels then pass into legacy version
        float deltaX = (MapCenterPixelX_ - pixel.x) /
                Settings.MAP_TO_WEB_DPI_RATIO;
        float deltaY = (MapCenterPixelY_ - pixel.y) /
                Settings.MAP_TO_WEB_DPI_RATIO;

        Vector3 newWorldCoordPx = new Vector3();
        newWorldCoordPx.x = MapCenterWorldCoordPixelX_ + deltaX;

        // we use '-' because origin for WorldCoord is actually top left,
        // while our screen origin is bottom left.
        newWorldCoordPx.y = MapCenterWorldCoordPixelY_ - deltaY;
        return LegacyCoordinateConverter.px2ll(
                newWorldCoordPx, Settings.WORLD_MAP_ZOOM);
    }
}
