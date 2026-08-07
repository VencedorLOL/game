package com.mygdx.game.items.guielements;

@SuppressWarnings("all")
/**
 * Stands for: "Class And Equipment Texts", but it was too long to be convenient
 */
public class CAETexts {

	// USE THIS CHARACTER FOR WHEN LINE BREAKS ARE DISALLOWED:  

	public enum Classes{
		CLASSLESS( "Default",
				"Default class." +
				"\n HP: 40 - Damage: 20" +
				"\n Movement: 2 (4) - Speed: 2" +
				"\n Range: 1",
				new String[]{"None"}),
		MELEE("Melee",
				"A class that focuses on close-combat and shredding your enemies hitting hard!" +
				"\nDefault stats:" +
				"\nHP: 40 - Damage: 40" +
				"\nMovement: 1.5 (3) - Speed: 2" +
				"\nRange: 1",
				new String[]{"One for All: When activated, you will be able to aim a heavy attack that deals x6 your damage."}),
		SPEEDSTER("Speedster","Outrun everything and everyone! Hit as fast as you can imagine!" +
				"\nDefault stats:" +
				"\nHP: 40 - Damage: 10" +
				"\nMovement: 3.5 (7) - Speed: 8" +
				"\nRange: 1",
				new String[]{"Even faster: Gain, for that round, one Movement range, one Speed and the ability to aim 7 attacks in that turn! This ability doesn't end your turn."}),
		HEALER("Healer","The healer class is able to redirect the damage inflicted as healing for your teammates!" +
				"\nHP: 40 - Damage: 5" +
				"\nMovement: 1 (2) - Speed: 3" +
				"\nRange: 1",
				new String[]{"Redirect healing: Target any entity. The last targeted entity by this ability gets the healing from your attacks. This ability can be casted as many times as you want in a turn."}),
		TANK("Tank","Are your teammates taking too much damage? No worries, the tank comes to redirect most of the damage they take!" +
				"\nHP: 80 - Damage: 10" +
				"\nMovement: 1 (2) - Speed: 2" +
				"\nRange: 1",
				new String[]{"Passive: All allies take 20% of the incomming damage, but you take the other 80% instead."}),
		MAGE("Mage","Use mana to destroy your enemies from a safe distance!" +
				"\nHP: 30 - Magic Damage: 20" +
				"\nMov: 1.5 (3) - Sp: 4 - Mana: 100" +
				"\nMana Regen: 40 - Range: 5",
				new String[]{"This character uses mana to attack. Passive: All mana abilities cost half the mana to cast."}),
		SWORD_MAGE("Melee Mage","This melee class uses mana to extend the power of its abilities!" +
				"\nHP: 30 - Damage: 20" +
				"\nMov: 1.5 (3) - Sp: 6 - Mana: 100" +
				"\nMana Regen: 50 - Range: 1",
				new String[]{"Magical Infusion: Toggleable. While active, all your attacks deal 125% its normal damage and get +2 Range, but they cost x2 times its damage in mana to cast. This ability can be casted as many times as you want in a turn."}),
		SUMMONER("Summoner","Loney? No worries! With the summoner, you'll have all the friends you'll ever need!" +
				"\nHP: 40 - Damage: 20" +
				"\nMovement: 2.5 (5) - Speed: 6" +
				"\nRange: 1",
				new String[]{"Summon: Summons a summon in the selected tile." ,
						"Heal Summon (only if there are 5 summons in the arena): Heals the most damaged summon and teleports it" +
						" to the selected tile or heals a selected summon." ,
						"Command: Command your summons to move towards a tile, to attack a specific enemy or to follow you " +
						" (this triggers their normal behaviour) This ability can be casted as many times as you want in a turn."}),
		IMP("Imp","The imp will fulfill your darkest desires! If those desires are to either curse enemies or to enhance your allies, of course." +
				"\nHP: 30 - Damage: 40" +
				"\nMovement: 1.5 (3) - Speed: 7" +
				"\nRange: 1",
				new String[]{"Ritual: Gives you and all your allies a x1.33 damage multiplier, 1 Movement, 2 Speed" +
						" and 2 Range for 6 turns. This ability consumes your turn when used." ,
						"Demonize: Select a tile. In your turn, if anything is standing on that tile, it will be demonized," +
						" taking x1.66 the damage, dealing x0.75 the damage, having x0.75 the defense and -0.5 Movement for 6 turns." ,
						"After using either Demonize or Ritual, the other ability will enter in cooldown."}),
		CATAPULT("Catapult","Snipe your enemies! Snipe yourself! With the catapult, no one is safe from your flying rocks!" +
				"\nHP: 30 - Damage: 20 [200]" +
				"\nMovement: 1.5 (3) - Speed: 1" +
				"\nRange: 8 [30]",
				new String[]{"Charge the Catapult: Your next attack will throw a giant rock at the targeted tile." +
						" This rock will take a number of turns to fall. The further you aim, the faster it falls." +
						" These ranges are indicated when selecting the rock. This will take your turn." ,
						"Charge!!: Select a targetable location. Gain 6 Speed. In your turn, you will" +
						" dash towards that tile, stunning any entity you ram in the way, and dealing damage."}),
		STELLAR_EXPLOSION("Stellar Explosion","Channel the power of the stars through yourself! At a price..." +
				"\nHP: 15 - Magic Damage: 125" +
				"\nMov: 3 (6) - Sp: 6 - Mana: 250" +
				"\nMana Regen: 50 - Range: 1 [3] - Mana per Use: 300",
				new String[]{"Passive: Your attack, if you have enough mana, will hit all enemies on a 3-tile radius," +
						"dealing massive magic damage." ,
						"Emergency Implosion: When casted, in your turn, you gain +100 Temporal defense." +
						" This ability consumes x2 your maximum mana, but it can be used without any mana requirement." +
						" However, your mana will be negative, not being able to attack until it is regenerated."}),
		EARTHQUAKER("Earthquaker","BROOMMM! Crack the [Planet name]! Be careful not to hurt your allies!" +
				"\nHP: 40 - Magic Damage: 35" +
				"\nMov: 1 (2) - Sp: 1 - Mana: 150" +
				"\nMana Regen: 15 - Range: 1 [7]",
				new String[]{"Passive: Your main attack consumes mana and creates an earthquake with radius 7." +
						" The direction of this earthquake is slightly customizable. This earthquake deals magic damage" +
						" and pierces defense, also destroying all Temporal defense."}),
		TRAPPER( "Trapper",
				"Watch your step! With this class you will trap your enemies." +
						"\n HP: 25 - Damage: 20" +
						"\n Movement: 3 (6) - Speed: 7" +
						"\n Range: 3",
				new String[]{"Passive: Your main attack throws whatever trap you got equipped as weapon."})
		;
		public final String name;
		public final String text;
		public final String[] abilities;
		Classes(String name, String text, String[] abilities){
			this.name = name;
			this.text = text;
			this.abilities = abilities;
		}

	}


