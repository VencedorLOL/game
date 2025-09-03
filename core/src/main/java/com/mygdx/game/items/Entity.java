package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class Entity {

	float x, y, base, height;
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
			TextureManager.addToList(texture,x,y);
	}

	public void render(float opacity,float rotation){
		if(render)
			TextureManager.addToList(texture,x,y,opacity,rotation);
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

	// Actor will have this


}
