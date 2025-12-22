package com.mygdx.game.items.characters;

import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.characters.classes.Classless;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.AttackTextProcessor.DamageReasons.EARTHQUAKE;
import static com.mygdx.game.items.AttackTextProcessor.DamageReasons.ELECTRIC;
import static com.mygdx.game.items.AttackTextProcessor.addAttackText;
import static com.mygdx.game.items.FieldEffects.getAdditive;
import static com.mygdx.game.items.FieldEffects.getMultiplier;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;
import static com.mygdx.game.items.TurnManager.isDecidingWhatToDo;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.*;
import static com.mygdx.game.items.characters.ClassStoredInformation.ClassInstance.getClIns;
import static java.lang.Math.max;

public class CharacterClasses {
	public Character character = chara;
	public String name;
	public float health;
	public float damage;
	public byte  speed;
	public byte  attackSpeed;
	public float defense;
	public float rainbowDefense;
	public float magicDefense;
	public int   range;
	public float mana;
	public float manaPerTurn;
	public float manaPerUse;
	public float magicDamage;
	public float magicHealing;
	public float manaPool;

	public Weapons weapon;

	public Shields shield;

	public float totalHealth;
	public float totalDamage;
	public byte  totalSpeed;
	public byte  totalAttackSpeed;
	public float totalMana;
	public float totalManaPerTurn;
	public float totalManaPerUse;
	public float totalMagicDamage;
	public float totalMagicHealing;
	public float totalDefense;
	public float totalRainbowDefense;
	public float totalMagicDefense;
	public int   totalRange;
	public float totalAggro;

	public float currentHealth;
	public float tempDefense;

	public boolean attacksIgnoreTerrain = false;

	public AttackTextProcessor.DamageReasons damageReason = AttackTextProcessor.DamageReasons.MELEE;

	// Any class might have more than one ability.
	public ArrayList<Ability> abilities;

	public boolean pierces;

	OnVariousScenarios oVE;

	public float aggro;

	public CharacterClasses(){
		abilities = new ArrayList<>();
		reset();
		currentHealth = totalHealth;
		manaPool = mana;
		oVE = new OnVariousScenarios(){
			public void onTurnPass(){
				turnHasPassed();
			}
			public void onDamagedActor(Actor damagedActor, AttackTextProcessor.DamageReasons source) {
				if (damagedActor == character){
					onHurt(source);
				}
			}
		};
	}


	public CharacterClasses(Character characteer){
		this.character = characteer;
		oVE = new OnVariousScenarios(){
			@Override
			public void onTurnPass(){
				turnHasPassed();
			}
			// Detecting damage this way so damage can be manipulated before damage() method is run
			@Override
			public void onDamagedActor(Actor damagedActor, AttackTextProcessor.DamageReasons source) {
				if (damagedActor == character){
					onHurt(source);
				}
			}

		};
	}

	public void handleHpManaTempDf(){
		totalStatsCalculator();
		currentHealth = totalHealth * standarizedHealth;
		manaPool = ClassStoredInformation.ClassInstance.mana;
		tempDefense = tempDf;
	}

	public void getEquipment(){
		if(getClIns(name).getWeapon() != null)
			equipWeapon(getClIns(name).getWeapon());
		if(getClIns(name).getShield() != null)
			equipShield(getClIns(name).getShield());
		if(abilities != null && !abilities.isEmpty())
			if(getClIns(name).getCooldown().length >= abilities.size())
				for(int i = 0; i < abilities.size(); i++)
					abilities.get(i).cooldownCounter = getClIns(name).getCooldown()[i];
	}


