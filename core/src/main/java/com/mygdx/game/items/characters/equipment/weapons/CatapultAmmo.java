package com.mygdx.game.items.characters.equipment.weapons;

import com.mygdx.game.items.*;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Catapult;
import com.mygdx.game.items.characters.equipment.Weapons;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actorInPos;
import static com.mygdx.game.items.Hazards.*;
import static com.mygdx.game.items.TargetProcessor.Circle.simpleCircle;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.TurnManager.turnStopTimer;
import static com.mygdx.game.items.TurnManager.turnables;
import static java.lang.Math.*;

public class CatapultAmmo extends Weapons {

	public CatapultAmmo(CharacterClasses holder, boolean effectiveInstantiation) {
		super(holder, effectiveInstantiation);
		weaponName = "Basic Rock";
	}

	public void throwRock(float x,float y,float objectiveX, float objectiveY,float damage,float speed){
		new Catapult.Rock(x,y,objectiveX,objectiveY,damage,speed);
	}

	public static class ClusterRock extends CatapultAmmo{
		public ClusterRock(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			weaponName = "Cluster Rock";
			weaponHealth = 0;
			weaponDamage = 50;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 0;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			equippableBy = "Catapult";
			aggro = 0;
		}

		public void throwRock(float x,float y,float objectiveX, float objectiveY,float damage,float speed){
			new Cluster_Rock(x,y,objectiveX,objectiveY,damage,speed);
		}

		public static class Cluster_Rock extends Catapult.Rock {
			public Cluster_Rock(float x,float y,float objectiveX, float objectiveY,float damage,float speed){
				super(x,y,objectiveX,objectiveY,damage,speed);
				setTexture("ClusterBoulder");
			}

			public void advanceRock(){
				glide(xPerTurn, yPerTurn, dC(objectiveX,objectiveY)/globalSize()*1.5 > initialDistance ? zPerTurn : -zPerTurn,60);
				turnStopTimer(61);
				if(turnsToFall == 0){
					Catapult.Rock temp = this;
					if (!finished)
						ove = new OnVariousScenarios.CounterObject(60){
							public void onCounterFinish(){
								animations.add(new TextureManager.Animation("boulderbreaking",x,y));
								if(actorInPos(objectiveX,objectiveY) != null)
									actorInPos(objectiveX,objectiveY).damage(damage, AttackTextProcessor.DamageReasons.RANGED,chara);
								turnStopTimer(12);
								new OnVariousScenarios.CounterObject(10){
									public void onCounterFinish() {
										Catapult.Rock[] rocks = {
											new Catapult.Rock(x,y,objectiveX+globalSize(),objectiveY,damage,speed){public void changeZ() {zPerTurn = -.5f;}},
											new Catapult.Rock(x,y,objectiveX+globalSize(),objectiveY+globalSize(),damage,speed){public void changeZ() {zPerTurn = -.5f;}},
											new Catapult.Rock(x,y,objectiveX,objectiveY+globalSize(),damage,speed){public void changeZ() {zPerTurn = -.5f;}},
											new Catapult.Rock(x,y,objectiveX-globalSize(),objectiveY+globalSize(),damage,speed){public void changeZ() {zPerTurn = -.5f;}},
											new Catapult.Rock(x,y,objectiveX-globalSize(),objectiveY,damage,speed){public void changeZ() {zPerTurn = -.5f;}},
											new Catapult.Rock(x,y,objectiveX-globalSize(),objectiveY-globalSize(),damage,speed){public void changeZ() {zPerTurn = -.5f;}},
											new Catapult.Rock(x,y,objectiveX,objectiveY-globalSize(),damage,speed){public void changeZ() {zPerTurn = -.5f;}},
											new Catapult.Rock(x,y,objectiveX+globalSize(),objectiveY-globalSize(),damage,speed){public void changeZ() {zPerTurn = -.5f;}},
										};
										turnStopTimer(12);
										for(Catapult.Rock r : rocks){
											r.advanceRock();
											r.advanceRock();
											r.advanceRock();
											r.advanceRock();
										}
										new OnVariousScenarios.CounterObject(10){
											public void onCounterFinish() {
												Catapult.Rock[] rocks = {
														new Catapult.Rock(x,y,objectiveX+globalSize()*2,objectiveY,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX+globalSize()*2,objectiveY+globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX,objectiveY+globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX-globalSize()*2,objectiveY+globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX-globalSize()*2,objectiveY,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX-globalSize()*2,objectiveY-globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX,objectiveY-globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX+globalSize()*2,objectiveY-globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},

														new Catapult.Rock(x,y,objectiveX+globalSize()*2,objectiveY+globalSize(),damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX+globalSize(),objectiveY+globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX+globalSize(),objectiveY-globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX-globalSize()*2,objectiveY+globalSize(),damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX-globalSize(),objectiveY+globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX-globalSize(),objectiveY-globalSize()*2,damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX+globalSize()*2,objectiveY-globalSize(),damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
														new Catapult.Rock(x,y,objectiveX-globalSize()*2,objectiveY-globalSize(),damage/3,speed){public void changeZ() {zPerTurn = -.5f;}},
												};
												for (Catapult.Rock r : rocks) {
													r.advanceRock();
													r.advanceRock();
													r.advanceRock();
													r.advanceRock();
												}
											}
										};

										entityList.remove(temp);
										turnables.remove(temp);
									}
								};
								destroyListener(ove);
								destroyListener(lifeOVE);

							}
					};
					finished = true;
				}
				turnsToFall--;
				permitToAct = false;
			}


		}


	}
	public static class HomingRock extends CatapultAmmo {
		public HomingRock(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			weaponName = "Homing Rock";
			weaponHealth = 0;
			weaponDamage = 10;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 0;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			equippableBy = "Catapult";
			aggro = 0;
			if(effectiveInstantiation){
				((Catapult) holder).circle2.borderOpacity = 0;
				((Catapult) holder).circle5.borderOpacity = 0;
				((Catapult) holder).circle8.borderOpacity = 0;
			}
		}

