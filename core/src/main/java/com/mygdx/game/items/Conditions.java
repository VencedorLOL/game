package com.mygdx.game.items;

import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Imp;
import com.mygdx.game.items.characters.classes.Melee;
import com.mygdx.game.items.characters.classes.SwordMage;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.floor;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.Conditions.ConditionNames.*;

public class Conditions {

	boolean tickDownOnTurn;
	int turnsActive;
	String name = "NullCondition";
	Actor owner;
	String texture;
	boolean queuedForRemoval = false;

	public Conditions(Actor owner){
		this.owner = owner;
	}

	public void setTurns(int turns){turnsActive = turns;}

	public int getTurns(){return turnsActive;}

	protected void onKill(){}

	protected void onDeath(){}

	protected void onTurn(){}

	protected void onDamaged(AttackTextProcessor.DamageReasons reason){}

	protected void onAttack(){}

	protected boolean onAttackDecided(){return true;}

	protected void onMove(){}

	protected void onStageChange(){}

	float getHealthMultiplier(){return 1;}
	float getHealthAdditive(){return 0;}
	float getDamageMultiplier(){return 1;}
	float getDamageAdditive(){return 0;}
	float getSpeedMultiplier(){return 1;}
	float getSpeedAdditive(){return 0;}
	float getActingSpeedMultiplier(){return 1;}
	float getActingSpeedAdditive(){return 0;}
	float getDefenseMultiplier(){return 1;}
	float getDefenseAdditive(){return 0;}
	float getRangeMultiplier(){return 1;}
	float getRangeAdditive(){return 0;}
	float getManaMultiplier(){return 1;}
	float getManaAdditive(){return 0;}
	float getManaPerTurnMultiplier(){return 1;}
	float getManaPerTurnAdditive(){return 0;}
	float getManaPerUseMultiplier(){return 1;}
	float getManaPerUseAdditive(){return 0;}
	float getMagicDamageMultiplier(){return 1;}
	float getMagicDamageAdditive(){return 0;}
	float getAggroMultiplier(){return 1;}
	float getAggroAdditive(){return 0;}

	public void destroyCondition(){}

	public static class Hyperthermia extends Conditions {
		boolean damaged = false;
		public Hyperthermia (Actor owner){
			super(owner);
			name = "Hyperthermia";

		}

		protected void onAttack() {
			if(!damaged) {
				owner.damage(owner.totalMaxHealth * .15f, AttackTextProcessor.DamageReasons.BURNT, null);
				damaged = true;
			}
		}

		protected void onMove() {
			if(!damaged) {
				owner.damage(owner.totalMaxHealth * .15f, AttackTextProcessor.DamageReasons.BURNT, null);
				damaged = true;
			}
		}

		@Override
		protected void onTurn() {
			damaged = false;
		}
	}

	public static class Hypothermia extends Conditions {

		public Hypothermia (Actor owner){
			super(owner);
			name = "Hypothermia";
		}

		protected float getSpeedMultiplier(){
			return 0.8f;
		}

		protected float getActingSpeedMultiplier(){
			return 0.8f;
		}

	}

	public static class OneForAll extends Conditions {

		public OneForAll (Actor owner){
			super(owner);
			name = "OneForAll";
			turnsActive = 1;
			tickDownOnTurn = true;
			texture = "OneForAllStatus";
		}

		protected float getDamageMultiplier(){
			if(owner instanceof Character && ((Character) owner).classes instanceof Melee)
				return ((Melee) ((Character) owner).classes).OfAMultiplier;
			return 1;
		}

	}
	public static class Burning extends Conditions{
		public Burning(Actor owner){super(owner); name = "Burning"; texture  = "BurntStatus"; turnsActive = 5; tickDownOnTurn = true;}
		protected void onTurn(){owner.damage(owner.totalMaxHealth * .05f + 5, AttackTextProcessor.DamageReasons.BURNT,null);}
	}
	public static class BurningBright extends Conditions{
		public BurningBright(Actor owner){super(owner); name = "BurningBright"; texture = "VeryBurntStatus";}
		protected void onTurn(){owner.damage(owner.totalMaxHealth * .15f + 10, AttackTextProcessor.DamageReasons.BURNT,null);}
	}
	public static class Melting extends Conditions{
		public Melting(Actor owner){super(owner); name = "Melting"; texture = "MeltingStatus";}
		protected void onTurn(){owner.damage(owner.totalMaxHealth * .35f + 50, AttackTextProcessor.DamageReasons.BURNT,null);}
	}
	public static class Sublimating extends Conditions{
		public Sublimating(Actor owner){super(owner); name = "Sublimating"; tickDownOnTurn = false; turnsActive = 2;}
		protected void onTurn(){owner.damage(owner.totalMaxHealth * 2 + 500, AttackTextProcessor.DamageReasons.BURNT,null);}
	}

