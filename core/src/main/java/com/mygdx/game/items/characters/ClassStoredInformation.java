package com.mygdx.game.items.characters;

import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;

import java.lang.reflect.InvocationTargetException;

public class ClassStoredInformation {


	public static class ClassInstance {
		public String weaponName;
		public String shieldName;
		public Class<?> weaponClass;
		public Class<?> shieldClass;
		public int[] cooldownsState;

		static ClassInstance[] classes;
		static{
			//ignore the error, ironically the same Java static characteristic I am trying to avoid prevents the recursive softlock
			classes = new ClassInstance[]{
					new Melee(), new Speedster(), new Mage() ,new Tank() ,new Healer(), new SwordMage(), new Summoner(), new Imp(), new Catapult(), new StellarExplosion(),
					new Earthquaker()
			};

		}

		public static ClassInstance getClIns(String name){
			for(ClassInstance c : classes){
				if(c.getClass().getSimpleName().equals(name))
					return c;
			}
			return null;
		}

		public void setWeapon(Weapons weapon) {
			weaponName = weapon.weaponName;
			weaponClass = weapon.getClass();
		}
		public void setShield(Shields shield){
			shieldName = shield.shieldName;
			shieldClass = shield.getClass();
		}
		public Class<? extends Weapons> getWeapon(){
			return (Class<? extends Weapons>) weaponClass;
		}
		public Class<? extends Shields> getShield(){
			return (Class<? extends Shields>) shieldClass;
		}
		public String getWeaponName(){return weaponName;}
		public String getShieldName(){return shieldName;}
		public void setCooldown(int... cooldowns){cooldownsState = cooldowns;}
		public int[] getCooldown(){if(cooldownsState != null) return cooldownsState; else return new int[]{0};}

	}

	public static class Melee extends ClassInstance{}

	public static class Speedster extends ClassInstance{}

	public static class Healer extends ClassInstance{}

	public static class Tank extends ClassInstance{}

	public static class Mage extends ClassInstance{}

	public static class SwordMage extends ClassInstance{}

	public static class Summoner extends ClassInstance{}

	public static class Imp extends ClassInstance{}

	public static class Catapult extends ClassInstance{}

	public static class StellarExplosion extends ClassInstance{}

	public static class Earthquaker extends ClassInstance{}


}
