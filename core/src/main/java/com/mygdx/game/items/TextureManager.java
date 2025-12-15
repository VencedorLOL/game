package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.Utils.cC;
import static com.mygdx.game.items.AttackTextProcessor.coordsUpdater;
import static com.mygdx.game.items.TextureManager.Text.createFont;
import static java.lang.Math.*;

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
				t.draw(batch);
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
				t.draw(batch);
		}
		priorityText.removeIf(tex -> tex.fakeNull);

		// Static text display

		for (TextureManager.Text t : fixatedText){
			if (!t.fakeNull && t.render) {
				t.drawStatic(batch);
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

	public static void fixatedText (String text,float x, float y,int timeOnScreen,Fonts font,int size){
		TextureManager.fixatedText.add(new Text(text,x,y,timeOnScreen,createFont(font,size)));
	}

	public static Text dinamicFixatedText (String text,float x, float y,int timeOnScreen,Fonts font,int size){
		Text text1 = new Text(text,x,y,timeOnScreen,createFont(font,size));
		TextureManager.fixatedText.add(text1);
		return text1;
	}


	public static void text (String text,float x, float y,Fonts font,int size){
		TextureManager.text.add(new Text(text,x,y,createFont(font,size)));
	}

	public static void text (String text,float x, float y,int timeTilDisappear,Fonts font,int size,Entity entityToFollow){
		TextureManager.text.add(new Text(text,x,y,timeTilDisappear,createFont(font,size),entityToFollow));
	}

	public static void text (String text,float x, float y,int timeTilDisappear,Fonts font, int size,int r, int g, int b,float opacity,float vanishingThreshold){
		TextureManager.text.add(new Text(text,x,y,timeTilDisappear,createFont(font,size),r,g,b,opacity,vanishingThreshold));
	}

	public static void text (String text,float x, float y,int timeTilDisappear,Fonts font,int size){
		if (text!= null)
			TextureManager.text.add(new Text(text,x,y,timeTilDisappear,createFont(font,size)));}

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
		public BitmapFont font;
		int r,g,b;
		public float opacity;
		float vanishingThreshold;
		public boolean render = true;
		public Entity entityToFollow;

		public Text(String text, float x, float y, BitmapFont font){
			this.text = text;
			this.x = x;
			this. y = y;
			onScreenTime = -1;
			this.font = font;
			this.r = -1;

		}

		public Text(String text, float x, float y,int onScreenTime,BitmapFont font,int r, int b, int g,float opacity,float vanishingThreshold){
			this.onScreenTime = onScreenTime;
			this.text = text;
			this.x = x;
			this. y = y;
			this.font = font;
			this.r = r;
			this.g = g;
			this.b = b;
			this.opacity = opacity;
			this.vanishingThreshold = vanishingThreshold;
		}

		public Text(String text, float x, float y,int onScreenTime,BitmapFont font){
			this.onScreenTime = onScreenTime;
			this.text = text;
			this.x = x;
			this. y = y;
			this.font = font;
			this.r = -1;
		}

		public Text(String text, float x, float y,int onScreenTime,BitmapFont font,Entity entityToFollow){
			this.text = text;
			this.onScreenTime = onScreenTime;
			this.x = x;
			this. y = y;
			this.entityToFollow = entityToFollow;
			this.font = font;
			this.r = -1;
		}

		public Text(){}


		public void draw(Batch batch){
			if (r != -1)
				font.setColor(cC(r), cC(g), cC(b), vanishingThreshold >= onScreenTime && vanishingThreshold > 0 ? opacity * (1 - (vanishingThreshold - onScreenTime) / (vanishingThreshold)) : opacity);
			if(entityToFollow!=null && getRender())
				font.draw(batch, text,x + entityToFollow.x,y + entityToFollow.y);
			else if (getRender())
				font.draw(batch, text,x,y);
			onScreenTime--;
			if(onScreenTime == 0){
				fakeNull = true;
				onFinishOverridable();
			}

		}

		public void drawStatic(Batch batch){
			Vector3 coords = GameScreen.getCamara().camara.unproject(new Vector3(x,y,0f));
			if (r != -1 && vanishingThreshold > 0)
				font.setColor(cC(r),cC(g),cC(b), vanishingThreshold >= onScreenTime ? opacity * (1-(vanishingThreshold-onScreenTime)/(vanishingThreshold)) : opacity);
			else if (r != -1)
				font.setColor(cC(r),cC(g),cC(b),1);
			if(getRender())
				font.draw(batch, text,coords.x,coords.y);
			onScreenTime--;
			if(onScreenTime == 0){
				fakeNull = true;
				onFinishOverridable();
			}
		}


		public void onFinishOverridable(){}


		public void setColor(int[] color){
			this.r = color[0];
			this.g = color[1];
			this.b = color[2];
		}

		public static BitmapFont createFont(Fonts name, int size) {
			FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
			fontParameter.size = size;
			return new FreeTypeFontGenerator(Gdx.files.internal(name.path)).generateFont(fontParameter);
		}



	}

	public enum Fonts {
		ComicSans("Fonts\\Comic Sans MS.ttf");

		public final String path;
		Fonts(String path) {
			this.path = path;
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
			} catch (FileNotFoundException ignored){text("ANIMATION NOT FOUND ", chara.x,chara.y,100,Fonts.ComicSans,40,255,255,40,1,50);
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
			} catch (FileNotFoundException ignored){text("ANIMATION NOT FOUND ", chara.x,chara.y,100,Fonts.ComicSans,40,255,255,40,1,50);
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
		}

		public void updateOverridable(){}


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
