package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.GUI;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.InputHandler.actionConfirmJustPressed;
import static com.mygdx.game.items.InputHandler.escapeJustPressed;
import static com.mygdx.game.items.TextureManager.DrawableObject;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;
import static java.lang.Math.min;

public class SelectionButton extends GUI {


	float size;
	boolean selected = false;
	boolean hovered = false;
	String texture = "SelectionBox";
	String secTexture = null;

	public SelectionButton(){
		super();
	}

	public void render(float size,float x, float y){
		this.size = size;
		fixatedDrawables.add(new DrawableObject(texture, x , y, 1, 0, size, size,true));
		fixatedDrawables.add(new DrawableObject(secTexture, x , y, 1, 0, size, size,true));
		if(selected)
			fixatedDrawables.add(new DrawableObject("SelectedSelection", x , y, 0.7f, 0, size, size,true));
		else if(hovered)
			fixatedDrawables.add(new DrawableObject("HoveringSelection", x , y, 0.7f, 0, size, size,true));
		onTouchDetect(x,y);
	}

	public void onTouchDetect(float x, float y){
		if ((Gdx.input.justTouched() && Gdx.input.getX() >= x && Gdx.input.getX() <= x + size*32 &&
				Gdx.input.getY() >= y - size*32 && Gdx.input.getY() <= y)  || (actionConfirmJustPressed() && hovered))
			onTouchOverridable();

	}

	public void onTouchOverridable(){}

}
