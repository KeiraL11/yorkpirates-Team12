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

public class ShopScreen extends ScreenAdapter {

    private final YorkPirates game;

    private final GameScreen screen;

    private final Label loot;

    private final Stage shopStage;

    public boolean give_more_damage_bought;
    public boolean immunity_bought;
    public boolean take_less_damage_bought;

    /**
     * The shop, the user can buy power ups to use in the game.
     * @param game          the base game
     * @param screen        the main game screen.
     * @param pauseScreen   the pause screen (used to get to the shop)
     */
    public ShopScreen(YorkPirates game, GameScreen screen, PauseScreen pauseScreen) {
        this.game = game;
        this.screen = screen;

        // Generate skin
        TextureAtlas atlas;
        atlas = new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("Skin/YorkPiratesSkin.json"), new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas")));
        skin.addRegions(atlas);

        // Generate stage and table
        shopStage = new Stage(screen.getViewport());
        Gdx.input.setInputProcessor(shopStage);
        Table table = new Table();
        table.setFillParent(true);
        table.setTouchable(Touchable.enabled);
        table.setBackground(skin.getDrawable("Selection"));
        if(YorkPirates.DEBUG_ON) table.setDebug(true);

        // Generate title texture
        Texture titleT = new Texture(Gdx.files.internal("loot.png"));
        loot = new Label(screen.loot.GetString(), skin);
        loot.setFontScale(1.6f);
        Image title = new Image(titleT);
        //title.setScaling(Scaling.fit);

        Image coin1 = new Image(titleT);
        Image coin2 = new Image(titleT);
        Image coin3 = new Image(titleT);

        Label coin1L = new Label("15", skin);
        Label coin2L = new Label("15", skin);
        Label coin3L = new Label("15", skin);

        // Generate powerup textures
        Texture lessDamageT = new Texture(Gdx.files.internal("take_more_damage_grey.png"));
        Image lessDamage = new Image(lessDamageT);
        lessDamage.setScaling(Scaling.fit);

        Texture immunityT = new Texture(Gdx.files.internal("immunity_grey.png"));
        Image immunity = new Image(immunityT);
        immunity.setScaling(Scaling.fit);

        Texture damageT = new Texture(Gdx.files.internal("give_more_damage_grey.png"));
        Image damage = new Image(damageT);
        damage.setScaling(Scaling.fit);

        // Generate buttons
        ImageButton menuButton = new ImageButton(skin, "Menu");
        menuButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(pauseScreen);
            }
        });

        TextButton damageButton = new TextButton("Give More Damage", skin);
        damageButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (screen.loot.Get() >= 15) {
                    give_more_damage_bought = true;
                    screen.loot.Add(-15);
                    screen.getPlayer().damageIncrease();
                }
            }
        });

        TextButton immunityButton = new TextButton("Immunity", skin);
        immunityButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (screen.loot.Get() >= 15) {
                    immunity_bought = true;
                    screen.loot.Add(-15);
                    screen.getPlayer().immunityPowerup();
                }

            }
        });

        TextButton lessDamageButton = new TextButton("Juggernaut", skin);
        lessDamageButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (screen.loot.Get() >= 15) {
                    take_less_damage_bought = true;
                    screen.loot.Add(-15);
                    screen.getPlayer().takeMoreDamagePowerup();
                }
            }
        });

        // Add title to table
        table.row();
        table.add(menuButton).size(150).left();
        table.add(title).size(100).left();
        table.add(loot);
        table.row();

        // Add buttons and powerups to table
        table.add(damage).right().padRight(100);
        table.add(damageButton).right().padRight(100);
        table.add(coin1).right().padRight(10);
        table.add(coin1L);
        table.row();

        table.add(immunity).right().padRight(100);
        table.add(immunityButton).right().padRight(100);
        table.add(coin2).right().padRight(10);
        table.add(coin2L);
        table.row();

        table.add(lessDamage).right().padRight(100);
        table.add(lessDamageButton).right().padRight(100);
        table.add(coin3).right().padRight(10);
        table.add(coin3L);

        // Add table to the stage
        shopStage.addActor(table);
    }

    /**
     * Is called once every frame. Runs update() and then renders the title screen.
     * @param delta The time passed since the previously rendered frame.
     */
    @Override
    public void render(float delta){
        Gdx.input.setInputProcessor(shopStage);
        update();
        ScreenUtils.clear(0.6f, 0.6f, 1.0f, 1.0f);
        loot.setText(screen.loot.GetString());
        screen.render(delta);// Draws the gameplay screen as a background
        shopStage.draw(); // Draws the stage
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
        screen.setPaused(false);
        game.setScreen(screen);
    }
}