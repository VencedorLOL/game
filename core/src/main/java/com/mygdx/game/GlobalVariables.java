package com.mygdx.game;

import com.mygdx.game.items.ClassChanger;

public class GlobalVariables {
	public static ClassChanger.ClassObject[] classSlots;

	static{
		classSlots  = new ClassChanger.ClassObject[3];
		for(int i = 0; i < classSlots.length; i++){
			if(classSlots[i] == null){
				if(!hasMelee())
					classSlots[i] = new ClassChanger.Melee();
				else if(!hasSpeedster())
					classSlots[i] = new ClassChanger.Speedster();
				else if(!hasHealer())
					classSlots[i] = new ClassChanger.Healer();
				else if(!hasTank())
					classSlots[i] = new ClassChanger.Tank();
				else if(!hasMage())
					classSlots[i] = new ClassChanger.Mage();
			}
		}
	}

	private static boolean hasMelee(){
		for(ClassChanger.ClassObject o : classSlots)
			if(o instanceof ClassChanger.Melee)
				return true;
		return false;
	}

	private static boolean hasSpeedster(){
		for(ClassChanger.ClassObject o : classSlots)
			if(o instanceof ClassChanger.Speedster)
				return true;
		return false;
	}

	private static boolean hasHealer(){
		for(ClassChanger.ClassObject o : classSlots)
			if(o instanceof ClassChanger.Healer)
				return true;
		return false;
	}

	private static boolean hasTank(){
		for(ClassChanger.ClassObject o : classSlots)
			if(o instanceof ClassChanger.Tank)
				return true;
		return false;
	}


	private static boolean hasMage(){
		for(ClassChanger.ClassObject o : classSlots)
			if(o instanceof ClassChanger.Mage)
				return true;
		return false;
	}
}
