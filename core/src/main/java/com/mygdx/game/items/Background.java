package com.mygdx.game.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.floor;
import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.TextureManager.*;

public class Background {

	public static ArrayList<Background> backgrounds = new ArrayList<>();

	public static void clearBG(){
		backgrounds.clear();
	}

	public static void addBG(Background bG){
		backgrounds.add(bG);
	}

	public static void addBG(String texture){
		backgrounds.add(new Background(texture));
	}

	@SuppressWarnings("all")
	public static void renderBGs(){
		float[] aid = getCamara().getDimensions();
		for(float f : aid)
			if (f == 1/0f || f == -1/0f || Float.isNaN(f))
				return;
		for (Background b : backgrounds)
			b.render(aid[0],aid[1],aid[2],aid[3]);
	}

	public Background(String texture){
		this.texture = texture;
	}

	String texture;
	Sprite sprite;
	TextureRegion region;
	public void render(float xOr, float xFin, float yOr, float yFin) {
		if(getRender()) {
			try {
				region = atlas.findRegion(texture);
				sprite = new Sprite(region);
				float w = sprite.getWidth(), h = sprite.getHeight();
				for (int i = (int) (floor(xOr / w) * w - w); i < xFin + w; i = (int)(i + w)) {
					for (int j = (int) (floor(yOr / h)*h - h); j < yFin + h; j = (int)(j + h)) {
						sprite.setPosition(i, j);
						sprite.draw(batch);
					}
				}
			} catch (NullPointerException ignored) {printErr("[BG]: NPE caught while trying to print " + texture + " AT BACKGROUND CLASS");}
		}
	}



}
