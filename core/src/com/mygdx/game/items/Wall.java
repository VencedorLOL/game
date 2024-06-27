package com.mygdx.game.items;

import com.badlogic.gdx.graphics.Texture;

public class Wall extends Entity{
    float x, y, base, height;
    public Texture box;
    public boolean doesItHaveATexture = true;
    public Wall(float x, float y, float base, float height) {
        super(x,y,base,height);
        this.x = x;
        this.y = y;
        this.base = base;
        this.height = height;
        box = new Texture("BoxToTestCollisionsAndHitboxes.png");
    }
    public Wall(float x, float y, float base, float height, boolean doesItHaveATexture) {
        super(x,y,base,height);
        this.x = x;
        this.y = y;
        this.base = base;
        this.height = height;
        this.doesItHaveATexture = false;
    }
    public Wall(){}

    public void refresh(float x, float y, float base, float height){
        this.x = x;
        this.y = y;
        this.base = base;
        this.height = height;
    }

    public Texture textureGetter(){
        return box = new Texture("BoxToTestCollisionsAndHitboxes.png");
    }

}