	public static class Frostbite extends Conditions{
		public Frostbite(Actor owner){super(owner); name = "Frostbite"; texture = "FrostbiteStatus";}
		protected void onTurn(){owner.damage(owner.totalMaxHealth * .15f + 10, AttackTextProcessor.DamageReasons.FROSTBITE,null);}
	}
	public static class Frozen extends Conditions{
		public Frozen(Actor owner){super(owner); name = "Frozen"; texture = "FrozenStatus";}

	}


	public static class ManaHit extends Conditions{
		public ManaHit(Actor owner) {
			super(owner); name = "ManaHit"; texture = "MagicallyEnhanced"; turnsActive = -1; tickDownOnTurn = false;
		}

		boolean hits = false;

		protected void onAttack() {
			if (owner instanceof Character && ((Character) owner).classes instanceof SwordMage) {
				if (((Character) owner).classes.totalDamage *
						((SwordMage) ((Character) owner).classes).finalDamageMultiplier *
						((SwordMage) ((Character) owner).classes).finalManaCost
						<= ((Character) owner).classes.manaPool){
					hits = true;
				}
			} else 	text("Out Of Mana!",0,150,200, TextureManager.Fonts.ComicSans,40,owner);
		}

		float getDamageMultiplier() {
			if (owner instanceof Character && ((Character) owner).classes instanceof SwordMage && hits &&
					((Character) owner).classes.manaPool >= ((Character) owner).classes.totalDamage *
					((SwordMage) ((Character) owner).classes).finalDamageMultiplier *
					((SwordMage) ((Character) owner).classes).finalManaCost) {

				((Character) owner).classes.manaPool -= ((Character) owner).classes.totalDamage *
						((SwordMage) ((Character) owner).classes).finalDamageMultiplier *
						((SwordMage) ((Character) owner).classes).finalManaCost;
				hits = false;
				return ((SwordMage) ((Character) owner).classes).finalDamageMultiplier;
			}
			hits = false;
			return 1;
		}

		float getRangeAdditive(){
			if (owner instanceof Character && ((Character) owner).classes instanceof SwordMage &&
					((Character) owner).classes.manaPool >= ((Character) owner).classes.totalDamage *
							((SwordMage) ((Character) owner).classes).finalDamageMultiplier *
							((SwordMage) ((Character) owner).classes).finalManaCost)
				return 2;
			return 0;
		}

	}

	public static class EvenFaster extends Conditions{
		public EvenFaster(Actor owner){
			super(owner); texture = "evenFasterStatus"; name = "EvenFaster"; turnsActive = 1; tickDownOnTurn = true;
		}

		float getSpeedAdditive(){
			return 2;
		}

		float getActingSpeedAdditive(){
			return 1;
		}

	}

	public static class Protected extends Conditions {

		public ArrayList<Actor> protectors;

		public Protected(Actor owner) {
			super(owner);
			texture = "Protected"; name = "Protected";
		}

		float getAggroMultiplier(){
			return checkForProtections() ? 0 : 1;
		}

		protected void onDamaged(AttackTextProcessor.DamageReasons reasons){
			owner.damageRecieved *= checkForProtections() ? 0 : 1;
		}

		public boolean checkForProtections(){
			protectors.removeIf(a -> a.conditions.hasStatus(ConditionNames.PROTECTING));
			if(protectors.isEmpty()) {
				owner.conditions.remove(ConditionNames.PROTECTED);
				return false;
			}
			return true;
		}
		
	}

	public static class Protecting extends Conditions{

		public ArrayList<Actor> protecting;

		public Protecting(Actor owner) {
			super(owner);
			texture = "Protecting"; name = "Protecting";
		}

