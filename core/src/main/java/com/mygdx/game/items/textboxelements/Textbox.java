package com.mygdx.game.items.textboxelements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class Textbox extends GUI {

	public int[] exteriorColor;
	public int[] interiorColor;
	public int[] textColor;
	public TextureManager.Text text;
	public String storedText;
	public String cornerTexture;
	public String sideTexture;
	public String sideWaysTexture;
	public String backgroundTexture;

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

	private int cooldownToRemove = 10;

	public final static float szTxtr = 32;


	/**
	 * <h1>
	 *     {@code PLEASE:}
	 * </h1>
	 * <h3>
	 *     Initialize {@code AT LEAST} this variable:
	 * </h3>
	 * <ul>
	 *     <li>{@code storedText} (remember to break the lines and that each textbox can only have 3 breaklines)</li>
	 * </ul>
	 *
	 * <h4>
	 *     Would be good but not strictly necesary to change:
	 * </h4>
	 * <ul>
	 *     <li>{@code framesTilNextLetter}</li>
	 *     <li>{@code textColor}</li>
	 *     <li>{@code interiorColor}</li>
	 *     <li>{@code exteriorColor}</li>
	 * </ul>
	 *
	 * <h5>
	 *     Everything looks ugly if I do:
	 * </h5>
	 * <ul>
	 *     <li>{@code cornerTexture}</li>
	 *     <li>{@code sideTexture}</li>
	 *     <li>{@code sideWaysTexture}</li>
	 *     <li>{@code backgroundTexture}</li>
	 * </ul>
	 */
	public Textbox(){
		super();
		cornerTexture = "BackgroundTextbox";
		sideTexture = "BackgroundTextbox";
		sideWaysTexture = "BackgroundTextbox";
		backgroundTexture = "BackgroundTextbox";
		exteriorColor = new int[]{255,255,255};
		interiorColor = new int[]{0,0,0};
		textColor = new int[]{255,255,255};
		framesTilNextLetter = 10;
		storedText = "NULL TEXTBOX";
	}

	public void beforeRenderOverridable(){}

	public void render(){
		beforeRenderOverridable();
		mathCalculator();
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerTexture, startingX,startingY,1,false,false,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerTexture, finalX,startingY,1,true,false,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerTexture, startingX,finalY,1,false,true,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerTexture, finalX,finalY,1,true,true,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));

		fixatedDrawables.add(new TextureManager.DrawableObject(sideTexture, sideStartingX,startingY,1,false,false,widthSide,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideTexture, sideStartingX,finalY,1,false,true,widthSide,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideWaysTexture, startingX,sideFinalY,1,false,false,thickness,heightSide,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideWaysTexture, finalX,sideFinalY,1,true,false,thickness,heightSide,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));

		fixatedDrawables.add(new TextureManager.DrawableObject(backgroundTexture, sideStartingX,sideFinalY,1,false,false,widthSide,heightSide,true,interiorColor[0],interiorColor[1],interiorColor[2]));

		closeMechanism();

	}

	//Override if another closing mechanism should be used instead
	public void closeMechanism(){
		if ((leftClickReleased() || escapePressed() || actionConfirmReleased() || rightClickReleased()) && cooldownToRemove <= 0) {
			if (amountOfTextWritten >= storedText.length()) {
				delete(this);
				text.render = false;
				text.onScreenTime = 1;
				text.fakeNull = true;
			} else
				writeTheRestOfTheText();
		}
	}


	public void mathCalculator(){
		cooldownToRemove -= cooldownToRemove <= 0 ? 0 : 1;
		//	Double color border (black + exterior):
		//	change the variables of the textures of the first four drawable objects for "cornerTexture",
		//	the variables for the next two with "sideTexture" and the variables for the next two with "sideWaysTexture
		//	and double the thickness (optional)
		float height = Gdx.graphics.getHeight();
		float width = Gdx.graphics.getWidth();

		thickness = height*.00075f;

		float boxWidth = height*1.2f;
		float posStartX = width*.15f;
		float posFinalX = width*.85f - thickness*szTxtr;
		float difference = (boxWidth - (posFinalX - posStartX))/2f;

		startingX = posStartX - difference;
		startingY = height*.65f;
		finalX = posFinalX + difference;
		finalY = height*.95f;

		sideStartingX = startingX + thickness*szTxtr;
		sideStartingY = startingY ;
		sideFinalX = finalX;
		sideFinalY = finalY - thickness*szTxtr;

		widthSide = (sideFinalX - sideStartingX)/szTxtr;
		heightSide = (sideFinalY - sideStartingY)/szTxtr;

		textSize = (finalY - startingY - thickness*2)/5 ;
		textInitialX = startingX + thickness*64*1f ;
		textInitialY = startingY + thickness*16;
		textJumpLine = finalX - thickness*.1f;

		beforeTextOverridable();
		if(text == null)
			text = dynamicFixatedText("",textInitialX,textInitialY,-1,textSize);
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

	 @SuppressWarnings("all")
	public void writeTheRestOfTheText(){
		while(amountOfTextWritten < storedText.length())
			text.text = text.text + storedText.charAt(amountOfTextWritten++);
	}


	@SuppressWarnings("all")
	public void beforeTextOverridable(){}


}
