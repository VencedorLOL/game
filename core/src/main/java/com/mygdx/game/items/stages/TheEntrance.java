package com.mygdx.game.items.stages;

import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.enemies.Soldier;
import com.mygdx.game.items.textboxelements.Textbox;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.savefile;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.Utils.deparalyzeCharacter;

public class TheEntrance extends Stage {
	public TheEntrance(){
		startX 		= 0;
		startY 		= 0;
		finalX 		= 19;
		finalY 		= 19;
		spawnX 		= 0;
		spawnY 		= 10;
		wallX			= new int[]{-1, -1, -1, 7, 7, 7, 7, 7, 7, 7, 7, 7, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 7, 7, 6, 5, 2, 7, 7, 6, 5, 7, 7, 2, 1, 0, 7, 7, 7, 18, 18, 18, 18, 18, 18, 19, 19, 18, 18, 18, 18, 18, 18, 18, 18, 19, 8, 8, 9, 9, 10, 8, 10, 10, 10, 9, 8, 9, 8, 9, 10, 11, 11, 11, 11, 11, 12, 12, 12, 13, 13, 14, 14, 13, 12, 14, 15, 6, 8, 9, 10, 14, 14, 17, 15, 15, 15, 15, 14, 18, 19, 19, 19, 19, 19, 19, 13, 19, 19, 19, 19, 19, 19, 20, 21, 22, 23, 24, 24, 24, 24, 24, 24, 24, 23, 22, 21, 20, 20, 21, 22, 23, 23, 22, 21, 20, 20, 21, 22, 23, 23, 22, 21, 21, 20, 20, 22, 23, 20, 21, 22, 23, 24, 24, 24, 24, 24, 24, 23, 22, 21, 20, 20, 21, 22, 23, 23, 22, 21, 20, 20, 21, 21, 22, 22, 23, 23, 24, 23, 24, 23, 22, 22, 21, 20, 20, 21, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 2, 2, 0, -1, -1, -1, -1, -1, 0, 1, 2, 2, 1, 1, 0, 0, 0, 4, 5, 5, 5, 6, 6, 6, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 8, 9, 10, 11, 12, 13, 14, 15, 15, 14, 13, 12, 11, 10, 9, 10, 11, 9, 7, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 13, 13, 14, 15, 12, 10, 9, 11, 12, 12, 13, 14, 15, 15, 14, 14, 13, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
		wallY			= new int[]{10, 11, 9, 15, 14, 13, 12, 9, 8, 7, 6, 5, 6, 5, 4, 3, 5, 4, 3, 2, 1, 0, 0, 1, 2, 4, 3, 3, 3, 3, 16, 17, 17, 17, 18, 19, 17, 17, 17, 2, 1, 0, 13, 14, 15, 16, 17, 18, 13, 9, 9, 8, 7, 6, 5, 4, 3, 2, 2, 2, 3, 3, 4, 4, 4, 3, 2, 1, 1, 1, 2, 0, 0, 0, 0, 1, 2, 3, 4, 3, 2, 1, 1, 2, 2, 1, 0, 0, 0, 0, 0, 16, 16, 16, 4, 3, 4, 19, 18, 17, 16, 16, 19, 19, 18, 17, 16, 15, 14, 16, 8, 7, 6, 5, 4, 3, 13, 13, 13, 13, 13, 14, 15, 16, 17, 18, 19, 19, 19, 19, 19, 18, 18, 18, 18, 17, 17, 17, 17, 16, 16, 16, 16, 15, 15, 15, 14, 14, 15, 14, 14, 2, 2, 2, 2, 2, 5, 6, 7, 8, 9, 9, 9, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 6, 6, 5, 5, 6, 6, 5, 4, 4, 3, 3, 3, 4, 4, 4, 3, 3, 5, 12, 13, 14, 15, 16, 17, 18, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -1, -1, -2, -3, -3, -4, -2, -2, -3, -4, -5, -6, -6, -6, -6, -5, -5, -4, -4, -3, -5, -6, -6, -5, -4, -4, -3, -2, -1, -3, -5, -6, -5, -4, -3, -2, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, -2, -2, -2, -2, -2, -2, -3, -3, -3, -3, -4, -6, -6, -5, -5, -4, -4, -5, -5, -4, -4, -4, -3, -3, -3, -3, -6, -6, -6, -6, -5, -5, -4, -4, -5, -6, -5, -6, -6, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
		wallType		= new int[]{6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 7, 7, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
		enemySpawnX		= new int[]{4, 11, 12, 16, 17, 16, 15};
		enemySpawnY 	= new int[]{17, 16, 16, 16, 16, 4, 4};
		enemyType		= new int[]{3, 3, 3, 3, 3, 3, 3};
		screenWarpX 	= new int[]{24};
		screenWarpY		= new int[]{10};
		screenWarpIsHorizontal	= new boolean[]{false};
		screenWarpAlignment		= new boolean[]{false};
		screenWarpSize			= new float[]{3};
		screenWarpType			= new int[]{0};
		screenWarpDestinationSpecification = new byte[]{0};
		floorTexture = "Grass";
		bgTexture = "default";
		staticCameraXmin = true;
		staticCameraXmax = true;
		staticCameraYmin = true;
		staticCameraYmax = true;
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new TheBase());
	}

	public void tilesetCleanup() {
			tileset.remove(getTile(18.0,2.0));
			tileset.remove(getTile(19.0,2.0));
			tileset.remove(getTile(18.0,3.0));
			tileset.remove(getTile(19.0,3.0));
			tileset.remove(getTile(18.0,4.0));
			tileset.remove(getTile(19.0,4.0));
			tileset.remove(getTile(18.0,5.0));
			tileset.remove(getTile(19.0,5.0));
			tileset.remove(getTile(18.0,6.0));
			tileset.remove(getTile(19.0,6.0));
			tileset.remove(getTile(18.0,7.0));
			tileset.remove(getTile(19.0,7.0));
			tileset.remove(getTile(18.0,8.0));
			tileset.remove(getTile(19.0,8.0));
			tileset.remove(getTile(18.0,9.0));
			tileset.remove(getTile(19.0,9.0));
			tileset.remove(getTile(18.0,13.0));
			tileset.remove(getTile(19.0,13.0));
			tileset.remove(getTile(18.0,14.0));
			tileset.remove(getTile(19.0,14.0));
			tileset.remove(getTile(18.0,15.0));
			tileset.remove(getTile(19.0,15.0));
			tileset.remove(getTile(18.0,16.0));
			tileset.remove(getTile(19.0,16.0));
			tileset.remove(getTile(18.0,17.0));
			tileset.remove(getTile(19.0,17.0));
			tileset.remove(getTile(18.0,18.0));
			tileset.remove(getTile(19.0,18.0));
			tileset.remove(getTile(18.0,19.0));
			tileset.remove(getTile(19.0,19.0));
			tileset.add(createTile(6.0,20.0));
			tileset.add(createTile(5.0,20.0));
			tileset.add(createTile(4.0,20.0));
			tileset.add(createTile(3.0,20.0));
			tileset.add(createTile(2.0,20.0));
			tileset.add(createTile(1.0,20.0));
			tileset.add(createTile(0.0,20.0));
			tileset.add(createTile(0.0,21.0));
			tileset.add(createTile(1.0,21.0));
			tileset.add(createTile(2.0,21.0));
			tileset.add(createTile(3.0,21.0));
			tileset.add(createTile(4.0,21.0));
			tileset.add(createTile(5.0,21.0));
			tileset.add(createTile(6.0,21.0));
			tileset.add(createTile(6.0,22.0));
			tileset.add(createTile(5.0,22.0));
			tileset.add(createTile(4.0,22.0));
			tileset.add(createTile(3.0,22.0));
			tileset.add(createTile(2.0,22.0));
			tileset.add(createTile(1.0,22.0));
			tileset.add(createTile(0.0,22.0));
			tileset.add(createTile(0.0,23.0));
			tileset.add(createTile(0.0,24.0));
			tileset.add(createTile(1.0,24.0));
			tileset.add(createTile(2.0,24.0));
			tileset.add(createTile(2.0,23.0));
			tileset.add(createTile(3.0,23.0));
			tileset.add(createTile(4.0,23.0));
			tileset.add(createTile(5.0,23.0));
			tileset.add(createTile(6.0,23.0));
			tileset.add(createTile(6.0,24.0));
			tileset.add(createTile(5.0,24.0));
			tileset.add(createTile(4.0,24.0));
			tileset.add(createTile(3.0,24.0));
			tileset.add(createTile(1.0,23.0));
			tileset.add(createTile(1.0,25.0));
			tileset.add(createTile(2.0,25.0));
			tileset.add(createTile(3.0,25.0));
			tileset.add(createTile(4.0,25.0));
			tileset.add(createTile(5.0,25.0));
			tileset.add(createTile(4.0,26.0));
			tileset.add(createTile(4.0,27.0));
			tileset.add(createTile(3.0,26.0));
			tileset.add(createTile(2.0,26.0));
			tileset.add(createTile(2.0,27.0));
			tileset.add(createTile(3.0,27.0));
			tileset.add(createTile(3.0,28.0));
			tileset.add(createTile(4.0,28.0));
			tileset.add(createTile(4.0,29.0));
			tileset.add(createTile(5.0,-1.0));
			tileset.add(createTile(4.0,-1.0));
			tileset.add(createTile(3.0,-1.0));
			tileset.add(createTile(2.0,-1.0));
			tileset.add(createTile(2.0,-2.0));
			tileset.add(createTile(3.0,-2.0));
			tileset.add(createTile(4.0,-2.0));
			tileset.add(createTile(5.0,-2.0));
			tileset.add(createTile(4.0,-3.0));
			tileset.add(createTile(3.0,-3.0));
			tileset.add(createTile(3.0,-4.0));
			tileset.add(createTile(4.0,-4.0));
			tileset.add(createTile(4.0,-5.0));
			tileset.add(createTile(3.0,-5.0));
			tileset.add(createTile(3.0,-6.0));
			tileset.add(createTile(19.0,-1.0));
			tileset.add(createTile(18.0,-1.0));
			tileset.add(createTile(17.0,-1.0));
			tileset.add(createTile(16.0,-1.0));
			tileset.add(createTile(-1.0,-6.0));
			tileset.add(createTile(20.0,1.0));
			tileset.add(createTile(20.0,0.0));
			tileset.add(createTile(20.0,-1.0));
			tileset.add(createTile(21.0,-1.0));
			tileset.add(createTile(21.0,0.0));
			tileset.add(createTile(21.0,1.0));
			tileset.add(createTile(22.0,1.0));
			tileset.add(createTile(22.0,0.0));
			tileset.add(createTile(22.0,-1.0));
			tileset.add(createTile(23.0,0.0));
			tileset.add(createTile(23.0,1.0));
			tileset.add(createTile(23.0,-1.0));
			tileset.add(createTile(20.0,10.0));
			tileset.add(createTile(20.0,11.0));
			tileset.add(createTile(20.0,12.0));
			tileset.add(createTile(21.0,12.0));
			tileset.add(createTile(21.0,11.0));
			tileset.add(createTile(21.0,10.0));
			tileset.add(createTile(22.0,10.0));
			tileset.add(createTile(22.0,11.0));
			tileset.add(createTile(22.0,12.0));
			tileset.add(createTile(23.0,12.0));
			tileset.add(createTile(23.0,11.0));
			tileset.add(createTile(23.0,10.0));
			tileset.add(createTile(24.0,12.0));
			tileset.add(createTile(24.0,11.0));
			tileset.add(createTile(24.0,10.0));
			tileset.add(createTile(24.0,1.0));
			tileset.add(createTile(24.0,0.0));
			tileset.add(createTile(24.0,-1.0));
	}

	Soldier a,b,c,d,e;
	public boolean talkedTo = false;
	public void customEnemySetter() {
		a = new Soldier(7*globalSize(),10*globalSize()){
			public void update(){
				if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !isDead) {
					statsUpdater();
					path.getStats(x,y,totalSpeed);
					loop();
					onDeath();
					if(isDead)
						return;
					conditions.render();
					glideProcess();
					if(chara.x > 12*globalSize() && !executed)
						finalSoldiersWalking();
				}

			}

			public void setAction() {
				Enemy temp = this;
				action = new Interactable(this){
					public void onInteract(Character character) {
						new Textbox(){
							public void onRemoval() {
								iniSoldiersWalking();
							}

							public void onOpenOverridable() {
								framesTilNextLetter = 5;
								if(temp.x == 7*globalSize() && !savefile.getFBool(1))
									setText("Anima: Returning from the mission");
								else
									setText("SName Soldier: Understood, Anima.");
							}};}};}};
		b = new Soldier(7*globalSize(),11*globalSize()){
			public void update(){
				if (haveWallsBeenRendered && haveEnemiesBeenRendered && hasFloorBeenRendered && haveScreenWarpsBeenRendered && !isDead) {
					statsUpdater();
					path.getStats(x,y,totalSpeed);
					loop();
					onDeath();
					if(isDead)
						return;
					conditions.render();
					glideProcess();
					if(chara.x > 12 *globalSize() && !executed)
						finalSoldiersWalking();
				}

			}

			public void setAction() {
				Enemy temp = this;
				action = new Interactable(this){
					public void onInteract(Character character) {
						new Textbox(){
							public void onRemoval() {
								iniSoldiersWalking();
							}

							public void onOpenOverridable() {
								framesTilNextLetter = 5;
								if(temp.x == 7*globalSize() && !savefile.getFBool(1))
									setText("Anima: Returning from the mission");
								else
									setText("SName Soldier: Got it, Anima.");
							}};}};}};
		c = new Soldier(3*globalSize(),3*globalSize()){
			public void setAction() {
				action = new Interactable(this){
					public void onInteract(Character character) {
						new Textbox(){

							public void onOpenOverridable() {
								framesTilNextLetter = 5;
								setText("SpecilTest: You see, I have special text qualities<wait:5>.<wait:10>.<wait:15>. Sometimes, It shakes. Other times, I choose to tint it rainbow. Rarely, I do both.");
								initiateRainbow(1000, 20);
								initiateShake(3, 2);
								setColor(255, 255, 255);
								changeAttribute(0, 130, 135, 0);
								changeAttribute(0, 68, 73, 0);
								changeAttribute(1, 109, 116, 0);
								changeAttribute(1, 130, 135, 0);
								changeAttribute(2, 40, 52, 255);
								changeAttribute(3, 40, 52, 0);
								changeAttribute(4, 40, 52, 0);
							}};}};}};
		d = new Soldier(4*globalSize(),3*globalSize()){
			public void setAction() {
				action = new Interactable(this){
					public void onInteract(Character character) {
						new Textbox(){
							public void onOpenOverridable() {
								framesTilNextLetter = 0;
								setText("<c><r:150><g:250><b:250>hello world, this is a kinda long sentence " +
											"kinda<render:anima>to test the <c><rainbow:200,0>new algorithm <c><r:250><g:20><b:10>i<c> made at " +
											"detecting where line breaks should go. i, at a <red:50>later date, <r:200><g:200><b:60>added <c><rbow:200,0>this part of the text you're reading right now<c> " +
											"to <wait:120><red:255>test <c>whether the new part of textboxes <c><r:250><g:20><b:10>i impemented <c>where textboxes would <shake:10,2>jump to a new textbox<c> to render " +
											"bigger amounts of text would work. to help me <red:200><g:150><b:75>further this goal of testing<c>, <c><r:250><g:20><b:10>i <c>am going to write some random words " +
											"whose purpose is <r:34><g:134><b:234>to let me check accurately that the <c><m:200,10>algorithm i made<c> <setdelay:2>to detect <setdelay:3>where a new <setdelay:5>textbox should start " +
											"works <setdelay:10>properly and <setdelay:20>flawlessly. <setdelay:1><c><r:250><g:20><b:10>penguin. <c><r:40><g:250><b:30>scissors. <c><r:60><g:80><b:250>heavy machinery.");


							}};}};}};
		e = new Soldier(3*globalSize(),17*globalSize()){
			public void setAction() {
				action = new Interactable(this){
					public void onInteract(Character character) {
						new Textbox(){
							public void onOpenOverridable() {
								framesTilNextLetter = 0;
								setText("texture render text ig:" +
										"<render:Spawnpoint>-!!!!!!!!!!\n" +
										"texture render text ig:<render:H>-!!!!!!!!!!<render:SWConfigure><render:crater><render:largeBarricade>");


							}};}};}};
		enemy.add(a);
		enemy.add(b);
		enemy.add(c);
		enemy.add(d);
		enemy.add(e);
		if(savefile.getFBool(1) && !talkedTo)
			if(chara.x < 7 * globalSize())
				iniSoldiersWalking();
			else
				finalSoldiersWalking();

	}

	public void iniSoldiersWalking(){
		if(!talkedTo) {
			talkedTo = true;
			savefile.setFBool(1,true);
			a.glide(globalSize(), 0, 20);
			b.glide(globalSize(), 0, 20);
			new OnVariousScenarios.CounterObject(22) {
				public void onCounterFinish() {
					a.glide(0, -globalSize(), 20);
					b.glide(0, globalSize(), 20);
					new OnVariousScenarios.CounterObject(22) {
						public void onCounterFinish() {
							//im pretty sure i put these for floatin point context precision
							a.x = globalSize()*8;
							a.y = globalSize()*9;
							b.x = globalSize()*8;
							b.y = globalSize()*12;
							a.testCollision.x = a.x;
							a.testCollision.y = a.y;
							b.testCollision.x = b.x;
							b.testCollision.y = b.y;
							deparalyzeCharacter();
						}
					};
				}
			};
		} else deparalyzeCharacter();
	}

	boolean executed = false;
	public void finalSoldiersWalking(){
		talkedTo = true;
		executed = true;
		a.glide(0, +globalSize(), 20);
		b.glide(0, -globalSize(), 20);
		new OnVariousScenarios.CounterObject(22) {
			public void onCounterFinish() {
				a.glide(-globalSize(), 0, 20);
				b.glide(-globalSize(), 0, 20);
				new OnVariousScenarios.CounterObject(22) {
					public void onCounterFinish() {
						a.x = globalSize()*7;
						a.y = globalSize()*10;
						b.x = globalSize()*7;
						b.y = globalSize()*11;
						a.testCollision.x = a.x;
						a.testCollision.y = a.y;
						b.testCollision.x = b.x;
						b.testCollision.y = b.y;
					}
				};
			}
		};
	}



}
