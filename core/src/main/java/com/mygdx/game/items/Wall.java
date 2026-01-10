package com.mygdx.game.items;

import com.mygdx.game.items.solids.ClassChangeStation;
import com.mygdx.game.items.solids.Crater;
import com.mygdx.game.items.solids.LargeBarricade;
import com.mygdx.game.items.solids.Tree;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.printErr;

//Extend any other wall texture to this one
public class Wall extends Entity {

    public static ArrayList<Wall> walls = new ArrayList<>();

    // creator scene only


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

    /** <h1>Destroy listeners here
     * <h1/>
     */
    public void destroyWall(){}

    public static void flushList(){
        walls.clear();
    }

    public int getType(){
        for(int i = 0; i < Walls.values().length; i++)
            if (Walls.values()[i].wall == this.getClass())
                return i + 1;
        return -1;
    }

    public static class WallConst<T extends Wall>{
        Class<?> wall;
        @SafeVarargs
		public WallConst(T... none){wall = none.getClass().componentType();}
        public Class<?> getWall(){return wall;}
    }

    @SuppressWarnings("all")
    public enum Walls{
        WALL( (Class<Wall>) new WallConst<Wall>().getWall()),
        L_BARRICADE( (Class<Wall>) new WallConst<LargeBarricade>().getWall()),
        CRATER((Class<Wall>) new WallConst<Crater>().getWall()),
        TREE( (Class<Wall>) new WallConst<Tree>().getWall()),
        C_C_STATION((Class<Wall>) new WallConst<ClassChangeStation>().getWall()),
        ;

        public static int listDifference(){
            return +1;
        }

        public static int getType(Wall wall){
            for(int i = 0; i < Walls.values().length; i++)
                if (Walls.values()[i].wall == wall.getClass())
                    return i + 1;
            return -1;
        }

        public Wall getWall(float x, float y){
            try{
                return wall.getConstructor(float.class,float.class).newInstance(x,y);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException ignored){printErr("Coudn't get a new wall!"); return new Wall(x, y);}
        }

        public Wall getTexture(float x, float y){
            try{
                return wall.getConstructor(float.class,float.class).newInstance(x,y);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException ignored){printErr("Coudn't get a new wall!"); return new Wall(x, y);}
        }
        public final Class<Wall> wall;
        Walls(Class<Wall> wall){
            this.wall = wall;
        }
    }


}


