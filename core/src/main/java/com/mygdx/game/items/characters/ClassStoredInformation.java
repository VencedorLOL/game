package com.mygdx.game.items.characters;

import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;


public class ClassStoredInformation {


	@SuppressWarnings("all")
	public static class ClassInstance {
		public static float mana;
		public static float standarizedHealth;
		public static float tempDf;

		public String weaponName;
		public String shieldName;
		public Class<?> weaponClass;
		public Class<?> shieldClass;
		public int[] cooldownsState;

		public static ClassInstance[] classes;

		static{
			classes = new ClassInstance[]{
					new Classless(), new Melee(), new Speedster(), new Healer() ,new Tank() ,new Mage(), new SwordMage(), new Summoner(), new Imp(), new Catapult(), new StellarExplosion(),
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
 			if(weapon != null) {
				weaponName = weapon.weaponName;
				weaponClass = weapon.getClass();
			}
		}
		public void setShield(Shields shield){
			if(shield != null) {
				shieldName = shield.shieldName;
				shieldClass = shield.getClass();
			}
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

	public static class Classless extends ClassInstance{}

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
