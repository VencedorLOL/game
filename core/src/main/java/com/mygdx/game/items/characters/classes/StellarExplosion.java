package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.InputHandler.actionConfirmJustPressed;
import static com.mygdx.game.items.InputHandler.actionResetJustPressed;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;

public class StellarExplosion extends CharacterClasses {

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
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.cancelDecision();
			}
		});

		text = dinamicFixatedText(manaPool+"",100,400,-1, TextureManager.Fonts.ComicSans,30);
		text.setColor(new int[]{157,216,242});
		reset();
		currentHealth = totalHealth;
		manaPool = totalMana;
		damageReason = AttackTextProcessor.DamageReasons.MELEE;
	}

	TextureManager.Text text;
	public void updateOverridable() {
		text.text = manaPool+"";
		abilities.get(0).render();
		if(isDecidingWhatToDo(character))
			abilities.get(0).touchActivate();
		if (Gdx.input.isKeyJustPressed(Input.Keys.I) && isDecidingWhatToDo(character))
			abilities.get(0).keybindActivate();

		if(isDecidingWhatToDo(character) && (character.attackMode || decidingExplode) && manaPool >= totalManaPerUse) {
			if(decidingExplode && character.attackMode){
				circle = null;
				explode = false;
				character.cancelAttackMode();
				decidingExplode = false;
			} else {
				character.cancelAttackMode();
				decidingExplode = true;
				circleProcesor();
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
				if(character.dC(a.getX(),a.y)/globalSize() <= explosionRange + 0.25f && a.team != character.team) {
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

	Tile.Circle circle;
	private void circleProcesor(){
		if (circle == null || circle.center != stage.findATile(character.getX(),character.getY()) || circle.tileset != stage.tileset || circle.radius != explosionRange || !circle.walkable) {
			if (circle != null)
				for (Tile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Tile.Circle(stage.findATile(character.getX(), character.getY()), stage.tileset, explosionRange, true,false);

		}
		circle.renderCircle();
		Vector3 temporal = roundedClick();
		if ((Gdx.input.justTouched() && circle.isInsideOfCircle(temporal.x, temporal.y)) || actionConfirmJustPressed()) {
			explode = true;
			character.actionDecided();
			decidingExplode = false;
		}
		else if (actionResetJustPressed()){
			circle = null;
			explode = false;
			decidingExplode = false;
		}
	}

	public void destroyOverridable(){
		text.onScreenTime = 1;
	}


}