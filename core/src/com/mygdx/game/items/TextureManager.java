package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class TextureManager {
	public SpriteBatch batch;
	public TextureAtlas atlas;
	public Sprite sprite;
	TextureRegion region;
	ArrayList<DrawableObject> drawables;
	ArrayList<DrawableObject> priorityDrawables;

	public TextureManager (){
		drawables = new ArrayList<>();
		priorityDrawables = new ArrayList<>();
		batch = new SpriteBatch();
		bounder();
	}

	public void bounder(){
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("Atlas/AtlasOne.atlas"));
	}

	private void drawer(String texture, float x, float y){
		region = atlas.findRegion(texture);
		sprite = new Sprite(region);
		sprite.setPosition(x,y);
		sprite.draw(batch);
	}

	public void addToList(String texture, float x, float y){
		drawables.add(new DrawableObject(texture, x, y));
	}

	public void addToPriorityList(String texture, float x, float y){
		priorityDrawables.add(new DrawableObject(texture, x, y));
	}

	public void render(){
		for (TextureManager.DrawableObject d : drawables){
			drawer(d.texture,d.x,d.y);
		}
		drawables.clear();
		for (TextureManager.DrawableObject d : priorityDrawables){
			drawer(d.texture,d.x,d.y);
			priorityDrawables.remove(d);
		}
		priorityDrawables.clear();
	}



	public static class DrawableObject{
		public float x,y;
		public String texture;

		public DrawableObject(String texture, float x, float y){
			this.x = x;
			this.y = y;
			this.texture = texture;
		}
	}

}
