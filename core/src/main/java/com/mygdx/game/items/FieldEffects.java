package com.mygdx.game.items;

import java.util.ArrayList;
import java.util.Objects;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.AttackTextProcessor.DamageReasons.*;
import static com.mygdx.game.items.AudioManager.quickPlay;
import static com.mygdx.game.items.Conditions.ConditionNames.*;
import static com.mygdx.game.items.Hazards.HazardNames.FIRE;
import static com.mygdx.game.items.Hazards.getHazard;
import static com.mygdx.game.items.Hazards.hazards;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Turns.isTurnRunning;
import static java.lang.Math.*;

public class FieldEffects {

	public static ArrayList<FieldEffects> fieldEffects = new ArrayList<>();

	public static void updateFields(){
		for(FieldEffects f : fieldEffects)
			f.update();
		for(FieldEffects f : fieldEffects)
			if(f.queuedForRemoval)
				f.destroyField();
		fieldEffects.removeIf(f -> f.queuedForRemoval);
	}

	public static void addField(FieldNames field){
		fieldEffects.add(fieldBuilder(field));
	}
	public static void clearFields(){fieldEffects.clear();}

	public static FieldEffects fieldBuilder(FieldNames field){
		switch(field){
			case LIGHTNING: 					return new Lightning();
			case HAILSTORM:						return new Hailstorm();
			case CLOWDY:						return new Clowdy();
			case RAINY:							return new Rainy();
			case SNOWY:							return new Snowy();
			case SUNNY:							return new Sunny();
			case ALERT_SNOWSTORM:   			return new AlertSnowstorm();
			case ALERT_FIRE:					return new AlertFire();
			case ALERT_STELLAR_STORM:			return new AlertStellarStorm();
			case ALERT_TSUNAMI:					return new AlertTsunami();
			case ALERT_ELECTRIC_GROUND:			return new AlertElectricGround();
			case CATACLYSM_GLATIATION:			return new CataclysmGlacial();
			case CATACLYSM_GRAVITATIONAL:		return new CataclysmGravitational();
			case CATACLYSM_NUCLEAR:				return new CataclysmNuclear();
			case CATACLYSM_ELECTRIC:			return new CataclysmElectric();
			case CATACLYSM_STELLAR_EXPLOSION:	return new CataclysmStellarExplosion();


		}
		return null;
	}

	@SuppressWarnings("all")
	public static void deleteField(FieldNames field){
		for(FieldEffects f : fieldEffects)
			if(f.name.equals(field.name)) {
				f.queuedForRemoval = true;
			}
//		fieldEffects.removeIf(f -> f.name.equals(field.name));
	}

