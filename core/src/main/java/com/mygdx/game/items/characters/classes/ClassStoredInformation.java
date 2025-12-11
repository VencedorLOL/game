package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;

public class ClassStoredInformation {


	public static class ClassInstance {
		static Weapons currentWeapon;
		static Shields currentShield;
		static int[] cooldownsState;

		public static void setWeapon(Weapons weapon){currentWeapon = weapon;}
		public static void setShield(Shields shield){currentShield = shield;}
		public static Weapons getWeapon(){return currentWeapon;}
		public static Shields getShield(){return currentShield;}
		public static void setCooldown(int... cooldowns){cooldownsState = cooldowns;}
		public static int[] getCooldown(){return cooldownsState;}

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
