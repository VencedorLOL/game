package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.characters.Ability;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.*;
import com.mygdx.game.items.textboxelements.textboxes.tests.Test1;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.*;
import static com.mygdx.game.GlobalVariables.classSlots;
import static com.mygdx.game.MainClass.currentStage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.AttackIconRenderer.actorsThatAttack;
import static com.mygdx.game.items.AudioManager.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.FieldEffects.addField;
import static com.mygdx.game.items.Friend.friend;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.Interactable.interactables;
import static com.mygdx.game.items.ParticleManager.particleEmitter;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.TextureManager.Text.textSize;
import static com.mygdx.game.items.TurnManager.*;

public class Character extends Actor {

	public CharacterClasses classes;

	OnVariousScenarios oVS2;

	public boolean attackMode = false;
	public float lastClickX, lastClickY;
	public byte lastDamageCounter;
	public static ArrayList<ControllableFriend> controllableCharacters = new ArrayList<>();

	public Animation walkingAnimation;
	TextureManager.Text text;

	public TargetProcessor targetProcessor;

	public boolean lockClass = false;

	public ClassAndEquipmentChanger cC;

	public boolean classChanging = false;
	public int changePos = -1;

	public Animation idleAnimation;
	public String idleAnimationFile;
	public String idleTexture = "anima";

	public Character(float x, float y, float base, float height) {
		super("anima",x,y,base,height);
		testCollision.x = x;
		testCollision.y = y;
		testCollision.base = base;
		testCollision.height = height;
		oVS2 = new OnVariousScenarios(){
			public void onStageChange(){
				attackMode = false;
				speedLeft[0] = 0; speedLeft[1] = 0;
				didItAct = false;
				permittedToAct = false;
				lockClassTilAnimationFinishes = false;
				texture = idleTexture;
				conditions.onStageChange();
			}
		};
		team = 1;
		classes = new Classless();
		classes.character = this;
		path = new Path(x,y, classes.totalSpeed,this);
		actorsThatAttack.add(this);
		text = dynamicFixatedText(classes.currentHealth+"",80,100,-1, 30);
		text.setColor(244,83,23);
		targetProcessor = new TargetProcessor(this,classes.totalRange,true,classes.pierces,"target","notarget");
		targetProcessor.opacity = .2f;
		cC = new ClassAndEquipmentChanger(this);
		path.pathReset();
	}

	public void spendTurn(){
		permittedToAct = false;
		path.pathStart();
		isOnTheGrid();
		classes.runFinalizedTurn();
		getCamara().smoothZoom(1,30);
	}

	protected void automatedMovement(){
		if(leftClickReleased()){
			Vector3 temporal = roundedClick();
			lastClickX = temporal.x;
			lastClickY = temporal.y;
			print("last ckik x " + lastClickX + " y " + lastClickY);
			pathFinding();
		}
	}


	public void healThis(float heal){
		classes.healThis(heal);
	}

	private void pathFinding(){
		path.pathReset();
		if (pathFindAlgorithm.quickSolve(x,y,lastClickX,lastClickY, getTakeEnemiesIntoConsideration()))
			path.setPathTo(pathFindAlgorithm.convertTileListIntoPath());
		else
			print("no path found");
	}

	public void controlProcessor(){
		controllableCharacters.removeIf(c -> c.isDead);
		if(!isDecidingWhatToDo(this) && !isTurnRunning()) {
			for (ControllableFriend c : controllableCharacters) {
				if (!c.active && isDecidingWhatToDo(c) && !c.isDead) {
					c.active = true;
					getCamara().smoothAttachment(c,40);
					controlOfCamara = false;
					targetProcessor.circle = null;
					c.targetProcessor.circle = null;
					return;
				} else if (c.active)
					return;
			}
			getCamara().smoothAttachment(this,40);
			controlOfCamara = true;
			if(getCamara().isCamaraMoving())
				turnStopTimer(30);
		} else if (isTurnRunning()) {
			controlOfCamara = true;
			getCamara().smoothAttachment(this, 40);
		}
	}

