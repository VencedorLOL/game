package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.util.HashMap;
import java.util.Map;

public class InputHandler implements KeyListener, ActionListener {

	static Input up = new Input();
	static Input down = new Input();
	static Input left = new Input();
	static Input right = new Input();
	static Input attackMode = new Input();
	static Input actionConfirm = new Input();
	static Input actionReset = new Input();
	static Input escape = new Input();
	static KeyEvent keyPress;

	static ArrayList<Input> keys = new ArrayList<>();

	static {
		keys.add(up);
		keys.add(down);
		keys.add(left);
		keys.add(right);
		keys.add(attackMode);
		keys.add(actionConfirm);
		keys.add(actionReset);
		keys.add(escape);
	}

	public static void resetter(){
		justPressedKeyID = -1;
		for (Input i : keys)
			i.setUsed(false);


	}


	public static String storedKeybind = "none";
	// i could have made this much better
	//FIXME change this string argument for an imput argument and get that ugly swich out of here
	public static boolean setKeybind(String keybind) {
		if (storedKeybind == "none" && justPressedKeyID != -1) {
			switch (keybind) {
				// I could do refraction but, ehhhhh, i think i'll pass. super prone to errors and this code only runs on a keybind set,
				// not as if it'd run always
				case "up": {up.setKey(justPressedKeyID); return true;}
				case "down": {down.setKey(justPressedKeyID); return true;}
				case "left": {left.setKey(justPressedKeyID); return true;}
				case "right": {right.setKey(justPressedKeyID); return true;}
				case "attackMode": {attackMode.setKey(justPressedKeyID); return true;}
				case "actionConfirm": {actionConfirm.setKey(justPressedKeyID); return true;}
				case "actionReset": {actionReset.setKey(justPressedKeyID); return true;}
				case "escape": {escape.setKey(justPressedKeyID); return true;}
				default: {storedKeybind = keybind; return false;}
			}
		}
		else if (justPressedKeyID != -1){
			switch (storedKeybind) {
				// disk space-wise I could have handled this differently. doesn't matter.
				case "up": {up.setKey(justPressedKeyID); storedKeybind = "none"; return true;}
				case "down": {down.setKey(justPressedKeyID); storedKeybind = "none"; return true;}
				case "left": {left.setKey(justPressedKeyID); storedKeybind = "none"; return true;}
				case "right": {right.setKey(justPressedKeyID); storedKeybind = "none"; return true;}
				case "attackMode": {attackMode.setKey(justPressedKeyID); storedKeybind = "none"; return true;}
				case "actionConfirm": {actionConfirm.setKey(justPressedKeyID); storedKeybind = "none"; return true;}
				case "actionReset": {actionReset.setKey(justPressedKeyID); storedKeybind = "none"; return true;}
				case "escape": {escape.setKey(justPressedKeyID); storedKeybind = "none"; return true;}
				default: {return false;}
			}
		}
		return false;
	}

	public static void defaultKeybinds(){
		up.setKey(87);
		down.setKey(83);
		left.setKey(65);
		right.setKey(68);
		attackMode.setKey(86);
		actionConfirm.setKey(32);
		actionReset.setKey(82);
		escape.setKey(27);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public static int justPressedKeyID;
	@Override
	public void keyPressed(KeyEvent e) {
		justPressedKeyID = e.getID();
		for (Input i : keys)
			if (justPressedKeyID == i.getKey()) {
				i.setUsed(true);
				break;
			}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}


	static class Input{
		boolean used;
		private int keyChar;

		private void setKey(int key){keyChar = key;}
		private int getKey(){return keyChar;}
		private void setUsed(boolean used){this.used = used;}
		private boolean wasUsed(){return used;}
	}

}
