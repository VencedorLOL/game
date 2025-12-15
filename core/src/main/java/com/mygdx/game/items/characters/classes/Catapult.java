package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Actor.actorInPos;
import static com.mygdx.game.items.ClickDetector.rayCasting;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.InputHandler.actionConfirmJustPressed;
import static com.mygdx.game.items.InputHandler.leftClickJustPressed;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static com.mygdx.game.items.Turns.turnStopTimer;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.getClIns;

public class Catapult extends CharacterClasses {

	public float chargeRange = 3;
	public float throwRange = 30;
	public ArrayList<Rock> rocks = new ArrayList<>();
	public float[] rocksCoords = new float[2];
	public float[] chargeCoords = new float[2];
	public boolean isCharged = false;
	public boolean willShoot = false;
	public boolean throwingMode = false;
	OnVariousScenarios oVS;
	OnVariousScenarios oVS2;
	TargetProcessor targetProcessor;
	TargetProcessor circle2;
	TargetProcessor circle5;
	TargetProcessor circle8;

	public Catapult() {
		super();
		name = "Catapult";
		health = 30;
		damage = 20;
		speed = 3;
		attackSpeed = 1;
		defense = 0;
		range = 8;
		tempDefense = 0;
		rainbowDefense = 0;
		mana = 0;
		magicDefense = 0;
		magicDamage = 0;
		manaPerTurn = 0;
		manaPerUse = 0;
		magicHealing = 0;
		aggro = 1;

		if(getClIns("Catapult").getWeapon(this) != null)
			equipWeapon(weapon = getClIns("Catapult").getWeapon(this));
		if(getClIns("Catapult").getShield(this) != null)
			equipShield(getClIns("Catapult").getShield(this));

		abilities.add(new Ability("ChargeCatapult", "Charge the Catapult", 0, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				if(!isCharged) {
					print("activated cata 2");
					isItActive = true;
					cancelRam();
					character.cancelAttackMode();
					character.actionDecided();
				}

			}

			public void cancelActivation() {
				isItActive = false;
				targetProcessor.reset();
				resetCircle();
				chargeCoords = new float[2];
			}

			public void keybindActivate(){
				if(!isCharged) {
					if (!isItActive) {
						if (cooldownCounter >= cooldown) {
							isItActive = true;
							active();
							print("activated cata");
							text(name + " activated!", chara.getX() + chara.getBase() - globalSize() * 2, chara.getY() + chara.getHeight() + globalSize() * 3/4f, 120, TextureManager.Fonts.ComicSans, 32, 40, 200, 40, 1, 30);
						} else if (cooldown - cooldownCounter > 1)
							text("Couldn't activate " + name + "! You still have to wait " + (cooldown - cooldownCounter) + " more turns!"
									, chara.getX() + chara.getBase() - globalSize() * 5, chara.getY() + globalSize() * 3f/4 + chara.getHeight(), 120, TextureManager.Fonts.ComicSans, 32, 256, 0, 0, 1, 30);
						else
							text("Couldn't activate " + name + "! You still have to wait one more turn!"
									, chara.getX() + chara.getBase() - globalSize() * 5, chara.getY() + chara.getHeight() + globalSize() * 3f/4, 120, TextureManager.Fonts.ComicSans, 32, 256, 0, 0, 1, 30);
					} else {
						cancelActivation();
						text(name + " deactivated!", chara.getX() + chara.getBase() - globalSize() * 2, chara.getY() + chara.getHeight() + globalSize() * 3/4f, 120, TextureManager.Fonts.ComicSans, 32, 200, 200, 40, 1, 30);
					}
				} else text("Cannot charge the catapult as it's already charged!",chara.getX() + chara.getBase() - globalSize() * 2 ,chara.getY() + chara.getHeight() + globalSize() * 3/4f ,120, TextureManager.Fonts.ComicSans,32,200,200,40,1,30);
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
			}

			@Override
			public void cancelActivation() {
		//		isItActive = false;
				character.movementLock = false;
				targetProcessor.reset();
				resetCircle();
				chargeCoords = new float[2];
				character.conditions.remove(Conditions.ConditionNames.COMING_THROUGH);
			}

			@Override
			public void finished() {
				cooldownCounter = 0;
				isItActive = false;
				character.movementLock = false;

				character.conditions.remove(Conditions.ConditionNames.COMING_THROUGH);
			}
		});

