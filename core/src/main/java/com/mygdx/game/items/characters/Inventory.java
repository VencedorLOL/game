package com.mygdx.game.items.characters;

import com.mygdx.game.items.characters.equipment.Items;
import com.mygdx.game.items.characters.equipment.items.Stackable;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mygdx.game.GameScreen.savefile;
import static com.mygdx.game.Settings.printErr;
import static com.mygdx.game.Utils.indexForwardClosestToTarget;
@SuppressWarnings("all")
public class Inventory {


	ArrayList<Items> items = new ArrayList<>();



	public void addToActive(ActiveInventory active, Items item){
		if(!removeItem(item))
			return;
		if(!active.addItem(item))
			addItem(item);
	}

	public void removeFromActive(ActiveInventory active, Items item) {
		if(active.removeItem(item) != 0)
			addItem(item);
	}

	public boolean removeItem(Items item){
		return items.remove(item);
	}

	public boolean addItem(Items item){
		return items.add(item);
	}


	public static final String NAME = "Name: ";
	public static final String OWNER = "Equip: ";
	public static final String SIZE = "Size: ";

	public static final String OWNERLESS = "None";
	public static final String OWNER_ANIMA = "Anima";

	public static final String FLAG_NAME = "Inventory";

	public void restoreState() {
		String state = savefile.getFlag(FLAG_NAME).substring(FLAG_NAME.length()+2);
		if (state == null)
			return;
		String[] items = state.split(";");
		String[][] itemsAndProperties = new String[items.length][];
		for (int i = 0; i < items.length; i++)
			itemsAndProperties[i] = items[i].split(",");

		for (String[] s : itemsAndProperties) {
			Items item = null;
			for (String ss : s) {
				if (ss.indexOf(NAME) == 0) {
					for (Items.ItemTypes i : Items.ItemTypes.values())
						if (i.getName().equals(ss.substring(NAME.length()))) {
							item = i.createItem();
							break;
						}
					break;
				}
			} if (item != null)
				for (String ss : s) {
					if (ss.indexOf(OWNER) == 0) {
						switch (ss.substring(OWNER.length())) {
							case (OWNERLESS):
								addItem(item);
								break;
							case OWNER_ANIMA:
								printErr("This shoudda add an item to anima bug like uhh i didnt make that code yet so instead you get like this and such");
								addItem(item);
								break;
							default:
								addItem(item);
								break;
						}
					}
					if (ss.indexOf(SIZE) == 0) {
						if (!(item instanceof Stackable)) {
							printErr("wtf AT:[Inventory.restoreState()]");
							continue;
						}
						((Stackable) item).setStack(Short.parseShort(ss.substring(SIZE.length())));
					}
				} else printErr("Located non-existing item whose data is: " + Arrays.toString(s));
		}
	}

	public void storeState(){
		StringBuilder finalData = new StringBuilder();
		for(Items i : items) {
			finalData.append(";");
			for (Items.ItemTypes it : Items.ItemTypes.values())
				if(i.getClass() == it.getClasss()){
					finalData.append(NAME).append(it.getName());
					break;
				}
			finalData.append(",");
			//TODO: actually code this so the condition for it being inside Inventory or an ActiveInventory is detected so it can be assigned different values ("None" or "Anima")
			if(true){
				finalData.append(OWNER);
				if(true)
					finalData.append(OWNERLESS);
				finalData.append(",");
			}
			if(i instanceof Stackable)
				finalData.append(SIZE).append(((Stackable)i).getStack()).append(",");
		}

		finalData.deleteCharAt(indexForwardClosestToTarget(';',finalData.toString(),0));
		savefile.setFlag(FLAG_NAME, finalData.toString());
	}




}







