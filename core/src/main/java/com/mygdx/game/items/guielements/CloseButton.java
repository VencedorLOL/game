package com.mygdx.game.items.guielements;

import com.mygdx.game.items.GUI;

import static com.mygdx.game.GlobalVariables.classSlots;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.*;

public class CloseButton extends GUI {


	float size;
	boolean hovered = false;
	String texture = "CloseButton";
	int counterr = 10;

	public CloseButton(){}

	public void render(float size, float x, float y, boolean touch){
		this.size = size;
		fixatedDrawables.add(new DrawableObject(texture, x, y, 1, 0, size, size,true));
		if(hovered)
			fixatedDrawables.add(new DrawableObject("HoveringSelection", x , y, 0.7f, 0, size*4, size*4,true));
		onTouchDetect(x ,y,touch);
	}

	public void onTouchDetect(float x, float y, boolean touch){
		if ((touchActionCursorDetector(x,y) || escapeJustPressed() || (actionConfirmReleased() && hovered)) && touch)
			onTouchOverridable();

	}

	public void onTouchOverridable(){}

	boolean touchedIn;
	boolean touchedOut;
	private boolean touchActionCursorDetector(float x, float y){
		boolean bool = false;
		if(counterr <= 0) {
			if (leftClickJustPressed() && cursorX() >= x && cursorX() <= x + size * globalSize() &&
					cursorY() >= y - size * globalSize() && cursorY() <= y)
				touchedIn = true;
			else if (leftClickJustPressed())
				touchedIn = false;
			if (leftClickReleased() && cursorX() >= x && cursorX() <= x + size * globalSize() &&
					cursorY() >= y - size * globalSize() && cursorY() <= y)
				touchedOut = true;
			else if (leftClickReleased())
				touchedOut = false;

			if(touchedIn && touchedOut){
				counterr = 20;
				touchedIn = false;
				touchedOut = false;
				bool = true;
			}
		}

		counterr -= counterr > 0 ? 1 : 0;
		return bool;
	}



}
