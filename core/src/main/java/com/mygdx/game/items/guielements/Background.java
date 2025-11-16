package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.Camara;
import com.mygdx.game.items.GUI;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.TextureManager.*;

public class Background extends GUI {

	boolean renderr = false;
	float sizeX, sizeY;
	String texture = "GUIBackground";

	public Background(){
		super();
		// change all of this math so there can be like a 10% of space of screen not covered by the gui on each axis
		sizeX = Gdx.graphics.getWidth() /(globalSize()*1f);
		sizeY = Gdx.graphics.getHeight() /(globalSize()*.5625f);
		Vector3 realCoords = (new Vector3(Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f,Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f, 0));
		realCoords = Camara.camara.unproject(realCoords);
		Animation animation = new Animation("guibackgroundopening",realCoords.x,realCoords.y){
			public void onFinish() {
				renderr = true;
			}
		};
		animation.scaleY = sizeY;
		animation.scaleX = sizeX;
		animations.add(animation);

	}

	public void render(){
		if(renderr) {
			sizeX = Gdx.graphics.getWidth() /(globalSize()*1f);
			sizeY = Gdx.graphics.getHeight() /(globalSize()*.5625f);
			fixatedDrawables.add(new DrawableObject(texture, Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f, Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f , 1, 0, sizeX, sizeY));
		}
	}

}
