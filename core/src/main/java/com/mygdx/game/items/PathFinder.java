package com.mygdx.game.items;

import com.mygdx.game.items.pathfinding.AStarAlgorithm;
import com.mygdx.game.items.pathfinding.element.Grid;
import com.mygdx.game.items.pathfinding.element.Tile;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.POSITIVE_INFINITY;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.print;

public class PathFinder {
	public static Grid grid;
	public AStarAlgorithm algorythm;
	public Stage stage;


	public PathFinder(Stage stage){
		this.stage = stage;
		grid = generateGrid(stage);
		algorythm = new AStarAlgorithm(grid);
	}

	public void reStage(Stage newStage){
		stage = newStage;
		grid = generateGrid(stage);
		algorythm = new AStarAlgorithm(grid);
	}

	public void calculateNeighbours(){
		for (Tile t : grid.getTiles()) {
			t.calculateNeighbours(grid);
		}
	}

	public void solve(){
		calculateNeighbours();
		justSolve();
	}
	public void justSolve(){
		algorythm.solve();
	}

	public ArrayList<Path.PathStep> getSolvedPath(){
		if (algorythm.getPath() == null)
			return null;


		return null;
	}

	public void setStart(float x, float y){
		for (Tile tile : algorythm.getGrid().getNodes())
				if(tile.getX() == x && tile.getY() == y) {
					algorythm.setStart(tile);
					return;
				}
		algorythm.setStart(new Tile((int) x, (int) y));
	}

	public void setEnd(float x, float y){
		for (Tile tile : algorythm.getGrid().getNodes())
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


	public Grid generateGrid(Stage stage) {
		int sX = stage.startX;
		int sY = stage.startY;
		int fX = stage.finalX;
		int fY = stage.finalY;
		int mapBase = fX - sX + globalSize();
		int mapHeight = fY - sY + globalSize();
		ArrayList<Tile> tiles = new ArrayList<>();
		ArrayList<Wall> walls = stage.walls;
		for (int i = 0; i < mapBase; i += globalSize() ) {
			for (int j = 0; j < mapHeight; j += globalSize()) {
				Tile t = new Tile(i, j);
				tiles.add(t);
			}
		}

		for (Wall w : walls)
			for (Tile t : tiles){
				if (w.x == t.getX() && w.y == t.getY()){
					t.reverseValidation();
					break;
				}
			}
		return new Grid(mapBase, mapHeight, tiles);
	}






	/* Last resort option:
		public static class Point {
			public int x;
			public int y;
			public Point previous;

			public Point(int x, int y, Point previous) {
				this.x = x;
				this.y = y;
				this.previous = previous;
			}

			public String toString() { return String.format("(%d, %d)", x, y); }

			public boolean equals(Point point) {
				return x == point.x && y == point.y;
			}

			public boolean equals(float x, float y) {
				return this.x == x && this.y == y;
			}


			@Override
			public int hashCode() { return Objects.hash(x, y); }

			public Point offset(int ox, int oy) {
				return new Point(x + ox, y + oy, this);  }
		}

		public static boolean isWalkable(int[][] map, Point point) {
			if (point.y < 0 || point.y > map.length - 1)
				return false;
			if (point.x < 0 || point.x > map[0].length - 1)
				return false;
			return map[point.y][point.x] == 0;
		}

		public static List<Point> findNeighbors(int[][] map, Point point) {
			List<Point> neighbors = new ArrayList<>();
			Point up = point.offset(0,  1);
			Point down = point.offset(0,  -1);
			Point left = point.offset(-1, 0);
			Point right = point.offset(1, 0);
			Point upLeft = point.offset(1,  1);
			Point upRight = point.offset(-1,  1);
			Point downLeft = point.offset(1, -1);
			Point downRight = point.offset(-1, -1);
			if (isWalkable(map, up)) neighbors.add(up);
			if (isWalkable(map, down)) neighbors.add(down);
			if (isWalkable(map, left)) neighbors.add(left);
			if (isWalkable(map, right)) neighbors.add(right);
			if (isWalkable(map, up)) neighbors.add(upLeft);
			if (isWalkable(map, down)) neighbors.add(upRight);
			if (isWalkable(map, left)) neighbors.add(downLeft);
			if (isWalkable(map, right)) neighbors.add(downRight);
			return neighbors;
		}

		public static List<Point> findPath(int[][] map, float startX, float startY, float finalX, float finalY ){
			return findPath(map, new Point((int) startX, (int) startY,null),new Point((int) finalX, (int) finalY,null));
		}



		public static List<Point> findPath(int[][] map, Point start, Point end) {
			boolean finished = false;
			List<Point> used = new ArrayList<>();
			used.add(start);
			while (!finished) {
				List<Point> newOpen = new ArrayList<>();
				for(int i = 0; i < used.size(); ++i){
					Point point = used.get(i);
					for (Point neighbor : findNeighbors(map, point)) {
						if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
							newOpen.add(neighbor);
						}
					}
				}

				for(Point point : newOpen) {
					used.add(point);
					if (end.equals(point)) {
						finished = true;
						break;
					}
				}

				if (!finished && newOpen.isEmpty())
					return null;
			}

			List<Point> path = new ArrayList<>();
			Point point = used.get(used.size() - 1);
			while(point.previous != null) {
				path.add(0, point);
				point = point.previous;
			}
			return path;
		}







		public static void main(String[] args) {
			int[][] map = {
				{0, 0, 0, 1, 1},
				{1, 1, 0, 1, 1},
				{1, 0, 1, 1, 1},
				{1, 1, 0, 1, 1},
				{1, 1, 1, 0, 1}
			};

			Point start = new Point(0, 0, null);
			Point end = new Point(2, 3, null);
			List<Point> path = findPath(map, start, end);
			if (path != null) {
				for (Point point : path) {
					System.out.println(point);
				}
			}
			else
				System.out.println("No path found");
		}

*/

}
