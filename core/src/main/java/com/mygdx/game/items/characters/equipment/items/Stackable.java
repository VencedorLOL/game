package com.mygdx.game.items.characters.equipment.items;

import com.mygdx.game.items.characters.ActiveInventory;
import com.mygdx.game.items.characters.equipment.Items;

import static com.mygdx.game.items.characters.Inventory.globalInventory;

public abstract class Stackable extends Items {
	public final static int DEFAULT_MAX_STACK_SZ = 10;
	final int MAX_STACK_SZ;
	int stack;


	public Stackable(short space, int maxStackSize) {
		super(space);
		MAX_STACK_SZ = maxStackSize;

	}

	public Stackable(short space) {
		super(space);
		MAX_STACK_SZ = DEFAULT_MAX_STACK_SZ;

	}


	public void add(int stack){
		setStack(this.stack + stack);
	}

	public void remove(int stack){
		setStack(this.stack - stack);
	}

	public void setStack(int stack){
		if(stack <= MAX_STACK_SZ)
			this.stack = stack;
		else {
			int restOfStack = stack + this.stack - MAX_STACK_SZ;
			this.stack = MAX_STACK_SZ;
			while(restOfStack > 0){
				if(restOfStack > MAX_STACK_SZ) {
					Stackable clone = this.clone();
					clone.setStack(MAX_STACK_SZ);
					globalInventory.addItem(clone);
					restOfStack -= MAX_STACK_SZ;
					continue;
				}
				Stackable clone = this.clone();
				clone.setStack(restOfStack);
				globalInventory.addItem(clone);
				restOfStack -= restOfStack;

			}
		}
		if(this.stack < 0) {
			ActiveInventory.DifferentInventories d = ActiveInventory.DifferentInventories.belongsInAnActive(this);
			if(d != null)
				d.inventory.removeItem(this);
			globalInventory.removeItem(this);
		}
	}

	public int getStack(){
		if(stack > MAX_STACK_SZ)
			stack = MAX_STACK_SZ;
		return stack;
	}

	public abstract Stackable clone();

}
