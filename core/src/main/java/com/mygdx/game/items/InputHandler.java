package com.mygdx.game.items;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;

import static com.mygdx.game.Settings.*;
import static java.lang.Math.floor;

@SuppressWarnings("all")
public class InputHandler implements InputProcessor {

	static Input up 			= new Input();
	static Input down 			= new Input();
	static Input left 			= new Input();
	static Input right 			= new Input();
	static Input attackMode 	= new Input();
	static Input actionConfirm 	= new Input();
	static Input actionReset 	= new Input();
	static Input escape 		= new Input();
	static Input abilityKey1 	= new Input();
	static Input abilityKey2 	= new Input();
	static Cursor leftClick		= new Cursor();
	static Cursor rightClick	= new Cursor();

	static float x,y;
	static boolean dragged = false;
	static boolean moved = false;

	static ArrayList<Input> keys = new ArrayList<>();
	static ArrayList<Cursor> cursor = new ArrayList<>();

	static ArrayList<String> keysPressed = new ArrayList<>();

	static {
		keys.add(up);
		keys.add(down);
		keys.add(left);
		keys.add(right);
		keys.add(attackMode);
		keys.add(actionConfirm);
		keys.add(actionReset);
		keys.add(escape);
		keys.add(abilityKey1);
		keys.add(abilityKey2);

		cursor.add(leftClick);
		cursor.add(rightClick);
	}

	public static void resetter(){
		keysPressed.clear();
		dragged = false;
		moved = false;
		for(Input i : keys){
			if(i.released)
				i.resetRelease();
			if(i.wasUsed() && !i.getGettingUsed())
				i.gettingUsed();
			if(i.buffer > 0 && i.wasUsed())
				i.buffer--;
			}
		for(Cursor c : cursor){
			if(c.released)
				c.resetRelease();
			if(c.wasUsed() && !c.getGettingUsed())
				c.gettingUsed();
			if(c.buffer > 0 && c.wasUsed())
				c.buffer--;
		}
		thisTickCounter = -1;

	}


	public static boolean isActionPressed(byte action){
		switch (action){
			case 0: {return up.wasUsed();}
			case 1: {return down.wasUsed();}
			case 2: {return left.wasUsed();}
			case 3: {return right.wasUsed();}
			case 4: {return attackMode.wasUsed();}
			case 5: {return actionConfirm.wasUsed();}
			case 6: {return actionReset.wasUsed();}
			case 7: {return escape.wasUsed();}
			default: {return false;}
		}
	}
	public static boolean wasReleased(byte action){
		switch (action){
			case 0: {return up.wasReleased();}
			case 1: {return down.wasReleased();}
			case 2: {return left.wasReleased();}
			case 3: {return right.wasReleased();}
			case 4: {return attackMode.wasReleased();}
			case 5: {return actionConfirm.wasReleased();}
			case 6: {return actionReset.wasReleased();}
			case 7: {return escape.wasReleased();}
			default: {return false;}
		}
	}


	public static boolean upPressed() {return up.wasUsed();}
	public static boolean downPressed() {return down.wasUsed();}
	public static boolean leftPressed() {return left.wasUsed();}
	public static boolean rightPressed() {return right.wasUsed();}
	public static boolean attackModePressed() {return attackMode.wasUsed();}
	public static boolean actionConfirmPressed() {return actionConfirm.wasUsed();}
	public static boolean actionResetPressed() {return actionReset.wasUsed();}
	public static boolean escapePressed() {return escape.wasUsed();}
	public static boolean leftClickPressed(){return leftClick.wasUsed();}
	public static boolean rightClickPressed(){return rightClick.wasUsed();}

	public static boolean upJustPressed() {return up.wasUsed() && !up.getGettingUsed();}
	public static boolean downJustPressed() {return down.wasUsed() && !down.getGettingUsed(); }
	public static boolean leftJustPressed() {return left.wasUsed() && !left.getGettingUsed();}
	public static boolean rightJustPressed() {return right.wasUsed() && !right.getGettingUsed();}
	public static boolean attackModeJustPressed() {return attackMode.wasUsed() && !attackMode.getGettingUsed();}
	public static boolean actionConfirmJustPressed() {return actionConfirm.wasUsed() && !actionConfirm.getGettingUsed();}
	public static boolean actionResetJustPressed() {return actionReset.wasUsed() && !actionReset.getGettingUsed();}
	public static boolean escapeJustPressed() {return escape.wasUsed() && !escape.getGettingUsed();}
	public static boolean ability1JustPressed() {return abilityKey1.wasUsed() && !abilityKey1.getGettingUsed();}
	public static boolean ability2JustPressed() {return abilityKey2.wasUsed() && !abilityKey2.getGettingUsed();}
	public static boolean leftClickJustPressed() {return leftClick.wasUsed() && !leftClick.getGettingUsed();}
	public static boolean rightClickJustPressed() {return rightClick.wasUsed() && !rightClick.getGettingUsed();}