	public interface EquipmentUnifier{
		public EquipmentInfo[] getElement();
	}


	public enum Weapons implements EquipmentUnifier{
		MELEE(MeleeWeapons.values()),
		SPEEDSTER(SpeedsterWeapons.values()),
		HEALER(HealerWeapons.values()),
		TANK(TankWeapons.values()),
		MAGE(MageWeapons.values()),
		SWORD_MAGE(SwordMageWeapons.values()),
		SUMMONER(SummonerWeapons.values()),
		IMP(ImpWeapons.values()),
		CATAPULT(CatapultWeapons.values()),
		STELLAR_EXPLOSION(StellarExplosionWeapons.values()),
		EARTHQUAKER(EarthquakerWeapons.values()),
		TRAPPER(TrapperWeapons.values()),;

		public final EquipmentInfo[] weapons;
		Weapons(EquipmentInfo[] weapons){
			this.weapons = weapons;
		}

		public EquipmentInfo[] getElement() {
			return weapons;
		}
	}


	public enum Shields implements EquipmentUnifier{
		MELEE(MeleeShields.values()),
		SPEEDSTER(SpeedsterShields.values()),
		HEALER(HealerShields.values()),
		TANK(TankShields.values()),
		MAGE(MageShields.values()),
		SWORD_MAGE(SwordMageShields.values()),
		SUMMONER(SummonerShields.values()),
		IMP(ImpShields.values()),
		CATAPULT(CatapultShields.values()),
		STELLAR_EXPLOSION(StellarExplosionShields.values()),
		EARTHQUAKER(EarthquakerShields.values()),
		TRAPPER(TrapperShields.values()),;

		public final EquipmentInfo[] shields;
		Shields(EquipmentInfo[] shields){
			this.shields = shields;
		}


		public EquipmentInfo[] getElement() {
			return shields;
		}

	}


	public interface EquipmentInfo{
		public String getText();
		public String getName();
	}

	public enum MeleeWeapons implements EquipmentInfo {
		aBat("Strike your enemies!" +
				"\n +40 Damage" +
				"\n +1 Range",
				"A Bat");
		public final String text;
		public final String name;
		MeleeWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum MeleeShields implements EquipmentInfo{
		WoodShield("Well, this wooden makeshift of a shield will at least block some attacks..." +
				"\n +20 HP" +
				"\n +1 Defense",
				"Wood Shield");
		public final String text;
		public final String name;
		MeleeShields(String text, String name){
			this.text = text;
			this.name = name;
		}
		public String getText(){return text;}
		public String getName(){return name;}
	}

