package com.mygdx.game.items;

import java.util.ArrayList;

public class Entity {
	boolean isDead;
	float x, y, base, height;
	String texture;
	boolean render = true;
	public static ArrayList<Entity> entityList = new ArrayList<>();

	public Entity(String texture, float x, float y, float base, float height){
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		this.texture = texture;
		entityList.add(this);
	}
	public Entity(){}
	public void refresh(String texture, float x, float y, float base, float height){
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		this.texture = texture;
		entityList.add(this);
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

	//Override
	public boolean isPermittedToAct(){return true;}
	//Override
	public void permitToMove(){}

	public float getX () { return x; }
	public float getY () { return y; }
	public float getBase (){ return base; }
	public float getHeight () { return height; }
	public String getTexture() { return texture;}
	public boolean getIsDead() { return isDead; }
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

	// Override this
	public void damage(float damage, String damageReason){
	}


}
