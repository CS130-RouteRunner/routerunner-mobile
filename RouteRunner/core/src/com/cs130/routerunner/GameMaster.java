package com.cs130.routerunner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.awt.TexturePaint;
import java.util.ArrayList;

/**
 * Created by julianyang on 10/22/15.
 * GameMaster should be the root for all of the actual gameplay.  We will have another screen for
 * logins and whatnot.
 */
public class GameMaster implements Screen, InputProcessor {
    private RouteRunner game_;
    private Texture texture;
    private OrthographicCamera cam_;
    private Viewport viewport_;
    private TmxMapLoader mapLoader_;
    private TiledMap map_;
    private OrthogonalTiledMapRenderer renderer_;

    private Actor truck;
    private Route r;

    private Rectangle base;
    private ShapeRenderer shapeRenderer;

    private boolean currentlyMakingRoute = false;
    private RouteFactory routeFactory;

    private Sprite baseSprite;

    public GameMaster(RouteRunner game) {
        Gdx.input.setInputProcessor(this);

        //setup some map related things
        this.game_ = game;
        cam_ = new OrthographicCamera();
        viewport_ = new StretchViewport(game.VIEW_WIDTH, game_.VIEW_HEIGHT,
                cam_);

        mapLoader_ = new TmxMapLoader();
        map_ = mapLoader_.load("tile-sample.tmx");
        renderer_ = new OrthogonalTiledMapRenderer(map_);

        cam_.position.set(viewport_.getWorldWidth() / 2,
                viewport_.getWorldHeight() / 2, 0);

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

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer_.setView(cam_);
        renderer_.render();


        renderer_.getBatch().begin();
        //truck.draw(renderer.getBatch());

        renderer_.getBatch().draw(truck, truck.getX(), truck.getY());
        renderer_.getBatch().draw(baseSprite, base.getX(), base.getY());
        renderer_.getBatch().end();



//        if(Gdx.input.isTouched()) {
//            Vector3 touchPos = new Vector3();
//            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//            camera.unproject(touchPos);
//            truck.setMoveTo(touchPos.x - 64/2, touchPos.y);
//            //TESTING OF ROUTES
//        }

        //truck.update(0.5f);
        if (r != null)
            r.updateTruckPosition();
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
    }
    public void handleTap(float x, float y, int count) {
        // the user has tapped, and we need to do stuff depending on what
        // mode we're in (ie creating a route)
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
                        game_.WORLD_WIDTH / cam_.viewportWidth);
        cam_.update();
    }
    public void clampCameraPan() {
        float effectiveViewportWidth = cam_.viewportWidth * cam_.zoom;
        float effectiveViewportHeight = cam_.viewportHeight * cam_.zoom;
        float leftBoundary = effectiveViewportWidth / 2f;
        float rightBoundary = game_.WORLD_WIDTH - (effectiveViewportWidth / 2f);
        float bottomBoundary = effectiveViewportHeight / 2f;
        float topBoundary = game_.WORLD_HEIGHT - (effectiveViewportHeight / 2f);
        cam_.position.x =
                MathUtils.clamp(cam_.position.x, leftBoundary, rightBoundary);
        cam_.position.y =
                MathUtils.clamp(cam_.position.y, bottomBoundary, topBoundary);
        cam_.update();
    }
    //input processing functions
    @Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3();
        touchPos.set(screenX, screenY, 0);
        cam_.unproject(touchPos);

        if (currentlyMakingRoute == false){
            currentlyMakingRoute = true;
            routeFactory.startCreatingRoute();
        }
        else {
            routeFactory.addWayPoint(touchPos.x, touchPos.y);
            if(base.contains(touchPos.x, touchPos.y)){
                currentlyMakingRoute = false;
                r = routeFactory.getRoute(truck);
            }
        }

        return true;
    }

    @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
        return false;
    }

    @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override public boolean keyDown (int keycode) {
        return false;
    }

    @Override public boolean keyUp (int keycode) {
        return false;
    }

    @Override public boolean keyTyped (char character) {
        return false;
    }

    @Override public boolean scrolled (int amount) {
        return false;
    }

    public boolean mouseMoved (int screenX, int screenY){
        return false;
    }

    public boolean pointInRectangle (Rectangle r, float x, float y) {
        Vector3 rect = new Vector3(r.x, r.y, 0);
        cam_.unproject(rect);

        return rect.x <= x && rect.x + r.width >= x && rect.y <= y && rect.y + r.height >= y;
    }
}
