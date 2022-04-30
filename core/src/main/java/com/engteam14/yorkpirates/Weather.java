package com.engteam14.yorkpirates;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


public class Weather extends GameObject {

    /**
     * Generates a generic object within the game with animated frame(s) and a hit-box.
     * @param x         The x coordinate within the map to initialise the object at.
     * @param y         The y coordinate within the map to initialise the object at.
     * @param scale     The size we wish the sprite to be scaled at.
     * @param width     The size of the object in the x-axis.
     * @param height    The size of the object in the y-axis.
     * @param team      The team the player is on.
     */
    public Weather(float x, float y, float scale, float width, float height, String team) {
        super(x, y, width, height, team);
    }

}