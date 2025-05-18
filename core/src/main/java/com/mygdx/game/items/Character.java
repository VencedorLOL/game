package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Settings;
import com.mygdx.game.Utils;
import com.mygdx.game.items.characters.CharacterClasses;
import com.mygdx.game.items.characters.classes.Healer;
import com.mygdx.game.items.characters.classes.Melee;
import com.mygdx.game.items.characters.classes.Vencedor;
import com.mygdx.game.items.characters.equipment.Shields;
import com.mygdx.game.items.characters.equipment.Weapons;
import com.mygdx.game.items.characters.equipment.shields.HealerShields;
import com.mygdx.game.items.characters.equipment.shields.MeleeShields;
import com.mygdx.game.items.characters.equipment.weapons.HealerWeapons;
import com.mygdx.game.items.characters.equipment.weapons.MeleeWeapons;


import java.util.ArrayList;
import java.util.Set;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.AudioManager.*;
import static com.mygdx.game.items.ClickDetector.*;
import static com.mygdx.game.items.TextureManager.animationToList;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.Turns.didTurnJustPass;
import static com.mygdx.game.items.Turns.isDecidingWhatToDo;
import static java.lang.Math.*;

public class Character extends Actor implements Utils {

	public CharacterClasses character = new CharacterClasses();

	OnVariousScenarios oVE;

	// Used when the turn is being controlled by classes
	public boolean hasAttacked;

	public Vector3 attacksCoordinate;
	public boolean attackMode = false;
	public float lastClickX, lastClickY;
	int[] attackDirection = new int[2];
	boolean gridMode = true;
	boolean willDoNormalTextureChange = true;

	public Character(){}

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
		path = new Path(x,y,character.speed,stage);
		oVE = new OnVariousScenarios(){
			@Override
			public void onStageChange(){
				if(pathFindAlgorithm == null)
					pathFindAlgorithm = new PathFinder(stage);
				PathFinder.reset(stage);
				attackMode = false;
				canDecide[0] = true; canDecide[1] = true;

			}
		};
		team = 1;
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
		if(touchDetect()){
			lastClickX = flooredClick().x;
			lastClickY = flooredClick().y;
			pathFinding();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
			pathFinding();
		}
	}



	public void finalizedAttack(){
		if (numberOfHits == 0)
			print("Hit nothing!");
		else if (numberOfHits > 1)
			print("Hit " + numberOfHits + " times!");
		else
			print("Hit 1 time!");
		speedLeft[0] = 0;
		speedLeft[1] = 0;
		canDecide[0] = true;
		spendTurn();
		attacksCoordinate = null;
		attackDirection[0] = 0;
		attackDirection[1] = 0;
	}

	private void pathFinding(){
	/*	if(pathFindAlgorithm == null)
			pathFindAlgorithm = new PathFinder(stage);
		path.pathReset();
		PathFinder.reset(stage);
		pathFindAlgorithm.setStart(x,y);
		pathFindAlgorithm.setEnd(lastClickX,lastClickY);
		pathFindAlgorithm.solve();
		if (pathFindAlgorithm.algorithm.getPath() != null){
			path.setPathTo(pathFindAlgorithm.getSolvedPath());
		} else
			print("no path found");
*/	}



	public void update(Stage stage, GameScreen cam){
		speed = character.speed;
		range = character.range;
		if(didTurnJustPass)
			canDecide[1] = true;
		onDeath();
		character.update(this);
		actingSpeed = character.attackSpeed;
		path.getStats(x,y,character.speed);

		if (!attackMode)
			movement();
		else
			attack();

		textureUpdater();

		debug(cam);

		path.render();
		render();
	}

	int numberOfHits = 0;
	public void attack(){
		testCollision.x = x;
		testCollision.y = y;
		if (isPermittedToAct()) {
			if (!path.pathEnded){
				attackDetector();
			}

		}
		else if (canDecide() && isDecidingWhatToDo(this))
			attackInput();

		super.refresh(texture,x, y, base, height);
	}


	private void attackDetector(){
		attackDirection = path.pathProcess(this);
		for (Actor e : stage.enemy)
			if (path.getCurrentPathCoords()[0] == e.x && path.getCurrentPathCoords()[1] == e.y){
				e.damage(character.outgoingDamage(), "Melee");
				numberOfHits++;
				if (!character.pierces){
					path.pathEnded = true;
					finalizedAttack();
					return;
				}
			}
		if (path.pathEnded) {
			finalizedAttack();
			return;
		}
		attackDetector();
	}

	protected void attackInput(){
		Tile.Circle circle = new Tile.Circle(stage.findATile(x,y),stage.tileset, character.range); 
	}

	private void automatedAttack(){
		if(touchDetect()){
			lastClickX = flooredClick().x;
			lastClickY = flooredClick().y;
			pathFinding();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
			pathFinding();
		}
	}







	boolean didntRunMovementMethodYetEver = true;
	public byte texture(){
		didntRunMovementMethodYetEver = false;
		byte movement = 7;
		if (speedLeft[0] > 0)
			movement = 1;
		if (speedLeft[0] < 0)
			movement = 4;
		if (speedLeft[1] > 0)
			movement++;
		if (speedLeft[1] < 0)
			movement--;
		return movement;
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
		textureUpdater();
	}


	public void textureUpdater(){
		if (willDoNormalTextureChange) {
			switch (texture()) {
				case 0: texture = "CharaDiagonalDownRight"; break;
				case 1: texture = "CharaRight";			    break;
				case 2: texture = "CharaDiagonalUpRight";   break;
				case 3: texture = "CharaDiagonalDownLeft";  break;
				case 4: texture = "CharaLeft";              break;
				case 5: texture = "CharaDiagonalUpLeft";    break;
				case 6: texture = "char";                   break;
				case 8: texture = "CharaUp";                break;
			}
			if (didntRunMovementMethodYetEver)
				texture = "char";
		}
	}




	public void onDeath(){
		if (character.currentHealth <= 0) {
			isDead = false;
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
			character = new Healer();
		}
	}

	public void changeToMelee(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F2)){
			character = new Melee();
		}
	}

	public void changeToVencedor(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.F3)){
			character = new Vencedor();
		}
	}


	public void equipBlessedSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
			character.equipWeapon(new HealerWeapons.BlessedSword());
		}
	}

	public void equipBestSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
			character.equipWeapon(new HealerWeapons.BestHealerSword());
		}
	}

	public void equipBlessedShield(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
			character.equipShield(new HealerShields.BlessedShield());
		}
	}

	public void equipMeleeSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
			character.equipWeapon(new MeleeWeapons.ABat());
		}
	}

	public void equipMeleeShield(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)){
			character.equipShield(new MeleeShields.MeleeShield());
		}
	}

	public void equipVencedorSword(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)){
			character.equipWeapon(new Weapons.VencedorSword());
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

		if (Gdx.input.isKeyPressed(Input.Keys.I)){
			print("canDecide: " + (canDecide[1] && canDecide[0]));
			print("speedLeft on x: " + speedLeft[0]);
			print("speedLeft on y: " + speedLeft[1]);
			print("permittedToAct: " + permittedToAct);
			print("Weapon" + character.weapon);
			print("Health: " + character.totalHealth);
			print("Damage: " + character.totalDamage);
			print("Current health: " + character.currentHealth);
			print("Texture" + texture);
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
			attackMode = !attackMode;
			path.pathReset();
			print("attackMode is now: "+attackMode);
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
			turnMode = !turnMode;
			print("turnMode is now: "+ turnMode);
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
		changeTo();
	}

}
