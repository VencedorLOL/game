package com.mygdx.game;

import com.mygdx.game.items.ClassAndEquipmentChanger;

public class GlobalVariables {
	public static ClassAndEquipmentChanger.ClassObject[] classSlots;

	static{
		classSlots  = new ClassAndEquipmentChanger.ClassObject[3];
		for(int i = 0; i < classSlots.length; i++){
			if(classSlots[i] == null){
				if(!hasMelee())
					classSlots[i] = new ClassAndEquipmentChanger.Melee();
				else if(!hasSpeedster())
					classSlots[i] = new ClassAndEquipmentChanger.Speedster();
				else if(!hasHealer())
					classSlots[i] = new ClassAndEquipmentChanger.Healer();
				else if(!hasTank())
					classSlots[i] = new ClassAndEquipmentChanger.Tank();
				else if(!hasMage())
					classSlots[i] = new ClassAndEquipmentChanger.Mage();
			}
		}
	}

	private static boolean hasMelee(){
		for(ClassAndEquipmentChanger.ClassObject o : classSlots)
			if(o instanceof ClassAndEquipmentChanger.Melee)
				return true;
		return false;
	}

	private static boolean hasSpeedster(){
		for(ClassAndEquipmentChanger.ClassObject o : classSlots)
			if(o instanceof ClassAndEquipmentChanger.Speedster)
				return true;
		return false;
	}

	private static boolean hasHealer(){
		for(ClassAndEquipmentChanger.ClassObject o : classSlots)
			if(o instanceof ClassAndEquipmentChanger.Healer)
				return true;
		return false;
	}

	private static boolean hasTank(){
		for(ClassAndEquipmentChanger.ClassObject o : classSlots)
			if(o instanceof ClassAndEquipmentChanger.Tank)
				return true;
		return false;
	}


	private static boolean hasMage(){
		for(ClassAndEquipmentChanger.ClassObject o : classSlots)
			if(o instanceof ClassAndEquipmentChanger.Mage)
				return true;
		return false;
	}
}
