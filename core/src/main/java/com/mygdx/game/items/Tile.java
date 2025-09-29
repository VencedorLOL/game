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


	public static class Circle{
		public ArrayList<Tile> circle = new ArrayList<>();
		public ArrayList<Tile> tileset;
		public float radius;
		public Tile center;
		public boolean walkable;
		boolean rayCast;

		public Circle(Tile center, ArrayList<Tile> tileset, float radius,boolean checkWalkable,boolean rayCast){
			this.center = center;
			this.radius = radius;
			this.tileset = tileset;
			this.walkable = checkWalkable;
			this.rayCast = rayCast;
			if (checkWalkable)
				checkWalkable();
			circle = circle();
			detectCornersOfCircle(circle);

		}

		public void renderCircle(){
			for (Tile t : circle){
				t.texture.renderCircle(t.x,t.y);
			}
		}

		public Tile findATile(float x, float y){
			for (Tile t : circle){
				if (t.x() == x && t.y() == y)
					return t;
			}
			return null;
		}


		public void checkWalkable(){
			for (Tile t : tileset) {
				t.checkIfWalkable();
//				if (!t.walkable)
//					print("not walkalb at: " + t.x + " " + t.y);

			}
		}

		public ArrayList<Tile> circle(){return circle(center, tileset,radius,rayCast);}

		public ArrayList<Tile> circle(Tile center, ArrayList<Tile> tileset, float radius,boolean raycast){
			radius += 0.5f;
			for (Tile t : center.orthogonalTiles(tileset)) {
				if (center.relativeModuleTo(t) <= radius && (!walkable || t.walkable))
					circle.add(t);
			}
			// +3 jst to be safe
			int failsafe = (int) (radius + 3);
			while(true){
				int addedATileThisLoop = 0;
				ArrayList<Tile> avoidConcurrentModification = new ArrayList<>();
				for(Tile t : circle){
					for (Tile tt : t.orthogonalTiles(tileset)) {
						if (center.relativeModuleTo(tt) <= radius && !tt.isTileAlreadyInList(circle) && !tt.isTileAlreadyInList(avoidConcurrentModification) && (!walkable || tt.walkable)) {
							addedATileThisLoop++;
							avoidConcurrentModification.add(tt);
						}
					}
				}
				circle.addAll(avoidConcurrentModification);
				if (addedATileThisLoop <= 0)
					break;
				failsafe--;
				if(failsafe <= 0)
					break;
			}
			if(raycast)
				circle.removeIf(t -> wallRayCasting(center.x,center.y,t.x,t.y));
			return circle;
		}

		public void detectCornersOfCircle(ArrayList<Tile> circle) {
			for (Tile t : circle){
				boolean lu = false, u = false, ru = false,r= false, rd = false,d= false, dl = false,l = false;
				for (Tile tt : t.orthogonalTiles(circle)){
					if(tt.x() == t.x() && tt.y() == t.y()+globalSize())
						u = true;
					if(tt.x() == t.x()+globalSize() && tt.y() == t.y()+globalSize())
						ru = true;
					if(tt.x() == t.x()+globalSize() && tt.y() == t.y())
						r = true;
					if(tt.x() == t.x()+globalSize() && tt.y() == t.y()-globalSize())
						rd = true;
					if(tt.x() == t.x() && tt.y() == t.y()-globalSize())
						d = true;
					if(tt.x() == t.x()-globalSize() && tt.y() == t.y()-globalSize())
						dl = true;
					if(tt.x() == t.x()-globalSize() && tt.y() == t.y())
						l = true;
					if(tt.x() == t.x()-globalSize() && tt.y() == t.y()+globalSize())
						lu = true;
				}


				t.texture.secondaryReset();

				if(!ru || (!r && !u))
					if(!r&&!u)
						t.texture.setSecondaryTexture("selectionIndicatorCOutwards",0.6f,90,false,false,1);
					else
						t.texture.setSecondaryTexture("selectionIndicatorC",0.6f,90,false,false,1);
				if(!rd || (!r && !d))
					if(!r&&!d)
						t.texture.setSecondaryTexture("selectionIndicatorCOutwards",0.6f,0,false,false,3);
					else
						t.texture.setSecondaryTexture("selectionIndicatorC",0.6f,0,false,false,3);
				if(!dl || (!d && !l))
					if(!d&&!l)
						t.texture.setSecondaryTexture("selectionIndicatorCOutwards",0.6f,270,false,false,5);
					else
						t.texture.setSecondaryTexture("selectionIndicatorC",0.6f,270,false,false,5);
				if(!lu || (!l && !u))
					if(!l&&!u)
						t.texture.setSecondaryTexture("selectionIndicatorCOutwards",0.6f,180,false,false,7);
					else
						t.texture.setSecondaryTexture("selectionIndicatorC",0.6f,180,false,false,7);


				if(!u) {
					t.texture.setSecondaryTexture("selectionIndicatorS", 0.6f, 180, false, false, 0);
					if(l)
						t.texture.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 180, false, false, 7);
					if(r)
						t.texture.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 180, true, false, 1);
				}
				if(!r) {
					t.texture.setSecondaryTexture("selectionIndicatorS", 0.6f, 90, false, false, 2);
					if(u)
						t.texture.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 90, false, false, 1);
					if(d)
						t.texture.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 90, true, false, 3);
				}
				if(!d) {
					t.texture.setSecondaryTexture("selectionIndicatorS", 0.6f, 0, false, false, 4);
					if(r)
						t.texture.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 0, false, false, 3);
					if(l)
						t.texture.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 0, true, false, 5);
				}
				if(!l) {
					t.texture.setSecondaryTexture("selectionIndicatorS", 0.6f, 270, false, false, 6);
					if(d)
						t.texture.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 270, false, false, 5);
					if(u)
						t.texture.setSecondaryTexture("selectionIndicatorSMinus", 0.6f, 270, true, false, 7);
				}



				t.texture.setSecondaryTexture("selectionIndicator",0f,0,false,false,8);

			}
		}



		public boolean isInsideOfCircle(float x, float y){
			for (Tile t : circle)
				if (t.x == x && t.y == y)
					return true;
			return false;
		}





	}



}