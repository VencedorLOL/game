package com.mygdx.game.items.characters.classes;

import static com.mygdx.game.Settings.touchDetect;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.TextureManager.addToFixatedList;
import static java.lang.Float.POSITIVE_INFINITY;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.Entity;
import com.mygdx.game.items.characters.CharacterClasses;

import java.util.ArrayList;

public class Vencedor extends CharacterClasses {
	public static String name = "Vencedor";
	public static float health = 40;
	public static float damage = 5;
	public static byte speed = 30;
	public static byte attackSpeed = 8;
	public static float defense = 0;
	public static int range = 7;
	public static float tempDefense = 0;
	public static float rainbowDefense = 0;
	public static float mana = 0;
	public static float magicDefense = 0;
	public static float magicDamage = 0;
	public static float manaPerTurn = 0;
	public static float manaPerUse = 0;
	public static float magicHealing = 0;
	public static float aggro;

	public boolean attackMode = false;
	public boolean controlVenceSwordMode = false;
	public ArrayList<VenceSword> venceSwordArray = new ArrayList<>();

	public Vencedor() {
		super(name, health, damage, speed, attackSpeed, defense, range, tempDefense, rainbowDefense, mana, magicDefense,
				magicDamage, manaPerTurn, manaPerUse, magicHealing,aggro);
	}

	public void updateOverridable(){
		VenceSword();
		for (VenceSword v : venceSwordArray){
			v.render();
		}
		refresh();
	}


	public void VenceSword(){
		if (character.canDecide() && Gdx.input.isKeyJustPressed(Input.Keys.E) && !attackMode){
			attackMode = true;
			return;
		}
		if (character.canDecide() && Gdx.input.isKeyJustPressed(Input.Keys.E) && attackMode){
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
