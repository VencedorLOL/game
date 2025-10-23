package com.mygdx.game.items.characters.equipment;

import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.characters.CharacterClasses;

public class Shields {

	public String shieldName;
	public float shieldHealth;
	public float shieldDamage;
	public byte shieldSpeed;
	public byte shieldAttackSpeed;
	public float shieldDefense;
	public float shieldRainbowDefense;
	public float shieldMagicDefense;
	public int shieldRange;
	public float shieldMana;
	public float shieldManaPerTurn;
	public float shieldManaPerUse;
	public float shieldMagicDamage;
	public float shieldMagicHealing;
	public String equippableBy;
	public float aggro;

	public CharacterClasses holder;

	public Shields(CharacterClasses holder) {
		this.holder = holder;
	}

	public void update() {
		// Overridable method. Runs every tick. For shield-specific abilities.
	}

	public void onHurt(AttackTextProcessor.DamageReasons source){}

	public void onAttack(){}

	public void onMove(){}

	public boolean onAttackDecided(){return true;}

	public void turnHasPassed(){}

	public void onKill() {}


	public static class NoShield extends Shields {
		public NoShield(CharacterClasses holder) {
			super(holder);
			shieldName = "NoShield";
			shieldHealth = 0;
			shieldDamage = 0;
			shieldSpeed = 0;
			shieldAttackSpeed = 0;
			shieldDefense = 0;
			shieldRange = 0;
			shieldRainbowDefense = 0;
			shieldMana = 0;
			shieldMagicDefense = 0;
			shieldMagicDamage = 0;
			shieldManaPerTurn = 0;
			shieldManaPerUse = 0;
			shieldMagicHealing = 0;
			aggro = 0;
		}
	}


}
