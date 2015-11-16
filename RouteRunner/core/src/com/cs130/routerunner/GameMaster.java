package com.cs130.routerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Routes.*;
import com.cs130.routerunner.TapHandler.TapHandler;
import com.badlogic.gdx.math.Vector3;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
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
    private Sprite mapSprite_;
    private Stage stage_;
    private ShapeRenderer shapeRenderer_;
    private Player localPlayer_;
    private Player opponentPlayer_;

    private PlayerButtonInfo playerButtonInfo_;

    private ArrayList<Actor> trucks_;
    private ArrayList<Missile> missiles_;
    private Rectangle base_;
    private Sprite baseSprite_;

    private Sprite waypointSprite_;
    private ArrayList<Vector3> waypoints_;

    private MessageCenter messageCenter_;

    private int framesSinceLastSync_;

    public GameMaster(RouteRunner game, MessageCenter messageCenter) {
        framesSinceLastSync_ = 0;
        this.messageCenter_ = messageCenter;

        camera_ = new MapCamera();

        //create self reference
        this.game_ = game;
        //setup touch stuff
        tapHandler_ = new TapHandler(this);

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

        localPlayer_ = new Player(Settings.INITIAL_MONEY);
        localPlayer_.setPlayerButtonInfo(playerButtonInfo);
        opponentPlayer_ = new Player(Settings.INITIAL_MONEY);

        //setup missile arraylist
        missiles_ = new ArrayList<Missile>();

        //setup missile arraylist
        missiles_ = new ArrayList<Missile>();

        //create first (example) truck

        Actor truck = new Actor(new Sprite(new Texture("bus.png")), stage_, tapHandler_, 50);
        truck.setX(35f);
        truck.setY(35f);

        trucks_.add(truck);

        //create base sprite and logical box
        baseSprite_ = new Sprite(new Texture("base.png"));
        base_ = new Rectangle(500, 500, 500, 500);

        //create waypoint sprites
        waypointSprite_ = new Sprite(new Texture("waypoint2.png"));
        waypoints_ = new ArrayList<Vector3>();

        //create the "dock" (PlayerButtonInfo)
        playerButtonInfo_ = new PlayerButtonInfo(stage_, tapHandler_);
        playerButtonInfo_.display();

        localPlayer_ = new Player(Settings.INITIAL_MONEY);
        opponentPlayer_ = new Player(Settings.INITIAL_MONEY);
    }

    @Override
    public void render(float delta) {
        update(delta);
        camera_.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage_.getBatch().setProjectionMatrix(camera_.getCamera().combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage_.getBatch().begin();
        mapSprite_.draw(stage_.getBatch());
        for (Actor truck: localPlayer_.getTruckList()) {
            drawSpriteCentered(truck, truck.getX(), truck.getY());
        }

        for (Missile missile: missiles_) {
            drawSpriteCentered(missile, missile.getX(), missile.getY());
        }

//        for (Vector3 waypoint: waypoints_) {
//            drawSpriteCentered(waypointSprite_, waypoint.x, waypoint.y);
//        }

        drawSpriteCentered(baseSprite_, base_.getX(), base_.getY());
        stage_.getBatch().end();

        // need to match shaperender's projection matrix with spritebatch's
        shapeRenderer_.setProjectionMatrix(stage_.getBatch().getProjectionMatrix());

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

        for (Actor truck: localPlayer_.getTruckList()) {
            Gdx.app.debug("GameMaster", "Calling update on truck");
            truck.update();
        }
        for (Missile missile: missiles_) {
            Gdx.app.debug("GameMaster", "Calling update on missile");
            missile.update();
            if (missile.getTargetTruck()!= null) {
                float tolerance = Settings.MISSILE_MOVEMENT / Settings.EPSILON * Gdx.graphics.getDeltaTime();
                if (Math.abs(missile.getX() - missile.getTargetTruck().getX()) < tolerance
                         && Math.abs(missile.getY() - missile.getTargetTruck().getY()) < tolerance) {
                    localPlayer_.getTruckList().remove(missile.getTargetTruck());
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
        // TEST FOR PUBNUB HELPER
        JSONObject dummy = new JSONObject();
        dummy.put("type", "purchase");
        dummy.put("uid", messageCenter_.getUUID());
        JSONObject payload = new JSONObject();
        payload.put("item", "truck");
        Message toSend = messageCenter_.createPurchaseMessage("12345",
                payload);
        messageCenter_.sendMessage(toSend);

        long now = (new Date().getTime() - (2*60*1000)) * 10000;
        List<Message> result = messageCenter_.getMessages(now);
        if (result != null) {
            Gdx.app.log("MessageSizeTag", String.valueOf(result.size()));
            for (Message m : result) {
                Gdx.app.log("MessageTag", m.toString());
            }
        }
        Gdx.app.log("LastSyncTag", Long.toString(messageCenter_.getLastSyncTime()));
    }

    public boolean mouseMoved (int screenX, int screenY){
        return false;
    }

    public MapCamera getCamera(){
        return camera_;
    }

    public ArrayList<Actor> getTrucks() { return localPlayer_.getTruckList(); }

    public ArrayList<Missile> getMissiles() { return missiles_; }


    //TODO(juliany): clean this up to use routes or soemthing.
    public void setWaypoints(ArrayList<Vector3> waypoints) {
        this.waypoints_ = new ArrayList<Vector3>(waypoints);
    }

    public void addWaypoint(Vector3 waypoint) {
        this.waypoints_.add(waypoint);
    }

    public boolean baseContains(float x, float y){
        return base_.contains(x, y);
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
    public boolean buyTruck() {
        //check if we can afford
        if (localPlayer_.getMoney() >= Settings.BUY_TRUCK_COST) {
            Actor truck = new Actor(new Sprite(new Texture("bus.png")), stage_, tapHandler_, Settings.INITIAL_TRUCK_MONEY);
            truck.setX(35f);
            truck.setY(35f);
            localPlayer_.addTruck(truck);
            Gdx.app.log("BoughtTruck", "Bought truck! Now: " + localPlayer_.getTruckList().size() + " trucks!");
            return true;
        } else
            return false;
    }

    public Missile buyMissile(){
        Missile missile = new Missile(new Sprite(new Texture("missile.png")), stage_, tapHandler_);
        missile.setX(35f);
        missile.setY(35f);
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
}
