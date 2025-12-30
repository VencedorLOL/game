package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.Utils.toFloat;
import static com.mygdx.game.items.AttackTextProcessor.coordsUpdater;
import static java.lang.Math.*;

@SuppressWarnings("all")
public class TextureManager {
	public static SpriteBatch batch;
	public static TextureAtlas atlas;
	public static Sprite sprite;
	static TextureRegion region;
	public static Camara camara;
	public static ArrayList<Text> text;
	public static ArrayList<Text> priorityText;
	static ArrayList<Text> fixatedText;
	public static ArrayList<DrawableObject> drawables;
	static ArrayList<DrawableObject> priorityDrawables;
	public static ArrayList<DrawableObject> fixatedDrawables;
	public static ArrayList<Animation> animations;
	public static ArrayList<Animation> fixatedAnimations;
	static ArrayList<AtlasAndName> atlases;
	static ArrayList<DrawableTexture> videos;
	static OnVariousScenarios oVE = new OnVariousScenarios(){
		@Override
		public void onStageChange() {
			text.clear();
			drawables.clear();
			videos.clear();
		}
	};

	static{
		drawables = new ArrayList<>();
		priorityDrawables = new ArrayList<>();
		fixatedDrawables = new ArrayList<>();
		text = new ArrayList<>();
		fixatedText = new ArrayList<>();
		batch = new SpriteBatch();
		animations = new ArrayList<>();
		fixatedAnimations = new ArrayList<>();
		atlases = new ArrayList<>();
		videos = new ArrayList<>();
		priorityText = new ArrayList<>();
		bounder();
	}


	public static void getCamara(Camara camara){
		TextureManager.camara = camara;
	}

	public static SpriteBatch getBatch(){
		return batch;
	}

