package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.items.OnVariousScenarios.destroyListener;

public class Mage extends CharacterClasses {
	public static String name = "Mage";
	public static float health = 30;
	public static float damage = 20;
	public static byte speed = 3;
	public static byte attackSpeed = 4;
	public static float defense = 0;
	public static int range = 50;
	public static float tempDefense = 0;
	public static float rainbowDefense = 0;
	public static float mana = 100;
	public static float magicDefense = 0;
	public static float magicDamage = 20;
	public static float manaPerTurn = 50;
	public static float manaPerUse = 0;
	public static float magicHealing = 0;
	public static float aggro;

	public Mage(){
		super(name,health,damage,speed,attackSpeed,defense,range,tempDefense,rainbowDefense,mana,magicDefense,
				magicDamage,manaPerTurn,manaPerUse,magicHealing, aggro);
	}


	@Override
	public boolean onAttackDecided() {
		refresh();
		if (manaPool - (totalManaPerUse/2) >= 0){
			manaPool -= (totalManaPerUse/2);
			return true;
		} return false;
	}
}

