package com.mygdx.game.items.pathfinding.element;

import com.mygdx.game.items.Enemy;
import com.mygdx.game.items.Stage;
import com.mygdx.game.items.Wall;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;

public class Grid {

    private int base, height;
    private ArrayList<Tile> tiles;
	public int stageStartX;
	public int stageStartY;


    public Grid(int base, int height, ArrayList<Tile> tiles) {
        this.base = base;
        this.height = height;
        this.tiles = tiles;
    }

	public Grid(){}

    public int getBase() {
        return base;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public Tile find(int x, int y){
        for(Tile t : tiles){
            if(t.getX() == x && t.getY() == y)
                return t;
        }
        return null;
    }

	public ArrayList<Tile> getNewListWithTiles() {
		return new ArrayList<>(tiles);
	}

	private void reset(int base, int height, ArrayList<Tile> tiles){
		this.base = base;
		this.height = height;
		this.tiles = tiles;
	}


	public void generateGrid(Stage stage) {
		ArrayList<Tile> tiles = new ArrayList<>();
		ArrayList<Wall> walls = stage.walls;
		for (int i = stage.startX; i < stage.finalX + globalSize(); i += globalSize() ) {
			for (int j = stage.startY; j < stage.finalY + globalSize(); j += globalSize()) {
				Tile t = new Tile(i, j);
				tiles.add(t);
			}
		}

		for (Wall w : walls)
			for (Tile t : tiles)
				if (w.getX() == t.getX() && w.getY() == t.getY()){
					t.reverseValidation();
					break;
				}
		stageStartX = stage.startX;
		stageStartY = stage.startY;
		reset(stage.finalX - stage.startX + globalSize(), stage.finalY - stage.startY + globalSize(), tiles);
	}

	public void generateEnemyGrid(Stage stage) {
		ArrayList<Tile> tiles = new ArrayList<>();
		ArrayList<Wall> walls = stage.walls;
		ArrayList<Enemy> enemies = stage.enemy;
		for (int i = stage.startX; i < stage.finalX + globalSize(); i += globalSize() ) {
			for (int j = stage.startY; j < stage.finalY + globalSize(); j += globalSize()) {
				Tile t = new Tile(i, j);
				tiles.add(t);
			}
		}

		for (Wall w : walls)
			for (Tile t : tiles)
				if (w.getX() == t.getX() && w.getY() == t.getY()){
					t.reverseValidation();
					break;
				}

		for (Enemy e : enemies)
			for (Tile t : tiles)
				if (e.getX() == t.getX() && e.getY() == t.getY() && t.isValid()){
					t.reverseValidation();
					break;
				}
		stageStartX = stage.startX;
		stageStartY = stage.startY;
		reset(stage.finalX - stage.startX + globalSize(), stage.finalY - stage.startY + globalSize(), tiles);
	}


}
