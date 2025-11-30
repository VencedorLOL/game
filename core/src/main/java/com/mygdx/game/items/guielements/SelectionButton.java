package com.mygdx.game.items.guielements;

import com.mygdx.game.items.GUI;

import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.InputHandler.cursorY;
import static com.mygdx.game.items.TextureManager.DrawableObject;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class SelectionButton extends GUI {
	float size,x,y;
	boolean selected = false;
	boolean hovered = false;
	String texture = "SelectionBox";
	String secTexture = null;

	public SelectionButton(){
		super();
	}

	public void render(float size,float x, float y){
		this.size = size*32;
		this.x = x;
		this.y = y;
		fixatedDrawables.add(new DrawableObject(texture, x , y, 1, 0, size, size,true));
		fixatedDrawables.add(new DrawableObject(secTexture, x , y, 1, 0, size, size,true));
		if(selected)
			fixatedDrawables.add(new DrawableObject("SelectedSelection", x , y, 0.7f, 0, size, size,true));
		else if(hovered)
			fixatedDrawables.add(new DrawableObject("HoveringSelection", x , y, 0.7f, 0, size, size,true));
		onTouchDetect(x,y);
	}

	public void onTouchDetect(float x, float y){
		if ((leftClickJustPressed() && cursorX() >= x && cursorX() <= x + size &&
				cursorY() >= y - size && cursorY() <= y)  || (actionConfirmJustPressed() && hovered))
			onTouchOverridable();

	}

	public void onTouchOverridable(){}

}
