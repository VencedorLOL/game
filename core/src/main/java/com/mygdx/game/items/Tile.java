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
	Floor texture;
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
		ArrayList<Tile> circle = new ArrayList<>();
		ArrayList<Tile> tileset;
		float radius;
		Tile center;
		boolean walkable;
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
				if(!u)
					t.texture.setSecondaryTexture("selectionIndicatorS",0.8f,180,false,false,0);
				if(!ru)
					t.texture.setSecondaryTexture("selectionIndicatorC",0.8f,0,false,true,1);
				if(!r)
					t.texture.setSecondaryTexture("selectionIndicatorS",0.8f,90,false,false,2);
				if(!rd)
					t.texture.setSecondaryTexture("selectionIndicatorC",0.8f,0,false,false,3);
				if(!d)
					t.texture.setSecondaryTexture("selectionIndicatorS",0.8f,0,false,false,4);
				if(!dl)
					t.texture.setSecondaryTexture("selectionIndicatorC",0.8f,0,true,false,5);
				if(!l)
					t.texture.setSecondaryTexture("selectionIndicatorS",0.8f,270,false,false,6);
				if(!lu)
					t.texture.setSecondaryTexture("selectionIndicatorC",0.8f,180,false,false,7);
				t.texture.setSecondaryTexture("selectionIndicator",0.8f,0,false,false,8);

			}
		}

	/*				if (counter == 1){
					tileAndCirclePos = oneCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
					continue;
				}
				if (counter == 2){
					tileAndCirclePos = twoCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
					continue;
				}
				if (counter == 3){
					tileAndCirclePos = threeCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
					continue;
				}
				if (counter == 4){
					tileAndCirclePos = fourCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
					continue;
				}
				if (counter == 5){
					tileAndCirclePos = threeCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
					continue;
				}
				if (counter == 6) {
					tileAndCirclePos = sixCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
					continue;
				}
				if (counter == 7){
					tileAndCirclePos = sevenCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
					continue;
				}
				if (counter == 8)
					tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 13));

				//flat
				if (!u&&!R){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)1)); continue;}
				if (!D&&!R){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)3)); continue;}
				if (!D&&!L){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)5)); continue;}
				if (!u&&!L){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)7)); continue;}
				// corner
				if (!lu&&!ru || !u){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)0)); continue;}
				if (!rd&&!ru || !R){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)2)); continue;}
				if (!dl&&!rd || !D){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)4)); continue;}
				if (!dl&&!lu || !L){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)6)); continue;}
				//double corner



				// interior. thx smc.
				if (!ru){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)8)); continue;}
				if (!rd){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)9)); continue;}
				if (!dl){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)10)); continue;}
				if (!lu){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)11));}

				*/

	/*	public ArrayList<TileAndCirclePos> oneCounter(Tile t, boolean lu, boolean u, boolean ru,boolean r, boolean rd,boolean d, boolean dl,boolean l,
							   ArrayList<TileAndCirclePos> tileAndCirclePos){
			if (r || u || l || d)
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 3));
			else
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 0));
			TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = r ? twoHunderSeventy : d ? 180 : l  ? ninty : 0;
			return tileAndCirclePos;
		}


		public ArrayList<TileAndCirclePos> twoCounter(Tile t, boolean lu, boolean u, boolean ru,boolean r, boolean rd,boolean d, boolean dl,boolean l,
													  ArrayList<TileAndCirclePos> tileAndCirclePos){
			if (r && (ru || rd) || u && (ru || lu) || d && (dl || rd) || l && (lu || dl)) {
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 3));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = r ? twoHunderSeventy : d ? 180 : l ? ninty : 0;
				return tileAndCirclePos;
			}
			if((u && l) || (u && r) || (d && r) || (d && l)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 6));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = d && l ? twoHunderSeventy : d ? 180 : l ? ninty : 0;
				return tileAndCirclePos;
			}
			if((u && d) || (l && r)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 4));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = d ? ninty : 0;
				return tileAndCirclePos;
			}
			if((u && (dl || rd)) || (d && (ru || lu)) || (r && (dl || lu)) || (l && (rd || ru))){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 3));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = r ? twoHunderSeventy : d ? 180 : l ? ninty : 0;
				return tileAndCirclePos;
			}

			return oneCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
		}

		public ArrayList<TileAndCirclePos> threeCounter(Tile t, boolean lu, boolean u, boolean ru,boolean r, boolean rd,boolean d, boolean dl,boolean l,
													  ArrayList<TileAndCirclePos> tileAndCirclePos){

			if((u && l && lu) ||(u && r && ru)||(d && l && dl)||(d && r && rd)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 2));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = d && l ? twoHunderSeventy : d ? 180 : l ? ninty : 0;
				return tileAndCirclePos;
			}
			if((u && lu && ru) ||(rd && r && ru)||(d && rd && dl)||(lu && l && dl)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 1));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = r ? twoHunderSeventy : d ? 180 : l ? ninty : 0;
				return tileAndCirclePos;
			}
			if((d && l && u) || (l && u && r) || (u && r && d) || (r && d && l)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 7));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = !l ? twoHunderSeventy : !u ? 180 : !r ? ninty : 0;
				return tileAndCirclePos;
			}
			return twoCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
		}

		public ArrayList<TileAndCirclePos> fourCounter(Tile t, boolean lu, boolean u, boolean ru,boolean r, boolean rd,boolean d, boolean dl,boolean l,
														ArrayList<TileAndCirclePos> tileAndCirclePos){

			if (u&&l&&d&&r) {
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 12));
				return tileAndCirclePos;
			}
			if (!u&&!l&&!d&&!r) {
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 0));
				return tileAndCirclePos;
			}
			if((l && u && lu && (dl || ru)) ||(u && r && ru && (lu || rd))||(d && l && dl && (lu || rd))||(d && r && rd && (dl || ru))){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 2));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = d && l ? twoHunderSeventy : d ? 180 : l ? 90 : 0;
				return tileAndCirclePos;
			}
			if((l && u && lu && (d || r)) || (u && r && ru && (l || d)) || (d && l && dl && (u || r)) || (d && r && rd && (l || u))){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 5));
				if(!l){
					TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = twoHunderSeventy;
					if(!ru)
						TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).flippedY = true;
				}
				else if (!u) {
					TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).rotationDegrees = 180;
					if(!rd)
						TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).flippedX = true;
				}
				else if (!r) {
					TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).rotationDegrees = ninty;
					if(!dl)
						TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).flippedY = true;
				}
				else {
					TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).rotationDegrees = 0;
					if(!lu)
						TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).flippedX = true;
				}
				return tileAndCirclePos;
			}
			if((u && d) || (l && r)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 4));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = d ? ninty : 0;
				return tileAndCirclePos;
			}
			return threeCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
		}


		public ArrayList<TileAndCirclePos> fiveCounter(Tile t, boolean lu, boolean u, boolean ru,boolean r, boolean rd,boolean d, boolean dl,boolean l,
													   ArrayList<TileAndCirclePos> tileAndCirclePos) {

			if((!u && !l && !lu) ||(!u && !r && !ru)||(!d && !l && !dl)||(!d && !r && !rd)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 2));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = l && u ? twoHunderSeventy : u ? 180 : r ? ninty : 0;
				return tileAndCirclePos;
			}
			if((!u && !lu && !ru) ||(!rd && !r && !ru)||(!d && !rd && !dl)||(!lu && !l && !dl)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 1));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = !l ? twoHunderSeventy : !u ? 180 : !r ? ninty : 0;
				return tileAndCirclePos;
			}
			if (u&&l&&d&&r) {
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 11));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = ru ? twoHunderSeventy : rd ? 180 : dl ? ninty : 0;
				return tileAndCirclePos;
			}
			if((u && lu && ru) ||(rd && r && ru)||(d && rd && dl)||(lu && l && dl)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 1));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = r ? twoHunderSeventy : d ? 180 : l ? ninty : 0;
				return tileAndCirclePos;
			}

			return fourCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
		}

		public ArrayList<TileAndCirclePos> sixCounter(Tile t, boolean lu, boolean u, boolean ru,boolean r, boolean rd,boolean d, boolean dl,boolean l,
													   ArrayList<TileAndCirclePos> tileAndCirclePos) {


			if (!r && (!ru || !rd) || !u && (!ru || !lu) || !d && (!dl || !rd) || !l && (!lu || !dl)) {
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 1));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = !r ? twoHunderSeventy : !u ? 180 : !l ? ninty : 0;
				return tileAndCirclePos;
			}
			if((!u && !l) || (!u && !r) || (!d && !r) || (!d && !l)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 2));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = d && l ? twoHunderSeventy : d ? 180 : l ? ninty : 0;
				return tileAndCirclePos;
			}
			if((!u && !d) || (!l && !r)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 4));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = !d ? ninty : 0;
				return tileAndCirclePos;
			}
			if((!u && (!dl || !rd)) || (!d && (!ru || !lu)) || (!r && (!dl || !lu)) || (!l && (!rd || !ru))){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 5));
				if(!l){
					TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = twoHunderSeventy;
					if(!ru)
						TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).flippedY = true;
				}
				else if (!u) {
					TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).rotationDegrees = 180;
					if(!rd)
						TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).flippedX = true;
				}
				else if (!r) {
					TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).rotationDegrees = ninty;
					if(!dl)
						TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).flippedY = true;
				}
				else {
					TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).rotationDegrees = 0;
					if(!lu)
						TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).flippedX = true;
				}
				return tileAndCirclePos;
			}
			if((!ru && !lu) || (!ru && !rd) || (!dl && !rd) || (!dl && !lu)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 9));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = !rd && !dl ? 0 : !dl && !ru ? ninty : !ru && !rd ? 180 : twoHunderSeventy;
				return tileAndCirclePos;
			}
			if((!ru && !dl) || (!lu && !rd)){
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 10));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos,t).rotationDegrees = !ru ? ninty : 0;
				return tileAndCirclePos;
			}

			return sevenCounter(t,lu,u,ru,r,rd,d,dl,l,tileAndCirclePos);
		}


		public ArrayList<TileAndCirclePos> sevenCounter(Tile t, boolean lu, boolean u, boolean ru,boolean r, boolean rd,boolean d, boolean dl,boolean l,
													  ArrayList<TileAndCirclePos> tileAndCirclePos) {
			if (!r || !u || !l || !d) {
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 1));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).rotationDegrees = !d ? 0 : !r ? ninty : !u ? 180 : twoHunderSeventy;
			}
			else {
				tileAndCirclePos.add(new TileAndCirclePos(t, (byte) 8));
				TileAndCirclePos.getTileAndCirclePos(tileAndCirclePos, t).rotationDegrees = !dl ? 0 : !rd ? ninty : !ru ? 180 : twoHunderSeventy;
			}
			return tileAndCirclePos;

		} */



		/*public void cornerer(){

			for (Tile t : stage.findATile(x,y).detectCornersOfCircle(circle)){
				switch (t.getTileCirclePos()){
					case 0: t.getTile().texture.setSecondaryTexture("SelU",.9f); break;
					case 1: t.getTile().texture.setSecondaryTexture("SelUR",.9f); break;
					case 2: t.getTile().texture.setSecondaryTexture("SelR",.9f); break;
					case 3: t.getTile().texture.setSecondaryTexture("SelRD",.9f); break;
					case 4: t.getTile().texture.setSecondaryTexture("SelD",.9f); break;
					case 5: t.getTile().texture.setSecondaryTexture("SelDL",.9f); break;
					case 6: t.getTile().texture.setSecondaryTexture("SelL",.9f); break;
					case 7: t.getTile().texture.setSecondaryTexture("SelLU",.9f); break;
					case 8: t.getTile().texture.setSecondaryTexture("SelInRU",.9f); break;
					case 9: t.getTile().texture.setSecondaryTexture("SelInRD",.9f); break;
					case 10: t.getTile().texture.setSecondaryTexture("SelInDL",.9f); break;
					case 11: t.getTile().texture.setSecondaryTexture("SelInLU",.9f); break;

				}
			}


		}*/


		public boolean isInsideOfCircle(float x, float y){
			for (Tile t : circle)
				if (t.x == x && t.y == y)
					return true;
			return false;
		}



	/* Circle edges:
	Missing LU, U, RU: 0  ┬
	Missing U, RU, R: 1   ┐
	Missing RU, R, RD: 2   ┤
	Missing R, RD, D: 3   ┘
	Missing RD, D, DL: 4  ┴
	Missing D, DL, L: 5   └
	Missing DL, L, LU: 6  ├
	Missing L, LU, U: 7   ┌

	Position of these shapes in a "circle", for further understanding:

	┌	┬	┐

	├		┤

	└	┴	┘


	Missing: RU: 8
	Missing: RD: 9
	Missing: DL: 10
	Missing: LU: 11



	*/


	}










}
/*				counter++;
				if(tt.x() == t.x() && tt.y() == t.y()+globalSize())
					U = true;
				if(tt.x() == t.x()+globalSize() && tt.y() == t.y()+globalSize())
					RU = true;
				if(tt.x() == t.x()+globalSize() && tt.y() == t.y())
					R = true;
				if(tt.x() == t.x()+globalSize() && tt.y() == t.y()-globalSize())
					RD = true;
				if(tt.x() == t.x() && tt.y() == t.y()-globalSize())
					D = true;
				if(tt.x() == t.x()-globalSize() && tt.y() == t.y()-globalSize())
					DL = true;
				if(tt.x() == t.x()-globalSize() && tt.y() == t.y())
					L = true;
				if(tt.x() == t.x()-globalSize() && tt.y() == t.y()+globalSize())
					LU = true;

*/