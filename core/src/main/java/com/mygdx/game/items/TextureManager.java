package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.*;

import java.util.ArrayList;

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

	public TextureManager (){
		drawables = new ArrayList<>();
		priorityDrawables = new ArrayList<>();
		fixatedDrawables = new ArrayList<>();
		text = new ArrayList<>();
		fixatedText = new ArrayList<>();
		batch = new SpriteBatch();
		bounder();

	}

	public void getCamara(Camara camara){
		this.camara = camara;
	}


	public void bounder(){
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("Atlas/AtlasOne.atlas"));
	}

	private void drawer(String texture, float x, float y){
		region = atlas.findRegion(texture);
		sprite = new Sprite(region);
		sprite.setPosition(x,y);
		sprite.draw(batch);
	}

	private void fixatedScreenDrawer(String texture,float xPercentage, float yPercentage, float xOffset, float yOffset){
		region = atlas.findRegion(texture);
		sprite = new Sprite(region);
		//if percentages are >1 || <-1 'll go offscreen. 0/0 is the center of the screen.
		sprite.setPosition( xPercentage * Camara.getBase() + xOffset + Camara.getX(),
 				Camara.getY() + yPercentage * Camara.getHeight() + yOffset);
		sprite.draw(batch);
	}



	public static void addToList(String texture, float x, float y){
		drawables.add(new DrawableObject(texture, x, y));
	}

	public static void addToFixatedList(String texture, float xPercentage, float yPercentage){
		addToFixatedList(texture,xPercentage,yPercentage,0,0);
	}

	public static void addToFixatedList(String texture, float xPercentage, float yPercentage,float xOffset,float yOffset){
		fixatedDrawables.add(new DrawableObject(texture, xPercentage, yPercentage,xOffset,yOffset));
	}

	public static void addToPriorityList(String texture, float x, float y){
		priorityDrawables.add(new DrawableObject(texture, x, y));
	}

	public void render(Camara camara){
		getCamara(camara);
		for (TextureManager.DrawableObject d : drawables){
			if (d.texture != null)
				 drawer(d.texture,d.x,d.y);
		}
		drawables.clear();
		for (TextureManager.Text t : text){
			t.drawStatic(batch,Camara.x / camara.zoom,Camara.y / camara.zoom);
		}
		text.clear();
		for (TextureManager.DrawableObject d : priorityDrawables){
			if (d.texture != null)
				drawer(d.texture,d.x,d.y);
		}
		priorityDrawables.clear();
		for (TextureManager.DrawableObject d : fixatedDrawables){
			if (d.texture != null)
				fixatedScreenDrawer(d.texture,d.xPercentage,d.yPercentage,d.x,d.y);
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

		public DrawableObject(String texture, float x, float y){
			this.x = x;
			this.y = y;
			this.yPercentage = 0;
			this.xPercentage = 0;
			this.texture = texture;
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




}
