package com.engteam14.yorkpirates;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Objects;

import static java.lang.Math.abs;

public class Enemy extends GameObject {
    private final int loot;
    private HealthBar enemyBar;
    private long lastMovementChange;
    private static final int movementChangeFreq = 2000;
    private float xGradient;
    private float yGradient;
    private static final float SPEED = 60;
    private long lastShotFired;
    private final Array<Texture> bulletSprites = new Array<>();

    /**
     * Creates an enemy object, enemys are different from colleges, in that
     * they can move.
     *
     * @param x      The x coordinate within the map to initialise the object at.
     * @param y      The y coordinate within the map to initialise the object at.
     * @param width  The size of the object in the x-axis.
     * @param height The size of the object in the y-axis.
     * @param team   The team the object is on.
     */
    public Enemy(float x, float y, float width, float height, String team) {
        super(x, y, width, height, team);
        setMaxHealth(50);
        setCurrentHealth(getMaxHealth());
        loot = MathUtils.random(5,20);
    }

    /**
     * Allows for random location spawning of the enemy
     * @param screen        the main game screen
     * @param lowerXbound   the lower x bound location that the enemy can spawn in.
     * @param upperxBound   the upper x bound location that the enemy can spawn in.
     * @param lowerYbound   the lower y bound location that the enemy can spawn in.
     * @param upperYbound   the upper y bound location that the enemy can spawn in.
     */
    public void changeSpawn(GameScreen screen, int lowerXbound, int upperxBound,
                            int lowerYbound, int upperYbound){
        boolean invalidSpawnLocation = false;
        // choose a new location until the location is valid.
        while (!invalidSpawnLocation){
            this.x = MathUtils.random(lowerXbound, upperxBound);
            this.y = MathUtils.random(lowerYbound, upperYbound);
            //If the enemy spawns in the edges, then spawn in a new (x,y) location.
            invalidSpawnLocation = safeMove(screen.getMain().edges);
        }
    }

    /**
     * Set the images for the enemy. Also creates the health bar.
     * creates its own image, health bar image, projectile image.
     * @param sprites       array of textures that the enemy uses
     * @throws Exception    inherited from parent class.
     */
    public void changeImage(Array<Texture> sprites) throws Exception {
        super.changeImage(sprites);
        Array<Texture> healthBarSprite = new Array<>();
        healthBarSprite.add(new Texture("enemyHealthBar.png"));
        createHealthBar();
        enemyBar.changeImage(healthBarSprite);
        bulletSprites.add(new Texture("tempProjectile.png"));
    }

    /**
     * Creates the health bar.
     */
    public void createHealthBar(){enemyBar = new HealthBar(this);}

    /**
     * Getter for the health bar of the enemy
     * @return  health bar of the enemy.
     */
    public HealthBar getEnemyBar() {return enemyBar;}
    /**
     * Called every frame, allows for movement, shooting, death.
     * @param screen        the main game screen
     * @throws Exception    inherited from changeImage()
     */
    public void update(GameScreen screen) throws Exception {
        float playerX = screen.getPlayer().x;
        float playerY = screen.getPlayer().y;
        Vector2 oldPos = new Vector2(x, y);
        boolean nearPlayer = abs(this.x - playerX) < (Gdx.graphics.getWidth()/15f)
                && abs(this.y - playerY) < (Gdx.graphics.getHeight()/10f);
        //Shoot the player is they are close
        if (nearPlayer){
            if (!Objects.equals(team, GameScreen.playerTeam)){
                int shootFrequency = 400;
                if (TimeUtils.timeSinceMillis(lastShotFired) > shootFrequency){
                    lastShotFired = TimeUtils.millis();
                    Projectile newProjectile = new Projectile(this, playerX, playerY, team);
                    newProjectile.changeImage(bulletSprites);
                    screen.projectiles.add(newProjectile);
                }
            }
        }
        //This has been separated for the sake of readability, could just go into an else loop.
        //If the player is far away from the enemy, the enemy will try to "chase" the player.
        if (!nearPlayer){
            xGradient = playerX - this.x;
            yGradient = playerY - this.y;
        } else {
            if (TimeUtils.timeSinceMillis(lastMovementChange) > movementChangeFreq) {
                lastMovementChange = TimeUtils.millis();
                xGradient = MathUtils.randomSign() * MathUtils.random();
                yGradient = MathUtils.randomSign() * MathUtils.random();
            }
        }
        // Normalise the vectors, so the overall vector is 1, then multiply is by SPEED
        // So the overall vector is equal to SPEED
        double normalScaling = Math.sqrt(xGradient * xGradient + yGradient * yGradient);
        float xDirection = (float) (SPEED * xGradient / normalScaling);
        float yDirection = (float) (SPEED * yGradient / normalScaling);

        move(xDirection, yDirection, Gdx.graphics.getDeltaTime());
        //Check for collision with the map.
        if (!safeMove(screen.getMain().edges)){
            Vector2 newPos = new Vector2(x, y);
            x = oldPos.x;
            if (!safeMove(screen.getMain().edges)){
                x = newPos.x;
                y = oldPos.y;
                if (!safeMove(screen.getMain().edges)){
                    x = oldPos.x;
                }
            }
        }
        //Update the hitbox after the enemy has moved.
        this.updateHitboxPos();
        if (currentHealth > 0){
            enemyBar.resize(currentHealth);
        } else{
            screen.loot.Add(loot);
            enemyBar = null;
            destroy(screen);
        }
    }

    /**
     * returns whether the enemy movement is valid.
     * @param edges     2D array of boolean edges, generated in the YorkPirates() class.
     * @return          Whether the enemy collides with the edges.
     */
    private Boolean safeMove(Array<Array<Boolean>> edges){
        try {
            return (
                    edges.get((int) ((y + height / 2) / 16)).get((int) ((x + width / 2) / 16)) &&
                            edges.get((int) ((y + height / 2) / 16)).get((int) ((x - width / 2) / 16)) &&
                            edges.get((int) ((y - height / 2) / 16)).get((int) ((x + width / 2) / 16)) &&
                            edges.get((int) ((y - height / 2) / 16)).get((int) ((x - width / 2) / 16))
            );
            //Due to how the maths works for this method, the rounding can result in a IndexOutOfBoundsException
        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    /**
     * Moves the enemy and it's health bar.
     * @param x     The amount to move the object within the x-axis.
     * @param y     The amount to move the object within the y-axis.
     * @param delta standardises the movement speed of the enemy.
     */
    @Override
    public void move(float x, float y, float delta){
        this.x += x * delta;
        this.y += y * delta;
        enemyBar.move(this.x, this.y + height/2 + 2, delta);
    }

    /**
     * Draws the enemy and health bar on the screen
     * @param batch         The batch to draw the object within.
     * @param elapsedTime   The current time the game has been running for.
     */
    @Override
    public void draw(SpriteBatch batch, float elapsedTime){
        if(shader == null){generateShader();}
        // Rotates the boat to face the direction they are travelling.
        float rotation = (float) Math.toDegrees(Math.atan2(yGradient, xGradient));
        batch.draw(sprite, x - width/2, y - height/2, width/2, height/2, width, height,
                1f, 1f, rotation, 0, 0,
                sprite.getWidth(), sprite.getHeight(), false, false);
        // Draw the health bar.
        enemyBar.draw(batch, 0);
    }

    /**
     * called when the enemy needs to be destroyed.
     * @param screen    main game screen.
     */
    private void destroy(GameScreen screen){
        screen.enemies.removeValue(this,true);
    }
}
