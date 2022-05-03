package com.engteam14.yorkpirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Objects;

public class GameScreen extends ScreenAdapter {
    // Team name constants
    public static final String playerTeam = "PLAYER";
    public static final String enemyTeam = "ENEMY";

    //Map properties
    public static int MAPWIDTH;
    public static int MAPHEIGHT;
    public static int TILE_PIXEL_WIDTH;
    public static int TILE_PIXEL_HEIGHT;
    public static int TOTAL_WIDTH;
    public static int TOTAL_HEIGHT;

    // Score managers
    public ScoreManager points;
    public ScoreManager loot;


    //PowerUps
    public Array<PowerUps> powerups;

    // Arrays (for loops)
    public Array<College> colleges;
    public Array<Projectile> projectiles;
    public Array<Enemy> enemies;
    private final Array<Texture> alcuinSprite;
    private final Array<Texture> derwentSprite;
    private final Array<Texture> langwithSprite;
    private final Array<Texture> sprites;

    //Weather
    public Array<Weather> weatherArray;

    // Sound
    public Music music;

    // Main classes
    private final YorkPirates game;

    // Player
    private final Player player;
    private String playerName;
    private Vector3 followPos;
    private boolean followPlayer = false;

    // Enemy Spawning
    private static int totalEnemiesAllowed = 5;
    private static int enemySpawnFreqency = 1000;
    private static long timeLastEnemySpawned;
    private static int ambushRate = 15000;
    private long timeLastAmbushChance;
    private static float ambush_chance = 0;
    private static int ambushSize = 4;
    private static boolean ambush = false;
    // UI & Camera
    private final HUD gameHUD;
    private final SpriteBatch HUDBatch;
    private final OrthographicCamera HUDCam;
    private final FitViewport viewport;

    // Tilemap
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;

    // Trackers
    private float elapsedTime = 0;
    private boolean isPaused = false;
    private float lastPause = 0;

    /**
     * Initialises the main game screen, as well as relevant entities and data.
     * @param game  Passes in the base game class for reference.
     */
    public GameScreen(YorkPirates game) throws Exception {
        this.game = game;
        playerName = "Player";

        // Initialise points and loot managers
        points = new ScoreManager();
        loot = new ScoreManager();

        // Initialise HUD
        HUDBatch = new SpriteBatch();
        HUDCam = new OrthographicCamera();
        HUDCam.setToOrtho(false, game.camera.viewportWidth, game.camera.viewportHeight);
        viewport = new FitViewport( Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), HUDCam);
        gameHUD =  new HUD(this);

        //initialise sound
        music = Gdx.audio.newMusic(Gdx.files.internal("Pirate1_Theme1.ogg"));
        music.setLooping(true);
        music.setVolume(0);
        music.play();

        // Initialise sprites array to be used generating GameObjects
        sprites = new Array<>();

        // Initialise player
        sprites.add(new Texture("ship1.png"), new Texture("ship2.png"), new Texture("ship3.png"));
        player = new Player(821, 489, 32, 16, playerTeam);
        player.changeImage(sprites);
        sprites.clear();
        followPos = new Vector3(player.x, player.y, 0f);
        game.camera.position.lerp(new Vector3(760, 510, 0f), 1f);

        // Initialise tilemap
        tiledMap = new TmxMapLoader().load("FINAL_MAP.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        MapProperties prop  = tiledMap.getProperties();
        MAPWIDTH = prop.get("width", Integer.class);
        MAPHEIGHT = prop.get("height", Integer.class);
        TILE_PIXEL_WIDTH = prop.get("tilewidth", Integer.class);
        TILE_PIXEL_HEIGHT = prop.get("tileheight", Integer.class);
        TOTAL_WIDTH = MAPWIDTH * TILE_PIXEL_WIDTH;
        TOTAL_HEIGHT = MAPHEIGHT * TILE_PIXEL_HEIGHT;

        // Create all the object arrays (makes them iterable: can use for loops.)
        powerups = new Array<>();
        enemies = new Array<>();
        weatherArray = new Array<>();
        colleges = new Array<>();
        projectiles = new Array<>();
        // Texture for enemy boats.
        alcuinSprite = new Array<>();
        alcuinSprite.add(new Texture("alcuin_boat.png"));
        derwentSprite = new Array<>();
        derwentSprite.add(new Texture("derwent_boat.png"));
        langwithSprite = new Array<>();
        langwithSprite.add(new Texture("langwith_boat.png"));

        // Initialise powerups
        createPowerUps();
        // Initialise weather
        createWeather();

        // Initialise colleges
        College.capturedCount = 0;
        createColleges();

        // Initialise projectiles array to be used storing live projectiles
        sprites.add(new Texture("tempProjectile.png"));
    }

