package com.mygdx.game.items.pathfinding.element;

import java.awt.Point;
import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;

public class Tile {
	private Tile parent;
	private ArrayList<Tile> neighbours;
	private double cost, heuristic, function;
	private boolean valid;
    private final int x, y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        setValid(true);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public void calculateNeighbours(Grid grid) {

		ArrayList<Tile> nodes = new ArrayList<>();

        int minX = 0;
        int minY = 0;
        int maxX = grid.getWidth() - globalSize();
        int maxY = grid.getHeight() - globalSize();

        if (x > minX) {
            nodes.add(grid.find(x - globalSize(), y)); //west
        }

        if (x < maxX) {
            nodes.add(grid.find(x + globalSize(), y)); //east
        }

        if (y > minY) {
            nodes.add(grid.find(x, y - globalSize())); //north
        }

        if (y < maxY) {
            nodes.add(grid.find(x, y + globalSize())); //south
        }

        if (x > minX && y > minY) {
            nodes.add(grid.find(x - globalSize(), y - globalSize())); //northwest
        }

        if (x < maxX && y < maxY) {
            nodes.add(grid.find(x + globalSize(), y + globalSize())); //southeast
        }

        if(x < maxX && y > minY){
            nodes.add(grid.find(x + globalSize(), y - globalSize())); //northeast
        }

        if(x > minY && y < maxY){
            nodes.add(grid.find(x - globalSize(), y + globalSize())); //southwest
        }

        setNeighbours(nodes);

    }


    public double heuristic(Tile dest) {
        return distanceTo(dest);
    }

    public double distanceTo(Tile dest) {
		return new Point(x,y).distance(new Point(dest.x, dest.y));
    }

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

	public double getFunction() {
		return function;
	}

	public void setFunction(double function) {
		this.function = function;
	}



	public ArrayList<Tile> getNeighbours() {return neighbours; }

	public void setNeighbours(ArrayList<Tile> neighbours) {this.neighbours = neighbours; }

	public Tile getParent() {return parent; }

	public void setParent(Tile parent) {this.parent = parent; }

	public boolean isValid() {return valid; }

	public void setValid(boolean valid) {this.valid = valid; }

	public void reverseValidation() {valid = !valid; }



}