	public static FieldEffects getField(FieldNames field){
		for(FieldEffects f : fieldEffects)
			if(f.name.equals(field.name))
				return f;
		return null;
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
	@SuppressWarnings("all")
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
	@SuppressWarnings("all")
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
			}
		return finalSum;
	}

	//--------------------------------------------

	public boolean queuedForRemoval = false;
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

	public void clearCondition(Conditions.ConditionNames condition){
		for(Actor a : actors)
			a.conditions.remove(condition);
	}

	public void finishedActing(){
		canFieldAct = false;
	}

	float getHealthMultiplier(){return 1;}
	float getHealthAdditive(){return 0;}
	float getDamageMultiplier(){return 1;}
	float getDamageAdditive(){return 0;}
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

	protected void playSiren(){
		fixatedAnimations.add(new Animation("siren",50,45){
			public int counter = 3;

			public void updateOverridable() {
				scaleX = 6;
				scaleY = 6;
			}

			public void onFinish() {
				if(counter > 0) {
					finished = false;
					line = 0;
					listOfUsedLoops = new ArrayList<>();
					framesForCurrentAnimation = 0;
					counter--;
				}
			}
		});
	}


	//---------------------------------------------------

	public static class Lightning extends FieldEffects{
		int warningTurnsCounter;
		int cooldownTurnsCounter;
		int turnsConstant = 2;
		int cooldownConstant = 1;
		boolean renderLightningLocation;
		@SuppressWarnings("all")
		int numberLightning = -1;
		TargetProcessor warning;

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
					for(float[] t : locations)
						animations.add(new TextureManager.Animation("lightning",t[0],t[1]){
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
					for(float[] t : locations)
						for(Actor a : actors){
							if(t[0] == a.x && t[1] == a.y) {
								a.damage(a.totalMaxHealth * 0.25f + 10, ELECTRIC, null);
							}
							if( ( (t[0] == a.x + globalSize() || t[0] == a.x - globalSize()) && (t[1] == a.y + globalSize() || t[1] == a.y - globalSize()) ) ||
									((t[0] == a.x + globalSize()*2 || t[0] == a.x - globalSize()*2) && t[1] == a.y )||
									((t[1] == a.y + globalSize()*2 || t[1] == a.y - globalSize()*2) && t[0] == a.x ) ) {
								a.damage(a.totalMaxHealth * 0.075f + 10, ELECTRIC, null);
							}
							if((t[0] == a.x && (t[1] == a.y + globalSize() || t[1] == a.y - globalSize())) ||
									(t[1] == a.y && (t[0] == a.x + globalSize() || t[0] == a.x - globalSize()))) {
								a.damage(a.totalMaxHealth * 0.15f + 10, ELECTRIC, null);
							}
						}
					locations = null;
					warning = new TargetProcessor();
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
			if(warningTurnsCounter >= turnsConstant) {
				warning.changeColor((byte) 200,(byte)0,(byte)0);
			}
			warning.render();
			for(float[] t : locations){
				if(warningTurnsCounter < turnsConstant) {
					addToList("LightningWarning", t[0], t[1], 0.8f, 0, 250, 50, 0);
			/*		addToList("LightningWarning", t[0] + globalSize(), t[1], 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0] - globalSize(), t[1], 0.8f, 0, 250, 125, 50);
			*		addToList("LightningWarning", t[0], t[1] + globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0], t[1] - globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0] + globalSize(), t[1] + globalSize(), 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t[0] - globalSize(), t[1] - globalSize(), 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t[0] - globalSize(), t[1] + globalSize(), 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t[0] + globalSize(), t[1] - globalSize(), 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t[0] + globalSize() * 2, t[1], 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t[0] - globalSize() * 2, t[1], 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t[0], t[1] + globalSize() * 2, 0.8f, 0, 250, 250, 50);
					addToList("LightningWarning", t[0], t[1] - globalSize() * 2, 0.8f, 0, 250, 250, 50); */
				} else{
					addToList("LightningWarning", t[0], t[1], 0.8f, 0, 250, 0, 0);
			/*		addToList("LightningWarning", t[0] + globalSize(), t[1], 0.8f, 0, 250, 50, 0);
					addToList("LightningWarning", t[0] - globalSize(), t[1], 0.8f, 0, 250, 50, 0);
			*		addToList("LightningWarning", t[0], t[1] + globalSize(), 0.8f, 0, 250, 50, 0);
					addToList("LightningWarning", t[0], t[1] - globalSize(), 0.8f, 0, 250, 50, 0);
					addToList("LightningWarning", t[0] + globalSize(), t[1] + globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0] - globalSize(), t[1] - globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0] - globalSize(), t[1] + globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0] + globalSize(), t[1] - globalSize(), 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0] + globalSize() * 2, t[1], 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0] - globalSize() * 2, t[1], 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0], t[1] + globalSize() * 2, 0.8f, 0, 250, 125, 50);
					addToList("LightningWarning", t[0], t[1] - globalSize() * 2, 0.8f, 0, 250, 125, 50); */
				}
			}
		}

		ArrayList<float[]> locations;
		public void setLightningLocation(){
			locations = new ArrayList<>();
			warning = new TargetProcessor();
			warning.circle = new TargetProcessor.Circle(stage.tileset.get(0),stage.tileset,1,false,false,200,200,0,false);
			warning.circle.circle.clear();
			for(int i = 0; i < numberLightning; i++) {
				locations.add(stage.tileset.get(com.badlogic.gdx.math.MathUtils.random(0, (stage.tileset.size() - 1))).xAndY());
				warning.circle.addToCircle(locations.get(i)[0],locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0] + globalSize(), locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0] - globalSize(), locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0], locations.get(i)[1] + globalSize());
				warning.circle.addToCircle(locations.get(i)[0], locations.get(i)[1] - globalSize());
				warning.circle.addToCircle(locations.get(i)[0] + globalSize(), locations.get(i)[1] + globalSize());
				warning.circle.addToCircle(locations.get(i)[0] - globalSize(), locations.get(i)[1] - globalSize());
				warning.circle.addToCircle(locations.get(i)[0] - globalSize(), locations.get(i)[1] + globalSize());
				warning.circle.addToCircle(locations.get(i)[0] + globalSize(), locations.get(i)[1] - globalSize());
				warning.circle.addToCircle(locations.get(i)[0] + globalSize() * 2, locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0] - globalSize() * 2, locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0], locations.get(i)[1] + globalSize() * 2);
				warning.circle.addToCircle(locations.get(i)[0], locations.get(i)[1] - globalSize() * 2);
			}
			warning.circle.circle.removeIf(Objects::isNull);
			warning.circle.detectCornersOfCircle(warning.circle.circle);
			renderLightningLocation = true;
		}

		@Override
		public void destroyField() {
			deleteField(FieldNames.RAINY);
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
					for(float[] t : locations)
						animations.add(new TextureManager.Animation("icefall",t[0],t[1]+globalSize()){
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
					for(float[] t : locations) {
						for (Actor a : actors) {
							if (t[0] == a.x && t[1] == a.y) {
								a.damage(a.totalMaxHealth * 0.15f + 10, ICE_BALL, null);
							}
						}
						for (Hazards h : getHazard(FIRE, t[0], t[1])) {
							if(h instanceof Hazards.FireTile && ((Hazards.FireTile) h).time > 1)
								((Hazards.FireTile) h).time -= 2;
							else if (h instanceof Hazards.FireTile && ((Hazards.FireTile) h).time == 1)
								((Hazards.FireTile) h).time -= 1;
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
			for(float[] t : locations){
				if(warningTurnsCounter < turnsConstant)
					addToList("IceBallIndicator",t[0],t[1],0.8f,0,100,250,250);
				else
					addToList("IceBallIndicator",t[0],t[1],0.8f,0,100,100,250);
			}
		}

		ArrayList<float[]> locations;
		public void setIceBallLocation(){
			locations = new ArrayList<>();
			for(int i = 0; i < numberHail; i++)
				locations.add(stage.tileset.get(com.badlogic.gdx.math.MathUtils.random(0,(stage.tileset.size()-1))).xAndY());
			renderHailLocation = true;
		}

		public void destroyField() {
			deleteField(FieldNames.SNOWY);
		}
	}

	public static class Clowdy extends FieldEffects{
		public Clowdy(){name = "Clowdy";}
		public void update() {
			if(canFieldAct) {
				setCondition(CLOWDY);
				finishedActing();
			}
		}

		public void destroyField() {
			clearCondition(CLOWDY);
		}
	}

	public static class Rainy extends FieldEffects{
		public Rainy(){name = "Rainy";}
		public void update() {
			if(canFieldAct) {
				setCondition(RAINY);
				finishedActing();
			}
		}
		public void destroyField() {
			clearCondition(RAINY);
		}

	}
	public static class Snowy extends FieldEffects{
		public Snowy(){name = "Snowy";}
		public void update() {
			if(canFieldAct) {
				setCondition(SNOWY);
				finishedActing();
			}
		}

		public void destroyField() {
			clearCondition(SNOWY);
		}
	}
	public static class Sunny extends FieldEffects{
		public Sunny(){name = "Sunny";}
		public void update() {
			if(canFieldAct) {
				setCondition(SUNNY);
				finishedActing();
			}
		}

		public void destroyField() {
			clearCondition(SUNNY);
		}
	}

	@SuppressWarnings("all")
	public static class AlertSnowstorm extends FieldEffects{
		public AlertSnowstorm(){
			name="Alert Snowstorm";
			addField(FieldNames.LIGHTNING);
			addField(FieldNames.HAILSTORM);
			playSiren();
		}

		public void destroyField() {
			deleteField(FieldNames.LIGHTNING);
			deleteField(FieldNames.HAILSTORM);
		}
	}
	public static class AlertFire extends FieldEffects{
		int warningTurnsCounter = 0;
		int cooldownTurnsCounter;
		int turnsConstant = 1;
		int cooldownConstant = 0;
		boolean renderFireLocation;
		int numberFire;

		public AlertFire(){
			name = "Alert Fire";
			numberFire = (int) sqrt((stage.finalX-stage.startX) * (stage.finalY-stage.startY)/pow(globalSize(),2));
			cooldownTurnsCounter = 1;
			playSiren();
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
						setFireLocation();
					warningTurnsCounter++;
					finishedActing();
				}
				else if(locations != null && !dealDamage){
/*					blockTheClass = true;
*					for(Tile t : locations)
						animations.add(new TextureManager.Animation("icefall",t.x,t.y+globalSize()){
							@Override
							public void onFinish() {
								dealDamage = true;
								blockTheClass = false;
							}});*/
					dealDamage = true;
				}
				else if(locations != null){
					dealDamage = false;
					cooldownTurnsCounter = cooldownConstant;
					warningTurnsCounter = 0;
					for(float[] t : locations) {
						for (Actor a : actors) {
							if (t[0] == a.x && t[1] == a.y) {
								a.damage(a.totalMaxHealth * 0.15f + 20, BURNT, null);
								a.conditions.status(BURNING);
							}
						}
						hazards.add(new Hazards.FireTile(t[0],t[1],4));
					}
					justFinished = true;
					setFireLocation();
					finishedActing();
				} else
					finishedActing();
			}
			if(!isTurnRunning())
				justFinished = false;
			if(renderFireLocation && locations != null && !justFinished)
				renderFireLocation();
		}


		public void renderFireLocation(){
			for(float[] t : locations){
				if(warningTurnsCounter < turnsConstant)
					addToList("FireWarning",t[0],t[1],0.8f,0,100,250,250);
				else
					addToList("FireWarning",t[0],t[1],0.8f,0,100,100,250);
			}
		}

		ArrayList<float[]> locations;
		public void setFireLocation(){
			locations = new ArrayList<>();
			for(int i = 0; i < numberFire; i++){
				locations.add(stage.tileset.get(com.badlogic.gdx.math.MathUtils.random(0,(stage.tileset.size()-1))).xAndY());
			}
			renderFireLocation = true;
		}

	}


	public static class AlertStellarStorm extends FieldEffects{
		int turnCount = 15;
		public AlertStellarStorm(){
			name = "Alert StellarStorm";
			playSiren();
		}
		public void update() {
			if(canFieldAct) {
				setCondition(STELLAR_STORM);
				finishedActing();
			}
		}

		public void destroyField() {
			clearCondition(STELLAR_STORM);
		}

		public void finishedActing() {
			super.finishedActing();
			turnCount--;
			if(turnCount == 0){
				queuedForRemoval = true;
			}
		}
	}


	public static class AlertTsunami extends FieldEffects{
		int warningTurnsCounter;
		int cooldownTurnsCounter;
		int turnsConstant;
		int cooldownConstant = 1;
		boolean renderTsunamiLocation;
		byte direction;
		TargetProcessor warning;

		public AlertTsunami(){
			name = "Alert Tsunami";
			direction = (byte) com.badlogic.gdx.math.MathUtils.random(0,3);
			turnsConstant = direction > 1 ? max((stage.finalY-stage.startY)/(6*globalSize()),3) : max((stage.finalX-stage.startX)/(6*globalSize()),3);
			playSiren();
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
						setTsunamiLocation();
						justFinished = true;}
					warningTurnsCounter++;
					finishedActing();
				}
/*				else if(locations != null && !dealDamage){
*					blockTheClass = true;
					for(Tile t : locations)
						animations.add(new TextureManager.Animation("lightning",t.x,t.y){
							@Override
							public void onFinish() {
								dealDamage = true;
								blockTheClass = false;
							}});

				}*/
				else if(locations != null){
					dealDamage = false;
					cooldownTurnsCounter = cooldownConstant;
					warningTurnsCounter = 0;
					renderTsunamiLocation = false;
//					quickPlay("lightning");
					for(float[] t : locations) {
						for (Actor a : actors) {
							if (t[0] == a.x && t[1] == a.y) {
								a.damage(a.totalMaxHealth * 0.8f + 50, PRESSURE, null);
								a.conditions.remove(BURNING);
								a.conditions.remove(BURNING_BRIGHT);
								a.conditions.remove(MELTING);
								a.conditions.remove(HYPERTHERMIA);
								a.conditions.remove(SUBLIMATING);
							}
						}
						for (Hazards h : getHazard(FIRE, t[0], t[1])) {
							if (h instanceof Hazards.FireTile && ((Hazards.FireTile) h).time > 0)
								((Hazards.FireTile) h).time = 0;
						}
					}
					locations = null;
					warning = new TargetProcessor();
					queuedForRemoval = true;
					finishedActing();
				} else
					finishedActing();
			}
			if(!isTurnRunning())
				justFinished = false;
			if(renderTsunamiLocation && locations != null && !justFinished)
				renderTsunami();
		}


		public void renderTsunami(){
			if(warningTurnsCounter >= turnsConstant)
				warning.changeColor((byte) 0,(byte)0,(byte)250);
			warning.render();
//			for(float[] t : locations){
//*				if(warningTurnsCounter < turnsConstant) {
//					addToList("TsunamiWarning", t[0], t[1], 0.5f, 0, 20, 50, 200);
//				} else{
//					addToList("TsunamiWarning", t[0], t[1], 0.8f, 0, 60, 100, 250);
//				}
//			}
		}

		//floor() my beloved
		ArrayList<float[]> locations;
		public void setTsunamiLocation(){
			direction = (byte) com.badlogic.gdx.math.MathUtils.random(0,3);
			turnsConstant = direction > 1 ? max((stage.finalY-stage.startY)/(6*globalSize()),3) : max((stage.finalX-stage.startX)/(6*globalSize()),3);
			locations = new ArrayList<>();
			warning = new TargetProcessor();
			warning.circle = new TargetProcessor.Circle(stage.tileset.get(0),stage.tileset,1,false,false,20,50,201,false);
			warning.circle.circle.clear();
			for(int i = 0; i < ((stage.finalX-stage.startX)/globalSize()+1) * ((stage.finalY-stage.startY)/globalSize()+1) / (2); i++) {
				if(direction == 0) {
					locations.add(new float[]{
							(float) (stage.startX + globalSize() * floor((double) i / (1+(double) (stage.finalY - stage.startY) / globalSize())))
						,	(float) (stage.startY + globalSize() * valueCutter(i, 1+(stage.finalY - stage.startY) / globalSize()))});
				}
				if(direction == 1) {
					locations.add(new float[]{
							(float) (stage.finalX - globalSize() * floor((double) i / (1+(double) (stage.finalY - stage.startY) / globalSize())))
						,	(float) (stage.finalY - globalSize() * valueCutter(i, 1+(stage.finalY - stage.startY) / globalSize()))});
				}
				if(direction == 2) {
					locations.add(new float[]{
							(float) (stage.startX + globalSize() * valueCutter(i, 1+(stage.finalX - stage.startX) / globalSize()))
						,	(float) (stage.startY + globalSize() * floor((double) i / (1+(double) (stage.finalX - stage.startX) / globalSize())))});
				}

				if(direction == 3) {
					locations.add(new float[]{
							(float) (stage.finalX - globalSize() * valueCutter(i, 1+(stage.finalX - stage.startX) / globalSize()))
						,	(float) (stage.finalY - globalSize() * floor((double) i / (1+(double) (stage.finalX - stage.startX) / globalSize())))});


				}
				warning.circle.addToCircle(locations.get(i)[0],locations.get(i)[1]);
			}
			warning.circle.circle.removeIf(Objects::isNull);
			warning.circle.detectCornersOfCircle(warning.circle.circle);
			renderTsunamiLocation = true;

		}

	}

	public int valueCutter(float initial, int delimiter){
		if(initial >= delimiter)
			initial -= delimiter;
		return initial < delimiter ? (int) initial : valueCutter(initial,delimiter);
	}

	@SuppressWarnings("all")
	public static class AlertEarthquake extends FieldEffects{
		int warningTurnsCounter;
		int cooldownTurnsCounter;
		int turnsConstant = 2;
		int cooldownConstant = 1;
		boolean renderTsunamiLocation;
		byte direction;
		TargetProcessor warning;

		public AlertEarthquake(){
			name = "Alert Earthquake";
			direction = (byte) com.badlogic.gdx.math.MathUtils.random(0,3);
			turnsConstant = direction > 1 ? max((stage.finalY-stage.startY)/(6*globalSize()),3) : max((stage.finalX-stage.startX)/(6*globalSize()),3);
			playSiren();
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
						setTsunamiLocation();
						justFinished = true;}
					warningTurnsCounter++;
					finishedActing();
				}
