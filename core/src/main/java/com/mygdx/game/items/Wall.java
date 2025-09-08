package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

//Extend any other wall texture to this one
public class Wall extends Entity {

    public static ArrayList<Wall> walls = new ArrayList<>();

    public Wall(float x, float y) {
        super("Rock",x,y,globalSize(),globalSize());
        refresh(x,y,globalSize(),globalSize());
        walls.add(this);
    }

    public Wall(float x, float y, float base, float height, boolean render) {
        super("Rock",x,y,base,height);
        this.render = render;
        walls.add(this);
    }

    public Wall(float x, float y,String texture) {
        super(texture,x,y);
        refresh(x,y,globalSize(),globalSize());
        walls.add(this);
    }

    public void refresh(float x, float y, float base, float height){
        this.x = x;
        this.y = y;
        this.base = base;
        this.height = height;
    }

    public static void flushList(){
        walls.clear();
    }


}


