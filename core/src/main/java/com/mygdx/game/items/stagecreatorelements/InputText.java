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

	public InputText(){
		super();
		freeze = true;
		info = new InformationTransferer();
	}


	public InformationTransferer getInfo(){
		return info;
	}

	public void render(){
		mathCalculator();
		fixatedDrawables.add(new TextureManager.DrawableObject(texture,startingX,startingY,1,false,false,size*2,size,true,255,255,255));
		if ((actionConfirmReleased() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) && canSend()){
			info.string = storedText;
			info.ready = true;
			delete(this);
			text.fakeNull = true;
			freeze = false;
		}


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
			String finalKey = toKey(k);
			if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
				finalKey = toKey(k).toUpperCase();
			storedText = storedText + finalKey;
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
			else if(Gdx.input.isKeyJustPressed(Input.Keys.DEL) && !storedText.isEmpty())
				storedText = storedText.substring(0,storedText.length()-1);
			else if(Gdx.input.isKeyPressed(Input.Keys.DEL) && delCounter >= delCooldown && !storedText.isEmpty()) {
				storedText = storedText.substring(0, storedText.length() - 1);
				delCounter = 0;
			}
			else if (delCounter < delCooldown)
				delCounter++;
			if(Gdx.input.isKeyJustPressed(Input.Keys.DEL))
				delCounter = 0;
			text.text = storedText;
		}

	}
	int delCooldown = 30;
	int delCounter = 0;


	public String toKey(String num){
		switch (num){
			case "7" : return "0";
			case "8" : return "1";
			case "9" : return "2";
			case "10": return "3";
			case "11": return "4";
			case "12": return "5";
			case "13": return "6";
			case "14": return "7";
			case "15": return "8";
			case "16": return "9";

			case "29": return "a";
			case "30": return "b";
			case "31": return "c";
			case "32": return "d";
			case "33": return "e";
			case "34": return "f";
			case "35": return "g";
			case "36": return "h";
			case "37": return "i";
			case "38": return "j";
			case "39": return "k";
			case "40": return "l";
			case "41": return "m";
			case "42": return "n";
			case "43": return "o";
			case "44": return "p";
			case "45": return "q";
			case "46": return "r";
			case "47": return "s";
			case "48": return "t";
			case "49": return "u";
			case "50": return "v";
			case "51": return "w";
			case "52": return "x";
			case "53": return "y";
			case "54": return "z";
		}
		return "";
	}

	public boolean canSend(){
		return true;
	}


	public static class InformationTransferer{
		public String string;
		public boolean ready = false;
	}



}
