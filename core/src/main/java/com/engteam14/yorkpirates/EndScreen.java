package com.engteam14.yorkpirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;

public class EndScreen extends ScreenAdapter {
    private final YorkPirates game;
    private final Stage endStage;
    private final GameScreen screen;

    /**
     * Initialises the title screen, as well as relevant textures and data it may contain.
     * @param game      Passes in the base game class for reference.
     * @param screen    Passes in the game screen for reference.
     * @param win       Passes in the win status.
     */
    public EndScreen(YorkPirates game, GameScreen screen, boolean win){
        this.game = game;
        this.screen = screen;
        screen.setPaused(true);

        // Generate title image based on win/lose
        String imageN;
        if (win)    imageN="game_win.png";
        else        imageN="game_over.png";
        Texture titleT = new Texture(Gdx.files.internal(imageN));
        Image title = new Image(titleT);

        // Generate skin
        TextureAtlas atlas;
        atlas = new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("Skin/YorkPiratesSkin.json"), new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas")));
        skin.addRegions(atlas);

        // Generate stage and table
        endStage = new Stage(screen.getViewport());
        Table table = new Table();
        table.setFillParent(true);
        Gdx.input.setInputProcessor(endStage);
        table.setBackground(skin.getDrawable("Selection"));
        if(YorkPirates.DEBUG_ON) table.setDebug(true);

        // Calculate stats
        int seconds = (int) screen.getElapsedTime();
        Label time = new Label(((seconds / 60) == 0 ? "" : ((seconds / 60) + " Minutes, ")) + (seconds % 60) + " Seconds Elapsed", skin);
        Label points = new Label(screen.points.Get() == 0 ? "No Points Gained" : screen.points.GetString() + " Points Gained", skin);
        Label loot = new Label(screen.loot.Get() == 0 ? "No Loot Won" : screen.loot.GetString() + " Loot Won", skin);

        // Make UI Buttons
        ImageButton quitB = new ImageButton(skin, "Quit");
        ImageButton restartB = new ImageButton(skin, "Restart");

        quitB.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        restartB.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                try {
                    screen.gameReset();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Add title image to table
        table.row();
        title.setScaling(Scaling.fit);
        table.add(title).expand();

        // Add stats to table
        table.row();
        table.add(time).expand();
        table.row();
        table.add(points).expand();
        table.row();
        table.add(loot).expand();

        // Make buttons subtable
        table.row();
        Table buttons = new Table();
        buttons.add(restartB).expand().size(200f).pad(100f);
        buttons.add(quitB).expand().size(200f).pad(100f);
        if(YorkPirates.DEBUG_ON) buttons.setDebug(true);
        table.add(buttons);

        // Add table to the stage
        endStage.addActor(table);
    }

    /**
     * Is called once every frame. Runs update() and then renders the title screen.
     * @param delta The time passed since the previously rendered frame.
     */
    @Override
    public void render(float delta){
        try {
            update();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ScreenUtils.clear(0.1f, 0.6f, 0.6f, 1.0f);
        screen.render(delta); // Draws the gameplay screen as a background
        endStage.draw(); // Draws the stage
    }

    /**
     * Is called once every frame. Used for calculations that take place before rendering / inputs.
     */
    private void update() throws Exception {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.setScreen(new TitleScreen(game));
        }
    }
}
