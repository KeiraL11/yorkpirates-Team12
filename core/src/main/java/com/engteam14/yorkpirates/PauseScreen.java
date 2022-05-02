package com.engteam14.yorkpirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Preferences;


public class PauseScreen extends ScreenAdapter {

    private final YorkPirates game;
    private final GameScreen screen;

    private final ShopScreen shopScreen;
    private final Stage pauseStage;

    /**
     * Called when the game needs to be paused.
     * Contains the shop and saving.
     * @param game      The base game, stores some important values.
     * @param screen    the main game.
     */
    public PauseScreen(YorkPirates game, GameScreen screen){
        this.game = game;
        this.screen = screen;

        shopScreen = new ShopScreen(game, screen,this);

        // Generate skin
        TextureAtlas atlas;
        atlas = new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("Skin/YorkPiratesSkin.json"), new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas")));
        skin.addRegions(atlas);

        // Generate stage and table
        pauseStage = new Stage(screen.getViewport());
        Gdx.input.setInputProcessor(pauseStage);
        Table table = new Table();
        table.setFillParent(true);
        table.setTouchable(Touchable.enabled);
        table.setBackground(skin.getDrawable("Selection"));
        if(YorkPirates.DEBUG_ON) table.setDebug(true);

        // Generate title texture
        Texture titleT = new Texture(Gdx.files.internal("paused.png"));
        Image title = new Image(titleT);
        title.setScaling(Scaling.fit);

        // Generate buttons
        TextButton shop = new TextButton("Shop", skin);
        shop.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(shopScreen);
            }
        });

        TextButton resume = new TextButton("Resume", skin);
        resume.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gameContinue();
            }
        });

        TextButton save = new TextButton("Save & Quit", skin);
        save.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gameSave();
                Gdx.app.exit();
            }
        });

        TextButton restart = new TextButton("End Game", skin);
        restart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                screen.gameEnd(false);

            }
        });

        TextButton music = new TextButton("", skin);
        if (screen.music.getVolume() == 0)  music.setText("Turn Music ON");
        else                                music.setText("Turn Music OFF");
        music.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (screen.music.getVolume() == 0) {
                    screen.music.setVolume(1);
                    music.setText("Turn Music OFF");
                } else {
                    screen.music.setVolume(0);
                    music.setText("Turn Music ON");

                }
            }
        });

        TextButton quit = new TextButton("Quit", skin);
        quit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();

            }
        });

        // Add title to table
        table.row();
        table.add(title).expand();

        // Add buttons to table
        table.row();
        table.add(shop).expand();
        table.row();
        table.add(resume).expand();
        table.row();
        table.add(save).expand();
        table.row();
        table.add(restart).expand();
        table.row();
        table.add(music).expand();
        table.row();
        table.add(quit).expand();

        // Add table to the stage
        pauseStage.addActor(table);
    }

    /**
     * Is called once every frame. Runs update() and then renders the title screen.
     * @param delta The time passed since the previously rendered frame.
     */
    @Override
    public void render(float delta){
        Gdx.input.setInputProcessor(pauseStage);
        update();
        ScreenUtils.clear(0.6f, 0.6f, 1.0f, 1.0f);
        screen.render(delta); // Draws the gameplay screen as a background
        pauseStage.draw(); // Draws the stage
    }

    /**
     * Is called once every frame. Used for calculations that take place before rendering.
     */
    private void update(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gameContinue();
        }
    }

    /**
     * Generates a HUD object within the game that controls elements of the UI.
     */
    private void gameContinue() {
        if (shopScreen.immunity_bought) {
            screen.getPlayer().immunityPowerup();
        }

        if (shopScreen.give_more_damage_bought) {
            screen.getPlayer().damageIncrease();
        }

        if (shopScreen.take_more_damage_bought) {
            screen.getPlayer().takeMoreDamagePowerup();
        }
        screen.setPaused(false);
        game.setScreen(screen);
    }
    private void gameSave() {
        screen.setPaused(true);

        game.prefs.putString("PlayerName", screen.getPlayerName());
        game.prefs.putFloat("PlayerHealth", screen.getPlayer().currentHealth);
        game.prefs.putFloat("Playerx", screen.getPlayer().x);
        game.prefs.putFloat("Playery", screen.getPlayer().y);
        game.prefs.putInteger("PlayerLoot", screen.loot.Get());
        game.prefs.putInteger("PlayerPoints", screen.points.Get());
        game.prefs.putFloat("PlayerDistance", screen.getPlayer().getDistance());
        game.prefs.putInteger("capturedCount", College.capturedCount);
        game.prefs.putString("Difficulty", screen.getPlayer().getDifficulty());

        for (int i = 0; i < screen.colleges.size; i++) {
            game.prefs.putFloat(screen.colleges.get(i).getCollegeName() + "Health", screen.colleges.get(i).currentHealth);
            game.prefs.putString(screen.colleges.get(i).getCollegeName() + "Team", screen.colleges.get(i).team);
            game.prefs.putBoolean(screen.colleges.get(i).getCollegeName() + "Captured", screen.colleges.get(i).captured);
        }

        game.prefs.flush();
    }
}