	public static void bounder(){
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("Atlas/AtlasOne.atlas"));
		atlases.add(new AtlasAndName(atlas, "AtlasOne.atlas"));
	}

	private static void drawer(String texture, float x, float y,float opacity,float rotationDegrees){
		drawer(texture,x,y,0,opacity,false,false,rotationDegrees,1,1,1,1,1,false);
	}

	private static void drawer(String texture, float x, float y,float z,float opacity,boolean flipX,boolean flipY,float rotationDegrees,float scaleX,float scaleY,float r, float g, float b,boolean originZero){
		drawer(texture,x,y,z,0,0,opacity,flipX,flipY,rotationDegrees,scaleX,scaleY,r,g,b,originZero);
	}

	private static void drawer(String texture, float x, float y,float z,float base,float height,float opacity,boolean flipX,boolean flipY,float rotationDegrees,float scaleX,float scaleY,float r, float g, float b,boolean originZero){
		//	drawer(texture,x,y,opacity,"AtlasOne.atlas");
		region = atlas.findRegion(texture);
		try {
			sprite = new Sprite(region);
		} catch (NullPointerException ignored){printErr("NPE caught while trying to print " + texture);}
		sprite.setPosition(x-base/2,y-height/2);
		sprite.setFlip(flipX,flipY);
		sprite.setRotation(rotationDegrees);
		sprite.setScale(scaleX*(z*0.2f + 1),scaleY*(z*0.2f + 1));
		sprite.setColor(r,g,b,(1/(z*0.1f + 1))+(opacity-1));
		if(originZero)
			sprite.setOrigin(0,0);
		if(getRender())
			sprite.draw(batch);
	}

	private static void textureDrawer(Texture texture, float x, float y){
		batch.draw(texture,x,y);
	}

	private static void drawer(String texture, float x, float y,float opacity,String atlasPath){
		for (AtlasAndName t : atlases) {
			if (Objects.equals(t.getName(), atlasPath)) {
				region = t.getAtlas().findRegion(texture);
				sprite = new Sprite(region);
				sprite.setAlpha(opacity);
				sprite.setPosition(x, y);
				if(getRender())
					sprite.draw(batch);
				return;
			}
		}

		atlases.add(new AtlasAndName(new TextureAtlas(Gdx.files.internal("Atlas/" + atlasPath)), atlasPath));
		drawer(texture,x,y,opacity,atlasPath);

	}

	private static void drawer(String texture,float x, float y,float scaleX,float scaleY,boolean originZero,float xPercentage){
		region = atlas.findRegion(texture);
		region.setRegionWidth((int) (region.getRegionWidth()*xPercentage));
		sprite = new Sprite(region);
		sprite.setPosition(x,y);
		sprite.setOrigin(0,0);
		sprite.setScale(scaleX,scaleY);
		sprite.draw(batch);

	}



	private static void fixatedScreenDrawer(String texture, float x, float y,float z, float opacity,float rotationDegrees,float scaleX, float scaleY,float r,float g, float b,boolean originZero,float xPercentage){
		Vector3 coords = GameScreen.getCamara().camara.unproject(new Vector3(x,y,0f));
		if(xPercentage != 0 && xPercentage != 1){
			drawer(texture,coords.x,coords.y,scaleX,scaleY,originZero,xPercentage);
		} else
			drawer(texture,coords.x, coords.y,z,opacity,false,false,rotationDegrees,scaleX,scaleY,r,g,b, originZero);
	}

	private static void fixatedScreenDrawer(String texture, float x, float y,float z,float base,float height, float opacity,boolean flipX, boolean flipY,float rotationDegrees,float scaleX, float scaleY,float r,float g, float b,boolean originZero){
		Vector3 coords = GameScreen.getCamara().camara.unproject(new Vector3(x,y,0f));
		drawer(texture,coords.x, coords.y,z,base,height,opacity,flipX,flipY,rotationDegrees,scaleX,scaleY,r,g,b,originZero);
	}

	public static void animationToList(String file, float x, float y){
		animations.add(new Animation(file,x,y));
	}

	public static void fixatedAnimationToList(String file, float x, float y){
		fixatedAnimations.add(new Animation(file,x,y));
	}

	public static void addToList(String texture, float x, float y){
		addToList( texture,  x,  y,1,0,256,256,256);
	}

	public static void addToList(String texture, float x, float y,float z){
		drawables.add(new DrawableObject(texture, x, y,z));
	}

	public static void addToList(String texture, float x, float y,float opacity,float rotationDegrees){
		addToList(texture, x, y,opacity,rotationDegrees,256,256,256);
	}

	public static void addToList(String texture, float x, float y,float opacity,float rotationDegrees,boolean flipX, boolean flipY){
		drawables.add(new DrawableObject(texture, x, y,opacity,rotationDegrees,flipX,flipY));
	}

	public static void addToList(String texture, float x, float y,float opacity,float rotationDegrees,float r,float g,float b,float scaleX,float scaleY){
		drawables.add(new DrawableObject(texture, x, y,opacity,rotationDegrees,r,g,b,scaleX,scaleY));
	}

	public static void addToList(String texture, float x, float y,float opacity,float rotationDegrees,float r,float g,float b){
		drawables.add(new DrawableObject(texture, x, y,opacity,rotationDegrees,r,g,b));
	}

	public static void addToFixatedList(String texture, float x, float y){
		addToFixatedList(texture,x,y,1,0);
	}

	public static void addToFixatedList(String texture, float x, float y,float opacity,float rotationDegrees){
		fixatedDrawables.add(new DrawableObject(texture, x, y,opacity,rotationDegrees,255,255,255));
	}

	public static void addToFixatedList(String texture, float x, float y,float opacity,float rotationDegrees,float scaleX, float scaleY){
		fixatedDrawables.add(new DrawableObject(texture, x, y,opacity,rotationDegrees,scaleX,scaleY));
	}

	public static void addToFixatedList(String texture, float x, float y,float opacity,float rotationDegrees,float r,float g,float b){
		fixatedDrawables.add(new DrawableObject(texture, x, y,opacity,rotationDegrees,r,g,b));
	}


	public static void addToPriorityList(String texture, float x, float y){
		addToPriorityList( texture,  x,  y,1);
	}
	public static void addToPriorityList(String texture, float x, float y,float opacity){
		priorityDrawables.add(new DrawableObject(texture, x, y,opacity,0));
	}

	public static void addToPriorityList(String texture, float x, float y,float opacity,float rotationDegrees,boolean flipX, boolean flipY){
		priorityDrawables.add(new DrawableObject(texture, x, y,opacity,rotationDegrees,flipX,flipY));
	}


	public static void renderVideo(Texture texture, float x, float y){
		videos.add(new DrawableTexture(texture, x, y));
	}


	public static void render(Camara camara){
		getCamara(camara);
		// Least priority drawable objects

		coordsUpdater();
		for (TextureManager.DrawableObject d : drawables){
			if (d.texture != null)
				 drawer(d.texture,d.x,d.y,d.z,d.opacity,d.flipX,d.flipY,d.rotationDegrees,d.scaleX,d.scaleY,d.r,d.g,d.b,d.originZero);
		}
		drawables.clear();
		// Animations

		for (TextureManager.Animation a : animations){
			a.update();
			if (a.texture != null)
				 drawer(a.texture, a.x, a.y,0, a.opacity,a.flipX,false,0,a.scaleX,a.scaleY,1,1,1,false);
		}
		animations.removeIf(ani -> ani.finished);

		// Fixated Animations

		for (TextureManager.Animation a : fixatedAnimations){
			a.update();
			if (a.texture != null)
				fixatedScreenDrawer(a.texture, a.x/100 * Gdx.graphics.getWidth()
						, a.y/100 * Gdx.graphics.getHeight()
						,0,a.base,a.height, a.opacity,a.flipX,false,0,a.scaleX,a.scaleY,1,1,1,false);
		}
		fixatedAnimations.removeIf(ani -> ani.finished);



		// Text display
		for (TextureManager.Text t : text){
			if (!t.fakeNull && t.render)
				t.draw();
		}
		text.removeIf(tex -> tex.fakeNull);
		// Most priority drawables

		for (TextureManager.DrawableObject d : priorityDrawables){
			if (d.texture != null)
				drawer(d.texture,d.x,d.y,d.z,d.opacity,d.flipX,d.flipY,d.rotationDegrees,d.scaleX,d.scaleY,d.r,d.g,d.b,d.originZero);
		}
		priorityDrawables.clear();
		// Static drawables


		for (TextureManager.DrawableObject d : fixatedDrawables){
			if (d.texture != null)
				fixatedScreenDrawer(d.texture,d.x,d.y,d.z,d.opacity,d.rotationDegrees,d.scaleX,d.scaleY,d.r,d.g,d.b,d.originZero,d.xPercentage);
		}
		fixatedDrawables.clear();
		//Priority Text
		for (TextureManager.Text t : priorityText){
			if (!t.fakeNull && t.render)
				t.draw();
		}
		priorityText.removeIf(tex -> tex.fakeNull);

		// Static text display

		for (TextureManager.Text t : fixatedText){
			if (!t.fakeNull && t.render) {
				t.drawStatic();
			}
		}
		fixatedText.removeIf(tex -> tex.fakeNull);

		//Video reserved

		for (TextureManager.DrawableTexture t : videos){
			if (t.texture != null)
				textureDrawer(t.texture,t.x,t.y);
		}
		videos.clear();
	}

	public static void fixatedText (String text,float x, float y,int timeOnScreen,int size){
		TextureManager.fixatedText.add(new Text(text,x,y,size,timeOnScreen));
	}

	public static void fixatedText (String text,float x, float y,int timeOnScreen,int size,int r, int g, int b){
		TextureManager.fixatedText.add(new Text(text,x,y, timeOnScreen,r,g,b,1,0,size));
	}

	public static Text dinamicFixatedText (String text,float x, float y,int timeOnScreen,int size){
		Text text1 = new Text(text,x,y,size,timeOnScreen);
		TextureManager.fixatedText.add(text1);
		return text1;
	}


	public static void text (String text,float x, float y,int size){
		TextureManager.text.add(new Text(text,x,y,size));
	}

	public static void text (String text,float x, float y,int timeTilDisappear,int size,Entity entityToFollow){
		TextureManager.text.add(new Text(text,x,y,timeTilDisappear,entityToFollow,size));
	}

	public static void text (String text,float x, float y,int timeTilDisappear, int size,int r, int g, int b,float opacity,float vanishingThreshold){
		TextureManager.text.add(new Text(text,x,y,timeTilDisappear,r,g,b,opacity,vanishingThreshold,size));
	}

	public static void rainbowText (String text,float x, float y,int timeTilDisappear, int size,float opacity,float vanishingThreshold,int cycleTime, float multiplicator){
		TextureManager.text.add(new Text(text,x,y,timeTilDisappear,opacity,vanishingThreshold,size,cycleTime,multiplicator));
	}

	public static void shakyText (String text,float x, float y,int timeTilDisappear, float size,int vanishingThreshold,int r,int g,int b,float maxY,int time){
		TextureManager.text.add(new Text(text,x,y,size,timeTilDisappear,vanishingThreshold,r,g,b,maxY,time));
	}

	public static void text (String text,float x, float y,int timeTilDisappear,int size){
		if (text!= null)
			TextureManager.text.add(new Text(text,x,y,timeTilDisappear));}

	public static class DrawableObject{
		public float x,y,z = 0;
		public String texture;
		public float opacity;
		public float rotationDegrees;
		public float scaleX = 1, scaleY = 1;
		public float r = 1,g = 1,b = 1;
		public boolean flipX = false, flipY = false;
		public boolean remove = true;
		public boolean originZero = false;
		public float xPercentage;

		public DrawableObject(String texture, float x, float y,float z){
			this.x = x;
			this.y = y;
			this.z = z;
			this.texture = texture;
			opacity = 1;
		}

		public DrawableObject(String texture, float x, float y,float opacity, float rotationDegrees){
			this.x = x;
			this.y = y;
			this.texture = texture;
			this.rotationDegrees = rotationDegrees;
			if (opacity > 1)
				this.opacity = opacity / 100;
			else
				this.opacity = opacity;
		}

		public DrawableObject(String texture, float x, float y,float opacity, float rotationDegrees,boolean flipX, boolean flipY){
			this.x = x;
			this.y = y;
			this.texture = texture;
			this.rotationDegrees = rotationDegrees;
			if (opacity > 1)
				this.opacity = opacity / 100;
			else
				this.opacity = opacity;
			this.flipX = flipX;
			this.flipY = flipY;
		}

		public DrawableObject(String texture, float x, float y,float opacity, float rotationDegrees,float r, float g, float b){
			this.x = x;
			this.y = y;
			this.texture = texture;
			this.r = r / 255; this.g = g / 255; this.b = b / 255;
			this.rotationDegrees = rotationDegrees;
			if (opacity > 1)
				this.opacity = opacity / 100;
			else
				this.opacity = opacity;
		}

		public DrawableObject(String texture, float x, float y,float opacity, float rotationDegrees,float r, float g, float b,float scaleX,float scaleY){
			this.x = x;
			this.y = y;
			this.texture = texture;
			this.r = r / 255; this.g = g / 255; this.b = b / 255;
			this.rotationDegrees = rotationDegrees;
			if (opacity > 1)
				this.opacity = opacity / 100;
			else
				this.opacity = opacity;
			this.scaleX = scaleX;
			this.scaleY = scaleY;
		}

		public DrawableObject(String texture, float x, float y,float opacity, float rotationDegrees,float scaleX,float scaleY){
			this.x = x;
			this.y = y;
			this.texture = texture;
			this.rotationDegrees = rotationDegrees;
			if (opacity > 1)
				this.opacity = opacity / 100;
			else
				this.opacity = opacity;
			this.scaleX = scaleX;
			this.scaleY = scaleY;
		}

		public DrawableObject(String texture, float x, float y,float opacity, float rotationDegrees,float scaleX,float scaleY,boolean originZero){
			this.x = x;
			this.y = y;
			this.texture = texture;
			this.rotationDegrees = rotationDegrees;
			if (opacity > 1)
				this.opacity = opacity / 100;
			else
				this.opacity = opacity;
			this.scaleX = scaleX;
			this.scaleY = scaleY;
			this.originZero = originZero;
		}

	}

	public static class Text {
		public float x,y;
		public String text;
		public int onScreenTime;
		public boolean fakeNull = false;
		int r,g,b;
		public float opacity;
		float vanishingThreshold;
		public boolean render = true;
		public Entity entityToFollow;
		public float realSize;
		//rainbow mode only
		public static final float totalJumps = 1530;
		public int jumpsPerTick; // = totalJumps/cycleTime
		public float letterMultiplicator;
		//shaking mode only
		//if i want to make it compatible with changing the text, i should change this to an array list
		public float[] shiftedCoordinates;
		public boolean[] isUp;
		public int[] timeTilStart;
		public float maxVariation;
		public int timeToReachMaxVar;

		public Text(String text, float x, float y, float realSize){
			this.text = text;
			this.x = x;
			this. y = y;
			onScreenTime = -1;
			this.r = -1;
			this.realSize = realSize;
			opacity = 1;

		}

		public Text(String text, float x, float y, float realSize,int onScreenTime,int vanishingThreshold,int r,int g,int b,float yVariation,int time){
			this.text = text;
			this.x = x;
			this. y = y;
			this.onScreenTime = onScreenTime;
			this.vanishingThreshold = vanishingThreshold;
			this.r = r;
			this.g = g;
			this.b = b;
			this.realSize = realSize;
			opacity = 1;
			maxVariation = yVariation;
			shiftedCoordinates = new float[text.length()];
			timeTilStart = new int[text.length()];
			isUp = new boolean[text.length()];
			for(int i = 0; i < text.length(); i++) {
				timeTilStart[i] = (int) (random() * time);
				isUp[i] = random() < .5 ? false : true;
			}
			timeToReachMaxVar = time;
		}


		public Text(String text, float x, float y,int onScreenTime,float opacity,float vanishingThreshold,float size,int cycleTime,float multiplicator){
			this.onScreenTime = onScreenTime;
			this.text = text;
			this.x = x;
			this. y = y;
			this.r = 255;
			this.g = 0;
			this.b = 0;
			this.opacity = opacity;
			this.vanishingThreshold = vanishingThreshold;
			this.realSize = size;
			letterMultiplicator = multiplicator;
			jumpsPerTick = (int) (totalJumps/cycleTime);

		}

		public Text(String text, float x, float y,int onScreenTime,int r, int b, int g,float opacity,float vanishingThreshold,float size){
			this.onScreenTime = onScreenTime;
			this.text = text;
			this.x = x;
			this. y = y;
			this.r = r;
			this.g = g;
			this.b = b;
			this.opacity = opacity;
			this.vanishingThreshold = vanishingThreshold;
			this.realSize = size;
		}


		public Text(String text, float x, float y,int onScreenTime,Entity entityToFollow,float size){
			this.text = text;
			this.onScreenTime = onScreenTime;
			this.x = x;
			this. y = y;
			this.entityToFollow = entityToFollow;
			this.r = -1;
			realSize = size;
			opacity = 1;
		}

		public Text(){}

		public Text(String text, float x, float y, int size, int timeOnScreen) {
			this.text = text;
			this.x = x;
			this. y = y;
			onScreenTime = -1;
			this.r = -1;
			this.realSize = size;
			this.onScreenTime = timeOnScreen;
			opacity = 1;
		}

		//for late shake initiation
		public void initiateShake(float yVariation, int time){
			maxVariation = yVariation;
			shiftedCoordinates = new float[text.length()];
			timeTilStart = new int[text.length()];
			isUp = new boolean[text.length()];
			for(int i = 0; i < text.length(); i++) {
				timeTilStart[i] = (int) (random() * time);
				isUp[i] = random() < .5 ? false : true;
			}
			timeToReachMaxVar = time;
		}


		public final static float charSize = 8;
		public void drawAll(float x, float y, float opacity){
			char[] letters = text.toCharArray();
			int lineJumps = 0;
			float addition;
			if(letters.length > 0)
				addition = -(realSize/charSize*((getTexture(letters[0]).size+1) +(charSize - getTexture(letters[0]).size)));
			else
				addition = 0;
			if(jumpsPerTick != 0)
				rainbowColorCalculation();
			for(int i = 0; i < letters.length; i++){
				if(letters[i] == '\n'){
					lineJumps++;
					addition = 0;
					continue;
				}
				addition += realSize/charSize*(getTexture(letters[i]).size + 1);
				float[] colors = {r,g,b};
				if(letterMultiplicator != 0)
					colors = toFloat(rainbowColorCalculationPerLetter(letters.length-i,(int) colors[0],(int) colors[1],(int) colors[2]));
				if(shiftedCoordinates == null || shiftedCoordinates.length < letters.length || maxVariation == 0)
					drawer(getTexture(letters[i]).texture,x+(addition),y - lineJumps*realSize*1.5f - realSize,0,opacity,false,false,0,
						realSize/charSize,realSize/charSize,colors[0]/255,colors[1]/255,colors[2]/255,true);
				else {
					processShake();
					drawer(getTexture(letters[i]).texture, x + (addition), y - lineJumps * realSize*1.5f - realSize + shiftedCoordinates[i]
							, 0, opacity, false, false, 0,
							realSize / charSize, realSize / charSize, colors[0] / 255, colors[1] / 255, colors[2] / 255, true);
				}

			}
		}

		//changing these can be fun!
		public static final int upperEnd = 255;
		public static final int lowerEnd = 0;
		public void rainbowColorCalculation(){
			if (b <= lowerEnd && r >= upperEnd && g != upperEnd)
				g += jumpsPerTick;
			else if (b <= lowerEnd && g >= upperEnd && r != lowerEnd)
				r -= jumpsPerTick;
			else if (r <= lowerEnd && g >= upperEnd && b != upperEnd)
				b += jumpsPerTick;
			else if (r <= lowerEnd && b >= upperEnd && g != lowerEnd)
				g -= jumpsPerTick;
			else if (g <= lowerEnd && b >= upperEnd && r != upperEnd)
				r += jumpsPerTick;
			else if (g <= lowerEnd && r >= upperEnd  && b != lowerEnd)
				b -= jumpsPerTick;
			r = r > upperEnd ? upperEnd : r < lowerEnd ? lowerEnd : r;
			g = g > upperEnd ? upperEnd : g < lowerEnd ? lowerEnd : g;
			b = b > upperEnd ? upperEnd : b < lowerEnd ? lowerEnd : b;
		}

		public int[] rainbowColorCalculationPerLetter(int letter, int r, int g, int b){
			for(int i = 0; i < letter; i++) {
				if (r == upperEnd && g != upperEnd && b == lowerEnd)
					g += jumpsPerTick*letterMultiplicator;
				else if (b == lowerEnd && g == upperEnd && r != lowerEnd)
					r -= jumpsPerTick*letterMultiplicator;
				else if (r == lowerEnd && g == upperEnd && b != upperEnd)
					b += jumpsPerTick*letterMultiplicator;
				else if (r == lowerEnd && b == upperEnd && g != lowerEnd)
					g -= jumpsPerTick*letterMultiplicator;
				else if (g == lowerEnd && b == upperEnd && r != upperEnd)
					r += jumpsPerTick*letterMultiplicator;
				else if (b != lowerEnd && g == lowerEnd && r == upperEnd)
					b -= jumpsPerTick*letterMultiplicator;
				r = r > upperEnd ? upperEnd : r < lowerEnd ? lowerEnd : r;
				g = g > upperEnd ? upperEnd : g < lowerEnd ? lowerEnd : g;
				b = b > upperEnd ? upperEnd : b < lowerEnd ? lowerEnd : b;
			}
			return new int[]{r,g,b};
		}


		public void processShake(){
			for(int i = 0; i < text.length(); i++){
				if (timeTilStart[i] > 0){
					timeTilStart[i]--;
					continue;
				}
				if (shiftedCoordinates[i] == 0)
					shiftedCoordinates[i] += random() > .5 ? maxVariation/timeToReachMaxVar : -maxVariation/timeToReachMaxVar;
				else if (isUp[i]){
					shiftedCoordinates[i] += maxVariation/timeToReachMaxVar;
					if(shiftedCoordinates[i] >= maxVariation)
						isUp[i] = false;
					else if (shiftedCoordinates[i] == 0)
						isUp[i] = random() < .5 ? true : false;
				} else if(!isUp[i]){
					shiftedCoordinates[i] -= maxVariation/timeToReachMaxVar;
					if(shiftedCoordinates[i] <= -maxVariation)
						isUp[i] = true;
					else if (shiftedCoordinates[i] == 0)
						isUp[i] = random() < .5 ? true : false;
				}
			}
		}



		public static float textSize(String text, float realSize){
			char[] letters = text.toCharArray();
			float counter = 0;
			for(char c : letters)
				counter += realSize/charSize*(getTexture(c).size + 1);
			return counter;
		}

		public static float adequateSize(String text, float maxXSpace){
			char[] letters = text.toCharArray();
			float counter = 0;
			for(char c : letters)
				counter += (getTexture(c).size + 1);
			return maxXSpace/counter*charSize;
		}


		public static Letters getTexture(char character){
			switch(character){
				case 'A': return Letters.A;
				case 'B': return Letters.B;
				case 'C': return Letters.C;
				case 'D': return Letters.D;
				case 'E': return Letters.E;
				case 'F': return Letters.F;
				case 'G': return Letters.G;
				case 'H': return Letters.H;
				case 'I': return Letters.I;
				case 'J': return Letters.J;
				case 'K': return Letters.K;
				case 'L': return Letters.L;
				case 'M': return Letters.M;
				case 'N': return Letters.N;
				case 'Ñ': return Letters.ENNE;
				case 'O': return Letters.O;
				case 'P': return Letters.P;
				case 'Q': return Letters.Q;
				case 'R': return Letters.R;
				case 'S': return Letters.S;
				case 'T': return Letters.T;
				case 'U': return Letters.U;
				case 'V': return Letters.V;
				case 'W': return Letters.W;
				case 'X': return Letters.X;
				case 'Y': return Letters.Y;
				case 'Z': return Letters.Z;
				case 'a': return Letters.a;
				case 'b': return Letters.b;
				case 'c': return Letters.c;
				case 'd': return Letters.d;
				case 'e': return Letters.e;
				case 'f': return Letters.f;
				case 'g': return Letters.g;
				case 'h': return Letters.h;
				case 'i': return Letters.i;
				case 'j': return Letters.j;
				case 'k': return Letters.k;
				case 'l': return Letters.l;
				case 'm': return Letters.m;
				case 'n': return Letters.n;
				case 'ñ': return Letters.enne;
				case 'o': return Letters.o;
				case 'p': return Letters.p;
				case 'q': return Letters.q;
				case 'r': return Letters.r;
				case 's': return Letters.s;
				case 't': return Letters.t;
				case 'u': return Letters.u;
				case 'v': return Letters.v;
				case 'w': return Letters.w;
				case 'x': return Letters.x;
				case 'y': return Letters.y;
				case 'z': return Letters.z;
				case ' ': return Letters.BLANK;
				case ':': return Letters.CO;
				case ';': return Letters.SECO;
				case ',': return Letters.COMM;
				case '.': return Letters.DOT;
				case '1': return Letters.ON;
				case '2': return Letters.TW;
				case '3': return Letters.TH;
				case '4': return Letters.FO;
				case '5': return Letters.FI;
				case '6': return Letters.SI;
				case '7': return Letters.SE;
				case '8': return Letters.EI;
				case '9': return Letters.NI;
				case '0': return Letters.ZE;
				case '\'':return Letters.APO;
				case '!': return Letters.EXC;
				case '¡': return Letters.EXO;
				case '?': return Letters.INTC;
				case '¿': return Letters.INTO;
				case '_': return Letters.UNDS;
				case '-': return Letters.DASH;
				case '@': return Letters.ARR;
				case '\"':return Letters.QUO;
				case '+': return Letters.PL;
				case '%': return Letters.PER;
				case '*': return Letters.AST;
				case '=': return Letters.EQU;
				case '(': return Letters.BRO;
				case ')': return Letters.BRC;
				case '\\':return Letters.CDD;
				case '/': return Letters.CCDD;
				case '>': return Letters.SUCO;
				case '<': return Letters.INCO;
				case '#': return Letters.PO;
				case '·': return Letters.MUAR;
				case '[': return Letters.BKC;
				case ']': return Letters.BKO;
				case '{': return Letters.CBKO;
				case '}': return Letters.CBKC;
				case 'º': return Letters.MOI;
				case 'ª': return Letters.FOI;
				case '&': return Letters.AND;
				case 'ü': return Letters.DIEU;
				case '$': return Letters.DOLL;
			}
			return null;
		}

		enum Letters{
			A("A",5),B("B",5),C("C",6),D("D",5),E("E",5),
			F("F",5),G("G",6),H("H",4),I("I",5),
			J("J",3),K("K",4),L("L",3),M("M",7),N("N",5),
			O("O",6),P("P",5),Q("Q",6),R("R",5),S("S",5),
			T("T",5),U("U",5),V("V",5),W("W",7),X("X",5),Y("Y",5),Z("Z",6),
			a("aa",5),
			b("bb",4),c("cc",4),d("dd",4),e("ee",5),
			f("ff",4),g("gg",4),h("hh",4),i("ii",2),j("jj",3),k("kk",3),
			l("ll",2),m("mm",5),n("nn",4),o("oo",5),p("pp",4),q("qq",5),
			r("rr4",4),s("ss",4),t("tt",3),u("uu",5),v("vv",5),
			w("ww",5),x("xx",4),y("yy",3),z("zz",4),ENNE("ENNE",5),enne("ennne",4),
			ON("1",3),TW("2",5),TH("3",5),FO("4",5),FI("5",5),SI("6",5),SE("7",5),
			EI("8",5),NI("9",5),
			ZE("0",5),BLANK("Space",4),
			CO("Colon",2),SECO("Semicolon",2),COMM("Comma",2),DOT("Dot",2),
			APO("Apostrophe",1),EXO("ExclamationOpen",2),EXC("ExclamationClose",2),
			INTO("InterrogationOpen",4),INTC("InterrogationClose",4),UNDS("Underscore",8),
			DASH("Minus",3),ARR("Arroba",8),PL("Plus",3),QUO("QuotationMarks",4),PER("Percent",4),
			AST("Asterisk",3),EQU("Equal",3),
			BRO("BraceOpen",2),BRC("BraceClose",2),CDD("ClockwiseDiagonalDash",5),CCDD("CounterclockwiseDiagonalDash",5),
			SUCO("SuperiorityComparator",4),INCO("InferiorityComparator",4),
			PO("Pound",5),MUAR("MultiplicationArithmetic",2),
			BKO("BracketOpen",3),BKC("BracketClose",3),CBKO("CurlyBracketOpen",4),
			CBKC("CurlyBracketClose",4),MOI("MascOrdIndicator",3),FOI("FemOrdIndicator",3),
			AND("Anderson",5),DIEU("DieresisU",5),
			DIV("Division",3),DOLL("Dollar",5)
			;

			final int size;
			final String texture;
			Letters(String texture, int size){
				this.size = size;
				this.texture = texture;
			}

		}




		public void draw(){
			float functionalOpacity = opacity;
			int r = this.r,g = this.g,b = this.b;
			if (r != -1)
				functionalOpacity = vanishingThreshold >= onScreenTime && vanishingThreshold > 0 ? opacity * (1 - (vanishingThreshold - onScreenTime) / (vanishingThreshold)) : opacity;
			if(entityToFollow!=null && getRender())
				drawAll(x + entityToFollow.x,y + entityToFollow.y,functionalOpacity);
			else if (getRender())
				drawAll(x,y,functionalOpacity);
			onScreenTime--;
			if(onScreenTime == 0){
				fakeNull = true;
				onFinishOverridable();
			}

		}

		public void drawStatic(){
			Vector3 coords = GameScreen.getCamara().camara.unproject(new Vector3(x,y,0f));
			float functionalOpacity = opacity;
			int r = this.r,g = this.g,b = this.b;
			if (r != -1 && vanishingThreshold > 0)
				functionalOpacity = vanishingThreshold >= onScreenTime && vanishingThreshold > 0 ? opacity * (1 - (vanishingThreshold - onScreenTime) / (vanishingThreshold)) : opacity;
			if(getRender())
				drawAll(coords.x,coords.y,functionalOpacity);
			onScreenTime--;
			if(onScreenTime == 0){
				fakeNull = true;
				onFinishOverridable();
			}
		}


		public void onFinishOverridable(){}


		public void setColor(int... color){
			this.r = color[0];
			this.g = color[1];
			this.b = color[2];
		}

	}


	public static class Animation extends Entity {
		public float startX;
		public float startY;
		public File file;
		public Scanner fileReader;
		public int line = 0;
		public ArrayList<String> code = new ArrayList<>();
		public int framesForCurrentAnimation = 0;
		public ArrayList<Used> listOfUsedLoops = new ArrayList<>();
		public float opacity = 1;


		int glideFrames;
		boolean isGliding = false;
		float glidePixelsPerFrameX, glidePixelsPerFrameY;
		float glideFinalX, glideFinalY;

		float radius;
		float angleR;
		float angleRSection;
		float angleRStart;
		float orbitFrames;
		float orbitCounter;
		boolean isOrbiting;
		boolean clockwise;

		public boolean finished = false;

		Entity entityToFollow;

		public boolean flipX = false;
		String name;

		public float scaleX = 1, scaleY = 1;

		public Animation(String file, float x, float y){
			name = file;
			try {
				this.file = new File("Animations//" + file + ".ani");
				fileReader = new Scanner(new FileReader(this.file));
				startX = x;startY = y;this.x = x;this.y = y;
				read();
				opacity = 1;
				base = height = globalSize();
			} catch (FileNotFoundException ignored){text("ANIMATION NOT FOUND ", chara.x,chara.y,100,40,255,255,40,1,50);
			print("Name of NFA: " + name);}
		}

		public Animation(String file, Entity entityToFollow){
			name = file;
			try {
				this.entityToFollow = entityToFollow;
				this.file = new File("Animations//" + file + ".ani");
				fileReader = new Scanner(new FileReader(this.file));
				this.x = entityToFollow.x;this.y = entityToFollow.y;
				read();
				opacity = 1;
				base = height = globalSize();
			} catch (FileNotFoundException ignored){text("ANIMATION NOT FOUND ", chara.x,chara.y,100,40,255,255,40,1,50);
				print("Name of NFA: " + name);}
		}

		public void play(){
			if (line < code.size()) {
				boolean didRunCode = false;
				String reader = code.get(line);
			//	print("Line at " + line + " was: " + reader);
				if (reader.contains("<") && reader.contains(">")) {
					didRunCode = true;
					String opacityString = reader.substring(reader.indexOf("<") + 1, reader.indexOf(">"));
					opacity = opacityString.contains("+") || opacityString.contains("-") ?
							Float.parseFloat(opacityString) + opacity:
							Float.parseFloat(opacityString);
					opacity = opacity > 1 ? 1 : opacity;
					opacity = opacity < 0 ? 0 : opacity;
				}
				if (reader.contains(";") && reader.indexOf(';') != reader.lastIndexOf(';')) {
					didRunCode = true;
					if (entityToFollow == null){
						x = startX;
						y = startY;
					} else {
						x = entityToFollow.x;
						y = entityToFollow.y;
						startX = 0;
						startY = 0;
					}
				}
				if (reader.charAt(0) == '?') {
					didRunCode = true;
					framesForCurrentAnimation = Integer.parseInt(reader.substring(1, reader.lastIndexOf('?')));
					int indexOfCoordinateDelimitator = reader.contains("/") ? reader.indexOf('/'): reader.contains("*") ? reader.indexOf("*"): reader.indexOf(';');

					if (reader.contains(":")) {
						if (reader.indexOf(':') == reader.lastIndexOf(':') && !(reader.contains("y"))) {
							move(true, Float.parseFloat(reader.substring(reader.indexOf(':') + 1, indexOfCoordinateDelimitator)));
						}
						else if (reader.contains("y")) {
							move(false, Float.parseFloat(reader.substring(reader.indexOf(':') + 2, indexOfCoordinateDelimitator)));
						}
						else {
							move(true, Float.parseFloat(reader.substring(reader.indexOf(':') + 1, reader.lastIndexOf(':'))));
							move(false, Float.parseFloat(reader.substring(reader.lastIndexOf(':') + 1, indexOfCoordinateDelimitator)));
						}
					}
					texture = reader.substring((reader.contains("~") ? reader.indexOf('~') : reader.lastIndexOf('?')) + 1,
								reader.contains("<") ?
									reader.indexOf('<'):
								reader.contains(":")?
									reader.indexOf(':'):
								indexOfCoordinateDelimitator);

					if(reader.contains("/") && (reader.contains("X") && reader.contains("Y"))) {
						glideFrames =
								reader.lastIndexOf('/') + 1 == reader.lastIndexOf('X') ?
									framesForCurrentAnimation:
									Integer.parseInt(reader.substring(reader.lastIndexOf("/") + 1, reader.lastIndexOf("X")));

						glide(reader.lastIndexOf('X') + 1 == reader.lastIndexOf('Y') ? 0:
								Float.parseFloat(reader.substring(reader.lastIndexOf("X") + 1, reader.lastIndexOf("Y"))),
							  reader.lastIndexOf('Y') + 1 ==  (reader.contains("*") ? reader.indexOf("*"): reader.indexOf(';')) ? 0:
								Float.parseFloat(reader.substring(reader.lastIndexOf("Y") + 1, reader.contains("*") ?
																								reader.indexOf("*"):
																								reader.indexOf(";"))));
					}
					if(reader.contains("*")){
						String orbital = reader.substring(reader.indexOf("*"),reader.indexOf(";"));
						orbitStart(
								Float.parseFloat(orbital.substring(orbital.indexOf("r")+1,orbital.indexOf("a"))),
								Float.parseFloat(orbital.substring(orbital.indexOf("a")+1,orbital.contains("s") ? orbital.indexOf('s'): orbital.contains("c") ? orbital.indexOf("c"): orbital.indexOf(";"))),
								x,y,
								orbital.contains("s") ? Float.parseFloat(orbital.substring(orbital.indexOf("s")+1,
								orbital.contains("c") ? orbital.indexOf("c") : orbital.indexOf(";"))) : 0,
								!orbital.contains("cc") && orbital.contains("c"),
								orbital.indexOf("*")+1 == orbital.indexOf("r") ? framesForCurrentAnimation :
								Float.parseFloat(orbital.substring(orbital.indexOf("*")+1,orbital.indexOf("r"))));
					}
					flipX = reader.contains("~");
				}
				if (reader.charAt(0) == '^') {
					didRunCode = true;
					boolean willSkip = false;
					for (Used u : listOfUsedLoops)
						if (line == u.line() && u.bool()) {
							willSkip = true;
							break;
						}
					if (!willSkip) {
						listOfUsedLoops.add(new Used(true, line));
						line -= Integer.parseInt(reader.substring(reader.indexOf('^') + 1, reader.contains("*") ? reader.indexOf("*"):reader.indexOf(';')));
						play();
						return;
					}
				}
				line++;
				if (!didRunCode)
					play();
			}
			else {
				stop();
			}
		}

		//you dont need to write "finished = true" in this method's overrides
		public void onFinish(){

		}

		public void stop(){
			finished = true;
			onFinish();
		}



		public void move(boolean isOnX, float coordinate){
			coordinate = coordinate * globalSize();
			if (entityToFollow == null) {
				if (isOnX) x += coordinate;
				else y += coordinate;
			} else{
				if (isOnX)
					startX += coordinate;
				else
					startY += coordinate;
				x = entityToFollow.x + startX;
				y = entityToFollow.y + startY;
				startY = 0; startX = 0;
			}
		}


		public void glide(float x, float y){
			isGliding = true;
			glideFinalX = globalSize() * x; glideFinalY = globalSize() * y;
			glideFrames = min(glideFrames, framesForCurrentAnimation);
			glidePixelsPerFrameX = glideFinalX / glideFrames;
			glidePixelsPerFrameY = glideFinalY / glideFrames;

		}

		public void orbitStart(float radius, float angleD, float x, float y, float angleDStart, boolean clockwise,float orbitFrames){
			this.isOrbiting = true;
			this.radius = radius * globalSize();
			angleR = (float) (angleD * PI / 180);
			angleRStart = (float) (angleDStart * PI / 180);
			this.clockwise = clockwise;
			angleRSection = (float) ((angleD*PI/180)/orbitFrames);
			this.orbitFrames = orbitFrames;
			this.orbitCounter = 0;
			this.x = x; this.y = y;
		}






		public void orbit(){
			orbitCounter++;
			x -= (float) (radius * (cos(angleRSection*(orbitCounter-1) + angleRStart)
					- cos(angleRSection*orbitCounter + angleRStart)));

			if (!clockwise)
				y -= (float) (radius * (sin(angleRSection*(orbitCounter-1) + angleRStart)
						- sin(angleRSection*orbitCounter + angleRStart)));

			else
				y += (float) (radius * (sin(angleRSection*(orbitCounter-1) + angleRStart)
						- sin(angleRSection*orbitCounter + angleRStart)));

			if(orbitCounter >= orbitFrames)
				isOrbiting = false;
		}






		public final void update(){
			updateOverridable();
			if (entityToFollow != null)
				move(true,0);
			if (isOrbiting)
				orbit();
			if (isGliding){
				x += glidePixelsPerFrameX;
				y += glidePixelsPerFrameY;
				glideFrames--;
				if (glideFrames <= 0) isGliding = false;
			}
			if (framesForCurrentAnimation <= 0) play();
			else framesForCurrentAnimation--;
			updateOverridableFinal();
		}

		public void updateOverridable(){}

		public void updateOverridableFinal(){}

		public final void read(){
			while (fileReader.hasNext()){
				code.add(fileReader.next());
			}
		}

		public static class Used {
			boolean bool;
			int line;
			private Used(boolean bool, int line){
				this.bool = bool;
				this.line = line;
			}

			private boolean bool(){return bool;}

			private void bool(boolean bool){this.bool = bool;}

			private void line(int line){this.line = line;}

			private int line (){return line;}

		}

	}


	private static class AtlasAndName{
		TextureAtlas atlas;
		String path;

		private AtlasAndName(TextureAtlas atlas, String path){
			this.atlas = atlas;
			this.path = path;
		}

		private TextureAtlas getAtlas(){return atlas;}
		private void setAtlas(TextureAtlas atlas){this.atlas = atlas;}

		private String getName(){return path;}
		private void setPath(String path){this.path = path;}

	}


	private static class DrawableTexture{
		public float x,y;
		public Texture texture;

		public DrawableTexture(Texture texture, float x, float y){
			this.x = x;
			this.y = y;
			this.texture = texture;
		}


	}

}
