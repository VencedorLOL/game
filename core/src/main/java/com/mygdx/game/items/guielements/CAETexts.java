package com.mygdx.game.items.guielements;
@SuppressWarnings("all")
/**
 * Stands for: "Class And Equipment Texts", but it was too long to be convenient
 */
public class CAETexts {


	public enum Classes{
		CLASSLESS( "Default",
				"Default class." +
				"\n HP: 40 - Damage: 20" +
				"\n Movement: 2 (4) - Speed: 2" +
				"\n Range: 1",
				new String[]{"None"}),
		MELEE("Melee",
				"A class that focuses on close-combat and shredding your enemies hitting hard!" +
				"\nDefault stats:" +
				"\n HP: 40 - Damage: 40" +
				"\n Movement: 1.5 (3) - Speed: 2" +
				"\n Range: 1",
				new String[]{"One for All: When activated, you will be able to aim a heavy attack" +
						"that deals x6 your damage."}),
		SPEEDSTER("Speedster","Outrun everything and everyone! Hit as fast as you can imagine!" +
				"\n Default stats:" +
				"\n HP: 40 - Damage: 10" +
				"\n Movement: 3.5 (7) - Speed: 8" +
				"\n Range: 1",
				new String[]{"Even faster: Gain, for that round, one Movement range, one Speed and the ability to aim 7 attacks in that turn!" +
						"This ability doesn't end your turn."}),
		HEALER("Healer","The healer class is albe to redirect the damage inflicted as healing for your teammates!" +
				"\n HP: 40 - Damage: 5" +
				"\n Movement: 1 (2) - Speed: 3" +
				"\n Range: 1",
				new String[]{"Redirect healing: Target any entity. The last targeted entity by this ability gets the healing from your attacks." +
						"This ability can be casted as many times as you want in a turn."}),
		TANK("Tank","Are your teammates taking too much damage? No worries, the tank comes to redirect most of the damage they take!" +
				"\n HP: 80 - Damage: 10" +
				"\n Movement: 1 (2) - Speed: 2" +
				"\n Range: 1",
				new String[]{"Passive: All allies take 20% of the incomming damage, but you take the other 80% instead."}),
		MAGE("Mage","Use mana to destroy your enemies from a safe distance!" +
				"\n HP: 30 - Magic Damage: 20" +
				"\n Mov: 1.5 (3) - Sp: 4 - Mana: 100" +
				"\n Mana Regen: 40 - Range: 5",
				new String[]{"This character uses mana to attack.",
						"Passive: All mana abilities cost half the mana to cast."}),
		SWORD_MAGE("Melee Mage","This melee class uses mana to extend the power of its abilities!" +
				"\n HP: 30 - Damage: 20" +
				"\n Mov: 1.5 (3) - Sp: 6 - Mana: 100" +
				"\n Mana Regen: 50 - Range: 1",
				new String[]{"Magical Infusion: Toggleable. While active, all your attacks deal 125% its normal damage" +
						"and get +2 Range, but they cost x2 times its damage in mana to cast." +
						"This ability can be casted as many times as you want in a turn."}),
		SUMMONER("Summoner","Loney? No worries! With the summoner, you'll have all the friends you'll ever need!" +
				"\n HP: 40 - Damage: 20" +
				"\n Movement: 2.5 (5) - Speed: 6" +
				"\n Range: 1",
				new String[]{"Summon: Summons a summon in the selected tile." ,
						"Heal Summon (only if there are 5 summons in the arena): Heals the most damaged summon and teleports it" +
						"to the selected tile or heals a selected summon." ,
						"Command: Command your summons to move towards a tile, to attack a specific enemy or to follow you " +
						"(this triggers their normal behaviour) This ability can be casted as many times as you want in a turn."}),
		IMP("Imp","The imp will fulfill your darkest desires! If those desires are to either curse enemies or to enhance your allies, of course." +
				"\n HP: 30 - Damage: 40" +
				"\n Movement: 1.5 (3) - Speed: 7" +
				"\n Range: 1",
				new String[]{"Ritual: Gives you and all your allies a x1.33 damage multiplier, 1 Movement, 2 Speed" +
						" and 2 Range for 6 turns. This ability consumes your turn when used." ,
						"Demonize: Select a tile. In your turn, if anything is standing on that tile, it will be demonized," +
						"taking x1.66 the damage, dealing x0.75 the damage, having x0.75 the defense and -0.5 Movement for 6 turns." ,
						"After using either Demonize or Ritual, the other ability will enter in cooldown."}),
		CATAPULT("Catapult","Snipe your enemies! Snipe yourself! With the catapult, no one is safe from your flying rocks!" +
				"\n HP: 30 - Damage: 20 [200]" +
				"\n Movement: 1.5 (3) - Speed: 1" +
				"\n Range: 8 [30]",
				new String[]{"Charge the Catapult: Your next attack will throw a giant rock at the targeted tile." +
						"This rock will take a number of turns to fall. The further you aim, the faster it falls." +
						"These ranges are indicated when selecting the rock. This will take your turn." ,
						"Charge!!: Select a targetable location. Gain 6 Speed. In your turn, you will" +
						"dash towards that tile, stunning any entity you ram in the way, and dealing damage."}),
		STELLAR_EXPLOSION("Stellar Explosion","Channel the power of the stars through yourself! At a price..." +
				"\n HP: 15 - Magic Damage: 125" +
				"\n Mov: 3 (6) - Sp: 6 - Mana: 250" +
				"\n Mana Regen: 50 - Range: 1 [3] - Mana per Use: 300",
				new String[]{"Passive: Your attack, if you have enough mana, will hit all enemies on a 3-tile radius," +
						"dealing massive magic damage." ,
						"Emergency Implosion: When casted, in your turn, you gain +100 Temporal defense." +
						"This ability consumes x2 your maximum mana, but it can be used without any mana requirement." +
						"However, your mana will be negative, not being able to attack until it is regenerated."}),
		EARTHQUAKER("Earthquaker","BROOMMM! Crack the [Planet name]! Be careful not to hurt your allies!" +
				"\n HP: 40 - Magic Damage: 35" +
				"\n Mov: 1 (2) - Sp: 1 - Mana: 150" +
				"\n Mana Regen: 15 - Range: 1 [7]",
				new String[]{"Passive: Your main attack consumes mana and creates an earthquake with radius 7." +
						"The direction of this earthquake is slightly customizable. This earthquake deals magic damage" +
						"and pierces defense, also destroying all Temporal defense."})
		;
		public final String text;
		public final String name;
		public final String[] abilities;
		Classes(String name, String text, String[] abilities){
			this.text = text;
			this.name = name;
			this.abilities = abilities;
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