    /**
     * Is called once every frame. Runs update(), renders the game and then the HUD.
     * @param delta The time passed since the previously rendered frame.
     */
    @Override
    public void render(float delta){
        // Only update if not paused
        if(!isPaused) {
            elapsedTime += delta;
            try {
                update();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        ScreenUtils.clear(0.1f, 0.6f, 0.6f, 1.0f);

        // Gameplay drawing batch
        game.batch.begin();
        tiledMapRenderer.setView(game.camera); // Draw map first so behind everything
        tiledMapRenderer.render();

        // Draw Projectiles
        for(int i = 0; i < projectiles.size; i++) {
            projectiles.get(i).draw(game.batch, 0);
        }

        //Draw Weather
        for (int i = 0; i < weatherArray.size; i++){
            weatherArray.get(i).draw(game.batch, 0);
        }

        // Draw Player, Player Health and Player Name
        if(!isPaused) {
            player.drawHealthBar(game.batch);
            player.draw(game.batch, elapsedTime);
            HUDBatch.begin();
            Vector3 pos = game.camera.project(new Vector3(player.x, player.y, 0f));
            game.font.draw(HUDBatch, playerName, pos.x, pos.y + 170f, 1f, Align.center, true);
            HUDBatch.end();
        }

        // Draw Colleges
        for(int i = 0; i < colleges.size; i++) {
            colleges.get(i).draw(game.batch, 0);
        }

        //Draw powerups
        for (int i = 0; i < powerups.size; i++) {
            powerups.get(i).draw(game.batch, 0);
        }

        for(int i = 0; i < enemies.size; i++){
            enemies.get(i).draw(game.batch, 0);
        }

        game.batch.end();

        // Draw HUD
        HUDBatch.setProjectionMatrix(HUDCam.combined);
        if(!isPaused) {
            // Draw UI
            gameHUD.renderStage(this);
            HUDCam.update();
        }
    }

    /**
     * Is called once every frame. Used for game calculations that take place before rendering.
     */
    private void update() throws Exception {
        // Call updates for all relevant objects
        player.update(this, game.camera);
        for(int i = 0; i < colleges.size; i++) {
            colleges.get(i).update(this);
        }
        for(int i = 0; i < powerups.size; i++){
            powerups.get(i).update(this);
        }
        for (int i = 0; i< enemies.size; i++){
            enemies.get(i).update(this);
        }

        // Enemy spawning code
        // Spawn an enemy every couple seconds, in a random location.
        // Number of "non-ambush" enemies shouldn't exceed "totalEnemiesAllowed"
        if (TimeUtils.timeSinceMillis(timeLastEnemySpawned) > enemySpawnFreqency &&
                enemies.size < totalEnemiesAllowed){
            timeLastEnemySpawned = TimeUtils.millis();
            //This allows for spawning anywhere on the screen.
            spawnEnemies(0, TOTAL_WIDTH,0, TOTAL_HEIGHT);
        }
        // Chance for the player to get ambushed, this is where enemy boats
        // can spawn as a group close to the player
        //      added note: don't ambush the player in the first 20 seconds of the game starting.
        if(ambush && getElapsedTime() > 20) {
            // The total number of enemy boats possible would be ambush size + totalEnemiesAllowed,
            // the "+ 1" just added so that ambushes could happen, even if all the other enemies are
            // stuck behind a rock or something.
            if (TimeUtils.timeSinceMillis(timeLastAmbushChance) > ambushRate &&
                    enemies.size < totalEnemiesAllowed + 1) {
                timeLastAmbushChance = TimeUtils.millis();
                if(MathUtils.random() < ambush_chance) {
                    // "-1" just to make the ambushSize reflect true number of enemies in an ambush.
                    for (int i = 0; i < ambushSize - 1; i++) {
                        // Spawn the enemy boats around the player
                        // range*2 square spawning location.
                        int range = 350;
                        spawnEnemies((int) (player.x - range), (int) player.x + range,
                                (int) player.y - range, (int) player.y + range);
                    }
                }
            }
        }
        // Check for projectile creation, then call projectile update
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Vector3 mouseVector = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
            Vector3 mousePos = game.camera.unproject(mouseVector);

            Projectile newProjectile = new Projectile(player, mousePos.x, mousePos.y, playerTeam);
            newProjectile.changeImage(sprites);
            projectiles.add(newProjectile);
            gameHUD.endTutorial();
        } for(int i = projectiles.size - 1; i >= 0; i--) {
            projectiles.get(i).update(this);
        }

        // Camera calculations based on player movement
        if(followPlayer) followPos = new Vector3(player.x, player.y, 0);
        if(Math.abs(game.camera.position.x - followPos.x) > 1f || Math.abs(game.camera.position.y - followPos.y) > 1f){
            game.camera.position.slerp(followPos, 0.1f);
        }

        // Call to pause the game
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && elapsedTime - lastPause > 0.1f){
            gamePause();
        }

    }

