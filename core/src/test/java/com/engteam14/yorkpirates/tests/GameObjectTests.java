package com.engteam14.yorkpirates.tests;

import com.engteam14.yorkpirates.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class GameObjectTests {
    @Test
    public void oneEqualsOne(){
        assertEquals(1,1);
    }
    //Damage Tests
    @Test
    public void testDamageCollege(){
        College college = new College(50, 60, 100, 100, "Alcuin", "ENEMY");
        float oldHealth = college.currentHealth;
        college.takeDamage(50);
        assertEquals(oldHealth - 50, college.currentHealth, 0.0001);
        assertTrue(college.getDoBloodSplash());
    }
    @Test
    public void testPlayerDamage(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.setEasy();
        float oldHealth = player.currentHealth;
        player.takeDamage(20);
        assertEquals(oldHealth - 20, player.currentHealth, 0.0001);
    }
    @Test
    public void testEnemyDamage(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.setHard();
        Enemy enemy = new Enemy(100, 100, 32, 32, "ENEMY");
        float oldHealth = enemy.currentHealth;
        float playerdmg = player.getPlayerDamage();
        enemy.takeDamage(playerdmg);
        assertEquals(enemy.maxHealth - playerdmg, oldHealth - playerdmg, 0.0001f);
    }
    //Movement tests
    @Test
    public void testPlayerMoveUp(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        player.move(0, 10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(50, player.x, 0.001);
        assertEquals(60, player.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = player.getPlayerHealth();
        assertEquals(player.x, healthBar.x, 0.001);
        assertEquals(player.y+player.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testPlayerMoveDown(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        player.move(0, -10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(50, player.x, 0.001);
        assertEquals(40, player.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = player.getPlayerHealth();
        assertEquals(player.x, healthBar.x, 0.001);
        assertEquals(player.y+player.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testPlayerMoveLeft(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        player.move(-10, 0, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(40, player.x, 0.001);
        assertEquals(50, player.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = player.getPlayerHealth();
        assertEquals(player.x, healthBar.x, 0.001);
        assertEquals(player.y+player.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testPlayerMoveRight(){
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
    public void testPlayerMoveUpLeft(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        player.move(-10, 10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(40, player.x, 0.001);
        assertEquals(60, player.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = player.getPlayerHealth();
        assertEquals(player.x, healthBar.x, 0.001);
        assertEquals(player.y+player.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testPlayerMoveUpRight(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        player.move(10, 10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(60, player.x, 0.001);
        assertEquals(60, player.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = player.getPlayerHealth();
        assertEquals(player.x, healthBar.x, 0.001);
        assertEquals(player.y+player.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testPlayerMoveDownLeft(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        player.move(-10, -10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(40, player.x, 0.001);
        assertEquals(40, player.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = player.getPlayerHealth();
        assertEquals(player.x, healthBar.x, 0.001);
        assertEquals(player.y+player.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testPlayerMoveDownRight(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        player.move(10, -10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(60, player.x, 0.001);
        assertEquals(40, player.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = player.getPlayerHealth();
        assertEquals(player.x, healthBar.x, 0.001);
        assertEquals(player.y+player.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testGameObjectMoveUp(){
        GameObject gameObject = new GameObject(50, 50, 64, 64, "");
        gameObject.move(0, 10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(50, gameObject.x, 0.001);
        assertEquals(60, gameObject.y, 0.001);
    }
    @Test
    public void testGameObjectMoveDown(){
        GameObject gameObject = new GameObject(50, 50, 64, 64, "");
        gameObject.move(0, -10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(50, gameObject.x, 0.001);
        assertEquals(40, gameObject.y, 0.001);
    }
    @Test
    public void testGameObjectMoveLeft(){
        GameObject gameObject = new GameObject(50, 50, 64, 64, "");
        gameObject.move(-10, 0, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(40, gameObject.x, 0.001);
        assertEquals(50, gameObject.y, 0.001);
    }
    @Test
    public void testGameObjectMoveRight(){
        GameObject gameObject = new GameObject(50, 50, 64, 64, "");
        gameObject.move(10, 0, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(60, gameObject.x, 0.001);
        assertEquals(50, gameObject.y, 0.001);
    }
    @Test
    public void testGameObjectMoveUpLeft(){
        GameObject gameObject = new GameObject(50, 50, 64, 64, "");
        gameObject.move(-10, 10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(40, gameObject.x, 0.001);
        assertEquals(60, gameObject.y, 0.001);
    }
    @Test
    public void testGameObjectMoveUpRight(){
        GameObject gameObject = new GameObject(50, 50, 64, 64, "");
        gameObject.move(10, 10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(60, gameObject.x, 0.001);
        assertEquals(60, gameObject.y, 0.001);
    }
    @Test
    public void testGameObjectMoveDownLeft(){
        GameObject gameObject = new GameObject(50, 50, 64, 64, "");
        gameObject.move(-10, -10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(40, gameObject.x, 0.001);
        assertEquals(40, gameObject.y, 0.001);
    }
    @Test
    public void testGameObjectMoveDownRight(){
        GameObject gameObject = new GameObject(50, 50, 64, 64, "");
        gameObject.move(10, -10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(60, gameObject.x, 0.001);
        assertEquals(40, gameObject.y, 0.001);
    }
    //Movement tests
    @Test
    public void testEnemyMoveUp(){
        Enemy enemy = new Enemy(50, 50, 64, 64, "ENEMY");
        enemy.createHealthBar();
        enemy.move(0, 10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(50, enemy.x, 0.001);
        assertEquals(60, enemy.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = enemy.getEnemyBar();
        assertEquals(enemy.x, healthBar.x, 0.001);
        assertEquals(enemy.y+enemy.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testEnemyMoveDown(){
        Enemy enemy = new Enemy(50, 50, 64, 64, "ENEMY");
        enemy.createHealthBar();
        enemy.move(0, -10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(50, enemy.x, 0.001);
        assertEquals(40, enemy.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = enemy.getEnemyBar();
        assertEquals(enemy.x, healthBar.x, 0.001);
        assertEquals(enemy.y+enemy.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testEnemyMoveLeft(){
        Enemy enemy = new Enemy(50, 50, 64, 64, "ENEMY");
        enemy.createHealthBar();
        enemy.move(-10, 0, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(40, enemy.x, 0.001);
        assertEquals(50, enemy.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = enemy.getEnemyBar();
        assertEquals(enemy.x, healthBar.x, 0.001);
        assertEquals(enemy.y+enemy.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testEnemyMoveRight(){
        Enemy enemy = new Enemy(50, 50, 64, 64, "ENEMY");
        enemy.createHealthBar();
        enemy.move(10, 0, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(60, enemy.x, 0.001);
        assertEquals(50, enemy.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = enemy.getEnemyBar();
        assertEquals(enemy.x, healthBar.x, 0.001);
        assertEquals(enemy.y+enemy.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testEnemyMoveUpLeft(){
        Enemy enemy = new Enemy(50, 50, 64, 64, "ENEMY");
        enemy.createHealthBar();
        enemy.move(-10, 10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(40, enemy.x, 0.001);
        assertEquals(60, enemy.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = enemy.getEnemyBar();
        assertEquals(enemy.x, healthBar.x, 0.001);
        assertEquals(enemy.y+enemy.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testEnemyMoveUpRight(){
        Enemy enemy = new Enemy(50, 50, 64, 64, "ENEMY");
        enemy.createHealthBar();
        enemy.move(10, 10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(60, enemy.x, 0.001);
        assertEquals(60, enemy.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = enemy.getEnemyBar();
        assertEquals(enemy.x, healthBar.x, 0.001);
        assertEquals(enemy.y+enemy.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testEnemyMoveDownLeft(){
        Enemy enemy = new Enemy(50, 50, 64, 64, "ENEMY");
        enemy.createHealthBar();
        enemy.move(-10, -10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(40, enemy.x, 0.001);
        assertEquals(40, enemy.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = enemy.getEnemyBar();
        assertEquals(enemy.x, healthBar.x, 0.001);
        assertEquals(enemy.y+enemy.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testEnemyMoveDownRight(){
        Enemy enemy = new Enemy(50, 50, 64, 64, "ENEMY");
        enemy.createHealthBar();
        enemy.move(10, -10, 1);
        //Check whether the player has moved the correct amount.
        assertEquals(60, enemy.x, 0.001);
        assertEquals(40, enemy.y, 0.001);
        //Check whether the health bar is moving correctly, as well
        HealthBar healthBar = enemy.getEnemyBar();
        assertEquals(enemy.x, healthBar.x, 0.001);
        assertEquals(enemy.y+enemy.height/2+2f, healthBar.y, 0.001);
    }
    @Test
    public void testDifficultyChangeEasy(){
        float delta = 0.0001f;
        Player player = new Player(50, 50, 64, 64, "ENEMY");
        player.setEasy();
        assertEquals("Easy", player.getDifficulty());
        player.takeDamage(10);
        assertEquals(player.maxHealth-10, player.currentHealth, delta);
    }
    @Test
    public void testDifficultyChangeNormal(){
        float delta = 0.0001f;
        Player player = new Player(50, 50, 64, 64, "ENEMY");
        player.setNormal();
        assertEquals("Normal", player.getDifficulty());
        player.takeDamage(10);
        assertEquals(player.maxHealth-15, player.currentHealth, delta);
    }
    @Test
    public void testDifficultyChangeHard(){
        float delta = 0.0001f;
        Player player = new Player(50, 50, 64, 64, "ENEMY");
        player.setHard();
        assertEquals("Hard", player.getDifficulty());
        player.takeDamage(10);
        assertEquals(player.maxHealth-20, player.currentHealth, delta);
    }
    //Overlapping.
    @Test
    public void overlapTest(){
        GameObject object1 = new GameObject(100,200,64, 64, "");
        GameObject object2 = new GameObject(100,200,64, 64, "");
        assertTrue(object1.overlaps(object2.getHitBox()));
    }
    //Single pixel width overlapping.
    @Test
    public void overlapLeft(){
        int width = 64;
        int height = 64;
        int x = 100;
        int y = 200;
        GameObject object1 = new GameObject(x+width-1,y,width, height, "");
        GameObject object2 = new GameObject(x,y,width, height, "");
        assertTrue(object1.overlaps(object2.getHitBox()));
        object1.move(1,0,1);
        //Should no longer overlap.
        assertEquals(object1.x, x+width, 0.0001f);
        assertFalse(object1.overlaps(object2.getHitBox()));
    }
    @Test
    public void overlapRight(){
        int width = 64;
        int height = 64;
        int x = 100;
        int y = 200;
        GameObject object1 = new GameObject(x-width+1,y,width, height, "");
        GameObject object2 = new GameObject(x,y,width, height, "");
        assertTrue(object1.overlaps(object2.getHitBox()));
        object1.move(-1,0,1);
        //Should no longer overlap.
        assertEquals(object1.x, x-width, 0.0001f);
        assertFalse(object1.overlaps(object2.getHitBox()));
    }
    @Test
    public void overlapTop(){
        int width = 64;
        int height = 64;
        int x = 100;
        int y = 200;
        GameObject object1 = new GameObject(x,y+height-1,width, height, "");
        GameObject object2 = new GameObject(x,y,width, height, "");
        assertTrue(object1.overlaps(object2.getHitBox()));
        object1.move(0,1,1);
        //Should no longer overlap.
        assertEquals(object1.y, y+height, 0.0001f);
        assertFalse(object1.overlaps(object2.getHitBox()));
    }
    @Test
    public void overlapBottom(){
        int width = 64;
        int height = 64;
        int x = 100;
        int y = 200;
        GameObject object1 = new GameObject(x,y-height+1,width, height, "");
        GameObject object2 = new GameObject(x,y,width, height, "");
        assertTrue(object1.overlaps(object2.getHitBox()));
        object1.move(0,-1,1);
        //Should no longer overlap.
        assertEquals(object1.y, y-height, 0.0001f);
        assertFalse(object1.overlaps(object2.getHitBox()));
    }
    // Single pixel overlapping (corners are overlapping)
    @Test
    public void overlapTopLeft(){
        float delta = 0.0001f;
        int width = 64;
        int height = 64;
        int x = 100;
        int y = 200;
        GameObject object1 = new GameObject(x-width+1,y+height-1,width, height, "");
        GameObject object2 = new GameObject(x,y,width, height, "");
        assertTrue(object1.overlaps(object2.getHitBox()));

        //Should no longer overlap.
        object1.move(0,1,1);
        assertEquals(object1.x, x-width+1, delta);
        assertEquals(object1.y, y+height, delta);
        assertFalse(object1.overlaps(object2.getHitBox()));
        object1.move(-1,-1,1);
        assertEquals(object1.x, x-width, delta);
        assertEquals(object1.y, y+height-1, delta);
        assertFalse(object1.overlaps(object2.getHitBox()));
    }
    @Test
    public void overlapTopRight(){
        float delta = 0.0001f;
        int width = 64;
        int height = 64;
        int x = 100;
        int y = 200;
        GameObject object1 = new GameObject(x+width-1,y+height-1,width, height, "");
        GameObject object2 = new GameObject(x,y,width, height, "");
        assertTrue(object1.overlaps(object2.getHitBox()));

        //Should no longer overlap.
        object1.move(0,1,1);
        assertEquals(object1.x, x+width-1, delta);
        assertEquals(object1.y, y+height, delta);
        assertFalse(object1.overlaps(object2.getHitBox()));
        object1.move(1,-1,1);
        assertEquals(object1.x, x+width, delta);
        assertEquals(object1.y, y+height-1, delta);
        assertFalse(object1.overlaps(object2.getHitBox()));
    }
    @Test
    public void overlapBottomLeft(){
        float delta = 0.0001f;
        int width = 64;
        int height = 64;
        int x = 100;
        int y = 200;
        GameObject object1 = new GameObject(x-width+1,y-height+1,width, height, "");
        GameObject object2 = new GameObject(x,y,width, height, "");
        assertTrue(object1.overlaps(object2.getHitBox()));

        //Should no longer overlap.
        object1.move(0,-1,1);
        assertEquals(object1.x, x-width+1, delta);
        assertEquals(object1.y, y-height, delta);
        assertFalse(object1.overlaps(object2.getHitBox()));
        object1.move(-1,1,1);
        assertEquals(object1.x, x-width, delta);
        assertEquals(object1.y, y-height+1, delta);
        assertFalse(object1.overlaps(object2.getHitBox()));
    }
    @Test
    public void overlapBottomRight(){
        float delta = 0.0001f;
        int width = 64;
        int height = 64;
        int x = 100;
        int y = 200;
        GameObject object1 = new GameObject(x+width-1,y-height+1,width, height, "");
        GameObject object2 = new GameObject(x,y,width, height, "");
        assertTrue(object1.overlaps(object2.getHitBox()));

        //Should no longer overlap.
        object1.move(0,-1,1);
        assertEquals(object1.x, x+width-1, delta);
        assertEquals(object1.y, y-height, delta);
        assertFalse(object1.overlaps(object2.getHitBox()));
        object1.move(1,1,1);
        assertEquals(object1.x, x+width, delta);
        assertEquals(object1.y, y-height+1, delta);
        assertFalse(object1.overlaps(object2.getHitBox()));
    }

    // Health bar.
    @Test
    public void healthBarResizePlayer(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        HealthBar healthBar = player.getPlayerHealth();
        player.takeDamage(50);
        healthBar.resize(player.currentHealth);
        assertEquals(healthBar.getStartWidth() * player.currentHealth/player.maxHealth,
                player.getPlayerHealth().width, 0.0001);
    }
    @Test
    public void healthBarResizeCollege(){
        College college = new College(50, 60, 100, 100, "Alcuin", "ENEMY");
        college.createHealthBar();
        college.takeDamage(50);
        HealthBar healthBar = college.getCollegeBar();
        healthBar.resize(college.currentHealth);
        assertEquals(healthBar.getStartWidth() * college.currentHealth/college.maxHealth,
                college.getCollegeBar().width, 0.0001);
    }
    @Test
    public void healthBarResizeEnemy(){
        Enemy enemy = new Enemy(100, 100, 32, 32, "ENEMY");
        enemy.createHealthBar();
        enemy.takeDamage(20);
        HealthBar healthBar = enemy.getEnemyBar();
        healthBar.resize(enemy.currentHealth);
        assertEquals(healthBar.getStartWidth() * enemy.currentHealth/enemy.maxHealth,
                enemy.getEnemyBar().width, 0.0001);
    }
    @Test
    public void playerBloodSplash(){
        Player player = new Player(50, 50, 64, 64, "PLAYER");
        player.createHealthBar();
        assertFalse(player.isDoBloodSplash());
        //Will set doBloodSplash to true
        player.takeDamage(1);
        assertTrue(player.isDoBloodSplash());
        player.timerManager();
        assertTrue(player.isDoBloodSplash());
        player.timerManager();
        assertTrue(player.isDoBloodSplash());
        player.timerManager();
        assertFalse(player.isDoBloodSplash());
    }
}
