package com.mygdx.game.items.characters.equipment;

import com.mygdx.game.items.characters.equipment.items.Backpack;

import java.lang.reflect.InvocationTargetException;

public abstract class Items {
	public short space;


	public Items(short space){
		this.space = space;
	}







	public enum ItemTypes{
		BACKPACK("Backpack", Backpack.class);

		public Items createItem(){
			try {
				return (Items) classs.getConstructor().newInstance();
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
					 InstantiationException e) {
				throw new RuntimeException("coudnt get like a class or smth here details prob caused cos a typo or like i made a constructor with more than 0 arguments for an item but like they cant so yeah check that out its probably that: ",e);
			}
		}

		public Class<?> getClasss() {
			return classs;
		}

		public String getName() {
			return string;
		}

		final String string;
		final Class<?> classs;
		ItemTypes(String string, Class<?> classs){
			this.string = string;
			this.classs = classs;
		}

	}



}