	public final void totalStatsCalculator(){
		if (character != null) {
			totalHealth = (health + shield.shieldHealth + weapon.weaponHealth + character.conditions.getAdditive(0) + getAdditive(0)) * character.conditions.getMultiplier(0) *getMultiplier(0);
			totalDamage = (damage + shield.shieldDamage + weapon.weaponDamage + character.conditions.getAdditive(1) + getAdditive(1)) * character.conditions.getMultiplier(1) *getMultiplier(1);
			totalSpeed = (byte) ((speed + shield.shieldSpeed + weapon.weaponSpeed + character.conditions.getAdditive(2) + getAdditive(2))  * character.conditions.getMultiplier(2)*getMultiplier(2));
			totalAttackSpeed = (byte) ((attackSpeed + shield.shieldAttackSpeed + weapon.weaponAttackSpeed + character.conditions.getAdditive(3) + getAdditive(3)) * character.conditions.getMultiplier(3)*getMultiplier(3));
			totalDefense = (defense + shield.shieldDefense + weapon.weaponDefense + character.conditions.getAdditive(4) + getAdditive(4)) * character.conditions.getMultiplier(4)*getMultiplier(4);
			totalRange = (int) ((range + shield.shieldRange + weapon.weaponRange + character.conditions.getAdditive(5) + getAdditive(5)) * character.conditions.getMultiplier(5)*getMultiplier(5));
			totalMana = (mana + weapon.weaponMana + shield.shieldMana + character.conditions.getAdditive(6) + getAdditive(6)) * character.conditions.getMultiplier(6)*getMultiplier(6);
			totalManaPerTurn = (manaPerTurn + weapon.weaponManaPerTurn + shield.shieldManaPerTurn + character.conditions.getAdditive(7) + getAdditive(7)) * character.conditions.getMultiplier(7)*getMultiplier(7);
			totalManaPerUse = (manaPerUse + weapon.weaponManaPerUse + shield.shieldManaPerUse + character.conditions.getAdditive(8) + getAdditive(8)) * character.conditions.getMultiplier(8)*getMultiplier(8);
			totalMagicDamage = (magicDamage + weapon.weaponMagicDamage + shield.shieldMagicDamage + character.conditions.getAdditive(9) + getAdditive(9)) * character.conditions.getMultiplier(9)*getMultiplier(9);
			totalMagicHealing = (magicHealing + weapon.weaponMagicHealing + shield.shieldMagicHealing);
			totalRainbowDefense = (rainbowDefense + weapon.weaponRainbowDefense + shield.shieldRainbowDefense);
			totalMagicDefense = (magicDefense + weapon.weaponMagicDefense + shield.shieldMagicDefense);
			totalAggro = (aggro + weapon.aggro + shield.aggro + character.conditions.getAdditive(11) + getAdditive(11)) * character.conditions.getMultiplier(11) *getMultiplier(11);
		}
		else {
			totalHealth = health + shield.shieldHealth + weapon.weaponHealth;
			totalDamage = damage + shield.shieldDamage + weapon.weaponDamage;
			totalSpeed = (byte) (speed + shield.shieldSpeed + weapon.weaponSpeed);
			totalAttackSpeed = (byte) (attackSpeed + shield.shieldAttackSpeed + weapon.weaponAttackSpeed);
			totalDefense = defense + shield.shieldDefense + weapon.weaponDefense;
			totalRange = range + shield.shieldRange + weapon.weaponRange;
			totalMana = mana + weapon.weaponMana + shield.shieldMana;
			totalManaPerTurn = manaPerTurn + weapon.weaponMana + shield.shieldMana;
			totalManaPerUse = manaPerUse + weapon.weaponManaPerUse + shield.shieldManaPerUse;
			totalMagicDamage = magicDamage + weapon.weaponMagicDamage + shield.shieldMagicDamage;
			totalMagicHealing = magicHealing + weapon.weaponMagicHealing + shield.shieldMagicHealing;
			totalRainbowDefense = rainbowDefense + weapon.weaponRainbowDefense + shield.shieldRainbowDefense;
			totalMagicDefense = magicDefense + weapon.weaponMagicDefense + shield.shieldMagicDefense;
			totalAggro = aggro + weapon.aggro + shield.aggro;
		}
		totalStatsCalculatorOverridable();
	}

	public void healThis(float heal){
		currentHealth += heal;
		if(currentHealth > totalHealth)
			currentHealth = totalHealth;
		addAttackText(heal, AttackTextProcessor.DamageReasons.HEALING,character);
	}



	protected void totalStatsCalculatorOverridable(){}


	//Used in other classes
	public final float outgoingDamage(){
		refresh();
		return outgoingDamageOverridable();
	}

	//Used in CharacterClasses overrides
	protected float outgoingDamageOverridable(){
		return totalDamage;
	}

	//Used in other classes
	public void damage(float damage, AttackTextProcessor.DamageReasons source){
		refresh();
		runHurt(source);
		float damagedFor = max(overridableDamageTaken(damage, source), 0);
		tempDefense -= damagedFor;
		if(tempDefense < 0) {
			currentHealth += tempDefense;
			tempDefense = 0;
		}
		addAttackText(damagedFor,source,character);
	}

	@SuppressWarnings("all")
	//Used in CharacterClasses overrides
	protected float overridableDamageTaken(float damageRecieved, AttackTextProcessor.DamageReasons source){
		return getDamagedFor(damageRecieved,source);
	}

	public float getDamagedFor(float damage, AttackTextProcessor.DamageReasons damageReason) {
		float damagedFor;
		if(damageReason != AttackTextProcessor.DamageReasons.ELECTRIC && damageReason !=  AttackTextProcessor.DamageReasons.BURNT
				&& damageReason !=  AttackTextProcessor.DamageReasons.EARTHQUAKE && damageReason !=  AttackTextProcessor.DamageReasons.UNIVERSAL
				&& damageReason !=  AttackTextProcessor.DamageReasons.FROSTBITE && damageReason !=  AttackTextProcessor.DamageReasons.PRESSURE
				&& damageReason !=  AttackTextProcessor.DamageReasons.RADIATION)
			if(damageReason ==  AttackTextProcessor.DamageReasons.PIERCING)
				damagedFor = max(damage - (totalDefense/2),0);
			else
				damagedFor = max(damage - totalDefense,0);
		else
			damagedFor = damage;
		if((damageReason == ELECTRIC || damageReason == EARTHQUAKE) && character.airborn)
			damagedFor = 0;
		return damagedFor;
	}


