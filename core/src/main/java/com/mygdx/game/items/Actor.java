package com.mygdx.game.items;

public class Actor extends Entity{

	public Actor(String aChar, float x, float y, float base, float height) {
		super(aChar,x,y,base,height);
	}

	public Actor(){super();}

	public void damage(float damage, String damageReason){
	}

	//Override
	public boolean isPermittedToAct(){return true;}
	//Override
	public void permitToMove(){}


}
