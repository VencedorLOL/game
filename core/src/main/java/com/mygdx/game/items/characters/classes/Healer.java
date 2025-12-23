package com.mygdx.game.items.characters.classes;

import com.mygdx.game.Utils;
import com.mygdx.game.items.Actor;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.TargetProcessor;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.weapons.HealerWeapons;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actorInPos;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TurnManager.isDecidingWhatToDo;


public class Healer extends CharacterClasses {

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


		currentHealth = totalHealth;
		manaPool = mana;
		healTarget = character;

		abilities.add(new Ability("healDirection", "Heal Target", -1, 60	,80, (float) globalSize() /2){
			@Override
			public void active() {
				isItActive = true;
				healRange = 40;
				character.cancelAttackMode();

				targetProcessor.reset();
				character.movementLock = true;
				character.path.pathReset();
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.movementLock = false;
				targetProcessor.reset();
				getCamara().smoothZoom(1,30);
				character.path.pathReset();
			}

			public void finished() {
				cooldownCounter = 0;
				isItActive = false;
				character.movementLock = false;
				targetProcessor.reset();
				getCamara().smoothZoom(1,30);
				character.path.pathReset();
			}
		});

		oVS = new OnVariousScenarios(){
			public void onStageChange() {
				healTarget = character;
			}
		};
		getEquipment();
		reset();
		targetProcessor = new TargetProcessor(character,healRange,true,false,"healTarget","AnimahealTarget");
	}

	@Override
	public void resetClassesState() {
		targetProcessor.reset();
		abilities.get(0).cancelActivation();
	}

	protected void updateOverridable() {
		if(healTarget == null && healTarget.getIsDead())
			healTarget = character;

		abilitiesProcessor();

		if(character.attackMode || escapeReleased())
			abilities.get(0).cancelActivation();

		if(abilities.get(0).isItActive && isDecidingWhatToDo(character)){
			healDirectionInput();
		}

	}

	void healDirectionInput(){
		targetProcessor.changeRadius(healRange);
		targetProcessor.render();
		if((actionConfirmJustPressed() || leftClickReleased()) && targetProcessor.renderingTarget) {
			if (targetProcessor.findATile(targetProcessor.getTargetX(), targetProcessor.getTargetY()) != null) {
				getCamara().smoothZoom(1,30);
				Actor target;
				if ((target = actorInPos(targetProcessor.getTargetX(),targetProcessor.getTargetY())) != null)
						healTarget = target;
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

