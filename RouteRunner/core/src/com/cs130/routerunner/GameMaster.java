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
import com.badlogic.gdx.utils.viewport.FitViewport;
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
    private OrthographicCamera camera;
    private Viewport viewport;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Actor truck;

    public GameMaster(RouteRunner game) {
        //setup some map related things
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(game.VIEW_WIDTH, game.VIEW_HEIGHT, camera);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tile-sample.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        Gdx.input.setInputProcessor(
                new GestureDetector(new TouchHandler((this))));
        truck = new Actor(new Sprite(new Texture("bus.png")));
    }

    @Override
    public void render(float delta) {
        //update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
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
        camera.translate(-deltaX, deltaY);
        float leftBoundary = camera.viewportWidth / 2f;
        float rightBoundary = game.WORLD_WIDTH - (camera.viewportWidth / 2f);
        float bottomBoundary = camera.viewportHeight / 2f;
        float topBoundary = game.WORLD_HEIGHT - (camera.viewportHeight / 2f);
        camera.position.x =
                MathUtils.clamp(camera.position.x, leftBoundary, rightBoundary);
        camera.position.y =
                MathUtils.clamp(camera.position.y, bottomBoundary, topBoundary);
        camera.update();
    }
}
