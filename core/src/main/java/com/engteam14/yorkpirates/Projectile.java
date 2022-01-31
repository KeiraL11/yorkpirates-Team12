package com.engteam14.yorkpirates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

import static java.lang.Math.*;

public class Projectile extends GameObject{

    private float distanceTravelled;
    private final GameObject origin;

    private final float dx;
    private final float dy;
    private final float projectileSpeed; // Projectile movement speed.

    private static final float projectileDamage = 20f; // Projectile damage.
    private static final float maxDistance = 7500f; // Projectile max range.

    /**
     * Generates a projectile object within the game with animated frame(s) and a hit-box.
     * @param frames    The animation frames, or a single sprite.
     * @param fps       The number of frames to be displayed per second.
     * @param origin    The object which the projectile originates from.
     * @param goal_x    The x coordinate within the map the object is moving towards.
     * @param goal_y    The y coordinate within the map the object is moving towards.
     * @param team      The team of the projectile.
     */
    public Projectile(Array<Texture> frames, float fps, GameObject origin, float goal_x, float goal_y, String team) {
        super(frames, fps, origin.x, origin.y, 5f,5f,team);

        this.origin = origin;

        float changeInX = goal_x - origin.x;
        float changeInY = goal_y - origin.y;
        float scaleFactor = max(abs(changeInX),abs(changeInY));
        dx = changeInX / scaleFactor;
        dy = changeInY / scaleFactor;

        move(origin.hitBox.width * 2* dx, origin.hitBox.height * 2 * dy);

        distanceTravelled = 0;
        if(Objects.equals(team, GameScreen.playerTeam)){
            projectileSpeed = 150f;
        }else{
            projectileSpeed = 50f;
        }
    }

    /**
     * Called once per frame. Used to perform calculations such as projectile movement and collision detection.
     * @param screen    The main game screen.
     */
    public void update(GameScreen screen){
        // Movement Calculations
        move(projectileSpeed*dx, projectileSpeed*dy);
        distanceTravelled += projectileSpeed;

        if(origin == screen.player){
            for(int i = 0; i < screen.colleges.size; i++) {
                if (overlaps(screen.colleges.get(i).hitBox)){
                    if(!Objects.equals(team, screen.colleges.get(i).team)){ // Checks if projectile and college are on the same time
                        screen.colleges.get(i).takeDamage(screen,projectileDamage,team);
                    }
                    destroy(screen);
                }
            }
        }else{
            if (overlaps(screen.player.hitBox)){
                if(!Objects.equals(team, GameScreen.playerTeam)){ // Checks if projectile and player are on the same time
                    screen.player.takeDamage(screen,projectileDamage,team);
                }
                destroy(screen);
            }
        }

        if(distanceTravelled > maxDistance){
            destroy(screen);
        }
    }

    /**
     * Called when the projectile needs to be destroyed.
     * @param screen    The main game screen.
     */
    private void destroy(GameScreen screen){
        screen.projectiles.removeValue(this,true);
    }
}
