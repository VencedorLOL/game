package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Utils;
import com.mygdx.game.items.Actor;
import com.mygdx.game.items.Camara;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.TargetProcessor;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.weapons.HealerWeapons;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.InputHandler.actionConfirmJustPressed;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;


public class Healer extends CharacterClasses implements Utils {

	public float healingFromAbility = 1.2f;
	public Actor healTarget;
	public float healRange;
	public TargetProcessor targetProcessor;
	OnVariousScenarios oVS;

	public Healer(){
		super();
		name = "Healer";
		health = 40;
		damage = 5;
		speed = 2;
		attackSpeed = 3;
		defense = 0;
		range = 2;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 0;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 0;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
		healTarget = character;

		abilities.add(new Ability("healDirection", "Heal Target", -1, 60	,80, (float) globalSize() /2){
			@Override
			public void active() {
				isItActive = true;
				healRange = 40;
				character.cancelAttackMode();
				character.movementLock = true;
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.movementLock = false;
				getCamara().smoothZoom(1,30);
			}

			public void finished() {
				cooldownCounter = 0;
				isItActive = false;
				character.movementLock = false;
			}
		});

		oVS = new OnVariousScenarios(){
			public void onStageChange() {
				healTarget = character;
			}
		};
		targetProcessor = new TargetProcessor(character,healRange,true,false,"healtarget");
	}

	@Override
	public void resetClassesState() {
		targetProcessor.reset();
		abilities.get(0).cancelActivation();
	}

	protected void updateOverridable() {
		if(healTarget == null && healTarget.getIsDead())
			healTarget = character;

		abilities.get(0).render();
		if(isDecidingWhatToDo(character))
			abilities.get(0).touchActivate();
		if (Gdx.input.isKeyJustPressed(Input.Keys.E) && (isDecidingWhatToDo(character)))
			abilities.get(0).keybindActivate();

		if(character.attackMode)
			abilities.get(0).cancelActivation();

		if(abilities.get(0).isItActive && isDecidingWhatToDo(character)){
			healDirectionInput();
		}

	}

	void healDirectionInput(){
		targetProcessor.changeRadius(healRange);
		targetProcessor.render();
		if(Gdx.input.justTouched()) {
			getCamara().smoothZoom(1,30);
			Vector3 temporal = roundedClick();
			if (targetProcessor.findATile(temporal.x,temporal.y) != null) {
				for(Actor a : actors){
					if(a.x == temporal.x && a.y == temporal.y && a.team == character.team)
						healTarget = a;
				}
				abilities.get(0).finished();
			}
		}
		if(actionConfirmJustPressed()) {
			getCamara().smoothZoom(1,30);
			if (targetProcessor.findATile(targetProcessor.getTargetX(), targetProcessor.getTargetY()) != null && !(targetProcessor.getTargetY() == character.getX() && targetProcessor.getTargetY() == character.getY())) {
				for(Actor a : actors){
					if(a.x == targetProcessor.getTargetX() && a.y == targetProcessor.getTargetY() && a.team == character.team)
						healTarget = a;
				}
				abilities.get(0).finished();
			}
		}
	}

	public float outgoingDamageOverridable(){
		float damage = totalDamage / 2;
		healTarget.healThis(damage * healingFromAbility * Utils.pickValueAUnlessEqualsZeroThenPickB(weaponHealingAbilityBonus(),1));
		return damage;
	}


	public float weaponHealingAbilityBonus(){
		if (weapon instanceof HealerWeapons)
			return ((HealerWeapons) weapon).weaponHealingAbilityBonus;
		return 0;
	}



	protected void destroyOverridable() {
		destroyListener(oVS);
	}


}

