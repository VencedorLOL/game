package com.mygdx.game.items.stagecreatorelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Utils.*;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class InputDimensions extends GUI {

	public TextureManager.Text text;
	public String storedText;
	public int[] textColor = new int[]{255,255,255};
	public String texture = "TextBar";

	public float startingX;
	public float startingY;
	public float size;

	public float textSize;

	public InformationTransferer info;

	public InputDimensions(){
		super();
		info = new InformationTransferer();
	}


	public InformationTransferer getInfo(){
		return info;
	}

	public void render(){
		mathCalculator();
		fixatedDrawables.add(new TextureManager.DrawableObject(texture,startingX,startingY,1,false,false,size,size,true,255,255,255));
		if ((actionConfirmReleased() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) && canSend()){
			info.x = Integer.parseInt(storedText.split("x")[0]);
			info.y = Integer.parseInt(storedText.split("x")[1]);
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

		int[] keys = toInt(getKeysPressed().toArray(new String[0]));

		for (int k : keys)
			if (floatInRange(7, 16, k) || k == 52)
				storedText = storedText + toKey(k);

		if(text == null)
			text = dynamicFixatedText("",startingX,startingY,-1,textSize);
		text.setColor(textColor);
		text.x = startingX + size*2;
		text.y = height*.15f;
		text.realSize = textSize;

		if(storedText != null){
			if(storedText.contains("null"))
				storedText = storedText.replace("null","");
			if(Gdx.input.isKeyJustPressed(Input.Keys.DEL) && !storedText.isEmpty())
				storedText = storedText.substring(0,storedText.length()-1);
			text.text = storedText;
		}


	}

	public String toKey(int num){
		switch (num){
			case 7 : return "0";
			case 8 : return "1";
			case 9 : return "2";
			case 10: return "3";
			case 11: return "4";
			case 12: return "5";
			case 13: return "6";
			case 14: return "7";
			case 15: return "8";
			case 16: return "9";
			case 52: return "x";
		}
		return "";
	}

	public boolean canSend(){
		return storedText.split("x").length == 2;
	}


	public static class InformationTransferer{
		public float x;
		public float y;
		public boolean ready = false;
	}



}
