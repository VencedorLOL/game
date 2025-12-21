package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.FieldEffects.getField;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Tile.findATile;

public class Hazards {

	public static ArrayList<Hazards> hazards = new ArrayList<>();

	public static void updateHazards(){
		for(Hazards h : hazards) {
			h.update();
			h.render();
		}
		hazards.removeIf(h -> h.queuedForDeletion);
	}

	/**
	 *
	 * @param hazard: A HazardName object.
	 * @param x: The x coordinate, SIMPLIFIED.
	 * @param y: The y coordinate, SIMPLIFIED.
	 */
	public static void addHazard(HazardNames hazard,float x, float y){
		hazards.add(hazardsBuilder(hazard,x*globalSize(),y*globalSize()));
	}

	/**
	 *
	 * @param hazard: A HazardName object.
	 * @param x: The x coordinate, without simplify.
	 * @param y: The y coordinate, without simplify.
	 */
	public static void addHazard2(HazardNames hazard,float x, float y){
		hazards.add(hazardsBuilder(hazard,x,y));
	}

	public static void clearHazards(){hazards.clear();}

	@SuppressWarnings("all")
	public static Hazards hazardsBuilder(HazardNames hazard, float x, float y){
		switch(hazard){
			case SPIKES:	 	return new Spikes(x,y);
			case FIRE:			return new FireTile(x,y);
			case EARTH_CRACK:	return new EarthCrack(x,y,11);
		}
		return null;
	}

	@SuppressWarnings("all")
	public static void deleteHazard(HazardNames hazards, float x, float y){
		for(Hazards h : Hazards.hazards)
			if(h.name.equals(hazards.name) && h.x == x && h.y == y) {
				h.destroyHazard();
				h.queuedForDeletion = true;
			}
//		Hazards.hazards.removeIf(h -> h.name.equals(hazards.name) && h.x == x && h.y == y);
	}

	public static ArrayList<Hazards> getHazard(HazardNames name,float x, float y){
		ArrayList<Hazards> list = new ArrayList<>();
		for (Hazards h : hazards){
			if(h.name.equals(name.name) && h.x == x && h.y == y){
				list.add(h);
			}
		}
		return list;
	}



//--------------------------------------------

	public float x,y,base,height;
	public boolean didHazardAct;
	public boolean canHazardAct;
	public String name = "NullField";
	public String texture;
	public boolean queuedForDeletion = false;

	public void update(){
		if(canHazardAct) {
			finishedActing();
		}
	}

	public final Actor stepTrigger(){
		for(Actor a : actors)
			if(a.overlaps(x,y,base,height))
				return a;
		return null;
	}

	public void finishedActing(){
		canHazardAct = false;
	}

	public Hazards(float x, float y, float base, float height){
		this.x = x; this.y = y;
		this.base = base; this.height = height;

	}

	public void render(){
		TextureManager.addToList(texture,x,y);
	}

	public void destroyHazard(){}


	//---------------------------------------------------


	public static class Spikes extends Hazards {
		public float damage = 10;
		ArrayList<Actor> triggered = new ArrayList<>();

		public Spikes(float x,float y){
			super(x,y,globalSize(),globalSize());
			name = "Spikes";
			texture = "Spikes";
		}

		public void update() {
			Actor victim = stepTrigger();
			if(canHazardAct){
				triggered = new ArrayList<>();
				finishedActing();
			}
			if(victim != null && victim.x % globalSize() == 0 && victim.y % globalSize() == 0 && !triggered.contains(victim)) {
				victim.damage(damage, AttackTextProcessor.DamageReasons.PIERCING, null);
				triggered.add(victim);
			}
		}

	}

	public static class FireTile extends Hazards{
		public int time = -1;

		public FireTile(float x,float y){
			super(x,y,globalSize(),globalSize());
			name = "FireTile";
			texture = "FireTile";
		}

		public FireTile(float x,float y,int time){
			super(x,y,globalSize(),globalSize());
			name = "FireTile";
			texture = "FireTile";
			this.time = time;
			if(getField(FieldEffects.FieldNames.RAINY) != null)
				if(this.time != 0)
					this.time--;
			if(getField(FieldEffects.FieldNames.SNOWY) != null)
				if(this.time != 0)
					this.time--;
		}

		public void update() {
			Actor victim = stepTrigger();
			if(canHazardAct){
				if(victim != null) {
					victim.damage(victim.totalMaxHealth * .05f + 5, AttackTextProcessor.DamageReasons.BURNT, null);
					victim.conditions.status(Conditions.ConditionNames.BURNING);
				}
				if(time == 0)
					queuedForDeletion = true;
				time--;
				finishedActing();
			}
			if(victim != null && victim.x % globalSize() == 0 && victim.y % globalSize() == 0)
				victim.conditions.status(Conditions.ConditionNames.BURNING);
		}

	}


	public static class EarthCrack extends Hazards {
		public boolean[] segments;
		boolean finishedAnimation = false;
		ArrayList<Actor> triggered = new ArrayList<>();

		public EarthCrack(float x, float y, float size) {
			super(x, y, size*globalSize(), globalSize());
			print("x,y " + x + " " + y);
			name = "EarthCrack";
			texture = null;
			segments = new boolean[(int) size];
			int counter = 0;
			for(int i = 0; i < size; i++) {
				if(findATile(stage.tileset, x + i * globalSize(), y) != null) {
					segments[i] = true;
					counter++;
				}
				else
					segments[i] = false;

			}
			if(counter < 2)
				queuedForDeletion = true;
		}

		public void update() {
			if(canHazardAct){
				if(!finishedAnimation){
					print("didnt finish anitmiaon");
					for(int i = 0; i < segments.length; i++)
						if(segments[i])
							animations.add(new TextureManager.Animation("earthquake", x + globalSize() * i, y) {public void onFinish() {finishedAnimation = true;}});
				}
				else {
					triggered = new ArrayList<>();
					finishedActing();
				}
			}
			if(finishedAnimation) {
				Actor victim = stepTrigger();
				if(victim != null && victim.x % globalSize() == 0 && victim.y % globalSize() == 0 && !triggered.contains(victim)) {
					victim.damage(victim.totalMaxHealth * 0.70f + victim.totalDefense * 0.1f + 5, AttackTextProcessor.DamageReasons.EARTHQUAKE, null);
					triggered.add(victim);
				}
			}
		}

		public void render(){
			if(finishedAnimation) {
				int counter = 0;
				for (int i = 0; i < segments.length; i++)
					if (segments[i]) {
						if (counter == 0)
							addToList("EarthCrackPoint", x, y);
						counter++;
					}
				int secondCounter = counter;
				for (int i = 0; i < segments.length; i++)
					if (segments[i]) {
						if (--counter == 0) {
							addToList("EarthCrackPoint", x + i * globalSize(), y, 1, 0, true, false);
							break;
						}
						if(secondCounter - 1 != counter)
							addToList("EarthCrack", x + i * globalSize(), y);
					}
			}
		}

	}










	public enum HazardNames{
		SPIKES("Spikes"),
		FIRE("FireTile"),
		EARTH_CRACK("EarthCrack"),

		;

		public final String name;
		HazardNames(String name){
			this.name = name;
		}
	}






}
