package com.engteam14.yorkpirates;

import java.util.Objects;

public class PowerUps extends GameObject{
    public float x;
    public float y;
    public float width;
    public float height;
    public String powerup;

    /**
     * Generates a generic object within the game with animated frame(s) and a hit-box.
     * @param x         The x coordinate within the map to initialise the object at.
     * @param y         The y coordinate within the map to initialise the object at.
     * @param width     width of the power up.
     * @param height    heigh of the power up.
     * @param powerup   The type of power up.
     */
    PowerUps(float x, float y, float width, float height, String powerup){
        super(x, y, width, height, "");
        this.powerup = powerup;
    }
    /**
     * This will see if each power up has overlapped the player's hit box. If so, it will call its method.
     * @param screen    The main game screen.
     */
    void update(GameScreen screen){
        if(overlaps(screen.getPlayer().hitBox)){
            destroy(screen);
            if(Objects.equals(this.powerup, "GiveMoreDamage")){
                screen.getPlayer().damageIncrease();
            }
            if(Objects.equals(this.powerup, "HealthRestore")){
                screen.getPlayer().giveMaxHealth();
            }
            if(Objects.equals(this.powerup, "Immunity")){
                screen.getPlayer().immunityPowerup();
            }
            if(Objects.equals(this.powerup, "TakeMoreDamage")){
                screen.getPlayer().takeMoreDamagePowerup();
            }
            if(Objects.equals(this.powerup, "Speed")){
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