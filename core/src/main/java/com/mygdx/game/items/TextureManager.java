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
	static ArrayList<DrawableObject> drawables;
	static ArrayList<DrawableObject> priorityDrawables;
	static ArrayList<DrawableObject> fixatedScreenDrawables;

	public TextureManager (){
		drawables = new ArrayList<>();
		priorityDrawables = new ArrayList<>();
		fixatedScreenDrawables = new ArrayList<>();
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

	private void fixatedScreenDrawer(String texture, float x, float y,float xPercentage, float yPercentage){
		region = atlas.findRegion(texture);
		sprite = new Sprite(region);
		sprite.setPosition(Gdx.graphics.getWidth() * xPercentage + x,Gdx.graphics.getHeight() * yPercentage + y);
		sprite.draw(batch);
	}


	public static void addToList(String texture, float x, float y){
		drawables.add(new DrawableObject(texture, x, y));
	}

	public static void addToListFixatedScreenCoordinates(String texture,float x,float y, float xPercentage, float yPercentage){
		fixatedScreenDrawables.add(new DrawableObject(texture, x,y, xPercentage, yPercentage));
	}

	public static void addToPriorityList(String texture, float x, float y){
		priorityDrawables.add(new DrawableObject(texture, x, y));
	}

	public void render(){
		for (TextureManager.DrawableObject d : drawables){
			if (d.texture != null)
				drawer(d.texture,d.x,d.y);
		}
		drawables.clear();
		for (TextureManager.DrawableObject d : priorityDrawables){
			if (d.texture != null)
				drawer(d.texture,d.x,d.y);
		}
		priorityDrawables.clear();
		for (TextureManager.DrawableObject d : fixatedScreenDrawables){
			if (d.texture != null)
				fixatedScreenDrawer(d.texture,d.x,d.y,d.xPercentage,d.yPercentage);
		}
		fixatedScreenDrawables.clear();
	}



	public static class DrawableObject{
		public float x,y;
		public String texture;
		public float xPercentage;
		public float yPercentage;

		public DrawableObject(String texture, float x, float y){
			this.x = x;
			this.y = y;
			this.texture = texture;
		}

		public DrawableObject(String texture, float x, float y,float xPercentage, float yPercentage){
			this.x = x;
			this.y = y;
			this.texture = texture;
			this.xPercentage = xPercentage;
			this.yPercentage = yPercentage;
		}
	}

}