	public enum SpeedsterWeapons implements EquipmentInfo {
		Knife("Stab! Stab! Stab!" +
				"\n +10 Damage",
				"Knife");
		public final String text;
		public final String name;
		SpeedsterWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}
		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum SpeedsterShields implements EquipmentInfo {
		InsignificantShield("...... Ok....." +
				"\n +10 HP" +
				"\n +0.5 Defense",
				"Insignificant Shield");
		public final String text;
		public final String name;
		SpeedsterShields(String text, String name){
			this.text = text;
			this.name = name;
		}
		public String getText(){return text;}
		public String getName(){return name;}
	}

	public enum HealerWeapons implements EquipmentInfo {
		BlessedStick("Holy Heal!" +
				"\n +10 Damage" +
				"\n +1 Range",
				"Blessed Stick"),
		HolySword("Healing Crusade!!" +
				"\n +100 Damage" +
				"\n + 1 Range" +
				"\n x6 Healing!!",
				"Holy Sword");
		public final String text;
		public final String name;
		HealerWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}
		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum HealerShields implements EquipmentInfo {
		BlessedShield("Divine Defense" +
				"\n +30 HP" +
				"\n +5 Health Regeneration",
				"Blessed Shield");
		public final String text;
		public final String name;
		HealerShields(String text, String name){
			this.text = text;
			this.name = name;
		}
		public String getText(){return text;}
		public String getName(){return name;}
	}

	public enum TankWeapons implements EquipmentInfo {
		BulkyStone("The best defense is a good defense! With some offensive, of course!" +
				"\n +10 HP" +
				"\n +15 Damage" +
				"\n +1 Defense" +
				"\n +1 Range",
				"Bulky Stone");
		public final String text;
		public final String name;
		TankWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}
		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum TankShields implements EquipmentInfo {
		Shield("Reduces all incoming damage, except redirected damage, by 20%!" +
				"\n +30 HP" +
				"\n +3 Defense",
				"Shield");
		public final String text;
		public final String name;
		TankShields(String text, String name){
			this.text = text;
			this.name = name;
		}
		public String getText(){return text;}
		public String getName(){return name;}
	}