		public void onTurn(){
			protecting.removeIf(a -> a.conditions.hasStatus(ConditionNames.PROTECTED));
			if(protecting.isEmpty()) {
				owner.conditions.remove(ConditionNames.PROTECTING);
			}
		}

	}


	public static class Ritual extends Conditions {
		OnVariousScenarios deathListener;
		int extraTurnsGiven;
		int extraTurnsLimit;
		public boolean wingedRitual;
		public Ritual(Actor owner) {
			super(owner);
			texture = "RitualEffect"; name = "Ritual"; tickDownOnTurn = true;
			deathListener = new OnVariousScenarios(){
				@Override
				public void onActorDeath(Actor deadActor) {
					if(extraTurnsGiven < extraTurnsLimit){
						extraTurnsGiven++;
						turnsActive++;
					}
				}
			};
		}

		float getDamageMultiplier() {return 1.33f;}
		public float speed = 2;
		float getSpeedAdditive() {return speed;}
		public float actingSpeed = 2;
		float getActingSpeedAdditive() {return actingSpeed;}
		float getRangeAdditive() {return 2;}
		public void setExtraTurnsLimit(int extraTurnsLimit){this.extraTurnsLimit = extraTurnsLimit;}
		public void update(){if(wingedRitual) owner.airborn = true;}
		public void destroyCondition(){
			destroyListener(deathListener);
			if(wingedRitual) owner.airborn = false;
		}
	}


	public static class Demonized extends Conditions{
		CharacterClasses beneficiary;
		public Demonized(Actor owner) {
			super(owner);
			texture = "Demonize"; name = "Demonized"; tickDownOnTurn = true;
		}

		protected void onDamaged(AttackTextProcessor.DamageReasons reason) {
			owner.damageRecieved *= (1 + 2/3f);
		}

		float getSpeedAdditive() {return -1;}
		float getDamageMultiplier() {return 0.75f;}
		float getDefenseMultiplier() {return 0.75f;}

		public void getBeneficiary(CharacterClasses chara){beneficiary = chara;}

		@Override
		protected void onDeath() {
			if (beneficiary instanceof Imp){
				((Imp) beneficiary).diedMark = true;
			}
		}
	}

	public static class Clowdy extends Conditions{
		public Clowdy(Actor owner){
			super(owner);
			texture = "ClowdyStatus"; name = "Clowdy"; tickDownOnTurn = false; turnsActive = 2;
		}
		float getDamageMultiplier() {return 0.95f;}
		protected void onStageChange() {
			queuedForRemoval = true;
		}
	}

	public static class Rainy extends Conditions{
		public Rainy(Actor owner){
			super(owner);
			texture = "RainyStatus"; name = "Rainy"; tickDownOnTurn = false; turnsActive = 2;
		}
		float getDamageMultiplier() {return 0.90f;}
		protected void onStageChange() {
			queuedForRemoval = true;}

