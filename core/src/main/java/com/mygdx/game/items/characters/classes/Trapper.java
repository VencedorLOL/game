package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.*;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.weapons.CatapultAmmo;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.AttackTextProcessor.DamageReasons.*;
import static com.mygdx.game.items.ClickDetector.rayCasting;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.OnVariousScenarios.*;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.TurnManager.*;
import static java.lang.Math.max;

public class Trapper extends CharacterClasses {

	public float chargeRange = 3;
	public float throwRange = 30;
	public float[] rocksCoords = new float[2];
	public float[] chargeCoords = new float[2];
	public boolean isCharged = false;
	public boolean willShoot = false;
	public boolean throwingMode = false;
	OnVariousScenarios oVS;
	OnVariousScenarios oVS2;
	TargetProcessor targetProcessor;
	public TargetProcessor circle2;
	public TargetProcessor circle5;
	public TargetProcessor circle8;

	public Trapper() {
		super();
		name = "Trapper";
		health = 25;
		damage = 5;
		speed = 7;
		attackSpeed = 7;
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

		abilities.add(new Ability("ChargeCatapult", "Charge the Catapult", 0, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				if(!isCharged) {
					isItActive = true;
					cancelRam();
					character.cancelAttackMode();
					character.actionDecided();
					character.movementLock = true;
					character.path.pathReset();
				}

			}

			public void cancelActivation() {
				isItActive = false;
				targetProcessor.reset();
				resetCircle();
				chargeCoords = new float[2];
				character.movementLock = false;
				character.path.pathReset();
			}

			public void finished(){
				cooldownCounter = 0;
				isItActive = false;
				character.movementLock = false;
				character.path.pathReset();
			}

			public void keybindActivate(){
				if(!isCharged) {
					if (!isItActive) {
						if (cooldownCounter >= cooldown) {
							isItActive = true;
							active();
							print("activated cata");
							text(name + " activated!", chara.getX() + chara.getBase() - globalSize() * 2, chara.getY() + chara.getHeight() + globalSize() * 3/4f, 120, 32, 40, 200, 40, 1, 30);
						} else if (cooldown - cooldownCounter > 1)
							text("Couldn't activate " + name + "! You still have to wait " + (cooldown - cooldownCounter) + " more turns!"
									, chara.getX() + chara.getBase() - globalSize() * 5, chara.getY() + globalSize() * 3f/4 + chara.getHeight(), 120, 32, 256, 0, 0, 1, 30);
						else
							text("Couldn't activate " + name + "! You still have to wait one more turn!"
									, chara.getX() + chara.getBase() - globalSize() * 5, chara.getY() + chara.getHeight() + globalSize() * 3f/4, 120,  32, 256, 0, 0, 1, 30);
					} else {
						cancelActivation();
						text(name + " deactivated!", chara.getX() + chara.getBase() - globalSize() * 2, chara.getY() + chara.getHeight() + globalSize() * 3/4f, 120, 32, 200, 200, 40, 1, 30);
					}
				} else text("Cannot charge the catapult as it's already charged!",chara.getX() + chara.getBase() - globalSize() * 2 ,chara.getY() + chara.getHeight() + globalSize() * 3/4f ,120,32,200,200,40,1,30);
			}

		});

