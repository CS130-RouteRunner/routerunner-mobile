package com.cs130.routerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cs130.routerunner.Routes.*;
import com.cs130.routerunner.TapHandler.TapHandler;

/**
 * Created by julianyang on 10/22/15.
 * GameMaster should be the root for all of the actual gameplay.  We will have another screen for
 * logins and whatnot.
 */
public class GameMaster implements Screen{
    private RouteRunner game_;
    private Texture texture;
    private OrthographicCamera cam_;
    private Viewport viewport_;
    private TapHandler tapHandler_;
    private SpriteBatch batch;
    private Sprite mapSprite;

    private Actor truck;
    private Route r;

    private Rectangle base;
    private ShapeRenderer shapeRenderer;

    private boolean currentlyMakingRoute = false;
    private RouteFactory routeFactory;

    private Sprite baseSprite;

    public GameMaster(RouteRunner game) {
        //create self reference
        this.game_ = game;
        //setup touch stuff
        tapHandler_ = new TapHandler(this);
        //setup some map related things
        mapSprite = new Sprite(new Texture(Gdx.files.internal
                ("testmap3.png")));
        mapSprite.setPosition(0,0);
        mapSprite.setSize(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT);
        //setup camera
        cam_ = new OrthographicCamera();
        viewport_ = new StretchViewport(Settings.VIEW_WIDTH,
                Settings.VIEW_HEIGHT,
                cam_);
        cam_.position.set(viewport_.getWorldWidth() / 2,
                viewport_.getWorldHeight() / 2, 0);
        cam_.update();

        //setup batch (drawing mechanism)
        batch = new SpriteBatch();

        //set input
        Gdx.input.setInputProcessor(
                new GestureDetector(new GestureHandler((this))));

        //create first (example) truck
        truck = new Actor(new Sprite(new Texture("bus.png")));
        truck.setX(0f);
        truck.setY(0f);

        //create base sprite and logical box
        baseSprite = new Sprite(new Texture("base.png"));
        base = new Rectangle(500, 500, 500, 500);

        //create routeFactory
        routeFactory = new RouteFactory();
        routeFactory.startCreatingRoute();
    }

    @Override
    public void render(float delta) {
        update(delta);
        cam_.update();
        batch.setProjectionMatrix(cam_.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        mapSprite.draw(batch);
        batch.draw(truck, truck.getX(), truck.getY());
        batch.draw(baseSprite, base.getX(), base.getY());
        batch.end();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        viewport_.update(width, height);
    }

    @Override
    public void dispose() {

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
        if (r != null)
            r.updateTruckPosition();
    }
    public void handleTap(float x, float y, int count) {
        // the user has tapped, and we need to do stuff depending on what
        // mode we're in (ie creating a route)

        //AS OF 10/26/2015 TOUCH IS AS FOLLOWS:
        //you can tap the screen to switch from normal mode to route-edit mode
        //in route edit mode you tap waypoints to create a route, ending in the other base (currently another car)
        //when route creation is finished the car in the bottom left will start moving and you're back in normal mode
        //tap again to draw another route for the car
        tapHandler_.Tap(x, y, count);
    }

    // Navigation related things!
    public void moveCamera(float deltaX, float deltaY) {
        cam_.translate(-deltaX, deltaY);
        clampCameraPan();
    }

    public void zoomCamera(float zoom) {
        cam_.zoom += zoom;
        clampCameraZoom();
    }

    public void clampCameraZoom() {
        cam_.zoom =
                MathUtils.clamp(cam_.zoom, Settings.MAX_ZOOM,
                        Settings.WORLD_WIDTH / cam_.viewportWidth);
        cam_.update();
    }
    public void clampCameraPan() {
        float effectiveViewportWidth = cam_.viewportWidth * cam_.zoom;
        float effectiveViewportHeight = cam_.viewportHeight * cam_.zoom;
        float leftBoundary = effectiveViewportWidth / 2f;
        float rightBoundary = Settings.WORLD_WIDTH - (effectiveViewportWidth /
                2f);
        float bottomBoundary = effectiveViewportHeight / 2f;
        float topBoundary =
                Settings.WORLD_HEIGHT - (effectiveViewportHeight / 2f);
        cam_.position.x =
                MathUtils.clamp(cam_.position.x, leftBoundary, rightBoundary);
        cam_.position.y =
                MathUtils.clamp(cam_.position.y, bottomBoundary, topBoundary);
        cam_.update();
    }

    public boolean mouseMoved (int screenX, int screenY){
        return false;
    }

    public boolean pointInRectangle (Rectangle r, float x, float y) {
        Vector3 rect = new Vector3(r.x, r.y, 0);
        cam_.unproject(rect);

        return rect.x <= x && rect.x + r.width >= x && rect.y <= y && rect.y + r.height >= y;
    }

    public void unproject(Vector3 v){
        cam_.unproject(v);
    }

    public RouteFactory getRouteFactory(){
        return routeFactory;
    }

    public void setRoute(){
        r = routeFactory.getRoute(truck);
    }

    public boolean baseContains(float x, float y){
        return base.contains(x, y);
    }
}
