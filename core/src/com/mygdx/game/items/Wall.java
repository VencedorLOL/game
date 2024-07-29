package com.mygdx.game.items;

import com.badlogic.gdx.graphics.Texture;

public class Wall extends Entity{

    public boolean doesItHaveATexture = true;
    public Wall(float x, float y, float base, float height) {
        super("BoxToTestCollisionsAndHitboxes",x,y,base,height);
        this.x = x;
        this.y = y;
        this.base = base;
        this.height = height;
        texture = "BoxToTestCollisionsAndHitboxes";
    }
    public Wall(float x, float y, float base, float height, boolean doesItHaveATexture) {
        super("BoxToTestCollisionsAndHitboxes",x,y,base,height);
        this.x = x;
        this.y = y;
        this.base = base;
        this.height = height;
        this.doesItHaveATexture = false;
    }

    public void refresh(float x, float y, float base, float height){
        this.x = x;
        this.y = y;
        this.base = base;
        this.height = height;
    }
}