		abilities.add(new Ability("ChargeForwardCatapult", "Charge with Catapult", 5, 60	,80, (float) globalSize() /2){
			@Override
			public void active() {
				isItActive = true;
				character.cancelAttackMode();
				character.movementLock = true;
				throwingMode = false;
				character.conditions.condition(Conditions.ConditionNames.COMING_THROUGH);
				character.path.pathReset();
			}

			@Override
			public void cancelActivation() {
				isItActive = false;
				character.movementLock = false;
				targetProcessor.reset();
				resetCircle();
				chargeCoords = new float[2];
				character.conditions.remove(Conditions.ConditionNames.COMING_THROUGH);
				character.path.pathReset();
			}

			@Override
			public void finished() {
				cooldownCounter = 0;
				isItActive = false;
				character.movementLock = false;
				character.conditions.remove(Conditions.ConditionNames.COMING_THROUGH);
				character.path.pathReset();
			}
		});
		targetProcessor = new TargetProcessor(character,chargeRange,true,true,"target","notarget"); targetProcessor.opacity = .2f;
		circle2 = new TargetProcessor(character,1.5f,true,false);circle2.opacity = 0f;
		circle5 = new TargetProcessor(character,4.5f,true,false);circle5.opacity = 0f;
		circle8 = new TargetProcessor(character,7.5f,true,false);circle8.opacity = 0f;
		getEquipment();
		reset();
		currentHealth = totalHealth;
	}


	public void updateOverridable() {
		abilitiesProcessor();


		
	}


	public void cancelRam(){
		abilities.get(1).cancelActivation();
	}


	void chargeInput(){
		targetProcessor.changeRadius(chargeRange);
		targetProcessor.changeCheckWalkable(true);
		targetProcessor.changeRayCast(true);
		targetProcessor.render();
		if(actionConfirmJustPressed() || leftClickReleased()) {
			if (targetProcessor.findATile(targetProcessor.getTargetX(), targetProcessor.getTargetY()) != null && !(targetProcessor.getTargetX() == character.getX() && targetProcessor.getTargetY() == character.getY())) {
				chargeCoords[0] = targetProcessor.getTargetX();
				chargeCoords[1] = targetProcessor.getTargetY();
				character.actionDecided();
			} else if (targetProcessor.getTargetX() == character.getX() && targetProcessor.getTargetY() == character.getY())
				cancelRam();
		}
	}

	protected void renderCircle(){
		circle2.render();
		circle5.render();
		circle8.render();
	}

	protected void resetCircle(){
		circle2.reset();
		circle5.reset();
		circle8.reset();
	}


	protected void rockThrowInput() {
		character.movementLock = true;
		targetProcessor.changeRadius(throwRange);
		targetProcessor.changeCheckWalkable(false);
		targetProcessor.changeRayCast(false);
		renderCircle();
		targetProcessor.render();
		if(actionConfirmJustPressed() || leftClickReleased()) {
			if (targetProcessor.findATile(targetProcessor.getTargetX(), targetProcessor.getTargetY()) != null) {
				rocksCoords[0] = targetProcessor.getTargetX();
				rocksCoords[1] = targetProcessor.getTargetY();
				willShoot = true;
				character.actionDecided();
				character.movementLock = false;
			}
		}
	}


	@Override
	protected void destroyOverridable() {
		destroyListener(oVS);
		character.conditions.remove(Conditions.ConditionNames.COMING_THROUGH);
	}

	public static class Trap extends Hazards {
		public float damage;
		ArrayList<Actor> triggered = new ArrayList<>();

		public Trap(float x, float y){
			super(x,y,globalSize(),globalSize());
			name = "DamagableTrap";
			texture = "Spikes";
		}

		public void update() {
			Actor victim = stepTrigger();
			if(canHazardAct){
				triggered = new ArrayList<>();
				finishedActing();
			}
			if(victim != null && victim.x % globalSize() == 0 && victim.y % globalSize() == 0 && !triggered.contains(victim)) {
				victim.damage(damage, AttackTextProcessor.DamageReasons.PIERCING, null);
				triggered.add(victim);
			}
		}



	}



	public static class DamagableTrap extends Trap implements DamageReceiver {
		public float maxHp;
		public float hp;
		public byte team = 1;
		public AttackTextProcessor.DamageReasons[] immunities;

		public DamagableTrap(float x, float y){
			super(x,y);
			name = "DamagableTrap";
			texture = "Spikes";
			damageReceivers.add(this);
			immunities = new AttackTextProcessor.DamageReasons[]{};
		}


		@Override
		public void destroyHazard() {damageReceivers.remove(this);}

		@Override
		public void damage(float damage, AttackTextProcessor.DamageReasons damageReason, Entity lastDamager) {
			triggerOnDamaged(this,damageReason);
			if(damage > 0 && !DamageReceiver.checkImmunities(damageReason,immunities))
				hp -= damage;
			if(hp < 0){
				destroyHazard();
				queuedForDeletion = true;
			}

		}
		@Override
		public float getTotalHealth(){return maxHp;}
		@Override
		public byte totalTeam(){return team;}

		@Override
		public boolean getIsDead() {
			return hp <= 0;
		}

		@Override
		public float getHealth(){return hp;}


	}













}