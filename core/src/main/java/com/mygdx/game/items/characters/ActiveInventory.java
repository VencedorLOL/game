package com.mygdx.game.items.characters;

import com.mygdx.game.items.characters.equipment.Items;

import java.util.ArrayList;

public class ActiveInventory {

	public short totalCapacity;
	public short defaultCapacity;
	public short addedCapacity;
	public short currentCapacity;

	public ArrayList<Items> items = new ArrayList<>();

	public ActiveInventory(){}

	public void calculateCapacity(){
		currentCapacity = 0;
		for(Items i : items)
			currentCapacity += i.space;
		short counter = 10;
		totalCapacity = (short) (defaultCapacity + addedCapacity);
		while(currentCapacity > totalCapacity && counter > 0) {
			int num = items.size();
			removeItem(items.get(items.size() - 1));
			if(num==items.size())
				counter--;
		}
	}

	private void calculateAddedCapacity(){
		for(Items i : items){
		// some code to detect equipped inventory-enhancing items
		}

	}

	public boolean addItem(Items item){
		if(item.space + currentCapacity < totalCapacity) {
			items.add(item);
			calculateCapacity();
			return true;
		}
		return false;
	}

	public short removeItem(Items item){
		short space = items.remove(item) ? item.space : 0;
		currentCapacity -= space;
		return space;
	}



}
