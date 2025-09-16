package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Settings;
import com.mygdx.game.Utils;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.*;
import com.mygdx.game.items.characters.equipment.Weapons;
import com.mygdx.game.items.characters.equipment.shields.HealerShields;
import com.mygdx.game.items.characters.equipment.shields.MeleeShields;
import com.mygdx.game.items.characters.equipment.shields.TankShields;
import com.mygdx.game.items.characters.equipment.weapons.HealerWeapons;
import com.mygdx.game.items.characters.equipment.weapons.MeleeWeapons;


import java.util.ArrayList;

import static com.mygdx.game.GameScreen.*;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.AudioManager.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.Enemy.enemies;
import static com.mygdx.game.items.Interactable.interactables;
import static com.mygdx.game.items.Stage.betweenStages;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static com.mygdx.game.items.VideoManager.*;
import static java.lang.Math.*;
import static java.lang.Math.pow;

public class Character extends Actor implements Utils {

	public CharacterClasses classes;

	OnVariousScenarios oVS2;

	public boolean attackMode = false;
	public float lastClickX, lastClickY;
	boolean willDoNormalTextureChange = true;

	TextureManager.Animation walkingAnimation;


	public Character(float x, float y, float base, float height) {
		super("char",x,y,base,height);
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
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
				texture = "char";
			}
		};
		team = 1;
		classes = new Classless();
		classes.character = this;
		path = new Path(x,y, classes.speed,this);
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



	public void update(){
		classes.update();
		speed = classes.speed;
		actingSpeed = classes.attackSpeed;
		range = classes.range;
		pierces = classes.pierces;
		onDeath();
		path.getStats(x,y, classes.speed);

		if (!attackMode)
			movement();
		else
			attack();

		if (!turnMode)
			interact();


		textureUpdater();
		debug();
		path.render();
		attackRenderer();
		conditions.render();
		render();
	}


	public void attackRenderer(){
		for (int i = 0; i < attacks.size(); i++){
			if(attacks.get(i).render) {
				byte counter = 0;
				for (int j = i; j < attacks.size(); j++) {
					if (attacks.get(j).targetX == attacks.get(i).targetX && attacks.get(j).targetY == attacks.get(i).targetY && attacks.get(j) != attacks.get(i))
						counter++;
				}
				addToList("attackIndicator", attacks.get(i).targetX - 5 * counter, attacks.get(i).targetY - 5 *counter, 1,
						0, 256, attacks.get(i).isBeingExecuted ? 20 : 256, attacks.get(i).isBeingExecuted ? 68 : 256);
			}
		}
	}






	Entity targetsTarget = new Entity("default",x,y,false);
	TextureManager.Animation target;
	Tile.Circle circle;
	boolean mouseMoved;
	float[] lastRecordedMousePos = new float[]{.1f,0.264f};
	private void targetProcesor(){
		if (circle == null || circle.center != stage.findATile(x,y) || circle.tileset != stage.tileset || circle.radius != classes.range || !circle.walkable) {
			if (circle != null)
				for (Tile t : circle.circle)
					for (int i = 0; i < 9; i++)
						t.texture.setSecondaryTexture(null,0.8f,0,false,false,i);
			circle = new Tile.Circle(stage.findATile(x, y), stage.tileset, classes.range, true,classes.attacksIgnoreTerrain);

		}
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
			target = new TextureManager.Animation("target", targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
			animations.add(target);
		}
		if (target.finished){
			target = new TextureManager.Animation("target", targetsTarget){public void updateOverridable() {if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) this.stop();}};
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


	public void attackDetector(){
		ArrayList<Actor> temp = new ArrayList<>();
		temp.add(this);
		classes.runAttack();
		ArrayList<Actor> list = null;
		try {
			list = rayCasting(x, y, attacks.get(elementOfAttack - 1).targetX, attacks.get(elementOfAttack - 1).targetY,temp, classes.pierces,this);
		} catch (Exception e) {
			printErr("attackState is " + attackMode);
			e.printStackTrace();
		}
		if (list != null)
			for (Actor aa : list) {
				aa.damage(classes.outgoingDamage(), AttackTextProcessor.DamageReasons.MELEE);
				if (!classes.pierces)
					break;
			}
		else
			text("Missed!", attacks.get(elementOfAttack -  1).targetX,attacks.get(elementOfAttack -  1).targetY + 140,60, TextureManager.Fonts.ComicSans,40,127,127,127,1,30);
		attacks.get(elementOfAttack - 1).render = false;
	}


	protected void attackInput() {
		targetProcesor();
		if(Gdx.input.justTouched()) {
			Vector3 temporal = roundedClick();
			if (circle.findATile(temporal.x,temporal.y) != null) {
				attacks.add(new Attack(temporal.x, temporal.y));
				if (classes.runOnAttackDecided())
					actionDecided();
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (circle.findATile(targetsTarget.x,targetsTarget.y) != null && !(targetsTarget.x == x && targetsTarget.y == y)) {
				attacks.add(new Attack(targetsTarget.x, targetsTarget.y));
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
		willDoNormalTextureChange = false;
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
			walkingAnimation = new TextureManager.Animation(walkingFile, this){
				public void onFinish() {
					entityToFollow.texture = "char";
					willDoNormalTextureChange = true;
				}};

			animations.add(walkingAnimation);
		}
	}

	public void textureUpdater(){
			texture(false);
			switch (textureOrientation) {
				case 0: texture = "CharaDiagonalDownRight"; animationWalking();	 break;
				case 1: texture = "CharaRight";				animationWalking();	 break;
				case 2: texture = "CharaDiagonalUpRight";   animationWalking();  break;
				case 3: texture = "CharaDiagonalDownLeft"; 	animationWalking();	 break;
				case 4: texture = "CharaLeft";				animationWalking();  break;
				case 5: texture = "CharaDiagonalUpLeft";   	animationWalking();	 break;
				case 6: texture = "char"; 					animationWalking();  break;
				case 8: texture = "CharaUp";				animationWalking();  break;
			}

		if (speedLeft[0] == 0 && speedLeft[1] == 0 && lastTimeTilLastMovement >= 2 && !isGliding) {
			texture = "char";
			if (walkingAnimation != null)
				walkingAnimation.stop();
		}
		if (alreadyTextured)
			print(textureOrientation +" is the texuter orientation");
	}

	public void damageOverridable(float damage, AttackTextProcessor.DamageReasons damageReason){
		classes.damage(damage,damageReason);
	}


	public void onDeath(){
		if (classes.currentHealth <= 0)
			isDead = false;
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
			classes = new Tank();
			classes.equipShield(new TankShields.TankShield(classes));
		}
	}

	public void changeToMelee(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F2)){
			classes.destroy();
			classes = new Melee();
		}
	}

	public void changeToVencedor(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F3)){
			classes.destroy();
			classes = new Vencedor();
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
				fixatedText("speedLeft on x: " + speedLeft[0] + "\n"+
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
			animations.add(new TextureManager.Animation("beneath the mask",x,y));
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
			AudioManager.stopAll();
			VideoManager.stopAll();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)){
			for (Actor a : actors){
				a.damage(1000, AttackTextProcessor.DamageReasons.UNIVERSAL);
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
			if(!getReleaseVersion())
				print(stage+"");
			else fixatedText("Current Stage: " + stage,300,200,200,Fonts.ComicSans,40);
		}
		changeTo();
		if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
			damage(20, AttackTextProcessor.DamageReasons.SELF);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
		setTakeEnemiesIntoConsideration((byte) (-1* getTakeEnemiesIntoConsideration() + 1));
		print("takenemiesintoconsideration is " + getTakeEnemiesIntoConsideration());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.U)){
			fixatedText("Version: B",400,200,100, TextureManager.Fonts.ComicSans,40);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
			classes.attacksIgnoreTerrain = !classes.attacksIgnoreTerrain;
		}

	}

}
