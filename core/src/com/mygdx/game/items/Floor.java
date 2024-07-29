package com.mygdx.game.items;

public class Floor {
	public float x,y;
	public int base,height;
	public String texture;
	public Floor(String texture,float x, float y, int base, int height) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		this.texture = texture;
	}
	public Floor() {
		this.base = 128;
		this.height = 128;
	}

}
