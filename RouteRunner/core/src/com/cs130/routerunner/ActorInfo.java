package com.cs130.routerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Evannnnn on 10/29/15.
 */
public class ActorInfo {
    private Stage stage_;
    private Dialog dialog_;
    private Skin skin_;
    private boolean editRoute_;
    private InputProcessor preProcessor_;

    public ActorInfo(Stage stage){
        stage_ = stage;
        editRoute_ = false;
    }

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
        skin_ = new Skin(Gdx.files.internal("uiskin.json"));
        dialog_ = new Dialog("Info", skin_, "dialog");
        Button yesButton = new TextButton("yes", skin_);
        yesButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                editRoute_ = true;
                hide();
            }
        });
        Button noButton = new TextButton("no", skin_);
        noButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                editRoute_ = false;
                hide();
            }
        });
        dialog_.text("Edit route?");
        dialog_.button(yesButton, true);
        dialog_.button(noButton, false);
        dialog_.invalidateHierarchy();
        dialog_.invalidate();
        dialog_.setPosition(0, 0);
        dialog_.layout();

        dialog_.show(stage_);
    }

    public void hide(){
        if(dialog_ != null && dialog_.isVisible())
            dialog_.remove();

        restoreInputProcessor();
    }

    public boolean isEditRoute(){
        return editRoute_;
    }

}
