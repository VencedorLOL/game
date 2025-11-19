package com.mygdx.game.items;

import java.util.ArrayList;
import java.util.Collections;

import static com.mygdx.game.GameScreen.*;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.StartScreen.startAsPathfinding;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.Enemy.enemies;
import static com.mygdx.game.items.Enemy.enemyGrid;
import static com.mygdx.game.items.Friend.allaiesGrid;
import static com.mygdx.game.items.Friend.friend;
import static com.mygdx.game.items.TextureManager.fixatedText;
import static com.mygdx.game.items.Tile.findATile;
import static java.lang.Math.abs;
import static java.lang.Float.POSITIVE_INFINITY;
import static java.lang.System.nanoTime;


public class PathFinder {
	public static ArrayList<Tile> grid  	 = new ArrayList<>();
	public static ArrayList<Tile> actorGrid  = new ArrayList<>();
	public static OnVariousScenarios oVE;
	Tile startTile;
	Tile objectiveTile;
	public ArrayList<Tile> solution = new ArrayList<>();
	Boolean needsReset = new Boolean(true);
	public static ArrayList<Boolean> resetList = new ArrayList<>();
	float minimunDistance = POSITIVE_INFINITY;
	Report timeReporter;

	static{
		oVE = new OnVariousScenarios(){
			public void onStageChange() {
				for (Boolean b : resetList) b.bool = true;
				generateGrids();
			}
			public void onTurnPass() {
				for (Boolean b : resetList) b.bool = true;
				generateGrids();
			}
		};
	}

	public PathFinder(){
		generateGrids();
		resetList.add(needsReset);
		if(startAsPathfinding){
			timeReporter = new Report();
		}
	}

	public static void generateGrids(){
		grid = (ArrayList<Tile>) stage.tileset.clone();
		for (Tile t : grid){
			t.parent = new ArrayList<>();
			t.distanceToStart = 0;
			t.generation = 0;
		}
		actorGrid = (ArrayList<Tile>) grid.clone();
		for (Tile t : actorGrid) {
			if (t.walkable && !t.isWalkable)
				t.isWalkable = true;
			for (Actor a : actors)
				if (t.overlaps(a))
					t.isWalkable = false;
		}
		Collections.shuffle(enemies);
		//TODO: make it so it also takes speed in consideration
		enemies.sort((o1, o2) -> Integer.compare(o2.totalActingSpeed * 100 + o2.totalSpeed, o1.totalActingSpeed * 100 + o1.totalSpeed));
		enemyGrid = new ArrayList<>();
		for (Tile t : grid) {
			enemyGrid.add(t.clone());
			t.parent = new ArrayList<>();
		}
		for (Tile t : enemyGrid)
			for (Actor a : actors)
				if (!(a instanceof Enemy) && a.x == t.x && a.y == t.y) {
					t.isWalkable = false;
					break;
				}


		friend.sort((o1, o2) -> Integer.compare(o2.totalActingSpeed * 100 + o2.totalSpeed, o1.totalActingSpeed * 100 + o1.totalSpeed));
		allaiesGrid = new ArrayList<>();
		for (Tile t : grid) {
			allaiesGrid.add(t.clone());
			t.parent = new ArrayList<>();
		}
		for (Tile t : allaiesGrid)
			for (Actor a : actors)
				if (!(a instanceof Enemy) && a.x == t.x && a.y == t.y) {
					t.isWalkable = false;
					break;
				}



	}



	public void reset(float startX,float startY, float finalX, float finalY){
		generateGrids();
		setStart(startX,startY);
		setEnd(finalX,finalY);
		needsReset.bool = false;
		minimunDistance = POSITIVE_INFINITY;
		solution = new ArrayList<>();
		currentAnalizing = new ArrayList<>();
		dumpList = new ArrayList<>();
	}


	public void reset(float startX,float startY, float finalX, float finalY,ArrayList<Tile> grid){
		reset(startX,startY,finalX,finalY);
		setStart(startX,startY,grid);
		setEnd(finalX,finalY,grid);
	}


	public boolean quickSolve(float startX,float startY, float finalX, float finalY,byte listType){
		reset(startX,startY,finalX,finalY);
		if (objectiveTile != null) {
			if (listType == 0) solve(grid);
			else if (listType == 1) solve(actorGrid);
			if(startAsPathfinding) {
				fixatedText(("time difference is of: " + (nanoTime() - time)), 100, 100, 100, TextureManager.Fonts.ComicSans, (int) (20 * getCamara().zoom));
				timeReporter.writeFile(nanoTime() - time);
			}
			return !solution.isEmpty();
		} return false;
	}

