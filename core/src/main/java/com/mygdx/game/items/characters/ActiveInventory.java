package com.mygdx.game.items.characters;

import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.Conditions;
import com.mygdx.game.items.characters.equipment.Items;
import com.mygdx.game.items.characters.equipment.items.Backpack;

import java.util.ArrayList;
import java.util.Objects;

import static com.mygdx.game.items.characters.Inventory.OWNERLESS;
@SuppressWarnings("all")
public class ActiveInventory {

	public short totalCapacity;
	public short defaultCapacity;
	public short addedCapacity;
	public short currentCapacity;

	public ArrayList<Items> items = new ArrayList<>();

	public ActiveInventory(){}

	public void calculateCapacity(){
		currentCapacity = 0;
		for(Items i : items)
			currentCapacity += i.space;
		short counter = 10;
		calculateAddedCapacity();
		calculateTotalCapacity();
		while(currentCapacity > totalCapacity && counter > 0) {
			int num = items.size();
			removeItem(items.get(items.size() - 1));
			calculateTotalCapacity();
			if(num==items.size())
				counter--;
		}
	}

	private void calculateTotalCapacity(){
		totalCapacity = (short) (defaultCapacity + addedCapacity);
	}

	private void calculateAddedCapacity(){
		addedCapacity = 0;
		for(Items i : items)
			if(i instanceof Backpack)
				addedCapacity += ((Backpack)i).getAddedSlots();
	}

	public boolean addItem(Items item){
		if(item.space + currentCapacity < totalCapacity) {
			items.add(item);
			calculateCapacity();
			return true;
		}
		return false;
	}

	public short removeItem(Items item){
		short space = items.remove(item) ? item.space : 0;
		currentCapacity -= space;
		if(item instanceof Backpack)
			addedCapacity -= ((Backpack)item).getAddedSlots();
		return space;
	}

	//here comes the fun part

	public float addHealth;
	public float addDamage;
	public float addSpeed;
	public float addActingSpeed;
	public float addDefense;
	public float addRange;
	public float addMana;
	public float addManaPerTurn;
	public float addManaPerUse;
	public float addMagicDamage;
	public float addAggro;
	public float multHealth;
	public float multDamage;
	public float multSpeed;
	public float multActingSpeed;
	public float multDefense;
	public float multRange;
	public float multMana;
	public float multManaPerTurn;
	public float multManaPerUse;
	public float multMagicDamage;
	public float multAggro;

	private void defaultStats(){
		addHealth = 0;
		addDamage = 0;
		addSpeed = 0;
		addActingSpeed = 0;
		addDefense = 0;
		addRange = 0;
		addMana = 0;
		addManaPerTurn = 0;
		addManaPerUse = 0;
		addMagicDamage = 0;
		addAggro = 0;
		multHealth = 1;
		multDamage = 1;
		multSpeed = 1;
		multActingSpeed = 1;
		multDefense = 1;
		multRange = 1;
		multMana = 1;
		multManaPerTurn = 1;
		multManaPerUse = 1;
		multMagicDamage = 1;
		multAggro = 1;
	}

	public void getStats(){
		for(Items i : items){
			addHealth += i.getHealthAdditive();
			addDamage += i.getDamageAdditive();
			addSpeed += i.getSpeedAdditive();
			addActingSpeed += i.getActingSpeedAdditive();
			addDefense += i.getDefenseAdditive();
			addRange += i.getRangeAdditive();
			addMana += i.getManaAdditive();
			addManaPerTurn += i.getManaPerTurnAdditive();
			addManaPerUse += i.getManaPerUseAdditive();
			addMagicDamage += i.getMagicDamageAdditive();
			addAggro += i.getAggroAdditive();
			multHealth *= i.getHealthMultiplier();
			multDamage *= i.getDamageMultiplier();
			multSpeed *= i.getSpeedMultiplier();
			multActingSpeed *= i.getActingSpeedMultiplier();
			multDefense *= i.getDefenseMultiplier();
			multRange *= i.getRangeMultiplier();
			multMana *= i.getManaMultiplier();
			multManaPerTurn *= i.getManaPerTurnMultiplier();
			multManaPerUse *= i.getManaPerUseMultiplier();
			multMagicDamage *= i.getMagicDamageMultiplier();
			multAggro *= i.getAggroMultiplier();
		}
	}

	public float getAddActingSpeed() {return addActingSpeed;}
	public float getAddAggro() {return addAggro;}
	public float getAddDamage() {return addDamage;}
	public float getAddDefense() {return addDefense;}
	public float getAddHealth() {return addHealth;}
	public float getAddMagicDamage() {return addMagicDamage;}
	public float getAddMana() {return addMana;}
	public float getAddManaPerTurn() {return addManaPerTurn;}
	public float getAddManaPerUse() {return addManaPerUse;}
	public float getAddRange() {return addRange;}
	public float getAddSpeed() {return addSpeed;}

	public float getMultActingSpeed() {return multActingSpeed;}
	public float getMultAggro() {return multAggro;}
	public float getMultDamage() {return multDamage;}
	public float getMultDefense() {return multDefense;}
	public float getMultHealth() {return multHealth;}
	public float getMultMagicDamage() {return multMagicDamage;}
	public float getMultMana() {return multMana;}
	public float getMultManaPerTurn() {return multManaPerTurn;}
	public float getMultManaPerUse() {return multManaPerUse;}
	public float getMultRange() {return multRange;}
	public float getMultSpeed() {return multSpeed;}


	public void update(){for(Items i : items) i.update();}

	public void onHurt(AttackTextProcessor.DamageReasons source){for(Items i : items) i.onHurt(source);}

	public void onAttack(){for(Items i : items) i.onAttack();}

	public void onMove(){for(Items i : items) i.onMove();}

	public boolean onAttackDecided(){
		boolean returnTrue = true;
		for(Items i : items)
			if(!i.onAttackDecided())
				returnTrue = false;
		return returnTrue;
	}

	public void turnHasPassed(){for(Items i : items) i.turnHasPassed();}

	public void onKill(){for(Items i : items) i.onKill();}

	public void destroyAll(){for(Items i : items) i.destroy();}

	public enum DifferentInventories{
		ANIMA("Anima",new ActiveInventory());

		public static DifferentInventories belongsInAnActive(Items item){
			for(DifferentInventories a : DifferentInventories.values())
				for(Items i : a.inventory.items)
					if(i == item)
						return a;
			return null;
		}

		public final String name;
		public final ActiveInventory inventory;
		DifferentInventories(String name, ActiveInventory inventory){
			this.name = name;
			if(Objects.equals(name, OWNERLESS))
				throw new Error("Active inventory cannot be named: \"" + OWNERLESS+"\"");
			this.inventory = inventory;
		}

	}


}
