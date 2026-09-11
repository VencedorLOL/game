package com.mygdx.game.items.textboxelements;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;
import com.mygdx.game.items.guielements.Box;

import java.util.ArrayList;

import static com.mygdx.game.Settings.print;
import static com.mygdx.game.Settings.printErr;
import static com.mygdx.game.Utils.*;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static java.lang.Math.max;

public class Textbox extends GUI {
	public static final int PX_LIMIT = 182;

	public int[] exteriorColor;
	public int[] interiorColor;
	public int[] textColor;
	public TextureManager.Text text;
	private String storedText;
	private final ArrayList<String> textChunks = new ArrayList<>();
	private int textLine = 0;
	public String cornerTexture;
	public String sideTexture;
	public String sideWaysTexture;
	public String backgroundTexture;
	public boolean useCutter = false;

	private float thickness;
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

	private float textSize;
	private float textInitialX;
	private float textInitialY;
	public float textJumpLine;

	public int amountOfTextWritten = 0;
	public int framesTilNextLetter;
	public int framesTilNextLetterCounter;
	private boolean doFastText;

	private int cooldownToRemove = 10;

	public final static float szTxtr = 32;

	public Box box;


	/**
	 * <h2>WARNING:</h2>
	 *
	 * <h3>This class by default paralyzes the character on initialization. Override beforeRenderOverridable() to change this behaviour.</h3>
	 * <h3>This class also deparalyzes the character on removal. To change this behaviour, override onRemoval().</h3>
	 *
	 * <h1>
	 *     {@code PLEASE:}
	 * </h1>
	 * <h3>
	 *     Initialize {@code AT LEAST} this variable:
	 * </h3>
	 * <ul>
	 *     <li>{@code storedText} (remember to break the lines and that each textbox can only have 3 breaklines)
	 *     WARNING, {@code storedText} IS GOTTA BE INITIALIZED THROUGH setText(String text). This can be done by overriding onOpenOverridable(), or through other means.</li>
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

		box = new Box(1);

		text = dynamicFixatedText("", textInitialX, textInitialY, -1, textSize);
		text.render = false;
		onOpenOverridable();
		cmds = new byte[storedText.length()];
		args = new int[storedText.length()];
		text.setColor(textColor);
		text.setDefaultAttribute(5,0f);
		text.updateText(textChunks.get(textLine));

	}

	byte[] cmds;
	int[] args;
	//storedText: filtrar comandos, recortar comandos, guardar el index donde em
	private void parseText(){
		StringBuilder finalText = new StringBuilder(storedText);
		int counter = 0;
		for(int i = 0; i < storedText.length(); i++){
			if(storedText.charAt(i) == '>' && (i == 0 || storedText.charAt(i-1) != '\\')) {
				int endCmd = i;
				for (; endCmd < storedText.length(); endCmd++)
					if (storedText.charAt(endCmd) == '<' && storedText.charAt(endCmd-1) != '\\')
						break;
				if(endCmd == i)
					break;
				String command = storedText.substring(i+1,endCmd);
				if(command.indexOf("wait:") == 0){
					try {
						args[i-counter] = Integer.valueOf(command.substring(5));
						cmds[i-counter] += 1;
					} catch (NumberFormatException ignored){printErr("Malformed textbox command at: " + storedText + " | Wait command is not a number.");}
					finalText.delete(i+1,endCmd);
				}
				if(command.indexOf("setdelay:") == 0){
					try {
						args[i-counter] = Integer.valueOf(command.substring(9));
						cmds[i-counter] += 2;
					} catch (NumberFormatException ignored){printErr("Malformed textbox command at: " + storedText + " | SetDelay command is not a number.");}
					finalText.delete(i+1,endCmd);
				}



				counter += i+1-endCmd;
				i = endCmd;
			}
			print("counter: " + counter + " storedText len: " + storedText.length() + " finalText len: " + finalText.length()
				+ " difference between finalText and storedText: " storedText.length()-finalText.length());
			storedText = finalText.toString();
		}


	}

	public void onOpenOverridable(){}


	public void beforeRenderOverridable(){
		paralyzeCharacter();
	}

	public void onRemoval(){
		deparalyzeCharacter();
	}

	public void render(){
		beforeRenderOverridable();
		fastText();
		mathCalculator();
		box.render(startingX,startingY,finalX,finalY,thickness,(byte) exteriorColor[0],(byte) exteriorColor[1],(byte) exteriorColor[2],(byte) interiorColor[0],(byte) interiorColor[1],(byte) interiorColor[2]);

/*		fixatedDrawables.add(new TextureManager.DrawableObject(cornerTexture, startingX,startingY,1,false,false,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerTexture, finalX,startingY,1,true,false,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerTexture, startingX,finalY,1,false,true,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerTexture, finalX,finalY,1,true,true,thickness,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));

		fixatedDrawables.add(new TextureManager.DrawableObject(sideTexture, sideStartingX,startingY,1,false,false,widthSide,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideTexture, sideStartingX,finalY,1,false,true,widthSide,thickness,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideWaysTexture, startingX,sideFinalY,1,false,false,thickness,heightSide,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideWaysTexture, finalX,sideFinalY,1,true,false,thickness,heightSide,true,exteriorColor[0],exteriorColor[1],exteriorColor[2]));

		fixatedDrawables.add(new TextureManager.DrawableObject(backgroundTexture, sideStartingX,sideFinalY,1,false,false,widthSide,heightSide,true,interiorColor[0],interiorColor[1],interiorColor[2]));
*/
		closeMechanism();

	}

	//Override if another closing mechanism should be used instead
	public void closeMechanism(){
		if ((leftClickReleased() || escapePressed() || actionConfirmReleased() || rightClickReleased()) && cooldownToRemove <= 0) {
			if(amountOfTextWritten >= textChunks.get(textLine).length()){
				 if(textChunks.size() - 1 <= textLine){
					 delete(this);
					 text.render = false;
					 text.onScreenTime = 1;
					 text.fakeNull = true;
				 	return;
				 }
				 textLine++;
				 amountOfTextWritten = 0;
				 text.updateText(textChunks.get(textLine));
				 text.changeAttribute(5,0,textChunks.get(textLine).length(),0f);
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

		textSize = (finalY - startingY - thickness*2)*.15f ;
		textInitialX = (startingX + thickness*64)*1.05f;
		textInitialY = (startingY + thickness*16)*1.01f;
		textJumpLine = finalX - thickness*.1f;


		text.render = true;
		text.x = textInitialX;
		text.y = textInitialY;
		text.realSize = textSize;
		if (amountOfTextWritten != textChunks.get(textLine).length()){
			if(framesTilNextLetterCounter++ >= framesTilNextLetter || doFastText){
				framesTilNextLetterCounter = 0;
				text.changeAttribute(5,0,amountOfTextWritten++,1);
			}
		}
	}

	private void executeCommand(int index){
		if(cmds[index] % 2 != 1){
			framesTilNextLetterCounter -= args[index];
		} if (cmds[index] >> 1 % 2 != 1){
			framesTilNextLetter += args[index];
		}
	}

	public void setText(String text){
		textChunks.clear();
		storedText = useCutter ? stringCutter(text,PX_LIMIT + 16) : stringSeparator(text,PX_LIMIT,' ');
		int counter = 0;
		for (int i = 0; i < storedText.length(); i++)
			if(storedText.charAt(i) == '\n' && ++counter % 4 == 0)
				createChunkAtIndex(i);
		createChunkAtIndex(storedText.length());
	}

	private void createChunkAtIndex(int index){
		int counter = 0;
		for (String s : textChunks)
			counter += s.length() + 1;
		textChunks.add(storedText.substring(counter,index));
	}

	public String getText(){
		return storedText;
	}

	public void fastText(){
		doFastText = attackModePressed();
	}


	 @SuppressWarnings("all")
	public void writeTheRestOfTheText(){
		text.changeAttribute(5,0,textChunks.get(textLine).length(),1f);
		amountOfTextWritten = textChunks.get(textLine).length();
	}




}
