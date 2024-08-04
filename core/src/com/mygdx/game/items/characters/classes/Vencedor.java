package com.mygdx.game.items.characters.classes;

import static java.lang.Float.POSITIVE_INFINITY;
import com.mygdx.game.items.Entity;
import com.mygdx.game.items.characters.CharacterClasses;

public class Vencedor extends CharacterClasses {
	public static String name = "Vencedor";
	public static float health = 40;
	public static float damage = 5;
	public static byte speed = 4;
	public static byte attackSpeed = 8;
	public static float defense = 0;
	public static int range = 4;
	public static float tempDefense = 0;
	public static float rainbowDefense = 0;
	public static float mana = 0;
	public static float magicDefense = 0;
	public static float magicDamage = 0;
	public static float manaPerTurn = 0;
	public static float manaPerUse = 0;
	public static float magicHealing = 0;

	public Vencedor() {
		super(name, health, damage, speed, attackSpeed, defense, range, tempDefense, rainbowDefense, mana, magicDefense,
				magicDamage, manaPerTurn, manaPerUse, magicHealing);
	}




	public class VenceSword extends Entity {
		float defense = POSITIVE_INFINITY;
		Entity owner;

		public VenceSword(Entity owner){
			this.owner = owner;
		}

	}



}