package com.cs130.routerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


/**
 * Created by Evannnnn on 10/29/15.
 */
public class ActorInfo {
    private Stage stage_;
    private Skin skin_;
    private InputProcessor preProcessor_;
    private TextureAtlas atlas_;
    private boolean editRoute_;
    private boolean saveRoute_;
    private Button buttonEditRoute_;
    private Button buttonSaveRoute_;

    public ActorInfo(Stage stage){
        stage_ = stage;
        editRoute_ = false;
        saveRoute_ = false;
        /*
        atlas_ = new TextureAtlas(Gdx.files.internal("ui-blue.atlas"));
        skin_ = new Skin();
        skin_.addRegions(atlas_);*/
        skin_ = new Skin(Gdx.files.internal("uiskin.json"));
        buttonEditRoute_ = new TextButton("Edit Route", skin_);
        buttonSaveRoute_ = new TextButton("Save Route", skin_);

        buttonEditRoute_.setBounds(Settings.BUTTON_X, Settings.BUTTON_Y, Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
        buttonSaveRoute_.setBounds(Settings.BUTTON_X, Settings.BUTTON_Y, Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
    }

    // below two functions may refactor with InputMultiplexer
    protected void updateInputProcessor(InputProcessor inputProcessor){
        preProcessor_ = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    protected void restoreInputProcessor(){
        if(preProcessor_ != null)
            Gdx.input.setInputProcessor(preProcessor_);
    }

    public void display(){
        updateInputProcessor(stage_);
        editRoute_ = false;
        saveRoute_ = false;

        buttonEditRoute_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                editRoute_ = true;
                saveRoute_ = false;
                buttonEditRoute_.remove();
                stage_.addActor(buttonSaveRoute_);
            }
        });
        buttonSaveRoute_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                editRoute_ = false;
                saveRoute_ = true;
                hide();
            }
        });

        stage_.addActor(buttonEditRoute_);
    }

    public void hide(){
        if(buttonEditRoute_ != null && buttonEditRoute_.isVisible())
            buttonEditRoute_.remove();
        if(buttonSaveRoute_ != null && buttonSaveRoute_.isVisible())
            buttonSaveRoute_.remove();
        restoreInputProcessor();
    }

    public boolean isEditRoute() { return editRoute_; }

    public boolean isSaveRoute() { return saveRoute_; }

}
