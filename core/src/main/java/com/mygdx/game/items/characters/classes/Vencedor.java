package com.mygdx.game.items.characters.classes;

import static com.mygdx.game.Settings.touchDetect;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.TextureManager.addToFixatedList;
import static com.mygdx.game.items.TurnManager.isDecidingWhatToDo;
import static java.lang.Float.POSITIVE_INFINITY;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.items.Entity;
import com.mygdx.game.items.characters.CharacterClasses;

import java.util.ArrayList;

public class Vencedor extends CharacterClasses {


	public boolean attackMode = false;
	public boolean controlVenceSwordMode = false;
	public ArrayList<VenceSword> venceSwordArray = new ArrayList<>();

	public Vencedor() {
		super();
		name = "Vencedor";
		health = 40;
		damage = 5;
		speed = 30;
		attackSpeed = 8;
		defense = 0;
		range = 7;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 0;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 0;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;
	}

	public void updateOverridable(){
		VenceSword();
		for (VenceSword v : venceSwordArray){
			v.render();
		}
		refresh();
	}


	public void VenceSword(){
		if (isDecidingWhatToDo(character) && Gdx.input.isKeyJustPressed(Input.Keys.E) && !attackMode){
			attackMode = true;
			return;
		}
		if (isDecidingWhatToDo(character) && Gdx.input.isKeyJustPressed(Input.Keys.E) && attackMode){
			attackMode = false;
			return;
		}
		if (attackMode){
			addToFixatedList("AttackMode",1F,1f);
			if (touchDetect()) {
				float x = roundedClick().x;
				float y = roundedClick().y;
				for (VenceSword v : venceSwordArray){
					if (x == v.getX() && y == v.getY()) {
						controlVenceSwordMode = !controlVenceSwordMode;
						v.isBeingControlled = !v.isBeingControlled;
						if(v.isBeingControlled)
							System.out.println("Sword at x " + x + " y " + y + " is being controlled." );
						else
							System.out.println("Sword at x " + x + " y " + y + " is no longer being controlled." );
					}
					if (controlVenceSwordMode && v.isBeingControlled && v.getX() != x && v.getY() != y){
						v.setX(x);
						v.setY(y);
						controlVenceSwordMode = false;
						v.isBeingControlled = false;
					}

				}

				if (venceSwordArray.size() <= 6)
					venceSwordArray.add(new VenceSword(x, y));
				else
					System.out.println("Outta swords");
			}
		}
	}






	public static class VenceSword extends Entity {
		final float defense = POSITIVE_INFINITY;
		boolean isBeingControlled = false;


		public VenceSword(float x, float y){
			super("VenceSword", x, y,128,128);

		}




	}



}
