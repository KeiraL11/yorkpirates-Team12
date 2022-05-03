package com.engteam14.yorkpirates;


public class Weather extends GameObject {

    /**
     * Weather, as the player moves through bad weather, their movement speed is slowed down.
     * @param x         The x coordinate within the map to initialise the object at.
     * @param y         The y coordinate within the map to initialise the object at.
     * @param width     The size of the object in the x-axis.
     * @param height    The size of the object in the y-axis.
     * @param team      The team the player is on.
     */
    public Weather(float x, float y, float width, float height, String team) {
        super(x, y, width, height, team);
    }

}