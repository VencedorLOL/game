package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.CharacterClasses;

public class Classless extends CharacterClasses {


	public Classless(){
		super();
		name = "Classless";
		health = 40;
		damage = 5;
		speed = 2;
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
		aggro = 0;
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
	}

}

