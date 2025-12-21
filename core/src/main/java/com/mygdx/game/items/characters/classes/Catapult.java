package com.mygdx.game.items.characters.classes;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.*;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.weapons.CatapultAmmo;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Actor.actorInPos;
import static com.mygdx.game.items.ClickDetector.rayCasting;
import static com.mygdx.game.items.ClickDetector.roundedClick;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.TurnManager.*;
import static java.lang.Math.max;

public class Catapult extends CharacterClasses {

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

		abilities.add(new Ability("ChargeCatapult", "Charge the Catapult", 0, 75	,76, (float) globalSize() /2){
			@Override
			public void active() {
				if(!isCharged) {
					print("activated cata 2");
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
		if(character.permittedToAct){
			if(willShoot && isCharged){
				throwingMode = false;
				isCharged = false;
				willShoot = false;
				if(weapon != null && weapon instanceof CatapultAmmo)
					((CatapultAmmo) weapon).throwRock(character.x,character.y,rocksCoords[0],rocksCoords[1],totalDamage*10,totalAttackSpeed*100+totalSpeed);
				else
					new Rock(character.x,character.y,rocksCoords[0],rocksCoords[1],totalDamage*10,totalAttackSpeed*100+totalSpeed);
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

		} else if (isDecidingWhatToDo(character)) {

			if (character.attackMode && isCharged) {
				character.cancelAttackMode();
				throwingMode = !throwingMode;
				abilities.get(1).cancelActivation();
			}

			if (throwingMode)
				rockThrowInput();
			else if (character.attackMode)
				abilities.get(1).cancelActivation();
			if (abilities.get(1).isItActive) {
				chargeInput();
			}
		}

		
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


	public static class Rock extends Entity implements TurnManager.Turnable {
		public float objectiveX, objectiveY,xPerTurn,yPerTurn,zPerTurn;
		public int turnsToFall;
		public float damage;
		public boolean finished;
		public OnVariousScenarios ove;
		public OnVariousScenarios lifeOVE;
		public float initialDistance;
		public float speed;
		public boolean didItAct;
		public boolean permitToAct;

		public Rock(float x,float y,float objectiveX, float objectiveY,float damage,float speed){
			super("Boulder",x,y);
			this.damage = damage;
			this.objectiveX = objectiveX;
			this.objectiveY = objectiveY;
			initialDistance = (float) dC(objectiveX,objectiveY)/globalSize();
			print("Distance of rock is of " + initialDistance);
			turnsToFall = initialDistance <= 2 ? 3 : initialDistance <= 5 ? 2 : initialDistance <= 8 ? 1 : 0;
			xPerTurn = (objectiveX - x) / (1+turnsToFall);
			yPerTurn = (objectiveY - y) / (1+turnsToFall);
			zPerTurn = max(5 / (initialDistance + 2),1.5f);
			this.speed = speed - .1f;
			Rock temp = this;
			lifeOVE = new OnVariousScenarios(){
				@Override
				public void onStageChange() {
					destroyListener(ove);
					destroyListener(lifeOVE);
					entityList.remove(temp);
					turnables.remove(temp);
				}
			};
			generalRender = true;
			turnables.add(this);
			changeZ();
		}

		public void render(){
			glideProcess();
			if(render)
				TextureManager.addToList(getTexture(),x,y,z);
			if(finished && !isGliding){
				destroyListener(ove);
				destroyListener(lifeOVE);
				entityList.remove(this);
				turnables.remove(this);
			}
		}

		public void changeZ(){}

		public void advanceRock(){
			glide(xPerTurn, yPerTurn, dC(objectiveX,objectiveY)/globalSize()*1.5 > initialDistance ? zPerTurn : -zPerTurn,60);
			turnStopTimer(60);
			if(turnsToFall == 0){
				Rock temp = this;
				if (!finished)
					ove = new OnVariousScenarios.CounterObject(60){
					public void onCounterFinish(){
						animations.add(new Animation("boulderbreaking",x,y));
						if(actorInPos(objectiveX,objectiveY) != null)
							actorInPos(objectiveX,objectiveY).damage(damage, AttackTextProcessor.DamageReasons.RANGED,chara);
						destroyListener(ove);
						destroyListener(lifeOVE);
						entityList.remove(temp);
						turnables.remove(temp);
					}
				};

				finished = true;
			}
			turnsToFall--;
			permitToAct = false;
		}

		@Override
		public float getSpeed() {
			return speed;
		}

		@Override
		public boolean didItAct() {
			return didItAct;
		}

		@Override
		public boolean getIsDead() {
			return false;
		}

		@Override
		public boolean isPermittedToAct() {
			return permitToAct;
		}

		@Override
		public void setDidItAct(boolean didItAct) {
			this.didItAct = didItAct;
		}

		@Override
		public void permitToAct() {
			permitToAct = true;
		}

		@Override
		public void letAct() {
			if (!didItAct()) {
				permitToAct();
				setDidItAct(true);
				advanceRock();

			}
		}
	}










}