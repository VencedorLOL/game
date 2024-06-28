package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;
import static com.mygdx.game.items.characters.equipment.Weapons.HealerSwords.*;
import java.lang.reflect.Method;

public class Healer extends CharacterClasses {
	public static String name = "Healer";
	public static float health = 40;
	public static float damage = 5;
	public static byte speed = 2;
	public static byte attackSpeed = 3;
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

	public float healingFromAbility = 4;
	public float weaponHealingAbilityBonus;
	public float shieldHealingPerTurn;

	public boolean hasHealedInThisTurn = false;

	public Healer(){
		super(name,health,damage,speed,attackSpeed,defense,range,tempDefense,rainbowDefense,mana,magicDefense,
				magicDamage,manaPerTurn,manaPerUse,magicHealing);
	}


	public float outgoingDamage(){
		float damage = totalDamage / 2;
		if (weaponHealingAbilityBonus == 0)
			weaponHealingAbilityBonus = 1;
		currentHealth += damage * healingFromAbility * weaponHealingAbilityBonus;
		return damage;
	}

	public float shieldAbilityHealing(CharacterClasses characterClasses)  {
		if(characterClasses.shield instanceof Shields.HealerShield)
			return ((Shields.HealerShield) characterClasses.shield).shieldHealingPerTurn;
		return 0;
	}

	public float weaponHealingAbilityBonus(CharacterClasses characterClasses){
		if (characterClasses.weapon instanceof HealerSwords)
			return ((HealerSwords) characterClasses.weapon).weaponHealingAbilityBonus;
		return 0;
	}

	@Override
	public void update(Character character){
		shieldHealingPerTurn = shieldAbilityHealing(this);
		weaponHealingAbilityBonus = weaponHealingAbilityBonus(this);
		if (character.isOnTurn && !hasHealedInThisTurn){
			currentHealth += shieldHealingPerTurn;
			hasHealedInThisTurn = true;
		}
		if (!character.isOnTurn && hasHealedInThisTurn) {
			hasHealedInThisTurn = false;
		}
		refresh(this);
	}

}

