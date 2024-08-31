package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Utils;

import java.util.ArrayList;

import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.Settings.isDevMode;
import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.POSITIVE_INFINITY;
import static java.lang.Math.*;
import static java.lang.Math.pow;

public class ClickDetector implements Utils {
	static Camara camara;
	static float halfSize = globalSize() / 2f;

	public ClickDetector(Camara camara){
		ClickDetector.camara = camara;
	}


	public void camaraUpdater(Camara camara){
		ClickDetector.camara = camara;
	}

	public static Vector3 click(){
		Vector3 touchedPosition = (new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0));
		camara.camara.unproject(touchedPosition);
		touchedPosition.x = (float) (globalSize() * floor((touchedPosition.x) / globalSize()));
		touchedPosition.y = (float) (globalSize() * floor((touchedPosition.y) / globalSize()));
		return touchedPosition;
	}

	public static float clickDistance(float fromX, float fromY){
		return round((sqrt(pow(fromX - click().x, 2) + pow(fromY - click().y, 2))) / globalSize());
	}


	public static ArrayList<Enemy> rayCasting(float fromX, float fromY, float toX, float toY, Entity entityToIgnore,
											  ArrayList<Enemy> enemyList, ArrayList<Wall> wallList, boolean pierces) {
		ArrayList<Enemy> piercesEnemyArrayOfTargets = new ArrayList<>();
		Ray rayCheckerCenter = new Ray(fromX + halfSize, fromY + halfSize);
		Ray rayCheckerDownLeft = new Ray(fromX + 1, fromY + 1);
		Ray rayCheckerDownRight = new Ray(fromX + globalSize() - 1, fromY + 1);
		Ray rayCheckerUpLeft = new Ray(fromX + 1, fromY + globalSize() - 1);
		Ray rayCheckerUpRight = new Ray(fromX + globalSize() - 1, fromY + globalSize() - 1);
		ArrayList<Ray> rayCheckerList = new ArrayList<>();
		rayCheckerList.add(rayCheckerCenter);
		rayCheckerList.add(rayCheckerDownLeft);
		rayCheckerList.add(rayCheckerDownRight);
		rayCheckerList.add(rayCheckerUpLeft);
		rayCheckerList.add(rayCheckerUpRight);
		for (Enemy e : enemyList) {
			if (toX == e.x && toY == e.y && !e.isDead) {
				float rect = ((e.y + halfSize) - (fromY + halfSize)) / ((e.x + halfSize) - (fromX + halfSize));
				int sign = 1;
				if (e.x < fromX)
					sign = -1;
				if (rect == POSITIVE_INFINITY || rect == NEGATIVE_INFINITY)
					sign = 0;
				for (int i = 0; i < Utils.pickValueAUnlessEqualsZeroThenPickB(abs(e.x - fromX), abs(e.y - fromY)); i++) {
					for (Entity r : rayCheckerList)
						r.x += sign;
					if (sign != 0) {
						for (Entity r : rayCheckerList)
							r.y += rect * sign;;

					} else if (rect == POSITIVE_INFINITY) {
						for (Entity r : rayCheckerList)
							r.y++;

					} else if (rect == NEGATIVE_INFINITY) {
						for (Entity r : rayCheckerList)
							r.y--;

					}
					if(isDevMode())
						for (Entity r : rayCheckerList)
							r.render();

					if (pierces)
						for (Ray r: rayCheckerList)
							// it's alright, if pierces, then this returns an ArrayList<Enemy>
							piercesEnemyArrayOfTargets = (ArrayList<Enemy>) r.enemyRayCheck(enemyList);
					else
						for(Ray r: rayCheckerList)
							if(!r.getEnemiesThatGotHit().isEmpty())
								for(EnemyAndTimesTheRayTouchedIt en : r.getEnemiesThatGotHit())
									if (en.getTimesTheRayTouchedTheEnemy() >= 325) {
										ArrayList<Enemy> temporal = new ArrayList<>();
										temporal.add(en.getEnemy());
										return temporal;
									}

					for(Ray r: rayCheckerList)
						r.wallRayCheck(wallList);
					if((rayCheckerCenter.timesRayTouchedWall + rayCheckerUpLeft.timesRayTouchedWall +
							rayCheckerDownLeft.timesRayTouchedWall + rayCheckerUpRight.timesRayTouchedWall +
							rayCheckerDownRight.timesRayTouchedWall) >= 325)
						return null;
				}
				return piercesEnemyArrayOfTargets;
			}
		}
		return null;

	}

	// lmao ure now useless dw ill find a use for u later
	public static ArrayList<Enemy> clickAndRayCasting(float fromX, float fromY, Entity entityToIgnore,
													  ArrayList<Enemy> enemyList, ArrayList<Wall> wallList, boolean phases){
		Vector3 utilVector = click();
		float toX = utilVector.x;
		float toY = utilVector.y;

		return rayCasting(fromX,fromY,toX,toY,entityToIgnore,enemyList,wallList, phases);
	}

	public static boolean clickAndRayCastingButOnlyForWallsAndNowReturnsBoolean(float fromX, float fromY,
											 ArrayList<Wall> wallList){
		Vector3 utilVector = click();
		float toX = utilVector.x;
		float toY = utilVector.y;

		return rayCastingButOnlyChecksForWallsAndNowItReturnsBoolean(fromX,fromY,toX,toY,wallList);
	}



	public static boolean rayCastingButOnlyChecksForWallsAndNowItReturnsBoolean(float fromX, float fromY, float toX, float toY, ArrayList<Wall> wallList) {
				Ray rayCheckerCenter = new Ray(fromX + halfSize, fromY + halfSize);
				Ray rayCheckerDownLeft = new Ray(fromX + 1, fromY + 1);
				Ray rayCheckerDownRight = new Ray(fromX + globalSize() - 1, fromY + 1);
				Ray rayCheckerUpLeft = new Ray(fromX + 1, fromY + globalSize() - 1);
				Ray rayCheckerUpRight = new Ray(fromX + globalSize() - 1, fromY + globalSize() - 1);
				ArrayList<Ray> rayCheckerList = new ArrayList<>();
				rayCheckerList.add(rayCheckerCenter);
				rayCheckerList.add(rayCheckerDownLeft);
				rayCheckerList.add(rayCheckerDownRight);
				rayCheckerList.add(rayCheckerUpLeft);
				rayCheckerList.add(rayCheckerUpRight);

				float rect = ((toY + halfSize) - (fromY + halfSize)) / ((toY + halfSize) - (fromX + halfSize));
				int sign = 1;
				if (toX < fromX)
					sign = -1;
				if (rect == POSITIVE_INFINITY || rect == NEGATIVE_INFINITY)
					sign = 0;
				for (int i = 0; i < Utils.pickValueAUnlessEqualsZeroThenPickB(abs(toX - fromX), abs(toY - fromY)); i++) {
					for (Entity r : rayCheckerList) {
						r.x += sign;
					}
					if (sign != 0) {
						for (Entity r : rayCheckerList)
							r.y += rect * sign;
					} else if (rect == POSITIVE_INFINITY) {
						for (Entity r : rayCheckerList)
							r.y++;
					} else {
						for (Entity r : rayCheckerList)
							r.y--;
					}
					if (isDevMode())
						for (Entity r : rayCheckerList)
							r.render();

					for (Ray r : rayCheckerList)
						r.wallRayCheck(wallList);
					if((rayCheckerCenter.timesRayTouchedWall + rayCheckerUpLeft.timesRayTouchedWall +
							rayCheckerDownLeft.timesRayTouchedWall + rayCheckerUpRight.timesRayTouchedWall +
							rayCheckerDownRight.timesRayTouchedWall) >= 325)
						return false;
				}
		return true;
	}





	private static class Ray extends Entity {
		short timesRayTouchedWall = 0, timesRayTouchedOtherEnemy = 0;
		boolean phases;
		ArrayList<Enemy> hitEnemies;
		ArrayList<EnemyAndTimesTheRayTouchedIt> enemiesThatGotHit;

		// chillllll, idea, might use them later
		public Ray(String texture,float x, float y, float base, float height, boolean phases){
			super(texture,x,y,base,height);
			if(phases) {
				this.phases = true;
				hitEnemies = new ArrayList<>();
			}
			else
				enemiesThatGotHit = new ArrayList<>();
		}

		public Ray(String texture,float x, float y, float base, float height){
			super(texture,x,y,base,height);
			enemiesThatGotHit = new ArrayList<>();
		}

		public Ray(float x, float y,boolean phases){
			super("FourByFour",x,y,4,4);
			if(phases) {
				this.phases = true;
				hitEnemies = new ArrayList<>();
			}
			else
				enemiesThatGotHit = new ArrayList<>();
		}

		public Ray(float x, float y){
			super("FourByFour",x,y,4,4);
			enemiesThatGotHit = new ArrayList<>();
		}


		public void wallRayCheck(ArrayList<Wall> wallList){
			for (Wall w : wallList) {
				if (x < w.x + globalSize() && x + 1 > w.x && y < w.y + globalSize() && y + 1 > w.y) {
					timesRayTouchedWall++;
				}
			}
		}

		public ArrayList<?> enemyRayCheck(ArrayList<Enemy> enemyList) {
			for (Enemy en : enemyList) {
					if (x < en.x + globalSize() && x + 1 > en.x && y < en.y + globalSize() && y + 1 > en.y && !en.isDead) {
						if(phases) {
							timesRayTouchedOtherEnemy++;
							hitEnemies.add(en);
						}
						else{
							if(!isEnemyAlreadyOnHashSet(en))
								enemiesThatGotHit.add(new EnemyAndTimesTheRayTouchedIt(en));
							hashSetEnemySearcher(en).touch();
						}
					}
			}
			if (phases)
				return hitEnemies;
			else
				return enemiesThatGotHit;
		}

		private boolean isEnemyAlreadyOnHashSet(Enemy enemy){
			for(EnemyAndTimesTheRayTouchedIt e : enemiesThatGotHit)
				if (e.getEnemy() == enemy)
					return true;
			return false;
		}

		private EnemyAndTimesTheRayTouchedIt hashSetEnemySearcher(Enemy enemy){
			for(EnemyAndTimesTheRayTouchedIt e : enemiesThatGotHit)
				if (e.getEnemy() == enemy)
					return e;
			return null;
		}

		public ArrayList<EnemyAndTimesTheRayTouchedIt> getEnemiesThatGotHit(){
			return enemiesThatGotHit;
		}

		// intellij wont let me have anything i might use in the future without shouting me for it.
		public void rayCheck(ArrayList<Enemy> enemyList, ArrayList<Wall> wallList){
			wallRayCheck(wallList);
			enemyRayCheck(enemyList);
		}

	}


	private static class EnemyAndTimesTheRayTouchedIt{
		Enemy objective;
		int timesTheRayTouchedTheEnemy;

		public EnemyAndTimesTheRayTouchedIt(Enemy objective){
			this.objective = objective;
			timesTheRayTouchedTheEnemy = 0;
		}

		public void touch(){
			timesTheRayTouchedTheEnemy++;
		}

		public Enemy getEnemy(){
			return objective;
		}

		public int getTimesTheRayTouchedTheEnemy(){
			return timesTheRayTouchedTheEnemy;
		}
	}




}
