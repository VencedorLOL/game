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
	public static float aggro = 1;

	public byte attackState;
	public final byte FoANumberOfExtraHits = 4;
	public byte OfAMultiplier = 6;

	public Melee(Character chara) {
		super(chara,name, health, damage, speed, attackSpeed, defense, range, tempDefense, rainbowDefense, mana, magicDefense,
				magicDamage, manaPerTurn, manaPerUse, magicHealing,aggro);
		abilities = new ArrayList<>();
		abilities.add(new Ability("flurryofhits", "Flurry Of Attacks", false, 4,
				.25f, -.25f, (float) globalSize() /2){
			@Override
			public void active() {
				character.attackMode = true;
			}
		});
		abilities.add(new Ability("oneforall", "One For All", false, 4,
				.40f, -.40f, (float) globalSize() /2));
	}


	public void updateOverridable() {
		for (Ability a : abilities){
			a.runThispls();
			a.touchActivate();
		}
		turnHandler();
		if (!character.canDecide() || !character.attackMode)
			for (Ability a : abilities)
				a.cancelActivation();
		if (Gdx.input.isKeyJustPressed(Input.Keys.I)){
			System.out.println("ability recharge is at: "+ abilities.get(0).cooldownCounter);
			System.out.println("attackState is: "+ attackState);
			System.out.println("FoA is: " + abilities.get(1).isItActive);
			System.out.println("OfA is: " + abilities.get(0).isItActive);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F))
			abilities.get(0).keybindActivate();
		if (Gdx.input.isKeyJustPressed(Input.Keys.O))
			abilities.get(1).keybindActivate();
	}


	public void turnHandler(){
		if (abilities.get(0).isItActive && character.hasAttacked){
			if (attackState >= FoANumberOfExtraHits) {
				character.finalizedAttack();
				abilities.get(0).finished();
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
		if (abilities.get(1).isItActive) {
			abilities.get(1).finished();
			return totalDamage * OfAMultiplier;
		}
		else
			return totalDamage;
	}



}