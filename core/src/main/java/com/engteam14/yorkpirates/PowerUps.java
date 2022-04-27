package com.engteam14.yorkpirates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PowerUps extends GameObject{
    public float x;
    public float y;
    public float width;
    public float height;
    public String powerup;

    private final Array<Texture> powerImages;

    Texture sprite;
    Rectangle hitBox;
    Animation<Texture> anim;

    /**
     * Generates a generic object within the game with animated frame(s) and a hit-box.
     * @param frames    The animation frames, or a single sprite.
     * @param fps       The number of frames to be displayed per second.
     * @param x         The x coordinate within the map to initialise the object at.
     * @param y         The y coordinate within the map to initialise the object at.
     * @param powerup   The powerup.
     */
    PowerUps(Array<Texture> frames, float fps, float x, float y, String powerup){
        super(frames, fps, x, y, (float) (frames.get(0).getWidth()*0.05), (float)(frames.get(0).getHeight()*0.05), "");

        //changeImage(frames,fps);
        this.powerup = powerup;
        powerImages = new Array<>();
        for(int i = 0; i < frames.size; i++) {
            powerImages.add(frames.get(i));
        }
    }
    /**
     * This will see if each powerup has overlapped the player's hitbox. If so, it will call its method.
     * @param screen    The main game screen.
     */
    void update(GameScreen screen){
        if(overlaps(screen.getPlayer().hitBox)){
            destroy(screen);
            if (this.powerup == "GiveMoreDamage"){
                screen.getPlayer().damageIncrease();
            }
            if(this.powerup == "HealthRestore"){
                screen.getPlayer().giveMaxHealth();
            }
            if(this.powerup == "Immunity"){
                screen.getPlayer().immunityPowerup();
            }
            if(this.powerup == "TakeMoreDamage"){
                screen.getPlayer().takeMoreDamagePowerup();
            }
            if(this.powerup == "Speed"){
                screen.getPlayer().speedPowerup();
            }
        }
    }
    /**
     * Called when the powerup needs to be destroyed.
     * @param screen    The main game screen.
     */
    private void destroy(GameScreen screen){
        screen.powerups.removeValue(this,true);
    }
}