    /**
     * Allows for the spawning of enemies in a random location, bound by the parameters.
     * Enemies will have a random image chosen, then be added to the "enemies" array.
     *
     * @param lowerXbound The lower bound of the x coordinate that the enemy can spawn.
     * @param upperXbound The upper bound of the x coordinate that the enemy can spawn.
     * @param lowerYbound The lower bound of the y coordinate that the enemy can spawn.
     * @param upperYbound The upper bound of the y coordinate that the enemy can spawn.
     * @throws Exception will throw exception because of the "changeImage" method.
     */
    private void spawnEnemies(int lowerXbound, int upperXbound,
                              int lowerYbound, int upperYbound) throws Exception {
        Enemy newEnemy = new Enemy(1000, 725,
                32, 16,
                enemyTeam);
        // Enemy will spawn in a random location within the boundaries set by the parameters.
        newEnemy.changeSpawn(this, lowerXbound, upperXbound, lowerYbound, upperYbound);

        //Pick an image to draw the enemy
        int imagePicker = MathUtils.random(0,2);
        if (imagePicker == 0) {
            newEnemy.changeImage(alcuinSprite);
        } else if (imagePicker == 1){
            newEnemy.changeImage(derwentSprite);
        } else{
            newEnemy.changeImage(langwithSprite);
        }
        //Add the enemy to the array of enemies.
        enemies.add(newEnemy);
    }
    // Creation methods, made to reduce the constructor size.
    /**
     * creates the various power ups on the map.
     * @throws Exception inherited from changeImage()
     */
    private void createPowerUps() throws Exception {
        //PowerUps
        PowerUps newPower;
        Array<Texture> powerSprites = new Array<>();

        //Add Give More Damage PowerUp
        powerSprites.add(new Texture("give_more_damage.png"));
        powerSprites.add(new Texture("give_more_damage_grey.png"));
        newPower = new PowerUps(1000f, 1500,
                powerSprites.get(0).getWidth()*0.05f, powerSprites.get(0).getHeight()*0.05f,
                "GiveMoreDamage");
        newPower.changeImage(powerSprites);
        //newPower.addPower(-70, -20, 60); Think this was to add separately - do not want this.
        powerups.add(newPower);
        powerSprites.clear();

        //Add Take More Damage PowerUp
        powerSprites.add(new Texture("take_more_damage_grey.png"));
        newPower = new PowerUps(3000, 3000,
                powerSprites.get(0).getWidth()*0.05f, powerSprites.get(0).getHeight()*0.05f,
                "TakeMoreDamage");
        newPower.changeImage(powerSprites);
        powerups.add(newPower);
        powerSprites.clear();

        //Add Immunity
        powerSprites.add(new Texture("immunity_grey.png"));
        newPower = new PowerUps(1500f, 2000f,
                powerSprites.get(0).getWidth()*0.05f, powerSprites.get(0).getHeight()*0.05f,
                "Immunity");
        newPower.changeImage(powerSprites);
        powerups.add(newPower);
        powerSprites.clear();

        //Add Health Restore
        powerSprites.add(new Texture("health_restore.png"));
        newPower = new PowerUps(1000, 725,
                powerSprites.get(0).getWidth()*0.05f, powerSprites.get(0).getHeight()*0.05f,
                "HealthRestore");
        newPower.changeImage(powerSprites);
        powerups.add(newPower);
        powerSprites.clear();

        //Add Speed
        powerSprites.add(new Texture("speed_grey.png"));
        newPower = new PowerUps(2200, 800,
                powerSprites.get(0).getWidth()*0.05f, powerSprites.get(0).getHeight()*0.05f,
                "Speed");
        newPower.changeImage(powerSprites);
        powerups.add(newPower);
        powerSprites.clear();
    }

