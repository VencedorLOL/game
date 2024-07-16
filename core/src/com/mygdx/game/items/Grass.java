package com.mygdx.game.items;

public class Grass {
	public float x,y;
	public int base,height;
	public String grass;
	public Grass(float x, float y, int base, int height) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		grass = "Grass";
	}
	public Grass() {
		this.base = 128;
		this.height = 128;
	}

}