	public static boolean upReleased() {return up.wasReleased();}
	public static boolean downReleased() {return down.wasReleased();}
	public static boolean leftReleased() {return left.wasReleased();}
	public static boolean rightReleased() {return right.wasReleased();}
	public static boolean attackModeReleased() {return attackMode.wasReleased();}
	public static boolean actionConfirmReleased() {return actionConfirm.wasReleased();}
	public static boolean actionResetReleased() {return actionReset.wasReleased();}
	public static boolean escapeReleased() {return escape.wasReleased();}
	public static boolean ability1Released() {return abilityKey1.wasReleased();}
	public static boolean ability2Released() {return abilityKey2.wasReleased();}
	public static boolean leftClickReleased() {return leftClick.wasReleased();}
	public static boolean rightClickReleased() {return rightClick.wasReleased();}

	public static void resetAttackMode(){attackMode.release(); attackMode.resetRelease();}

	public static float[] cursorCoordinates(){return new float[] {x, y};}
	public static float cursorX(){return x;}
	public static float cursorY(){return y;}
	public static boolean cursorDragged(){return dragged;}
	public static boolean cursorMoved(){return moved;}

	public static ArrayList<String> getKeysPressed(){return keysPressed;}

	static byte thisTickCounter = -1;
	public static byte directionalBuffer(){
		if(thisTickCounter == -1) {
			byte counter = 0;
			if 		(((up.buffer == 0 && up.wasUsed()) || (up.wasReleased() && up.buffer != -2)) ||
					((down.buffer == 0 && down.wasUsed()) || (down.wasReleased() && down.buffer != -2)) ||
					((right.buffer == 0 && right.wasUsed())) || (right.wasReleased() && right.buffer != -2) ||
					((left.buffer == 0 && left.wasUsed()) || (left.wasReleased() && left.buffer != -2)))
			{
				if (right.wasUsed() || right.wasReleased()) {
					counter += 1;
					right.buffer = -2;
				}
				if (down.wasUsed() || down.wasReleased()) {
					counter += 2;
					down.buffer = -2;
				}
				if (up.wasUsed() || up.wasReleased()) {
					counter += 4;
					up.buffer = -2;
				}
				if (left.wasUsed() || left.wasReleased()) {
					counter += 8;
					left.buffer = -2;
				}
			}
			thisTickCounter = counter;
			return counter;
		} else return thisTickCounter;
	}


	// Just pass in the current keybind number, then run once a frame until returns true. or cancel whenever.
	static int justPressedKeyID;
	public static boolean setKeybind (int keybind) {
		if (justPressedKeyID != -1) {
			if (keybind == escape.keyChar && !isOverridingEscAllowed()) {
				printErr("As a safety feature, should the escape action have other key attached to it, the option \"Override escape key\" must be activated in settings.");
				return false; }

			for (Input i : keys)
				if (keybind == i.keyChar) {
					i.setKey(justPressedKeyID);
					return true;
				}
		}
		print("Coudln't set the desired keybind, number: " + keybind + ", probably because no target key was pressed.");
		return false;
	}

	public static void defaultKeybinds(){
		up.setKey(51);
		down.setKey(47);
		left.setKey(29);
		right.setKey(32);
		attackMode.setKey(59);
		actionConfirm.setKey(62);
		actionReset.setKey(46);
		escape.setKey(111);
		abilityKey1.setKey(30);
		abilityKey2.setKey(36);

		leftClick.setKey(0);
		rightClick.setKey(1);
	}

	@Override
	public boolean keyDown(int keycode) {
		for (Input i : keys){
			if(i.getKey() == keycode)
				i.press();
		}
		justPressedKeyID = keycode;
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		keysPressed.add(keycode+"");
		for (Input i : keys){
			if(i.getKey() == keycode)
				i.release();
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (Cursor c : cursor)
			if (c.getKey() == button)
				c.press();
		x = screenX;
		y = screenY;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (Cursor c : cursor)
			if (c.getKey() == button)
				c.release();
		x = screenX;
		y = screenY;
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		x = screenX;
		y = screenY;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		x = screenX;
		y = screenY;
		dragged = true;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		x = screenX;
		y = screenY;
		moved = true;
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}


	public static class Input{
		boolean used;
		boolean released;
		boolean gettingUsed;
		int buffer = 10;
		private int keyChar;

		private void setKey(int key){keyChar = key;}
		private int getKey(){return keyChar;}

		private void press(){used = true;}
		private boolean wasUsed(){return used;}

		private void release(){released = true; used = false; gettingUsed = false; }
		private boolean wasReleased(){return released;}
		private void resetRelease(){released = false;buffer = 10;}

		public boolean getGettingUsed(){return gettingUsed;}
		private void gettingUsed(){gettingUsed = true;}
	}

	public static class Cursor{
		boolean used;
		boolean released;
		boolean gettingUsed;
		int buffer = 10;
		private int keyChar;


		private void setKey(int key){keyChar = key;}
		private int getKey(){return keyChar;}

		private void press(){used = true;}
		private boolean wasUsed(){return used;}

		private void release(){released = true; used = false; gettingUsed = false; }
		private boolean wasReleased(){return released;}
		private void resetRelease(){released = false;buffer = 10;}

		public boolean getGettingUsed(){return gettingUsed;}
		private void gettingUsed(){gettingUsed = true;}

	}


}
