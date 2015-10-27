package com.cs130.routerunner;

/**
 * Created by julianyang on 10/22/15.
 */
public class Settings {
    public static final float PAN_SPEED = 1f;
    public static final float ZOOM_SPEED = 0.00005f;
    public static final float MAX_ZOOM = 0.9f;
    public static final int WORLD_WIDTH = 2826;
    public static final int WORLD_HEIGHT = 3412;
	public static final int VIEW_WIDTH = 800;
	public static final int VIEW_HEIGHT = 450;

    // Pixel Conversion stuff
    public static final float WORLD_CENTER_LAT = 34.05336f;
    public static final float WORLD_CENTER_LNG = -118.4439f;
    public static final int WORLD_MAP_ZOOM = 16;
    public static final int WORLD_MAP_DPI = 150;
    public static final int WEB_IMAGE_DPI = 72;
    public static final float MAP_TO_WEB_DPI_RATIO = WORLD_MAP_DPI /
            (float) WEB_IMAGE_DPI;

    public static final String SNAP_ROADS_KEY = "AIzaSyAQceKfG7dnkUsnOelOzUfpul5Zi4-FKUg";
}