		protected void onTurn() {
			if(owner.conditions.hasStatus(ConditionNames.BURNING) && owner.conditions.getStatus(ConditionNames.BURNING).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.BURNING).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.MELTING) && owner.conditions.getStatus(ConditionNames.MELTING).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.MELTING).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.SUBLIMATING) && owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.BURNING_BRIGHT) && owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.HYPERTHERMIA) && owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive--;
		}
	}

	public static class Snowy extends Conditions{
		int turnCounter;
		public Snowy(Actor owner){
			super(owner);
			texture = "SnowyStatus"; name = "Snowy"; tickDownOnTurn = false; turnsActive = 2;
		}
		float getDamageMultiplier() {return 0.95f;}
		protected void onTurn() {
			turnCounter++;
			if(owner.conditions.hasStatus(ConditionNames.BURNING) && owner.conditions.getStatus(ConditionNames.BURNING).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.BURNING).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.MELTING) && owner.conditions.getStatus(ConditionNames.MELTING).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.MELTING).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.SUBLIMATING) && owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.BURNING_BRIGHT) && owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.HYPERTHERMIA) && owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive--;
		}
		float getSpeedAdditive() {return -floor(turnCounter/20f);}
		protected void onStageChange() {
			queuedForRemoval = true;}

	}

	public static class Sunny extends Conditions{
		boolean[] extendedDuration = new boolean[]{false,false,false,false,false};
		public Sunny(Actor owner){
			super(owner);
			texture = "SunnyStatus"; name = "Sunny"; tickDownOnTurn = false; turnsActive = 2;
		}
		float getDamageMultiplier() {return 1.05f;}
		protected void onStageChange() {
			queuedForRemoval = true;
		}

		protected void onTurn(){
			if(owner.conditions.hasStatus(ConditionNames.BURNING) && owner.conditions.getStatus(ConditionNames.BURNING).turnsActive > 0 && !extendedDuration[0]) {
				owner.conditions.getStatus(ConditionNames.BURNING).turnsActive += 2;
				extendedDuration[0] = true;}
			if(owner.conditions.hasStatus(ConditionNames.MELTING) && owner.conditions.getStatus(ConditionNames.MELTING).turnsActive > 0 && !extendedDuration[1]) {
				owner.conditions.getStatus(ConditionNames.MELTING).turnsActive += 2;
				extendedDuration[1] = true;}
			if(owner.conditions.hasStatus(ConditionNames.SUBLIMATING) && owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive > 0 && !extendedDuration[2]) {
				owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive += 2;
				extendedDuration[2] = true;}
			if(owner.conditions.hasStatus(ConditionNames.BURNING_BRIGHT) && owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive > 0 && !extendedDuration[3]) {
				owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive += 2;
				extendedDuration[3] = true;}
			if(owner.conditions.hasStatus(ConditionNames.HYPERTHERMIA) && owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive > 0 && !extendedDuration[4]) {
				owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive += 2;
				extendedDuration[4] = true;}

			if(owner.conditions.hasStatus(ConditionNames.HYPOTHERMIA) && owner.conditions.getStatus(ConditionNames.HYPOTHERMIA).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.HYPOTHERMIA).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.FROZEN) && owner.conditions.getStatus(ConditionNames.FROZEN).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.FROZEN).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.FROSTBITE) && owner.conditions.getStatus(ConditionNames.FROSTBITE).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.FROSTBITE).turnsActive--;
		}

	}

	public static class Stunned extends Conditions{
		public Stunned(Actor owner){
			super(owner);
			texture = "StunnedStatus"; name = "Stunned"; tickDownOnTurn = true; turnsActive = 2;
		}
		float getDamageMultiplier() {
			if(owner.permittedToAct)
				owner.spendTurn();
			return 1;
		}
		protected void onStageChange() {
			queuedForRemoval = true;
		}
	}


	public static class ComingThrough extends Conditions{
		public ComingThrough(Actor owner){
			super(owner); texture = "ComingThrough"; name = "ComingThrough"; turnsActive = 1; tickDownOnTurn = true;
		}

		float getActingSpeedAdditive(){
			return 6;
		}

	}

	public static class StellarStorm extends Conditions{
		boolean[] extendedDuration = new boolean[]{false,false,false,false,false};
		int turnCounter;
		public StellarStorm(Actor owner){
			super(owner);
			texture = "StellarStorm"; name = "StellarStorm"; tickDownOnTurn = false; turnsActive = 2;
		}
		float getDamageMultiplier() {return 1.15f;}
		protected void onTurn() {
			turnCounter++;
			if(turnCounter > 5){
				owner.damage(owner.health * (floor((turnCounter-3)/2f))/100f +
						owner.totalMaxHealth * (floor((turnCounter-5)/2f))/200f, AttackTextProcessor.DamageReasons.BURNT,null);
			}

			if(owner.conditions.hasStatus(ConditionNames.BURNING) && owner.conditions.getStatus(ConditionNames.BURNING).turnsActive > 0 && !extendedDuration[0]) {
				owner.conditions.getStatus(ConditionNames.BURNING).turnsActive += 5;
				extendedDuration[0] = true;}
			if(owner.conditions.hasStatus(ConditionNames.MELTING) && owner.conditions.getStatus(ConditionNames.MELTING).turnsActive > 0 && !extendedDuration[1]) {
				owner.conditions.getStatus(ConditionNames.MELTING).turnsActive += 5;
				extendedDuration[1] = true;}
			if(owner.conditions.hasStatus(ConditionNames.SUBLIMATING) && owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive > 0 && !extendedDuration[2]) {
				owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive += 5;
				extendedDuration[2] = true;}
			if(owner.conditions.hasStatus(ConditionNames.BURNING_BRIGHT) && owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive > 0 && !extendedDuration[3]) {
				owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive += 5;
				extendedDuration[3] = true;}
			if(owner.conditions.hasStatus(ConditionNames.HYPERTHERMIA) && owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive > 0 && !extendedDuration[4]) {
				owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive += 5;
				extendedDuration[4] = true;}

			if(owner.conditions.hasStatus(ConditionNames.HYPOTHERMIA) && owner.conditions.getStatus(ConditionNames.HYPOTHERMIA).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.HYPOTHERMIA).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.FROZEN) && owner.conditions.getStatus(ConditionNames.FROZEN).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.FROZEN).turnsActive--;
			if(owner.conditions.hasStatus(ConditionNames.FROSTBITE) && owner.conditions.getStatus(ConditionNames.FROSTBITE).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.FROSTBITE).turnsActive--;
		}

		protected void onStageChange() {
			queuedForRemoval = true;
		}

	}

	public static class ElectricGround extends Conditions{

		public ElectricGround(Actor owner) {
			super(owner);
			name = "ElectricGround";
			texture = "ElectricGround";
			turnsActive = -1;
		}

		float getSpeedMultiplier() {
			return owner.airborn ? 1 : 0.5f;
		}

		public void update(){
			if(owner.airborn)
				texture = "ElectricGroundAirborn";
			else
				texture = "ElectricGround";
		}

		protected void onStageChange() {
			queuedForRemoval = true;
		}
	}


	public static class AugmentedGravity extends Conditions{

		public AugmentedGravity(Actor owner) {
			super(owner);
			name = "AumentedGravity";
			texture = "GravityCataclysm";
			turnsActive = -1;
		}

		float getSpeedMultiplier() {return .25f;}
		float getActingSpeedMultiplier() {return .25f;}
		float getRangeMultiplier() {return .60f;}

		protected void onMove() {
			if(random() > .85f){
				if(!owner.conditions.hasStatus(STUNNED)) {
					owner.conditions.condition(STUNNED);
					owner.conditions.getStatus(STUNNED).setTurns(2);
				} else if (owner.conditions.getStatus(STUNNED).turnsActive < 2)
					owner.conditions.getStatus(STUNNED).setTurns(2);
				owner.damage(owner.health*.2f, AttackTextProcessor.DamageReasons.PRESSURE,owner);
			}
		}

		public void update(){
			if(owner.airborn)
				owner.airborn = false;
		}

		protected void onStageChange() {
			queuedForRemoval = true;
		}
	}

	public static class NuclearEvent extends Conditions{

		public NuclearEvent(Actor owner) {
			super(owner);
			name = "NuclearEvent";
			texture = "NuclearCataclysm";
			turnsActive = -1;
		}

		float getSpeedMultiplier() {return .90f;}
		float getActingSpeedMultiplier() {return .90f;}
		float getDamageMultiplier() {return .90f;}
		float getMagicDamageMultiplier() {return .90f;}
		float getDefenseMultiplier() {return .90f;}
		float getHealthMultiplier() {return .90f;}
		float getAggroMultiplier() {return .9f;}
		float getRangeMultiplier() {return .9f;}
		float getManaMultiplier() {return 0.9f;}
		float getManaPerTurnMultiplier() {return 0.9f;}


		protected void onStageChange() {
			queuedForRemoval = true;
		}
	}

	public static class Glaciation extends Conditions{

		public Glaciation(Actor owner) {
			super(owner);
			name = "Glaciation";
			texture = "GlacialCataclysm";
			turnsActive = -1;
		}

		float getSpeedMultiplier() {return .25f;}
		float getActingSpeedMultiplier() {return .25f;}
		float getDamageMultiplier() {return .75f;}
		float getMagicDamageMultiplier() {return .75f;}
		float getDefenseMultiplier() {return .15f;}
		float getHealthMultiplier() {return .75f;}


		protected void onStageChange() {
			queuedForRemoval = true;
		}
	}

	public static class StellarExplosion extends Conditions {
		boolean[] extendedDuration = new boolean[]{false, false, false, false, false};
		int turnCounter;

		public StellarExplosion(Actor owner) {
			super(owner);
			texture = "StellarExplosionCataclysm";
			name = "StellarExplosion";
			tickDownOnTurn = false;
			turnsActive = -1;
		}

		float getDamageMultiplier() {return 2f;}
		float getMagicDamageMultiplier() {return 2f;}

		protected void onTurn() {
			turnCounter++;
			if (turnCounter > 5) {
				owner.damage(owner.health * (floor((turnCounter - 3) / 2f)) / 100f +
						owner.totalMaxHealth * (floor((turnCounter - 5) / 2f)) / 200f, AttackTextProcessor.DamageReasons.BURNT, null);
			}

			if (owner.conditions.hasStatus(ConditionNames.BURNING) && owner.conditions.getStatus(ConditionNames.BURNING).turnsActive > 0 && !extendedDuration[0]) {
				owner.conditions.getStatus(ConditionNames.BURNING).turnsActive += 55;
				extendedDuration[0] = true;
			}
			if (owner.conditions.hasStatus(ConditionNames.MELTING) && owner.conditions.getStatus(ConditionNames.MELTING).turnsActive > 0 && !extendedDuration[1]) {
				owner.conditions.getStatus(ConditionNames.MELTING).turnsActive += 55;
				extendedDuration[1] = true;
			}
			if (owner.conditions.hasStatus(ConditionNames.SUBLIMATING) && owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive > 0 && !extendedDuration[2]) {
				owner.conditions.getStatus(ConditionNames.SUBLIMATING).turnsActive += 55;
				extendedDuration[2] = true;
			}
			if (owner.conditions.hasStatus(ConditionNames.BURNING_BRIGHT) && owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive > 0 && !extendedDuration[3]) {
				owner.conditions.getStatus(ConditionNames.BURNING_BRIGHT).turnsActive += 55;
				extendedDuration[3] = true;
			}
			if (owner.conditions.hasStatus(ConditionNames.HYPERTHERMIA) && owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive > 0 && !extendedDuration[4]) {
				owner.conditions.getStatus(ConditionNames.HYPERTHERMIA).turnsActive += 55;
				extendedDuration[4] = true;
			}

			if (owner.conditions.hasStatus(ConditionNames.HYPOTHERMIA) && owner.conditions.getStatus(ConditionNames.HYPOTHERMIA).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.HYPOTHERMIA).turnsActive -= 10;
			if (owner.conditions.hasStatus(ConditionNames.FROZEN) && owner.conditions.getStatus(ConditionNames.FROZEN).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.FROZEN).turnsActive -= 10;
			if (owner.conditions.hasStatus(ConditionNames.FROSTBITE) && owner.conditions.getStatus(ConditionNames.FROSTBITE).turnsActive > 1)
				owner.conditions.getStatus(ConditionNames.FROSTBITE).turnsActive -= 10;
		}

		protected void onStageChange() {
			queuedForRemoval = true;
		}

	}









	public enum ConditionNames {
		BURNING("Burning"),
		BURNING_BRIGHT("BurningBright"),
		MELTING("Melting"),
		HYPERTHERMIA("Hyperthermia"),
		HYPOTHERMIA("Hypothermia"),
		SUBLIMATING("Sublimating"),
		FROSTBITE("Frostbite"),
		FROZEN("Frozen"),
		ONE_FOR_ALL("OneForAll"),
		MANA_HIT("ManaHit"),
		EVEN_FASTER("EvenFaster"),
		PROTECTED("Protected"),
		PROTECTING("Protecting"),
		RITUAL("Ritual"),
		DEMONIZED("Demonized"),
		CLOWDY("Clowdy"),
		RAINY("Rainy"),
		SNOWY("Snowy"),
		SUNNY("Sunny"),
		STUNNED("Stunned"),
		COMING_THROUGH("ComingThrough"),
		STELLAR_STORM("StellarStorm"),
		ELECTRIC_GROUND("ElectricGround"),
		AUGMENTED_GRAVITY("AumentedGravity"),
		NUCLEAR_EVENT("NuclearEvent"),
		GLACIATION("Glaciation"),
		STELLAR_EXPLOSION("StellarExplosion"),
		;


		public final String name;
		ConditionNames(String name){
			this.name = name;
		}



	}


}
