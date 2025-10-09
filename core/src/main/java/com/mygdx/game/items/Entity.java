package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.*;
import static java.lang.Math.*;
import static java.lang.Math.abs;

public class Entity {

	public float x, y,z, base, height;
	String texture;
	boolean render = true;
	public static ArrayList<Entity> entityList = new ArrayList<>();

	public Entity(String texture, float x, float y, float base, float height){
		refresh(texture,x,y,base,height);
		entityList.add(this);
	}

	public Entity() {
		entityList.add(this);
	}

	public Entity(String texture, float x, float y){
		refresh(texture,x,y,globalSize(),globalSize());
		entityList.add(this);
	}

	public Entity(String texture, float x, float y,boolean render){
		refresh(texture,x,y,globalSize(),globalSize());
		this.render = render;
		entityList.add(this);
	}

	public void refresh(String texture, float x, float y, float base, float height){
		this.x       = x;
		this.y       = y;
		this.base    = base;
		this.height  = height;
		this.texture = texture;
	}

	public boolean overlaps (Entity entity) {
		return x < entity.x + entity.base && x + base > entity.x && y < entity.y + entity.height && y + height > entity.y;
	}

	public boolean overlapsTiles(Entity entity){
		return x == entity.x && y == entity.y;
	}

	public boolean overlapsTiles(ArrayList<Entity> entityList){
		for (Entity e : entityList)
			if(e != this)
				return e.x == this.x && e.y == this.y;
		return false;
	}

	public float getX () { return x; }
	public float getY () { return y; }
	public float getBase (){ return base; }
	public float getHeight () { return height; }
	public String getTexture() { return texture;}

	public boolean getRender() { return render; }

	public void setX (float x){this.x = x;}
	public void setY (float y){this.y = y;}
	public void setBase (float base){this.base = base;}
	public void setHeight (float height) {this.height = height;}
	public void setTexture(String texture) { this.texture = texture; }
	public void setRender (boolean render) { this.render = render; }


	public void render(){
		if(render)
			TextureManager.addToList(texture,x,y,z);
	}

	public void render(float opacity,float rotation,float r, float g,float b){
		if(render)
			TextureManager.addToList(texture,x,y,opacity,rotation,r,g,b);
	}

	public boolean overlapsWithWalls(Stage stage, Entity entity){
		for(Wall b : stage.walls)
			if (entity.overlapsTiles(b) || entity.overlaps(b))
				return true;
		return false;
	}

	public boolean overlapsWithStage(Stage stage, Entity entity, Entity ignore){
		if (overlapsWithWalls(stage,entity))
			return true;
		for(Enemy e : stage.enemy)
			if (entity.x == e.x && entity.y == e.y && !e.isDead && e != ignore)
				return true;
		return false;
	}

	public float glideXPerFrame, glideYPerFrame, glideZPerFrame;
	public boolean isGliding = false;
	public float glideTime;
	public Floatt expectedX, expectedY;
	public void glide(float x, float y, float time){
		isGliding = true;
		glideTime = time;
		glideXPerFrame = (x+glideXPerFrame*glideTime) / time;
		glideYPerFrame = (y+glideYPerFrame*glideTime) / time;
	}

	public void glide(float x, float y,float z, float time){
		isGliding = true;
		glideXPerFrame = (x+glideXPerFrame*glideTime) / time;
		glideYPerFrame = (y+glideYPerFrame*glideTime) / time;
		glideZPerFrame = (z+glideZPerFrame*glideTime) / time;
		glideTime = time;
	}

	public void glideAbsoluteCoords(float x, float y, float time){
		isGliding = true;
		glideTime = time;
		glideXPerFrame = (x - this.x) / time;
		glideYPerFrame = (y - this.y) / time;
		expectedX = new Floatt(x);
		expectedY = new Floatt(y);
	}

	public void glideAbsoluteCoords(float x, float y){
		glideAbsoluteCoords(x,y,(float)sqrt(pow(x,2)+pow(y,2))/2);
	}


	public void glide(float x, float y){
		glide(x,y,(float)sqrt(pow(x,2)+pow(y,2))/2);
	}

	public void autoGlide(float x, float y,float z){
		glide(x,y,z,(float)sqrt(pow(x,2)+pow(y,2))/2);
	}

	public void glideProcess(){
		if (glideTime <= 0) {
			glideXPerFrame = 0;
			glideYPerFrame = 0;
			glideZPerFrame = 0;
			isGliding = false;
			expectedX = null;
			expectedY = null;
		}
		else {
			glideTime--;
			x += glideXPerFrame;
			y += glideYPerFrame;
			z += glideZPerFrame;
			if(glideTime == 0 && expectedX != null && expectedY != null){
				x = expectedX.aFloat;
				y = expectedY.aFloat;
			}
		}
	}

	public double dC(float x, float y){return sqrt(pow(abs(x)-abs(this.x),2)+pow(abs(y)-abs(this.y),2));}

	public static class Floatt{
		float aFloat;
		Floatt (float aFloat){
			this.aFloat = aFloat;
		}
	}

}
