package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.Actor;
import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.TargetProcessor;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.InputHandler.actionConfirmJustPressed;
import static com.mygdx.game.items.InputHandler.actionResetJustPressed;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.getClIns;
import static java.lang.Math.abs;

public class Earthquaker extends CharacterClasses {

	TargetProcessor targetProcessor;
	public TargetProcessor earthquakeProcessor;
	public boolean earthquake  = false;
	public float earthquakeSelectionRange = 1;
	public float earthquakeRadius = 7;
	public boolean decidingEarthquake = false;
	public boolean playingAnimation = false;

	public Earthquaker() {
		super();
		name = "Earthquaker";
		health = 40;
		damage = 20;
		speed = 2;
		attackSpeed = 1;
		defense = 0;
		range = 2;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 150;
		magicDefense = 0;
		magicDamage = 35;
		manaPerTurn = 15;
		manaPerUse = 55;
		magicHealing = 0;
		aggro = 1;

		if(getClIns("Earthquaker").getWeapon(this) != null)
			equipWeapon(getClIns("Earthquaker").getWeapon(this));
		if(getClIns("Earthquaker").getShield(this) != null)
			equipShield(getClIns("Earthquaker").getShield(this));

		text = dinamicFixatedText(manaPool+"",100,400,-1, Fonts.ComicSans,30);
		text.setColor(new int[]{157,216,242});
		reset();
		currentHealth = totalHealth;
		manaPool = totalMana;
		damageReason = AttackTextProcessor.DamageReasons.MELEE;
		targetProcessor = new TargetProcessor(character,earthquakeSelectionRange,false,false,"earthquaketarget");
		earthquakeProcessor = new TargetProcessor(targetProcessor.targetsTarget,earthquakeRadius,false,false){
			@Override
			public void circleOverridable(Vector3 click) {
				if ((Gdx.input.justTouched() && targetProcessor.isInsideCircle(click.x, click.y)) || actionConfirmJustPressed()) {
					earthquake = true;
					character.actionDecided();
					decidingEarthquake = false;
					character.movementLock = false;
				}
				else if (actionResetJustPressed()){
					targetProcessor.reset();
					earthquake = false;
					decidingEarthquake = false;
					character.movementLock = false;
				}
			}
		};
	}

	public void resetClassesState() {
		targetProcessor.reset();
		decidingEarthquake = false;
		earthquake = false;
	}

	Text text;
	public void updateOverridable() {
		text.text = manaPool+"";
		targetProcessor.changeRadius(earthquakeSelectionRange);
		earthquakeProcessor.changeRadius(earthquakeRadius);
		if(isDecidingWhatToDo(character) && (character.attackMode || decidingEarthquake) && manaPool >= totalManaPerUse) {
			if(decidingEarthquake && character.attackMode){
				targetProcessor.reset();
				earthquakeProcessor.reset();
				earthquake = false;
				character.cancelAttackMode();
				decidingEarthquake = false;
				character.movementLock = false;
			} else {
				character.cancelAttackMode();
				character.movementLock = true;
				decidingEarthquake = true;
				if(targetProcessor.targetsTarget != null && targetProcessor.target != null && targetProcessor.target.render)
					earthquakeProcessor.render();
				targetProcessor.render();
			}
		}

		if(character.permittedToAct && earthquake && manaPool >= totalManaPerUse && !playingAnimation){
			runAttack();
			playingAnimation = true;
			character.lockClass = true;
			for (TargetProcessor.Circle.CircleTile t : earthquakeProcessor.circle.circle) {
				if(!(t.x == earthquakeProcessor.circle.center.x() && t.y == earthquakeProcessor.circle.center.y()))
					animationToList("earthquake",t.x,t.y);
			}
			animations.add(new Animation("earthquake",earthquakeProcessor.circle.center.x(),earthquakeProcessor.circle.center.y()){
				public void onFinish() {
					manaPool -= totalManaPerUse;
					for(Actor a : actors)
						if(abs(targetProcessor.targetsTarget.dC(a.getX(),a.y)/globalSize()) <= earthquakeRadius + 0.5f && a != character) {
							a.damage(totalMagicDamage + a.totalDefense, AttackTextProcessor.DamageReasons.EARTHQUAKE, character);
						}
					earthquake = false;
					character.spendTurn();
					playingAnimation = false;
					character.lockClass = false;
				}
			});
		}

/*		if(decidingEarthquake){
*			if (zoom < maxZoom) {
				finalZoom = maxZoom;
			}
			else
				maxZoom = zoom;
		} else
			maxZoom = 0;

*/	}


	public void destroyOverridable(){
		getClIns("Earthquaker").setShield(shield);
		getClIns("Earthquaker").setWeapon(weapon);
		text.onScreenTime = 1;
	}


}