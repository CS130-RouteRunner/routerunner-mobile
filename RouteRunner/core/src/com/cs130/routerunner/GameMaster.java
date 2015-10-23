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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.TexturePaint;

/**
 * Created by julianyang on 10/22/15.
 * GameMaster should be the root for all of the actual gameplay.  We will have another screen for
 * logins and whatnot.
 */
public class GameMaster implements Screen {
    private RouteRunner game;
    private Texture texture;
    private OrthographicCamera cam;
    private Viewport viewport;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Actor truck;

    public GameMaster(RouteRunner game) {
        //setup some map related things
        this.game = game;
        cam = new OrthographicCamera();
        viewport = new StretchViewport(game.VIEW_WIDTH, game.VIEW_HEIGHT, cam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tile-sample.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        Gdx.input.setInputProcessor(
                new GestureDetector(new TouchHandler((this))));
        truck = new Actor(new Sprite(new Texture("bus.png")));
    }

    @Override
    public void render(float delta) {
        //update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(cam);
        renderer.render();

        renderer.getBatch().begin();
        truck.draw(renderer.getBatch());
        renderer.getBatch().end();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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

    public void moveCamera(float deltaX, float deltaY) {
        cam.translate(-deltaX, deltaY);
        clampCameraPan();
    }

    public void zoomCamera(float zoom) {
        cam.zoom += zoom;
        clampCameraZoom();
    }

    public void clampCameraZoom() {
        cam.zoom =
                MathUtils.clamp(cam.zoom, Settings.MAX_ZOOM,
                        game.WORLD_WIDTH / cam.viewportWidth);
        cam.update();
    }
    public void clampCameraPan() {
        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
        float leftBoundary = effectiveViewportWidth / 2f;
        float rightBoundary = game.WORLD_WIDTH - (effectiveViewportWidth / 2f);
        float bottomBoundary = effectiveViewportHeight / 2f;
        float topBoundary = game.WORLD_HEIGHT - (effectiveViewportHeight / 2f);
        cam.position.x =
                MathUtils.clamp(cam.position.x, leftBoundary, rightBoundary);
        cam.position.y =
                MathUtils.clamp(cam.position.y, bottomBoundary, topBoundary);
        cam.update();
    }
}
