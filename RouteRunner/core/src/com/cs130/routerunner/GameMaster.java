package com.cs130.routerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.cs130.routerunner.Actors.Box.Box;
import com.cs130.routerunner.Actors.Box.BoxFactory;
import com.cs130.routerunner.Actors.Box.BoxType;
import com.cs130.routerunner.Actors.Box.ConcreteBoxFactory;
import com.cs130.routerunner.Actors.Box.RandomEvent;
import com.cs130.routerunner.Actors.Missile;
import com.cs130.routerunner.Actors.Truck;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverter;
import com.cs130.routerunner.CoordinateConverter.CoordinateConverterAdapter;
import com.cs130.routerunner.CoordinateConverter.LatLngPoint;
import com.cs130.routerunner.Routes.Route;
import com.cs130.routerunner.SnapToRoads.SnapToRoads;
import com.cs130.routerunner.TapHandler.TapHandler;
import com.badlogic.gdx.math.Vector3;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by julianyang on 10/22/15.
 * GameMaster should be the root for all of the actual gameplay.  We will have another screen for
 * logins and whatnot.
 */
public class GameMaster implements Screen{
    private RouteRunner game_;
    private MapCamera camera_;
    private TapHandler tapHandler_;
    private Skin skin_;
    private Sprite mapSprite_;
    private Stage stage_;
    private ShapeRenderer shapeRenderer_;
    private Player localPlayer_;
    private Player opponentPlayer_;
    private List<Player> players_;
    private ArrayList<Missile> missiles_;
    private ArrayList<RandomEvent> randomEvents_;
    private Batch hudBatch_;
    private Sprite waypointSprite_;
    private ArrayList<Vector3> waypoints_;
    private BoxFactory boxFactory_;
    public SnapToRoads snapToRoads_;
    private MessageCenter messageCenter_;
    private FPSLogger fpsLogger_;

    private int framesSinceLastSync_;
    private int framesSinceLastTryEvent_;

    private int localPlayerNum_;
    private int opponentPlayerNum_;
    private boolean gameOver = false;
    private boolean restartGame = false;
    private boolean showOnce = false;
    private String winner;
    private String loser;
    private BitmapFont font_;

    private CoordinateConverter coordConverter_;

    public GameMaster(RouteRunner game, MessageCenter messageCenter,
                      int localPlayerNum) {
        fpsLogger_ = new FPSLogger();
        framesSinceLastSync_ = 0;
        this.messageCenter_ = messageCenter;

        camera_ = new MapCamera(localPlayerNum);

        coordConverter_ = new CoordinateConverterAdapter();
        snapToRoads_ = new SnapToRoads();
        //create self reference
        this.game_ = game;
        //setup touch stuff
        tapHandler_ = new TapHandler(this);

        //setup skin
        skin_ = new Skin(Gdx.files.internal("uiskin.json"));
        skin_.getFont("default-font").getData().setScale(2.00f, 2.00f);


        //setup some map related things
        mapSprite_ = new Sprite(new Texture(Gdx.files.internal
                ("westwood_map2.png")));
        mapSprite_.setPosition(0,0);
        mapSprite_.setSize(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT);

        //setup batch (drawing mechanism)
        //batch_ = new SpriteBatch();
        stage_ = new Stage();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(0, stage_);
        inputMultiplexer.addProcessor(1, new GestureDetector(new GestureHandler((this))));
        Gdx.input.setInputProcessor(inputMultiplexer);

        //create the "dock" (PlayerButtonInfo)
        PlayerButtonInfo playerButtonInfo = new PlayerButtonInfo(stage_, tapHandler_);
        playerButtonInfo.display();

        boxFactory_ = new ConcreteBoxFactory(snapToRoads_);
        randomEvents_ = new ArrayList<RandomEvent>();

        localPlayer_ = new Player(Settings.INITIAL_MONEY, localPlayerNum,
                boxFactory_);
        this.localPlayerNum_ = localPlayerNum;
        localPlayer_.setPlayerButtonInfo(playerButtonInfo);
        this.opponentPlayerNum_ = 1 - localPlayerNum;
        opponentPlayer_ = new Player(Settings.INITIAL_MONEY,
                opponentPlayerNum_, boxFactory_);
        players_ = new ArrayList<Player>();
        players_.add(localPlayer_);
        players_.add(opponentPlayer_);

        //setup missile arraylist
        missiles_ = new ArrayList<Missile>();

        //create waypoint sprites
        waypointSprite_ = new Sprite(new Texture("waypoint2.png"));
        waypoints_ = new ArrayList<Vector3>();

        font_ = new BitmapFont();
        font_.getData().setScale(5f, 5f);
        hudBatch_ = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
	if(Settings.LOG_FPS) {
            fpsLogger_.log();
        }

        //exit the game
        if(gameOver) {
            // Construct POST request
            HttpRequest post = new HttpRequest(Net.HttpMethods.POST);
            post.setUrl(Settings.END_GAME_URL);
            post.setHeader("Content-Type", "application/json");

            JSONObject content = new JSONObject();
            content.put("uid", messageCenter_.getUUID());
            content.put("lid", messageCenter_.getChannel());
            if (!winner.isEmpty()) {
                content.put("winner", winner);
            } else if (!loser.isEmpty()) {
                content.put("loser", loser);
            }
            post.setContent(content.toString());

            // Send it
            Gdx.net.sendHttpRequest(post, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {}

                @Override
                public void failed(Throwable t) {}

                @Override
                public void cancelled() {}
            });

            gameOver = false;
            Gdx.app.exit();
            return;
        }

