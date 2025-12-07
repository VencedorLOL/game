package com.mygdx.game.items.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.Camara;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.Text.createFont;
import static com.mygdx.game.items.TextureManager.fixatedDrawables;
import static com.mygdx.game.items.TextureManager.text;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Ability{
	public String textureIcon;
	public String name;
	public boolean isItActive;
	public int cooldown;
	public int cooldownCounter;
	public float x, y;
	public float radius;
	Circle circle = new Circle();
	TextureManager.Text text;


	public Ability(String texture, String name, int cooldown,float x, float y,float radius){
		textureIcon = texture;
		this.name = name;
		this.cooldown = cooldown;
		cooldownCounter = cooldown;
		this.radius = radius;
		this.x = x;
		this.y = y;
		text = new TextureManager.Text();
		text.render = false;
		text.onScreenTime = 1;
		text.font = createFont(TextureManager.Fonts.ComicSans,50);
		text.setColor(new int[]{255,255,255});
		text.opacity = 1;
		circle.set(Gdx.graphics.getWidth() * this.x + getCamara().getX(),
				Gdx.graphics.getHeight() * this.y + getCamara().getY(),radius);
	}

	boolean touchedIn, touchedOut;
	public boolean isBeingPressed(){
		// https://imgur.com/a/bgzC4LK
		Vector3 vector = authenticClick();
		float[] coords = getCamara().unproject(Gdx.graphics.getWidth() * x / 100,Gdx.graphics.getHeight() * y  / 100);
		circle.setPosition(coords[0] + globalSize()/2f ,coords[1] + globalSize()/2f);
		if(leftClickJustPressed() && sqrt(pow(circle.x - vector.x, 2) + pow(circle.y - vector.y, 2)) <= circle.radius)
			touchedIn = true;
		else if (leftClickJustPressed())
			touchedIn = false;
		if(leftClickReleased() && sqrt(pow(circle.x - vector.x, 2) + pow(circle.y - vector.y, 2)) <= circle.radius)
			touchedOut = true;
		else if (leftClickReleased())
			touchedOut = false;
		if(touchedIn && touchedOut){
			touchedIn = false; touchedOut = false; return true;
		}
		return false;

	}

	public boolean isBeingHovered(){
		Vector3 vector = authenticClick();
		float[] coords = getCamara().unproject(Gdx.graphics.getWidth() * x / 100,Gdx.graphics.getHeight() * y  / 100);
		circle.setPosition(coords[0] + globalSize()/2f ,coords[1] + globalSize()/2f);
		return sqrt(pow(circle.x - vector.x, 2) + pow(circle.y - vector.y, 2)) <= circle.radius;
	}


	public void renderKey(String key){
		fixatedDrawables.add(new TextureManager.DrawableObject(key.equals("H") ? "HKey" : "BKey",Gdx.graphics.getWidth() * x / 100 + globalSize()/2f - 28,Gdx.graphics.getHeight() * y / 100 + 24,1,0,1,1,true));
	}


	public void render() {
		if (cooldownCounter >= cooldown){
			TextureManager.addToFixatedList(textureIcon, Gdx.graphics.getWidth() * x / 100, Gdx.graphics.getHeight() * y / 100, 1, 0);
			if (isBeingHovered())
				TextureManager.addToFixatedList("SelectedAbility", Gdx.graphics.getWidth() * x / 100, Gdx.graphics.getHeight() * y / 100, .8f, 0, 175, 175, 175);
		} else {
			TextureManager.addToFixatedList(textureIcon, Gdx.graphics.getWidth() * x / 100, Gdx.graphics.getHeight() * y / 100, 1, 0, 175, 175, 175);
			float[] coords = getCamara().unproject(Gdx.graphics.getWidth() * x / 100,Gdx.graphics.getHeight() * y  / 100);
			TextureManager.priorityText.add(text);
			text.onScreenTime = 1;
			text.fakeNull = false;
			text.render = true;
			text.text = String.format("%.0f",( (float) cooldown - cooldownCounter));
			text.x = coords[0] + ((float) globalSize() /2) - 15;
			text.y = coords[1] + ((float) globalSize() /2) + 20;
		}
	}


	public void touchActivate(){
		if (isBeingPressed())
			keybindActivate();
	}

	public void keybindActivate(){
		if (!isItActive) {
			if (cooldownCounter >= cooldown) {
				isItActive = true;
				active();
				text(name+" activated!",chara.getX() + chara.getBase() - globalSize() * 2 ,chara.getY() + chara.getHeight() + globalSize() * 3/4f,120, TextureManager.Fonts.ComicSans,32,40,200,40,1,30);
			}
			else if (cooldown - cooldownCounter > 1)
				text("Couldn't activate " + name + "! You still have to wait " + (cooldown - cooldownCounter) + " more turns!"
						,chara.getX() + chara.getBase() - globalSize() * 5 ,chara.getY() + globalSize() * 3/4f + chara.getHeight(),120, TextureManager.Fonts.ComicSans,32,256,0,0,1,30);
			else
				text("Couldn't activate " + name + "! You still have to wait one more turn!"
						,chara.getX() + chara.getBase() - globalSize() * 5 ,chara.getY() + chara.getHeight() + globalSize() * 3/4f,120, TextureManager.Fonts.ComicSans,32,256,0,0,1,30);
		} else {
			cancelActivation();
			text(name+ " deactivated!",chara.getX() + chara.getBase() - globalSize() * 2 ,chara.getY() + chara.getHeight() + globalSize() * 3/4f ,120, TextureManager.Fonts.ComicSans,32,200,200,40,1,30);
		}
	}

	//Override this
	public void active(){

	}

	public void cancelActivation(){
		isItActive = false;
	}

	public void finished(){
		cooldownCounter = 0;
		isItActive = false;
	}


	public void updateCooldown(){
		if(cooldownCounter < cooldown)
			cooldownCounter++;
	}


}
