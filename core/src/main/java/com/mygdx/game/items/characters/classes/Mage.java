package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.TextureManager;
import com.mygdx.game.items.TextureManager.*;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.items.TextureManager.dinamicFixatedText;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.getClIns;

public class Mage extends CharacterClasses {
	public Mage(){
		super();
		name = "Mage";
		health = 30;
		damage = 20;
		speed = 3;
		attackSpeed = 4;
		defense = 0;
		range = 50;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 100;
		magicDefense = 0;
		magicDamage = 20;
		manaPerTurn = 50;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 0;
		if(getClIns("Mage").getWeapon(this) != null)
			equipWeapon(getClIns("Mage").getWeapon(this));
		if(getClIns("Mage").getShield(this) != null)
			equipShield(getClIns("Mage").getShield(this));
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
		text = dinamicFixatedText(manaPool+"",100,400,-1, Fonts.ComicSans,30);
		text.setColor(new int[]{157,216,242});
		damageReason = AttackTextProcessor.DamageReasons.MAGICAL;
	}

	Text text;
	protected void updateOverridable() {
		text.text = manaPool+"";

	}

	@Override
	public boolean onAttackDecided() {
		refresh();
		if (manaPool - (totalManaPerUse/2) >= 0){
			manaPool -= (totalManaPerUse/2);
			return true;
		}
		character.cancelAttackMode();
		text("Out Of Mana!",0,150,200, TextureManager.Fonts.ComicSans,40,character);
		return false;
	}

	public void destroyOverridable(){
		getClIns("Mage").setShield(shield);
		getClIns("Mage").setWeapon(weapon);
		text.onScreenTime = 1;
	}

}

