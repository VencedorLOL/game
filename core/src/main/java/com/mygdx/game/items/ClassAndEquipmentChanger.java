package com.mygdx.game.items;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;
import com.mygdx.game.items.characters.equipment.shields.*;
import com.mygdx.game.items.characters.equipment.weapons.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.mygdx.game.GlobalVariables.classSlots;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.ClickDetector.authenticClick;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.TextureManager.addToList;
import static com.mygdx.game.items.TextureManager.text;

public class ClassAndEquipmentChanger {

	Character character;
	float x,y;
	int selectedSlot = -1;
	boolean renderSelected = false;

	public ClassAndEquipmentChanger(Character character){
		this.character = character;
		this.x = character.x + globalSize()/2f - globalSize()/4f * (classSlots.length+1);
		this.y = character.y + globalSize()*3/2f;
		for(int i = 0; i < classSlots.length; i++){
			touchedIn[i] = false;
			touchedOut[i] = false;
		}
	}

	public void render(){
		this.x = character.x + globalSize()/2f - globalSize()/4f * (classSlots.length+1);
		this.y = character.y + globalSize()*3/2f;
		addToList("SelectionZero",x+ globalSize()/8f,y,1,0,255,255,255,2,2);
		if(selectedSlot == -1)
			addToList("HoveringSelection", x + globalSize() / 8f, y, 1, 0, 255, 255, 255, 2, 2);
		for (int i = 0; i < classSlots.length; i++){
			addToList("SelectionBox",x+globalSize()/2f*(i+1) + globalSize()/8f,y,1,0,255,255,255,2,2);
			addToList(classSlots[i].texture,x+globalSize()/2f*(i+1) + globalSize()/8f,y,1,0,255,255,255,2,2);
			if(isBeingHovered(x+globalSize()/2f*(i+1) + globalSize()/8f,y,globalSize()/2f,globalSize()/2f) || (selectedSlot == i && renderSelected)) {
				addToList("HoveringSelection", x + globalSize() / 2f * (i + 1) + globalSize() / 8f, y, 1, 0, 255, 255, 255, 2, 2);
				selectedSlot = i;
			}
			if(isBeingPressed(x+globalSize()/2f*(i+1) + globalSize()/8f,y,globalSize()/2f,globalSize()/2f,i) || (actionConfirmJustPressed() && selectedSlot == i)) {
				character.onCharacterChange(i);
				selectedSlot = -1;
			}

		}
	}

	boolean[] touchedIn = new boolean[classSlots.length], touchedOut = new boolean[classSlots.length];
	public boolean isBeingPressed(float x, float y, float base, float height,int i){
		Vector3 vector = authenticClick();
		boolean hit = vector.x >= x && vector.x <=  x + base && vector.y >= y && vector.y <=  y + height;
		if(leftClickJustPressed()) {
			touchedIn[i] = hit;
		}if(leftClickReleased()) {
			touchedOut[i] = hit;
			print("touchedOut is now  " + hit);
		}
		if(touchedIn[i] && touchedOut[i]){
			touchedIn[i] = false; touchedOut[i] = false; return true;
		}
		return false;

	}

	public boolean isBeingHovered(float x, float y, float base, float height){
		Vector3 vector = authenticClick();
		if(cursorMoved() && vector.x >= x && vector.x <= x + base && vector.y >= y && vector.y <= y + height)
			renderSelected = false;
		keyboardHover();
		return vector.x >= x && vector.x <= x + base && vector.y >= y && vector.y <= y + height;
	}

	int cooldown;
	public void keyboardHover(){
		if((upJustPressed() || rightJustPressed()) && selectedSlot + 1 < classSlots.length && cooldown <= 0){
			selectedSlot++;
			renderSelected = true;
			cooldown = 20;
		} if ((leftJustPressed() || downJustPressed()) && selectedSlot > 0 && cooldown <= 0){
			selectedSlot--;
			renderSelected = true;
			cooldown = 20;
		}
		cooldown -= cooldown > 0 ? 1 : 0;
	}


	public void activate(int pos){
		classSlots[pos].activate(character);
	}

	@SuppressWarnings("all")
	public static class ClassObject{
		public String name;
		public String texture;
		ArrayList weaponer = new ArrayList<Weaponer<?>>();
		ArrayList shielder = new ArrayList<Shielder<?>>();


		public void activate(Character character){}

