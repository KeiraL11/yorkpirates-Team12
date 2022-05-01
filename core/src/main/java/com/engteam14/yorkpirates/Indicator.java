package com.engteam14.yorkpirates;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Indicator extends GameObject{

    private final Player player;
    private final College college;

    private Vector2 gradient;
    private boolean visible;

    /**
     * Arrow which points to the college locations, green for ally colleges, red for enemy colleges.
     * @param college   The college that the indicator is associated with.
     * @param player    player, so that the arrows can change gradient and location, with the player.
     * @param width     width of the arrow.
     * @param height    height of the arrow.
     */
    public Indicator(College college, Player player, float width, float height) {
        super(player.x, player.y, width, height, college.team);

        this.player = player;
        this.college = college;
        gradient = updateGradient();
        visible = false;
        move();
    }

    /**
     * Called when drawing the Indicator.
     * @param batch         The batch to draw the player within.
     * @param elapsedTime   The current time the game has been running for.
     */
    @Override
    public void draw(SpriteBatch batch, float elapsedTime){
        // Draw the indicator if visible
        if(visible){
            float rotation = (float) Math.toDegrees(Math.atan2(gradient.y,gradient.x));
            batch.draw(sprite, x - width/2, y - height/2, width/2, height/2, width, height, 1f, 1f, rotation, 0, 0, sprite.getWidth(), sprite.getHeight(), false, false);
        }
    }

    /**
     * Called when updating the gradient of the Indicator.
     */
    public Vector2 updateGradient(){
        // Calculate the gradient to draw the indicator at.
        float changeInX = college.x - player.x;
        float changeInY = college.y - player.y;
        float scaleFactor = max(abs(changeInX),abs(changeInY));
        float dx = changeInX / scaleFactor;
        float dy = changeInY / scaleFactor;

        return (new Vector2(dx, dy));
    }

    /**
     * Moves the object within the x and y-axis of the game world.
     */
    void move(){
        gradient = updateGradient();
        this.x = player.x + (50 * gradient.x);
        this.y = player.y + (50 * gradient.y);
    }

    /**
     * Set whether the indicator is visible.
     * @param visible   Whether the indicator is visible.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