	public boolean quickSolve(float startX,float startY, float finalX, float finalY,ArrayList<Tile> grid){
		reset(startX,startY,finalX,finalY,grid);
		if (objectiveTile != null) {
			solve(grid);
			return !solution.isEmpty();
		} return false;
	}

	ArrayList<Tile> currentAnalizing = new ArrayList<>();
	ArrayList<Tile> dumpList = new ArrayList<>();
	long time;
	private boolean solve(ArrayList<Tile> currentGrid){
		if(startAsPathfinding)
			time = nanoTime();
		if (!needsReset.bool) {
			if (currentAnalize(startTile,currentGrid))
				return true;
			for (int i = 1; i <= grid.size(); i++) {
				currentAnalizing = (ArrayList<Tile>) dumpList.clone();
				dumpList.clear();
				for (Tile t : currentAnalizing)
					if (currentAnalize(t,currentGrid))
						return true;
			}
			return !solution.isEmpty();
		} else print("needs reset");
		return false;
	}


	private boolean currentAnalize(Tile originalTile,ArrayList<Tile> currentGrid){
		ArrayList<Tile> neighbours;
		if (currentGrid == grid)
			neighbours = originalTile.walkableOrthogonalTiles(currentGrid);
		else
			neighbours = originalTile.isWalkableOrthogonalTiles(currentGrid);
		for (Tile t : neighbours){
			if ((originalTile == startTile || t.generation == 0 || originalTile.generation < t.generation)
					&& originalTile.distanceToStart < minimunDistance) {
				if (t.generation == 0){
					t.generation = (byte) (originalTile.generation + 1);
				}
				boolean temp = false;
				if (!dumpList.isEmpty())
					for (Tile tt : dumpList)
						if (tt == t){
							temp = true;
							break;
						}
				if (!temp && t != objectiveTile)
					dumpList.add(t);
				t.parent.add(originalTile);
				if (originalTile.distanceToStart + t.relativeModuleTo(originalTile) < t.distanceToStart || t.distanceToStart == 0)
					t.distanceToStart = originalTile.distanceToStart + t.relativeModuleTo(originalTile);
				if (t == objectiveTile && objectiveTile.distanceToStart < minimunDistance) {
					return traceback();
				}
			}
		}
		return false;
	}


	private boolean traceback(){
		minimunDistance = objectiveTile.distanceToStart;
		solution.clear();
		Tile checkedTile = objectiveTile;
		do{
			solution.add(checkedTile);
			checkedTile.parent.sort((o1, o2) -> Float.compare(o2.distanceToStart, o1.distanceToStart));
			Collections.reverse(checkedTile.parent);
			checkedTile = checkedTile.parent.get(0);
		}
		while(checkedTile != startTile);
		Collections.reverse(solution);
		return objectiveTile.distanceToStart == objectiveTile.relativeModuleTo(startTile);
	}


	public ArrayList<Path.PathStep> convertTileListIntoPath(){
		ArrayList<Path.PathStep> path = new ArrayList<>();
		for (int i = 0; i < solution.size(); i++){
			if (i == 0){
				path.add(new Path.PathStep(
						(int) (solution.get(i).x - startTile.x),
						(int) (solution.get(i).y - startTile.y)));
			}
			else {
				path.add(new Path.PathStep(
						(int) (solution.get(i).x - solution.get(i - 1).x),
						(int) (solution.get(i).y - solution.get(i - 1).y)));
			}

		}
		return path;
	}



	public void setStart(float x, float y){
		startTile = findATile(grid,x,y);
		if (startTile != null)
			startTile.generation = 1;
		else
			print("startile is null. x was " + x + " y was " + y);
	}

	public void setEnd(float x, float y){
		objectiveTile = findATile(grid,x,y);
		if (objectiveTile == null) {
			print("objectiveTile IS NULL WITH COORDS " + x + " y" + y);
			print("GRID SIZE IS OF " + grid.size());
		}
		if(objectiveTile != null)
			objectiveTile.isWalkable = true;
	}

	public void setStart(float x, float y,ArrayList<Tile> grid){
		startTile = findATile(grid,x,y);
		if (startTile != null)
			startTile.generation = 1;
		else
			print("startile is null. x was " + x + " y was " + y);
	}

	public void setEnd(float x, float y,ArrayList<Tile> grid){
		objectiveTile = findATile(grid,x,y);
		if (objectiveTile == null)
			print("ObjectiveTiile IS NULL");
		else
			objectiveTile.isWalkable = true;
	}

	public void setPlayerAsEnd(){
		setEnd(chara.getX(), chara.getY());
	}

	public static class Boolean{
		public boolean bool;
		public Boolean(boolean bool){this.bool = bool;}
	}


}
