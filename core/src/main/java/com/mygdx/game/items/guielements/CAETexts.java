package com.mygdx.game.items.guielements;

/**
 * Stands for: "Class And Equipment Texts", but it was too long to be convenient
 */
public class CAETexts {




	public enum Classes{
		MELEE("A class that focuses on close-combat and shredding your enemies hitting hard!" +
				"\nDefault stats:" +
				"\n HP: 40 - Damage: 40" +
				"\n Movement: 1.5 (3) Speed: 2" +
				"\n Range: 2"),




		;

		public final String text;
		Classes(String text){
			this.text = text;
		}



	}




}
