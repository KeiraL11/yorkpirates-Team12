package com.engteam14.yorkpirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameObject2 {
    float x, y, width, height;
    String team;
    float health = 200;
    Texture sprite;
    Rectangle hitBox;
    Animation<Texture> anim;

    ShaderProgram shader;
    public GameObject2(float x, float y, float width, float height, String team){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.team = team;
    }
    public void takeDamage(float damage){
        this.health -= damage;
    }
    public float getHealth(){return health;}
    public void generateShader() {
     shader = new ShaderProgram(Gdx.files.internal("red.vsh"), Gdx.files.internal("red.fsh"));
    }

    public void changeImage(Array<Texture> frames, float fps) {
        this.sprite = frames.get(0);
        anim = new Animation<>(fps==0?0:(1f/fps), frames);
    }
}