/*				else if(locations != null && !dealDamage){
*					blockTheClass = true;
					for(Tile t : locations)
						animations.add(new TextureManager.Animation("lightning",t.x,t.y){
							@Override
							public void onFinish() {
								dealDamage = true;
								blockTheClass = false;
							}});

				}*/
				else if(locations != null){
					dealDamage = false;
					cooldownTurnsCounter = cooldownConstant;
					warningTurnsCounter = 0;
					renderTsunamiLocation = false;
//					quickPlay("lightning");
					for(float[] t : locations) {
						for (Actor a : actors) {
							if (t[0] == a.x && t[1] == a.y) {
								a.damage(a.totalMaxHealth * 0.8f + 50, PRESSURE, null);
								a.conditions.remove(BURNING);
								a.conditions.remove(BURNING_BRIGHT);
								a.conditions.remove(MELTING);
								a.conditions.remove(HYPERTHERMIA);
								a.conditions.remove(SUBLIMATING);
							}
						}
						for (Hazards h : getHazard(FIRE, t[0], t[1])) {
							if (h instanceof Hazards.FireTile && ((Hazards.FireTile) h).time > 0)
								((Hazards.FireTile) h).time = 0;
						}
					}
					locations = null;
					warning = new TargetProcessor();
					queuedForRemoval = true;
					finishedActing();
				} else
					finishedActing();
			}
			if(!isTurnRunning())
				justFinished = false;
			if(renderTsunamiLocation && locations != null && !justFinished)
				renderTsunami();
		}


		public void renderTsunami(){
			if(warningTurnsCounter >= turnsConstant)
				warning.changeColor((byte) 60,(byte)100,(byte)250);
			warning.render();
//			for(float[] t : locations){
//				if(warningTurnsCounter < turnsConstant) {
//					addToList("TsunamiWarning", t[0], t[1], 0.5f, 0, 20, 50, 200);
//				} else{
//					addToList("TsunamiWarning", t[0], t[1], 0.8f, 0, 60, 100, 250);
//				}
//			}
		}

		//floor() my beloved
		ArrayList<float[]> locations;
		public void setTsunamiLocation(){
			direction = (byte) com.badlogic.gdx.math.MathUtils.random(0,3);
			turnsConstant = direction > 1 ? max((stage.finalY-stage.startY)/(6*globalSize()),3) : max((stage.finalX-stage.startX)/(6*globalSize()),3);
			locations = new ArrayList<>();
			warning = new TargetProcessor();
			warning.circle = new TargetProcessor.Circle(stage.tileset.get(0),stage.tileset,1,false,false,20,50,201,false);
			warning.circle.circle.clear();
			for(int i = 0; i < ((stage.finalX-stage.startX)/globalSize()+1) * ((stage.finalY-stage.startY)/globalSize()+1) / (2); i++) {
				if(direction == 0) {
					locations.add(new float[]{
							(float) (stage.startX + globalSize() * floor((double) i / (1+(double) (stage.finalY - stage.startY) / globalSize())))
							,	(float) (stage.startY + globalSize() * valueCutter(i, 1+(stage.finalY - stage.startY) / globalSize()))});
				}
				if(direction == 1) {
					locations.add(new float[]{
							(float) (stage.finalX - globalSize() * floor((double) i / (1+(double) (stage.finalY - stage.startY) / globalSize())))
							,	(float) (stage.finalY - globalSize() * valueCutter(i, 1+(stage.finalY - stage.startY) / globalSize()))});
				}
				if(direction == 2) {
					locations.add(new float[]{
							(float) (stage.startX + globalSize() * valueCutter(i, 1+(stage.finalX - stage.startX) / globalSize()))
							,	(float) (stage.startY + globalSize() * floor((double) i / (1+(double) (stage.finalX - stage.startX) / globalSize())))});
				}

				if(direction == 3) {
					locations.add(new float[]{
							(float) (stage.finalX - globalSize() * valueCutter(i, 1+(stage.finalX - stage.startX) / globalSize()))
							,	(float) (stage.finalY - globalSize() * floor((double) i / (1+(double) (stage.finalX - stage.startX) / globalSize())))});

				}
				warning.circle.addToCircle(locations.get(i)[0],locations.get(i)[1]);
			}
			warning.circle.circle.removeIf(Objects::isNull);
			warning.circle.detectCornersOfCircle(warning.circle.circle);
			renderTsunamiLocation = true;

		}

	}


	public static class AlertElectricGround extends FieldEffects{
		public AlertElectricGround(){
			name = "Alert ElectricGround";
			setCondition(ELECTRIC_GROUND);
			playSiren();
		}

		@Override
		public void destroyField() {
			clearCondition(ELECTRIC_GROUND);
		}

		public void update() {
			if(canFieldAct){
				for(Actor a : actors){
					a.damage(max(a.totalMaxHealth * 0.05f, 0.1f), ELECTRIC,null);
					if (random() >= 0.85)
						if(!a.conditions.hasStatus(STUNNED)) {
							a.conditions.condition(STUNNED);
							a.conditions.getStatus(STUNNED).setTurns(1);
						}
					if (random() >= 0.95)
						a.conditions.status(BURNING);
				}
				finishedActing();
			}
		}
	}


	@SuppressWarnings("all")
	public static class CataclysmGlacial extends FieldEffects{
		int turnCount;
		public CataclysmGlacial(){
			name = "Cataclysm Glaciation";
			setCondition(GLACIATION);
		}

		public void destroyField() {
			clearCondition(GLACIATION);
		}

		public void update() {
			if(canFieldAct){
				if(++turnCount > 9)
					for(Actor a : actors)
						a.damage(1+(float) (10*(floor(turnCount/10f)-1)), AttackTextProcessor.DamageReasons.FROSTBITE,null);
				finishedActing();
			}
		}
	}

	@SuppressWarnings("all")
	public static class CataclysmNuclear extends FieldEffects{
		int turnCount;
		public CataclysmNuclear(){
			name = "Cataclysm Nuclear";
			setCondition(NUCLEAR_EVENT);
		}

		public void destroyField() {
			clearCondition(NUCLEAR_EVENT);
		}

		public void update() {
			if(canFieldAct){
				if(++turnCount > 4)
					for(Actor a : actors) {
						a.damage(a.totalMaxHealth * .1f, RADIATION, null);
						if(turnCount > 19)
							a.conditions.status(BURNING);
						if(turnCount > 49)
							a.conditions.status(BURNING_BRIGHT);
						if(turnCount > 74)
							a.conditions.status(MELTING);
						if(turnCount > 99)
							a.conditions.status(SUBLIMATING);
					}
				finishedActing();
			}
		}
	}

	@SuppressWarnings("all")
	public static class CataclysmElectric extends FieldEffects{
		int turnCounter;
		int warningTurnsCounter;
		int cooldownTurnsCounter;
		int turnsConstant = 2;
		int cooldownConstant = 1;
		boolean renderLightningLocation;
		int numberLightning;
		boolean killElectricGround = true;
		TargetProcessor warning;
		boolean killLightning = true;
		boolean killRainy = true;

		public CataclysmElectric(){
			name = "Cataclysm Electric";

			if (getField(FieldNames.ALERT_ELECTRIC_GROUND) != null)
				killElectricGround = false;
			if (getField(FieldNames.LIGHTNING) != null) {
				killLightning = false;
				if (getField(FieldNames.RAINY) != null)
					killRainy = false;
				deleteField(FieldNames.LIGHTNING);
				if(!killRainy)
					addField(FieldNames.RAINY);
			}
			addField(FieldNames.ALERT_ELECTRIC_GROUND);
		}

		public void destroyField() {
			if(killElectricGround)
				deleteField(FieldNames.ALERT_ELECTRIC_GROUND);
			if(!killLightning)
				addField(FieldNames.LIGHTNING);
			if(!killRainy)
				addField(FieldNames.RAINY);
		}

		public void finishedActing(){
			turnCounter++;
			canFieldAct = false;
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
					for(float[] t : locations)
						animations.add(new TextureManager.Animation("lightning",t[0],t[1]){
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
					for(float[] t : locations)
						for(Actor a : actors){
							if(t[0] == a.x && t[1] == a.y) {
								a.damage(a.totalMaxHealth * 0.25f + 10, ELECTRIC, null);
							}
							if( ( (t[0] == a.x + globalSize() || t[0] == a.x - globalSize()) && (t[1] == a.y + globalSize() || t[1] == a.y - globalSize()) ) ||
									((t[0] == a.x + globalSize()*2 || t[0] == a.x - globalSize()*2) && t[1] == a.y )||
									((t[1] == a.y + globalSize()*2 || t[1] == a.y - globalSize()*2) && t[0] == a.x ) ) {
								a.damage(a.totalMaxHealth * 0.075f + 10, ELECTRIC, null);
							}
							if((t[0] == a.x && (t[1] == a.y + globalSize() || t[1] == a.y - globalSize())) ||
									(t[1] == a.y && (t[0] == a.x + globalSize() || t[0] == a.x - globalSize()))) {
								a.damage(a.totalMaxHealth * 0.15f + 10, ELECTRIC, null);
							}
						}
					locations = null;
					warning = new TargetProcessor(0,0,1,false,false);
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
			if(warningTurnsCounter >= turnsConstant)
				warning.changeColor((byte) 200,(byte)0,(byte)0);
			warning.render();
			for(float[] t : locations)
				if(warningTurnsCounter < turnsConstant)
					addToList("LightningWarning", t[0], t[1], 0.8f, 0, 250, 50, 0);
				else
					addToList("LightningWarning", t[0], t[1], 0.8f, 0, 250, 0, 0);

		}

		ArrayList<float[]> locations;
		public void setLightningLocation(){
			locations = new ArrayList<>();
			numberLightning = (int) ((sqrt((stage.finalX-stage.startX) * (stage.finalY-stage.startY)/pow(globalSize(),2)))/2 * max(floor(turnCounter/2f),1));
			warning = new TargetProcessor();
			warning.circle = new TargetProcessor.Circle(stage.tileset.get(0),stage.tileset,1,false,false,200,200,0,false);
			warning.circle.circle.clear();
			for(int i = 0; i < numberLightning; i++) {
				locations.add(stage.tileset.get(com.badlogic.gdx.math.MathUtils.random(0, (stage.tileset.size() - 1))).xAndY());
				warning.circle.addToCircle(locations.get(i)[0],locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0] + globalSize(), locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0] - globalSize(), locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0], locations.get(i)[1] + globalSize());
				warning.circle.addToCircle(locations.get(i)[0], locations.get(i)[1] - globalSize());
				warning.circle.addToCircle(locations.get(i)[0] + globalSize(), locations.get(i)[1] + globalSize());
				warning.circle.addToCircle(locations.get(i)[0] - globalSize(), locations.get(i)[1] - globalSize());
				warning.circle.addToCircle(locations.get(i)[0] - globalSize(), locations.get(i)[1] + globalSize());
				warning.circle.addToCircle(locations.get(i)[0] + globalSize(), locations.get(i)[1] - globalSize());
				warning.circle.addToCircle(locations.get(i)[0] + globalSize() * 2, locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0] - globalSize() * 2, locations.get(i)[1]);
				warning.circle.addToCircle(locations.get(i)[0], locations.get(i)[1] + globalSize() * 2);
				warning.circle.addToCircle(locations.get(i)[0], locations.get(i)[1] - globalSize() * 2);
			}
			warning.circle.circle.removeIf(Objects::isNull);
			warning.circle.detectCornersOfCircle(warning.circle.circle);
			renderLightningLocation = true;
		}

	}

	@SuppressWarnings("all")
	public static class CataclysmGravitational extends FieldEffects{
		int turnCount;
		public CataclysmGravitational(){
			name = "Cataclysm Gravitational";
			setCondition(AUGMENTED_GRAVITY);
		}

		public void destroyField() {
			clearCondition(AUGMENTED_GRAVITY);
		}

		public void update() {
			if(canFieldAct){
					for(Actor a : actors)
						a.damage(max(a.totalMaxHealth * .05f,0.1f), PRESSURE, null);
				finishedActing();
			}
		}
	}


	@SuppressWarnings("all")
	public static class CataclysmStellarExplosion extends FieldEffects{
		int turnCount;
		public CataclysmStellarExplosion(){
			name = "Cataclysm StellarExplosion";
			setCondition(STELLAR_EXPLOSION);
		}

		public void destroyField() {
			clearCondition(STELLAR_EXPLOSION);
		}

		public void update() {
			if(canFieldAct){
					for(Actor a : actors) {
						a.conditions.status(BURNING);
						if(++turnCount > 4) {
							a.damage(max(a.health * .2f, 0.1f), BURNT, null);
							a.conditions.status(BURNING_BRIGHT);
						}
					}
				finishedActing();
			}
		}
	}


	@SuppressWarnings("all")
	public enum FieldNames{
		LIGHTNING("Lightning"),
		HAILSTORM("Hailstorm"),
		CLOWDY("Clowdy"),
		RAINY("Rainy"),
		SNOWY("Snowy"),
		SUNNY("Sunny"),
		ALERT_SNOWSTORM("Alert Snowstorm"),
		ALERT_FIRE("Alert Fire"),
		ALERT_STELLAR_STORM("Alert StellarStorm"),
		ALERT_TSUNAMI("Alert Tsunami"),
		ALERT_ELECTRIC_GROUND("Alert ElectricGround"),
		CATACLYSM_GLATIATION("Cataclysm Glaciation"),
		CATACLYSM_ELECTRIC("Cataclysm Electric"),
		CATACLYSM_NUCLEAR("Cataclysm Nuclear"),
		CATACLYSM_GRAVITATIONAL("Cataclysm Gravitational"),
		CATACLYSM_STELLAR_EXPLOSION("Cataclysm StellarExplosion"),
		;

		public final String name;
		FieldNames(String name){
			this.name = name;
		}
	}

}
