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
import static com.mygdx.game.items.TextureManager.Text.*;
import static com.mygdx.game.items.TextureManager.dynamicFixatedText;
import static com.mygdx.game.items.TextureManager.fixatedAnimations;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

public class Textbox extends GUI {
	public static final int PX_LIMIT = 200;

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

	/**
	 * At the end of the textbox rendering, this will always be +1 the amount of characters actually written.
	 */
	public int totalAmountOfTextWritten = 0;
	public int amountOfTextWritten = 0;
	public int framesTilNextLetter;
	public int framesTilNextLetterCounter;
	private boolean doFastText;

	private int cooldownToRemove = 10;

	public final static float szTxtr = 32;

	public Box box;

	float[][] attributes;
	float defShake = DEF_DEFAULT_SHAKING ? 1 : 0;
	float defRainb = DEF_DEFAULT_RAINBOW ? 1 : 0;
	float defOpacity = DEF_DEFAULT_OPACITY;
	float defR = DEF_DEFAULT_RED, defG = DEF_DEFAULT_GREEN, defB = DEF_DEFAULT_BLUE;


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
		text.setColor(textColor);
		createAttributes();
		fixTextAttr();
		print("stored text len is; " + storedText.length());
		setText(storedText);
		text.setDefaultAttribute(5,0f);
		text.updateText(textChunks.get(textLine));

	}



	private void createAttributes(){
		if(attributes == null || attributes.length < storedText.length()){
			attributes = new float[storedText.length()][MAX_ATTRIBUTES];
			for(int i = 0; i < attributes.length;i++){
				attributes[i][0] = defShake;
				attributes[i][1] = defRainb;
				attributes[i][2] = defR;
				attributes[i][3] = defB;
				attributes[i][4] = defG;
				attributes[i][5] = defOpacity;
			}
		}
	}


	public void changeAttribute(int attribute, int from, int to, float value){
		if(attribute >= MAX_ATTRIBUTES || attribute < 0)
			return;
		if(attributes == null)
			attributes = new float[storedText.length()][MAX_ATTRIBUTES];
		float[][] temp;
		if(attributes.length < storedText.length()) {
			temp = new float[storedText.length()][MAX_ATTRIBUTES];
			for (int i = 0; i < attributes.length; i++)
				System.arraycopy(attributes[i], 0, temp[i], 0, MAX_ATTRIBUTES);
			for(int i = attributes.length+1; i < storedText.length(); i++) {
				temp[i][0] = defShake;
				temp[i][1] = defRainb;
				temp[i][2] = defR;
				temp[i][3] = defG;
				temp[i][4] = defB;
				temp[i][5] = defOpacity;
			}
		}
		else
			temp = attributes;
		to = (min(to+1,temp.length));
		for(int i = from; i < to; i++)
			temp[i][attribute] = value;
		attributes = temp;
	}


	public void initiateRainbow(float cycleTime, float multiplicator){text.initiateRainbow(cycleTime,multiplicator);}
	public void initiateShake(float yVariation, int time){text.initiateShake(yVariation,time);}

	public void setDefaultAttribute(int attribute, float value){
		switch (attribute) {
			case 0: defShake = value; break;
			case 1: defRainb = value; break;
			case 2: defR = value; break;
			case 3: defG = value; break;
			case 4: defB = value; break;
			case 5: defOpacity = value; break;
		}
	}

	public void setColor(int... color){
		if(attributes == null) createAttributes();
		if(color.length > 0) {
			setDefaultAttribute(2, color[0]);
			changeAttribute(2, 0, attributes.length, color[0]);
		} if(color.length > 1) {
			setDefaultAttribute(3, color[1]);
			changeAttribute(3,0, attributes.length, color[1]);
		} if(color.length > 2){
			setDefaultAttribute(4, color[2]);
			changeAttribute(4,0, attributes.length, color[2]);
		}
	}

	public void fixTextAttr(){
		int counter = 0;
		for(int i = 0; i < textLine; i++)
			counter+=textChunks.get(i).length()+1;
		float[] isShake = new float[textChunks.get(textLine).length()];
		float[] isRainb = new float[textChunks.get(textLine).length()];
		float[] red = new float[textChunks.get(textLine).length()];
		float[] green = new float[textChunks.get(textLine).length()];
		float[] blue = new float[textChunks.get(textLine).length()];
		float[] opacity = new float[textChunks.get(textLine).length()];
		for(int i = 0; i < isShake.length; i++){
			isShake[i] = attributes[i+counter][0];
			isRainb[i] = attributes[i+counter][1];
			red[i] = attributes[i+counter][2];
			green[i] = attributes[i+counter][3];
			blue[i] = attributes[i+counter][4];
			opacity[i] = attributes[i+counter][5];

		}
		for(int i = 0; i < MAX_ATTRIBUTES; i++)
			text.resetAttribute(i);
		text.changeAttribute(0,isShake);
		text.changeAttribute(1,isRainb);
		text.changeAttribute(2,red);
		text.changeAttribute(3,green);
		text.changeAttribute(4,blue);
		if(textLine != 0)
			text.changeAttribute(5,opacity);

	}



	byte[] cmds;
	int[] args;
	private void parseText(){
		cmds = new byte[storedText.length()];
		args = new int[storedText.length()];
		StringBuilder finalText = new StringBuilder(storedText);
		int counter = 0;
		for(int i = 0; i < storedText.length(); i++){
			if(storedText.charAt(i) == '<' && (i == 0 || storedText.charAt(i-1) != '\\')) {
				int endCmd = i;
				for (; endCmd < storedText.length(); endCmd++)
					if (storedText.charAt(endCmd) == '>' && storedText.charAt(endCmd-1) != '\\')
						break;
				if(endCmd == i)
					break;
				String command = storedText.substring(i+1,endCmd);
				if(command.indexOf("wait:") == 0 || command.indexOf("w:") == 0){
					try {
						args[i-counter] = parseInt(command.substring(command.indexOf(":")+1));
						cmds[i-counter] += 1;
						print("set number to: " + args[i-counter] + " at index " + (i-counter));
					} catch (NumberFormatException ignored){printErr("Malformed textbox command at: " + storedText + " | Wait command is not a number.");}
					finalText.delete(i-counter,endCmd+1-counter);
				}
				if(command.indexOf("setdelay:") == 0 || command.indexOf("d:") == 0){
					try {
						args[i-counter] = parseInt(command.substring(command.indexOf(":")+1));
						cmds[i-counter] += 2;
					} catch (NumberFormatException ignored){printErr("Malformed textbox command at: " + storedText + " | SetDelay command is not a number.");}
					finalText.delete(i-counter,endCmd+1-counter);
				} if(command.indexOf("halt") == 0 || command.indexOf("stop") == 0 || command.indexOf("break") == 0 || command.indexOf("clear") == 0 || command.indexOf("c") == 0){ //technically the "clear" entry is redundant
					changeAttribute(0,i-counter,storedText.length(),1);
					changeAttribute(1,i-counter,storedText.length(),1);
					changeAttribute(2,i-counter,storedText.length(),255);
					changeAttribute(3,i-counter,storedText.length(),255);
					changeAttribute(4,i-counter,storedText.length(),255);
					changeAttribute(5,i-counter,storedText.length(),1);

					finalText.delete(i-counter,endCmd+1-counter);
				} if(command.indexOf("shake:") == 0 || command.indexOf("s:") == 0) {
					try {
						initiateShake(parseInt(command.substring(command.indexOf(":")+1)),parseInt(command.substring(command.indexOf(",")+1)));
						changeAttribute(0,i-counter,storedText.length(),0);
					} catch (NumberFormatException ignored){printErr("Malformed textbox command at: " + storedText + " | Shake command does not return two numbers.");}
					finalText.delete(i-counter,endCmd+1-counter);
				} if(command.indexOf("rainbow:") == 0 || command.indexOf("w:") == 0 || command.indexOf("m:") == 0 || command.indexOf("multicolor:") == 0 || command.indexOf("rbow:") == 0) {
					try {
						initiateRainbow(parseInt(command.substring(command.indexOf(":")+1)),parseInt(command.substring(command.indexOf(",")+1)));
						changeAttribute(1,i-counter,storedText.length(),0);
					} catch (NumberFormatException ignored){printErr("Malformed textbox command at: " + storedText + " | Rainbow command does not return two numbers.");}
					finalText.delete(i-counter,endCmd+1-counter);
				}if(command.indexOf("red:") == 0 || command.indexOf("r:") == 0) {
					try {
						changeAttribute(2,i-counter,storedText.length(),parseInt(command.substring(command.indexOf(":")+1)));
						print("command detected at " + (i-counter));
					} catch (NumberFormatException ignored){printErr("Malformed textbox command at: " + storedText + " | Red command is not a number.");}
					finalText.delete(i-counter,endCmd+1-counter);
				}if(command.indexOf("green:") == 0 || command.indexOf("g:") == 0) {
					try {
						changeAttribute(3,i-counter,storedText.length(),parseInt(command.substring(command.indexOf(":")+1)));
					} catch (NumberFormatException ignored){printErr("Malformed textbox command at: " + storedText + " | Green command is not a number.");}
					finalText.delete(i-counter,endCmd+1-counter);
				}if(command.indexOf("blue:") == 0 || command.indexOf("b:") == 0) {
					try {
						changeAttribute(4,i-counter,storedText.length(),parseInt(command.substring(command.indexOf(":")+1)));
					} catch (NumberFormatException ignored){printErr("Malformed textbox command at: " + storedText + " | Blue command is not a number.");}
					finalText.delete(i-counter,endCmd+1-counter);
				}


				counter += endCmd+1-(i);
				i = endCmd;
			}
		}
		print("counter: " + counter + " storedText len: " + storedText.length() + " finalText len: " + finalText.length()
				+ " difference between finalText and storedText: " + (storedText.length()-finalText.length()));
		storedText = finalText.toString();
		print(storedText);
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
				 totalAmountOfTextWritten--;
				 amountOfTextWritten = 0;
				 text.updateText(textChunks.get(textLine));
				 fixTextAttr();
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
			if(totalAmountOfTextWritten == 0)
				executeCommand(totalAmountOfTextWritten++);
			if(framesTilNextLetterCounter++ >= framesTilNextLetter || doFastText){
				framesTilNextLetterCounter = 0;
				executeCommand(totalAmountOfTextWritten++);
				text.changeAttribute(5,0,amountOfTextWritten++,1);
			}
		}
	}

	private void executeCommand(int index){
		if(index < cmds.length){
			if(cmds[index] % 2 == 1){
				framesTilNextLetterCounter -= args[index];
			} if (cmds[index] >> 1 % 2 == 1){
				framesTilNextLetter += args[index];
			}
		}
	}

	public void setText(String text){
		textChunks.clear();
		storedText = text;
		if(args == null || cmds == null)
			parseText();
		storedText = useCutter ? stringCutter(storedText,PX_LIMIT) : stringSeparator(storedText,PX_LIMIT,' ');
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
