package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.characters.equipment.shields.*;
import com.mygdx.game.items.characters.equipment.weapons.*;
import static com.mygdx.game.GlobalVariables.classSlots;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.ClickDetector.authenticClick;
import static com.mygdx.game.items.TextureManager.addToList;

public class ClassChanger {

	Character character;
	float x,y;

	public ClassChanger(Character character){
		this.character = character;
		this.x = character.x + globalSize()/2f - globalSize()/4f * classSlots.length;
		this.y = character.y + globalSize()*3/2f;
	}

	public void render(){
		this.x = character.x + globalSize()/2f - globalSize()/4f * classSlots.length;
		this.y = character.y + globalSize()*3/2f;
		for (int i = 0; i < classSlots.length; i++){
			addToList("SelectionBox",x+globalSize()/2f*i + globalSize()/8f,y,1,0,255,255,255,2,2);
			addToList(classSlots[i].texture,x+globalSize()/2f*i + globalSize()/8f,y,1,0,255,255,255,2,2);
			if(isBeingPressed(x+globalSize()/2f*i + globalSize()/8f,y,globalSize()/2f,globalSize()/2f))
				character.onCharacterChange(i);

		}
	}

	public boolean isBeingPressed(float x, float y, float base, float height){
		if (Gdx.input.justTouched()){
			Vector3 vector = authenticClick();
			return vector.x >= x && vector.x <=  x + base && vector.y >= y && vector.y <=  y + height;
		}
		return false;
	}

	public void activate(int pos){
		classSlots[pos].activate(character);
	}

	public static class ClassObject{
		String name;
		public String texture;



		public void activate(Character character){}

	}

	public static class Melee extends ClassObject{

		public Melee(){
			name = "Melee";
			texture = "MeleeIcon";
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Melee();
			character.classes.equipWeapon(new MeleeWeapons.ABat(character.classes));
			character.classes.equipShield(new MeleeShields.MeleeShield(character.classes));
		}
	}

	public static class Speedster extends ClassObject{

		public Speedster(){
			name = "Speedster";
			texture = "SpeedsterIcon";
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Speedster();
			character.classes.equipWeapon(new SpeedsterWeapons.SpeedsterDagger(character.classes));
			character.classes.equipShield(new SpeedsterShields.SpeedsterShield(character.classes));
		}
	}

	public static class Healer extends ClassObject{

		public Healer(){
			name = "Healer";
			texture = "HealerIcon";
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Healer();
			character.classes.equipWeapon(new HealerWeapons.BlessedSword(character.classes));
			character.classes.equipShield(new HealerShields.BlessedShield(character.classes));
		}
	}

	public static class Tank extends ClassObject{

		public Tank(){
			name = "Tank";
			texture = "TankIcon";
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Tank();
			character.classes.equipWeapon(new TankWeapons.TankSword(character.classes));
			character.classes.equipShield(new TankShields.TankShield(character.classes));
		}
	}

	public static class Mage extends ClassObject{

		public Mage(){
			name = "Mage";
			texture = "MageIcon";
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Mage();
			character.classes.equipWeapon(new MageWeapons.BasicWand(character.classes));
			character.classes.equipShield(new MageShields.RoughCrystal(character.classes));
		}
	}

	public static class SwordMage extends ClassObject{

		public SwordMage(){
			name = "SwordMage";
			texture = "SwordMageIcon";
		}

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.SwordMage();
			character.classes.equipWeapon(new SwordMageWeapons.SwordWand(character.classes));
			character.classes.equipShield(new SwordMageShields.CrystalizedShield(character.classes));
		}
	}

	public static class Summoner extends ClassObject{

		public Summoner(){
			name = "Summoner";
			texture = "SummonerIcon";
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

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.Imp();
			character.classes.equipWeapon(new ImpWeapons.ImpDagger(character.classes));
			character.classes.equipShield(new ImpShields.DarkWings(character.classes));
		}
	}

	public static class Catapult extends ClassObject{

		public Catapult(){
			name = "Catapult";
			texture = "CatapultIcon";
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

		public void activate(Character character){
			character.classes.destroy();
			character.classes = new com.mygdx.game.items.characters.classes.StellarExplosion();
			character.classes.equipWeapon(new StellarExplosionWeapons.EnergyCondensator(character.classes));
			character.classes.equipShield(new StellarExplosionShields.EnergyAccelerator(character.classes));
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
			character.classes.equipWeapon(new EarthquakerWeapons.GroundStomper(character.classes));
			character.classes.equipShield(new EarthquakerShields.StablePlatform(character.classes));
		}
	}

}
