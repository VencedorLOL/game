package com.mygdx.game.items;

import static com.mygdx.game.Settings.globalSize;

public class Floor {
	public float x,y;
	public int base,height;
	public String texture;

	public Floor(String texture,float x, float y) {
		this.x = x;
		this.y = y;
		this.base = globalSize();
		this.height = globalSize();
		this.texture = texture;
	}
	public Floor() {
		this.base = globalSize();
		this.height = globalSize();
	}

	public void render(){
		TextureManager.addToList(texture,x,y);
	}

}
