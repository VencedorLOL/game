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

	public void reset(boolean doEnemiesExist,float startX,float startY, float finalX, float finalY){
		grid = stage.tileset;
		generateGrids();
		this.doEnemiesExist = doEnemiesExist;
		startTile = findATile(grid,startX ,startY);
		objectiveTile = findATile(grid,startX ,startY);
		solution = new ArrayList<>();
	}

	public void orthogonalTiles(){
		for (Tile t : grid)
			t.orthogonalTiles(grid);
		for (Tile t : actorGrid)
			t.orthogonalTiles(actorGrid);
		for (Tile t : badGrid)
			t.orthogonalTiles(badGrid);
		for (Tile t : goodGrid)
			t.orthogonalTiles(goodGrid);

	}



	public void solve(){
		if (!startTile.walkable || !objectiveTile.walkable)
			return;
		startTile.g = 0; startTile.f = 0;
		ArrayList<Tile> path = new ArrayList<>();
		path.add(startTile);
		Tile currentTile;
		float ng;
		while(!path.isEmpty()){
			currentTile = path.get(0);
			path.remove(0);
			currentTile.closed = true;
			if (currentTile == objectiveTile) {
				finalSolve(currentTile);
				return;
			}

			for (Tile t : currentTile.orthogonalTiles(grid)){
				if (t.closed && !t.walkable){
					continue;
				}
				ng = currentTile.g;

				if (!t.opened || ng < t.g){

					t.g = ng;
					t.h = t.h == 0 ? (float) sqrt(pow(abs(objectiveTile.x()) - abs(t.x), 2 + pow(abs(objectiveTile.y) - abs(t.y), 2))) : 0;
					if (t.n == 0) {
						ArrayList<Tile> tiles = t.orthogonalTiles(grid);
						tiles.removeIf(tt -> (!tt.walkable));
						t.n = (float) tiles.size() / 9;
					}
					t.f = (t.g * t.n) + t.h;
					t.parent = currentTile;

					if (!t.opened){
						path.add(t);
						t.opened = true;
					}
				}
			}

		}
	}


	public void finalSolve(Tile finalTile){
		if (finalTile.parent != null) {
			do {
				solution.add(finalTile);
				finalTile = finalTile.parent;
			} while (Objects.requireNonNull(finalTile).parent != null || finalTile.f == 0);
		}

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
