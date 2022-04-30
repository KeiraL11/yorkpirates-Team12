package com.engteam14.yorkpirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

import static java.lang.Math.*;

public class Projectile extends GameObject{

    private final float maxDistance; // Projectile max range.
    private float distanceTravelled;
    private final GameObject origin;

    private final float dx;
    private final float dy;
    private final float projectileSpeed; // Projectile movement speed.

    private static final float projectileDamage = 20f; // Projectile damage.

    /**
     * Generates a projectile object within the game with animated frame(s) and a hit-box.
     * @param origin    The object which the projectile originates from.
     * @param goal_x    The x coordinate within the map the object is moving towards.
     * @param goal_y    The y coordinate within the map the object is moving towards.
     * @param team      The team of the projectile.
     */
    public Projectile(GameObject origin, float goal_x, float goal_y, String team) {
        super(origin.x, origin.y, 5f,5f,team);
        this.origin = origin;

        // Speed calculations
        if(Objects.equals(team, GameScreen.playerTeam)){
            projectileSpeed = 150f;
        }else{
            projectileSpeed = 100f;
        }

        // Movement calculations
        float changeInX = goal_x - origin.x;
        float changeInY = goal_y - origin.y;
        float scaleFactor = max(abs(changeInX),abs(changeInY));
        dx = changeInX / scaleFactor;
        dy = changeInY / scaleFactor;

        distanceTravelled = 0;
        float rangeModifier = min(origin.hitBox.width,origin.hitBox.height);
        maxDistance = rangeModifier * projectileSpeed;
    }

    /**
     * Called once per frame. Used to perform calculations such as projectile movement and collision detection.
     * @param screen    The main game screen.
     */
    public void update(GameScreen screen) throws Exception {
        // Movement Calculations
        float xMove = projectileSpeed*dx;
        float yMove = projectileSpeed*dy;
        distanceTravelled += projectileSpeed;
        move(xMove, yMove, Gdx.graphics.getDeltaTime());

        // Hit calculations
        if(origin == screen.getPlayer()){
            for(int i = 0; i < screen.colleges.size; i++) {
                if (overlaps(screen.colleges.get(i).hitBox)){
                    if(!Objects.equals(team, screen.colleges.get(i).team)){ // Checks if projectile and college are on the same time
                        screen.colleges.get(i).takeDamage(screen.getPlayer().getPlayerDamage(),team);
                    }
                    destroy(screen);
                }
            }
            for(int i = 0; i < screen.enemies.size; i++) {
                if (overlaps(screen.enemies.get(i).hitBox)){
                    if(!Objects.equals(team, screen.enemies.get(i).team)){ // Checks if projectile and college are on the same time
                        screen.enemies.get(i).takeDamage(screen.getPlayer().getPlayerDamage(),team);
                    }
                    destroy(screen);
                }
            }
        }else{
            if (overlaps(screen.getPlayer().hitBox)){
                if(!Objects.equals(team, GameScreen.playerTeam)){ // Checks if projectile and player are on the same time
                    screen.getPlayer().takeDamage(projectileDamage,team);
                }
                destroy(screen);
            }
        }

        // Destroys after max travel distance
        if(distanceTravelled > maxDistance) destroy(screen);
    }

    /**
     * Called when the projectile needs to be destroyed.
     * @param screen    The main game screen.
     */
    private void destroy(GameScreen screen){
        screen.projectiles.removeValue(this,true);
    }
}
