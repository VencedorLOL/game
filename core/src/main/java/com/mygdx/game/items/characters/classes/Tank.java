package com.mygdx.game.items.characters.classes;

import com.mygdx.game.items.Actor;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.OnVariousScenarios;
import com.mygdx.game.items.characters.CharacterClasses;

import static com.mygdx.game.Settings.print;
import static com.mygdx.game.items.OnVariousScenarios.destroyListener;

public class Tank extends CharacterClasses {
	public static String name = "Tank";
	public static float health = 80;
	public static float damage = 5;
	public static byte speed = 2;
	public static byte attackSpeed = 3;
	public static float defense = 0;
	public static int range = 2;
	public static float tempDefense = 0;
	public static float rainbowDefense = 0;
	public static float mana = 0;
	public static float magicDefense = 0;
	public static float magicDamage = 0;
	public static float manaPerTurn = 0;
	public static float manaPerUse = 0;
	public static float magicHealing = 0;
	public static float aggro;

	public Tank(){
		super(name,health,damage,speed,attackSpeed,defense,range,tempDefense,rainbowDefense,mana,magicDefense,
				magicDamage,manaPerTurn,manaPerUse,magicHealing, aggro);
	}

	OnVariousScenarios oVE = new OnVariousScenarios() {
			public void onDamagedActor(Actor damagedActor) {
				if (damagedActor.team == character.team && character.classes.name == "Tank") {
					damagedActor.damageRecieved *= 0.2f;
					damage(damagedActor.damageRecieved * 4); //this is exactly 80% of the damage
				}
			}
		};

	@Override
	protected void destroyOverridable() {
		destroyListener(oVE);
	}
}

