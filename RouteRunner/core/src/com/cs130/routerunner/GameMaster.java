package com.cs130.routerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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

        //setup touch stuff
        tapHandler_ = new TapHandler(this);

        //setup some map related things
        this.game_ = game;

        mapSprite = new Sprite(new Texture(Gdx.files.internal
                ("1280x1280westwood2x.png")));
        mapSprite.setPosition(0,0);
        mapSprite.setSize(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT);

        cam_ = new OrthographicCamera();
        viewport_ = new StretchViewport(Settings.VIEW_WIDTH,
                Settings.VIEW_HEIGHT,
                cam_);


        cam_.position.set(viewport_.getWorldWidth() / 2,
                viewport_.getWorldHeight() / 2, 0);
        cam_.update();

        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(
                new GestureDetector(new GestureHandler((this))));
        truck = new Actor(new Sprite(new Texture("bus.png")));
        truck.setX(0f);
        truck.setY(0f);

        baseSprite = new Sprite(new Texture("bus.png"));

        //r = new Route(truck);

        base = new Rectangle(500, 500, 500, 500);

//          test one
//        Vector3 v0 = new Vector3(0f, 0f, 0f);
//        Vector3 v1 = new Vector3(0f, 100f, 0f);
//        Vector3 v2 = new Vector3(100f, 100f, 0f);
//        Vector3 v3 = new Vector3(100f, 200f, 0f);
//        Vector3 v4 = new Vector3(1000f, 200f, 0f);
//        Vector3 v5 = new Vector3(0f, 200f, 0f);
//        ArrayList<Vector3> wp = new ArrayList<Vector3>();
//        wp.add(v0);
//        wp.add(v1);
//        wp.add(v2);
//        wp.add(v3);
//        wp.add(v4);
//        wp.add(v5);
//        r.setWayPoints(wp);

        routeFactory = new RouteFactory();

        //test two
        routeFactory.startCreatingRoute();
//        routeFactory.addWayPoint(0f, 0f);
//        routeFactory.addWayPoint(200f, 0f);
//        routeFactory.addWayPoint(200f, 200f);
//        r = routeFactory.getRoute(truck);

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
