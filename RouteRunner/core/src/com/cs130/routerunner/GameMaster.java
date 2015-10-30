package com.cs130.routerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cs130.routerunner.Routes.*;
import com.cs130.routerunner.TapHandler.TapHandler;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by julianyang on 10/22/15.
 * GameMaster should be the root for all of the actual gameplay.  We will have another screen for
 * logins and whatnot.
 */
public class GameMaster implements Screen{
    private RouteRunner game_;
    private MapCamera camera_;
    private TapHandler tapHandler_;
    private SpriteBatch batch_;
    private Sprite mapSprite_;
    private Stage stage_;

    private ArrayList<Actor> trucks_;
    private Route route_;
    private Rectangle base_;
    private RouteFactory routeFactory_;
    private Sprite baseSprite_;

    private Sprite waypointSprite_;
    private ArrayList<Vector3> waypoints_;

    public GameMaster(RouteRunner game) {

        camera_ = new MapCamera();


        //create self reference
        this.game_ = game;
        //setup touch stuff
        tapHandler_ = new TapHandler(this);

        //setup some map related things
        mapSprite_ = new Sprite(new Texture(Gdx.files.internal
                ("testmap3.png")));
        mapSprite_.setPosition(0,0);
        mapSprite_.setSize(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT);

        //setup batch (drawing mechanism)
        //batch_ = new SpriteBatch();
        stage_ = new Stage();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(0, stage_);
        inputMultiplexer.addProcessor(1, new GestureDetector(new GestureHandler((this))));
        Gdx.input.setInputProcessor(inputMultiplexer);

        // TODO(rlau): create actor creation method
        trucks_ = new ArrayList<Actor>();

        //create first (example) truck
        Actor truck = new Actor(new Sprite(new Texture("bus.png")), stage_, tapHandler_);
        truck.setX(0f);
        truck.setY(0f);
        trucks_.add(truck);

        //create base sprite and logical box
        baseSprite_ = new Sprite(new Texture("base.png"));
        base_ = new Rectangle(500, 500, 500, 500);

        waypointSprite_ = new Sprite(new Texture("waypoint2.png"));
        waypoints_ = new ArrayList<Vector3>();

        //create routeFactory
        routeFactory_ = new RouteFactory();
        routeFactory_.startCreatingRoute();
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
        for (Actor truck: trucks_) {
            stage_.getBatch().draw(truck, truck.getX(), truck.getY());
        }

        for (Vector3 waypoint: waypoints_) {
            stage_.getBatch().draw(waypointSprite_, waypoint.x, waypoint.y);
        }

        stage_.getBatch().draw(baseSprite_, base_.getX(), base_.getY());
        stage_.getBatch().end();

        stage_.draw();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        camera_.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
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
        for (Actor truck: trucks_) {
            truck.moveAlongRoute();
        }

    }
    public void handleTap(float x, float y, int count) {
        // adjust android coordinates to libgdx coordinates
        Gdx.app.log("GMTag", x + " " + y + "\n");
        Vector3 touchPos = new Vector3();
        touchPos.set(x, y, 0);
        camera_.unproject(touchPos);
        Gdx.app.log("GMTag", "transformed: " + touchPos.x + ", "+  touchPos.y);

        // the user has tapped, and we need to do stuff depending on what
        // mode we're in (ie creating a route)

        //AS OF 10/26/2015 TOUCH IS AS FOLLOWS:
        //you can tap the screen to switch from normal mode to route-edit mode
        //in route edit mode you tap waypoints to create a route, ending in the other base (currently another car)
        //when route creation is finished the car in the bottom left will start moving and you're back in normal mode
        //tap again to draw another route for the car

        tapHandler_.Tap(touchPos.x, touchPos.y, count);
    }

    public boolean mouseMoved (int screenX, int screenY){
        return false;
    }

    public MapCamera getCamera(){
        return camera_;
    }

    public ArrayList<Actor> getTrucks() { return trucks_; }

    public RouteFactory getRouteFactory(){
        return routeFactory_;
    }

    public void setRoute(){
        //TODO: (rlau) change this method to set the route of a specific truck, not just all of em
        for (Actor truck: trucks_) {
            truck.setRoute(routeFactory_.getRoute());
        }
    }

    //TODO(juliany): clean this up to use routes or soemthing.
    public void setWaypoints(ArrayList<Vector3> waypoints) {
        this.waypoints_ = new ArrayList<Vector3>(waypoints);
    }

    public boolean baseContains(float x, float y){
        return base_.contains(x, y);
    }

    public void clearWaypoints() {
        this.waypoints_.clear();
    }
}