		public Weapons getWeapon(int numberOWeapon,Character character){
			if(weaponer.isEmpty() || shielder.isEmpty())
				helperMaker();
			return ((Weaponer<?>) weaponer.get(numberOWeapon)).getWeapon(character);
		}

		public Shields getShield(int numberOWeapon,Character character){
			if(weaponer.isEmpty() || shielder.isEmpty())
				helperMaker();
			return ((Shielder<?>) shielder.get(numberOWeapon)).getShield(character);
		}

		public String getWeaponName(int numberOShield, Character character){
			if(weaponer.isEmpty() || shielder.isEmpty())
				helperMaker();
			return ((Weaponer<?>) weaponer.get(numberOShield)).getWeaponName(character);
		}

		public String getShieldName(int numberOWeapon,Character character){
			if(weaponer.isEmpty() || shielder.isEmpty())
				helperMaker();
			return ((Shielder<?>) shielder.get(numberOWeapon)).getShieldName(character);
		}

		public int shieldAmount(){
			if(weaponer.isEmpty() || shielder.isEmpty())
				helperMaker();
			return shielder.size();
		}

		public int weaponAmount(){
			if(weaponer.isEmpty() || shielder.isEmpty())
				helperMaker();
			return weaponer.size();
		}

		public void helperMaker(){}

	}

	public static class Melee extends ClassObject{

		public Melee(){
			name = "Melee";
			texture = "MeleeIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<MeleeWeapons.ABat>());
			shielder.add(new Shielder<MeleeShields.MeleeShield>());
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Melee();
		}
	}

	public static class Speedster extends ClassObject{

		public Speedster(){
			name = "Speedster";
			texture = "SpeedsterIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<SpeedsterWeapons.SpeedsterDagger>());
			shielder.add(new Shielder<SpeedsterShields.SpeedsterShield>());
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Speedster();
		}
	}

	public static class Healer extends ClassObject{

		public Healer(){
			name = "Healer";
			texture = "HealerIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<HealerWeapons.BlessedSword>());
			weaponer.add(new Weaponer<HealerWeapons.BestHealerSword>());
			shielder.add(new Shielder<HealerShields.BlessedShield>());
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Healer();
		}
	}

	public static class Tank extends ClassObject{

		public Tank(){
			name = "Tank";
			texture = "TankIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<TankWeapons.TankSword>());
			shielder.add(new Shielder<TankShields.TankShield>());
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Tank();
		}
	}

	public static class Mage extends ClassObject{

		public Mage(){
			name = "Mage";
			texture = "MageIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<MageWeapons.BasicWand>());
			shielder.add(new Shielder<MageShields.RoughCrystal>());
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Mage();
		}
	}

	public static class SwordMage extends ClassObject{

		public SwordMage(){
			name = "SwordMage";
			texture = "SwordMageIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<SwordMageWeapons.SwordWand>());
			shielder.add(new Shielder<SwordMageShields.CrystalizedShield>());
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.SwordMage();
		}
	}

	public static class Summoner extends ClassObject{

