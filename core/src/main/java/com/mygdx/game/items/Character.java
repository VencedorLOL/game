package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Utils;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.*;
import com.mygdx.game.items.characters.equipment.Weapons;
import com.mygdx.game.items.characters.equipment.shields.*;
import com.mygdx.game.items.characters.equipment.weapons.HealerWeapons;
import com.mygdx.game.items.characters.equipment.weapons.SpeedsterWeapons;
import com.mygdx.game.items.characters.equipment.weapons.MeleeWeapons;
import com.mygdx.game.items.characters.equipment.weapons.SwordMageWeapons;


import java.util.ArrayList;

import static com.mygdx.game.GameScreen.*;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.AttackIconRenderer.actorsThatAttack;
import static com.mygdx.game.items.AudioManager.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.FieldEffects.addField;
import static com.mygdx.game.items.Friend.friend;
import static com.mygdx.game.items.Interactable.interactables;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Turns.*;

public class Character extends Actor implements Utils {

	public CharacterClasses classes;

	OnVariousScenarios oVS2;

	public boolean attackMode = false;
	public float lastClickX, lastClickY;
	public byte lastDamageCounter;

	Animation walkingAnimation;
	public static ArrayList<ControllableFriend> controllableCharacters = new ArrayList<>();

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
				texture = "anima";
				conditions.onStageChange();
			}
		};
		team = 1;
		classes = new Classless();
		classes.character = this;
		path = new Path(x,y, classes.totalSpeed,this);
		actorsThatAttack.add(this);
	}

	public void spendTurn(){
		printErr("Called Spend Turn");
		permittedToAct = false;
		path.pathStart();
		isOnTheGrid();
	}

	protected void automatedMovement(){
		if(Gdx.input.justTouched()){
			lastClickX = roundedClick().x;
			lastClickY = roundedClick().y;
			print("last ckik x " + lastClickX + " y " + lastClickY);
			pathFinding();
		}
//		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
//			pathFinding();
//		}
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
					Camara.smoothAttachment(c,40);
					circle = null;
					c.circle = null;
					return;
				} else if (c.active)
					return;
			}
			Camara.smoothAttachment(this,40);
			if(Camara.isCamaraMoving())
				turnStopTimer(30);
		} else if (isTurnRunning())
			Camara.smoothAttachment(this,40);
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
				c.circle = null;
			}
			cancelDecision();
			Camara.smoothAttachment(chara,40);
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
	}

	public void update(){
		classes.update();
		statsUpdater();
		onDeath();
		path.getStats(x,y, classes.totalSpeed);

		if (!attackMode)
			movement();
		else
			attack();

		if (!turnMode)
			interact();

		updateFriends();
		controlProcessor();
		massCancel();
		debug();
		path.render();
		conditions.render();
		textureUpdater();
		render();
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



	Entity targetsTarget = new Entity("default",x,y,false);
	Animation target;
	Tile.Circle circle;
	boolean mouseMoved;
	float[] lastRecordedMousePos = new float[]{.1f,0.264f};
	private void targetProcesor(){
		if (circle == null || circle.center != stage.findATile(x,y) || circle.tileset != stage.tileset || circle.radius != classes.totalRange || !circle.walkable) {
			if (circle != null)
				for (Tile t : circle.circle)
					for (int i = 0; i < 13; i++)
						t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Tile.Circle(stage.findATile(x, y), stage.tileset, classes.totalRange, true,classes.attacksIgnoreTerrain);
		}
		circle.renderCircle();
		Vector3 temporal = roundedClick();
		mouseMoved = !(temporal.x == lastRecordedMousePos[0] && temporal.y == lastRecordedMousePos[1]);
		if (Gdx.input.justTouched())
			mouseMoved = true;
		lastRecordedMousePos[0] = temporal.x; lastRecordedMousePos[1] = temporal.y;
		if (circle.isInsideOfCircle(temporal.x, temporal.y)) {

			if (!mouseMoved)
				targetKeyboardMovement();

			if (!circle.isInsideOfCircle(targetsTarget.x, targetsTarget.y) || mouseMoved) {
				targetsTarget.x = roundedClick().x;
				targetsTarget.y = roundedClick().y;
			}

			targetRender();

		} else if (!mouseMoved){
			targetKeyboardMovement();
			if (!(targetsTarget.x == x && targetsTarget.y == y))
				targetRender();
		} else {
			animations.remove(target);
			target = null;
			targetsTarget.x = x;
			targetsTarget.y = y;
		}
	}

	private void targetRender(){
		if (target == null) {
			target = new Animation("target", targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
			animations.add(target);
		}
		if (target.finished){
			target = new Animation("target", targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
			animations.add(target);
		}
	}

	private void targetKeyboardMovement(){
		float x = targetsTarget.x; float y = targetsTarget.y;
		if (Gdx.input.isKeyJustPressed(Input.Keys.W))
			y += globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.A))
			x -= globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.S))
			y -= globalSize();
		if (Gdx.input.isKeyJustPressed(Input.Keys.D))
			x += globalSize();
		if(circle.isInsideOfCircle(x,y)) {
			targetsTarget.x = x;
			targetsTarget.y = y;
		} else if (circle.isInsideOfCircle(x,targetsTarget.y))
			targetsTarget.x = x;
		else if (circle.isInsideOfCircle(targetsTarget.x,y))
			targetsTarget.y = y;
	}


	public void cancelAttackMode(){
		attackMode = false;
		if (circle != null)
			for (Tile t : circle.circle)
				for (int i = 0; i < 9; i++)
					t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
		circle = null;
		animations.remove(target);
		target = null;
		attacks.clear();
	}

	public void attackActuator(){
		if(!attacks.isEmpty() && !lockClassTilAnimationFinishes) {
			lockClassTilAnimationFinishes = true;
			attacks.get(elementOfAttack).isBeingExecuted = true;
			elementOfAttack++;
			if (elementOfAttack >= attacks.size())
				animations.add(new TextureManager.Animation("animaAttack", x, y) {
					public void onFinish() {
						lockClassTilAnimationFinishes = false;
						attackDetector();
						finalizedTurn();
						elementOfAttack = 0;
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
			text("Missed!", attacks.get(elementOfAttack -  1).targetX,attacks.get(elementOfAttack -  1).targetY + 240,60, Fonts.ComicSans,40,127,127,127,1,30);
		attacks.get(elementOfAttack - 1).render = false;
	}


	protected void attackInput() {
		targetProcesor();
		if(Gdx.input.justTouched()) {
			Vector3 temporal = roundedClick();
			if (circle.findATile(temporal.x,temporal.y) != null) {
				attacks.add(new Attack(temporal.x, temporal.y,this));
				if (classes.runOnAttackDecided())
					actionDecided();
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (circle.findATile(targetsTarget.x,targetsTarget.y) != null && !(targetsTarget.x == x && targetsTarget.y == y)) {
				attacks.add(new Attack(targetsTarget.x, targetsTarget.y,this));
				if (classes.runOnAttackDecided())
					actionDecided();
			}
		}
	}


	//FIXME: revisit when proper key handlin
	public void movementInputManual(){
		if (Gdx.input.isKeyPressed(Input.Keys.W))
			speedLeft[1] += globalSize()/16;
		if (Gdx.input.isKeyPressed(Input.Keys.A))
			speedLeft[0] -= globalSize()/16;
		if (Gdx.input.isKeyPressed(Input.Keys.S))
			speedLeft[1] -= globalSize()/16;
		if (Gdx.input.isKeyPressed(Input.Keys.D))
			speedLeft[0] += globalSize()/16;
		if (speedLeft[0] != 0 || speedLeft[1] != 0)
			lastTimeTilLastMovement = 0;
		textureUpdater();

	}

	public void texture(boolean state){
		if (!alreadyTextured) {
			textureOrientation = 7;
			if (speedLeft[0] > 0)
				textureOrientation = 1;
			if (speedLeft[0] < 0)
				textureOrientation = 4;
			if (speedLeft[1] > 0)
				textureOrientation++;
			if (speedLeft[1] < 0)
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
		if(walkingAnimation == null || (walkingAnimation.finished || walkingAnimation.name != walkingFile)) {
			if (walkingAnimation != null)
				walkingAnimation.stop();
			walkingAnimation = new Animation(walkingFile, this){
				public void onFinish() {
					entityToFollow.texture = "anima";
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
			texture = lastDamageCounter > 0 ? lastDamageCounter-- >= 0 ? "animaPain" + damageAnimation : "anima" : "anima";
			if(lockClassTilAnimationFinishes)
				texture = null;
			if (walkingAnimation != null && !walkingAnimation.finished)
				walkingAnimation.stop();
		}
		if (alreadyTextured)
			print(textureOrientation +" is the texuter orientation");
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
			byte[] gitGud = new byte[1];
			gitGud[1] = 1;
		}
	}


	public void interact(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			testCollision.x = x; testCollision.y = y + globalSize();
			for(Interactable i : interactables)
				if (testCollision.overlaps(i))
					i.onInteract();
		}
	}



	// Debug

	public void changeTo(){
		changeToHealer();
		changeToMelee();
		changeToVencedor();
		changeToSummon();
		changeToImp();
		equipBestSword();
		equipBlessedShield();
		equipBlessedSword();
		equipMeleeShield();
		equipMeleeSword();
		equipVencedorSword();
	}


	public void changeToHealer(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
			classes.destroy();
			classes = new SwordMage();
			classes.equipWeapon(new SwordMageWeapons.SwordWand(classes));
			classes.equipShield(new SwordMageShields.CrystalizedShield(classes));
		}
	}

	public void changeToMelee(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F2)){
			classes.destroy();
			classes = new Melee();
			classes.equipWeapon(new MeleeWeapons.ABat(classes));
			classes.equipShield(new MeleeShields.MeleeShield(classes));
		}
	}

	public void changeToVencedor(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F3)){
			classes.destroy();
			classes = new Speedster();
			classes.equipShield(new SpeedsterShields.SpeedsterShield(classes));
			classes.equipWeapon(new SpeedsterWeapons.SpeedsterDagger(classes));
		}
	}

	public void changeToSummon(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F4)){
			classes.destroy();
			classes = new Summoner();
		}
	}

	public void changeToImp(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F5)){
			classes.destroy();
			classes = new Imp();
		}
	}


	public void equipBlessedSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
			classes.equipWeapon(new HealerWeapons.BlessedSword(classes));
		}
	}

	public void equipBestSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
			classes.equipWeapon(new HealerWeapons.BestHealerSword(classes));
		}
	}

	public void equipBlessedShield(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
			classes.equipShield(new HealerShields.BlessedShield(classes));
		}
	}

	public void equipMeleeSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
			classes.equipWeapon(new MeleeWeapons.ABat(classes));
		}
	}

	public void equipMeleeShield(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)){
			classes.equipShield(new MeleeShields.MeleeShield(classes));
		}
	}

	public void equipVencedorSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)){
			classes.equipWeapon(new Weapons.VencedorSword(classes));
		}
	}


	private void debug(){

		if (Gdx.input.isKeyJustPressed(Input.Keys.X))
			if(!getReleaseVersion())
				print("Chara real x pos is: " + x + " and simplified x is: " + x/globalSize());
			else
				fixatedText("Chara real x pos is: " + x + " and on-the-grid x is: " + x/globalSize(),300,300,100,Fonts.ComicSans,40);
		if (Gdx.input.isKeyJustPressed(Input.Keys.Y))
			if(!getReleaseVersion())
				print("Chara real y pos is: " + y + " and simplified y is: " + y/globalSize());
			else
				fixatedText("Chara real y pos is: " + y + " and on-the-grid y is: " + y/globalSize(),300,300,100,Fonts.ComicSans,40);


		if (Gdx.input.isKeyJustPressed(Input.Keys.B)){
			Turns.reset();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.I)){
			if(!getReleaseVersion()) {
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
				print("mouse moved? " + mouseMoved);
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
	   		"speedLeft on x: " + speedLeft[0] + "\n"+
			"speedLeft on y: " + speedLeft[1] +"\n"+
			"permittedToAct: " + permittedToAct +"\n"+
			"Weapon: " + classes.weapon +"\n"+
			"Health: " + classes.totalHealth+"\n"+
			"Damage: " + classes.totalDamage+"\n"+
			"Class: " + classes.name+"\n"+
			"WeaponName: " + classes.weapon.weaponName+"\n"+
			"ShieldName: " + classes.shield.shieldName+"\n"+
			"Current health: " + classes.currentHealth+"\n"+
			"Texture" + texture+"\n"+
			"mouse moved? " + mouseMoved+"\n"+
			"Real x: " + x + " simplified x: " + x / globalSize()+"\n"+
			"Real y: " + y + " simplified y: " + y / globalSize(),300,100,500,Fonts.ComicSans,40);
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.T) && isDecidingWhatToDo(this)) {
			if (turnMode) {
				attackMode = !attackMode;
				path.pathReset();
				if (!attackMode)
					cancelAttackMode();
				mouseMoved = true;
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
			if (!attackMode) {
				turnMode = !turnMode;
				path.pathReset();
				fixatedText("turnMode is now: " + turnMode,500,500,100,Fonts.ComicSans,40);
				if (turnMode)
					isOnTheGrid();
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.F8)){
			particle.particleEmitter("BLOB",x+ (float) globalSize() /2,
					y+ (float) globalSize() /2,1, 10,true,false);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F9)){
			stage.enemy.add(new Enemy(x+256,y));
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)){
			quickPlay("test");
			animations.add(new Animation("beneath the mask",x,y));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
			setVolume(getRealVolume() > 0 ? getRealVolume()-10 : 100);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
			setVolume(getRealVolume() <= 100 ? getRealVolume()+10 : 100);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
			setMute(!getMute());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
			stopAll();
			VideoManager.stopAll();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)){
			for (Actor a : actors){
				a.damage(1000, AttackTextProcessor.DamageReasons.UNIVERSAL,null);
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
			if(!getReleaseVersion())
				print(stage+"");
			else fixatedText("Current Stage: " + stage,300,200,200,Fonts.ComicSans,40);
			classes.currentHealth = classes.totalHealth;
			fixatedText("Healed to max hp",400,400,200,Fonts.ComicSans,40);
		}
		changeTo();
		if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
			damage(20, AttackTextProcessor.DamageReasons.SELF,null);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
		setTakeEnemiesIntoConsideration((byte) (-1* getTakeEnemiesIntoConsideration() + 1));
		print("takenemiesintoconsideration is " + getTakeEnemiesIntoConsideration());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.U)){
			fixatedText("Version: B",400,200,100, Fonts.ComicSans,40);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
			classes.attacksIgnoreTerrain = !classes.attacksIgnoreTerrain;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.N)){
			new ControllableFriend(x,y+128,"animaAnnoyed",100).softlockOverridable();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.Y)){
			addField(FieldEffects.FieldNames.LIGHTNING);
		}


	}

}
