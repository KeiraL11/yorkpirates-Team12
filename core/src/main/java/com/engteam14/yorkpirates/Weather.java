package com.engteam14.yorkpirates;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


public class Weather extends GameObject {

    /**
     * Generates a generic object within the game with animated frame(s) and a hit-box.
     * @param sprites    The animation frames, or a single sprite.
     * @param x         The x coordinate within the map to initialise the object at.
     * @param y         The y coordinate within the map to initialise the object at.
     * @param scale     The size we wish the sprite to be scaled at.
     * @param width     The size of the object in the x-axis.
     * @param height    The size of the object in the y-axis.
     * @param team      The team the player is on.
     */
    public Weather(Array<Texture> sprites, float x, float y, float scale, float width, float height, String team) {
        super(sprites, 0, x, y, sprites.get(0).getWidth()*scale, sprites.get(0).getHeight()*scale, team);
    }
}