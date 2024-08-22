package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Utils;

import java.util.ArrayList;

import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.POSITIVE_INFINITY;
import static java.lang.Math.*;
import static java.lang.Math.pow;

public class ClickDetector implements Utils {
	static Camara camara;


	public ClickDetector(Camara camara){
		ClickDetector.camara = camara;
	}


	public void camaraUpdater(Camara camara){
		ClickDetector.camara = camara;
	}

	public static Vector3 click(){
		Vector3 touchedPosition = (new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0));
		camara.camara.unproject(touchedPosition);
		touchedPosition.x = (float) (128 * floor((touchedPosition.x) / 128));
		touchedPosition.y = (float) (128 * floor((touchedPosition.y) / 128));
		return touchedPosition;
	}

	public static Entity rayCasting(float fromX, float fromY, float toX, float toY, Entity entityToIgnore,
									ArrayList<Enemy> enemyList, ArrayList<Wall> wallList, float range) {
		// clue: the flaw starts here. This comment doesn't make sense if you don't read the rest of the method, so, do it
		short timesRayTouchedWall = 0, touchedDL = 0, touchedDR = 0, touchedUL = 0, touchedUR = 0, touchedC = 0,
				timesRayTouchedOtherEnemy = 0, raysThroughWalls = 0,
				touchedEDL = 0, touchedEDR = 0, touchedEUL = 0, touchedEUR = 0, touchedEC = 0, raysThroughEnemies = 0;

		for (Enemy e : enemyList) {
			if (toX == e.x && toY == e.y && !e.isDead) {
				// distance gathered using pythagorean theorem.
				float distance = (int) round((sqrt(pow(fromX - e.x, 2) + pow(fromY - e.y, 2))) / 128);
				// gettin' the rect following (y2 - y2) / (x2 - x1)
				float rect = ((e.y + 64) - (fromY + 64)) / ((e.x + 64) - (fromX + 64));
				Entity rayCheckerCenter = new Entity("FourByFour", fromX + 64, fromY + 64, 4, 4);
				Entity rayCheckerDownLeft = new Entity("FourByFour", fromX, fromY, 4, 4);
				Entity rayCheckerDownRight = new Entity("FourByFour", fromX + 128, fromY, 4, 4);
				Entity rayCheckerUpLeft = new Entity("FourByFour", fromX, fromY + 128, 4, 4);
				Entity rayCheckerUpRight = new Entity("FourByFour", fromX + 128, fromY + 128, 4, 4);
				// just centring the rays.
				rayCheckerDownLeft.x += 1;
				rayCheckerDownRight.x -= 1;
				rayCheckerUpLeft.x += 1;
				rayCheckerUpRight.x -= 1;
				int sign = 1;
				//so the detector rays go negative if e.x < x. This one didn't explain much bruh.
				if (e.x < fromX)
					sign = -1;
				// For some reason if I put 1/0 a warning pops up,
				// but if I put these constants, which are literally the same thing as I had
				// (1/0 and -1/0) it doesn't. Bruh.
				// for later math, when the rect isn't a function.
				if (rect == POSITIVE_INFINITY || rect == NEGATIVE_INFINITY)
					sign = 0;
				//if horizontal difference == 0, use vertical as the number of times the loop will go on.
				// this algorithm uses a linear function approach for the ray casting.
				// increases x coords by 1 (or -1) and y by 1 * rect. If it isn't a function (multiple values of y
				// aka, the lines go straight up or straight down), sign must be 0, x isn't changed at all and y
				//goes up and down accordingly. Then just detect if the ray's colliding with something.
				// more explanation below.
				for (int i = 0; i < Utils.pickValueAUnlessEqualsZeroThenPickB(abs(e.x - fromX), abs(e.y - fromY)); i++) {
					rayCheckerCenter.x += sign;
					rayCheckerDownLeft.x += sign;
					rayCheckerDownRight.x += sign;
					rayCheckerUpLeft.x += sign;
					rayCheckerUpRight.x += sign;
					if (sign != 0) {
						rayCheckerCenter.y += rect * sign;
						rayCheckerDownLeft.y += rect * sign;
						rayCheckerDownRight.y += rect * sign;
						rayCheckerUpLeft.y += rect * sign;
						rayCheckerUpRight.y += rect * sign;
					} else if (rect == POSITIVE_INFINITY) {
						rayCheckerCenter.y++;
						rayCheckerDownLeft.y++;
						rayCheckerDownRight.y++;
						rayCheckerUpLeft.y++;
						rayCheckerUpRight.y++;
					} else if (rect == NEGATIVE_INFINITY) {
						rayCheckerCenter.y--;
						rayCheckerDownLeft.y--;
						rayCheckerDownRight.y--;
						rayCheckerUpLeft.y--;
						rayCheckerUpRight.y--;
					}
					rayCheckerCenter.render();
					rayCheckerDownLeft.render();
					rayCheckerDownRight.render();
					rayCheckerUpLeft.render();
					rayCheckerUpRight.render();

					//if it collides with a wall, it'll raise the total number of times rays have ever touched a wall
					// on this calculation; and by one which ray touched a wall.
					for (Wall w : wallList) {
						if (rayCheckerCenter.x < w.x + 128 && rayCheckerCenter.x + 1 > w.x && rayCheckerCenter.y < w.y + 128 && rayCheckerCenter.y + 1 > w.y) {
							timesRayTouchedWall++;
							touchedC++;
						}
						if (rayCheckerDownLeft.x < w.x + 128 && rayCheckerDownLeft.x + 1 > w.x && rayCheckerDownLeft.y < w.y + 128 && rayCheckerDownLeft.y + 1 > w.y) {
							timesRayTouchedWall++;
							touchedDL++;
						}
						if (rayCheckerDownRight.x < w.x + 128 && rayCheckerDownRight.x + 1 > w.x && rayCheckerDownRight.y < w.y + 128 && rayCheckerDownRight.y + 1 > w.y) {
							timesRayTouchedWall++;
							touchedDR++;
						}
						if (rayCheckerUpLeft.x < w.x + 128 && rayCheckerUpLeft.x + 1 > w.x && rayCheckerUpLeft.y < w.y + 128 && rayCheckerUpLeft.y + 1 > w.y) {
							timesRayTouchedWall++;
							touchedUL++;
						}
						if (rayCheckerUpRight.x < w.x + 128 && rayCheckerUpRight.x + 1 > w.x && rayCheckerUpRight.y < w.y + 128 && rayCheckerUpRight.y + 1 > w.y) {
							timesRayTouchedWall++;
							touchedUR++;
						}
						// then if a particular ray touched the wall 64 times (half a tile)  'raysThroughWalls' (rTW from now on)
						// goes up by one. If rTW >= 3, or if rays touched in total walls 325 times, the attack doesn't happen.
						if (touchedC >= 64) {
							raysThroughWalls++;
							touchedC = -256;
						}
						if (touchedDL >= 64) {
							raysThroughWalls++;
							touchedDL = -256;
						}
						if (touchedDR >= 64) {
							raysThroughWalls++;
							touchedDR = -256;
						}
						if (touchedUL >= 64) {
							raysThroughWalls++;
							touchedUL = -256;
						}
						if (touchedUR >= 64) {
							raysThroughWalls++;
							touchedUR = -256;
						}
						if (timesRayTouchedWall >= 325 || raysThroughWalls >= 3)
							return null;

					}
					// with enemies is same as walls, but if the checks of touching another enemy go through,
					// it returns the other enemy instead of returning nothing.
					//TODO Yes, ik there's a fundamental flaw in here (That's your homework for today: detect the flaw)
					// Buuuuttttt... i don't care atm
					for (Enemy en : enemyList) {
						if (!en.isDead) {
							float distanceAux = (int) round((sqrt(pow(fromX - en.x, 2) + pow(fromY - en.y, 2))) / 128);
							if (rayCheckerCenter.x < en.x + 128 && rayCheckerCenter.x + 1 > en.x && rayCheckerCenter.y < en.y + 128 && rayCheckerCenter.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEC++;
							}
							if (rayCheckerDownLeft.x < en.x + 128 && rayCheckerDownLeft.x + 1 > en.x && rayCheckerDownLeft.y < en.y + 128 && rayCheckerDownLeft.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEDL++;
							}
							if (rayCheckerDownRight.x < en.x + 128 && rayCheckerDownRight.x + 1 > en.x && rayCheckerDownRight.y < en.y + 128 && rayCheckerDownRight.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEDR++;
							}
							if (rayCheckerUpLeft.x < en.x + 128 && rayCheckerUpLeft.x + 1 > en.x && rayCheckerUpLeft.y < en.y + 128 && rayCheckerUpLeft.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEUL++;
							}
							if (rayCheckerUpRight.x < en.x + 128 && rayCheckerUpRight.x + 1 > en.x && rayCheckerUpRight.y < en.y + 128 && rayCheckerUpRight.y + 1 > en.y) {
								timesRayTouchedOtherEnemy++;
								touchedEUR++;
							}
							if (touchedEC >= 64) {
								raysThroughEnemies++;
								touchedEC = -256;
							}
							if (touchedEDL >= 64) {
								raysThroughEnemies++;
								touchedEDL = -256;
							}
							if (touchedEDR >= 64) {
								raysThroughEnemies++;
								touchedEDR = -256;
							}
							if (touchedEUL >= 64) {
								raysThroughEnemies++;
								touchedEUL = -256;
							}
							if (touchedEUR >= 64) {
								raysThroughEnemies++;
								touchedEUR = -256;
							}
							if ((timesRayTouchedOtherEnemy >= 128 || raysThroughEnemies >= 3) && range >= distanceAux)
								return en;

						}
					}
				}
				return e;
			}
		}
		return null;

	}

	public static Entity clickAndRayCasting(float fromX, float fromY, Entity entityToIgnore,
									 ArrayList<Enemy> enemyList, ArrayList<Wall> wallList, float range){
		Vector3 utilVector = click();
		float toX = utilVector.x;
		float toY = utilVector.y;

		return rayCasting(fromX,fromY,toX,toY,entityToIgnore,enemyList,wallList,range);
	}


}
