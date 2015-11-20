package com.cs130.routerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cs130.routerunner.TapHandler.TapHandler;

/**
 * Created by Roger on 11/8/2015.
 */

//for now place in the GameMaster
    //later place into the Player class
public class PlayerButtonInfo {
    private Stage stage_;
    private Button buttonBuyTruck_;
    private Button buttonBuyMissile_;
    private TapHandler tapHandler_;
    private Skin skin_;
    private static ButtonType lastClicked_;

    //construct and initialize variables
    public PlayerButtonInfo(Stage stage, TapHandler tapHandler){
        lastClicked_ = ButtonType.NONE;
        stage_ = stage;
        tapHandler_ = tapHandler;
        skin_ = new Skin(Gdx.files.internal("uiskin.json"));
        skin_.getFont("default-font").getData().setScale(2.00f, 2.00f);

        buttonBuyMissile_ = new TextButton("Buy Missile", skin_);
        buttonBuyTruck_ = new TextButton("Buy Truck", skin_);

        buttonBuyMissile_.setBounds(Settings.BUTTON_X-Settings.BUTTON_WIDTH, Settings.BUTTON_Y,
                Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);
        buttonBuyTruck_.setBounds(Settings.BUTTON_X, Settings.BUTTON_Y,
                Settings.BUTTON_WIDTH, Settings.BUTTON_HEIGHT);


        buttonBuyMissile_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                lastClicked_ = ButtonType.BUY_MISSILE;
                Gdx.app.log("AIdisplay", "Bought Missile\n");
                tapHandler_.Tap(x, y, 1);

            }
        });
        buttonBuyTruck_.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("AIdisplay", "Bought Truck\n");
                lastClicked_ = ButtonType.BUY_TRUCK;
                tapHandler_.Tap(x, y, 1);
            }
        });

    }

    //display the correct buttons onto the screen
    public void display(){
        //Gdx.app.log("PBIdisplay", "Enter display()\n");

        stage_.addActor(buttonBuyTruck_);
        stage_.addActor(buttonBuyMissile_);
    }

    //hide all buttons
    public void hide(){
        if(buttonBuyTruck_ != null)
            buttonBuyTruck_.remove();
        if(buttonBuyMissile_ != null)
            buttonBuyMissile_.remove();
    }

    //returns whether or not the last player dock button clicked
    //was BUY_TRUCK; if it was, then change last clicked to none
    public boolean isBuyTruck() {
        if (lastClicked_.equals(ButtonType.BUY_TRUCK)) {
            lastClicked_ = ButtonType.NONE;
            return true;
        }
        else
            return false;
    }

    //returns whether or not the last player dock button clicked
    //was BUY_MISSILE; if it was, then change last clicked to none
    public boolean isBuyMissile() {
        if (lastClicked_.equals(ButtonType.BUY_MISSILE)) {
            lastClicked_ = ButtonType.NONE;
            return true;
        }
        else
            return false;
    }
}
