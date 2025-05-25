package com.mygdx.game.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Actor.actors;
import static java.lang.Math.*;
import static java.lang.Math.abs;

public class PathFinder {
	public ArrayList<Tile> grid  	 = new ArrayList<>();
	public ArrayList<Tile> actorGrid = new ArrayList<>();
	public ArrayList<Tile> goodGrid	 = new ArrayList<>();
	public ArrayList<Tile> badGrid   = new ArrayList<>();
	public boolean doEnemiesExist;
	Tile startTile;
	Tile objectiveTile;
	public ArrayList<Tile> solution;


	public PathFinder(boolean doEnemiesExist,float startX, float startY, float finalX, float finalY ){
		reset(doEnemiesExist,startX,startY,finalX,finalY);
	}

	public void generateGrids(){
		for (Tile t : grid){
			for (Wall w : stage.walls){
				if (w.x == t.x && w.y == t.y) {
					t.walkable = false;
					break;
				}
			}
		}
		actorGrid = cloneList(grid); goodGrid = cloneList(grid); badGrid = cloneList(grid);
		for (int i = 0; i < actorGrid.size(); i++)
			for (Actor a : actors)
				if (actorGrid.get(i).x == a.x && a.y == actorGrid.get(i).y){
					actorGrid.get(i).walkable = false;
					if(a.team == 1)
						goodGrid.get(i).walkable = false;
					if(a.team == -1)
						badGrid.get(i).walkable = false;
				}
	}

	public static Tile findATile (ArrayList<Tile> tile,float x, float y){
		for (Tile t : tile){
			if (x == t.x && t.y == y)
				return t;
		}
		return null;
	}
	public static ArrayList<Tile> cloneList(ArrayList<Tile> list) {
		ArrayList<Tile> clonedList = new ArrayList<>();
		for (Tile t : list)
			clonedList.add(t.clone());
		return clonedList;
	}

	boolean cantDoPathCosSmthIsNull = false;
	public void reset(boolean doEnemiesExist,float startX,float startY, float finalX, float finalY){
		cantDoPathCosSmthIsNull = false;
		grid = stage.tileset;
		for(Tile t : grid){
			t.closed = false;
			t.opened = false;
			t.g = 0; t.h = 0; t.f = 0; t.n = 0;
			t.parent = null;
		}
		generateGrids();
		this.doEnemiesExist = doEnemiesExist;
		startTile = findATile(grid,startX ,startY);
		objectiveTile = findATile(grid,finalX ,finalY);
		if (objectiveTile == null){
			print("obj tile is null and x&y: " + finalX + " " + finalY);
			cantDoPathCosSmthIsNull = true;
		}
		solution = new ArrayList<>();
		counter = 0;
	}

	float counter = 0;
	public void solve(){
		if(!cantDoPathCosSmthIsNull) {
			if (!startTile.walkable || !objectiveTile.walkable) {
				print("not walbkabe");
				return;
			}
			startTile.g = 0;
			startTile.f = 0;
			ArrayList<Tile> path = new ArrayList<>();
			path.add(startTile);
			Tile currentTile;
			float ng;
			while (!path.isEmpty()) {
				counter++;
				currentTile = path.get(0);
				path.remove(0);
				currentTile.closed = true;
				if (currentTile == objectiveTile) {
					finalSolve(currentTile);
					print("retunr. current is " + currentTile.x + " " + currentTile.y + " and final is " + objectiveTile.x + " " + objectiveTile.y);
					return;
				}

				for (Tile t : currentTile.orthogonalTiles(grid)) {
					if (t.closed || !t.walkable) {
						continue;
					}
					ng = (float) (currentTile.g + (((t.x - currentTile.x) == 0 || (t.y - currentTile.y == 0)) ? 1 : sqrt(2)));

					if (!t.opened || ng < t.g) {

						t.g = ng;
						t.h = t.h == 0 ? (float) sqrt(pow(abs(objectiveTile.x()) - abs(t.x), 2) + pow(abs(objectiveTile.y) - abs(t.y), 2)) : 0;
						if (t.n == 0) {
							ArrayList<Tile> tiles = t.orthogonalTiles(grid);
							tiles.removeIf(tt -> (!tt.walkable));
							t.n = (float) tiles.size() / 9;
						}
						t.f = (t.g * t.n) + t.h;
						t.parent = currentTile;

						if (!t.opened) {
							path.add(t);
							t.opened = true;
						}
					}
				}
				print(counter + "");
				if (counter > 500) {
					print("tooling");
					return;
				}
			}
			print("mtyo path");
		}
	}


	public void finalSolve(Tile finalTile){
		if (finalTile.parent != null) {
			do {
				solution.add(finalTile);
				finalTile = finalTile.parent;
				if (finalTile.getParent() == null) {
					for(Tile t : solution){
						print("");
						print("x: "+t.x);
						print("y: "+t.y);
						print("");
					}
					return;
				}
			} while (finalTile.getParent() != null || finalTile.f == 0);
		}
		print(solution+"");

	}


	public ArrayList<Path.PathStep> convertTileListIntoPath(){
		// note to myself: algorithm generates 2 useless values and coordinates instead of paths.
		// This converts the coordinates into paths by taking the difference between their coordinates
		// and ignores the useless values.
		ArrayList<Path.PathStep> path = new ArrayList<>();

		for (int i = 0; i < solution.size(); i++)
			if ((i != 0) && !((solution.get(i - 1).x - solution.get(i).x) == 0 && (solution.get(i - 1).y - solution.get(i).y) == 0))
				path.add(new Path.PathStep((int) (solution.get(i - 1).x - solution.get(i).x), (int) (solution.get(i - 1).y - solution.get(i).y), (byte) 1));
		Collections.reverse(path);
		/*if (solution.size() > 2)
			path.add(new Path.PathStep((int) (solution.get(solution.size() - 2).x - objectiveTile.x), (int) (solution.get(solution.size() - 2).y - objectiveTile.y), (byte) 1));
		else if (solution.size() > 1)
			path.add(new Path.PathStep((int) (objectiveTile.x-solution.get(solution.size() - 1).x), (int) (objectiveTile.y-solution.get(solution.size() - 1).y), (byte) 1));
		else if (objectiveTile != null)
			path.add(new Path.PathStep((int) ( objectiveTile.x-startTile.x),(int) (objectiveTile.y-startTile.y),(byte) 1));*/
		return path;
	}



	public void setStart(float x, float y){
		startTile = findATile(grid,x,y);
	}

	public void setEnd(float x, float y){
		objectiveTile = findATile(grid,x,y);
	}

	public void setPlayerAsEnd(){
		for (Entity chara : Entity.entityList) {
			if (chara instanceof Character){
				setEnd(chara.getX(), chara.getY());
				return;
			}
		}
	}




}
