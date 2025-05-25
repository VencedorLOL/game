package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.Settings;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.Settings.*;

public class InputHandler {

	static Input up = new Input();
	static Input down = new Input();
	static Input left = new Input();
	static Input right = new Input();
	static Input attackMode = new Input();
	static Input actionConfirm = new Input();
	static Input actionReset = new Input();
	static Input escape = new Input();
	static KeyEvent keyPress;

//	static VFrame frame = new VFrame();


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
		keyPressed();
//		frame.initialize();
	}

	public static void resetter(){
		/*if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.G)){
			System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager());
			Method[] field = KeyboardFocusManager.getCurrentKeyboardFocusManager().getClass().getDeclaredMethods();

			for (Method f : field){
				System.out.println(f.getName());
				if (f.getName() == "getKeyEventDispatchers"){
					print("got merthod");
					f.setAccessible(true);
					try {
						Object o = f.invoke(f.getName());

						if (o instanceof List){
							print("size: "  + ((List<?>) o).size());

							for (int i = 0; i < ((List<?>) o).size(); i++){
								print("KeyEventDispatcher " + ((List<?>) o).get(i));


							}
						}

					} catch (InvocationTargetException | IllegalAccessException ignored){print("failed");}
				}
			}

			System.out.println();
		}
		justPressedKeyID = -1;
		for (Input i : keys)
			i.setUsed(false);
		for (Input i : keys)
			i.setReleased(false);
		if (!isOverridingEscAllowed() && escape.keyChar != 27)
			escape.setKey(27);
	*/}

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


	public static boolean isUpPressed() {return up.wasUsed();}
	public static boolean isDownPressed() {return down.wasUsed();}
	public static boolean isLeftPressed() {return left.wasUsed();}
	public static boolean isRightPressed() {return right.wasUsed();}
	public static boolean isAttackModePressed() {return attackMode.wasUsed();}
	public static boolean isActionConfirmPressed() {return actionConfirm.wasUsed();}
	public static boolean isActionResetPressed() {return actionReset.wasUsed();}
	public static boolean isEscapePressed() {return escape.wasUsed();}

	public static boolean isUpReleased() {return up.wasReleased();}
	public static boolean isDownReleased() {return down.wasReleased();}
	public static boolean isLeftReleased() {return left.wasReleased();}
	public static boolean isRightReleased() {return right.wasReleased();}
	public static boolean isAttackModeReleased() {return attackMode.wasReleased();}
	public static boolean isActionConfirmReleased() {return actionConfirm.wasReleased();}
	public static boolean isActionResetReleased() {return actionReset.wasReleased();}
	public static boolean isEscapeReleased() {return escape.wasReleased();}



	// Just pass in the current keybind number, then run once a frame until returns true. or cancel whenever.
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
		up.setKey(87);
		down.setKey(83);
		left.setKey(65);
		right.setKey(68);
		attackMode.setKey(86);
		actionConfirm.setKey(32);
		actionReset.setKey(82);
		escape.setKey(27);
	}

	public static int justPressedKeyID;
	static public void keyPressed() {


		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ke -> {
			synchronized (InputHandler.class) {
				switch (ke.getID()) {
					case KeyEvent.KEY_PRESSED: {
						if (ke.getKeyCode() == KeyEvent.VK_W) {
							up.setUsed(true);
							System.out.println("up!!!!!!!!!!!!");
						}
						if (ke.getKeyCode() == down.getKey()) {
							down.setUsed(true);
						}
						if (ke.getKeyCode() == left.getKey()) {
							left.setUsed(true);
						}
						if (ke.getKeyCode() == right.getKey()) {
							right.setUsed(true);
						}


						break;
					}
					case KeyEvent.KEY_RELEASED:
						if (ke.getKeyCode() == up.getKey()) {
							up.setReleased(true);
						}
						break;
				}
				return false;
		}});


		/*justPressedKeyID = e.getID();
		for (Input i : keys)
			if (justPressedKeyID == i.getKey()) {
				i.setUsed(true);
				break;
			}
		*/
	}


	public static class Input{
		boolean used;
		boolean released;
		private int keyChar;

		private void setKey(int key){keyChar = key;}
		private int getKey(){return keyChar;}
		private void setUsed(boolean used){this.used = used;}
		private boolean wasUsed(){return used;}
		private boolean wasReleased(){return released;}
		private void setReleased(boolean released){this.released = released;}
	}

/*	public static class VFrame extends Frame implements KeyListener {

		private Label displayLabel;

		public void initialize(){

			setTitle("Typed Text Display");
			setSize(400, 200);
			setLayout(new FlowLayout());

			TextField textField = new TextField(20);
			textField.addKeyListener(frame);
			frame.add(textField);

			displayLabel = new Label("Typed Text: ");
			add(displayLabel);

			setFocusable(true);
			setFocusTraversalKeysEnabled(false);

			setVisible(true);
		}



		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			Settings.print("key detected");
			InputHandler.keyPressed();
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}*/

}
