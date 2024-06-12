package com.mygdx.game.items;

import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;

public class Grass {
	public float x,y;
	public int base,height;
	public Texture grass;
	public ArrayList<Grass> gras = new ArrayList<>();
	public Grass(float x, float y, int base, int height) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		grass = new Texture("Grass.png");
	}
	public Grass() {
		this.base = 128;
		this.height = 128;
	}

	public Texture textureGetter(){
		return grass = new Texture("Grass.png");
	}

}