	public enum MageWeapons implements EquipmentInfo {
		MakeshiftWand("It's made out of plastic??!!" +
				"\n +5 Damage" +
				"\n +5 Range" +
				"\n +30 Magic Damage" +
				"\n Mana Per Use: 150 (Halved due to Mage's innate ability)",
				"Makeshift Wand");
		public final String text;
		public final String name;
		MageWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}
		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum MageShields implements EquipmentInfo {
		RandomCrystal("Is this it?? Just a random crystal??" +
				"\n +75 Mana" +
				"\n +20 Magic Damage" +
				"\n +15 Mana Regeneration",
				"Random Crystal");
		public final String text;
		public final String name;
		MageShields(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}

	public enum SwordMageWeapons implements EquipmentInfo {
		HardWand("It's made out of metal!" +
				"\n +30 Damage" +
				"\n +1 Range" +
				"\n +10 Mana" +
				"\n +0.5 Ability Damage (This is added to the ability damage multiplicator)",
				"Hard Wand");
		public final String text;
		public final String name;
		SwordMageWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum SwordMageShields implements EquipmentInfo {
		CrystalizedShield("It greately reduces the cost of your ability!" +
				"\n +30 HP" +
				"\n +1 Defense" +
				"\n +140 Mana" +
				"\n +15 Mana Regeneration" +
				"\n -0.75 Ability Cost (This is added to the ability cost multiplicator)",
				"Crystalized Shield");
		public final String text;
		public final String name;
		SwordMageShields(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum SummonerWeapons implements EquipmentInfo {
		Instrument("Attack, summons!!" +
				"\n +20 Damage" +
				"\n +1 Range" +
				"\n This weapon loses 10 damage if there's any alive summon, but your summons gain +10 Damage!",
				"Instrument");
		public final String text;
		public final String name;
		SummonerWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum SummonerShields implements EquipmentInfo {
		FlagOfTheLeader("Follow the leader!" +
				"\n +15 HP" +
				"\n +1 Defense" +
				"\n This weapon gains 1 defense per alive summon!",
				"Flag Of The Leader");
		public final String text;
		public final String name;
		SummonerShields(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}

	public enum ImpWeapons implements EquipmentInfo {
		DevilishDagger("Demonic! This weapon pierces either 25% of a demonized objective's defense or 5 defense, whatever is bigger!" +
				"\n +12 Damage" +
				"\n +1 Range",
				"Devilish Dagger"),
		LightDagger("This very light dagger gives everyone under the ritual 1.3 (3) movement and 3 speed!" +
				"\n +12 Damage" +
				"\n +2 Movement" +
				"\n +1 Range",
				"Light Dagger"),
		MassDemonizeDagger("This dagger uses the ritual's power to be able to inflict demonize on every single enemy." +
				"\n +60 Damage" +
				"\n +1 Range",
				"Mass Demonize Dagger");
		public final String text;
		public final String name;
		ImpWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum ImpShields implements EquipmentInfo {
		RitualShield("This shield gains 3 defense if under the ritual." +
				"\n +22 HP",
				"Ritual Shield"),
		DemonicShield("This shield gains 1 defense per demonized being." +
				"\n +40 HP",
				"Demonic Shield"),
		DarkWings("They are beautiful... The user is airborn, and so is everyone under the ritual! They also gain +1.5 (3) movement!" +
				"\n +66 HP",
				"Dark Wings"),
		Daredevil("A trinket filled with malevolent energy." +
				"\nWhen used, this shield loses most of its HP and defense, but gives the user and its allies permanent Ritual status." +
				"\n +333 HP" +
				"\n +33 Defense",
				"Daredevil");
		public final String text;
		public final String name;
		ImpShields(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}

	public enum CatapultWeapons implements EquipmentInfo {
		Rock("It's not a stone, it's a rock!","Rock"),
		RockOnFire("With the power of... magic, we managed to light the rock ablaze!" +
				"\n +1 Damage" +
				"\n Leaves a fire where it lands.","Rock on Fire"),
		HomingRock("This SmartRock will aim for you! Just don't aim yourself." +
				"\n +10 Damage" +
				"\n Takes ALWAYS 5 turns to fall.","Homing Rock"),
		ClusterRock("Cluster Rock not only rocks your enemies, it also rocks your rocks. And yourself, if you're not careful." +
				"\n +50 Damage","Cluster Rock");
		public final String text;
		public final String name;
		CatapultWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum CatapultShields implements EquipmentInfo {
		MetalBucket("For better throwing." +
				"\n +10 HP" +
				"\n +5 Damage" +
				"\n +5 Defense","Metal Bucket");
		public final String text;
		public final String name;
		CatapultShields(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}

	public enum StellarExplosionWeapons implements EquipmentInfo {
		EnergyCondensator("Will give you 5 temporal defense per enemy hit. Max. 50." +
				"\n +75 Magic Damage" +
				"\n +25 Mana Regeneration","Energy Condensator");
		public final String text;
		public final String name;
		StellarExplosionWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum StellarExplosionShields implements EquipmentInfo{
		EnergyAccelerator("It just accelerates the rate in which you gain energy, not yourself." +
				"\n +5 Defense" +
				"\n +50 Mana" +
				"\n +25 Mana Regeneration","Energy Accelerator");
		public final String text;
		public final String name;
		StellarExplosionShields(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}

	public enum EarthquakerWeapons implements EquipmentInfo{
		GroundStomper("Use directly above you for +30 Magic Damage." +
				"\n +5 Damage" +
				"\n +1 Range" +
				"\n +50 Mana" +
				"\n +15 Magic Damage","Ground Stomper");
		public final String text;
		public final String name;
		EarthquakerWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum EarthquakerShields implements EquipmentInfo{
		StablePlatform("This platform protects your allies in a radius of 7 tiles from your attacks." +
				"\n +20 HP" +
				"\n +25 Mana Regeneration",
				"Stable Platform");
		public final String text;
		public final String name;
		EarthquakerShields(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum TrapperWeapons implements EquipmentInfo {
		pStones("Stones may hurt their bones! Only twice tho." +
				"\n +10 Damage",
				"Pricky Stones"),
		Thorns("Spiky",
				"Thorns");
		public final String text;
		public final String name;
		TrapperWeapons(String text, String name){
			this.text = text;
			this.name = name;
		}

		public String getText(){return text;}
		public String getName(){return name;}
	}


	public enum TrapperShields implements EquipmentInfo{
		WoodShield("Temporal description for a temporal item" +
				"\n +20 HP iirc?" +
				"\n +1 Defense",
				"Idc this is just a temporal name really");
		public final String text;
		public final String name;
		TrapperShields(String text, String name){
			this.text = text;
			this.name = name;
		}
		public String getText(){return text;}
		public String getName(){return name;}
	}
















}
