package com.mygdx.game.items.characters.equipment;

public class Shields{
	public Class[] ShieldCounter() throws ClassNotFoundException {
		Class[] classCollector = Shields.class.getClasses();
		return classCollector;
	}

	public static class BlessedShield{
		public String shieldName = "BlessedShield";
		public double shieldHealth = 30;
		public double shieldDamage = 0;
		public byte shieldSpeed = 0;
		public byte shieldAttackSpeed = 0;
		public double shieldDefense = 10;
		public int shieldRange = 0;
		public double shieldHealingPerTurn = 5;

		public String equipableBy = "Healer";

		public boolean isEquipped = false;

		public boolean isOnInventory = true;

	}


}
