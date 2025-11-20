package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.GUI;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.InputHandler.actionConfirmJustPressed;
import static com.mygdx.game.items.InputHandler.escapeJustPressed;
import static com.mygdx.game.items.TextureManager.*;
import static java.lang.Math.min;

public class CloseButton extends GUI {


	float size;
	boolean hovered = false;
	String texture = "CloseButton";

	public CloseButton(){
		super();
	}

	public void render(float size, float x, float y){
		this.size = size;
		fixatedDrawables.add(new DrawableObject(texture, x, y, 1, 0, size, size,true));
		if(hovered)
			fixatedDrawables.add(new DrawableObject("HoveringSelection", x , y, 0.7f, 0, size*4, size*4,true));
		onTouchDetect(x ,y);
	}

	public void onTouchDetect(float x, float y){
		if ((Gdx.input.justTouched() && Gdx.input.getX() >= x && Gdx.input.getX() <= x + size*globalSize() &&
				Gdx.input.getY() >= y - size*globalSize() && Gdx.input.getY() <= y) || escapeJustPressed() || (actionConfirmJustPressed() && hovered))
			onTouchOverridable();

	}

	public void onTouchOverridable(){}

}
