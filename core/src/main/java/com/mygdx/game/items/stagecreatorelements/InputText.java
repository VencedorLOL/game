package com.mygdx.game.items.stagecreatorelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.items.InputHandler.actionConfirmReleased;
import static com.mygdx.game.items.InputHandler.getKeysPressed;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;
import static com.mygdx.game.StageCreatorScreen.*;

public class InputText extends GUI {

	public TextureManager.Text text;
	public String storedText = "";
	public int[] textColor = new int[]{255,255,255};
	public String texture = "TextBar";

	public float startingX;
	public float startingY;
	public float size;

	public float textSize;

	public InformationTransferer info;

	public boolean spareFrame = true;

	public InputText(){
		super();
		freeze = true;
		info = new InformationTransferer();
	}


	public InformationTransferer getInfo(){
		return info;
	}

	public void render(){
		if(!spareFrame) {
			mathCalculator();
			fixatedDrawables.add(new TextureManager.DrawableObject(texture, startingX, startingY, 1, false, false, size * 2, size, true, 255, 255, 255));
			if ((actionConfirmReleased() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) && canSend()) {
				info.string = storedText;
				info.ready = true;
				delete(this);
				text.fakeNull = true;
				freeze = false;
			}

		} else spareFrame = false;
	}

	@SuppressWarnings("all")
	public void mathCalculator(){
		float height = Gdx.graphics.getHeight();
		float width = Gdx.graphics.getWidth();

		size = height*.02f;

		startingX = width*.5f - size*32/4f;
		startingY = height*.45f;

		textSize = 64 * height/400;

		for (String k : getKeysPressed().toArray(new String[0])) {
			if(k.toCharArray()[0] != 8 && k.toCharArray()[0] != 10) {
				storedText = storedText + k.toCharArray()[0];
			} else if(!storedText.isEmpty() && k.toCharArray()[0] == 8 ){
				storedText = storedText.substring(0,storedText.length()-1);
			}
		}

		if(text == null)
			text = dynamicFixatedText("",startingX,startingY,-1,textSize);
		text.setColor(textColor);
		text.x = startingX + size*2;
		text.y = height*.15f;
		text.realSize = textSize;

		if(storedText != null){
			if(Gdx.input.isKeyJustPressed(Input.Keys.DEL) && (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) && !storedText.isEmpty())
				storedText = "";
			text.updateText(storedText);
		}

	}

	public boolean canSend(){
		return true;
	}


	public static class InformationTransferer{
		public String string;
		public boolean ready = false;
	}



}