		public Summoner(){
			name = "Summoner";
			texture = "SummonerIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<Weapons.NoWeapon>());
			shielder.add(new Shielder<Shields.NoShield>());
		}


		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Summoner();
		}
	}

	public static class Imp extends ClassObject{

		public Imp(){
			name = "Imp";
			texture = "ImpIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<ImpWeapons.ImpDagger>());
			weaponer.add(new Weaponer<ImpWeapons.ImpHastyDagger>());
			weaponer.add(new Weaponer<ImpWeapons.MassDemonizeDagger>());
			shielder.add(new Shielder<ImpShields.ImpDemonizeShield>());
			shielder.add(new Shielder<ImpShields.ImpRitualShield>());
			shielder.add(new Shielder<ImpShields.DarkWings>());
			shielder.add(new Shielder<ImpShields.Daredevil>());
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Imp();
		}
	}

	public static class Catapult extends ClassObject{

		public Catapult(){
			name = "Catapult";
			texture = "CatapultIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<CatapultAmmo>());
			weaponer.add(new Weaponer<CatapultAmmo.FlamingRock>());
			weaponer.add(new Weaponer<CatapultAmmo.HomingRock>());
			weaponer.add(new Weaponer<CatapultAmmo.ClusterRock>());
			shielder.add(new Shielder<CatapultParts.MetalBucket>());
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Catapult();
		}
	}

	public static class StellarExplosion extends ClassObject{

		public StellarExplosion(){
			name = "StellarExplosion";
			texture = "StellarExplosionIcon";
		}

		@SuppressWarnings("all")
		public void helperMaker(){
			weaponer.add(new Weaponer<StellarExplosionWeapons.EnergyCondensator>());
			shielder.add(new Shielder<StellarExplosionShields.EnergyAccelerator>());
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.StellarExplosion();
		}
	}

	public static class Earthquaker extends ClassObject{

		public Earthquaker(){
			name = "Earthquaker";
			texture = "EarthquakerIcon";
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Earthquaker();
		}
	}

	public static class Weaponer<T extends Weapons>{
		Class<?> weapon;
		/**
		 * DDD   OOO  N  N  TTT   PPP  AAA  SSS  SSS    AAA    SSS  EEE  CCC  OOO  N  N  DDD     AAA  RRR  GGG
		 * D  D  O O  NN N   T    P P  A A  S    S      A A    S    E    C    O O  NN N  D  D    A A  R R  G
		 * D  D  O O  N NN   T    PPP  AAA  SSS  SSS    AAA    SSS  EEE  C    O O  N NN  D  D    AAA  RR   G G
		 * D  D  O O  N  N   T    P    A A    S    S    A A      S  E    C    O O  N  N  D  D    A A  R R  G G
		 * DDD   OOO  N  N   T    P    A A  SSS  SSS    A A    SSS  EEE  CCC  OOO  N  N  DDD     A A  R R  GGG
		*/
		@SafeVarargs
		public Weaponer( T... none){
			weapon = none.getClass().componentType();
		}

		public Weapons getWeapon(Character character){
			try {
				return ((Weapons) weapon.getConstructor(CharacterClasses.class,boolean.class).newInstance(character.classes,false));
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored){text("Failed weapon getter. This is a bug.",100,100, TextureManager.Fonts.ComicSans,40);}
			return null;
		}

		public String getWeaponName(Character character){
			try {
				return (String) weapon.getField("weaponName").get(weapon.getConstructor(CharacterClasses.class,boolean.class).newInstance(character.classes,false));
			} catch (NoSuchFieldException |IllegalAccessException | InvocationTargetException | InstantiationException |
					 NoSuchMethodException ignored) {text("Failed weapon getter. This is a bug.",100,100, TextureManager.Fonts.ComicSans,40);
			print("Failed in the name getting");}
			return "ERROR. THIS IS A BUG";
		}

	}

	public static class Shielder<T extends Shields>{
		Class<?> shield;
		/**
		 * DDD   OOO  N  N  TTT   PPP  AAA  SSS  SSS    AAA    SSS  EEE  CCC  OOO  N  N  DDD     AAA  RRR  GGG
		 * D  D  O O  NN N   T    P P  A A  S    S      A A    S    E    C    O O  NN N  D  D    A A  R R  G
		 * D  D  O O  N NN   T    PPP  AAA  SSS  SSS    AAA    SSS  EEE  C    O O  N NN  D  D    AAA  RR   G G
		 * D  D  O O  N  N   T    P    A A    S    S    A A      S  E    C    O O  N  N  D  D    A A  R R  G G
		 * DDD   OOO  N  N   T    P    A A  SSS  SSS    A A    SSS  EEE  CCC  OOO  N  N  DDD     A A  R R  GGG
		 */
		@SafeVarargs
		public Shielder(T... none){
			shield = none.getClass().componentType();
		}

		public Shields getShield(Character character){
			try {
				return ((Shields) shield.getConstructor(CharacterClasses.class,boolean.class).newInstance(character.classes,false));
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored){text("Failed shield getter. This is a bug.",100,100, TextureManager.Fonts.ComicSans,40);}
			return null;
		}

		public String getShieldName(Character character){
			try {
				return (String) shield.getField("shieldName").get(shield.getConstructor(CharacterClasses.class,boolean.class).newInstance(character.classes,false));
			} catch (NoSuchFieldException |IllegalAccessException | InvocationTargetException | InstantiationException |
					 NoSuchMethodException ignored) {text("Failed shield getter. This is a bug.",100,100, TextureManager.Fonts.ComicSans,40);
			}


			return "ERROR. THIS IS A BUG";
		}


	}


}