    /**
     * Creates the weather
     * @throws Exception    inherited from changeImage()
     */
    private void createWeather() throws Exception {
        // Initialise weather
        Weather newWeather;
        sprites.add(new Texture("Ice_5_16x16.png"));
        newWeather = new Weather( 1920, 1520, sprites.get(0).getWidth() * 5f,
                sprites.get(0).getHeight() * 5f, "");
        newWeather.changeImage(sprites);
        weatherArray.add(newWeather);
        newWeather = new Weather(2080, 560, sprites.get(0).getWidth() * 5f,
                sprites.get(0).getHeight() * 5f, "");
        newWeather.changeImage(sprites);
        weatherArray.add(newWeather);
        newWeather = new Weather(1000, 600, sprites.get(0).getWidth() * 5f,
                sprites.get(0).getHeight() * 5f, "");
        newWeather.changeImage(sprites);
        weatherArray.add(newWeather);
        sprites.clear();
    }

    /**
     * creates the colleges.
     * @throws Exception    inherited from changeImage()
     */
    private void createColleges() throws Exception{
        College newCollege;
        Array<Texture> collegeSprites = new Array<>();

        // Add alcuin
        collegeSprites.add( new Texture("alcuin.png"),
                new Texture("alcuin_2.png"));
        newCollege = new College(1492, 665,
                collegeSprites.get(0).getWidth()*0.5f,collegeSprites.get(0).getWidth()*0.5f,
                "Alcuin", enemyTeam);
        newCollege.imageHandling(collegeSprites, "alcuin_boat.png", player);
        newCollege.addBoat(30, -20, -60);
        newCollege.addBoat(-50, -40, -150);
        newCollege.addBoat(-40, -70, 0);
        colleges.add(newCollege);
        collegeSprites.clear();

        // Add derwent
        collegeSprites.add( new Texture("derwent.png"),
                new Texture("derwent_2.png"));
        newCollege = new College(1815, 2105,
                collegeSprites.get(0).getWidth()*0.8f, collegeSprites.get(0).getHeight()*0.8f,
                "Derwent", enemyTeam);
        newCollege.imageHandling(collegeSprites, "derwent_boat.png", player);
        newCollege.addBoat(-70, -20, 60);
        newCollege.addBoat(-70, -60, 70);
        colleges.add(newCollege);
        collegeSprites.clear();

        // Add langwith
        collegeSprites.add( new Texture("langwith.png"),
                new Texture("langwith_2.png"));
        newCollege = new College(1300, 1530,
                collegeSprites.get(0).getWidth()*1.0f, collegeSprites.get(0).getHeight()*1.0f,
                "Langwith", enemyTeam);
        newCollege.imageHandling(collegeSprites, "langwith_boat.png", player);
        newCollege.addBoat(-150, -50, 60);
        newCollege.addBoat(-120, -10, -60);
        newCollege.addBoat(-10, -40, 230);
        newCollege.addBoat(140, 10, 300);
        newCollege.addBoat(200, 35, 135);
        colleges.add(newCollege);
        collegeSprites.clear();

        // Add goodricke
        collegeSprites.add( new Texture("goodricke.png"));
        newCollege = new College(700, 525,
                collegeSprites.get(0).getWidth()*0.7f, collegeSprites.get(0).getHeight()*0.7f,
                "Home",playerTeam);
        newCollege.imageHandling(collegeSprites, "ship1.png", player);
        colleges.add(newCollege);
        collegeSprites.clear();
    }
    /**
     * Called to switch from the current screen to the pause screen, while retaining the current screen's information.
     */
    public void gamePause(){
        isPaused = true;
        game.setScreen(new PauseScreen(game,this));
    }

