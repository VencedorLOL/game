package com.mygdx.game.items.textboxelements.textboxes.tests;

import com.mygdx.game.items.textboxelements.Textbox;

public class Test1 extends Textbox {


	public Test1() {
		super();
		storedText = "abcdefghijklmnñopqrstuvwxyzAB\nCDEFGHIJKLMNÑOPQRSTUVWXYZ\n0123456789 ºª¡!¿?\\/@\"'";
	}

	public void onRemoval() {
		new TestHelper();
	}

	public static class TestHelper extends Textbox{

		public TestHelper() {
			super();
			storedText = "#$%&()=[]{}-+*<>.:,;ü";
		}

	}


}
