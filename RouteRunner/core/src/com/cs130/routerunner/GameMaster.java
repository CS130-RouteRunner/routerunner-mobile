package com.cs130.routerunner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by julianyang on 10/22/15.
 * GameMaster should be the root for all of the actual gameplay.  We will have another screen for
 * logins and whatnot.
 */
public class GameMaster implements Screen {
    private RouteRunner game_;
    private OrthographicCamera cam_;
    private Viewport viewport_;
    private TmxMapLoader mapLoader_;
    private TiledMap map_;
    private OrthogonalTiledMapRenderer renderer_;

    private Actor truck;

    public GameMaster(RouteRunner game) {
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
        renderer_.getBatch().end();

        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam_.unproject(touchPos);
            truck.setMoveTo(touchPos.x - 64/2, touchPos.y);
        }

        truck.update(0.5f);
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
}
