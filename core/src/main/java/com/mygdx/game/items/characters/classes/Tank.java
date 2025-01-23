package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.CharacterClasses;

public class Tank extends CharacterClasses {
	public static String name = "Tank";
	public static float health = 80;
	public static float damage = 5;
	//TODO Put back to 2 actingSpeed
	// 10 is for testing
	public static byte speed = 10;
	public static byte attackSpeed = 3;
	public static float defense = 0;
	public static int range = 3;
	public static float tempDefense = 0;
	public static float rainbowDefense = 0;
	public static float mana = 0;
	public static float magicDefense = 0;
	public static float magicDamage = 0;
	public static float manaPerTurn = 0;
	public static float manaPerUse = 0;
	public static float magicHealing = 0;

	public Tank(){
		super(name,health,damage,speed,attackSpeed,defense,range,tempDefense,rainbowDefense,mana,magicDefense,
				magicDamage,manaPerTurn,manaPerUse,magicHealing);
	}


	@Override
	public void updateOverridable(Character character) {
		// Tank's only ability: Your teammates only suffer 20% of the damage, but you suffer it instead
		// Multiplayer's not a thing yet, soooo...
	}
}

