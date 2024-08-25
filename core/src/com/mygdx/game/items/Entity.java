package com.mygdx.game.items;

import java.util.ArrayList;

public class Entity {
	boolean isDead;
	float x, y, base, height;
	String texture;
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

	public boolean overlaps (ArrayList<Entity> entityList){
		for (Entity e : entityList){
			if(e != this)
				return e.x == this.x && e.y == this.y;
		}
		return false;
	}

	//Override
	public boolean isPermittedToAct(){return true;};
	//Override
	public void permitToMove(){};

	public float getX (){ return x; }
	public float getY (){ return y; }
	public float getBase (){ return base; }
	public float getHeight () { return height; }
	public String getTexture() { return texture;}
	public boolean getIsDead() {return isDead; }

	public void setX (float x){
		this.x = x;
	}
	public void setY (float y){
		this.y = y;
	}
	public void setBase (float base){
		this.base = base;
	}
	public void setHeight (float height) {
		this.height = height;
	}
	public void setTexture(String texture) { this.texture = texture; }

	public void render(){
		TextureManager.addToList(texture,x,y);
	}

	// Override this
	public void damage(float damage, String damageReason){
	}


}
