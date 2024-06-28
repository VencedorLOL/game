package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.CharacterClasses;

public class Melee extends CharacterClasses {
	public static String name = "Melee";
	public static float health = 40;
	public static float damage = 40;
	public static byte speed = 3;
	public static byte attackSpeed = 2;
	public static float defense = 0;
	public static int range = 2;
	public static float tempDefense = 0;
	public static float rainbowDefense = 0;
	public static float mana = 0;
	public static float magicDefense = 0;
	public static float magicDamage = 0;
	public static float manaPerTurn = 0;
	public static float manaPerUse = 0;
	public static float magicHealing = 0;
	public byte abilityCooldown;
	public byte attackState;
	public boolean FoA;
	public byte FoANumberOfExtraHits = 4;
	public boolean HH;
	public byte HHMultiplier = 6;

	public Melee() {
		super(name, health, damage, speed, attackSpeed, defense, range, tempDefense, rainbowDefense, mana, magicDefense,
				magicDamage, manaPerTurn, manaPerUse, magicHealing);
	}

	public void update(Character character) {
		if (turnHasPassed())
			abilityCooldown++;

		if (Gdx.input.isKeyPressed(Input.Keys.I)){
			System.out.println("abilityCD is : "+abilityCooldown);
			System.out.println("attackState is: "+ attackState);
			System.out.println("FoA is: " + FoA);
			System.out.println("HH is: " + HH);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
			activateFlurryOfAttacks();
		if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
			activateHardHitter();
	}

	public void activateFlurryOfAttacks(){
		if (abilityCooldown >= 4){
			abilityCooldown = 0;
			FoA = true;
		}
	}

	public void activateHardHitter(){
		if (abilityCooldown >= 4) {
			abilityCooldown = 0;
			HH = true;
		}
	}

	public float outgoingDamage(){
		if (HH) {
			HH = false;
			return totalDamage * HHMultiplier;
		}
		else
			return totalDamage;
	}

}