	public void equipWeapon(Class<? extends Weapons> targetWeapon) {
		Weapons temp;
		try {temp = targetWeapon.getConstructor(CharacterClasses.class,boolean.class).newInstance(this,true);
		} catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
				 InvocationTargetException | NullPointerException ignored) { print("ERROR: WEAPON DOES NOT EXIST"); return;}
		print(temp.weaponName + " was just equipped");
		if (name.equals(temp.equippableBy) || temp.equippableBy == null) {
			print(temp.equippableBy);
			weapon.destroy();
			weapon = temp;
			reset();
		}
	}

	public void equipShield(Class<? extends Shields> targetShield) {
		Shields temp;
		try {temp = targetShield.getConstructor(CharacterClasses.class,boolean.class).newInstance(this,true);
		} catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
				 InvocationTargetException | NullPointerException ignored) { print("ERROR: SHIELD DOES NOT EXIST"); return;}
		print(temp.shieldName + " was just equipped");
		if (name.equals(temp.equippableBy) || temp.equippableBy == null) {
			print(temp.equippableBy);
			shield.destroy();
			shield = temp;
			reset();
		}
	}

	protected void reset(){
		if (shield == null)
			shield = new Shields.NoShield(this, true);
		if (weapon == null)
			weapon = new Weapons.NoWeapon(this,true);
		refresh();
	}

	public void refresh(){
		totalStatsCalculator();
		if (currentHealth > totalHealth)
			currentHealth = totalHealth;
		if (manaPool > totalMana)
			manaPool = totalMana;
	}

	public final void update(){
		refresh();
		weapon.update();
		shield.update();
		updateOverridable();
		refresh();
	}

	public void abilitiesProcessor(){
		for(int i = 0; i < abilities.size(); i++){
			abilities.get(i).render();
			if(i == 0)
				abilities.get(i).renderKey("B");
			if(i == 1)
				abilities.get(i).renderKey("H");

			if(isDecidingWhatToDo(character)) {
				if(i == 0 && InputHandler.ability1JustPressed())
					abilities.get(i).keybindActivate();
				else if (i == 1 && InputHandler.ability2JustPressed())
					abilities.get(i).keybindActivate();
				else
					abilities.get(i).touchActivate();
			}

		}
	}

	public int[] getAbilitiesCd(){
		int[] cds = new int[abilities.size()];
		for(int i = 0; i < abilities.size(); i++){
			cds[i] = abilities.get(i).cooldownCounter;
		}
		return cds;
	}


	protected void updateOverridable() {}

	public final void turnHasPassed(){
		if (abilities != null)
			for (Ability a : abilities)
				a.updateCooldown();

		manaPool += totalManaPerTurn;
		refresh();
		turnHasPassedOverridable();
		weapon.turnHasPassed();
		shield.turnHasPassed();
	}

	protected void turnHasPassedOverridable() {}

	//Convenience methods
	public final void runHurt(AttackTextProcessor.DamageReasons source){
		onHurt(source);
		weapon.onHurt(source);
		shield.onHurt(source);
		character.conditions.onDamaged(source);
	}

	@SuppressWarnings("all")
	protected void onHurt(AttackTextProcessor.DamageReasons source){}

	public final void runKill(){
		onKill();
		weapon.onKill();
		shield.onKill();
	}

	public void onKill(){}

	public final void runAttack(){
		onAttack();
		weapon.onAttack();
		shield.onAttack();
		character.conditions.onAttack();
	}

	public void onAttack(){}


	public final void runMove(){
		onMove();
		weapon.onMove();
		shield.onMove();
		character.conditions.onMove();
	}

	public void onMove(){}

	public boolean runOnAttackDecided(){
		return onAttackDecided() && weapon.onAttackDecided() && shield.onAttackDecided() && character.conditions.onAttackDecided();
	}

	public boolean onAttackDecided(){return true;}

	public final void runFinalizedTurn(){
		onFinalizedTurn();
	}

	public void onFinalizedTurn(){}

	public void resetClassesState(){}


	public final void destroy(){
		if (!(this instanceof Classless)) {
			getClIns(name).setShield(shield);
			getClIns(name).setWeapon(weapon);
		}
		if(abilities != null && !abilities.isEmpty())
			getClIns(name).setCooldown(getAbilitiesCd());
		standarizedHealth = currentHealth / totalHealth;
		tempDf = tempDefense;
		ClassStoredInformation.ClassInstance.mana = manaPool;
		destroyListener(oVE);
		weapon.destroy();
		shield.destroy();
		destroyOverridable();
		abilities = null;
	}

	// please destroy all listeners this way
	protected void destroyOverridable(){}


}
