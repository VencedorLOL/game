package com.mygdx.game.items.pathfinding;

import java.util.ArrayList;

import com.mygdx.game.items.pathfinding.element.Grid;
import com.mygdx.game.items.pathfinding.element.Tile;


public class PathfindAlgorithm {

    private Grid grid;
    private ArrayList<Tile> path;

    private Tile start;
    private Tile end;

    private ArrayList<Tile> openList;
    private ArrayList<Tile> closedList;

    public PathfindAlgorithm(Grid grid) {
        this.grid = grid;
    }

	public PathfindAlgorithm(){}

    public void solve() {

        if (start == null && end == null) {
            return;
        }

		if (start == end) {
            this.path = new ArrayList<>();
            return;
        }

        this.path = new ArrayList<>();
        this.openList = new ArrayList<>();
        this.closedList = new ArrayList<>();

        this.openList.add(start);

        while (!openList.isEmpty()) {
			Tile current = getLowestF();

            if (current.equals(end)) {
                retracePath(current);
                break;
            }

            openList.remove(current);
            closedList.add(current);

            for (Tile n : current.getNeighbours()) {

                if (closedList.contains(n) || !n.isValid()) {
                    continue;
                }

                double tempScore = current.getCost() + current.distanceTo(n);

                if (openList.contains(n)) {
                    if (tempScore < n.getCost()) {
                        n.setCost(tempScore);
                        n.setParent(current);
                    }
                } else {
                    n.setCost(tempScore);
                    openList.add(n);
                    n.setParent(current);
                }

                n.setHeuristic(n.heuristic(end));
                n.setFunction(n.getCost() + n.getHeuristic());

            }
        }
    }

	public void resetJustGrid(Grid grid){
		this.grid = grid;
	}


    public void reset(Grid grid) {
		this.grid = grid;
        this.start = null;
        this.end = null;
        this.path = null;
        this.openList = null;
        this.closedList = null;
        for (Tile n : grid.getNewListWithTiles()) {
            n.setValid(true);
        }
    }

    private void retracePath(Tile current) {
		Tile temp = current;
        this.path.add(current);

        while (temp.getParent() != null) {
            this.path.add(temp.getParent());
            temp = temp.getParent();
        }

        this.path.add(start);
    }

    private Tile getLowestF() {
		Tile lowest = openList.get(0);
        for (Tile n : openList) {
            if (n.getFunction() < lowest.getFunction()) {
                lowest = n;
            }
        }
        return lowest;
    }

    public Grid getGrid() {
        return grid;
    }

    public ArrayList<Tile> getPath() {
        return path;
    }

    public Tile getStart() {
        return start;
    }

    public Tile getEnd() {
        return end;
    }

    public void setStart(Tile start) {
        this.start = start;
    }

    public void setEnd(Tile end) {
        this.end = end;
    }

}
