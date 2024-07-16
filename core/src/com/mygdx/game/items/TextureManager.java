package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {
	public SpriteBatch batch;
	public TextureAtlas atlas;
	public Sprite sprite;
	TextureRegion region;

	public TextureManager (){
		bounder();
	}

	public void bounder(){
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("atlas/AtlasOne.atlas"));
	}

	public void drawer(String texture, float x, float y){
		region = atlas.findRegion(texture);
		sprite = new Sprite(region);
		sprite.setPosition(x,y);
		sprite.draw(batch);
	}

}
