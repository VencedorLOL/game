package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import static com.mygdx.game.Settings.print;

public class Camara {

	// ---
	// if i ever need to instantiaze another camara, remove the static coordinates and figure out a way
	// of doing the static things other way
	// ---

	public static OrthographicCamera camara;
	static float x,y,base,height;
	static float zoom = (float) 2;
	static Entity attached;
	public void camaraStarter(float zoom){
		base = Gdx.graphics.getWidth() * zoom;
		height = Gdx.graphics.getHeight() * zoom;
		camara = new OrthographicCamera(0,0);
		camara.setToOrtho(false, base, height);
	}

	public static void attach(Entity entity){
		attached = entity;
	}

	public void updater(){
		if (attached != null){
			x = attached.getX() + attached.getBase() / 2;
			y = attached.getY() + attached.getHeight() / 2;
			camara.position.set(x,y,0);
			camara.update();
		}
	}

	public static float[] unproject(float x, float y){
		Vector3 coords = camara.unproject(new Vector3(x,y,0));
		float[] result = new float[2];
		result[0] = coords.x; result[1] = coords.y;
		return result;
	}


	public void updater(float x, float y){
		camara.position.set(x,y,0);
		camara.update();
	}
	public void updater(float x, float y, float z){
		camara.position.set(x,y,z);
		camara.update();
	}
	public void xSetter(float x){
		Camara.x = x;
	}
	public void ySetter(float y){
		Camara.y = y;
	}
	static public float getZoom(){
		return zoom;
	}
	public void fixedUpdater(){
		camara.update();
	}
	public static  float getX(){return x;}
	public static float getY(){return y;}
	public static float getBase(){return base;}
	public static float getHeight() {return height;}

	public void setToOrtho(boolean yDown, float x, float y, float zoom){
		Camara.zoom = zoom;
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
		Camara.zoom = zoom;
		base = Gdx.graphics.getWidth() * zoom;
		height = Gdx.graphics.getHeight() * zoom;
		camara.setToOrtho(false, base , height);
		updater();
	}
	public void finalizer(SpriteBatch batch){
		batch.setProjectionMatrix(camara.combined);
	}
}
