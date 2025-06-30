package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.CharacterClasses;

public class Classless extends CharacterClasses {
	public static String name = "Classless";
	public static float health = 40;
	public static float damage = 5;
	public static byte speed = 2;
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
	public static float aggro = 0;

	public Classless(){
		super(name,health,damage,speed,attackSpeed,defense,range,tempDefense,rainbowDefense,mana,magicDefense,
				magicDamage,manaPerTurn,manaPerUse,magicHealing,aggro);
	}

}

