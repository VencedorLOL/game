package com.mygdx.game.items;

import com.mygdx.game.items.characters.classes.Melee;
import com.mygdx.game.items.characters.classes.SwordMage;

import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.TextureManager.text;

public class Conditions {

	boolean tickDownOnTurn;
	int turnsActive;
	String name = "NullCondition";
	Actor owner;
	String texture;

	public Conditions(Actor owner){
		this.owner = owner;
	}

	public void setTurns(int turns){turnsActive = turns;}

	public int getTurns(){return turnsActive;}

	protected void onTurn(){}

	protected void onDamaged(AttackTextProcessor.DamageReasons reason){}

	protected void onAttack(){}

	protected boolean onAttackDecided(){return true;}

	protected void onMove(){}

	float getHealthMultiplier(){return 1;}

	float getHealthAdditive(){return 0;}

	float getDamageMultiplier(){return 1;}

	float getDamageAdditive(){return 0;}

	float getTempDefenseMultiplier(){return 1;}

	float getTempDefenseAdditive(){return 0;}

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


	public static class Hyperthermia extends Conditions {

		public Hyperthermia (Actor owner){
			super(owner);
			name = "Hyperthermia";
		}

		protected void onAttack() {
			owner.damage(owner.maxHealth * .15f, AttackTextProcessor.DamageReasons.BURNT);
		}

		protected void onMove() {
			owner.damage(owner.maxHealth * .15f, AttackTextProcessor.DamageReasons.BURNT);
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
		public Burning(Actor owner){super(owner); name = "Burning"; texture  = "BurntStatus";}
		protected void onTurn(){owner.damage(owner.maxHealth * .5f, AttackTextProcessor.DamageReasons.BURNT);}
	}
	public static class BurningBright extends Conditions{
		public BurningBright(Actor owner){super(owner); name = "BurningBright"; texture = "VeryBurntStatus";}
		protected void onTurn(){owner.damage(owner.maxHealth * .15f, AttackTextProcessor.DamageReasons.BURNT);}
	}
	public static class Melting extends Conditions{
		public Melting(Actor owner){super(owner); name = "Melting"; texture = "MeltingStatus";}
		protected void onTurn(){owner.damage(owner.maxHealth * .35f, AttackTextProcessor.DamageReasons.BURNT);}
	}
	public static class Sublimating extends Conditions{
		public Sublimating(Actor owner){super(owner); name = "Sublimating";}
		protected void onTurn(){owner.damage(owner.maxHealth * 2, AttackTextProcessor.DamageReasons.BURNT);}
	}

	public static class Frostbite extends Conditions{
		public Frostbite(Actor owner){super(owner); name = "Frostbite"; texture = "FrostbiteStatus";}
		protected void onTurn(){owner.damage(owner.maxHealth * .5f, AttackTextProcessor.DamageReasons.FROSTBITE);}
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

		public Protected(Actor owner) {
			super(owner);
			texture = "Protected"; name = "Protected";
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
		;


		public final String name;
		ConditionNames(String name){
			this.name = name;
		}



	}


}
