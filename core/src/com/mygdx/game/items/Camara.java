package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Camara {
	OrthographicCamera camara;
	float x,y,base,height;
	float zoom = (float) 2;
	public void camaraStarter(float zoom){
		base = Gdx.graphics.getWidth() * zoom;
		height = Gdx.graphics.getHeight() * zoom;
		camara = new OrthographicCamera(0,0);
		camara.setToOrtho(false, base, height);
	}

	public void updater(Entity chara){
		x = chara.getX() + chara.getBase() / 2;
		y = chara.getY() + chara.getHeight() / 2;
		camara.position.set(x,y,0);
		camara.update();
	}
	public void updater(float x, float y){
		camara.position.set(x,y,0);
		camara.update();
	}
	public void updater(float x, float y, float z){
		camara.position.set(x,y,z);
		camara.update();
	}
	public void updater(){
		camara.position.set(x,y,0);
		camara.update();
	}
	public void xSetter(float x){
		this.x = x;
	}
	public void ySetter(float y){
		this.y = y;
	}
	public float zoomGetter(){
		return zoom;
	}
	public void fixedUpdater(){
		camara.update();
	}
	public void setToOrtho(boolean yDown, float x, float y, float zoom){
		this.zoom = zoom;
		base = x * zoom;
		height = y * zoom;
		camara.setToOrtho(yDown,base,height);
		updater();
	}
	public void setToOrtho(boolean yDown, float x, float y){
		base = x;
		height = y;
		camara.setToOrtho(yDown,base,height);
		updater();
	}
	public void setToOrtho(){
		zoom = 1;
		base = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		camara.setToOrtho(false, base , height);
		updater();
	}
	public void setToOrtho(float zoom){
		this.zoom = zoom;
		base = Gdx.graphics.getWidth() * zoom;
		height = Gdx.graphics.getHeight() * zoom;
		camara.setToOrtho(false, base , height);
		updater();
	}
	public void finalizer(SpriteBatch batch){
		batch.setProjectionMatrix(camara.combined);
	}
}
