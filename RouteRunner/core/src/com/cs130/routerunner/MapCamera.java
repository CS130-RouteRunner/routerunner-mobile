package com.cs130.routerunner;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Patrick on 10/27/2015.
 */
public class MapCamera {
    private OrthographicCamera cam_;
    private Viewport viewport_;

    public MapCamera(){
        cam_ = new OrthographicCamera();
        viewport_ = new StretchViewport(Settings.VIEW_WIDTH,
                Settings.VIEW_HEIGHT,
                cam_);
        cam_.position.set(viewport_.getWorldWidth() / 2,
                viewport_.getWorldHeight() / 2, 0);
        cam_.update();
    }

    public OrthographicCamera getCamera(){
        return cam_;
    }

    public Viewport getViewport(){
        return viewport_;
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

    public boolean pointInRectangle (Rectangle r, float x, float y) {
        Vector3 rect = new Vector3(r.x, r.y, 0);
        cam_.unproject(rect);

        return rect.x <= x && rect.x + r.width >= x && rect.y <= y && rect.y + r.height >= y;
    }

    public void update(){
        cam_.update();
    }

    public void unproject(Vector3 v){
        cam_.unproject(v);
    }
}
