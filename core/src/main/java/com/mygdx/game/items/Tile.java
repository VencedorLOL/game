package com.mygdx.game.items;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Tile {

	float x,y,base,height;
	public int ID;
	public static int IDState = 0;
	Floor texture;


	public Tile(float x, float y, Floor texture){
		this.x = x;
		this.y = y;
		base = globalSize();
		height = globalSize();
		this.texture = texture;
		ID = IDState + 1;
	}

	public Tile(float x, float y, float base, float height, Floor texture){
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		this.texture = texture;
		ID = IDState + 1;
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


	public ArrayList<Tile> circle(ArrayList<Tile> tileset, float radius){
		radius += 0.5f;
		ArrayList<Tile> circle = new ArrayList<>();
		circle.add(this);
		for (Tile t : orthogonalTiles(tileset)) {
			if (relativeModuleTo(t) <= radius)
				circle.add(t);
		}
		// +3 jst to be safe
		int failsafe = (int) (radius + 3);
		while(true){
			int addedATileThisLoop = 0;
			ArrayList<Tile> avoidConcurrentModification = new ArrayList<>();
			for(Tile t : circle){
				for (Tile tt : t.orthogonalTiles(tileset)) {
					if (relativeModuleTo(tt) <= radius && !tt.isTileAlreadyInList(circle) && !tt.isTileAlreadyInList(avoidConcurrentModification)) {
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
		return circle;
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

	public ArrayList<TileAndCirclePos> detectCornersOfCircle(ArrayList<Tile> circle) {
		ArrayList<TileAndCirclePos> tileAndCirclePos = new ArrayList<>();
		for (Tile t : circle){
			boolean LU = false,U= false,RU= false,R= false,RD= false,D= false,DL= false,L = false;
			byte couner2 = (byte) t.orthogonalTiles(circle).size();
			for (Tile tt : t.orthogonalTiles(circle)){
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
			}
			//flat
			if (!U&&!R){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)1)); continue;}
			if (!D&&!R){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)3)); continue;}
			if (!D&&!L){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)5)); continue;}
			if (!U&&!L){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)7)); continue;}
			// corner
			if (!LU&&!RU || !U){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)0)); continue;}
			if (!RD&&!RU || !R){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)2)); continue;}
			if (!DL&&!RD || !D){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)4)); continue;}
			if (!DL&&!LU || !L){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)6)); continue;}
			// interior. thx smc.
			if (!RU){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)8)); continue;}
			if (!RD){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)9)); continue;}
			if (!DL){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)10)); continue;}
			if (!LU){ tileAndCirclePos.add(new TileAndCirclePos(t,(byte)11));}
		}
		return tileAndCirclePos;
	}





	public static class TileAndCirclePos{
		Tile tile;
		byte tileCirclePos;

		public TileAndCirclePos(Tile tile, byte tileCirclePos){
			this.tile = tile;
			this.tileCirclePos = tileCirclePos;
		}

		public void setTile(Tile tile){this.tile = tile;}

		public Tile getTile() {return tile;}

		public byte getTileCirclePos() {return tileCirclePos;}

		public void setTileCirclePos(byte tileCirclePos) {this.tileCirclePos = tileCirclePos;}
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