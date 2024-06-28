package com.mygdx.game.items;

public class Entity {
	float x, y, base, height;
	public Entity(float x, float y, float base, float height){
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
	}
	public Entity(){}
	public void refresh(float x, float y, float base, float height){
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
	}
	public boolean overlaps (Entity entity) {
		return x < entity.x + entity.base && x + base > entity.x && y < entity.y + entity.height && y + height > entity.y;
	}

	public float getX (){
		return x;
	}
	public float getY (){
		return y;
	}
	public float getBase (){
		return base;
	}
	public float getHeight () {
		return height;
	}

}
