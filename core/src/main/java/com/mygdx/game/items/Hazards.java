package com.mygdx.game.items;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.MainClass.currentStage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.Settings.printErr;
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
	 * @param p: Any number of parameters. If coordinates, not simplified. If the constructor doesn't exist... well, make sure the constructor exists.
	 */
	@SuppressWarnings("all")
	public static void addCustomHazard(HazardNames hazard,Object... p){
		hazards.add(customHazardsBuilder(hazard,p));
	}

	/**
	 *
	 * @param hazard: A HazardName object.
	 * @param x: The x coordinate, not simplified.
	 * @param y: The y coordinate, not simplified.
	 */

	@SuppressWarnings("all")
	public static void addHazard2(HazardNames hazard,float x, float y){
		hazards.add(hazardsBuilder(hazard,x,y));
	}

	public static void clearHazards(){hazards.clear();}

	@SuppressWarnings("all")
	public static Hazards hazardsBuilder(HazardNames hazard, float x, float y){
		return hazard.getHazard(x,y);
	}

	@SuppressWarnings("all")
	public static Hazards customHazardsBuilder(HazardNames hazard, Object... p){
		return hazard.getHazard(p);
	}


	@SuppressWarnings("all")
	public static void deleteHazard(HazardNames hazards, float x, float y){
		for(Hazards h : Hazards.hazards)
			if(h.getClass() == hazards.hazard && h.x == x && h.y == y) {
				h.destroyHazard();
				h.queuedForDeletion = true;
			}
//		Hazards.hazards.removeIf(h -> h.name.equals(hazards.name) && h.x == x && h.y == y);
	}

	public static ArrayList<Hazards> getHazard(HazardNames name,float x, float y){
		ArrayList<Hazards> list = new ArrayList<>();
		for (Hazards h : hazards){
			if(h.getClass() == name.hazard && h.x == x && h.y == y){
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
		public EarthCrack(float x, float y) {
			super(x, y, globalSize(), globalSize());
			init(x,y,overrideSetSize());
		}

		public EarthCrack(Float x, Float y, Float size) {
			super(x, y, globalSize(), globalSize());
			init(x,y,size);
		}

		public void init(Float x, Float y, Float size){
			base = size * globalSize();
			print("x,y " + x + " " + y);
			name = "EarthCrack";
			texture = currentStage.equals("Creator") || currentStage.equals("Start") ? "EarthCrack" : null;
			segments = new boolean[currentStage.equals("Start") ? 1 : size.intValue()];
			int counter = 0;
			if(!currentStage.equals("Creator") && !currentStage.equals("Start")) {
				for (int i = 0; i < size; i++) {
					if (findATile(stage.tileset, x + i * globalSize(), y) != null) {
						segments[i] = true;
						counter++;
					} else
						segments[i] = false;
				}
				if (counter < 2)
					queuedForDeletion = true;
			}
		}

		public float overrideSetSize(){return 2;}


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

		@SuppressWarnings("all")
		public void render(){
			if(finishedAnimation || currentStage.equals("Creator")) {
				int counter = 0;
				for (int i = 0; i < segments.length; i++)
					if (segments[i] || currentStage.equals("Creator")) {
						if (counter == 0)
							if (stage.getTile(x/globalSize(),y/globalSize()) != null || currentStage.equals("Creator"))
								addToList("EarthCrackPoint", x, y);
						counter++;
					}
				int secondCounter = counter;
				for (int i = 0; i < segments.length; i++)
					if (segments[i] || currentStage.equals("Creator")) {
						if (--counter == 0) {
							if (stage.getTile((x+ i * globalSize())/globalSize(),y/globalSize()) != null || currentStage.equals("Creator"))
								addToList("EarthCrackPoint", x + i * globalSize(), y, 1, 0, true, false);
							break;
						}
						if(secondCounter - 1 != counter)
							if (stage.getTile((x+ i * globalSize())/globalSize(),y/globalSize()) != null || currentStage.equals("Creator"))
								addToList("EarthCrack", x + i * globalSize(), y);
					}
			}
		}

	}


	@SuppressWarnings("all")
	public static class TriggerCharacter extends Hazards {
		Actor triggered;
		/**
		 * <h3>Don't forget to add it to the {@code hazards} ArrayList </h3>*/
		public TriggerCharacter(float x,float y){
			super(x,y,globalSize(),globalSize());
			name = "Trigger";
			texture = "Trigger";
		}

		public void update() {
			Actor victim = stepTrigger();
			if(victim != null && (triggered == null || triggered != victim))
				overrideOnStep();
			triggered = victim;
		}

		public void overrideOnStep(){

		}

		public void render(){
			if(currentStage.equals("Creator"))
				TextureManager.addToList(texture,x,y);
		}

	}


	@SuppressWarnings("all")
	public int getType(){
		for(int i = 0; i < HazardNames.values().length; i++)
			if (HazardNames.values()[i].hazard == this.getClass())
				return i + 1;
		return -1;
	}

	public static class HazardConst<T extends Hazards>{
		Class<?> hazard;
		@SafeVarargs
		public HazardConst(T... none){hazard = none.getClass().componentType();}
		public Class<?> getHazard(){return hazard;}
	}



	@SuppressWarnings("all")
	public enum HazardNames{
		SPIKES((Class<Hazards>) new Hazards.HazardConst<Spikes>().getHazard()),
		FIRE((Class<Hazards>) new Hazards.HazardConst<FireTile>().getHazard()),
		EARTH_CRACK((Class<Hazards>) new Hazards.HazardConst<EarthCrack>().getHazard()),
		TRIGGER((Class<Hazards>) new Hazards.HazardConst<TriggerCharacter>().getHazard()),
		;

		public static int listDifference(){
			return 0;
		}

		public static int getType(Hazards hazard){
			for(int i = 0; i < HazardNames.values().length; i++)
				if (HazardNames.values()[i].hazard == hazard.getClass())
					return i;
			return -1;
		}

		public Hazards getHazard(float x, float y){
			try{
				return hazard.getConstructor(float.class,float.class).newInstance(x,y);
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
					 InvocationTargetException ignored){printErr("Coudn't get a new hazard!"); return null;}
		}

		public Hazards getHazard(Object... p){
			try{
				Class<?>[] c = new Class[p.length];
				for(int i = 0; i < p.length; i++)
					c[i] = p[i].getClass();
				return hazard.getConstructor(c).newInstance(p);
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
					 InvocationTargetException ignored){printErr("Coudn't get a new hazard!"); return null;}
		}

		public final Class<Hazards> hazard;
		HazardNames(Class<Hazards> hazard){
			this.hazard = hazard;
		}
	}






}
