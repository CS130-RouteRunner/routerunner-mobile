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
import com.cs130.routerunner.TapHandler.TapHandler;


/**
 * Created by Evannnnn on 10/29/15.
 */
public class ActorInfo {
    private Stage stage_;
    private Skin skin_;
    private InputProcessor preProcessor_;
    private TextureAtlas atlas_;
    private static boolean editRoute_;
    private static boolean saveRoute_;
    private static boolean cancelEdit_;
    private Button buttonEditRoute_;
    private Button buttonSaveRoute_;
    private Button buttonCancelEdit_;
    private TapHandler tapHandler_;

    public ActorInfo(Stage stage, TapHandler tapHandler){
        stage_ = stage;
        editRoute_ = false;
        saveRoute_ = false;
        cancelEdit_ = true;
        tapHandler_ = tapHandler;
        /*
        atlas_ = new TextureAtlas(Gdx.files.internal("ui-blue.atlas"));
        skin_ = new Skin();
        skin_.addRegions(atlas_);*/
        skin_ = new Skin(Gdx.files.internal("uiskin.json"));
        skin_.getFont("default-font").getData().setScale(2.00f, 2.00f);

        buttonEditRoute_ = new TextButton("Edit Route", skin_);
        buttonCancelEdit_ = new TextButton("Cancel", skin_);
        buttonSaveRoute_ = new TextButton("Save Route", skin_);

        buttonEditRoute_.setBounds(Settings.BUTTON_X-Settings.BUTTON_WIDTH, Settings.BUTTON_Y, Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
        buttonCancelEdit_.setBounds(Settings.BUTTON_X, Settings.BUTTON_Y, Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
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
        //updateInputProcessor(stage_);
        Gdx.app.log("AIdisplay", "Enter display()\n");
        editRoute_ = false;
        saveRoute_ = false;
        cancelEdit_ = false;

        buttonEditRoute_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                editRoute_ = true;
                saveRoute_ = false;
                cancelEdit_ = false;
                Gdx.app.log("AIdisplay", "Edit\n");
                tapHandler_.Tap(x, y, 1);
                buttonEditRoute_.remove();
                stage_.addActor(buttonSaveRoute_);

            }
        });
        buttonCancelEdit_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                editRoute_ = false;
                saveRoute_ = false;
                cancelEdit_ = true;
                Gdx.app.log("AIdisplay", "Cancel Edit\n");
                tapHandler_.Tap(x, y, 1);
                hide();

            }
        });
        buttonSaveRoute_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                editRoute_ = false;
                saveRoute_ = true;
                cancelEdit_ = false;
                Gdx.app.log("AIdisplay", "Save\n");
                tapHandler_.Tap(x, y, 1);
                hide();
            }
        });

        stage_.addActor(buttonEditRoute_);
        stage_.addActor(buttonCancelEdit_);
        //while(! buttonEditRoute_.isPressed()) continue;
    }

    public void hide(){
        if(buttonEditRoute_ != null && buttonEditRoute_.isVisible())
            buttonEditRoute_.remove();
        if(buttonSaveRoute_ != null && buttonSaveRoute_.isVisible())
            buttonSaveRoute_.remove();
        if(buttonCancelEdit_ != null && buttonCancelEdit_.isVisible())
            buttonCancelEdit_.remove();
        //restoreInputProcessor();
    }

    public boolean isEditRoute() { return editRoute_; }

    public boolean isSaveRoute() { return saveRoute_; }

    public boolean isCancelEdit() { return cancelEdit_; }
}
