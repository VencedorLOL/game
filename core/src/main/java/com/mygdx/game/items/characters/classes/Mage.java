package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.TextureManager;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.fixatedText;
import static com.mygdx.game.items.TextureManager.text;

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
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
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
}

