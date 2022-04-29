package com.engteam14.yorkpirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;
import java.time.LocalDateTime;

public class Player extends GameObject {

    // Player constants
    private static final int POINT_FREQUENCY = 1000; // How often the player gains points by moving.
    private static final float CAMERA_SLACK = 0.1f; // What percentage of the screen the player can move in before the camera follows.
    private static final float SPEED =70f; // Player movement speed.
    public static final int HEALTH = 200;
    private static final int DAMAGE_POWERUP_VALUE = 1000;
    public static final int DEFUALT_DAMAGE = 20;
    private static final int DAMAGE_POWERUP_TOTAL_LENGTH = 10000;
    private static final int IMMUNITY_POWERUP_LENGTH = 10000;
    private static final int TAKE_DAMAGE_INCREASE = 350;
    //private static final float SPEED_POWERUP_NORMAL = 1;
    //private static final float SPEED_POWERUP_MULTIPLIER = 2;
    private static final int SPEED_POWERUP_TOTAL_LENGTH = 25000;
    private int speedMultiplier = 1;

    private static int timeBeforeRegen = 10000;
    private static double regenAmount = 0.03;
    private static float enemyDamageMultiplier = 1;

    // Movement calculation values
    private int previousDirectionX;
    private int previousDirectionY;
    private float distance;
    private long lastMovementScore;

    private HealthBar playerHealth;
    private float splashTime;
    private long timeLastHit;
    private boolean doBloodSplash = false;

    private float playerDamage = 20;
    private long damageIncreaseStart;
    private boolean immune = false;
    private long takeMoreDamageStart;
    private long speedStart;
    private long immunityStart;

    private float defaultDamage = 20;


    /**
     * Generates a generic object within the game with animated frame(s) and a hit-box.
     * @param frames    The animation frames, or a single sprite.
     * @param fps       The number of frames to be displayed per second.
     * @param x         The x coordinate within the map to initialise the object at.
     * @param y         The y coordinate within the map to initialise the object at.
     * @param width     The size of the object in the x-axis.
     * @param height    The size of the object in the y-axis.
     * @param team      The team the player is on.
     */
    public Player(Array<Texture> frames, float fps, float x, float y, float width, float height, String team){
        super(frames, fps, x, y, width, height, team);
        lastMovementScore = 0;
        splashTime = 0;

        // Generate health
        Array<Texture> sprites = new Array<>();
        sprites.add(new Texture("allyHealthBar.png"));
        setMaxHealth(HEALTH);
        playerHealth = new HealthBar(this,sprites);
    }

