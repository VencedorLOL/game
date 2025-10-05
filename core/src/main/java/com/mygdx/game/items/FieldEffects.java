package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.AudioManager.quickPlay;
import static com.mygdx.game.items.TextureManager.addToList;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.Turns.isTurnRunning;
import static java.lang.Math.*;

public class FieldEffects {

	public static ArrayList<FieldEffects> fieldEffects = new ArrayList<>();

	public static void updateFields(){
		for(FieldEffects f : fieldEffects)
			f.update();
	}

	public static void addField(FieldNames field){
		fieldEffects.add(fieldBuilder(field));
	}
	public static void clearFields(){fieldEffects.clear();}

	public static FieldEffects fieldBuilder(FieldNames field){
		switch(field){
			case LIGHTNING: return new Lightning();
			case HAILSTORM: return new Hailstorm();
			case CLOWDY:	return new Clowdy();
			case RAINY:		return new Rainy();
			case SNOWY:		return new Snowy();
			case SUNNY:		return new Sunny();
		}
		return null;
	}

	public static void deleteField(FieldNames field){
		for(FieldEffects f : fieldEffects)
			if(f.name.equals(field.name))
				f.destroyField();
		fieldEffects.removeIf(f -> f.name.equals(field.name));
	}




	/**
	 * 0: maxHealth
	 * 1: damage
	 * 2: speed
	 * 3: actingSpeed
	 * 4: defense
	 * 5: range
	 * 6: mana
	 * 7: mana regenerated/turn
	 * 8: mana/use
	 * 9: magicDamage
	 * 11: aggro
	 * 12: temp. defense
	 **/
	public static float getMultiplier(int type){
		float finalMultiplier = 1;
		for (FieldEffects c : fieldEffects)
			switch (type) {
				case 0 : finalMultiplier *= c.getHealthMultiplier();      break;
				case 1 : finalMultiplier *= c.getDamageMultiplier();      break;
				case 2 : finalMultiplier *= c.getSpeedMultiplier();       break;
				case 3 : finalMultiplier *= c.getActingSpeedMultiplier(); break;
				case 4 : finalMultiplier *= c.getDefenseMultiplier();     break;
				case 5 : finalMultiplier *= c.getRangeMultiplier();       break;
				case 6 : finalMultiplier *= c.getManaMultiplier();        break;
				case 7 : finalMultiplier *= c.getManaPerTurnMultiplier(); break;
				case 8 : finalMultiplier *= c.getManaPerUseMultiplier();  break;
				case 9 : finalMultiplier *= c.getMagicDamageMultiplier(); break;

				case 11: finalMultiplier *= c.getAggroMultiplier();       break;
				case 12: finalMultiplier *= c.getTempDefenseMultiplier(); break;
			}
		return finalMultiplier;
	}

	/**
	 * 0: maxHealth
	 * 1: damage
	 * 2: speed
	 * 3: actingSpeed
	 * 4: defense
	 * 5: range
	 * 6: mana
	 * 7: mana regenerated/turn
	 * 8: mana/use
	 * 9: magicDamage
	 * 11: aggro
	 * 12: temp. defense
	 **/
	public static float getAdditive(int type){
		float finalSum = 0;
		for (FieldEffects c : fieldEffects)
			switch (type) {
				case 0 : finalSum += c.getHealthAdditive();      break;
				case 1 : finalSum += c.getDamageAdditive();      break;
				case 2 : finalSum += c.getSpeedAdditive();       break;
				case 3 : finalSum += c.getActingSpeedAdditive(); break;
				case 4 : finalSum += c.getDefenseAdditive();     break;
				case 5 : finalSum += c.getRangeAdditive();       break;
				case 6 : finalSum += c.getManaAdditive();        break;
				case 7 : finalSum += c.getManaPerTurnAdditive(); break;
				case 8 : finalSum += c.getManaPerUseAdditive();  break;
				case 9 : finalSum += c.getMagicDamageAdditive(); break;

				case 11: finalSum += c.getAggroAdditive();       break;
				case 12: finalSum += c.getTempDefenseAdditive(); break;
			}
		return finalSum;
	}

	//--------------------------------------------


	public boolean didFieldAct;
	public boolean canFieldAct;
	public String name = "NullField";

	public void update(){
		if(canFieldAct)
			finishedActing();
	}

	public void setCondition(Conditions.ConditionNames condition){
		for(Actor a : actors)
			a.conditions.status(condition);

	}

	public void finishedActing(){
		canFieldAct = false;
	}

