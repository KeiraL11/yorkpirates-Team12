package com.engteam14.yorkpirates.tests;
import com.engteam14.yorkpirates.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


// Made a new class for power up tests because a lot of these require thread.sleep.
@RunWith(GdxTestRunner.class)
public class PowerupTests {
    // Power Up tests
    @Test
    public void testPlayerGiveMaxHealth(){
        float delta = 0.0001f;
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.setEasy();
        float oldHealth = player.currentHealth;
        player.createHealthBar();
        player.takeDamage(50);
        assertEquals(player.maxHealth - 50, player.currentHealth, delta);
        assertEquals(oldHealth - 50, player.currentHealth, delta);
        player.getPlayerHealth().resize(player.currentHealth);
        assertEquals(player.getPlayerHealth().getStartWidth() * player.currentHealth/player.maxHealth,
                player.getPlayerHealth().width, delta);
        player.giveMaxHealth();
        assertEquals(player.maxHealth, player.currentHealth, delta);
        assertEquals(player.getPlayerHealth().getStartWidth(), player.getPlayerHealth().width, delta);
    }
    @Test
    public void testPlayerTakeMoreDamagePowerup(){
        float delta = 0.0001f;
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.setEasy();
        float oldHealth = player.currentHealth;
        player.createHealthBar();
        player.takeDamage(50);
        assertEquals(player.maxHealth - 50, player.currentHealth, delta);
        assertEquals(oldHealth - 50, player.currentHealth, delta);
        player.getPlayerHealth().resize(player.currentHealth);
        assertEquals(player.getPlayerHealth().getStartWidth() * player.currentHealth/player.maxHealth,
                player.getPlayerHealth().width, delta);
        player.takeMoreDamagePowerup();
        assertEquals(player.maxHealth, player.currentHealth, delta);
        assertEquals(450, player.maxHealth);
        player.getPlayerHealth().resize(player.currentHealth);
        assertEquals((int) player.getPlayerHealth().getStartWidth() * player.maxHealth/player.getNonBoostedMaxHealth(),
                player.getPlayerHealth().width, delta);

        player = new Player(50, 50, 64, 64, "PLAYER");
        player.setNormal();
        oldHealth = player.currentHealth;
        player.createHealthBar();
        player.takeDamage(50);
        assertEquals(player.maxHealth - 75, player.currentHealth, delta);
        assertEquals(oldHealth - 75, player.currentHealth, delta);
        player.getPlayerHealth().resize(player.currentHealth);
        assertEquals(player.getPlayerHealth().getStartWidth() * player.currentHealth/player.maxHealth,
                player.getPlayerHealth().width, delta);
        player.takeMoreDamagePowerup();
        assertEquals(player.maxHealth, player.currentHealth, delta);
        assertEquals(450, player.maxHealth);
        player.getPlayerHealth().resize(player.currentHealth);
        assertEquals((int) player.getPlayerHealth().getStartWidth() * player.maxHealth/player.getNonBoostedMaxHealth(),
                player.getPlayerHealth().width, delta);
    }
    @Test
    public void testTakeMoreDamagePowerupTimer() throws InterruptedException {
        float delta = 0.0001f;
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.setEasy();
        player.createHealthBar();
        assertEquals(player.maxHealth, player.currentHealth, delta);
        player.takeDamage(20);
        assertEquals(player.maxHealth - 20, player.currentHealth, delta);
        player.takeMoreDamagePowerup();
        assertEquals(450, player.maxHealth);
        assertEquals(450, player.currentHealth, delta);
        player.timerManager();
        assertEquals(450, player.maxHealth);
        assertEquals(450, player.currentHealth, delta);
        player.takeDamage(50);
        Thread.sleep(9500);
        player.timerManager();
        assertEquals(450,player.maxHealth);
        //Delta of 1, because passive regen can increase the health very slightly.
        assertEquals(400, player.currentHealth, 1);
        Thread.sleep(500);
        player.timerManager();
        assertEquals(player.getNonBoostedMaxHealth(), player.maxHealth);
    }
    @Test
    public void testPlayerImmunePowerUp() throws InterruptedException {
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        // Before power up
        assertFalse(player.getImmune());
        player.immunityPowerup();
        // After power up
        assertTrue(player.getImmune());
        player.takeDamage(50);
        //Player shouldn't take any damage when immune
        assertEquals(player.maxHealth, player.currentHealth, 0.0001f);
        player.timerManager();
        Thread.sleep(9500);
        player.timerManager();
        assertTrue(player.getImmune());
        Thread.sleep(500);
        player.timerManager();
        assertFalse(player.getImmune());
        player.takeDamage(50);
        //Player should be able to take damage again.
        assertEquals(player.maxHealth - 50, player.currentHealth, 0.0001f);
    }
    @Test
    public void testPlayerSpeedPowerUp() throws InterruptedException {
        Player player = new Player(50,50, 64, 64, "PLAYER");
        player.createHealthBar();
        // Before power up
        assertEquals(1, player.getSpeedMultiplier());
        player.speedPowerup();
        // After power up
        assertEquals(2, player.getSpeedMultiplier());
        player.timerManager();
        assertEquals(2, player.getSpeedMultiplier());
        Thread.sleep(24500);
        player.timerManager();
        assertEquals(2, player.getSpeedMultiplier());
        Thread.sleep(500);
        player.timerManager();
        assertEquals(1, player.getSpeedMultiplier());
    }
    @Test
    public void testPlayerDamagePowerup() throws InterruptedException {
        float delta = 0.0001f;
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        player.setEasy();
        assertEquals(30, player.getPlayerDamage(), delta);
        player.damageIncrease();
        assertEquals(500, player.getPlayerDamage(), delta);
        player.timerManager();
        assertEquals(500, player.getPlayerDamage(), delta);
        Thread.sleep(9500);
        player.timerManager();
        assertEquals(500, player.getPlayerDamage(), delta);
        Thread.sleep(500);
        player.timerManager();
        assertEquals(30, player.getPlayerDamage(), delta);
    }
}
