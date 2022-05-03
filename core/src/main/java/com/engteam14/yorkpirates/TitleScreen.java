package com.engteam14.yorkpirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Objects;

public class TitleScreen extends ScreenAdapter {
    private final YorkPirates game;
    private final GameScreen nextGame;
    private final Stage stage;

    private final TextField textBox;
    private final Cell<Image> titleCell;

    private float elapsedTime = 0f;

    /**
     * Initialises the title screen, as well as relevant textures and data it may contain.
     * @param game  Passes in the base game class for reference.
     */
    public TitleScreen(YorkPirates game) throws Exception {
        this.game = game;

        // Generates main gameplay for use as background
        nextGame = new GameScreen(game);
        nextGame.setPaused(true);
        nextGame.setPlayerName("Player");

        // Generates skin
        TextureAtlas atlas;
        atlas = new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("Skin/YorkPiratesSkin.json"), new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas")));
        skin.addRegions(atlas);

        // Generates stage and table
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(skin.getDrawable("Selection"));
        if(YorkPirates.DEBUG_ON) table.setDebug(true);

        // Get title texture
        TextureRegion titleT = game.logo.getKeyFrame(0f);
        Image title = new Image(titleT);
        title.setScaling(Scaling.fit);

        // Generate textbox
        textBox = new TextField("Name (optional)", skin, "edges");
        textBox.setAlignment(Align.center);
        textBox.setOnlyFontChars(true);
        textBox.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                textBox.setText("");
            }});

        // Generate buttons
        ImageTextButton loadButton = new ImageTextButton("Load Game Save", skin);
        ImageTextButton easyButton = new ImageTextButton("New Game - Easy", skin);
        ImageTextButton normalButton = new ImageTextButton("New Game - Normal", skin);
        ImageTextButton hardButton = new ImageTextButton("New Game - Hard", skin);
        ImageTextButton quitButton = new ImageTextButton("Exit Game", skin, "Quit");

        loadButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (game.prefs.contains("PlayerName")) {
                        loadGame();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        easyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                setEasy();
            }
        });
        normalButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                setNormal();
            }
        });
        hardButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                setHard();
            }
        });
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.quit();
            }
        });

        // Add title to table
        titleCell = table.add(title).expand();

        // Add textbox to table
        table.row();
        Table textBoxFiller = new Table();
        textBoxFiller.add().expand().padRight(stage.getWidth()/3);
        textBoxFiller.add(textBox).expand().fillX();
        textBoxFiller.add().expand().padLeft(stage.getWidth()/3);
        if(YorkPirates.DEBUG_ON) textBoxFiller.debug();
        table.add(textBoxFiller).expand().fill();

        // Add buttons to table
        table.row();
        table.add(loadButton).expand();
        table.row();
        table.add(easyButton).expand();
        table.row();
        table.add(normalButton).expand();
        table.row();
        table.add(hardButton).expand();
        table.row();
        table.add(quitButton).expand();

        // Add table to the stage
        stage.addActor(table);
    }

    /**
     * Is called once every frame. Runs update() and then renders the title screen.
     * @param delta The time passed since the previously rendered frame.
     */
    @Override
    public void render(float delta){
        // Update values
        elapsedTime += delta;
        update();
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        // Render background
        ScreenUtils.clear(0f, 0f, 0f, 1.0f);
        nextGame.render(delta);

        // Animate title
        TextureRegion frame = game.logo.getKeyFrame(elapsedTime, true);
        titleCell.setActor(new Image(frame));

        // Draw UI over the top
        stage.draw();
    }

    /**
     * Is called once every frame to check for player input.
     */
    private void update(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            nextGame.setNormal();
            gameStart();
        }
    }

    /**
     * Is called to create a new game screen.
     */
    private void gameStart(){
        // Get player name
        String playerName;
        if ( textBox.getText().equals("Name (optional)") || textBox.getText().equals("")) {
            playerName = "Player";

        } else{
            playerName = textBox.getText();
        }
        // Set player name and unpause game
        nextGame.setPaused(false);
        nextGame.setPlayerName(playerName);
        game.setScreen(nextGame);
    }

    /**
     * Set the game mode to easy
     */
    private void setEasy(){
        nextGame.setEasy();
        gameStart();
    }

    /**
     * Set the game mode to normal
     */
    private void setNormal(){
        nextGame.setNormal();
        gameStart();
    }

    /**
     * Set the game mode to hard.
     */
    private void setHard(){
        nextGame.setHard();
        gameStart();
    }

    /**
     * Load previous game save
     */
    private void loadGame(){
        // Get player info
        String playerName = game.prefs.getString("PlayerName");
        nextGame.getPlayer().currentHealth = game.prefs.getFloat("PlayerHealth");
        nextGame.getPlayer().x = game.prefs.getFloat("Playerx");
        nextGame.getPlayer().y = game.prefs.getFloat("Playery");
        nextGame.loot.score = game.prefs.getInteger("PlayerLoot");
        nextGame.points.score = game.prefs.getInteger("PlayerPoints");
        nextGame.getPlayer().distance = game.prefs.getFloat("PlayerDistance");

        // Get capturedCount value
        College.capturedCount = game.prefs.getInteger("capturedCount");

        // Get colleges info
        for (int i = 0; i < nextGame.colleges.size; i++) {
            if (nextGame.colleges.get(i).getCollegeName() == "Alcuin") {
                nextGame.colleges.get(i).currentHealth = game.prefs.getFloat("AlcuinHealth");
                nextGame.colleges.get(i).team = game.prefs.getString("AlcuinTeam");
                nextGame.colleges.get(i).captured = game.prefs.getBoolean("AlcuinCaptured");
            } else if (nextGame.colleges.get(i).getCollegeName() == "Derwent") {
                nextGame.colleges.get(i).currentHealth = game.prefs.getFloat("DerwentHealth");
                nextGame.colleges.get(i).team = game.prefs.getString("DerwentTeam");
                nextGame.colleges.get(i).captured = game.prefs.getBoolean("DerwentCaptured");
            } else if (nextGame.colleges.get(i).getCollegeName() == "Langwith") {
                nextGame.colleges.get(i).currentHealth = game.prefs.getFloat("LangwithHealth");
                nextGame.colleges.get(i).team = game.prefs.getString("LangwithTeam");
                nextGame.colleges.get(i).captured = game.prefs.getBoolean("LangwithCaptured");
            }
        }

        // Get difficulty mode
        if (Objects.equals(game.prefs.getString("Difficulty"), "Easy")) {
            setEasy();
        } else if (Objects.equals(game.prefs.getString("Difficulty"), "Normal")) {
            setNormal();
        } else if (Objects.equals(game.prefs.getString("Difficulty"), "Hard")) {
            setHard();
        }

        nextGame.setPaused(false);
        nextGame.setPlayerName(playerName);
        game.setScreen(nextGame);
    }
}
