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

	public Melee() {
		super(name, health, damage, speed, attackSpeed, defense, range, tempDefense, rainbowDefense, mana, magicDefense,
				magicDamage, manaPerTurn, manaPerUse, magicHealing,aggro);
		abilities = new ArrayList<>();
		abilities.add(new Ability("flurryofhits", "Flurry Of Attacks", false, 4,
				0.9f, 0.9f, (float) globalSize() /2){
			@Override
			public void active() {
				character.attackMode = true;
				cancelAbilities();
				isItActive = true;

			}
		});
		abilities.add(new Ability("oneforall", "One For All", false, 4,
				-0.9f, -0.9f, (float) globalSize() /2){
			@Override
			public void active() {
				character.attackMode = true;
				cancelAbilities();
				isItActive = true;
			}
		});
	}


	public void updateOverridable() {
		for (Ability a : abilities){
			a.render();
			a.touchActivate();
		}
		if (!character.attackMode && (abilities.get(0).isItActive || abilities.get(1).isItActive))
			for (Ability a : abilities)
				a.cancelActivation();
		if (Gdx.input.isKeyJustPressed(Input.Keys.I)){
			System.out.println("ability recharge is at: "+ abilities.get(0).cooldownCounter);
			System.out.println("attackState is: "+ attackState);
			System.out.println("FoA is: " + abilities.get(0).isItActive);
			System.out.println("OfA is: " + abilities.get(1).isItActive);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F))
			abilities.get(0).keybindActivate();
		if (Gdx.input.isKeyJustPressed(Input.Keys.O))
			abilities.get(1).keybindActivate();
	}

	public void cancelAbilities(){
		for (Ability a : abilities)
			a.cancelActivation();

	}

	public void finishAbilities(){
		for (Ability a : abilities)
			a.finished();
	}

	@Override
	public boolean onAttackDecided() {
		if (abilities.get(0).isItActive){
			attackState++;
			character.canDecide = new boolean[]{true, true};
			if (attackState < FoANumberOfExtraHits)
				return true;
			finishAbilities();
		}
		return false;
	}

	public float outgoingDamage(){
		if (abilities.get(1).isItActive) {
			print("OFA was registered correctly");
			finishAbilities();
			return totalDamage * OfAMultiplier;
		}
		return totalDamage;
	}



}