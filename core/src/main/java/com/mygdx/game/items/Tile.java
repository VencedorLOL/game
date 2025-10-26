package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.stage;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.ClickDetector.wallRayCasting;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Tile implements Cloneable {

	float x,y,base,height;
	public int ID;
	public static int IDState = 0;
	public Floor texture;
	boolean walkable;
	//for pathfinding
	boolean isWalkable;
	public ArrayList<Tile> parent = new ArrayList<>();
	public float distanceToStart;
	public byte generation = 0;
	//for antisoftlock
	public boolean hasBeenChecked = false;

	public ArrayList<Tile> getParents(){
		return parent;
	}

	//IF "checkIfWalkable() constructor used, ENSURE walls got setted already
	public Tile(float x, float y, Floor texture){
		this.x = x;
		this.y = y;
		base = globalSize();
		height = globalSize();
		this.texture = texture;
		ID = IDState + 1;
		walkable = true;
		isWalkable = true;
		checkIfWalkable();
	}


	public Tile(float x, float y, float base, float height, Floor texture){
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		this.texture = texture;
		ID = IDState + 1;
		checkIfWalkable();
	}



	public void render(){texture.render(x,y);}

	public float[] xAndY(){
		return new float[] {x,y};
	}

	public float x(){return x;}

	public float y(){return y;}

	public float relativeModuleTo(Tile tile){
		return (float) sqrt(pow(tile.x() - x(),2) + pow(tile.y() - y(),2)) / globalSize();
	}

	public float realModuleGivenATile (Tile tile){
		return (float) sqrt(pow(tile.x() - x(),2) + pow(tile.y() - y(),2));
	}

	public boolean isTileAlreadyInList(ArrayList<Tile> tileset){
		for (Tile t : tileset)
			if (t == this)
				return true;
		return false;
	}

	public static Tile findATile (ArrayList<Tile> tile,float x, float y){
		for (Tile t : tile){
			if (x == t.x && t.y == y)
				return t;
		}
		return null;
	}


	public ArrayList<Tile> orthogonalTiles(ArrayList<Tile> tileset){
		ArrayList<Tile> tileSet = new ArrayList<>();
		for (Tile s : tileset){
			if( (s.x() == x() && s.y() == y()+globalSize()) ||
				(s.x() == x()+globalSize() && s.y() == y()+globalSize()) ||
				(s.x() == x()+globalSize() && s.y() == y()) ||
				(s.x() == x()+globalSize() && s.y() == y()-globalSize()) ||
				(s.x() == x() && s.y() == y()-globalSize()) ||
				(s.x() == x()-globalSize() && s.y() == y()-globalSize()) ||
				(s.x() == x()-globalSize() && s.y() == y()) ||
				(s.x() == x()-globalSize() && s.y() == y()+globalSize()))

				tileSet.add(s);

			if(tileSet.size() >= 8)
				break;

		}
		return tileSet;
	}

	public ArrayList<Tile> isWalkableOrthogonalTiles(ArrayList<Tile> tileset){
		ArrayList<Tile> tileSet = new ArrayList<>();
		for (Tile s : tileset){
			if( ((s.x() == x() && s.y() == y()+globalSize()) ||
					(s.x() == x() && s.y() == y()-globalSize()) ||
					(s.x() == x()+globalSize() && s.y() == y()) ||
					(s.x() == x()-globalSize() && s.y() == y()) ||
					(s.x() == x()+globalSize() && s.y() == y()+globalSize()) ||
					(s.x() == x()+globalSize() && s.y() == y()-globalSize()) ||
					(s.x() == x()-globalSize() && s.y() == y()-globalSize()) ||
					(s.x() == x()-globalSize() && s.y() == y()+globalSize()))
			&& s.isWalkable)

				tileSet.add(s);

			if(tileSet.size() >= 8)
				break;

		}
		return tileSet;
	}

	public ArrayList<Tile> walkableOrthogonalTiles(ArrayList<Tile> tileset){
		ArrayList<Tile> tileSet = new ArrayList<>();
		for (Tile s : tileset){
			if( ((s.x() == x() && s.y() == y()+globalSize()) ||
					(s.x() == x() && s.y() == y()-globalSize()) ||
					(s.x() == x()+globalSize() && s.y() == y()) ||
					(s.x() == x()-globalSize() && s.y() == y()) ||
					(s.x() == x()+globalSize() && s.y() == y()+globalSize()) ||
					(s.x() == x()+globalSize() && s.y() == y()-globalSize()) ||
					(s.x() == x()-globalSize() && s.y() == y()-globalSize()) ||
					(s.x() == x()-globalSize() && s.y() == y()+globalSize()))
					&& s.walkable)

				tileSet.add(s);

			if(tileSet.size() >= 8)
				break;

		}
		return tileSet;
	}

	@Override
	public Tile clone() {
		try {
			return (Tile) super.clone();
		} catch (CloneNotSupportedException ignored) {}
		return null;
	}


	public void checkIfWalkable(){
		for (Wall w : stage.walls)
			if (overlaps(w)) {
//				print("tile at " + x + " " + y + " wasnt walkable because of wall at " + w.x + " " + w.y + " called " + w);
				walkable = false;
				isWalkable = false;
				break;
			}
	}

	public static boolean coordentatesInWalkableTile(float x, float y){
		for (Tile t : stage.tileset)
			if (t.x == x && t.y == y)
				return t.walkable;
		return false;
	}


	public boolean overlaps (Entity entity) {
		return x < entity.x + entity.base && x + base > entity.x && y < entity.y + entity.height && y + height > entity.y;
	}

}