		oVS = new OnVariousScenarios(){
			@Override
			public void onStageChange() {
				rocks.clear();
			}
		};
		if(getClIns("Catapult").getCooldown().length >= abilities.size())
			for(int i = 0; i < abilities.size(); i++)
				abilities.get(i).cooldown = getClIns("Catapult").getCooldown()[i];
		reset();
		currentHealth = totalHealth;
		targetProcessor = new TargetProcessor(character,chargeRange,true,true,"target"); targetProcessor.opacity = .2f;
		circle2 = new TargetProcessor(character,1.5f,true,false);circle2.opacity = 0f;
		circle5 = new TargetProcessor(character,4.5f,true,false);circle5.opacity = 0f;
		circle8 = new TargetProcessor(character,7.5f,true,false);circle8.opacity = 0f;
	}

	public void onFinalizedTurn() {
		for(Rock r : rocks)
			r.advanceRock();
	}

	public void updateOverridable() {
		abilitiesProcessor();
		rocks.removeIf(r -> r.finished && !r.isGliding);
		for(Rock r : rocks)
			r.update();
		if(character.permittedToAct){
			if(willShoot && isCharged){
				throwingMode = false;
				isCharged = false;
				willShoot = false;
				rocks.add(new Rock(character.x,character.y,rocksCoords[0],rocksCoords[1],totalDamage*10));
				rocksCoords = new float[2];
				character.spendTurn();
			}
			if(abilities.get(0).isItActive) {
				isCharged = true;
				abilities.get(0).finished();
				character.spendTurn();
			}
			if(abilities.get(1).isItActive){
				turnStopTimer(60);
				character.glideAbsoluteCoords(chargeCoords[0],chargeCoords[1],60);
				float charX = character.x, charY = character.y;
				oVS2 = new OnVariousScenarios.CounterObject(60){
					@Override
					public void onCounterFinish() {
						ArrayList<Actor> chara = new ArrayList<>();
						chara.add(character);
						ArrayList<Actor> list = rayCasting(charX, charY, chargeCoords[0], chargeCoords[1], (chara), true, character);
						if (list != null)
							for (Actor l : list) {
								l.conditions.status(Conditions.ConditionNames.STUNNED);
								l.conditions.getStatus(Conditions.ConditionNames.STUNNED).setTurns(4);
								l.damage(damage, AttackTextProcessor.DamageReasons.MELEE, character);
							}
						character.softlockOverridable(true);
						destroyListener(oVS2);
					}
				};
				abilities.get(1).finished();
				character.spendTurn();
			}

		}

		if(character.attackMode && isCharged && isDecidingWhatToDo(character)){
			character.cancelAttackMode();
			throwingMode = !throwingMode;
			abilities.get(1).cancelActivation();
		}

		if(throwingMode && isDecidingWhatToDo(character))
			rockThrowInput();
		else if(character.attackMode && isDecidingWhatToDo(character))
			softCancelRam();
		if(abilities.get(1).isItActive && isDecidingWhatToDo(character)) {
			chargeInput();
		}


		
	}

	public void softCancelRam(){
		abilities.get(1).isItActive = false;
		character.movementLock = false;
		chargeCoords = new float[2];
		character.conditions.remove(Conditions.ConditionNames.COMING_THROUGH);
	}

	public void cancelRam(){
		abilities.get(1).cancelActivation();
	}


	void chargeInput(){
		targetProcessor.changeRadius(chargeRange);
		targetProcessor.changeCheckWalkable(true);
		targetProcessor.changeRayCast(true);
		targetProcessor.render();
		if(leftClickJustPressed()) {
			Vector3 temporal = roundedClick();
			if (targetProcessor.findATile(temporal.x,temporal.y) != null) {
				chargeCoords[0] = temporal.x;
				chargeCoords[1] = temporal.y;
				character.actionDecided();
			}
		}
		if(actionConfirmJustPressed()) {
			if (targetProcessor.findATile(targetProcessor.getTargetX(), targetProcessor.getTargetY()) != null && !(targetProcessor.getTargetX() == character.getX() && targetProcessor.getTargetY() == character.getY())) {
				chargeCoords[0] = targetProcessor.getTargetX();
				chargeCoords[1] = targetProcessor.getTargetY();
				character.actionDecided();
			}
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
		if(leftClickJustPressed()) {
			Vector3 temporal = roundedClick();
			if (targetProcessor.findATile(temporal.x,temporal.y) != null) {
				rocksCoords[0] = temporal.x;
				rocksCoords[1] = temporal.y;
				willShoot = true;
				character.actionDecided();
				character.movementLock = false;
			}
		}
		if(actionConfirmJustPressed()) {
			if (targetProcessor.findATile(targetProcessor.getTargetX(), targetProcessor.getTargetY()) != null && !(targetProcessor.getTargetX() == character.getX() && targetProcessor.getTargetY() == character.getY())) {
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
		getClIns("Catapult").setShield(shield);
		getClIns("Catapult").setWeapon(weapon);
		getClIns("Catapult").setCooldown(getAbilitiesCd());
		destroyListener(oVS);
		character.conditions.remove(Conditions.ConditionNames.COMING_THROUGH);
	}


	public static class Rock extends Entity {
		float objectiveX, objectiveY,xPerTurn,yPerTurn,zPerTurn;
		int turnsToFall;
		float damage;
		boolean finished;
		OnVariousScenarios ove;

		public Rock(float x,float y,float objectiveX, float objectiveY,float damage){
			super("Boulder",x,y);
			this.damage = damage;
			this.objectiveX = objectiveX;
			this.objectiveY = objectiveY;
			double distance = dC(objectiveX,objectiveY)/globalSize();
			print("Distance of rock is of " + distance);
			turnsToFall = distance <= 2 ? 3 : distance <= 5 ? 2 : distance <= 8 ? 1 : 0;
			xPerTurn = (objectiveX - x) / (1+turnsToFall);
			yPerTurn = (objectiveY - y) / (1+turnsToFall);
			zPerTurn = 3f / turnsToFall;
		}

		public void advanceRock(){
			glide(xPerTurn, yPerTurn, turnsToFall > 1 ? zPerTurn : -1,60);
			turnStopTimer(60);
			if(turnsToFall == 0){
				if (actorInPos(objectiveX,objectiveY) != null && !finished)
					ove = new OnVariousScenarios.CounterObject(60){
					public void onCounterFinish(){
						animations.add(new Animation("boulderbreaking",x,y){
							public void onFinish() {
								print("finished");
							}
						});
						actorInPos(objectiveX,objectiveY).damage(damage, AttackTextProcessor.DamageReasons.RANGED,chara);
						destroyListener(ove);
					}
				};
				else if (!finished){
					ove = new OnVariousScenarios.CounterObject(60){
						public void onCounterFinish(){
							animations.add(new Animation("boulderbreaking",x,y){
								public void onFinish() {
									print("finished");
								}
							});
							destroyListener(ove);
						}
					};
				}
				finished = true;
			}
			turnsToFall--;
		}

		public void update(){
			glideProcess();
			render();
		}

	}










}