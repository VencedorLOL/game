package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.Camara;
import com.mygdx.game.items.GUI;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.ClickDetector.authenticClick;
import static com.mygdx.game.items.InputHandler.escapeJustPressed;
import static com.mygdx.game.items.TextureManager.*;
import static java.lang.Math.min;

public class CloseButton extends GUI {


	float size, x,y;
	String texture = "CloseButton";

	public CloseButton(float endX, float endY){
		super();

		size = min(endX,endY)/globalSize();

		Vector3 realCoords = (new Vector3(Gdx.graphics.getWidth() - endX,endY, 0));
		realCoords = getCamara().camara.unproject(realCoords);
		x = realCoords.x; y = realCoords.y;
	}

	public void render(float endX, float endY){
		size = min(endX,endY)/globalSize();
		fixatedDrawables.add(new DrawableObject(texture, endX - (size/2), endY - (size/2), 1, 0, size, size));
		onTouchDetect(endX - (size/2),endY - (size/2));
	}

	public void onTouchDetect(float x, float y){
		if ((Gdx.input.justTouched() && Gdx.input.getX() >= x && Gdx.input.getX() <= x + size*globalSize() && Gdx.input.getY() >= y - size*globalSize() && Gdx.input.getY() <= y) || escapeJustPressed())
			onTouchOverridable();

	}

	public void onTouchOverridable(){}

}
