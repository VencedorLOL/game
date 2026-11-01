package com.mygdx.game.items.characters.equipment;

import com.mygdx.game.items.AttackTextProcessor;
import com.mygdx.game.items.characters.ActiveInventory;
import com.mygdx.game.items.characters.equipment.items.Backpack;

import java.lang.reflect.InvocationTargetException;

import static com.mygdx.game.items.characters.Inventory.globalInventory;

@SuppressWarnings("all")
public abstract class Items {
	public short space;
	public ActiveInventory active;

	public Items(short space){
		this.space = space;
	}

	/**
	 * IMPORTANT: If this method isn't called whenever a stat is updated, then the stat change won't get reflected on whomever is reading the stats.
	 */
	public void updateStats(){
		active.getStats();
	}

	public void setActive(ActiveInventory active) {
		this.active = active;
	}

	public void removeActive(){
		active = null;
	}

	public float getHealthMultiplier(){return 1;}
	public float getHealthAdditive(){return 0;}
	public float getDamageMultiplier(){return 1;}
	public float getDamageAdditive(){return 0;}
	public float getSpeedMultiplier(){return 1;}
	public float getSpeedAdditive(){return 0;}
	public float getActingSpeedMultiplier(){return 1;}
	public float getActingSpeedAdditive(){return 0;}
	public float getDefenseMultiplier(){return 1;}
	public float getDefenseAdditive(){return 0;}
	public float getRangeMultiplier(){return 1;}
	public float getRangeAdditive(){return 0;}
	public float getManaMultiplier(){return 1;}
	public float getManaAdditive(){return 0;}
	public float getManaPerTurnMultiplier(){return 1;}
	public float getManaPerTurnAdditive(){return 0;}
	public float getManaPerUseMultiplier(){return 1;}
	public float getManaPerUseAdditive(){return 0;}
	public float getMagicDamageMultiplier(){return 1;}
	public float getMagicDamageAdditive(){return 0;}
	public float getAggroMultiplier(){return 1;}
	public float getAggroAdditive(){return 0;}

	public void update() {}

	public void onHurt(AttackTextProcessor.DamageReasons source){}

	public void onAttack(){}

	public void onMove(){}

	public boolean onAttackDecided(){return true;}

	public void turnHasPassed(){}

	public void onKill() {}

	public final void destroy(){destroyOverridable();}

	public void destroyOverridable(){}

	public void activeAbility(){}

	public enum ItemTypes{
		BACKPACK("Backpack", Backpack.class);

		public Items createItem(){
			try {
				return (Items) classs.getConstructor().newInstance();
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
					 InstantiationException e) {
				throw new RuntimeException("coudnt get like a class or smth here details prob caused cos a typo or like i made a constructor with more than 0 arguments for an item but like they cant so yeah check that out its probably that: ",e);
			}
		}

		public Class<?> getClasss() {
			return classs;
		}

		public String getName() {
			return string;
		}

		final String string;
		final Class<?> classs;
		ItemTypes(String string, Class<?> classs){
			this.string = string;
			this.classs = classs;
		}

	}



}