        //logic for restarting game here
        if(restartGame){
            restartGame = false;
            showOnce = false;
            showAlert("A new game is starting.");
            localPlayer_.restartPlayer();
            opponentPlayer_.restartPlayer();
        }

        if(localPlayer_.getMoney() >= Settings.TARGET_MONEY && !showOnce){
            winner = messageCenter_.getUUID();
            showOnce = true;
            gameOverAlert("You have won the game!");
        }

        if(opponentPlayer_.getMoney() >= Settings.TARGET_MONEY && !showOnce){
            loser = messageCenter_.getUUID();
            showOnce = true;
            gameOverAlert("Your opponent has won the game.");
        }

        update(delta);
        camera_.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage_.getBatch().setProjectionMatrix(camera_.getCamera().combined);
        // need to match shaperender's projection matrix with spritebatch's
        shapeRenderer_.setProjectionMatrix(stage_.getBatch().getProjectionMatrix());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage_.getBatch().begin();
        mapSprite_.draw(stage_.getBatch());
        stage_.getBatch().end();

        Vector3 previous = null;
        if (!waypoints_.isEmpty())
            previous = waypoints_.get(0);

        Gdx.gl.glLineWidth(20);
        for (Vector3 waypoint : waypoints_) {
            shapeRenderer_.setColor(Color.FOREST);
            shapeRenderer_.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer_.rectLine(previous.x, previous.y, waypoint.x, waypoint.y, 15);
            shapeRenderer_.end();

            shapeRenderer_.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer_.circle(waypoint.x, waypoint.y, 7);
            shapeRenderer_.end();

            previous = waypoint;
        }

        stage_.getBatch().begin();

        for (Player player : players_) {
            Box spawnPoint = player.getSpawnPoint();
            drawSpriteCentered(spawnPoint.getSprite(), spawnPoint.getX(),
                    spawnPoint.getY());

            Box deliveryPoint = player.getDeliveryPoint();
            drawSpriteCentered(deliveryPoint.getSprite(), deliveryPoint.getX(),
                    deliveryPoint.getY());

            for (Truck truck : player.getTruckList()) {
                if (!truck.getTombStoned()) {
                    if (truck.getUpgraded()) {
                        String truckpng = Settings.TRUCK_PNG[localPlayerNum_ + 2];
                        truck.setTexture(new Texture(truckpng));
                    }
                    drawSpriteCentered(truck, truck.getX(), truck.getY());
                }
            }
        }
        for (Missile missile: missiles_) {
            drawSpriteCentered(missile, missile.getX(), missile.getY());
        }

        for (RandomEvent randomEvent : randomEvents_) {
            drawSpriteCentered(randomEvent.getSprite(), randomEvent.getX(), randomEvent
                    .getY());
        }

        //TODO: rlau (draw opponent money)
        stage_.getBatch().end();

