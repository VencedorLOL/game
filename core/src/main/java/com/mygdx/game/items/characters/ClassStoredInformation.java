package com.mygdx.game.items.characters;

import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;

import java.lang.reflect.InvocationTargetException;

public class ClassStoredInformation {


	public static class ClassInstance {
		public String weaponName;
		public String shieldName;
		public Weapons currentWeapon;
		public Shields currentShield;
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
		// these.. complications are so weapons/shields are instantiated ON USE instead of on set AND so the instance is conserved between class changes
		// (although i might not want that?? I have to have this in account for future on turn change weapon/shield effects)
		// Permanent TODO: check this (explained above)
		// Second TODO: maybe let this task up to equipWeapon()/equipShield()??
		//         DEF GET THE TASK UP TO equipWeapon()/equipShield().
		public void setWeapon(Weapons weapon) {
			if (currentWeapon != null && !currentWeapon.weaponName.equals(weapon.weaponName))
				currentWeapon = null;
			weaponName = weapon.weaponName;
			weaponClass = weapon.getClass();
		}
		public void setShield(Shields shield){
			if (currentShield != null && !currentShield.shieldName.equals(shield.shieldName))
				currentShield = null;
			shieldName = shield.shieldName;
			shieldClass = shield.getClass();
		}
		public Weapons getWeapon(CharacterClasses character){
			try {
				if(currentWeapon == null){
					currentWeapon = (Weapons) weaponClass.getConstructor(CharacterClasses.class,boolean.class).newInstance(character,true);
				}
				return currentWeapon;
			} catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException ignored) {return null;}
		}
		public Shields getShield(CharacterClasses character){
			try {
				if(currentShield == null){
					currentShield = (Shields) shieldClass.getConstructor(CharacterClasses.class,boolean.class).newInstance(character,true);
				}
				return currentShield;
			} catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException ignored) {return null;}
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
