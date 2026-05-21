package com.mygdx.game.items.guielements;
@SuppressWarnings("all")
/**
 * Stands for: "Class And Equipment Texts", but it was too long to be convenient
 */
public class CAETexts {


	public enum Classes{
		CLASSLESS("Default class." +
				"\n HP: 40 - Damage: 20" +
				"\n Movement: 2 (4) - Speed: 2" +
				"\n Range: 1"),
		MELEE("A class that focuses on close-combat and shredding your enemies hitting hard!" +
				"\nDefault stats:" +
				"\n HP: 40 - Damage: 40" +
				"\n Movement: 1.5 (3) - Speed: 2" +
				"\n Range: 1"),
		SPEEDSTER("Outrun everything and everyone! Hit as fast as you can imagine!" +
				"\n Default stats:" +
				"\n HP: 40 - Damage: 10" +
				"\n Movement: 3.5 (7) - Speed: 8" +
				"\n Range: 1"),
		HEALER("The healer class is albe to redirect the damage inflicted as healing for your teammates!" +
				"\n HP: 40 - Damage: 5" +
				"\n Movement: 1 (2) - Speed: 3" +
				"\n Range: 1"),
		TANK("Are your teammates taking too much damage? No worries, the tank comes to redirect most of the damage they take!" +
				"\n HP: 80 - Damage: 10" +
				"\n Movement: 1 (2) - Speed: 2" +
				"\n Range: 1"),
		MAGE("Use mana to destroy your enemies from a safe distance!" +
				"\n HP: 30 - Magic Damage: 20" +
				"\n Mov: 1.5 (3) - Sp: 4 - Mana: 100" +
				"\n Mana Regen: 40 - Range: 5"),
		SWORD_MAGE("This melee class uses mana to extend the power of its abilities!" +
				"\n HP: 30 - Damage: 20" +
				"\n Mov: 1.5 (3) - Sp: 6 - Mana: 100" +
				"\n Mana Regen: 50 - Range: 1"),
		SUMMONER("Loney? No worries! With the summoner, you'll have all the friends you'll ever need!" +
				"\n HP: 40 - Damage: 20" +
				"\n Movement: 2.5 (5) - Speed: 6" +
				"\n Range: 1"),
		IMP("The imp will fulfill your darkest desires! If those desires are to either curse enemies or to enhance your allies, of course." +
				"\n HP: 30 - Damage: 40" +
				"\n Movement: 1.5 (3) - Speed: 7" +
				"\n Range: 1"),
		CATAPULT("Snipe your enemies! Snipe yourself! With the catapult, no one is safe from your flying rocks!" +
				"\n HP: 30 - Damage: 20 [200]" +
				"\n Movement: 1.5 (3) - Speed: 1" +
				"\n Range: 8 [30]"),
		STELLAR_EXPLOSION("Channel the power of the stars through yourself! At a price..." +
				"\n HP: 15 - Magic Damage: 125" +
				"\n Mov: 3 (6) - Sp: 6 - Mana: 250" +
				"\n Mana Regen: 50 - Range: 1 [3] - Mana per Use: 300"),
		EARTHQUAKER("BROOMMM! Crack the [Planet name]! Be careful not to hurt your allies!" +
				"\n HP: 40 - Magic Damage: 35" +
				"\n Mov: 1 (2) - Sp: 1 - Mana: 150" +
				"\n Mana Regen: 15 - Range: 1 [7]")
		;
		public final String text;
		Classes(String text){
			this.text = text;
		}

	}


	public enum MeleeWeapons{
		aBat("Strike your enemies!" +
				"\n +40 Damage" +
				"\n +1 Range");
		public final String text;
		MeleeWeapons(String text){
			this.text = text;
		}
	}


	public enum MeleeShields{
		WoodShield("Well, this wooden makeshift of a shield will at least block some attacks..." +
				"\n +20 HP" +
				"\n +1 Defense");
		public final String text;
		MeleeShields(String text){
			this.text = text;
		}
	}

	public enum SpeedsterWeapons{
		Knife("Stab! Stab! Stab!" +
				"\n +10 Damage");
		public final String text;
		SpeedsterWeapons(String text){
			this.text = text;
		}
	}


	public enum SpeedsterShields{
		InsignificantShield("...... Ok....." +
				"\n +10 HP" +
				"\n +0.5 Defense");
		public final String text;
		SpeedsterShields(String text){
			this.text = text;
		}
	}

	public enum HealerWeapons {
		BlessedStick("Holy Heal!" +
				"\n +10 Damage" +
				"\n +1 Range"),
		HolySword("Healing Crusade!!" +
				"\n +100 Damage" +
				"\n + 1 Range" +
				"\n x6 Healing!!");
		public final String text;
		HealerWeapons(String text){
			this.text = text;
		}
	}


	public enum HealerShields {
		BlessedShield("Divine Defense" +
				"\n +30 HP" +
				"\n +5 Health Regeneration");
		public final String text;
		HealerShields(String text){
			this.text = text;
		}
	}

	public enum TankWeapons {
		BulkyStone("The best defense is a good defense! With some offensive, of course!" +
				"\n +10 HP" +
				"\n +15 Damage" +
				"\n +1 Defense" +
				"\n +1 Range");
		public final String text;
		TankWeapons(String text){
			this.text = text;
		}
	}


	public enum TankShields {
		Shield("Reduces all incoming damage, except redirected damage, by 20%!" +
				"\n +30 HP" +
				"\n +3 Defense");
		public final String text;
		TankShields(String text){
			this.text = text;
		}
	}

