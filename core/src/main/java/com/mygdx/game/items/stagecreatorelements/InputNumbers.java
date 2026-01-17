package com.mygdx.game.items.stagecreatorelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Utils.floatInRange;
import static com.mygdx.game.Utils.toInt;
import static com.mygdx.game.items.InputHandler.actionConfirmReleased;
import static com.mygdx.game.items.InputHandler.getKeysPressed;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class InputNumbers extends GUI {

	public TextureManager.Text text;
	public String storedText;
	public int[] textColor = new int[]{255,255,255};
	public String texture = "TextBar";

	public float startingX;
	public float startingY;
	public float size;

	public float textSize;

	public InformationTransferer info;

	public InputNumbers(){
		super();
		info = new InformationTransferer();
	}


	public InformationTransferer getInfo(){
		return info;
	}

	public void render(){
		mathCalculator();
		fixatedDrawables.add(new TextureManager.DrawableObject(texture,startingX,startingY,1,false,false,size,size,true,255,255,255));
		if ((actionConfirmReleased() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER))&& canSend()){
			info.number = Integer.parseInt(storedText.split("x")[0]);
			info.ready = true;
			delete(this);
			text.fakeNull = true;
		}


	}

	public void mathCalculator(){
		float height = Gdx.graphics.getHeight();
		float width = Gdx.graphics.getWidth();

		size = height*.02f;

		startingX = width*.5f - size*32/4f;
		startingY = height*.45f;

		textSize = 64 * height/400;

		String[] keys = (getKeysPressed().toArray(new String[0]));

		for (String k : keys) {
			if (k.equals("0") || k.equals("1") || k.equals("2") || k.equals("3") || k.equals("4") || k.equals("5") || k.equals("6") || k.equals("7") ||
					k.equals("8") || k.equals("9"))
				storedText = storedText + k;
			else if (!storedText.isEmpty() && k.toCharArray()[0] == 8 ){
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
			if(storedText.contains("null"))
				storedText = storedText.replace("null","");
			if(Gdx.input.isKeyJustPressed(Input.Keys.DEL) && (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) && !storedText.isEmpty())
				storedText = "";
			text.text = storedText;
		}


	}


	public boolean canSend(){
		return true;
	}


	public static class InformationTransferer{
		public float number;
		public boolean ready = false;
	}



}
