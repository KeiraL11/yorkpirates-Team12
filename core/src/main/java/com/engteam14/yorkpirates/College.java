package com.engteam14.yorkpirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Objects;

import static java.lang.Math.abs;

public class College extends GameObject {

    public static int capturedCount = 0;

    private HealthBar collegeBar;
    private Indicator direction;

    private float splashTime;
    private long lastShotFired;
    private final String collegeName;
    private final Array<Texture> collegeImages = new Array<>();
    private Array<Texture> boatTexture = new Array<>();
    private Array<GameObject> boats = new Array<>();
    private Array<Float> boatRotations = new Array<>();

    private boolean doBloodSplash = false;

    private Projectile newProjectile;

    /**
     * Generates a college object within the game with animated frame(s) and a hit-box.
     * @param x         The x coordinate within the map to initialise the object at.
     * @param y         The y coordinate within the map to initialise the object at.
     * @param name      The name of the college.
     * @param team      The team the college is on.
     */
    public College(float x, float y, float width, float height, String name, String team){
        super(x, y, width, height, team);
        splashTime = 0;
        setMaxHealth(2000);
        setCurrentHealth(getMaxHealth());
        lastShotFired = 0;
        collegeName = name;
    }
    public void imageHandling(Array<Texture> sprites, String boatTexture, Player player) throws Exception {
        super.changeImage(sprites, 0);
        this.boatTexture.add(new Texture(Gdx.files.internal(boatTexture)));
        for (int i = 0; i < sprites.size; i++){
            collegeImages.add(sprites.get(i));
        }
        Array<Texture> healthBarSprite = new Array<>();
        Array<Texture> indicatorSprite = new Array<>();
        if(Objects.equals(team, GameScreen.playerTeam)){
            if(Objects.equals(collegeName, "Home")){
                indicatorSprite.add(new Texture("homeArrow.png"));
            }else{
                indicatorSprite.add(new Texture("allyArrow.png"));
            }
            healthBarSprite.add(new Texture("allyHealthBar.png"));

        }else{
            healthBarSprite.add(new Texture("enemyHealthBar.png"));
            indicatorSprite.add(new Texture("questArrow.png"));
        }
        collegeBar = new HealthBar(this);
        collegeBar.changeImage(healthBarSprite);
        direction = new Indicator(this,player,
                indicatorSprite.get(0).getWidth()/50f, indicatorSprite.get(0).getHeight()/50f);
        direction.changeImage(indicatorSprite);
    }
    /**
     * Called once per frame. Used to perform calculations such as collision.
     * @param screen    The main game screen.
     */
    public void update(GameScreen screen) throws Exception {
        direction.move();
        float playerX = screen.getPlayer().x;
        float playerY = screen.getPlayer().y;
        boolean nearPlayer = abs(this.x - playerX) < (Gdx.graphics.getWidth()/15f) && abs(this.y - playerY) < (Gdx.graphics.getHeight()/10f);

        if(nearPlayer || screen.isPaused()){
            direction.setVisible(false);

            if(!Objects.equals(team, GameScreen.playerTeam)) { // Checks if the college is an enemy of the player
                // How often the college can shoot.
                int shootFrequency = 1000;
                if (TimeUtils.timeSinceMillis(lastShotFired) > shootFrequency){
                    lastShotFired = TimeUtils.millis();
                    Array<Texture> sprites = new Array<>();
                    sprites.add(new Texture("tempProjectile.png"));
                    newProjectile  = new Projectile(this, playerX, playerY, team);
                    newProjectile.changeImage(sprites);
                    screen.projectiles.add(newProjectile);
                }
            }else if(Objects.equals(collegeName, "Home")){
                boolean victory = true;
                for(int i = 0; i < screen.colleges.size; i++) {
                    if(!Objects.equals(screen.colleges.get(i).team, GameScreen.playerTeam)){
                        victory = false;
                    }
                }
                if(victory){
                    screen.getHUD().setGameEndable();
                    if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) screen.gameEnd(true);
                }
            }
        }else{
            direction.setVisible(true);
        }

        if(doBloodSplash){
            if(splashTime > 1){
                doBloodSplash = false;
                splashTime = 0;
            }else{
                splashTime += 1;
            }
        }
        if(currentHealth > 0){
            collegeBar.resize(currentHealth);
        }else{
            if(!Objects.equals(team, GameScreen.playerTeam)){ // Checks if the college is an enemy of the player
                // College taken over
                int pointsGained = 50;
                screen.points.Add(pointsGained);
                int lootGained = 15;
                screen.loot.Add(lootGained);

                Array<Texture> healthBarSprite = new Array<>();
                Array<Texture> indicatorSprite = new Array<>();
                healthBarSprite.add(new Texture("allyHealthBar.png"));
                indicatorSprite.add(new Texture("allyArrow.png"));
                boatTexture.clear();
                boatTexture.add(screen.getPlayer().anim.getKeyFrame(0f));

                Array<Texture> sprites = new Array<>();
                sprites.add(collegeImages.get(1));
                changeImage(sprites,0);

                collegeBar.changeImage(healthBarSprite,0);
                currentHealth = maxHealth;
                collegeBar.resize(currentHealth);
                College.capturedCount++;
                direction.changeImage(indicatorSprite,0);
                team = GameScreen.playerTeam;
            }else{
                // Destroy college
                collegeBar = null;
                direction = null;
                destroy(screen);
            }
        }
    }

    /**
     * Called when a projectile hits the college.
     * @param damage            The damage dealt by the projectile.
     */
    public void takeDamage(float damage){
        super.takeDamage(damage);
        doBloodSplash = true;
    }
    public boolean getDoBloodSplash(){return doBloodSplash;}

    /**
     * Called when the college needs to be destroyed.
     * @param screen    The main game screen.
     */
    private void destroy(GameScreen screen){
        screen.colleges.removeValue(this,true);
    }

    /**
     * Called when drawing the object.
     * @param batch         The batch to draw the object within.
     * @param elapsedTime   The current time the game has been running for.
     */
    @Override
    public void draw(SpriteBatch batch, float elapsedTime){
        if (shader == null){generateShader();}
        if(doBloodSplash)   batch.setShader(shader); // Set red shader to the batch
        else                batch.setShader(null);

        // Draw college
        batch.draw(anim.getKeyFrame(elapsedTime, true), x - width/2, y - height/2, width, height);

        // Draw boats after college, so under
        batch.setShader(null);
        for(int i = 0; i < boats.size; i++){
            GameObject boat = boats.get(i);
            batch.draw(boatTexture.get(0), boat.x+boat.height, boat.y, 0,0, boat.width, boat.height,
                    1f, 1f, boatRotations.get(i), 0, 0,
                    boatTexture.get(0).getWidth(), boatTexture.get(0).getHeight(), false, false);
        }

        collegeBar.draw(batch, 0);
        direction.draw(batch,0);
    }

    /**
     * Add a boat to this college.
     * @param x The x position of the new boat relative to the college.
     * @param y The y position of the new boat relative to the college.
     */
    public void addBoat(float x, float y, float rotation){
        GameObject newBoat = new GameObject(this.x+x, this.y+y, 25, 12, team);
        boats.add(newBoat);
        //boats.add(new GameObject(boatTexture, 0, this.x+x, this.y+y, 25, 12, team));
        boatRotations.add(rotation);
    }
}