		public void destroyOverridable() {
			((Catapult) holder).circle2.circle = null;
			((Catapult) holder).circle5.circle = null;
			((Catapult) holder).circle8.circle = null;
		}

		public void update() {
			if(((Catapult) holder).circle2.circle != null)
				((Catapult) holder).circle2.circle.circle.clear();
			if(((Catapult) holder).circle5.circle != null)
				((Catapult) holder).circle5.circle.circle.clear();
			if(((Catapult) holder).circle8.circle != null)
				((Catapult) holder).circle8.circle.circle.clear();
		}

		public void throwRock(float x, float y, float objectiveX, float objectiveY, float damage, float speed) {
			new Homing_Rock(x, y, objectiveX, objectiveY, damage, speed);
		}


		public static class Homing_Rock extends Catapult.Rock {
			Actor target;

			public Homing_Rock(float x, float y, float objectiveX, float objectiveY, float damage, float speed) {
				super(x, y, objectiveX, objectiveY, damage, speed);
				target = Actor.actorInPos(objectiveX,objectiveY);
				initialDistance = (float) dC(objectiveX,objectiveY)/globalSize();
				turnsToFall = 4;
				xPerTurn = (objectiveX - x) / (turnsToFall+1);
				yPerTurn = (objectiveY - y) / (turnsToFall+1);
				zPerTurn = max(5 / (initialDistance + 1),1.5f);
				setTexture("SmartBoulder");
			}

			public void advanceRock(){
				if(target != null) {
					xPerTurn = (target.x - x) / (turnsToFall+1);
					yPerTurn = (target.y - y) / (turnsToFall+1);
				}
				glide(xPerTurn, yPerTurn, turnsToFall > 1 ? zPerTurn : -zPerTurn,60);
				turnStopTimer(60);
				if(turnsToFall == 0){
					Catapult.Rock temp = this;
					if (!finished)
						ove = new OnVariousScenarios.CounterObject(60){
							public void onCounterFinish(){
								if(target == null) {
									x = round(x/globalSize())*globalSize();
									y = round(y/globalSize())*globalSize();
								} else {
									x = target.getX();
									y = target.getY();
								}
								animations.add(new TextureManager.Animation("boulderbreaking",x,y));
								if(actorInPos(x,y) != null)
									actorInPos(x,y).damage(damage, AttackTextProcessor.DamageReasons.RANGED,chara);
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

		}

	}


	public static class FlamingRock extends CatapultAmmo {
		public FlamingRock(CharacterClasses holder, boolean effectiveInstantiation) {
			super(holder, effectiveInstantiation);
			weaponName = "Rock On Fire";
			weaponHealth = 0;
			weaponDamage = 1;
			weaponSpeed = 0;
			weaponAttackSpeed = 0;
			weaponDefense = 0;
			weaponRange = 0;
			weaponRainbowDefense = 0;
			weaponMana = 0;
			weaponMagicDefense = 0;
			weaponMagicDamage = 0;
			weaponManaPerTurn = 0;
			weaponManaPerUse = 0;
			weaponMagicHealing = 0;
			equippableBy = "Catapult";
			aggro = 0;
		}

		public void throwRock(float x, float y, float objectiveX, float objectiveY, float damage, float speed) {
			new RockOnFire(x, y, objectiveX, objectiveY, damage, speed);
		}

		public static class RockOnFire extends Catapult.Rock {
			public RockOnFire(float x, float y, float objectiveX, float objectiveY, float damage, float speed) {
				super(x, y, objectiveX, objectiveY, damage, speed);
				setTexture("BoulderOnFire");
			}


			public void advanceRock(){
				glide(xPerTurn, yPerTurn, dC(objectiveX,objectiveY)/globalSize()*1.5 > initialDistance ? zPerTurn : -zPerTurn,60);
				turnStopTimer(60);
				if(turnsToFall == 0){
					Catapult.Rock temp = this;
					if (!finished)
						ove = new OnVariousScenarios.CounterObject(60){
							public void onCounterFinish(){
								animations.add(new TextureManager.Animation("boulderbreaking",x,y));
								if(actorInPos(objectiveX,objectiveY) != null)
									actorInPos(objectiveX, objectiveY).damage(damage, AttackTextProcessor.DamageReasons.RANGED, chara);
								for (TargetProcessor.Circle.CircleTile t : simpleCircle(objectiveX,objectiveY,1.5f))
									hazards.add(new Hazards.FireTile(t.x,t.y,3));
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

		}
	}






}
