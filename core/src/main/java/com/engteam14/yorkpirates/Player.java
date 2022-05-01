package com.engteam14.yorkpirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Player extends GameObject {

    // Player constants
    private static final int POINT_FREQUENCY = 1000; // How often the player gains points by moving.
    private static final float CAMERA_SLACK = 0.1f; // What percentage of the screen the player can move in before the camera follows.
    private static final float SPEED =70f; // Player movement speed.
    //Related to power-ups.
    private static final int DAMAGE_POWERUP_VALUE = 500;
    private static final int DAMAGE_POWERUP_TOTAL_LENGTH = 10000;
    private static final int IMMUNITY_POWERUP_LENGTH = 10000;
    private static final int TAKE_DAMAGE_INCREASE = 450;
    private static final int SPEED_POWERUP_TOTAL_LENGTH = 25000;
    private int speedMultiplier = 1;
    private static int timeBeforeRegen = 10000;
    private static double regenAmount = 0.03;
    private static int nonBoostedMaxHealth = 300;
    private static float enemyDamageMultiplier = 1;

    private static String difficulty = "Normal";

    // Movement calculation values
    private int previousDirectionX;
    private int previousDirectionY;
    private float distance;
    private long lastMovementScore;

    private HealthBar playerHealth;
    public long currentTime;
    private float splashTime;
    private long timeLastHit;
    private boolean doBloodSplash = false;

    private float defaultDamage = 20;

    private float playerDamage = 20;
    public long damageIncreaseStart;
    private boolean immune = false;
    public long takeMoreDamageStart;
    public long speedStart;
    public long immunityStart;


    /**
     * Generates a generic object within the game with animated frame(s) and a hit-box.
     * @param x         The x coordinate within the map to initialise the object at.
     * @param y         The y coordinate within the map to initialise the object at.
     * @param width     The size of the object in the x-axis.
     * @param height    The size of the object in the y-axis.
     * @param team      The team the player is on.
     */
    public Player(float x, float y, float width, float height, String team){
        super(x, y, width, height, team);
        lastMovementScore = 0;
        splashTime = 0;

        // Generate health
        setMaxHealth(nonBoostedMaxHealth);
        setCurrentHealth(getMaxHealth());
    }
    public void changeImage(Array<Texture> frames, float fps) throws Exception {
        super.changeImage(frames,frames.size-1);
        //Making the health bar.
        Array<Texture> sprites = new Array<>();
        sprites.add(new Texture("allyHealthBar.png"));
        createHealthBar();
        playerHealth.changeImage(sprites);
    }
    public void createHealthBar(){
        playerHealth  = new HealthBar(this);
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

        // Weather
        // The player moves at 0.5 times speed when inside bad weather.
        float weatherMovement = 1;
        for(int i = 0; i<screen.weatherArray.size; i++){
            if (this.overlaps(screen.weatherArray.get(i).hitBox)){
                weatherMovement = 0.5f;
            }
        }
        // Calculate collision && movement
        if (horizontal != 0 || vertical != 0){
            // Move the player, also change the amount the player
            move(speedMultiplier* weatherMovement *SPEED*horizontal,
                    speedMultiplier* weatherMovement *SPEED*vertical, Gdx.graphics.getDeltaTime());
            previousDirectionX = horizontal;
            previousDirectionY = vertical;
            if (safeMove(screen.getMain().edges)) {
                //Add points to the player when they move
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
        //Update the hitbox of the player after they have moved.
        updateHitboxPos();
        // Track distance travelled
        distance += Math.pow((Math.pow((x - oldPos.x),2f) + Math.pow((y - oldPos.y),2f)),0.5f)/10f;

        // Health-bar reduction
        if(currentHealth > 0){
            playerHealth.resize(currentHealth);
        }else{
            playerHealth = null;
            screen.gameEnd(false);
        }
        //All of the timers that go in the game: Power ups, blood splash, health regen.
        timerManager();
        // Camera Calculations
        ProcessCamera(screen, camera);


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
     * @param delta Allows for the standardised movement speed.
     */
    @Override
    public void move(float x, float y, float delta){
        this.x += x * delta;
        this.y += y * delta;
        // Healthbar moves with player
        playerHealth.move(this.x, this.y + height/2 + 2f, delta);
    }

    /**
     * Called when a projectile hits the college.
     * @param damage            The damage dealt by the projectile.
     */
    @Override
    public void takeDamage(float damage){
        timeLastHit = TimeUtils.millis();
        // If the immunity power up is active, take no damage.
        if (immune){
            damage = 0;
        }
        // The damage that the player takes changes depending on difficulty level
        // enemyDamageMultiplier changes depending on the difficulty methods.
        currentHealth -= damage * enemyDamageMultiplier;
        // Add the red tint to the player when hit.
        doBloodSplash = true;
    }

    /**
     * Manages all of the various timers that the player needs to keep track of.
     * Resets values to the default values, when the timers run out.
     *      - Red tint that the player has when they get hit.
     *      - Natural regeneration of health.
     *      - damage increase  power up {@link #damageIncrease()}
     *      - immunity power up {@link #immunityPowerup()}
     *      - temporary health increase {@link #takeMoreDamagePowerup()}
     *      - speed increase power up {@link #speedPowerup()}
     */
    public void timerManager(){
        // Blood splash calculations
        if(doBloodSplash){
            if(splashTime > 1){
                doBloodSplash = false;
                splashTime = 0;
            }else{
                splashTime += 1;
            }
        }
        currentTime = TimeUtils.millis();
        //If it has been 10 seconds since the player was last hit, then health will increase.
        if (currentTime - timeLastHit > timeBeforeRegen){
            currentHealth += regenAmount;
            //If current health goes above the max, then it will remain at max health.
            if(currentHealth > maxHealth) currentHealth = maxHealth;
            playerHealth.resize(currentHealth);
        }

        //Timing how long the GiveMoreDamage powerup lasts
        if(currentTime - damageIncreaseStart > DAMAGE_POWERUP_TOTAL_LENGTH){
            playerDamage = defaultDamage;
        }

        //Timing how long the Immunity powerup lasts
        if (currentTime - immunityStart > IMMUNITY_POWERUP_LENGTH){
            setImmune(false);
        }

        ////Timing how long the TakeMoreDamage powerup lasts

        if (TimeUtils.timeSinceMillis(takeMoreDamageStart) > DAMAGE_POWERUP_TOTAL_LENGTH){
            setMaxHealth(nonBoostedMaxHealth);
            if(currentHealth > maxHealth) currentHealth = maxHealth;
        }

        //Timing how long the Speed powerup lasts
        if (currentTime - speedStart > SPEED_POWERUP_TOTAL_LENGTH){
            speedMultiplier = 1;
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
        if (shader==null){generateShader();}
        // Generates the sprite, the image changes depending on the players current health.
        Texture frame = anim.getKeyFrame((currentHealth/maxHealth > 0.66f) ? 0 : ((currentHealth/maxHealth > 0.33f) ? 2 : 1), true);
        // Add a red tint if the player has been hit.
        if(doBloodSplash){
            batch.setShader(shader); // Set our grey-out shader to the batch
            //Rotate the image so that the ship faces the direction of travel.
        } float rotation = (float) Math.toDegrees(Math.atan2(previousDirectionY, previousDirectionX));

        // Draws sprite
        batch.draw(frame, x - width/2, y - height/2, width/2, height/2, width, height, 1f, 1f, rotation, 0, 0, frame.getWidth(), frame.getHeight(), false, false);
        batch.setShader(null);
    }

    /**
     * Draws the Player's health bar
     * @param batch     The batch used to draw player health.
     */
    public void drawHealthBar(SpriteBatch batch){
        if(!(playerHealth == null)) playerHealth.draw(batch, 0);
    }

    /**
     * Getter for distance travelled
     * @return total distance travelled.
     */
    public float getDistance() {
        return distance;
    }

    /**
     * Getter for player's current damage.
     * @return how much the player's projectiles damage.
     */
    public float getPlayerDamage() {
        return playerDamage;
    }

    //Power up methods.

    /**
     * Damage increases for a set amount of real world time.
     */
    public void damageIncrease(){
        this.playerDamage = DAMAGE_POWERUP_VALUE;
        this.damageIncreaseStart = TimeUtils.millis();
    }

    /**
     * Give the player max health.
     */
    public void giveMaxHealth(){
        this.setCurrentHealth(nonBoostedMaxHealth);
        playerHealth.resize(currentHealth);
    }

    /**
     * Gives the player immunity, projectiles no longer do damage for a set amount of time.
     */
    public void immunityPowerup(){
        this.immunityStart = TimeUtils.millis();
        setImmune(true);
    }

    /**
     * Getter for the immune value.
     * @return whether the player is immune from damage.
     */
    public boolean getImmune(){return immune;}

    /**
     * Setter for the immune value.
     * @param i change the attribute "immune" to the value of input.
     */
    public void setImmune(boolean i){immune = i;}

    /**
     * The player's maximum health increases temporarily
     * player is also given max health.
     */
    public void takeMoreDamagePowerup(){
        this.takeMoreDamageStart = TimeUtils.millis();
        setMaxHealth(TAKE_DAMAGE_INCREASE);
        setCurrentHealth(getMaxHealth());
    }

    /**
     * The player can travel 2 times faster temporarily.
     */
    public void speedPowerup(){
        this.speedStart = TimeUtils.millis();
        speedMultiplier = 2;
    }

    /**
     * Getter for the speed multiplier
     * @return the magnitude of the speed increase of the player when the speed powerup is active.
     */
    public int getSpeedMultiplier(){return speedMultiplier;}

    /**
     * Setter for maximum health, when {@link #takeMoreDamagePowerup()} isn't active.
     * @param mh    Set the value of maximum health without powerups.
     */
    public void setNonBoostedMaxHealth(int mh){nonBoostedMaxHealth = mh;}

    /**
     * Getter for nonBoostedMaxHealth
     * @return  Get the value of maximum health without power ups.
     */
    public int getNonBoostedMaxHealth(){return nonBoostedMaxHealth;}

    //Difficulty Methods
    /**
     * Sets the difficulty to easy
     */
    public void setEasy(){
        regenAmount = 0.06;
        timeBeforeRegen = 5000;
        enemyDamageMultiplier = 1;
        defaultDamage = 30;
        playerDamage = defaultDamage;
        setMaxHealth(400);
        setNonBoostedMaxHealth(getMaxHealth());
        setCurrentHealth(getMaxHealth());
        difficulty = "Easy";
    }

    /**
     * Sets the difficulty to Normal
     */
    public void setNormal(){
        regenAmount = 0.03;
        timeBeforeRegen = 10000;
        enemyDamageMultiplier = 1.5f;
        defaultDamage = 20;
        playerDamage = defaultDamage;
        setMaxHealth(300);
        setNonBoostedMaxHealth(getMaxHealth());
        setCurrentHealth(getMaxHealth());
        difficulty = "Normal";
    }

    /**
     * Sets the difficulty to hard.
     */
    public void setHard(){
        regenAmount = 0;
        timeBeforeRegen = 10000;
        enemyDamageMultiplier = 2f;
        defaultDamage = 15;
        playerDamage = defaultDamage;
        setMaxHealth(200);
        setNonBoostedMaxHealth(getMaxHealth());
        setCurrentHealth(getMaxHealth());
        difficulty = "Hard";
    }

    /**
     * Getter for the string difficulty.
     * see {@link #setEasy()}, {@link #setNormal()}, {@link #setHard()} for attribute values.
     * @return difficulty - implies what some of the other attribute values are set to.
     */
    public String getDifficulty(){ return difficulty;}

    /**
     * Getter for the health bar of the player.
     * @return playerHealth - allows for access to the HealthBar methods through the player
     */
    public HealthBar getPlayerHealth(){return playerHealth;}

    /**
     * Getter for doBloodSplash
     * @return  whether the player should be tinted red.
     */
    public boolean isDoBloodSplash(){return doBloodSplash;}
    /**
     * Debugging tool, prints certain attributes of the Player class.
     */
    public void printStats(){
        System.out.println("Regen: " + regenAmount);
        System.out.println("timeBeforeRegen: " + timeBeforeRegen);
        System.out.println("enemydmgmult: " + enemyDamageMultiplier);
        System.out.println("def dmg: " + defaultDamage);
        System.out.println("current dmg" + playerDamage);
        System.out.println("maxhealth: " + maxHealth);
        System.out.println("x: " + this.x + " y: " + y);
        System.out.println("Immune: " + this.immune);
        System.out.println("Current Health: " + this.currentHealth);
    }
}