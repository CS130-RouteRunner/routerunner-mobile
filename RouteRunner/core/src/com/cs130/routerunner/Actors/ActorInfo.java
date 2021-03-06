package com.cs130.routerunner.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.cs130.routerunner.ButtonType;
import com.cs130.routerunner.Settings;
import com.cs130.routerunner.TapHandler.TapHandler;

import java.util.EventListener;
import java.util.Set;


/**
 * Created by Evannnnn on 10/29/15.
 */
public class ActorInfo {
    private Actor actor_;
    private Stage stage_;
    private Skin skin_;
    private static ButtonType lastClicked_;
    private Button buttonEditRoute_;
    private Button buttonSnapRoute_;
    private Button buttonSaveRoute_;
    private Button buttonCancelEdit_;
    private Button buttonCancelSave_;
    private Button buttonUpgrade_;
    private TapHandler tapHandler_;
    private ShapeRenderer shapeRenderer_;

    public ActorInfo(Actor actor, Stage stage, final TapHandler tapHandler){
        actor_ = actor;
        stage_ = stage;
        lastClicked_ = ButtonType.NONE;
        tapHandler_ = tapHandler;
        skin_ = new Skin(Gdx.files.internal("uiskin.json"));
        skin_.getFont("default-font").getData().setScale(2.00f, 2.00f);

        buttonEditRoute_ = new TextButton("Edit Route", skin_);
        buttonCancelEdit_ = new TextButton("Cancel", skin_);
        buttonSnapRoute_ = new TextButton("Snap Route", skin_);
        buttonSaveRoute_ = new TextButton("Save Route", skin_);
        buttonCancelSave_ = new TextButton("Cancel", skin_);
        buttonUpgrade_ = new TextButton("Upgrade", skin_);

        buttonEditRoute_.setBounds(Settings.BUTTON_X-Settings.BUTTON_WIDTH, Settings.BUTTON_Y,
                Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
        buttonCancelEdit_.setBounds(Settings.BUTTON_X, Settings.BUTTON_Y,
                Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
        buttonUpgrade_.setBounds(Settings.BUTTON_X, Settings.BUTTON_Y-Settings.BUTTON_HEIGHT,
                Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
        buttonSnapRoute_.setBounds(Settings.BUTTON_X, Settings.BUTTON_Y,
                Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
        buttonSaveRoute_.setBounds(Settings.BUTTON_X-Settings.BUTTON_WIDTH, Settings.BUTTON_Y,
                Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
        buttonCancelSave_.setBounds(Settings.BUTTON_X, Settings.BUTTON_Y,
                Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);

        buttonEditRoute_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                lastClicked_ = ButtonType.EDIT_ROUTE;
                Gdx.app.log("AIdisplay", "Edit\n");
                tapHandler_.Tap(x, y, 1);
                hide();
                stage_.addActor(buttonSnapRoute_);

            }
        });

        buttonCancelEdit_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                lastClicked_ = ButtonType.CANCEL_EDIT;
                Gdx.app.log("AIdisplay", "Cancel Edit\n");
                tapHandler_.Tap(x, y, 1);
                hide();
            }
        });

        buttonUpgrade_.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                lastClicked_ = ButtonType.UPGRADE_TRUCK;
                Gdx.app.log("AIdisplay", "Upgrade truck");
                tapHandler_.Tap(x, y, 1);
                hide();
            }
        });

        buttonSnapRoute_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                lastClicked_ = ButtonType.SNAP_ROUTE;
                Gdx.app.log("AIdisplay", "Snap\n");
                tapHandler_.Tap(x, y, 1);
                buttonSnapRoute_.remove();
                stage_.addActor(buttonSaveRoute_);
                stage_.addActor(buttonCancelSave_);
            }
        });
        buttonSaveRoute_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                lastClicked_ = ButtonType.SAVE_ROUTE;
                Gdx.app.log("AIdisplay", "Save\n");
                tapHandler_.Tap(x, y, 1);
                hide();
            }
        });
        buttonCancelSave_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                lastClicked_ = ButtonType.CANCEL_SAVE;
                Gdx.app.log("AIdisplay", "Cancel Save\n");
                tapHandler_.Tap(x, y, 1);
                hide();
            }
        });
    }

    public void display(){
        Gdx.app.log("AIdisplay", "Enter display()\n");
        stage_.addActor(buttonEditRoute_);
        stage_.addActor(buttonCancelEdit_);
        if(! actor_.getUpgraded() )
            stage_.addActor(buttonUpgrade_);
    }

    public void hide(){
        if(buttonEditRoute_ != null && buttonEditRoute_.isVisible())
            buttonEditRoute_.remove();
        if(buttonSnapRoute_ != null && buttonSnapRoute_.isVisible())
            buttonSnapRoute_.remove();
        if(buttonUpgrade_ != null && buttonUpgrade_.isVisible())
            buttonUpgrade_.remove();
        if(buttonCancelEdit_ != null && buttonCancelEdit_.isVisible())
            buttonCancelEdit_.remove();
        if(buttonSaveRoute_ != null && buttonSaveRoute_.isVisible())
            buttonSaveRoute_.remove();
        if(buttonCancelSave_ != null && buttonCancelSave_.isVisible())
            buttonCancelSave_.remove();
        //lastClicked_ = ButtonType.NONE;
    }

    public boolean isEditRoute() { return lastClicked_.equals(ButtonType.EDIT_ROUTE); }

    public boolean isSnapRoute() { return lastClicked_.equals(ButtonType.SNAP_ROUTE); }

    public boolean isCancelEdit() { return lastClicked_.equals(ButtonType.CANCEL_EDIT); }

    public boolean isSaveRoute() { return lastClicked_.equals(ButtonType.SAVE_ROUTE); }

    public boolean isCancelSave() { return lastClicked_.equals(ButtonType.CANCEL_SAVE); }

    public boolean isUpdateTruck() {
        boolean ret = lastClicked_.equals(ButtonType.UPGRADE_TRUCK);
        lastClicked_ = ButtonType.NONE;
        return ret;
    }
}