    /**
     * Called to switch from the current screen to the end screen.
     * @param win   The boolean determining the win state of the game.
     */
    public void gameEnd(boolean win){
        game.prefs.clear();
        game.setScreen(new EndScreen(game, this, win));
    }

    /**
     * Called to switch from the current screen to the title screen.
     */
    public void gameReset() throws Exception {
        game.setScreen(new TitleScreen(game));
    }

    /**
     * Used to encapsulate elapsedTime.
     * @return  Time since the current session started.
     */
    public float getElapsedTime() { return elapsedTime; }

    /**
     * Used to toggle whether the camera follows the player.
     * @param follow  Whether the camera will follow the player.
     */
    public void toggleFollowPlayer(boolean follow) { this.followPlayer = follow; }

    /**
     * Get the player's name for the current session.
     * @return  Player's name.
     */
    public String getPlayerName() { return playerName; }

    /**
     * Set the player's name.
     * @param playerName    Chosen player name.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        gameHUD.updateName(this);
    }

    /**
     * Get the player.
     * @return  The player.
     */
    public Player getPlayer() { return player; }
    // Setting the difficulty of the game
    // The player method will change properties around the player
    // the rest will change the different enemy spawning properties.

    /**
     * Sets the game to "easy mode"
     * No ambushes
     */
    public void setEasy(){
        player.setEasy();
        enemySpawnFreqency = 15000;
        totalEnemiesAllowed = 5;
        ambushRate = 999999999;
        ambush_chance = 0;
        ambush = false;
        ambushSize = 0;
    }

    /**
     * Sets the game to "normal mode"
     */
    public void setNormal(){
        player.setNormal();
        enemySpawnFreqency = 10000;
        totalEnemiesAllowed = 10;
        ambushRate = 25000;
        ambush_chance = 0.1f;
        ambush = true;
        ambushSize = 3;
    }

    /**
     * Sets the game to "hard mode"
     */
    public void setHard(){
        player.setHard();
        enemySpawnFreqency = 5000;
        ambushRate = 20000;
        totalEnemiesAllowed = 11;
        ambush_chance = 0.25f;
        ambush = true;
        ambushSize = 4;
    }


    /**
     * Get the main game class.
     * @return  The main game class.
     */
    public YorkPirates getMain() { return game; }

    /**
     * Get the game's HUD.
     * @return  The HUD.
     */
    public HUD getHUD() { return gameHUD; }

    /**
     * Set whether the game is paused or not.
     * @param paused    Whether the game is paused.
     */
    public void setPaused(boolean paused) {
        if (!paused && isPaused) lastPause = elapsedTime;
        isPaused = paused;
    }

    /**
     * Gets whether the game is paused.
     * @return  True if the game is paused.
     */
    public boolean isPaused() { return  isPaused; }

    /**
     * Get the viewport.
     * @return  The viewport.
     */
    public FitViewport getViewport() { return viewport; }

    /**
     * Disposes of disposables when game finishes execution.
     */
    @Override
    public void dispose(){
        HUDBatch.dispose();
        tiledMap.dispose();
        music.dispose();
    }

    /**
     * Used for debugging.
     */
    public void printElaspedTime(){
        System.out.println("Elasped Time: " + getElapsedTime());
    }
}
