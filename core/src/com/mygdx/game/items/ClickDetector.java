package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import static java.lang.Math.floor;

public class ClickDetector {
	static Camara camara;


	public ClickDetector(Camara camara){
		ClickDetector.camara = camara;
	}


	public void camaraUpdater(Camara camara){
		ClickDetector.camara = camara;
	}

	public static Vector3 click(){
		Vector3 touchedPosition = (new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0));
		camara.camara.unproject(touchedPosition);
		touchedPosition.x = (float) (128 * floor((touchedPosition.x) / 128));
		touchedPosition.y = (float) (128 * floor((touchedPosition.y) / 128));
		System.out.println(touchedPosition.x);
		System.out.println(touchedPosition.y);
		return touchedPosition;
	}


}
