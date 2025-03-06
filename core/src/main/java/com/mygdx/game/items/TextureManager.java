package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;

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
	private static TextureManager tm;
	static ArrayList<AtlasAndName> atlases;


	public TextureManager (){
		drawables = new ArrayList<>();
		priorityDrawables = new ArrayList<>();
		fixatedDrawables = new ArrayList<>();
		text = new ArrayList<>();
		fixatedText = new ArrayList<>();
		batch = new SpriteBatch();
		animations = new ArrayList<>();
		atlases = new ArrayList<>();
		bounder();
		tm = this;

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
		drawer(texture,x,y,opacity,"AtlasOne.atlas");
	//	region = atlas.findRegion(texture);
	//	sprite = new Sprite(region);
	//	sprite.setAlpha(opacity);
	//	sprite.setPosition(x,y);
	//	sprite.draw(batch);
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
		region = atlas.findRegion(texture);
		sprite = new Sprite(region);
		sprite.setAlpha(opacity);
		//if percentages are >1 || <-1 'll go offscreen. 0/0 is the center of the screen.
		sprite.setPosition( xPercentage * Camara.getBase() + xOffset + Camara.getX(),
 				Camara.getY() + yPercentage * Camara.getHeight() + yOffset);
		sprite.draw(batch);
	}

	public static void animationToList(String file, float x, float y,float opacity){
		animations.add(new Animation(file,x,y,tm,opacity));
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

	public void render(Camara camara){
		getCamara(camara);

		for (TextureManager.DrawableObject d : drawables){
			if (d.texture != null)
				 drawer(d.texture,d.x,d.y,d.opacity);
		}
		drawables.clear();

		for (TextureManager.Animation a : animations){
			a.update();
			if (a.texture != null)
				drawer(a.texture,a.x,a.y,a.opacity);
		}
		ArrayList<Animation> deletionAider = new ArrayList<>(animations);
		for (TextureManager.Animation a : deletionAider) if (a.finished) animations.remove(a);

		for (TextureManager.Text t : text){
			t.drawStatic(batch,Camara.x / Camara.zoom,Camara.y / Camara.zoom);
		}
		text.clear();

		for (TextureManager.DrawableObject d : priorityDrawables){
			if (d.texture != null)
				drawer(d.texture,d.x,d.y,d.opacity);
		}
		priorityDrawables.clear();

		for (TextureManager.DrawableObject d : fixatedDrawables){
			if (d.texture != null)
				fixatedScreenDrawer(d.texture,d.xPercentage,d.yPercentage,d.x,d.y,d.opacity);
		}
		fixatedDrawables.clear();
	}

	public static void text (String text,float x, float y){
		TextureManager.text.add(new Text(text,x,y));
	}


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
		BitmapFont font;


		public Text(String text, float x, float y){
			this.text = text;
			this.x = x;
			this. y = y;
			font = new BitmapFont();
		}

		public Text(String text, float x, float y, FileHandle font){
			this.text = text;
			this.x = x;
			this. y = y;
			this.font = new BitmapFont(font);
		}

		public void draw(Batch batch){
			font.draw(batch, text,x,y);
		}

		public void drawStatic(Batch batch, float x, float y){
			font.draw(batch, text,x + x,y + y);
		}

	}

	public static class Animation{
		public float startX;
		public float startY;
		public float x;
		public float y;
		public File file;
		public Scanner fileReader;
		public int line = 0;
		public ArrayList<String> code = new ArrayList<>();
		public int framesForCurrentAnimation = 0;
		public String texture;
		private final ArrayList<Used> listOfUsedLoops = new ArrayList<>();
		public TextureManager tm;
		//TODO: will have to integrate it in the animation "coding language" i created
		public float opacity;

		boolean finished = false;

		public Animation(String file, float x, float y, TextureManager tm, float opacity) {
			try {
				this.file = new File("Animations//" + file);
				fileReader = new Scanner(new FileReader(this.file));
				startX = x;startY = y;this.x = x;this.y = y;
				read();
				this.tm = tm;
				this.opacity = opacity;
			} catch (FileNotFoundException ignored){}

		}

		// Hope i dont have to debug this
		public void play(){
			if (line < code.size()) {
				boolean[] didRunCode = {true,true};
				String reader = code.get(line);
				print("Line at " + line + " was: " + reader);
				if (reader.charAt(0) == '?') {
					framesForCurrentAnimation = Integer.parseInt(reader.substring(1, reader.lastIndexOf('?')));
					if (reader.contains(":")) {
						texture = reader.substring(reader.lastIndexOf('?') + 1, reader.indexOf(':'));
						if (reader.indexOf(':') == reader.lastIndexOf(':') && !(reader.contains("y") || reader.contains("Y")))
							move(true, Float.parseFloat(reader.substring(reader.indexOf(':') + 1, reader.indexOf(';'))));
						else if (reader.contains("y") || reader.contains("Y"))
							move(false, Float.parseFloat(reader.substring(reader.indexOf(':') + 2, reader.indexOf(';'))));
						else {
							move(true, Float.parseFloat(reader.substring(reader.indexOf(':') + 1, reader.lastIndexOf(':'))));
							move(false, Float.parseFloat(reader.substring(reader.lastIndexOf(':') + 1, reader.indexOf(';'))));
						}
					} else
						texture = reader.substring(reader.lastIndexOf('?') + 1, reader.indexOf(';'));
				} else didRunCode[0] = false;
				if (reader.charAt(0) == '^') {
					boolean willSkip = false;
					for (Used u : listOfUsedLoops)
						if (line == u.line() && u.bool()) {
							willSkip = true;
							break;
						}
					if (!willSkip) {
						listOfUsedLoops.add(new Used(true, line));
						line -= Integer.parseInt(reader.substring(reader.indexOf('^') + 1, reader.indexOf(';')));
						play();
					}
				} else didRunCode[1] = false;
				line++;
				if (!didRunCode[0] && !didRunCode[1])
					play();
			}
			else finished = true;
		}


		public void stop(){
			finished = true;
		}


		public void move(boolean isOnX, float coordinate){
			if (isOnX) x += coordinate*globalSize();
			else y += coordinate*globalSize();
		}


		public void glide(float x, float y){
			//TODO
		}


		public void update(){
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












}
