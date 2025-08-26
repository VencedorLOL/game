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

	public byte attackState;
	public final byte FoANumberOfExtraHits = 4;
	public byte OfAMultiplier = 6;

	public Melee() {
		super();
		name = "Melee";
		health = 40;
		damage = 40;
		speed = 3;
		attackSpeed = 2;
		defense = 0;
		range = 2;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 0;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 0;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;

		abilities.add(new Ability("flurryofhits", "Flurry Of Attacks", 4,
				0.9f, 0.9f, (float) globalSize() /2){
			@Override
			public void active() {
				character.attackMode = true;
				cancelAbilities();
				isItActive = true;

			}
		});
		abilities.add(new Ability("oneforall", "One For All", 4,
				-0.9f, -0.9f, (float) globalSize() /2){
			@Override
			public void active() {
				character.attackMode = true;
				cancelAbilities();
				isItActive = true;
			}
		});
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
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
			print("ability recharge is at: "+ abilities.get(0).cooldownCounter);
			print("attackState is: "+ attackState);
			print("FoA is: " + abilities.get(0).isItActive);
			print("OfA is: " + abilities.get(1).isItActive);
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
		attackState = 0;
	}

	@Override
	public boolean onAttackDecided() {
		if (abilities.get(0).isItActive){
			attackState++;
			character.canDecide = new boolean[]{true, true};
			print("FOA was registered correctly. AttackState is " + attackState);
			if (attackState < FoANumberOfExtraHits)
				return true;

			finishAbilities();
		}
		return false;
	}

	public float outgoingDamageOverridable(){
		if (abilities.get(1).isItActive) {
			print("OFA was registered correctly");
			finishAbilities();
			return totalDamage * OfAMultiplier;
		}
		return totalDamage;
	}



}