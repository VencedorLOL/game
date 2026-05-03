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

public class Hero extends CharacterClasses {


	public boolean attackMode = false;
	public boolean controlHeroSwordMode = false;
	public ArrayList<HeroSword> heroSwordArray = new ArrayList<>();

	public Hero() {
		super();
		name = "Her";
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
		HeroSword();
		for (HeroSword v : heroSwordArray){
			v.render();
		}
		refresh();
	}


	public void HeroSword(){
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
				for (HeroSword h : heroSwordArray){
					if (x == h.getX() && y == h.getY()) {
						controlHeroSwordMode = !controlHeroSwordMode;
						h.isBeingControlled = !h.isBeingControlled;
						if(h.isBeingControlled)
							System.out.println("Sword at x " + x + " y " + y + " is being controlled." );
						else
							System.out.println("Sword at x " + x + " y " + y + " is no longer being controlled." );
					}
					if (controlHeroSwordMode && h.isBeingControlled && h.getX() != x && h.getY() != y){
						h.setX(x);
						h.setY(y);
						controlHeroSwordMode = false;
						h.isBeingControlled = false;
					}

				}

				if (heroSwordArray.size() <= 6)
					heroSwordArray.add(new HeroSword(x, y));
				else
					System.out.println("Outta swords");
			}
		}
	}






	public static class HeroSword extends Entity {
		final float defense = POSITIVE_INFINITY;
		boolean isBeingControlled = false;


		public HeroSword(float x, float y){
			super("HeroSword", x, y,128,128);

		}




	}



}
