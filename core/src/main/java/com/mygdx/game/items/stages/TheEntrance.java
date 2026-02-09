package com.mygdx.game.items.stages;

import com.mygdx.game.items.*;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.enemies.Soldier;
import com.mygdx.game.items.textboxelements.Textbox;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Utils.deparalyzeCharacter;
import static com.mygdx.game.Utils.paralyzeCharacter;

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
		enemySpawnX		= new int[]{3, 3, 4, 4, 11, 12, 16, 17, 16, 15};
		enemySpawnY 	= new int[]{3, 17, 17, 3, 16, 16, 16, 16, 4, 4};
		enemyType		= new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
		screenWarpX 	= new int[]{24, 24, 24};
		screenWarpY	= new int[]{12, 11, 10};
		screenWarpDestinationSpecification = new byte[]{0, 0, 0};
		floorTexture = "Grass";
		bgTexture = "default";
		staticCameraXmax = true;
		staticCameraXmin = true;
		staticCameraYmax = true;
		staticCameraYmin = true;
		scale();
	}

	public void reStage() {
		screenWarpDestination.add(new StageOne());
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

	Soldier a,b;
	public boolean talkedTo = false;
	public void customEnemySetter() {
		a = new Soldier(7*globalSize(),10*globalSize()){

			public void setAction() {
				Enemy temp = this;
				action = new Interactable(this){
					public void onInteract(Character character) {
						new Textbox(){
							public void beforeRenderOverridable() {
								paralyzeCharacter();
							}

							public void onRemoval() {
								iniSoldiersWalking();
							}

							public void beforeTextOverridable() {
								framesTilNextLetter = 5;
								if(temp.x == 7*globalSize())
									storedText = "Anima: Returning from the \nmission";
								else
									storedText = "SName Soldier: Understood,\nAnima.";
							}};}};}};
		b = new Soldier(7*globalSize(),11*globalSize()){
			public void setAction() {
				Enemy temp = this;
				action = new Interactable(this){
					public void onInteract(Character character) {
						new Textbox(){
							public void beforeRenderOverridable() {
								paralyzeCharacter();
							}

							public void onRemoval() {
								iniSoldiersWalking();
							}

							public void beforeTextOverridable() {
								framesTilNextLetter = 5;
								if(temp.x == 7*globalSize())
									storedText = "Anima: Returning from the \nmission";
								else
									storedText = "SName Soldier: Got it, Anima.";
							}};}};}};
		enemy.add(a);
		enemy.add(b);
	}

	public void iniSoldiersWalking(){
		if(!talkedTo) {
			talkedTo = true;
			a.glide(globalSize(), 0, 20);
			b.glide(globalSize(), 0, 20);
			new OnVariousScenarios.CounterObject(22) {
				public void onCounterFinish() {
					a.glide(0, -globalSize(), 20);
					b.glide(0, globalSize(), 20);
					new OnVariousScenarios.CounterObject(22) {
						public void onCounterFinish() {
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

}
