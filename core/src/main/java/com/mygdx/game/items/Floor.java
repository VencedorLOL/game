package com.mygdx.game.items;

import static com.mygdx.game.Settings.globalSize;

public class Floor {
	public float x,y;
	public int base,height;
	public String texture;
	public int ID;
	public static int IDState = 0;

	public Floor(String texture,float x, float y) {
		this.x = x;
		this.y = y;
		this.base = globalSize();
		this.height = globalSize();
		this.texture = texture;
		ID = IDState + 1;
	}
	public Floor() {
		this.base = globalSize();
		this.height = globalSize();
		ID = IDState + 1;
	}

	public void render(){
		TextureManager.addToList(texture,x,y);
	}

}
