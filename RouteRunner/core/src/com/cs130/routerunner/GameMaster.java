package com.cs130.routerunner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private RouteRunner game;
    private Texture texture;
    private OrthographicCamera camera;
    private Viewport viewport;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Actor truck;
    private Route r;

    private Rectangle base;
    private ShapeRenderer shapeRenderer;

    private boolean currentlyMakingRoute = false;
    private RouteFactory routeFactory;

    private Sprite baseSprite;

    public GameMaster(RouteRunner game) {
        Gdx.input.setInputProcessor(this);

        this.game = game;
        //texture = new Texture("tiles.png");
        camera = new OrthographicCamera();
        viewport = new StretchViewport(game.VIEW_WIDTH, game.VIEW_HEIGHT, camera);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tile-sample.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        shapeRenderer = new ShapeRenderer();

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

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
        //update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();
        //truck.draw(renderer.getBatch());
        renderer.getBatch().draw(truck, truck.getX(), truck.getY());

        renderer.getBatch().draw(baseSprite, base.getX(), base.getY());
        renderer.getBatch().end();



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

    //input processing functions
    @Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3();
        touchPos.set(screenX, screenY, 0);
        camera.unproject(touchPos);

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
        camera.unproject(rect);

        return rect.x <= x && rect.x + r.width >= x && rect.y <= y && rect.y + r.height >= y;
    }
}
