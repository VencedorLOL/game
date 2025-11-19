package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Utils;
import java.util.ArrayList;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.Actor.actors;
import static com.mygdx.game.items.Wall.walls;
import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.POSITIVE_INFINITY;
import static java.lang.Math.*;
import static java.lang.Math.pow;

public class ClickDetector implements Utils {
	static float halfSize = globalSize() / 2f;


	public static Vector3 authenticClick(){
		Vector3 touchedPosition = (new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0));
		getCamara().camara.unproject(touchedPosition);
		return touchedPosition;
	}

	public static Vector3 roundedClick(){
		Vector3 touchedPosition = (new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0));
		getCamara().camara.unproject(touchedPosition);
		touchedPosition.x = (float) (globalSize() * floor((touchedPosition.x) / globalSize()));
		touchedPosition.y = (float) (globalSize() * floor((touchedPosition.y) / globalSize()));
		return touchedPosition;
	}

	public static float clickDistance(float fromX, float fromY){
		return round((sqrt(pow(fromX - roundedClick().x, 2) + pow(fromY - roundedClick().y, 2))) / globalSize());
	}


	public static ArrayList<Actor> rayCasting(float fromX, float fromY, float toX, float toY, ArrayList<Actor> entityToIgnore, boolean pierces, Entity runFrom) {
		ArrayList<Actor> piercesEnemyArrayOfTargets = new ArrayList<>();
		Ray rayCheckerCenter = new Ray(fromX + halfSize, fromY + halfSize, pierces);
//		Ray rayCheckerDownLeft = new Ray(fromX + 1, fromY + 1,pierces);
//		Ray rayCheckerDownRight = new Ray(fromX + globalSize() - 1, fromY + 1,pierces);
//		Ray rayCheckerUpLeft = new Ray(fromX + 1, fromY + globalSize() - 1,pierces);
//		Ray rayCheckerUpRight = new Ray(fromX + globalSize() - 1, fromY + globalSize() - 1,pierces);
		ArrayList<Ray> rayCheckerList = new ArrayList<>();
		rayCheckerList.add(rayCheckerCenter);
//		rayCheckerList.add(rayCheckerDownLeft);
//		rayCheckerList.add(rayCheckerDownRight);
//		rayCheckerList.add(rayCheckerUpLeft);
//		rayCheckerList.add(rayCheckerUpRight);

		float rect = ((toY + halfSize) - (fromY + halfSize)) / ((toX + halfSize) - (fromX + halfSize));

		int sign = 1;
		if (toX < fromX)
			sign = -1;
		if (rect == POSITIVE_INFINITY || rect == NEGATIVE_INFINITY)
			sign = 0;

		for (int i = 0; i < Utils.pickValueAUnlessEqualsZeroThenPickB(abs(toX - fromX), abs(toY - fromY)); i++) {
			for (Entity r : rayCheckerList)
				r.x += sign;
			if (sign != 0) {
				for (Entity r : rayCheckerList)
					r.y += rect * sign;

			} else if (rect == POSITIVE_INFINITY) {
				for (Entity r : rayCheckerList)
					r.y++;

			} else if (rect == NEGATIVE_INFINITY) {
				for (Entity r : rayCheckerList)
					r.y--;
			}
			for (Ray r : rayCheckerList)
				r.wallRayCheck();

			if ((rayCheckerCenter.timesRayTouchedWall /* + rayCheckerUpLeft.timesRayTouchedWall +
							//rayCheckerDownLeft.timesRayTouchedWall + rayCheckerUpRight.timesRayTouchedWall +
							/*rayCheckerDownRight.timesRayTouchedWall*/) >= globalSize() / 3)
				return null;

			for (Ray r : rayCheckerList) {
				if (r.x == toX + halfSize && r.y == toY + halfSize && !pierces) {
					if (!r.getEnemiesThatGotHit().isEmpty())
						for (ActorAndTimesTheRayTouchedIt en : r.getEnemiesThatGotHit())
							if (en.getTimesTheRayTouchedTheActor() >= globalSize() / 32) {
								ArrayList<Actor> temporal = new ArrayList<>();
								temporal.add(en.getActor());
							//	print("returned temporal. Enemy X is: " + en.getActor().x + " Enemy Y is : " + en.getActor().y);
								if (entityToIgnore != null)
									for (Actor a : entityToIgnore)
										temporal.removeIf(actor -> actor == a);

								return temporal.isEmpty() ? null : temporal;
							}
					return null;
				}
			}

			if (isDevMode())
				for (Entity r : rayCheckerList)
					r.render();


			if (pierces)
				for (Ray r : rayCheckerList) {
					for (ActorAndTimesTheRayTouchedIt en : r.enemyRayCheck(entityToIgnore)) {
						if (!valueSearcher(piercesEnemyArrayOfTargets,en.objective))
							piercesEnemyArrayOfTargets.add(en.getActor());
					}
				}
			else for (Ray r : rayCheckerList) {
					r.enemyRayCheck(entityToIgnore);
					if (!r.getEnemiesThatGotHit().isEmpty())
						for (ActorAndTimesTheRayTouchedIt en : r.getEnemiesThatGotHit())
							if (en.getTimesTheRayTouchedTheActor() >= globalSize() / 32) {
								ArrayList<Actor> temporal = new ArrayList<>();
								temporal.add(en.getActor());
							//	print("returned temporal. Enemy X is: " + en.getActor().x + " Enemy Y is : " + en.getActor().y);
								if (entityToIgnore != null)
									for (Actor a : entityToIgnore)
										temporal.removeIf(actor -> actor == a);
								if (!temporal.isEmpty())
									return temporal;
							}
				}

			for (Ray r : rayCheckerList) {
				if (r.x == toX + halfSize && r.y == toY + halfSize && pierces) {
					if (!piercesEnemyArrayOfTargets.isEmpty()) {
						if (entityToIgnore != null)
							for (Actor a : entityToIgnore)
								piercesEnemyArrayOfTargets.removeIf(actor -> actor == a);
						return piercesEnemyArrayOfTargets.isEmpty() ? null : piercesEnemyArrayOfTargets;
					}
				}
			}



		}
		if (!piercesEnemyArrayOfTargets.isEmpty()) {
			if (entityToIgnore != null)
				for (Actor a : entityToIgnore)
					piercesEnemyArrayOfTargets.removeIf(actor -> actor == a);
			return piercesEnemyArrayOfTargets.isEmpty() ? null : piercesEnemyArrayOfTargets;
		}

		return null;

	}

	// lmao ure now useless dw ill find a use for u later
	public static ArrayList<Actor> clickAndRayCasting(float fromX, float fromY, ArrayList<Actor> entityToIgnore, boolean phases){
		Vector3 utilVector = roundedClick();
		float toX = utilVector.x;
		float toY = utilVector.y;

		return rayCasting(fromX,fromY,toX,toY,entityToIgnore, phases,null);
	}

	public static boolean clickAndRayCastingButOnlyForWallsAndNowReturnsBoolean(float fromX, float fromY){
		Vector3 utilVector = roundedClick();
		float toX = utilVector.x;
		float toY = utilVector.y;

		return wallRayCasting(fromX,fromY,toX,toY);
	}



	public static boolean wallRayCasting(float fromX, float fromY, float toX, float toY) {
				Ray rayCheckerCenter = new Ray(fromX + halfSize, fromY + halfSize);
//				Ray rayCheckerDownLeft = new Ray(fromX + 1, fromY + 1);
//				Ray rayCheckerDownRight = new Ray(fromX + globalSize() - 1, fromY + 1);
//				Ray rayCheckerUpLeft = new Ray(fromX + 1, fromY + globalSize() - 1);
//				Ray rayCheckerUpRight = new Ray(fromX + globalSize() - 1, fromY + globalSize() - 1);
				ArrayList<Ray> rayCheckerList = new ArrayList<>();
				rayCheckerList.add(rayCheckerCenter);
//				rayCheckerList.add(rayCheckerDownLeft);
//				rayCheckerList.add(rayCheckerDownRight);
//				rayCheckerList.add(rayCheckerUpLeft);
//				rayCheckerList.add(rayCheckerUpRight);

				float rect = ((toY + halfSize) - (fromY + halfSize)) / ((toX + halfSize) - (fromX + halfSize));

				int sign = 1;
				if (toX < fromX)
					sign = -1;
				if (rect == POSITIVE_INFINITY || rect == NEGATIVE_INFINITY)
					sign = 0;
				for (int i = 0; i < Utils.pickValueAUnlessEqualsZeroThenPickB(abs(toX - fromX), abs(toY - fromY)); i++) {
					for (Ray r : rayCheckerList) {
						r.x += sign;
						if (sign != 0)
							r.y += rect * sign;
						else if (rect == POSITIVE_INFINITY)
							r.y++;
						else
							r.y--;
						r.wallRayCheck();
					}

					if (isDevMode())
						for (Ray r : rayCheckerList)
							r.render();
					if((rayCheckerCenter.timesRayTouchedWall /* + rayCheckerUpLeft.timesRayTouchedWall +
							rayCheckerDownLeft.timesRayTouchedWall + rayCheckerUpRight.timesRayTouchedWall +
							rayCheckerDownRight.timesRayTouchedWall) */ >= globalSize() / 3)) {
						return true;
					}
				}
		return false;
	}

	private static boolean valueSearcher(ArrayList<Actor> theListInQuestion, Actor theValueInQuestion){
		for (Actor l : theListInQuestion){
			if(l == theValueInQuestion)
				return true;
		}
		return false;
	}



	private static class Ray extends Entity {
		short timesRayTouchedWall = 0, timesRayTouchedOtherEnemy = 0;
		boolean phases;
		ArrayList<ActorAndTimesTheRayTouchedIt> hitEnemies;
		ArrayList<ActorAndTimesTheRayTouchedIt> enemiesThatGotHit;


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


		public void wallRayCheck(){
			for (Wall w : walls) {
				if (x < w.x + globalSize() && x + 1 > w.x && y < w.y + globalSize() && y + 1 > w.y) {
					timesRayTouchedWall++;
				}
			}
		}

		public ArrayList<ActorAndTimesTheRayTouchedIt> enemyRayCheck(ArrayList<Actor> enemiesToIgnore) {
			if (enemiesToIgnore != null)
				for (Entity e : enemiesToIgnore)
					for (Actor en : actors) {
						if (x < en.x + globalSize() && x + 1 > en.x && y < en.y + globalSize() && y + 1 > en.y && !en.isDead && en != e) {
							if (phases) {
								timesRayTouchedOtherEnemy++;
								hitEnemies.add(new ActorAndTimesTheRayTouchedIt(en));
							} else {
								if (!isEnemyAlreadyOnHashSet(en))
									enemiesThatGotHit.add(new ActorAndTimesTheRayTouchedIt(en));
								enemySearcher(en).touch();
							}
						}
					}
			else
				for (Actor en : actors)
					if (x < en.x + globalSize() && x + 1 > en.x && y < en.y + globalSize() && y + 1 > en.y && !en.isDead) {
						if (phases) {
							timesRayTouchedOtherEnemy++;
							hitEnemies.add(new ActorAndTimesTheRayTouchedIt(en));
						} else {
							if (!isEnemyAlreadyOnHashSet(en))
								enemiesThatGotHit.add(new ActorAndTimesTheRayTouchedIt(en));
							enemySearcher(en).touch();
						}
					}
			if (phases)
				return hitEnemies;
			else
				return enemiesThatGotHit;
		}

		private boolean isEnemyAlreadyOnHashSet(Actor enemy){
			for(ActorAndTimesTheRayTouchedIt e : enemiesThatGotHit)
				if (e.getActor() == enemy)
					return true;
			return false;
		}

		private boolean isEnemyAlreadyOnArrayList(Actor enemy){
			for(ActorAndTimesTheRayTouchedIt e : hitEnemies)
				if (e.getActor() == enemy)
					return true;
			return false;
		}

		private ActorAndTimesTheRayTouchedIt enemySearcher(Actor enemy){
			for(ActorAndTimesTheRayTouchedIt e : enemiesThatGotHit)
				if (e.getActor() == enemy)
					return e;
			return null;
		}

		public ArrayList<ActorAndTimesTheRayTouchedIt> getEnemiesThatGotHit(){
			return enemiesThatGotHit;
		}

	}


	private static class ActorAndTimesTheRayTouchedIt{
		Actor objective;
		int timesTheRayTouchedTheEnemy;

		public ActorAndTimesTheRayTouchedIt(Actor objective){
			this.objective = objective;
			timesTheRayTouchedTheEnemy = 0;
		}

		public void touch(){timesTheRayTouchedTheEnemy++;}

		public Actor getActor(){
			return objective;
		}

		public int getTimesTheRayTouchedTheActor(){
			return timesTheRayTouchedTheEnemy;
		}
	}




}
