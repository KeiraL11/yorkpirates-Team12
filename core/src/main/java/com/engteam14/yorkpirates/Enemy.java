package com.engteam14.yorkpirates;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;
import java.util.Objects;

import static java.lang.Math.abs;
import static java.lang.Math.random;

public class Enemy extends GameObject {
    private int loot;
    private HealthBar enemyBar;
    private long lastMovementChange;
    private static int movementChangeFreq = 2000;
    private float xDirection;
    private float yDirection;
    private float xGradient;
    private float yGradient;
    private static final float SPEED = 60;
    private int shootFrequency = 200;
    private long lastShotFired;
    private Array<Texture> bulletSprites = new Array<>();
    private Projectile newProjectile;

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
    public void changeSpawn(GameScreen screen, int lowerXbound, int upperxBound,
                            int lowerYbound, int upperYbound){
        boolean invalidSpawnLocation = false;
        while (!invalidSpawnLocation){
            this.x = MathUtils.random(lowerXbound, upperxBound);
            this.y = MathUtils.random(lowerYbound, upperYbound);
            //If the enemy spawns in the edges, then spawn in a new (x,y) location.
            invalidSpawnLocation = safeMove(screen.getMain().edges);
        }
    }

    public void changeImage(Array<Texture> sprites) throws Exception {
        super.changeImage(sprites);
        Array<Texture> healthBarSprite = new Array<>();
        healthBarSprite.add(new Texture("enemyHealthBar.png"));
        setEnemyBar();
        enemyBar.changeImage(healthBarSprite);
        bulletSprites.add(new Texture("tempProjectile.png"));
    }
    public void setEnemyBar(){enemyBar = new HealthBar(this);}
    public void update(GameScreen screen) throws Exception {
        float playerX = screen.getPlayer().x;
        float playerY = screen.getPlayer().y;
        Vector2 oldPos = new Vector2(x, y);
        boolean nearPlayer = abs(this.x - playerX) < (Gdx.graphics.getWidth()/15f)
                && abs(this.y - playerY) < (Gdx.graphics.getHeight()/10f);
        //Shoot the player is they are close
        if (nearPlayer){
            if (!Objects.equals(team, GameScreen.playerTeam)){
                if (TimeUtils.timeSinceMillis(lastShotFired) > shootFrequency){
                    lastShotFired = TimeUtils.millis();
                    newProjectile = new Projectile(this, playerX, playerY, team);
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
        xDirection = (float) (SPEED * xGradient / normalScaling);
        yDirection = (float) (SPEED * yGradient / normalScaling);

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
    @Override
    public void move(float x, float y, float delta){
        this.x += x * delta;
        this.y += y * delta;
        enemyBar.move(this.x, this.y + height/2 + 2, delta);
    }

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
    private void destroy(GameScreen screen){ screen.enemies.removeValue(this,true);}
}
