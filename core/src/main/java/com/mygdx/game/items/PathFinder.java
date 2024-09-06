package com.mygdx.game.items;

import com.mygdx.game.items.pathfinding.PathfindAlgorithm;
import com.mygdx.game.items.pathfinding.element.Grid;
import com.mygdx.game.items.pathfinding.element.Tile;

import java.util.ArrayList;
import java.util.Collections;

import static com.mygdx.game.Settings.*;

public class PathFinder {
	public static Grid grid;
	public static Grid enemyGrid;
	public PathfindAlgorithm algorythm;
	public PathfindAlgorithm takingEnemiesInConsiderationAlgorythm;
	public static Stage stage;


	public static ArrayList<PathfindAlgorithm> algorythmList = new ArrayList<>();
	public static ArrayList<PathfindAlgorithm> takingEnemiesInConsiderationAlgorythmList = new ArrayList<>();


	public PathFinder(Stage stage){
		PathFinder.stage = stage;
		grid = new Grid();
		grid.generateGrid(stage);
		enemyGrid = new Grid();
		enemyGrid.generateEnemyGrid(stage);
		algorythm = new PathfindAlgorithm(grid);
		algorythmList.add(algorythm);
		takingEnemiesInConsiderationAlgorythm = new PathfindAlgorithm(enemyGrid);
		takingEnemiesInConsiderationAlgorythmList.add(takingEnemiesInConsiderationAlgorythm);
	}


	public static void reset(Stage newStage){
		print("reseting thing");
		stage = newStage;
		grid.generateGrid(stage);
		enemyGrid.generateEnemyGrid(stage);
		for (PathfindAlgorithm a : algorythmList){
			a = new PathfindAlgorithm(grid);
		}
		for (PathfindAlgorithm a : takingEnemiesInConsiderationAlgorythmList){
			a = new PathfindAlgorithm(enemyGrid);
		}
	}

	public void calculateNeighbours(){
		for (Tile t : grid.getTiles()) {
			t.calculateNeighbours(grid);
		}
		for (Tile t : enemyGrid.getTiles()) {
			t.calculateNeighbours(enemyGrid);
		}
	}

	public void solve(){
		calculateNeighbours();
		justSolve();
	}
	public void justSolve(){
		algorythm.solve();
		takingEnemiesInConsiderationAlgorythm.solve();
	}

	public ArrayList<Path.PathStep> getSolvedPath(){
		if (algorythm.getPath() == null)
			return null;
		ArrayList<Tile> tiles = algorythm.getPath();
		if (getTakeEnemiesIntoConsideration() == 0 || (takingEnemiesInConsiderationAlgorythm.getPath() == null
			&& getTakeEnemiesIntoConsideration() != 4)) {
			return convertTileListIntoPath(tiles);
		}
		if(takingEnemiesInConsiderationAlgorythm != null){
			ArrayList<Tile> enemyTiles = takingEnemiesInConsiderationAlgorythm.getPath();
			if((getTakeEnemiesIntoConsideration() == 1 && enemyTiles.size() <= tiles.size())
			|| (getTakeEnemiesIntoConsideration() == 2 && enemyTiles.size() + getExtraAllowedPath() <= tiles.size()))
				return convertTileListIntoPath(enemyTiles);
			else if (getTakeEnemiesIntoConsideration() != 4)
				return convertTileListIntoPath(tiles);
			else
				return convertTileListIntoPath(enemyTiles);
		}
		else if (getTakeEnemiesIntoConsideration() != 4)
			return convertTileListIntoPath(tiles);
		return null;
	}

	public ArrayList<Path.PathStep> convertTileListIntoPath(ArrayList<Tile> tiles){
		// note to myself: algorythm generates 2 useless values and coordinates instead of paths.
		// This converts the coodrinates into paths by taking the diferrence between their coordinates
		// and ignores the useless values.
		ArrayList<Path.PathStep> path = new ArrayList<>();
		for (int i = 0; i < tiles.size(); i++)
			if ((i != 0) && !((tiles.get(i - 1).x - tiles.get(i).x) == 0 && (tiles.get(i - 1).y - tiles.get(i).y) == 0))
				path.add(new Path.PathStep((tiles.get(i - 1).x - tiles.get(i).x), (tiles.get(i - 1).y - tiles.get(i).y)));
		Collections.reverse(path);
		return path;
	}



	public void setStart(float x, float y){
		for (Tile tile : algorythm.getGrid().getTiles())
				if(tile.getX() == x && tile.getY() == y) {
					algorythm.setStart(tile);
					return;
				}
		algorythm.setStart(new Tile((int) x, (int) y));
	}

	public void setEnd(float x, float y){
		for (Tile tile : algorythm.getGrid().getTiles())
				if(tile.getX() == x && tile.getY() == y) {
					algorythm.setEnd(tile);
					return;
				}
		algorythm.setEnd(new Tile((int) x, (int) y));
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
