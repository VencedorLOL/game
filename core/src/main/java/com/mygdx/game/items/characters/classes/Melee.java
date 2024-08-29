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
	public final byte FoANumberOfExtraHits = 4;
	public boolean OfA;
	public byte OfAMultiplier = 6;

	public Melee() {
		super(name, health, damage, speed, attackSpeed, defense, range, tempDefense, rainbowDefense, mana, magicDefense,
				magicDamage, manaPerTurn, manaPerUse, magicHealing,true);
	}

	public void updateOverridable(Character character) {
		if (turnHasPassed())
			abilityCooldown++;
		turnHandler(character);
		if (Gdx.input.isKeyPressed(Input.Keys.I)){
			System.out.println("abilityCD is : "+abilityCooldown);
			System.out.println("attackState is: "+ attackState);
			System.out.println("FoA is: " + FoA);
			System.out.println("OfA is: " + OfA);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F) && Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
			System.out.println(activateFlurryOfAttacks());
		if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
			System.out.println(activateOneForAll());
	}

	public String activateFlurryOfAttacks(){
		if (abilityCooldown >= 4){
			abilityCooldown = 0;
			FoA = true;
			return "Flurry Of Attacks activated";
		}
		return "Couldn't activate Flurry Of Attacks. You still have to wait " +(4 - abilityCooldown)+" turns";
	}

	public String activateOneForAll(){
		if (abilityCooldown >= 4) {
			abilityCooldown = 0;
			OfA = true;
			return "One For All activated";
		}
		return "Couldn't activate One For All. You still have to wait " +(4 - abilityCooldown)+" turns";
	}

	public void turnHandler(Character character){
		if (FoA){
			if (attackState >= FoANumberOfExtraHits) {
				character.finalizedAttack();
				FoA = false;
				attackState = 0;
			} else
				attackState++;
		}
		else {
			if (character.hasAttacked){
				character.spendTurn();
				character.hasAttacked = false;
			}

		}
	}






	public float outgoingDamage(){
		if (OfA) {
			OfA = false;
			return totalDamage * OfAMultiplier;
		}
		else
			return totalDamage;
	}

}