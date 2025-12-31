package com.mygdx.game.items.textboxelements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.items.TextureManager.Text.textSize;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class Textbox extends GUI {

	public static final float fontSize = 100;
	public int[] exteriorColor;
	public int[] interiorColor;
	public int[] textColor;
	public TextureManager.Text text;
	public String storedText;
	public String[] dividedText;
	public String cornerTexture;
	public String sideTexture;
	public String sideWaysTexture;
	public String centerTexture;

	public float thickness;
	public float widthSide;
	public float heightSide;

	public float startingX;
	public float startingY;
	public float finalX;
	public float finalY;

	public float sideStartingX;
	public float sideStartingY;
	public float sideFinalX;
	public float sideFinalY;

	public float textSize;
	public float textInitialX;
	public float textInitialY;
	public float textJumpLine;

	public int amountOfTextWritten = 0;
	public int framesTilNextLetter;
	public int framesTilNextLetterCounter;

	public final static float szTxtr = 32;

	public Textbox(String a, String b, String c, String d, int framesPerLetter, String text){
		super();
		cornerTexture = a;
		sideTexture = b;
		sideWaysTexture = c;
		centerTexture = d;
		exteriorColor = new int[]{255,255,255};
		interiorColor = new int[]{0,0,0};
		textColor = new int[]{255,255,255};
		framesTilNextLetter = framesPerLetter;
		storedText = text;
		dividedText = text.split(" ");

	}

	public void render(){
		mathCalculator();
		fixatedDrawables.add(new TextureManager.DrawableObject(centerTexture, startingX,startingY,1,false,false,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(centerTexture, finalX,startingY,1,true,false,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(centerTexture, startingX,finalY,1,false,true,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(centerTexture, finalX,finalY,1,true,true,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));

		fixatedDrawables.add(new TextureManager.DrawableObject(centerTexture, sideStartingX,startingY,1,false,false,widthSide,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(centerTexture, sideStartingX,finalY,1,false,true,widthSide,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(centerTexture, startingX,sideFinalY,1,false,false,thickness,heightSide,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(centerTexture, finalX,sideFinalY,1,true,false,thickness,heightSide,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));

		fixatedDrawables.add(new TextureManager.DrawableObject(centerTexture, sideStartingX,sideFinalY,1,false,false,widthSide,heightSide,true,interiorColor[0],interiorColor[1],interiorColor[2]));





	}




	public void mathCalculator(){
		//	Double color border (black + exterior):
		//	change the variables of the textures of the first four drawable objects for "cornerTexture",
		//	the variables for the next two with "sideTexture" and the variables for the next two with "sideWaysTexture
		//	and double the thickness (optional)
		float height = Gdx.graphics.getHeight();
		float width = Gdx.graphics.getWidth();

		thickness = height*.00075f;

		startingX = width*.15f;
		startingY = height*.65f;
		finalX = width*.85f - thickness*szTxtr;
		finalY = height*.95f;

		sideStartingX = startingX + thickness*szTxtr;
		sideStartingY = startingY ;
		sideFinalX = finalX;
		sideFinalY = finalY - thickness*szTxtr;

		widthSide = (sideFinalX - sideStartingX)/szTxtr;
		heightSide = (sideFinalY - sideStartingY)/szTxtr;


		textSize = (finalY - startingY - thickness*2)/5 ;
		textInitialX = startingX + thickness*32*1.5f ;
		textInitialY = startingY + thickness*16;
		textJumpLine = finalX - thickness*.1f;


		if(text == null)
			text = dynamicFixatedText("*",textInitialX,textInitialY,-1,textSize);
		text.setColor(textColor);
		text.x = textInitialX;
		text.y = textInitialY;
		text.realSize = textSize;
		if (amountOfTextWritten != storedText.length()){
			if(framesTilNextLetterCounter++ >= framesTilNextLetter){
				framesTilNextLetterCounter = 0;
				text.text = text.text + storedText.charAt(amountOfTextWritten++);
			}
		}



	}

}
