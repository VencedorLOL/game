package com.mygdx.game.items.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.Camara;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Settings.print;
import static com.mygdx.game.Settings.touchDetect;
import static com.mygdx.game.items.ClickDetector.*;
import static java.lang.Math.pow;

public class Ability{
	public String textureIcon;
	public String name;
	public boolean isItActive;
	public int cooldown;
	public int cooldownCounter = 0;
	public float xOffset,yOffset;
	public float radius;
	Circle circle = new Circle();


	public Ability(String texture, String name, boolean isItActive, int cooldown,float x, float y,float radius){
		textureIcon = texture;
		this.name = name;
		this.isItActive = isItActive;
		this.cooldown = cooldown + 1;
		this.radius = radius;
		this.xOffset = x;
		this.yOffset = y;
		circle.set(Gdx.graphics.getWidth() * xOffset + Camara.getX(),
				Gdx.graphics.getHeight() * yOffset + Camara.getY(),radius);
	}

	public boolean isBeingPressed(){
		if (touchDetect()){
			// https://imgur.com/a/bgzC4LK
			Vector3 vector = authenticClick();
			return pow(circle.x - vector.x, 2) + pow(circle.y - vector.y, 2) <= pow(circle.radius, 2);
		}
		return false;
	}


	public void render(){
		circle.setPosition(xOffset * Camara.getBase() + Camara.getX(),Camara.getY() + yOffset * Camara.getHeight());
		TextureManager.addToFixatedList(textureIcon,xOffset,yOffset,-radius,-radius);
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
				print(name+" activated!");
			}
			else if (cooldown - cooldownCounter > 1)
				print("Couldn't activate " + name + "! You still have to wait " + (cooldown - cooldownCounter) + " more turns!");
			else
				print("Couldn't activate " + name + "! You still have to wait one more turn!");
		} else
			print("You can't activate this ability, as it is already active!");
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
		cooldownCounter++;
	}


}
