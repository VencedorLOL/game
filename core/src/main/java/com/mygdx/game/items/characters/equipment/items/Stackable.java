package com.mygdx.game.items.characters.equipment.items;

import com.mygdx.game.items.characters.equipment.Items;

public abstract class Stackable extends Items {
	public final static int MAX_STACK_SZ = 10;
	int stack;


	public Stackable(short space) {
		super(space);
	}


	public void setStack(short stack){
		if(stack + this.stack < MAX_STACK_SZ)
			this.stack = stack;
		else {
			this.stack = MAX_STACK_SZ;
			throw new Error("like i made nothing yet to like distribute the items amongst diverse stacks if it reaches max stack size or something idk i dont have\n" +
					"anything planned out about what to do whenever an item reaches max stack size so like deal with this and dont surpass " + MAX_STACK_SZ + " items. either that or like" +
					"\nimplement an actual solution");
		}
	}

	public int getStack(){
		if(stack > MAX_STACK_SZ)
			stack = MAX_STACK_SZ;
		return stack;
	}

}
