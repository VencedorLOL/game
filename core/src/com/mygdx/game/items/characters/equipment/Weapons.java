package com.mygdx.game.items.characters.equipment;

public class Weapons {

	public Class[] WeaponCounter() throws ClassNotFoundException {
		Class[] classCollector = Weapons.class.getClasses();
		return classCollector;
	}

	public static class BlessedSword{
		public String weaponName = "BlessedSword";
		public double weaponHealth = 0;
		public double weaponDamage = 10;
		public byte weaponSpeed = 0;
		public byte weaponAttackSpeed = 0;
		public double weaponDefense = 0;
		public int weaponRange = 0;
		public double weaponHealingAbilityBonus = 2;

		public String equipableBy = "Healer";

		public boolean isEquipped = false;

		public boolean isOnInventory = true;

	}

	public static class BestSword{
		public String weaponName = "BestSword";
		public double weaponHealth = 0;
		public double weaponDamage = 100;
		public byte weaponSpeed = 0;
		public byte weaponAttackSpeed = 0;
		public double weaponDefense = 0;
		public int weaponRange = 0;
		public double weaponHealingAbilityBonus = 6;

		public String equipableBy = "Healer";

		public boolean isEquipped = false;

		public boolean isOnInventory = true;

	}
}