	public enum MageWeapons {
		MakeshiftWand("It's made out of plastic??!!" +
				"\n +5 Damage" +
				"\n +5 Range" +
				"\n +30 Magic Damage" +
				"\n Mana Per Use: 150 (Halved due to Mage's innate ability)");
		public final String text;
		MageWeapons(String text){
			this.text = text;
		}
	}


	public enum MageShields {
		RandomCrystal("Is this it?? Just a random crystal??" +
				"\n +75 Mana" +
				"\n +20 Magic Damage" +
				"\n +15 Mana Regeneration");
		public final String text;
		MageShields(String text){
			this.text = text;
		}
	}

	public enum SwordMageWeapons {
		HardWand("It's made out of metal!" +
				"\n +30 Damage" +
				"\n +1 Range" +
				"\n +10 Mana" +
				"\n +0.5 Ability Damage (This is added to the ability damage multiplicator)");
		public final String text;
		SwordMageWeapons(String text){
			this.text = text;
		}
	}


	public enum SwordMageShields {
		CrystalizedShield("It greately reduces the cost of your ability!" +
				"\n +30 HP" +
				"\n +1 Defense" +
				"\n +140 Mana" +
				"\n +15 Mana Regeneration" +
				"\n -0.75 Ability Cost (This is added to the ability cost multiplicator)");
		public final String text;
		SwordMageShields(String text){
			this.text = text;
		}
	}


	public enum SummonerWeapons {
		Instrument("Attack, summons!!" +
				"\n +20 Damage" +
				"\n +1 Range" +
				"\n This weapon loses 10 damage if there's any alive summon, but your summons gain +10 Damage!");
		public final String text;
		SummonerWeapons(String text){
			this.text = text;
		}
	}


	public enum SummonerShields {
		FlagOfTheLeader("Follow the leader!" +
				"\n +15 HP" +
				"\n +1 Defense" +
				"\n This weapon gains 1 defense per alive summon!");
		public final String text;
		SummonerShields(String text){
			this.text = text;
		}
	}

	public enum ImpWeapons {
		DevilishDagger("Demonic! This weapon pierces either 25% of a demonized objective's defense or 5 defense, whatever is bigger!" +
				"\n +12 Damage" +
				"\n +1 Range"),
		LightDagger("This very light dagger gives everyone under the ritual 1.3 (3) movement and 3 speed!" +
				"\n +12 Damage" +
				"\n +2 Movement" +
				"\n +1 Range"),
		MassDemonizeDagger("This dagger uses the ritual's power to be able to inflict demonize on every single enemy." +
				"\n +60 Damage" +
				"\n +1 Range");
		public final String text;
		ImpWeapons(String text){
			this.text = text;
		}
	}


	public enum ImpShields {
		RitualShield("This shield gains 3 defense if under the ritual." +
				"\n +22 HP"),
		DemonicShield("This shield gains 1 defense per demonized being." +
				"\n +40 HP"),
		DarkWings("They are beautiful... The user is airborn, and so is everyone under the ritual! They also gain +1.5 (3) movement!" +
				"\n +66 HP"),
		Daredevil("A trinket filled with malevolent energy." +
				"\nWhen used, this shield loses most of its HP and defense, but gives the user and its allies permanent Ritual status." +
				"\n +333 HP" +
				"\n +33 Defense");
		public final String text;
		ImpShields(String text){
			this.text = text;
		}
	}

	public enum CatapultWeapons {
		Rock("It's not a stone, it's a rock!"),
		RockOnFire("With the power of... magic, we managed to light the rock ablaze!" +
				"\n +1 Damage" +
				"\n Leaves a fire where it lands."),
		HomingRock("This SmartRock will aim for you! Just don't aim yourself." +
				"\n +10 Damage" +
				"\n Takes ALWAYS 5 turns to fall."),
		ClusterRock("Cluster Rock not only rocks your enemies, it also rocks your rocks. And yourself, if you're not careful." +
				"\n +50 Damage");
		public final String text;
		CatapultWeapons(String text){
			this.text = text;
		}
	}


	public enum CatapultShields {
		MetalBucket("For better throwing." +
				"\n +10 HP" +
				"\n +5 Damage" +
				"\n +5 Defense");
		public final String text;
		CatapultShields(String text){
			this.text = text;
		}
	}

	public enum StellarExplosionWeapons {
		EnergyCondensator("Will give you 5 temporal defense per enemy hit. Max. 50." +
				"\n +75 Magic Damage" +
				"\n +25 Mana Regeneration");
		public final String text;
		StellarExplosionWeapons(String text){
			this.text = text;
		}
	}


	public enum StellarExplosionShields {
		EnergyAccelerator("It just accelerates the rate in which you gain energy, not yourself." +
				"\n +5 Defense" +
				"\n +50 Mana" +
				"\n +25 Mana Regeneration");
		public final String text;
		StellarExplosionShields(String text){
			this.text = text;
		}
	}

	public enum EarthquakerWeapons {
		GroundStomper("Use directly above you for +30 Magic Damage." +
				"\n +5 Damage" +
				"\n +1 Range" +
				"\n +50 Mana" +
				"\n +15 Magic Damage");
		public final String text;
		EarthquakerWeapons(String text){
			this.text = text;
		}
	}


	public enum EarthquakerShields {
		StablePlatform("This platform protects your allies in a radius of 7 tiles from your attacks." +
				"\n +20 HP" +
				"\n +25 Mana Regeneration");
		public final String text;
		EarthquakerShields(String text){
			this.text = text;
		}
	}
}
