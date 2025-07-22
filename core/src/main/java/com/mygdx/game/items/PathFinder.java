package com.mygdx.game.items;

import java.util.ArrayList;
import java.util.Collections;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.Actor.actors;
import static java.lang.Math.abs;
import static java.lang.Float.POSITIVE_INFINITY;


public class PathFinder {
	public static ArrayList<Tile> grid  	 = new ArrayList<>();
	public static ArrayList<Tile> actorGrid  = new ArrayList<>();
	public static OnVariousScenarios oVE;
	Tile startTile;
	Tile objectiveTile;
	public ArrayList<Tile> solution = new ArrayList<>();
	Boolean needsReset = new Boolean(false);
	public static ArrayList<Boolean> resetList = new ArrayList<>();
	float minimunDistance = POSITIVE_INFINITY;

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

	public PathFinder(float startX, float startY, float finalX, float finalY ){
		generateGrids();
		reset(startX,startY,finalX,finalY);
		resetList.add(needsReset);
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
	}

	public static Tile findATile (ArrayList<Tile> tile,float x, float y){
		for (Tile t : tile){
			if (x == t.x && t.y == y)
				return t;
		}
		return null;
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

	public boolean quickSolve(float startX,float startY, float finalX, float finalY,byte listType){
		reset(startX,startY,finalX,finalY);
		if(listType == 0) solve(grid);
		else if(listType == 1) solve(actorGrid);
		return !solution.isEmpty();
	}

	ArrayList<Tile> currentAnalizing = new ArrayList<>();
	ArrayList<Tile> dumpList = new ArrayList<>();
	private boolean solve(ArrayList<Tile> currentGrid){
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
		}
		return false;
	}


	private boolean currentAnalize(Tile originalTile,ArrayList<Tile> currentGrid){
		ArrayList<Tile> neighbours = originalTile.walkableOrthogonalTiles(currentGrid);
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
				print("added generation " + (originalTile.generation + 1) + " parent");
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
		print("called traceback!!! distance is of " + objectiveTile.distanceToStart);
		Tile checkedTile = objectiveTile;
		do{
			solution.add(checkedTile);
			checkedTile.parent.sort((o1, o2) -> Float.compare(o2.distanceToStart, o1.distanceToStart));
			Collections.reverse(checkedTile.parent);
			checkedTile = checkedTile.parent.get(0);
			print("did one dowhile. paremt tile was " + checkedTile + " and its parents are " + checkedTile.parent);
		}
		while(checkedTile != startTile);
		Collections.reverse(solution);
		for (Tile t : solution){
			print(t+" coords is: " + t.x + " ("+ t.x/globalSize()+") " + t.y + " (" + t.y/globalSize() + ")");
		}
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
		print("current path is: ");
		for(Path.PathStep p : path){
			print("path: x(" + p.x + ") y(" + p.y + ") relX("+ p.directionX + ") relY(" + p.directionY + ")");
		}
		return path;
	}



	public void setStart(float x, float y){
		startTile = findATile(grid,x,y);
		if (startTile != null)
			startTile.generation = 1;
	}

	public void setEnd(float x, float y){
		objectiveTile = findATile(grid,x,y);
		print( "objtile is " + x + " " + y );
		if (objectiveTile == null)
			print("ovktitile OS NILL");
	}

	public void setPlayerAsEnd(){
		setEnd(chara.getX(), chara.getY());
	}

	public static class Boolean{
		public boolean bool;
		public Boolean(boolean bool){this.bool = bool;}
	}


}
