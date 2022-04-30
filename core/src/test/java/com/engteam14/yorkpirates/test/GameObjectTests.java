package com.engteam14.yorkpirates.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.engteam14.yorkpirates.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class GameObjectTests {
    @Test
    public void testDamageGameObject2(){
        float delta = 0.001f;
        GameObject2 gameObject2 = new GameObject2(10,15,64,64, "");
        float oldHealth = gameObject2.getHealth();
        gameObject2.takeDamage(5);
        assertEquals(oldHealth - 5, gameObject2.getHealth(), delta);
    }
    @Test
    public void oneEqualsOne(){
        assertEquals(1,1);
    }
    @Test
    public void testDamageCollege(){
        College college = new College(50, 60, 100, 100, "Alcuin", "ENEMY");
        float oldHealth = college.currentHealth;
        college.takeDamage(50);
        assertEquals(oldHealth - 50, college.currentHealth, 0.0001);
        assertEquals(true, college.getDoBloodSplash());
    }
    @Test
    public void testPlayerMoveLeft(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        player.move(10, 0, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(60, player.x, 0.001);
        assertEquals(50, player.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = player.getPlayerHealth();
        assertEquals(player.x, healthBar.x, 0.001);
        assertEquals(player.y+player.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testPlayerImmunePowerUp(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        // Before power up
        assertEquals(false, player.getImmune());
        player.immunityPowerup();
        // After power up
        assertEquals(true, player.getImmune());
    }
    @Test
    public void testPlayerSpeedPowerUp() throws InterruptedException {
        Player player = new Player(50,50, 64, 64, "PLAYER");
        player.createHealthBar();
        int oldSpeedMultiplier = player.getSpeedMultiplier();
        // Before power up
        assertEquals(1, player.getSpeedMultiplier());
        player.speedPowerup();
        // After power up
        assertEquals(2, player.getSpeedMultiplier());
        player.timerManager();
        assertEquals(2, player.getSpeedMultiplier());
        Thread.sleep(25000);
        player.timerManager();
        assertEquals(1, player.getSpeedMultiplier());

    }
    @Test
    public void testDifficultyChange(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.setHard();
        assertEquals("Hard", player.getDifficulty());
    }
    @Test
    public void testPlayerDamage(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        float oldHealth = player.currentHealth;
        player.takeDamage(20);
        assertEquals(oldHealth - 20, player.currentHealth, 0.0001);
    }
    @Test
    public void overlapTest(){
        GameObject object1 = new GameObject(100,200,64, 64, "");
        GameObject object2 = new GameObject(110,200,64, 64, "");
        assertTrue(object1.overlaps(object2.getHitBox()));
    }
    @Test
    public void healthBarResize(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        HealthBar healthBar = new HealthBar(player);
        player.takeDamage(50);
        healthBar.resize(player.currentHealth);
        assertEquals(healthBar.getStartWidth() * player.currentHealth/player.maxHealth,
                healthBar.width, 0.0001);
    }
}
