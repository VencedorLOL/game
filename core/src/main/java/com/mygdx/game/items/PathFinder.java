package com.mygdx.game.items;

import java.util.ArrayList;
import java.util.Collections;

import static com.mygdx.game.GameScreen.chara;
import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.Actor.actors;
import static java.lang.Math.abs;

public class PathFinder {
	public static ArrayList<Tile> grid  	 = new ArrayList<>();
	public static ArrayList<Tile> actorGrid  = new ArrayList<>();
	public static OnVariousScenarios oVE;
	Tile startTile;
	Tile objectiveTile;
	public ArrayList<Tile> solution = new ArrayList<>();
	Boolean needsReset = new Boolean(false);
	public static ArrayList<Boolean> resetList = new ArrayList<>();
	float minimunDistance = (float) 1 /0;

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
		minimunDistance = (float) 1/0;
		solution = new ArrayList<>();
		currentAnalizing = new ArrayList<>();
		dumpList = new ArrayList<>();
	}

	ArrayList<Tile> currentAnalizing = new ArrayList<>();
	ArrayList<Tile> dumpList = new ArrayList<>();
	public boolean solve(){
		if (!needsReset.bool) {
			if (currentAnalize(startTile, (byte) 0))
				return true;
			for (int i = 1; i <= grid.size(); i++) {
				currentAnalizing = (ArrayList<Tile>) dumpList.clone();
				dumpList.clear();
				for (Tile t : currentAnalizing)
					currentAnalize(t, (byte) i);

			}
		}
		return false;
	}


	public boolean currentAnalize(Tile originalTile,byte generation){
		ArrayList<Tile> neighbours = originalTile.walkableOrthogonalTiles(actorGrid);
		for (Tile t : neighbours){
			if ((originalTile == startTile || t.parent.isEmpty() || originalTile.parent.get(0).tileCirclePos < t.parent.get(0).tileCirclePos) && t != startTile
					&& originalTile.distanceToStart < minimunDistance) {
				dumpList.add(t);
				t.parent.add(new Tile.TileAndCirclePos(originalTile,generation));
				t.distanceToStart = originalTile.distanceToStart + t.relativeModuleTo(originalTile);
				print("added generation " + generation + " parent");
				if (t == objectiveTile) {
					traceback(t);
				}
			}
		}
		return false;
	}


	public void traceback(Tile finalTile){
		minimunDistance = finalTile.distanceToStart;
		print("called traceback!!! distance is of " + objectiveTile.distanceToStart);
		Tile checkedTile = finalTile;
		do{
			solution.add(checkedTile);
			checkedTile.parent.sort((o1, o2) -> Float.compare(o2.tile.distanceToStart, o1.tile.distanceToStart));
			checkedTile = checkedTile.parent.get(0).tile;
			print("did one dowhile. paremt tile was " + checkedTile + " and its parents are " + checkedTile.parent);
		}
		while(checkedTile != startTile);
		Collections.reverse(solution);
		for (Tile t : solution){
			print(t+" coords is: " + t.x + " ("+ t.x/globalSize()+") " + t.y + " (" + t.y/globalSize() + ")");
		}

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
