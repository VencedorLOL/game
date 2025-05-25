package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.CharacterClasses;

public class Tank extends CharacterClasses {
	public static String name = "Tank";
	public static float health = 80;
	public static float damage = 5;
	public static byte speed = 2;
	public static byte attackSpeed = 3;
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
	public static float aggro;

	public Tank(Character chara){
		super(chara,name,health,damage,speed,attackSpeed,defense,range,tempDefense,rainbowDefense,mana,magicDefense,
				magicDamage,manaPerTurn,manaPerUse,magicHealing, aggro);
	}


	@Override
	public void updateOverridable() {
		// Tank's only ability: Your teammates only suffer 20% of the damage, but you suffer it instead
		// Multiplayer's not a thing yet, soooo...
	}
}

