package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import static com.mygdx.game.Settings.*;
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

	public static Vector3 authenticClick(){
		Vector3 touchedPosition = (new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0));
		camara.camara.unproject(touchedPosition);
		return touchedPosition;
	}

	public static Vector3 flooredClick(){
		Vector3 touchedPosition = (new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0));
		camara.camara.unproject(touchedPosition);
		touchedPosition.x = (float) (globalSize() * floor((touchedPosition.x) / globalSize()));
		touchedPosition.y = (float) (globalSize() * floor((touchedPosition.y) / globalSize()));
		return touchedPosition;
	}

	public static float clickDistance(float fromX, float fromY){
		return round((sqrt(pow(fromX - flooredClick().x, 2) + pow(fromY - flooredClick().y, 2))) / globalSize());
	}


	public static HashSet<Enemy> rayCasting(float fromX, float fromY, float toX, float toY, Entity entityToIgnore,
											  ArrayList<Enemy> enemyList, ArrayList<Wall> wallList, boolean pierces) {
		print("started RayCasting");
		HashSet<Enemy> piercesEnemyArrayOfTargets = new HashSet<>();
		Ray rayCheckerCenter = new Ray(fromX + halfSize, fromY + halfSize,pierces);
		Ray rayCheckerDownLeft = new Ray(fromX + 1, fromY + 1,pierces);
		Ray rayCheckerDownRight = new Ray(fromX + globalSize() - 1, fromY + 1,pierces);
		Ray rayCheckerUpLeft = new Ray(fromX + 1, fromY + globalSize() - 1,pierces);
		Ray rayCheckerUpRight = new Ray(fromX + globalSize() - 1, fromY + globalSize() - 1,pierces);
		ArrayList<Ray> rayCheckerList = new ArrayList<>();
		rayCheckerList.add(rayCheckerCenter);
		rayCheckerList.add(rayCheckerDownLeft);
		rayCheckerList.add(rayCheckerDownRight);
		rayCheckerList.add(rayCheckerUpLeft);
		rayCheckerList.add(rayCheckerUpRight);
		for (Enemy e : enemyList) {

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
						for (Ray r: rayCheckerList) {
							print("returned pierces.size of list is of: " + piercesEnemyArrayOfTargets.size());
							for (EnemyAndTimesTheRayTouchedIt en : r.enemyRayCheck(enemyList)){
								piercesEnemyArrayOfTargets.add(en.getEnemy());
							}
						}
					else
						for(Ray r: rayCheckerList) {
							r.enemyRayCheck(enemyList);
							if (!r.getEnemiesThatGotHit().isEmpty())
								for (EnemyAndTimesTheRayTouchedIt en : r.getEnemiesThatGotHit())
									if (en.getTimesTheRayTouchedTheEnemy() >= 1) {
										HashSet<Enemy> temporal = new HashSet<>();
										temporal.add(en.getEnemy());
										print("returned temporal. Enemy X is: " + en.getEnemy().x + "Enemy Y is : " + en.getEnemy().y);
										return temporal;
									}
						}

					for(Ray r: rayCheckerList)
						r.wallRayCheck(wallList);
					if((rayCheckerCenter.timesRayTouchedWall + rayCheckerUpLeft.timesRayTouchedWall +
							rayCheckerDownLeft.timesRayTouchedWall + rayCheckerUpRight.timesRayTouchedWall +
							rayCheckerDownRight.timesRayTouchedWall) >= 325)
						return null;
				}
				if (!piercesEnemyArrayOfTargets.isEmpty())
					return piercesEnemyArrayOfTargets;
			}
		return null;

	}

	// lmao ure now useless dw ill find a use for u later
	public static HashSet<Enemy> clickAndRayCasting(float fromX, float fromY, Entity entityToIgnore,
													  ArrayList<Enemy> enemyList, ArrayList<Wall> wallList, boolean phases){
		Vector3 utilVector = flooredClick();
		float toX = utilVector.x;
		float toY = utilVector.y;

		return rayCasting(fromX,fromY,toX,toY,entityToIgnore,enemyList,wallList, phases);
	}

	public static boolean clickAndRayCastingButOnlyForWallsAndNowReturnsBoolean(float fromX, float fromY,
											 ArrayList<Wall> wallList){
		Vector3 utilVector = flooredClick();
		float toX = utilVector.x;
		float toY = utilVector.y;

		return rayCastingButOnlyChecksForWallsAndNowItReturnsBoolean(fromX,fromY,toX,toY,wallList);
	}



	public static boolean rayCastingButOnlyChecksForWallsAndNowItReturnsBoolean(float fromX, float fromY, float toX, float toY, ArrayList<Wall> wallList) {
				print("toX is: " + toX + " :toY is: " + toY);
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

				float rect = ((toY + halfSize) - (fromY + halfSize)) / ((toX + halfSize) - (fromX + halfSize));
				print ("rect is: " + rect);
				int sign = 1;
				if (toX < fromX)
					sign = -1;
				if (rect == POSITIVE_INFINITY || rect == NEGATIVE_INFINITY)
					sign = 0;
				print ("sign is: " + sign);
				for (int i = 0; i < Utils.pickValueAUnlessEqualsZeroThenPickB(abs(toX - fromX), abs(toY - fromY)); i++) {
					for (Ray r : rayCheckerList) {
						r.x += sign;
						if (sign != 0)
							r.y += rect * sign;
						else if (rect == POSITIVE_INFINITY)
							r.y++;
						else
							r.y--;
					}

					if (isDevMode())
						for (Ray r : rayCheckerList)
							r.render();

					for (Ray r : rayCheckerList)
						r.wallRayCheck(wallList);
					if((rayCheckerCenter.timesRayTouchedWall + rayCheckerUpLeft.timesRayTouchedWall +
							rayCheckerDownLeft.timesRayTouchedWall + rayCheckerUpRight.timesRayTouchedWall +
							rayCheckerDownRight.timesRayTouchedWall) >= 325) {
						print("returned false");
						rayCheckerList.clear();
						return false;
					}
				}
		print("returned true");
		rayCheckerList.clear();
		return true;
	}





	private static class Ray extends Entity {
		short timesRayTouchedWall = 0, timesRayTouchedOtherEnemy = 0;
		boolean phases;
		ArrayList<EnemyAndTimesTheRayTouchedIt> hitEnemies;
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

		public ArrayList<EnemyAndTimesTheRayTouchedIt> enemyRayCheck(ArrayList<Enemy> enemyList) {
			for (Enemy en : enemyList) {
					if (x < en.x + globalSize() && x + 1 > en.x && y < en.y + globalSize() && y + 1 > en.y && !en.isDead) {
						if(phases) {
							timesRayTouchedOtherEnemy++;
							hitEnemies.add(new EnemyAndTimesTheRayTouchedIt(en));
						}
						else{
							if(!isEnemyAlreadyOnHashSet(en))
								enemiesThatGotHit.add(new EnemyAndTimesTheRayTouchedIt(en));
							enemySearcher(en).touch();
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

		private EnemyAndTimesTheRayTouchedIt enemySearcher(Enemy enemy){
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

		public void touch(){timesTheRayTouchedTheEnemy++;}

		public Enemy getEnemy(){
			return objective;
		}

		public int getTimesTheRayTouchedTheEnemy(){
			return timesTheRayTouchedTheEnemy;
		}
	}




}
