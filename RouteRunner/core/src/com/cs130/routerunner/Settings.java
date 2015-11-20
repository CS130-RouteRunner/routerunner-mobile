package com.cs130.routerunner;

/**
 * Created by julianyang on 10/22/15.
 */
public class Settings {
    public static final float PAN_SPEED = 1f;
    public static final float ZOOM_SPEED = 0.00005f;
    public static final float MAX_ZOOM = 0.9f;
    public static final int WORLD_WIDTH = 1984;
    public static final int WORLD_HEIGHT = 2244;
	public static final int VIEW_WIDTH = 800;
	public static final int VIEW_HEIGHT = 450;
    public static final float BUTTON_WIDTH = 380f;
    public static final float BUTTON_HEIGHT = 100f;
    public static final float BUTTON_X = 1780 - BUTTON_WIDTH;
    public static final float BUTTON_Y = 1060 - BUTTON_HEIGHT;
    public static final float NEXT_POINT_RADIUS = 200;

    // Pixel Conversion stuff
    public static final float WORLD_CENTER_LAT = 34.0553f;
    public static final float WORLD_CENTER_LNG = -118.4391f;
    public static final int WORLD_MAP_ZOOM = 16;
    public static final int WORLD_MAP_DPI = 150;
    public static final int WEB_IMAGE_DPI = 72;
    public static final float MAP_TO_WEB_DPI_RATIO = WORLD_MAP_DPI /
            (float) WEB_IMAGE_DPI;

    public static final String SNAP_ROADS_KEY = "AIzaSyAQceKfG7dnkUsnOelOzUfpul5Zi4-FKUg";

    // Actor (truck) default movement speed
    public static final float DEFAULT_MOVEMENT = 0.5f;
    public static final float MISSILE_MOVEMENT = 5.0f;

    //Our "float equals" epsilon
    public static final float EPSILON = 0.1f;

    // PubNub API keys
    public static final String PUBNUB_PUBLISH_KEY = "pub-c-7fd0bf0a-96ef-42eb-8378-a52012ac326a";
    public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-a5a6fc48-79cc-11e5-9720-0619f8945a4f";

    // Server endpoints
    public static final String LOBBY_PREFIX = "routerunner-";
    public static final String ROUTERUNNER_BASE = "http://route-runner-130.appspot.com";
    public static final String MATCHMAKING_URL = "/api/matchmaking/";
    public static final String CREATE_LOBBY_URL = ROUTERUNNER_BASE + MATCHMAKING_URL + "new";

    // Sync frame rate
    public static final int FRAMES_BETWEEN_SYNC = 10;

    // Game Mechanics
    public static final int INITIAL_MONEY = 500;
    public static final int INITIAL_TRUCK_MONEY = 50;
    public static final int BUY_TRUCK_COST = 100;
    public static final int BUY_MISSILE_COST = 200;

    // Spawn points
    public static final int SPAWN_POINT[][] = {{100, 100}, {1510, 1833}};
    public static final String SPAWN_PNG[] = {"factory_blue.png",
            "factory_red.png"};
    // Delivery points
    public static final int DELIVERY_POINT[][] = {{1082, 1143},{1120, 986}};
    public static final String DELIVERY_PNG[] = {
            "house_blue.png", "house_red.png"};

    public static final String TRUCK_PNG[] = {"truck_blue.png", "truck_red" +
            ".png"};

    // Setting flag for synchronized start game
    public static final boolean WAIT_FOR_PLAYERS = false;

    // Message constants
    public static final String PURCHASE_TYPE = "purchase";
    public static final String ROUTE_TYPE = "route";
    public static final String UPDATE_TYPE = "update";
    public static final String TRUCK_ITEM = "truck";
    public static final String MISSILE_ITEM = "missile";

}