	public void massCancel(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.X)){
			boolean isDeciding = false;
			for(ControllableFriend c : controllableCharacters)
				if(isDecidingWhatToDo(c)){
					isDeciding = true;
					break;
				}
			if(!isDeciding)
				return;
			for(ControllableFriend c : controllableCharacters) {
				c.cancelDecision();
				c.active = false;
				c.targetProcessor.circle = null;
			}
			cancelDecision();
			for(Ability a : classes.abilities)
				a.cancelActivation();
			getCamara().smoothAttachment(chara,40);
		}
	}

	public void statsUpdater(){
		totalSpeed = classes.totalSpeed;
		totalActingSpeed = classes.totalAttackSpeed;
		totalRange = classes.totalRange;
		totalDamage = classes.totalDamage;
		totalMaxHealth = classes.totalHealth;
		pierces = classes.pierces;
		health = classes.currentHealth;
		totalDefense = classes.totalDefense;
		totalAggro = classes.totalAggro;
		//anima cannot be mind controlled
		totalTeam = team;
	}

	public void update(){
		if(turnMode) {
			if (classes.tempDefense <= 0)
				text.text = classes.currentHealth + "/" + classes.totalHealth;
			else
				text.text = classes.currentHealth + "/" + classes.totalHealth + " + " + classes.tempDefense;
			classes.update();
			statsUpdater();
		} else
			text.text = "";
		onDeath();
		path.getStats(x,y, classes.totalSpeed);

		if(!lockClass) {
			if(!classChanging && changePos == -1) {
				if (!attackMode && !movementLock)
					movement();
				else if (attackMode)
					attack();
			}
			classChangeManager();

			if (!turnMode)
				interact();
		}
		glideProcess();
		updateFriends();
		debug();
		if(turnMode) {
			controlProcessor();
			massCancel();
			path.render();
			conditions.render();
		}
		textureUpdater();
		render();
	}

	public void classChangeManager(){
		if (classChanging && changePos == -1 && isDecidingWhatToDo(this))
			cC.render();
		else if (changePos != -1 && isPermittedToAct()){
			cC.activate(changePos);
			classes.handleHpManaTempDf();
			changePos = -1;
			spendTurn();
		}
	}

	public void onCharacterChange(int pos){
		changePos = pos;
		classChanging = false;
		actionDecided();
		path.pathReset();
	}


	@SuppressWarnings("all")
	public void updateFriends(){
		friend.removeIf(f -> f.isDead);
			for (int i = 0; i < friend.size(); i++)
				if (!friend.get(i).isDead) {
					friend.get(i).update();
					friend.get(i).render();
				}
	}

	public void onKillOverridable(){
		classes.runKill();
	}



	public void cancelAttackMode(){
		getCamara().smoothZoom(1,30);
		attackMode = false;
		if (targetProcessor.circle != null)
			targetProcessor.deleteTexture();
		targetProcessor.reset();
		attacks.clear();
	}

	public void leaveTurnMode(){
		lockClass = true;
		getCamara().smoothAttachment(this,20);
		cancelAttackMode();
		turnMode = false;
		classes.destroy();
		classes = new Classless();
		path.pathReset();
		new OnVariousScenarios.CounterObject(21){
			public void onCounterFinish() {
				lockClass = false;
			}
		};
	}

	public void enterTurnMode(){
		lockClass = true;
		cancelAttackMode();
		turnMode = true;
		path.pathReset();
		speedLeft[0] = 0;
		speedLeft[1] = 0;
		isOnTheGrid();
		cC.activate(0);
		classes.handleHpManaTempDf();
		changePos = -1;
		new OnVariousScenarios.CounterObject(21){
			public void onCounterFinish() {
				lockClass = false;
			}
		};
	}


	public void attackActuator(){
		if(!attacks.isEmpty() && !lockClassTilAnimationFinishes) {
			targetProcessor.reset();
			lockClassTilAnimationFinishes = true;
			attacks.get(elementOfAttack).isBeingExecuted = true;
			elementOfAttack++;
			if (elementOfAttack >= attacks.size())
				animations.add(new TextureManager.Animation("animaAttack", x, y) {
					public void onFinish() {
						lockClassTilAnimationFinishes = false;
						textureUpdater();
						attackDetector();
						elementOfAttack = 0;
						finalizedTurn();
					}});
			else
				animations.add(new TextureManager.Animation("animaAttack", x, y) {
					public void onFinish() {
						lockClassTilAnimationFinishes = false;
						attackDetector();
					}});

		}
	}


	public void attackDetector(){
		ArrayList<Actor> temp = new ArrayList<>();
		temp.add(this);
		classes.runAttack();
		ArrayList<Actor> list = rayCasting(x, y, attacks.get(elementOfAttack - 1).targetX, attacks.get(elementOfAttack - 1).targetY,temp, classes.pierces,this);
		if (list != null)
			for (Actor aa : list) {
				aa.damage(classes.outgoingDamage(), classes.damageReason,this);
				if (!classes.pierces)
					break;
			}
		else
			text("Missed!", attacks.get(elementOfAttack - 1).targetX + globalSize() / 2f - textSize("Missed!", 40) + 40,
					attacks.get(elementOfAttack - 1).targetY + globalSize() + 50, 100, 40, 200, 200, 200, 10, 50);

		attacks.get(elementOfAttack - 1).render = false;
	}


	protected void attackInput() {
		targetProcessor.changeRadius(totalRange);
		targetProcessor.render();
		if (actionConfirmJustPressed() || leftClickReleased()) {
			if (targetProcessor.findATile(targetProcessor.getTargetX(),targetProcessor.getTargetY()) != null && !(targetProcessor.getTargetX() == x && targetProcessor.getTargetY() == y)) {
				attacks.add(new Attack(targetProcessor.getTargetX(), targetProcessor.getTargetY(),this));
				if (classes.runOnAttackDecided())
					actionDecided();
			} else if (targetProcessor.getTargetX() == x && targetProcessor.getTargetY() == y)
				cancelAttackMode();
		} else if (escapeJustPressed())
			cancelAttackMode();
	}


	public void movementInputManual(){
		if (rightPressed())
			speedLeft[0] += globalSize()/16;
		if(leftPressed())
			speedLeft[0] -= globalSize()/16;
		if(upPressed())
			speedLeft[1] += globalSize()/16;
		if(downPressed())
			speedLeft[1] -= globalSize()/16;
		if (speedLeft[0] != 0 || speedLeft[1] != 0)
			lastTimeTilLastMovement = 0;
		textureUpdater();

	}

	public void texture(boolean state){
		if (!alreadyTextured) {
			textureOrientation = 7;
			if (speedLeft[0] > 0 || glideXPerFrame > 0)
				textureOrientation = 1;
			if (speedLeft[0] < 0 || glideXPerFrame < 0)
				textureOrientation = 4;
			if (speedLeft[1] > 0 || glideYPerFrame > 0)
				textureOrientation++;
			if (speedLeft[1] < 0 || glideYPerFrame < 0)
				textureOrientation--;
			alreadyTextured = state;
		}
	}

	private void animationWalking(){
		texture = null;
		String walkingFile = null;
		switch (textureOrientation){
			case 0: walkingFile = "walkingdiagrightdown";	break;
			case 1: walkingFile = "walkingright";			break;
			case 2: walkingFile = "walkingdiagrightup"; 	break;
			case 3: walkingFile = "walkingdiagleftdown";	break;
			case 4: walkingFile = "walkingleft";			break;
			case 5: walkingFile = "walkingdiagleftup";		break;
			case 6: walkingFile = "walkingdown";			break;
			case 8: walkingFile = "walkingup";				break;
		}
		if(walkingAnimation == null || walkingAnimation.finished || !walkingAnimation.name.equals(walkingFile)) {
			if (walkingAnimation != null)
				walkingAnimation.stop();
			walkingAnimation = new Animation(walkingFile, this){
				public void onFinish() {
					entityToFollow.texture = idleTexture;
				}};
			animations.add(walkingAnimation);
		}
	}

	@SuppressWarnings("all")
	public void textureUpdater(){
			texture(false);
			if(textureOrientation != 7)
				animationWalking();
		if (speedLeft[0] == 0 && speedLeft[1] == 0 && lastTimeTilLastMovement >= 2 && !isGliding) {
			texture = lastDamageCounter > 0 ? lastDamageCounter-- >= 0 ? "animaPain" + damageAnimation : idleTexture : idleTexture;
			if(lockClassTilAnimationFinishes)
				texture = null;
			if (walkingAnimation != null && !walkingAnimation.finished)
				walkingAnimation.stop();
		} else if (lastTimeTilLastMovement >= 2 && !isGliding){
			idleAnimation();
		}

		if (alreadyTextured)
			print(textureOrientation +" is the texuter orientation");
	}

	public void idleAnimation(){
		texture = null;
		if(idleAnimationFile != null && (walkingAnimation == null || walkingAnimation.finished || !walkingAnimation.name.equals(idleAnimationFile))){
			if(idleAnimation != null) idleAnimation.stop();
			idleAnimation = new Animation(idleAnimationFile,this){
				public void onFinish(){
					entityToFollow.texture =idleTexture;
				}};
			animations.add(idleAnimation);
		} else if (idleAnimationFile == null)
			texture = idleTexture;
	}


	byte damageAnimation;
	public void damageOverridable(float damage, AttackTextProcessor.DamageReasons damageReason){
		lastDamageCounter = (byte) (damage > 0 ? 120 : 0);
		damageAnimation = (byte) (damage / classes.health > 0.4 ? 2 : damage / classes.health >= 0.2 ? 1 : 3);
		classes.damage(damage,damageReason);
	}


	public void onDeathOverridable(){
		if (classes.currentHealth <= 0) {
			isDead = true;
			throw new Error("you died with " + classes.currentHealth + " health");
		}
	}




	boolean touchedIn, touchedOut;
	Interactable interactedObject;
	public void interact(){
		testCollision.x = x - globalSize(); testCollision.y = y - globalSize();
		testCollision.base = globalSize() * 3; testCollision.height = globalSize() * 3;
		interactableOverlaps(interactedObject);
		if(actionConfirmJustPressed() && interactableOverlaps(interactedObject))
			touchedIn = true;
		else if (actionConfirmJustPressed())
			touchedIn = false;
		if(actionConfirmReleased() && interactableOverlaps(interactedObject))
			touchedOut = true;
		else if (actionConfirmReleased())
			touchedOut = false;
		if(touchedIn && touchedOut && interactableOverlaps(interactedObject)){
			testCollision.base = globalSize(); testCollision.height = globalSize();
			touchedIn = false; touchedOut = false;
			interactedObject.onInteract(this);
		}
		testCollision.base = globalSize(); testCollision.height = globalSize();
	}

	@SuppressWarnings("all")
	public boolean interactableOverlaps(Interactable interactable){
		ArrayList<Interactable> sorted = ((ArrayList<Interactable>) interactables.clone());
		sorted.sort(((o1, o2) -> Float.compare((float) o1.dC(x,y), (float) o2.dC(x,y))));
		for(Interactable i : sorted)
			if (testCollision.overlaps(i)) {
				if(interactable == i){
					interactedObject = i;
					return true;
				}
				interactedObject = i;
			}
		return false;
	}



	// Debug

	private void debug(){

		if (Gdx.input.isKeyJustPressed(Input.Keys.X))
			if(!getReleaseVersion())
				print("Chara real x pos is: " + x + " and simplified x is: " + x/globalSize());
			else
				fixatedText("Chara real x pos is: " + x + " and on-the-grid x is: " + x/globalSize(),300,300,100,40);
		if (Gdx.input.isKeyJustPressed(Input.Keys.Y))
			if(!getReleaseVersion())
				print("Chara real y pos is: " + y + " and simplified y is: " + y/globalSize());
			else
				fixatedText("Chara real y pos is: " + y + " and on-the-grid y is: " + y/globalSize(),300,300,100,40);


		if (currentStage.equals("Creator")) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.N))
				noClip = !noClip;



		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
			fixatedText("Stages walls: ",300,0,300,40,255,255,255);
			for(int i = 0; i < stage.walls.size(); i++){
				fixatedText(stage.walls.get(i).getClass().getSimpleName() + " x: " + stage.walls.get(i).x + " y: " + stage.walls.get(i).y + " w: "
						+ stage.walls.get(i).base + " h: " + stage.walls.get(i).getHeight(), 200,50 + 50*i, 100 + 100 * stage.walls.size(),40,255,255,255);
			}
			print(stage.walls.size()+"");
		}

		if(currentStage.equals("Game")) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
				TurnManager.reset();
			}

			if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
				if (!getReleaseVersion()) {
					print("");
					print("Current Health is of: " + classes.health);
					print("Max Health is of: " + classes.totalHealth);
					print("speedLeft on x: " + speedLeft[0]);
					print("speedLeft on y: " + speedLeft[1]);
					print("permittedToAct: " + permittedToAct);
					print("Weapon: " + classes.weapon);
					print("Health: " + classes.totalHealth);
					print("Damage: " + classes.totalDamage);
					print("Class: " + classes.name);
					print("WeaponName: " + classes.weapon.weaponName);
					print("ShieldName: " + classes.shield.shieldName);
					print("Current health: " + classes.currentHealth);
					print("Texture" + texture);
					print("Real x: " + x + " simplified x: " + x / globalSize());
					print("Real y: " + y + " simplified y: " + y / globalSize());
					print("Base is: " + base + " Height is: " + height);
					print("");
				} else
					fixatedText(
							"Max Mana: " + classes.totalMana + "\n" +
									"Mana Pool: " + classes.manaPool + "\n" +
									"Mana Per Use: " + classes.totalManaPerUse + "\n" +
									"Mana Regeneration: " + classes.totalManaPerTurn + "\n" +
									"speedLeft on x: " + speedLeft[0] + "\n" +
									"speedLeft on y: " + speedLeft[1] + "\n" +
									"permittedToAct: " + permittedToAct + "\n" +
									"Weapon: " + classes.weapon + "\n" +
									"Health: " + classes.totalHealth + "\n" +
									"Damage: " + classes.totalDamage + "\n" +
									"Class: " + classes.name + "\n" +
									"WeaponName: " + classes.weapon.weaponName + "\n" +
									"ShieldName: " + classes.shield.shieldName + "\n" +
									"Current health: " + classes.currentHealth + "\n" +
									"Texture" + texture + "\n" +
									"Real x: " + x + " simplified x: " + x / globalSize() + "\n" +
									"Real y: " + y + " simplified y: " + y / globalSize(), 0, 0, 500, 40);
			}

			if (attackModeJustPressed() && isDecidingWhatToDo(this)) {
				if (turnMode) {
					targetProcessor.reset();
					attackMode = !attackMode;
					path.pathReset();
					if (!attackMode)
						cancelAttackMode();
					//	targetProcessor.mouseMoved = true;
				}
			}

			if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
				if (!attackMode) {
					if (!turnMode)
						enterTurnMode();
					else leaveTurnMode();
					fixatedText("Turn mode is now: " + turnMode, 300, 200, 100, 40, 255, 255, 255);
					//*	fixatedText("ABCDEFGHIJKLMNÑOPQRSTUVWXYZ\nabcdefghijklmnñopqrstuvwxyz" +
					//			"\n1234567890 ¡!¿?@#·ºª<> \n \"\"\\/$%&()[]{}ü=:;-_+*",400,400,1200*6,40,255,40,255);
				}
			}

			if (Gdx.input.isKeyJustPressed(Input.Keys.E) || (escapeJustPressed() && classChanging)) {
				if (turnMode && isDecidingWhatToDo(this)) {
					getCamara().smoothZoom(1, 30);
					path.pathReset();
					attacks.clear();
					targetProcessor.reset();
					classes.resetClassesState();
					classChanging = !classChanging;
				}
			}

			if (Gdx.input.isKeyPressed(Input.Keys.F10)) {
				particleEmitter("BLOB", (float) globalSize() / 2,
						(float) globalSize() / 2, 1, 50, true, false, 10, this);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.F12)) {
				stage.enemy.add(new Enemy(x + 256, y));
			}

			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
				quickPlay("test");
				animations.add(new Animation("beneath the mask", x, y));
				new Test1();
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
				setVolume(getRealVolume() - 10 >= 0 ? getRealVolume() - 10 : 0);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				setVolume(getRealVolume() + 10 <= 100 ? getRealVolume() + 10 : 100);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
				setMute(!getMute());
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
				stopAll();
				VideoManager.stopAll();
				print("x: " + glideXPerFrame + " y " + glideYPerFrame);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
				for (Actor a : actors) {
					a.damage(1000, AttackTextProcessor.DamageReasons.UNIVERSAL, null);
				}
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
				if (!getReleaseVersion())
					print(stage + "");
				else fixatedText("Current Stage: " + stage, 300, 200, 200, 40);
				classes.currentHealth = classes.totalHealth;
				fixatedText("Healed to max hp", 400, 400, 200, 40);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
				setTakeEnemiesIntoConsideration((byte) (-1 * getTakeEnemiesIntoConsideration() + 1));
				print("takenemiesintoconsideration is " + getTakeEnemiesIntoConsideration());
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
				print("is cam moving " + getCamara().isCamaraMoving());
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
				fixatedText("Version: B", 400, 200, 100, 40);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
				classes.attacksIgnoreTerrain = !classes.attacksIgnoreTerrain;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
				new ControllableFriend(x, y + 128, "animaAnnoyed", 100).softlockOverridable(false);
				print("Name of shield or whatever of thing just done: " + classSlots[0].getShieldName(0, this));
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
				addField(FieldEffects.FieldNames.CATACLYSM_GLATIATION);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
				addField(FieldEffects.FieldNames.CATACLYSM_NUCLEAR);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
				addField(FieldEffects.FieldNames.CATACLYSM_GRAVITATIONAL);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
				addField(FieldEffects.FieldNames.CATACLYSM_STELLAR_EXPLOSION);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
				addField(FieldEffects.FieldNames.ALERT_TSUNAMI);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
				addField(FieldEffects.FieldNames.CATACLYSM_ELECTRIC);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
				classes.health = 1000000;
				classes.currentHealth = 1000000;
			}
		}

	}

}