	float getHealthMultiplier(){return 1;}
	float getHealthAdditive(){return 0;}
	float getDamageMultiplier(){return 1;}
	float getDamageAdditive(){return 0;}
	float getTempDefenseMultiplier(){return 1;}
	float getTempDefenseAdditive(){return 0;}
	float getSpeedMultiplier(){return 1;}
	float getSpeedAdditive(){return 0;}
	float getActingSpeedMultiplier(){return 1;}
	float getActingSpeedAdditive(){return 0;}
	float getDefenseMultiplier(){return 1;}
	float getDefenseAdditive(){return 0;}
	float getRangeMultiplier(){return 1;}
	float getRangeAdditive(){return 0;}
	float getManaMultiplier(){return 1;}
	float getManaAdditive(){return 0;}
	float getManaPerTurnMultiplier(){return 1;}
	float getManaPerTurnAdditive(){return 0;}
	float getManaPerUseMultiplier(){return 1;}
	float getManaPerUseAdditive(){return 0;}
	float getMagicDamageMultiplier(){return 1;}
	float getMagicDamageAdditive(){return 0;}
	float getAggroMultiplier(){return 1;}
	float getAggroAdditive(){return 0;}
	public void destroyField(){}

	//---------------------------------------------------

	public static class Lightning extends FieldEffects{
		int warningTurnsCounter;
		int cooldownTurnsCounter;
		int turnsConstant = 2;
		int cooldownConstant = 1;
		boolean renderLightningLocation;
		int numberLightning = -1;

		public Lightning(){
			name = "Lightning";
			numberLightning = (int) (sqrt((stage.finalX-stage.startX) * (stage.finalY-stage.startY)/pow(globalSize(),2)))/2;
			addField(FieldNames.RAINY);
		}
		boolean justFinished = false;
		boolean dealDamage = false;
		boolean blockTheClass = false;
		public void update() {
			if(canFieldAct && !blockTheClass){
				if(cooldownTurnsCounter > 0&& !dealDamage){
					cooldownTurnsCounter--;
					finishedActing();

				}
				else if(warningTurnsCounter < turnsConstant&& !dealDamage){
					if(warningTurnsCounter == 0){
						setLightningLocation();
						justFinished = true;}
					warningTurnsCounter++;
					finishedActing();
				}
				else if(locations != null && !dealDamage){
					blockTheClass = true;
					for(Tile t : locations)
						animations.add(new TextureManager.Animation("lightning",t.x,t.y){
							@Override
							public void onFinish() {
								dealDamage = true;
								blockTheClass = false;
							}});
				}
				else if(locations != null){
					dealDamage = false;
					cooldownTurnsCounter = cooldownConstant;
					warningTurnsCounter = 0;
					renderLightningLocation = false;
					quickPlay("lightning");
					for(Tile t : locations)
						for(Actor a : actors){
							if(t.x == a.x && t.y == a.y) {
								a.damage(a.totalMaxHealth * 0.25f + 10, AttackTextProcessor.DamageReasons.LIGHTNING, null);
							}
							if( ( (t.x == a.x + globalSize() || t.x == a.x - globalSize()) && (t.y == a.y + globalSize() || t.y == a.y - globalSize()) ) ||
									((t.x == a.x + globalSize()*2 || t.x == a.x - globalSize()*2) && t.y == a.y )||
									((t.y == a.y + globalSize()*2 || t.y == a.y - globalSize()*2) && t.x == a.x ) ) {
								a.damage(a.totalMaxHealth * 0.075f + 10, AttackTextProcessor.DamageReasons.LIGHTNING, null);
							}
							if((t.x == a.x && (t.y == a.y + globalSize() || t.y == a.y - globalSize())) ||
									(t.y == a.y && (t.x == a.x + globalSize() || t.x == a.x - globalSize()))) {
								a.damage(a.totalMaxHealth * 0.15f + 10, AttackTextProcessor.DamageReasons.LIGHTNING, null);
							}
						}
					locations = null;
					finishedActing();
				} else
					finishedActing();
			}
			if(!isTurnRunning())
				justFinished = false;
			if(renderLightningLocation && locations != null && !justFinished)
				renderLightning();
		}


		public void renderLightning(){
			for(Tile t : locations){
				if(warningTurnsCounter < turnsConstant) {
					addToList("LightningWarning", t.x, t.y, 0.8f, 0, 250, 50, 0);
					addToList("LightningWarning", t.x + globalSize(), t.y, 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x - globalSize(), t.y, 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x, t.y + globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x, t.y - globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x + globalSize(), t.y + globalSize(), 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t.x - globalSize(), t.y - globalSize(), 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t.x - globalSize(), t.y + globalSize(), 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t.x + globalSize(), t.y - globalSize(), 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t.x + globalSize() * 2, t.y, 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t.x - globalSize() * 2, t.y, 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t.x, t.y + globalSize() * 2, 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t.x, t.y - globalSize() * 2, 0.8f, 0, 250, 250, 50);
				} else{
					addToList("LightningWarning", t.x, t.y, 0.8f, 0, 250, 0, 0);
					addToList("LightningWarning", t.x + globalSize(), t.y, 0.8f, 0, 250, 50, 0);
					addToList("LightningWarning", t.x - globalSize(), t.y, 0.8f, 0, 250, 50, 0);
					addToList("LightningWarning", t.x, t.y + globalSize(), 0.8f, 0, 250, 50, 0);
					addToList("LightningWarning", t.x, t.y - globalSize(), 0.8f, 0, 250, 50, 0);
					addToList("LightningWarning", t.x + globalSize(), t.y + globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x - globalSize(), t.y - globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x - globalSize(), t.y + globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x + globalSize(), t.y - globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x + globalSize() * 2, t.y, 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x - globalSize() * 2, t.y, 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x, t.y + globalSize() * 2, 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t.x, t.y - globalSize() * 2, 0.8f, 0, 250, 125, 50);
				}
			}
		}

