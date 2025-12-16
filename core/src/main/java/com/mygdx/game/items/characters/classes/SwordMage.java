package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.Conditions;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.TextureManager;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.shields.SwordMageShields;
import com.mygdx.game.items.characters.equipment.weapons.SwordMageWeapons;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.dinamicFixatedText;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.getClIns;

public class SwordMage extends CharacterClasses {

	public float manaCost = 1.5f;
	public float damageMultiplier = 1.25f;
	public float finalManaCost;
	public float finalDamageMultiplier;
	public OnVariousScenarios oVSce;

	public SwordMage() {
		super();
		name = "SwordMage";
		health = 30;
		damage = 20;
		speed = 3;
		attackSpeed = 6;
		defense = 0;
		range = 2;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 100;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 50;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;

		abilities.add(new Ability("manahit", "Magically Enhanced Attack", -1, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
		//		character.attackMode = true;
				isItActive = true;
				character.conditions.condition(Conditions.ConditionNames.MANA_HIT);
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.targetProcessor.reset();
				character.conditions.remove(Conditions.ConditionNames.MANA_HIT);
			}
		});
		@SuppressWarnings("all")
		OnVariousScenarios oVSce = new OnVariousScenarios(){
			@Override
			public void onTurnPass() {
				refresh();
				costAndDamage();
				if(totalDamage * finalDamageMultiplier * finalManaCost > manaPool)
					text("Warning! Right now you don't have enough mana!",-400,150,200, TextureManager.Fonts.ComicSans,40,character);
			}
		};

		text = dinamicFixatedText(manaPool+"",100,400,-1, TextureManager.Fonts.ComicSans,30);
		text.setColor(new int[]{157,216,242});
		if(getClIns("SwordMage").getWeapon() != null)
			equipWeapon(getClIns("SwordMage").getWeapon());
		if(getClIns("SwordMage").getShield() != null)
			equipShield(getClIns("SwordMage").getShield());
		reset();
		currentHealth = totalHealth;
		manaPool = totalMana;
	}

	TextureManager.Text text;
	public void updateOverridable() {
		costAndDamage();
		text.text = manaPool+"";
		abilitiesProcessor();

		if(totalDamage * finalDamageMultiplier * finalManaCost <= manaPool && abilities.get(0).isItActive) {
			if (damageReason != AttackTextProcessor.DamageReasons.MAGICAL)
				damageReason = AttackTextProcessor.DamageReasons.MAGICAL;}
		else if (damageReason != AttackTextProcessor.DamageReasons.MELEE)
			damageReason = AttackTextProcessor.DamageReasons.MELEE;
	}


	public void costAndDamage(){
		if(weapon instanceof SwordMageWeapons && shield instanceof SwordMageShields) {
			finalDamageMultiplier = ((SwordMageWeapons) weapon).damageMultiplier + damageMultiplier + ((SwordMageShields) shield).damageMultiplier;
			finalManaCost         = ((SwordMageWeapons) weapon).manaCost + manaCost + ((SwordMageShields) shield).manaCost;
		}
		else if(shield instanceof SwordMageShields) {
			finalDamageMultiplier = ((SwordMageShields) shield).damageMultiplier + damageMultiplier;
			finalManaCost         = ((SwordMageShields) shield).manaCost + manaCost;
		}
		else if (weapon instanceof SwordMageWeapons){
			finalDamageMultiplier = ((SwordMageWeapons) weapon).damageMultiplier + damageMultiplier;
			finalManaCost         = ((SwordMageWeapons) weapon).manaCost + manaCost;
		}
	}



	public void destroyOverridable(){
		getClIns("SwordMage").setShield(shield);
		getClIns("SwordMage").setWeapon(weapon);
		destroyListener(oVSce);
		text.onScreenTime = 1;
		character.conditions.remove(Conditions.ConditionNames.MANA_HIT);
	}


}