        //draw how much money the player has
        hudBatch_.begin();
        font_.draw(hudBatch_, "Money: " + localPlayer_.getMoney(), 40, 1020);
        hudBatch_.end();

        if(!waypoints_.isEmpty())
            showRadius(shapeRenderer_, waypoints_.get(waypoints_.size() - 1));

        stage_.draw();
    }

    @Override
    public void show() {
        shapeRenderer_ = new ShapeRenderer();
    }

    @Override
    public void resize(int width, int height) {
        camera_.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        shapeRenderer_.dispose();
        stage_.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    // the main game logic goes here
    public void update(float delta) {
        // detect collisions for actors
        if (framesSinceLastSync_ % Settings.FRAMES_BETWEEN_SYNC == 0) {
            syncGame();
            framesSinceLastSync_ = 0;
        }
        framesSinceLastSync_++;

        if (localPlayerNum_ == 0 &&
            framesSinceLastTryEvent_ % Settings.FRAMES_BETWEEN_TRY_EVENT == 0 &&
                randomEvents_.size() < Settings.RANDOM_EVENT_MAXCOUNT) {
            if (MathUtils.randomBoolean(Settings.RANDOM_EVENT_PROBABILITY)) {
                RandomEvent randomEvent = (RandomEvent) boxFactory_.createBox(
                        BoxType.RandomEvent, localPlayerNum_);
                randomEvents_.add(randomEvent);
                Gdx.app.log("RandomEvent", "Created Random Event at " +
                        randomEvent.getX() + " " + randomEvent.getY());
            }
            framesSinceLastTryEvent_ = 0;
        }

        for (Truck truck: localPlayer_.getTruckList()) {
            Gdx.app.debug("GameMaster", "Calling update on truck");
            truck.update();
            Iterator<RandomEvent> iter = randomEvents_.iterator();
            while(iter.hasNext()){
                RandomEvent randomEvent = iter.next();
                if(truck.checkIntersectingRandomEvent(randomEvent))
                    iter.remove();
            }

        }

        for (Truck truck: opponentPlayer_.getTruckList()) {
            Gdx.app.debug("GameMaster", "Calling update on opponent truck");
            truck.update();
        }

        for (Missile missile: missiles_) {
            Gdx.app.debug("GameMaster", "Calling update on missile");
            missile.update();
            if (missile.getTargetTruck()!= null) {
                float tolerance = Settings.MISSILE_MOVEMENT / Settings.EPSILON * Gdx.graphics.getDeltaTime();
                if (Math.abs(missile.getX() - missile.getTargetTruck().getX()) < tolerance
                        && Math.abs(missile.getY() - missile.getTargetTruck().getY()) < tolerance) {
                    // Prepare missile message
                    JSONObject data = new JSONObject();
                    data.put("item", Settings.MISSILE_ITEM);
                    Truck target = missile.getTargetTruck();
                    ArrayList<Truck> trucks = opponentPlayer_.getTruckList();
                    int truckId = trucks.indexOf(target);
                    data.put("id", truckId);
                    Gdx.app.log("TargetTruckTag", "selected truck:" + Integer.toString(truckId));

                    // Send it
                    Message toSend = messageCenter_.createPurchaseMessage(messageCenter_.getUUID(), data);
                    messageCenter_.sendMessage(toSend);

                    // Cleanup
                    missile.getTargetTruck().setTombStoned_(true);
                    missiles_.remove(missile);
                }
            }
        }


    }

    public void handleTap(float x, float y, int count) {
        // adjust android coordinates to libgdx coordinates
        Gdx.app.log("GMTag", x + " " + y + "\n");
        Vector3 touchPos = new Vector3();
        touchPos.set(x, y, 0);
        camera_.unproject(touchPos);
        Gdx.app.log("GMTag", "transformed: " + touchPos.x + ", " + touchPos.y);

        // the user has tapped, and we need to do stuff depending on what
        // mode we're in (ie creating a route)

        //AS OF 10/26/2015 TOUCH IS AS FOLLOWS:
        //you can tap the screen to switch from normal mode to route-edit mode
        //in route edit mode you tap waypoints to create a route, ending in the other base (currently another car)
        //when route creation is finished the car in the bottom left will start moving and you're back in normal mode
        //tap again to draw another route for the car

        tapHandler_.Tap(touchPos.x, touchPos.y, count);
    }

    public void syncGame() {
        List<Message> result = messageCenter_.getMessages(messageCenter_.getLastSyncTime());
        // Gdx.app.log("LastSyncTag", Long.toString(messageCenter_.getLastSyncTime()));
        if (result != null && !result.isEmpty()) {
            //Gdx.app.log("MessageSizeTag", String.valueOf(result.size()));
            for (Message m : result) {
                Gdx.app.log("MessageTag", m.toString());
                if (m.getType().equals(Settings.PURCHASE_TYPE)) {
                    // If opponent bought a truck
                    if (m.getItem().equals(Settings.TRUCK_ITEM)) {
                        String truckPng = Settings.TRUCK_PNG[opponentPlayerNum_];
                        Truck truck = new Truck(new Sprite(new Texture(truckPng)),
                                stage_, tapHandler_, opponentPlayer_);
                        truck.setX(opponentPlayer_.getSpawnPoint().getX());
                        truck.setY(opponentPlayer_.getSpawnPoint().getY());

                        opponentPlayer_.addTruck(truck);
                    }
                    // If opponnent bought a missile
                    else if (m.getItem().equals(Settings.MISSILE_ITEM)) {
                        int truckID = m.getItemId();
                        Truck target = localPlayer_.getTruckList().get(truckID);
                        target.setTombStoned_(true);
                        Gdx.app.log("MessageTag", String.valueOf(truckID));
                        showAlert("Your truck has been destroyed by a missile!");
                    }
                    // If opponent upgraded a truck
                    else if(m.getItem().equals(Settings.UPGRADE_ITEM)) {
                        int truckID = m.getItemId();
                        Truck truckToUpgrade = opponentPlayer_.getTruckList().get(truckID);
                        truckToUpgrade.upgrade();
                        Gdx.app.log("UpgradeMessageTag", String.valueOf(truckID));
                    }

                }
                // Opponent set a route for his truck
                else if (m.getType().equals(Settings.ROUTE_TYPE)) {
                    Truck truck = opponentPlayer_.getTruckList().get(m.getItemId());
                    List<LatLngPoint> points = m.getCoords();
                    Route r = new Route();

                    // Add the points for the route
                    if (points != null && !points.isEmpty()) {
                        for (LatLngPoint p : points) {
                            // Gdx.app.log("MessageRoute", p.toString());
                            Vector3 v = coordConverter_.ll2px(p.lat, p.lng);
                            r.addWayPoint(v.x, v.y);
                        }
                    }

                    // Set the route and unpause the truck
                    truck.setRoute(r);
                    truck.setPaused(false);
                }
                // Synchronization messages
                else if (m.getType().equals(Settings.UPDATE_TYPE)) {
                    // If it is a truck pause
                    Gdx.app.log("SyncTag", "Update: " + m.toString());
//                    if (m.getStatus().equals(Settings.PAUSE_STATUS)) {
                    int truckID = m.getItemId();
                    Truck target = opponentPlayer_.getTruckList().get(truckID);
                    target.setPaused(true);
                    Gdx.app.log("TruckPause", String.valueOf(truckID));
//                    }
                }
            }
        }
    }

    public boolean mouseMoved (int screenX, int screenY){
        return false;
    }

    public MapCamera getCamera(){
        return camera_;
    }

    public ArrayList<Truck> getTrucks() { return localPlayer_.getTruckList(); }

    public ArrayList<Truck> getOpponentTrucks() {return opponentPlayer_.getTruckList(); }

    public ArrayList<Missile> getMissiles() { return missiles_; }

    //TODO(juliany): clean this up to use routes or soemthing.
    public void setWaypoints(ArrayList<Vector3> waypoints) {
        this.waypoints_ = new ArrayList<Vector3>(waypoints);
    }

    public void addWaypoint(Vector3 waypoint) {
        this.waypoints_.add(waypoint);
    }

    public void clearWaypoints() {
        this.waypoints_.clear();
    }

    //draws using the stage batch but automatically centers all sprites
    //if just drawing a point or regular texture, using stage_.getBatch().draw() is fine
    public void drawSpriteCentered (Sprite sprite, float x, float y){
        stage_.getBatch().draw(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
    }

    public PlayerButtonInfo getLocalPlayerButtonInfo(){
        return localPlayer_.getPlayerButtonInfo();
    }

    public boolean upgradeTruck(){
        if(localPlayer_.getMoney() >= Settings.TRUCK_UPGRADE_COST) {
            localPlayer_.subtractMoney(Settings.TRUCK_UPGRADE_COST);
            return true;
        }else
            return false;
    }

    public boolean buyTruck() {
        //check if we can afford
        if (localPlayer_.getMoney() >= Settings.BUY_TRUCK_COST) {
            localPlayer_.subtractMoney(Settings.BUY_TRUCK_COST);
            String truckPng = Settings.TRUCK_PNG[localPlayerNum_];
            Truck truck = new Truck(new Sprite(new Texture(truckPng)), stage_,
                    tapHandler_, Settings.INITIAL_TRUCK_MONEY, localPlayer_);
            truck.setX(localPlayer_.getSpawnPoint().getX());
            truck.setY(localPlayer_.getSpawnPoint().getY());
            Gdx.app.log("BoughtTruck", "Bought truck! Now: " + localPlayer_.getTruckList().size() + " trucks!");
            // Send message to other client
            JSONObject payload = new JSONObject();
            payload.put("item", Settings.TRUCK_ITEM);
            payload.put("id", localPlayer_.getTruckID());
            localPlayer_.incTruckID_();
            localPlayer_.addTruck(truck);
            Message toSend = messageCenter_.createPurchaseMessage(messageCenter_.getUUID(), payload);
            messageCenter_.sendMessage(toSend);
            return true;
        }
        else
            return false;
    }

    public Missile buyMissile(){
        Missile missile = new Missile(new Sprite(new Texture("missile.png")), stage_, tapHandler_);
        missile.setX(localPlayer_.getSpawnPoint().getX());
        missile.setY(localPlayer_.getSpawnPoint().getY());
        missiles_.add(missile);
        Gdx.app.log("BoughtMissile", "Bought Missile!");
        return missile;
    }

    public Player getLocalPlayer(){
        return localPlayer_;
    }

    public Player getOpponentPlayer(){
        return opponentPlayer_;
    }


    public void showRadius(ShapeRenderer shapeRenderer, Vector3 point){
        //Gdx.app.log("GMshowRadius", "Enter showRadius()\n");
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(0, 1, 0, 0.2f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(point.x, point.y, Settings.NEXT_POINT_RADIUS);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public MessageCenter getMessageCenter() {
        return this.messageCenter_;
    }

    public ArrayList<Vector3> getWayPoints() {
        return this.waypoints_;

    }

    public void showAlert(String alertString){
        Label label = new Label(alertString, skin_);
        label.setWrap(true);
        label.setFontScale(1.6f);
        label.setAlignment(Align.center);

        final Dialog dialog = new Dialog("", skin_) {
            @Override
            public float getPrefWidth() { return 600f; }

            @Override
            public float getPrefHeight() { return 200f; }
        };
        dialog.getContentTable().add(label);

        TextButton dbutton = new TextButton("OK", skin_);
        dbutton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                dialog.remove();
            }
        });
        dialog.button(dbutton, true);
        dialog.invalidateHierarchy();
        dialog.invalidate();
        dialog.layout();
        dialog.show(stage_);
    }

    public void gameOverAlert(String alertString){
        Label label = new Label(alertString, skin_);
        label.setWrap(true);
        label.setFontScale(1.6f);
        label.setAlignment(Align.center);

        final Dialog dialog = new Dialog("", skin_) {
            @Override
            public float getPrefWidth() { return 600f; }

            @Override
            public float getPrefHeight() { return 200f; }
        };
        dialog.getContentTable().add(label);

        TextButton restartButton = new TextButton("Restart", skin_);
        restartButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                restartGame = true;
                dialog.remove();
            }
        });

        TextButton quitButton = new TextButton("Quit", skin_);
        quitButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                gameOver = true;
                dialog.remove();
            }
        });
        dialog.button(restartButton, true);
        dialog.button(quitButton, true);
        dialog.invalidateHierarchy();
        dialog.invalidate();
        dialog.layout();
        dialog.show(stage_);
    }
}
