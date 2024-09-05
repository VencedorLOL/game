package com.mygdx.game.items.pathfinding.element;

import java.util.ArrayList;

public class Grid {

    private int width, height;
    private ArrayList<Tile> tiles;

    public Grid(int width, int height, ArrayList<Tile> tiles) {
        this.width = width;
        this.height = height;
        this.tiles = tiles;
    }

    public int getWidth() {
        return width;
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

    public Iterable<Tile> getNodes() {
		return new ArrayList<>(tiles);
    }
}
