package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.Utils.cC;
import static java.lang.Math.*;

public class TextureManager {
	public SpriteBatch batch;
	public TextureAtlas atlas;
	public Sprite sprite;
	TextureRegion region;
	Camara camara;
	static ArrayList<Text> text;
	static ArrayList<Text> fixatedText;
	static ArrayList<DrawableObject> drawables;
	static ArrayList<DrawableObject> priorityDrawables;
	static ArrayList<DrawableObject> fixatedDrawables;
	static ArrayList<Animation> animations;
	static ArrayList<AtlasAndName> atlases;
	static ArrayList<DrawableTexture> videos;


	public TextureManager (){
		drawables = new ArrayList<>();
		priorityDrawables = new ArrayList<>();
		fixatedDrawables = new ArrayList<>();
		text = new ArrayList<>();
		fixatedText = new ArrayList<>();
		batch = new SpriteBatch();
		animations = new ArrayList<>();
		atlases = new ArrayList<>();
		videos = new ArrayList<>();
		bounder();
	}

	public void getCamara(Camara camara){
		this.camara = camara;
	}


	public void bounder(){
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("Atlas/AtlasOne.atlas"));
		atlases.add(new AtlasAndName(atlas, "AtlasOne.atlas"));
	}

	private void drawer(String texture, float x, float y,float opacity){
	//	drawer(texture,x,y,opacity,"AtlasOne.atlas");
		region = atlas.findRegion(texture);
		sprite = new Sprite(region);
		sprite.setAlpha(opacity);
		sprite.setPosition(x,y);
		sprite.draw(batch);
	}

	private void textureDrawer(Texture texture, float x, float y){
		batch.draw(texture,x,y);
	}

	private void drawer(String texture, float x, float y,float opacity,String atlasPath){
		for (AtlasAndName t : atlases) {
			if (Objects.equals(t.getName(), atlasPath)) {
				region = t.getAtlas().findRegion(texture);
				sprite = new Sprite(region);
				sprite.setAlpha(opacity);
				sprite.setPosition(x, y);
				sprite.draw(batch);
				return;
			}
		}

		atlases.add(new AtlasAndName(new TextureAtlas(Gdx.files.internal("Atlas/" + atlasPath)), atlasPath));
		drawer(texture,x,y,opacity,atlasPath);

	}




	private void fixatedScreenDrawer(String texture,float xPercentage, float yPercentage, float xOffset, float yOffset, float opacity){
		//if percentages are >1 || <-1 'll go offscreen. 0/0 is the center of the screen.
		drawer(texture,xPercentage * Camara.getBase() + xOffset + Camara.getX(),
				Camara.getY() + yPercentage * Camara.getHeight() + yOffset,opacity);
	}

	public static void animationToList(String file, float x, float y){
		animations.add(new Animation(file,x,y));
	}

	public static void addToList(String texture, float x, float y){
		addToList( texture,  x,  y,1);
	}

	public static void addToList(String texture, float x, float y,float opacity){
		drawables.add(new DrawableObject(texture, x, y,opacity));
	}

	public static void addToFixatedList(String texture, float xPercentage, float yPercentage){
		addToFixatedList(texture,xPercentage,yPercentage,0,0);
	}

	public static void addToFixatedList(String texture, float xPercentage, float yPercentage,float xOffset,float yOffset){
		fixatedDrawables.add(new DrawableObject(texture, xPercentage, yPercentage,xOffset,yOffset));
	}

	public static void addToPriorityList(String texture, float x, float y){
		addToPriorityList( texture,  x,  y,1);
	}
	public static void addToPriorityList(String texture, float x, float y,float opacity){
		priorityDrawables.add(new DrawableObject(texture, x, y,opacity));
	}

	public static void renderVideo(Texture texture, float x, float y){
		videos.add(new DrawableTexture(texture, x, y));
	}


	public void render(Camara camara){
		getCamara(camara);
		// Least priority drawable objects

		for (TextureManager.DrawableObject d : drawables){
			if (d.texture != null)
				 drawer(d.texture,d.x,d.y,d.opacity);
		}
		drawables.clear();
		// Animations

		for (TextureManager.Animation a : animations){
			a.update();
			if (a.texture != null)
				drawer(a.texture,a.x,a.y,a.opacity);
		}
		animations.removeIf(ani -> ani.finished);
		// Text display

		for (TextureManager.Text t : text){
			if (!t.fakeNull)
				t.draw(batch);
		}
		text.removeIf(tex -> tex.fakeNull);
		// Static text display

		for (TextureManager.Text t : fixatedText){
			if (!t.fakeNull)
				t.drawStatic(Camara.x / Camara.zoom,Camara.y / Camara.zoom,batch);
		}
		fixatedText.removeIf(tex -> tex.fakeNull);
		// Most priority drawables

		for (TextureManager.DrawableObject d : priorityDrawables){
			if (d.texture != null)
				drawer(d.texture,d.x,d.y,d.opacity);
		}
		priorityDrawables.clear();
		// Static drawables


		for (TextureManager.DrawableObject d : fixatedDrawables){
			if (d.texture != null)
				fixatedScreenDrawer(d.texture,d.xPercentage,d.yPercentage,d.x,d.y,d.opacity);
		}
		fixatedDrawables.clear();
		//Video reserved

		for (TextureManager.DrawableTexture t : videos){
			if (t.texture != null)
				textureDrawer(t.texture,t.x,t.y);
		}
		videos.clear();

	}

	public static void fixatedText (String text,float x, float y,Fonts font,int size){
		TextureManager.fixatedText.add(new Text(text,x,y,createFont(font,size)));
	}

	public static void text (String text,float x, float y,Fonts font,int size){
		TextureManager.text.add(new Text(text,x,y,createFont(font,size)));
	}

	public static void text (String text,float x, float y,int timeTilDisappear,Fonts font, int size,int r, int g, int b,float opacity,float vanishingThreshold){
		TextureManager.text.add(new Text(text,x,y,timeTilDisappear,createFont(font,size),r,g,b,opacity,vanishingThreshold));
	}

	public static void text (String text,float x, float y,int timeTilDisappear){
		if (text!= null)
			TextureManager.text.add(new Text(text,x,y,timeTilDisappear));}

	public static class DrawableObject{
		public float x,y;
		public String texture;
		public float xPercentage;
		public float yPercentage;
		public float opacity;

		public DrawableObject(String texture, float x, float y){
			this.x = x;
			this.y = y;
			this.yPercentage = 0;
			this.xPercentage = 0;
			this.texture = texture;
			opacity = 1;
		}

		public DrawableObject(String texture, float x, float y,float opacity){
			this.x = x;
			this.y = y;
			this.yPercentage = 0;
			this.xPercentage = 0;
			this.texture = texture;
			if (opacity > 1)
				this.opacity = opacity / 100;
			else
				this.opacity = opacity;
		}


		public DrawableObject(String texture,float xPercentage,float yPercentage,float x, float y){
			this.x = x;
			this.y = y;
			this.texture = texture;
			this.xPercentage = xPercentage;
			this.yPercentage = yPercentage;
		}
	}

	public static class Text {
		public float x,y;
		public String text;
		public int onScreenTime;
		public boolean fakeNull = false;
		public BitmapFont font;
		int r,g,b;
		float opacity;
		float vanishingThreshold;

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

		public Text(String text, float x, float y,int onScreenTime){
			this.onScreenTime = onScreenTime;
			this.text = text;
			this.x = x;
			this. y = y;
			font = new BitmapFont();
			this.r = -1;
		}

		public Text(String text, float x, float y){
			this.text = text;
			this.x = x;
			this. y = y;
			onScreenTime = -1;
			font = new BitmapFont();
			this.r = -1;
		}



		public void draw(Batch batch){
			if (r != -1)
				font.setColor(cC(r),cC(g),cC(b), vanishingThreshold >= onScreenTime ? opacity * (1-(vanishingThreshold-onScreenTime)/(vanishingThreshold)) : opacity);
			font.draw(batch, text,x,y);
			onScreenTime--;
			if(onScreenTime == 0){
				fakeNull = true;
			}

		}

		public void drawStatic(float x, float y,Batch batch){
			font.draw(batch, text,x,y);
			onScreenTime--;
			if(onScreenTime == 0){
				fakeNull = true;
			}
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
		private final ArrayList<Used> listOfUsedLoops = new ArrayList<>();
		public TextureManager tm;
		public float opacity;

		int glideFrames;
		boolean isGliding = false;
		float glidePixelsPerFrameX, glidePixelsPerFrameY;
		float glideFinalX, glideFinalY;

		// thanks you java for doing math in radians instead of degrees. Thanks.
		float radius;
		float angleR;
		float angleRSection;
		float angleRStart;
		float orbitFrames;
		float orbitCounter;
		boolean isOrbiting;
		boolean clockwise;

		boolean finished = false;

		Entity entityToFollow;

		public Animation(String file, float x, float y){
			try {
				this.file = new File("Animations//" + file + ".ani");
				fileReader = new Scanner(new FileReader(this.file));
				startX = x;startY = y;this.x = x;this.y = y;
				read();
				opacity = 1;
				base = height = globalSize();
			} catch (FileNotFoundException ignored){}
		}

		public Animation(String file, Entity entityToFollow){
			try {
				this.entityToFollow = entityToFollow;
				this.file = new File("Animations//" + file + ".ani");
				fileReader = new Scanner(new FileReader(this.file));
				this.x = entityToFollow.x;this.y = entityToFollow.y;
				read();
				opacity = 1;
				base = height = globalSize();
			} catch (FileNotFoundException ignored){}
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
					} else
						texture = reader.substring(reader.lastIndexOf('?') + 1,
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
				finished = true;
				print("Animation Finished");
				onFinish();
			}
		}

		public void onFinish(){

		}

		public void stop(){
			finished = true;
		}



		public void move(boolean isOnX, float coordinate){
			coordinate = coordinate * globalSize();
			if (entityToFollow == null) {
				if (isOnX) x += coordinate;
				else y += coordinate;
			} else{
				if (isOnX) {
					startX += coordinate;
				}
				else {
					startY += coordinate;
				}
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

		//this is the first time in my life i have to use sine and cosine functions outside of math class
		// THANKS JAVA FOR USING RADIANS INSTEAD OF DEGREES
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
			print("Is it clockwise? " + clockwise+".");
		}

		// epiphany moment when i realized i dont need the sin/cos * radius, but sin/cos * radius minus previus sin/cos * radius.





		public void orbit(){
			orbitCounter++;
			if (!clockwise){
				x -= (float) (radius * cos(angleRSection*(orbitCounter-1) + angleRStart)
						- radius * cos(angleRSection*orbitCounter + angleRStart));

				y -= (float) (radius * sin(angleRSection*(orbitCounter-1) + angleRStart)
						- radius * sin(angleRSection*orbitCounter + angleRStart));

			} else {
				x -= (float) (radius * cos(angleRSection*(orbitCounter-1) + angleRStart)
						- radius * cos(angleRSection*orbitCounter + angleRStart));

				y += (float) (radius * sin(angleRSection*(orbitCounter-1) + angleRStart)
						- radius * sin(angleRSection*orbitCounter	 + angleRStart));

			} if(orbitCounter >= orbitFrames)
				isOrbiting = false;
		}






		public void update(){
			//IMAGINE GLIDING CIRCLES THATD BE SO COOL
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


		public void read(){
			while (fileReader.hasNext()){
				code.add(fileReader.next());
			}
		}

		private class Used {
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



	public static BitmapFont createFont(Fonts name, int size) {
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = size;
		return new FreeTypeFontGenerator(Gdx.files.internal(name.path)).generateFont(fontParameter);
	}

	public enum Fonts {
		ComicSans("Fonts\\Comic Sans MS.ttf");

		public final String path;
		Fonts(String path) {
			this.path = path;
		}
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
