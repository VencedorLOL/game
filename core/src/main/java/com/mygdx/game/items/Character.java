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

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.AudioManager.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.Interactable.interactables;
import static com.mygdx.game.items.Stage.betweenStages;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.TextureManager.text;
import static com.mygdx.game.items.VideoManager.*;
import static java.lang.Math.round;

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
			@Override
			public void onStageChange(){
				attackMode = false;
				canDecide[0] = true; canDecide[1] = true;
				speedLeft[0] = 0;    speedLeft[1] = 0;
				didItAct = false;
				permittedToAct = false;
				texture = "char";
			}

			@Override
			public void onTurnPass(){
				canDecide[1] = true;
			}
		};
		team = 1;
		classes = new Classless();
		classes.character = this;
		path = new Path(x,y, classes.speed);
	}

	public void spendTurn(){
		printErr("Called Spend Turn");
		permittedToAct = false;
		path.pathStart();
		isOnTheGrid();
	}

	public void permitToMove(){
		//print("permitted to move");
		permittedToAct = true;
	}

	public boolean isPermittedToAct() {
		return permittedToAct;
	}

	protected void automatedMovement(){
		if(Gdx.input.justTouched()){
			lastClickX = roundedClick().x;
			lastClickY = roundedClick().y;
			print("last ckik x " + lastClickX + " y " + lastClickY);
			pathFinding();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
			pathFinding();
		}
	}



	private void pathFinding(){
		path.pathReset();
		if (pathFindAlgorithm.quickSolve(x,y,lastClickX,lastClickY, getTakeEnemiesIntoConsideration()))
			path.setPathTo(pathFindAlgorithm.convertTileListIntoPath());
		else
			print("no path found");
	}



	public void update(GameScreen cam){
		classes.update();
		speed = classes.speed;
		actingSpeed = classes.attackSpeed;
		range = classes.range;
		onDeath();
		actingSpeed = classes.attackSpeed;
		path.getStats(x,y, classes.speed);

		if (!attackMode)
			movement();
		else
			attack();

		if (!turnMode)
			interact();


		textureUpdater();
		debug(cam);
		path.render();
		render();
	}

	int numberOfHits = 0;








	Entity targetsTarget = new Entity("default",x,y,false);
	TextureManager.Animation target;
	Tile.Circle circle;
	boolean mouseMoved;
	float[] lastRecordedMousePos = new float[]{.1f,0.264f};
	private void targetProcesor(){
		if (circle == null || circle.center != stage.findATile(x,y) || circle.tileset != stage.tileset || circle.radius != classes.range || !circle.walkable) {
			if (circle != null)
				for (Tile t : circle.circle)
					t.texture.setSecondaryTexture(null);
			circle = new Tile.Circle(stage.findATile(x, y), stage.tileset, classes.range, true);

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
			target = new TextureManager.Animation("target", targetsTarget);
			animations.add(target);
		}
		if (target.finished){
			target = new TextureManager.Animation("target", targetsTarget);
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
		if (circle != null)
			for (Tile t : circle.circle)
				t.texture.setSecondaryTexture(null);
		circle = null;
		animations.remove(target);
		target = null;
	}


	public void attackDetector(){
		ArrayList<Actor> temp = new ArrayList<>();
		temp.add(this);
		for (Attack a : attacks) {
			classes.runAttack();
			ArrayList<Actor> list = rayCasting(x, y, a.targetX, a.targetY,temp, false,this);
			if (list != null)
				for (Actor aa : list) {
					aa.damage(classes.outgoingDamage(), "Melee");
					numberOfHits++;
					if (!classes.pierces)
						break;
				}
		}
		finalizedTurn();
	}


	protected void attackInput() {
		targetProcesor();
		if(Gdx.input.justTouched()) {
			Vector3 temporal = roundedClick();
			if (circle.findATile(temporal.x,temporal.y) != null) {
				attacks.add(new Attack(temporal.x, temporal.y));
				canDecide = new boolean[]{false, false};
				thisTurnVSM = getVisualSpeedMultiplier();
				if (classes.runOnAttackDecided())
					actionDecided();
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (circle.findATile(targetsTarget.x,targetsTarget.y) != null && !(targetsTarget.x == x && targetsTarget.y == y)) {
				attacks.add(new Attack(targetsTarget.x, targetsTarget.y));
				canDecide = new boolean[]{false, false};
				thisTurnVSM = getVisualSpeedMultiplier();
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

	public void damageOverridable(float damage, String damageReason){
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

	ArrayList<Tile> dumpList;
	protected void softlockOverridable() {
		if (overlapsWithStage(stage,this) && !betweenStages){
			print("SOFTLOCK SOFTLOCK");
			for (Tile t : stage.tileset)
				t.hasBeenChecked = false;
			dumpList = new ArrayList<>();
			ArrayList<Tile> currentTilesetChecking = new ArrayList<>();
			currentTilesetChecking.add(Tile.findATile(stage.tileset,(float) (globalSize() * round(this.x / globalSize())),(float) (globalSize() * round(this.y / globalSize()))));
			if(currentTilesetChecking.get(0) != null && currentAnalize(currentTilesetChecking.get(0))) {
				dumpList = null;
				return;
			}
			for (int i = 1; i <= stage.tileset.size(); i++) {
				currentTilesetChecking = (ArrayList<Tile>) dumpList.clone();
				dumpList.clear();
				for (Tile t : currentTilesetChecking)
					if (currentAnalize(t)) {
						dumpList = null;
						return;
					}
			}
		}
	}

	private boolean currentAnalize(Tile currentTile){
		ArrayList<Tile> neighbours = currentTile.walkableOrthogonalTiles(stage.tileset);
		for (Tile t : neighbours)
			if (!t.hasBeenChecked) {
				t.hasBeenChecked = true;
				dumpList.add(t);
				dumpList.removeIf(tt -> t == tt);
				testCollision.x = t.x; testCollision.y = t.y;
				if (!overlapsWithStage(stage,testCollision))
					x = t.x; y = t.y; return true;
			}
		return false;
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


	private void debug(GameScreen cam){

		if (Gdx.input.isKeyJustPressed(Input.Keys.X))
			print("Chara real x pos is: " + x + " and simplified x is: " + x/globalSize());
		if (Gdx.input.isKeyJustPressed(Input.Keys.Y))
			print("Chara real y pos is: " + y + " and simplified y is: " + y/globalSize());


		if (Gdx.input.isKeyJustPressed(Input.Keys.B)){
			canDecide[0] = true; canDecide[1] = true;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.I)){
			print("canDecide: " + (canDecide[1] && canDecide[0]));
			print("speedLeft on x: " + speedLeft[0]);
			print("speedLeft on y: " + speedLeft[1]);
			print("permittedToAct: " + permittedToAct);
			print("Weapon: " + classes.weapon);
			print("Health: " + classes.totalHealth);
			print("Damage: " + classes.totalDamage);
			print("Class: " + classes.name);
			print("Weapon: " + classes.weapon.weaponName);
			print("Shield: " + classes.shield.shieldName);
			print("Current health: " + classes.currentHealth);
			print("Texture" + texture);
			print("mouse moved? " + mouseMoved);
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.V) && canDecide[0] && canDecide[1]) {
			Settings.setFastMode(!Settings.getFastMode());
			print("FastMode is now "+Settings.getFastMode());
			if (Settings.getFastMode())
				setVisualSpeedMultiplier(32);
			else
				setVisualSpeedMultiplier(8);

		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.T) && canDecide()) {
			if (turnMode) {
				attackMode = !attackMode;
				path.pathReset();
				print("attackMode is now: " + attackMode);
				if (!attackMode)
					cancelAttackMode();
				mouseMoved = true;
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
			if (!attackMode) {
				turnMode = !turnMode;
				path.pathReset();
				print("turnMode is now: " + turnMode);
				if (turnMode)
					isOnTheGrid();
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.F8)){
			cam.particle.particleEmitter("BLOB",x+ (float) globalSize() /2,
					y+ (float) globalSize() /2,1, 10,true,false);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F9)){
			stage.enemy.add(new Enemy(x+256,y));
		}

		if(Gdx.input.isKeyPressed(Input.Keys.N)){
			//	stop("test");
			//	load("test",true);
			//	play("test");
			//	print("Size is: "+ sounds.size());
			texture = null;
			willDoNormalTextureChange = false;
			animations.add(new TextureManager.Animation("gliding circle",x,y){
				@Override
				public void onFinish(){
					finished = true;
					Character chara = null;
					for (Entity cha : Entity.entityList)
						if (cha instanceof Character)
							chara = (Character) cha;
					Camara.attach(chara);
					assert chara != null;
					chara.texture = "char";
					chara.willDoNormalTextureChange = true;
					print("HEYOO");
				}
			});
			//	Camara.attach(animations.get(0));
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
			texture = null;
			willDoNormalTextureChange = false;
			animations.add(new TextureManager.Animation("walkingright",x,y){
				@Override
				public void onFinish(){
					finished = true;
					Character chara = null;
					for (Entity cha : Entity.entityList)
						if (cha instanceof Character)
							chara = (Character) cha;
					Camara.attach(chara);
					assert chara != null;
					chara.texture = "char";
					chara.willDoNormalTextureChange = true;
					print("HEYOO");
				}
			});
			//	Camara.attach(animations.get(0));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
			quickPlay("test1");
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
			quickPlay("test2");
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
			quickPlay("test3");
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
			quickPlay("test1");
			quickPlay("test2");
			quickPlay("test3");
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
				a.damage(1000,"God Damage");
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
			print(stage+"");
		}
		changeTo();
		if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
			damage(20,"SelfDamage");
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
			createVideo(x,y);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
		setTakeEnemiesIntoConsideration((byte) (-1* getTakeEnemiesIntoConsideration() + 1));
		print("its " + getTakeEnemiesIntoConsideration());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.U)){
			text("Version: A",x,y,100, TextureManager.Fonts.ComicSans,40,150,150,150,1,100);
		}

	}


}
