package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.InputHandler.actionConfirmJustPressed;
import static com.mygdx.game.items.InputHandler.actionResetJustPressed;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.abs;

public class StellarExplosion extends CharacterClasses {

	TargetProcessor targetProcessor;
	public boolean explode  = false;
	public float explosionRange = 3;
	public boolean decidingExplode = false;

	public StellarExplosion() {
		super();
		name = "StellarExplosion";
		health = 15;
		damage = 15;
		speed = 6;
		attackSpeed = 6;
		defense = 0;
		range = 2;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 250;
		magicDefense = 0;
		magicDamage = 125;
		manaPerTurn = 50;
		manaPerUse = 300;
		magicHealing = 0;
		aggro = 1;

		abilities.add(new Ability("Implosion", "Implosion", 10, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				character.attackMode = false;
				isItActive = true;
				character.actionDecided();
				decidingExplode = false;
				character.movementLock = false;
				character.path.pathReset();
			}
		});

		text = dinamicFixatedText(manaPool+"",100,400,-1, TextureManager.Fonts.ComicSans,30);
		text.setColor(new int[]{157,216,242});
		reset();
		currentHealth = totalHealth;
		manaPool = totalMana;
		damageReason = AttackTextProcessor.DamageReasons.MELEE;
		targetProcessor = new TargetProcessor(character,explosionRange,false,false){
			@Override
			public void circleOverridable(Vector3 click) {
				if ((Gdx.input.justTouched() && targetProcessor.isInsideCircle(click.x, click.y)) || actionConfirmJustPressed()) {
					explode = true;
					character.actionDecided();
					decidingExplode = false;
					character.movementLock = false;
				}
				else if (actionResetJustPressed()){
					targetProcessor.reset();
					explode = false;
					decidingExplode = false;
					character.movementLock = false;
				}
			}
		};
	}

	public void resetClassesState() {
		targetProcessor.reset();
		abilities.get(0).cancelActivation();
		explode = false;
		decidingExplode = false;
	}

	TextureManager.Text text;
	public void updateOverridable() {
		text.text = manaPool+"";
		targetProcessor.changeRadius(explosionRange);
		abilitiesProcessor();

		if(isDecidingWhatToDo(character) && (character.attackMode || decidingExplode) && manaPool >= totalManaPerUse) {
			if(decidingExplode && character.attackMode){
				targetProcessor.reset();
				explode = false;
				character.cancelAttackMode();
				decidingExplode = false;
				character.movementLock = false;
			} else {
				character.cancelAttackMode();
				character.movementLock = true;
				decidingExplode = true;
				targetProcessor.render();
			}
		}

		if(explode && manaPool >= totalManaPerUse) {
			if (damageReason != AttackTextProcessor.DamageReasons.MAGICAL)
				damageReason = AttackTextProcessor.DamageReasons.MAGICAL;}
		else if (damageReason != AttackTextProcessor.DamageReasons.MELEE)
			damageReason = AttackTextProcessor.DamageReasons.MELEE;

		if(character.permittedToAct && explode && manaPool >= totalManaPerUse){
			manaPool -= totalManaPerUse;
			for(Actor a : actors)
				if(abs(character.dC(a.getX(),a.y)/globalSize()) <= explosionRange + 0.25f && a.team != character.team) {
					a.damage(totalMagicDamage, damageReason, character);
					runAttack();
				}
			explode = false;
			character.spendTurn();
		}

		if(character.permittedToAct && abilities.get(0).isItActive){
			manaPool -= totalManaPerUse * 2;
			if (tempDefense +  totalManaPerUse * 1/3f > totalManaPerUse) {
				if (tempDefense < totalManaPerUse)
					tempDefense = totalManaPerUse;
			} else
				tempDefense += totalManaPerUse * 1/3f;
			abilities.get(0).finished();
			character.spendTurn();
		}

	}


	public void destroyOverridable(){
		text.onScreenTime = 1;
	}


}