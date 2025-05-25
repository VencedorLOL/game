package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;

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

	public byte attackState;
	public boolean FoA;
	public final byte FoANumberOfExtraHits = 4;
	public boolean OfA;
	public byte OfAMultiplier = 6;

	public Melee() {
		super(name, health, damage, speed, attackSpeed, defense, range, tempDefense, rainbowDefense, mana, magicDefense,
				magicDamage, manaPerTurn, manaPerUse, magicHealing,true);
		abilityButton = new ArrayList<>();
		abilityButton.add(new Ability("flurryofhits", "FlurryOfAttacks", false, 4,
				.25f, -.25f, (float) globalSize() /2));
		abilityButton.add(new Ability("oneforall", "OneForAll", false, 4,
				.40f, -.40f, (float) globalSize() /2));
	}


	public void updateOverridable(Character character) {
		if (abilityButton.get(0).runThispls())
			System.out.println(activateFlurryOfAttacks(character));
		if (abilityButton.get(1).runThispls())
			System.out.println(activateOneForAll(character));
		turnHandler(character);
		if (Gdx.input.isKeyJustPressed(Input.Keys.I)){
			System.out.println("abilityCD is : "+defaultCooldown);
			System.out.println("attackState is: "+ attackState);
			System.out.println("FoA is: " + FoA);
			System.out.println("OfA is: " + OfA);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F))
			System.out.println(activateFlurryOfAttacks(character));
		if (Gdx.input.isKeyJustPressed(Input.Keys.O))
			System.out.println(activateOneForAll(character));
	}

	// trad: uno para todos
	public String activateFlurryOfAttacks(Character character){
		if (!FoA){
			if (defaultCooldown >= 4){
				defaultCooldown = 0;
				FoA = true;
				character.attackMode = true;
				return "Flurry Of Attacks activated";
			}
			return "Couldn't activate Flurry Of Attacks. You still have to wait " +(4 - defaultCooldown)+" turns";
		}
		return "You can't activate this ability, silly! It's already active!";
	}

	// trad: todos para una
	public String activateOneForAll(Character character){
		if (!OfA) {
			if (defaultCooldown >= 4) {
				defaultCooldown = 0;
				OfA = true;
				character.attackMode = true;
				return "One For All activated";
			}
			return "Couldn't activate One For All. You still have to wait " + (4 - defaultCooldown) + " turns";
		}
		return "You can't activate this ability, silly! It's already active!";
	}

	public void turnHandler(Character character){
		if (FoA && character.hasAttacked){
			if (attackState >= FoANumberOfExtraHits) {
				character.finalizedAttack();
				FoA = false;
				attackState = 0;
				character.hasAttacked = false;
				print("FoA Is false");
			} else {
				attackState++;
				character.hasAttacked = false;
				print("attackState's state was modified. It is: " + attackState);
			}
		}
		else {
			if (character.hasAttacked){
				character.finalizedAttack();
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