		ArrayList<Tile> locations;
		public void setLightningLocation(){
			locations = new ArrayList<>();
			for(int i = 0; i < numberLightning; i++)
				locations.add(stage.tileset.get(com.badlogic.gdx.math.MathUtils.random(0,(stage.tileset.size()-1))));
			renderLightningLocation = true;
		}

	}

	public static class Hailstorm extends FieldEffects{
		int warningTurnsCounter = 0;
		int cooldownTurnsCounter;
		int turnsConstant = 1;
		int cooldownConstant = 0;
		boolean renderHailLocation;
		int numberHail;

		public Hailstorm(){
			name = "Hailstorm";
			numberHail = (int) ((int) (sqrt((stage.finalX-stage.startX) * (stage.finalY-stage.startY)/pow(globalSize(),2)))*1.5);
			cooldownTurnsCounter = 1;
			addField(FieldNames.SNOWY);
		}

		boolean justFinished = false;
		boolean dealDamage = false;
		boolean blockTheClass = false;
		public void update() {
			if(canFieldAct && !blockTheClass){
				if(cooldownTurnsCounter > 0 && !dealDamage){
					cooldownTurnsCounter--;
					finishedActing();
				}
				else if(warningTurnsCounter < turnsConstant && !dealDamage){
					if(locations == null)
						setIceBallLocation();
					warningTurnsCounter++;
					finishedActing();
				}
				else if(locations != null && !dealDamage){
					blockTheClass = true;
					for(Tile t : locations)
						animations.add(new TextureManager.Animation("icefall",t.x,t.y+globalSize()){
							@Override
							public void onFinish() {
								dealDamage = true;
								blockTheClass = false;
							}});
				}
				else if(locations != null){
					dealDamage = false;
					cooldownTurnsCounter = cooldownConstant;
					warningTurnsCounter = 0;
					for(Tile t : locations)
						for(Actor a : actors){
							if(t.x == a.x && t.y == a.y) {
								a.damage(a.totalMaxHealth * 0.15f + 10, AttackTextProcessor.DamageReasons.ICE_BALL, null);
							}
						}
					justFinished = true;
					setIceBallLocation();
					finishedActing();
				} else
					finishedActing();
			}
			if(!isTurnRunning())
				justFinished = false;
			if(renderHailLocation && locations != null && !justFinished)
				renderIceLocation();
		}


		public void renderIceLocation(){
			for(Tile t : locations){
				if(warningTurnsCounter < turnsConstant)
					addToList("IceBallIndicator",t.x,t.y,0.8f,0,100,250,250);
				else
					addToList("IceBallIndicator",t.x,t.y,0.8f,0,100,100,250);
			}
		}

		ArrayList<Tile> locations;
		public void setIceBallLocation(){
			locations = new ArrayList<>();
			for(int i = 0; i < numberHail; i++)
				locations.add(stage.tileset.get(com.badlogic.gdx.math.MathUtils.random(0,(stage.tileset.size()-1))));
			renderHailLocation = true;
		}

	}

	public static class Clowdy extends FieldEffects{
		public Clowdy(){name = "Clowdy";}
		public void update() {
			if(canFieldAct) {
				setCondition(Conditions.ConditionNames.CLOWDY);
				finishedActing();
			}
		}
	}

	public static class Rainy extends FieldEffects{
		public Rainy(){name = "Rainy";}
		public void update() {
			if(canFieldAct) {
				setCondition(Conditions.ConditionNames.RAINY);
				finishedActing();
			}
		}
	}
	public static class Snowy extends FieldEffects{
		public Snowy(){name = "Snowy";}
		public void update() {
			if(canFieldAct) {
				setCondition(Conditions.ConditionNames.SNOWY);
				finishedActing();
			}
		}
	}
	public static class Sunny extends FieldEffects{
		public Sunny(){name = "Sunny";}
		public void update() {
			if(canFieldAct) {
				setCondition(Conditions.ConditionNames.SUNNY);
				finishedActing();
			}
		}
	}

	public static class AlertSnowstorm extends FieldEffects{
		public AlertSnowstorm(){
			name="Alert Snowstorm";
			addField(FieldNames.LIGHTNING);
			addField(FieldNames.HAILSTORM);
		}
	}


	public enum FieldNames{
		LIGHTNING("Lightning"),
		HAILSTORM("Hailstorm"),
		CLOWDY("Clowdy"),
		RAINY("Rainy"),
		SNOWY("Snowy"),
		SUNNY("Sunny"),
		ALERT_SNOWSTORM("Alert Snowstorm"),
		;

		public final String name;
		FieldNames(String name){
			this.name = name;
		}
	}

}
