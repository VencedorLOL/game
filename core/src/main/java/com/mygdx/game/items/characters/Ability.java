package com.mygdx.game.items.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.Camara;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.ClickDetector.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Ability{
	public String textureIcon;
	public String name;
	public boolean isItActive;
	public int cooldown;
	public int cooldownCounter = 0;
	public float x, y;
	public float radius;
	Circle circle = new Circle();


	public Ability(String texture, String name, int cooldown,float x, float y,float radius){
		textureIcon = texture;
		this.name = name;
		this.cooldown = cooldown + 1;
		this.radius = radius;
		this.x = x;
		this.y = y;
		circle.set(Gdx.graphics.getWidth() * this.x + Camara.getX(),
				Gdx.graphics.getHeight() * this.y + Camara.getY(),radius);
	}

	public boolean isBeingPressed(){
		if (touchDetect()){
			// https://imgur.com/a/bgzC4LK
			Vector3 vector = authenticClick();
			float[] coords = Camara.unproject(Gdx.graphics.getWidth() * x / 100,Gdx.graphics.getHeight() * y  / 100);
			circle.setPosition(coords[0] + globalSize()/2 ,coords[1] + globalSize()/2);
			return sqrt(pow(circle.x - vector.x, 2) + pow(circle.y - vector.y, 2)) <= circle.radius;
		}
		return false;
	}


	public void render(){
		TextureManager.addToFixatedList(textureIcon,Gdx.graphics.getWidth() * x / 100,Gdx.graphics.getHeight() * y  / 100,1,0);
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
		} else {
			cancelActivation();
			print(name+ " deactivated!");
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
		cooldownCounter++;
	}


}