    /**
     * Called once per frame. Used to perform calculations such as player/camera movement.
     * @param screen    The main game screen.
     * @param camera    The player camera.
     */
    public void update(GameScreen screen, OrthographicCamera camera){
        Vector2 oldPos = new Vector2(x,y); // Stored for next-frame calculations

        // Get input movement
        int horizontal = ((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ? 1 : 0)
                - ((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) ? 1 : 0);
        int vertical = ((Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) ? 1 : 0)
                - ((Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) ? 1 : 0);

        // Calculate collision && movement
        if (horizontal != 0 || vertical != 0){
            move(speedMultiplier*SPEED*horizontal, speedMultiplier*SPEED*vertical);
            previousDirectionX = horizontal;
            previousDirectionY = vertical;
            if (safeMove(screen.getMain().edges)) {
                if (TimeUtils.timeSinceMillis(lastMovementScore) > POINT_FREQUENCY) {
                    lastMovementScore = TimeUtils.millis();
                    screen.points.Add(1);
                }
            } else {    // Collision
                Vector2 newPos = new Vector2(x, y);
                x = oldPos.x;
                if (!safeMove(screen.getMain().edges)) {
                    x = newPos.x;
                    y = oldPos.y;
                    if (!safeMove(screen.getMain().edges)) {
                        x = oldPos.x;
                    }
                }
            }
        }
        updateHitboxPos();
        // Track distance travelled
        distance += Math.pow((Math.pow((x - oldPos.x),2f) + Math.pow((y - oldPos.y),2f)),0.5f)/10f;

        // Camera Calculations
        ProcessCamera(screen, camera);

        // Blood splash calculations
        if(doBloodSplash){
            if(splashTime > 1){
                doBloodSplash = false;
                splashTime = 0;
            }else{
                splashTime += 1;
            }
        }
        //If it has been 10 seconds since the player was last hit, then health will increase.
        if (TimeUtils.timeSinceMillis(timeLastHit) > 10000){
            currentHealth += 0.03;
            //If current health goes above the max, then it will remain at max health.
            if(currentHealth > maxHealth) currentHealth = maxHealth;
            playerHealth.resize(currentHealth);
        }

        //Timing how long the GiveMoreDamage powerup lasts
        if(TimeUtils.timeSinceMillis(damageIncreaseStart) > DAMAGE_POWERUP_TOTAL_LENGTH){
            playerDamage = DEFUALT_DAMAGE;
        }

        //Timing how long the Immunity powerup lasts
        if (TimeUtils.timeSinceMillis(immunityStart) > IMMUNITY_POWERUP_LENGTH){
            immune = false;
        }

        //Timing how long the TakeMoreDamage powerup lasts
        if (TimeUtils.timeSinceMillis(takeMoreDamageStart) < DAMAGE_POWERUP_TOTAL_LENGTH){
            setMaxHealth(HEALTH);
        }

        //Timing how long the Speed powerup lasts
        if (TimeUtils.timeSinceMillis(speedStart) > SPEED_POWERUP_TOTAL_LENGTH){
            speedMultiplier = 1;
        }

    }

    /**
     *  Calculate if the current player position is safe to be in.
     * @param edges A 2d array containing safe/unsafe positions to be in.
     * @return      If the current position is safe.
     */
    private Boolean safeMove(Array<Array<Boolean>> edges){
        return (
                edges.get((int)((y+height/2)/16)).get((int)((x+width/2)/16)) &&
                        edges.get((int)((y+height/2)/16)).get((int)((x-width/2)/16)) &&
                        edges.get((int)((y-height/2)/16)).get((int)((x+width/2)/16)) &&
                        edges.get((int)((y-height/2)/16)).get((int)((x-width/2)/16))
        );
    }

    /**
     * Moves the player within the x and y-axis of the game world.
     * @param x     The amount to move the object within the x-axis.
     * @param y     The amount to move the object within the y-axis.
     */
    @Override
    public void move(float x, float y){
        this.x += x * Gdx.graphics.getDeltaTime();
        this.y += y * Gdx.graphics.getDeltaTime();
        playerHealth.move(this.x, this.y + height/2 + 2f); // Healthbar moves with player
    }

    /**
     * Called when a projectile hits the college.
     * @param screen            The main game screen.
     * @param damage            The damage dealt by the projectile.
     * @param projectileTeam    The team of the projectile.
     */
    @Override
    public void takeDamage(GameScreen screen, float damage, String projectileTeam){
        timeLastHit = TimeUtils.millis();
        if (immune == true){
            damage = 0;
        }
        currentHealth -= damage;
        doBloodSplash = true;

        // Health-bar reduction
        if(currentHealth > 0){
            playerHealth.resize(currentHealth);
        }else{
            playerHealth = null;
            screen.gameEnd(false);
        }
    }

    /**
     * Called after update(), calculates whether the camera should follow the player and passes it to the game screen.
     * @param screen    The main game screen.
     * @param camera    The player camera.
     */
    private void ProcessCamera(GameScreen screen, OrthographicCamera camera) {
        Vector2 camDiff = new Vector2(x - camera.position.x, y - camera.position.y);
        screen.toggleFollowPlayer(Math.abs(camDiff.x) > camera.viewportWidth / 2 * CAMERA_SLACK || Math.abs(camDiff.y) > camera.viewportWidth / 2 * CAMERA_SLACK);
    }

    /**
     * Called when drawing the player.
     * @param batch         The batch to draw the player within.
     * @param elapsedTime   The current time the game has been running for.
     */
    @Override
    public void draw(SpriteBatch batch, float elapsedTime){
        // Generates the sprite
        Texture frame = anim.getKeyFrame((currentHealth/maxHealth > 0.66f) ? 0 : ((currentHealth/maxHealth > 0.33f) ? 2 : 1), true);
        if(doBloodSplash){
            batch.setShader(shader); // Set our grey-out shader to the batch
        } float rotation = (float) Math.toDegrees(Math.atan2(previousDirectionY, previousDirectionX));

        // Draws sprite and health-bar
        batch.draw(frame, x - width/2, y - height/2, width/2, height/2, width, height, 1f, 1f, rotation, 0, 0, frame.getWidth(), frame.getHeight(), false, false);
        batch.setShader(null);
    }

    public void drawHealthBar(SpriteBatch batch){
        if(!(playerHealth == null)) playerHealth.draw(batch, 0);
    }

    public float getDistance() {
        return distance;
    }
    public float getPlayerDamage() {
        return playerDamage;
    }
    public void damageIncrease(){
        this.playerDamage = DAMAGE_POWERUP_VALUE;
        this.damageIncreaseStart = TimeUtils.millis();
    }
    public void giveMaxHealth(){
        this.setCurrentHealth(HEALTH);
        playerHealth.resize(currentHealth);
    }
    public void immunityPowerup(){
        this.immunityStart = TimeUtils.millis();
        immune = true;
    }
    public void takeMoreDamagePowerup(){
        this.takeMoreDamageStart = TimeUtils.millis();
        setMaxHealth(TAKE_DAMAGE_INCREASE);
    }
    public void speedPowerup(){
        this.speedStart = TimeUtils.millis();
        speedMultiplier = 2;
    }

    /**
     * Called to set the difficulty at the start of the game.
     */
    public void setEasy(){
        regenAmount = 0.06;
        timeBeforeRegen = 5000;
        enemyDamageMultiplier = 1;
        defaultDamage = 30;
        setMaxHealth(400);
    }
    public void setNormal(){
        regenAmount = 0.03;
        timeBeforeRegen = 10000;
        enemyDamageMultiplier = 1.5f;
        defaultDamage = 20;
        setMaxHealth(300);
    }
    public void setHard(){
        regenAmount = 0;
        timeBeforeRegen = 10000;
        enemyDamageMultiplier = 2f;
        defaultDamage = 15;
        setMaxHealth(200);
    }

    public void printStats(){
        System.out.println("Regen: " + regenAmount);
        System.out.println("timeBeforeRegen: " + timeBeforeRegen);
        System.out.println("enemydmgmult: " + enemyDamageMultiplier);
        System.out.println("def dmg: " + defaultDamage);
        System.out.println("maxhealth: " + maxHealth);
        System.out.println("x: " + this.x + " y: " + y);

    }
}