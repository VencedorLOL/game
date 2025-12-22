package com.mygdx.game.items.guielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.items.Character;
import com.mygdx.game.items.ClassAndEquipmentChanger;
import com.mygdx.game.items.GUI;

import java.util.Objects;

import static com.mygdx.game.GameScreen.getCamara;
import static com.mygdx.game.GlobalVariables.classSlots;
import static com.mygdx.game.Settings.globalSize;
import static com.mygdx.game.items.InputHandler.*;
import static com.mygdx.game.items.InputHandler.upJustPressed;
import static com.mygdx.game.items.TextureManager.animations;
import static com.mygdx.game.items.TextureManager.*;
import static java.lang.Math.*;

public class Background extends GUI {
	Character character;

	boolean renderr = false;
	float sizeX, sizeY;
	float spaceX, spaceY, endSX, endSY;
	String texture = "GUIBackground";


	CloseButton close;
	boolean delete = false;

	SelectionButton[] selButtons;
	float selGapX, selIniGapX, selGapY, selSize;
	float selGapXB, selIniGapXB, selGapYB, selSizeB;


	Slider slider;
	float sliGapX, sliGapY, sliThickness, sliWidth, sliHeight;
	float totalXSpace;

	ClassesCards[] classesCards;
	float cardsSize, cardsY, cardsIniGapX, cardsGapX;
	float cardSizeB, cardYB, cardIniGapXB;

	byte selectedOne = -1;
	byte lastBox = -1;
	byte lastCard = -1;
	byte lastMode = -1;
	byte elementHovered = -2;
	byte persevereHover = -1;


	SelectionButton[] modeSelector;
	float modeGapX, modeIniGapX, modeGapY, modeSize;
	byte modes = 0;

	SelectionButton meleeHolder, shieldHolder;
	float meleeGapX, shieldGapX, holderGapY, holderSize;

	ItemsList items;
	float itemsIniGapX, itemsGapX, itemsY, itemsSize;


	int counter;

	public Background(Character chara){
		super();
		character = chara;
		sizeX = Gdx.graphics.getWidth() /(globalSize()*1f);
		sizeY = Gdx.graphics.getHeight() /(globalSize()*.5625f);
		Vector3 realCoords = (new Vector3(Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f,Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f, 0));
		realCoords = getCamara().camara.unproject(realCoords);
		Animation animation = new Animation("guibackgroundopening",realCoords.x,realCoords.y){
			public void onFinish() {
				renderr = true;
			}
		};
		animation.scaleY = sizeY;
		animation.scaleX = sizeX;
		animation.opacity = 0.5f;
		animations.add(animation);

		close =  new CloseButton(){
			public void onTouchOverridable() {
				if(modes == 0) {
						sizeX = Gdx.graphics.getWidth() / (globalSize() * 1f);
						sizeY = Gdx.graphics.getHeight() / (globalSize() * .5625f);
						Animation animation = new Animation("guibackgroundclosing", chara);
						animation.scaleY = sizeY;
						animation.scaleX = sizeX;
						animation.opacity = 0.5f;
						animations.add(animation);
						renderr = false;
						delete(close);
						delete = true;
				}
				else if(modes > 0 ){
					counter = 20;
					if (!existsSelCard()) {
						modes = 0;
						selectedOne = -1;
					} else {
						cardDeselect();
						if(modes == 2)
							elementHovered = lastCard;
					}
				}
			}
		};

		meleeHolder = new SelectionButton();
		meleeHolder.secTexture = "WeaponSlot";
		shieldHolder = new SelectionButton();
		shieldHolder.secTexture = "ShieldSlot";

		modeSelector = new SelectionButton[]{new SelectionButton(){
			public void onTouchOverridable() {
				modes = 1;
				selectedOne = -2;
				counter = 20;
				slider.xCursor = 0;
				selected = false;
				hovered = false;
			}
		},new SelectionButton(){
			public void onTouchOverridable() {
				modes = 2;
				selectedOne = -2;
				counter = 20;
				slider.xCursor = 0;
				selected = false;
				hovered = false;
			}
		}};
		modeSelector[0].secTexture = "SwapClasses";
		modeSelector[1].secTexture = "EditClasses";

		selButtons = new SelectionButton[classSlots.length];

		for(int i = 0; i < selButtons.length; i++) {
			byte finalI = (byte) i;
			selButtons[i] = new SelectionButton(){
				public void onTouchOverridable() {
					deselect();
					selected = true;
					hovered = false;
					if(elementHovered == finalI)
						elementHovered = -2;
					selectedOne = finalI;
				}
			};
			selButtons[i].secTexture = classSlots[i].texture;
		}

		slider = new Slider(){
			public void onTouchOverridable() {
				elementHovered = -3;
			}
		};

		classesCards = new ClassesCards[ClassesCards.ClsCardObj.values().length];
		for (int i = 0; i < classesCards.length; i++)
			classesCards[i] = new ClassesCards(ClassesCards.ClsCardObj.values()[i]){public void onTouchOverridable() {cardFunctionalitySquared(this);}};


	}

	public void cardFunctionalitySquared(ClassesCards card){
		cardDeselect();
		card.selected = true;
		card.hovered = false;
		if(modes == 2)
			elementHovered = -1;
		cardFunctionality(card);
	}

	private int getSelCard(){
		for (int i = 0; i < classesCards.length; i++)
			if (classesCards[i] != null && classesCards[i].selected)
				return i;
		return -1;
	}

	private boolean existsSelCard(){
		for (ClassesCards c : classesCards)
			if (c != null && c.selected)
				return true;
		return false;
	}


	private void deselect(){
		for (SelectionButton s : selButtons)
			if (s != null)
				s.selected = false;
	}

	private void cardDeselect(){
		for(ClassesCards c : classesCards)
			if(c != null)
				c.selected = false;

	}

	private boolean existsSelBox(){
		for (SelectionButton s : selButtons)
			if (s != null && s.selected)
				return true;
		return false;
	}

	private void cardFunctionality(ClassesCards card){
		if(existsSelBox()) {
			int aid = -1;
			String aid2 = null;
			String target = null;
			ClassAndEquipmentChanger.ClassObject cls = null;
			for (int i = 0; i < selButtons.length; i++)
				if (selButtons[i] != null && selButtons[i].selected) {
					aid2 = selButtons[i].secTexture;
					cls = classSlots[i];
					classSlots[i] = card.classs;
					selButtons[i].secTexture = card.classs.texture;
					target = card.classs.texture;
					card.selected = false;
					selButtons[i].selected = false;
					aid = i;
					break;
				}
			for (int i = 0; i < selButtons.length; i++)
				if (i != aid && Objects.equals(selButtons[i].secTexture, target)) {
					selButtons[i].secTexture = aid2;
					classSlots[i] = cls;
				}
		}
	}

	public void render(){
		if(renderr) {
			calculateMath();
			fixatedDrawables.add(new DrawableObject(texture, Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f, Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f , 0.5f, 0, sizeX, sizeY));
			hoverCheck();

			counter -= counter > 0 ? 1 : 0;
			if (modes == 0){
				for (int i = 0; i < modeSelector.length; i++) {
					modeSelector[i].render(modeSize, modeIniGapX + i * (modeGapX + modeSize * 32), modeGapY,counter <= 0);
				}
			} else if (modes == 1) {
				for (int i = 0; i < selButtons.length; i++) {
					selButtons[i].secTexture = classSlots[i].texture;
					if (!existsSelCard())
						selButtons[i].render(selSize, selIniGapX + i * (selGapX + selSize * 32), selGapY,true);
					else
						selButtons[i].render(selSizeB, selIniGapXB + i * (selGapXB + selSizeB * 32), selGapYB,true);
				}

				if (!existsSelCard())
					slider.render(sliGapX, sliGapY, sliWidth, sliHeight, sliThickness, totalXSpace);

				if (existsSelCard() && getSelCard() != -1)
					classesCards[getSelCard()].render(cardSizeB / 32, cardIniGapXB, cardYB,false);
				else
					for (int i = 0; i < classesCards.length; i++)
						classesCards[i].render(cardsSize / 32, cardsIniGapX + i * (cardsGapX + cardsSize) - slider.xCursor * totalXSpace / slider.realWidth, cardsY, counter <= 0);

				if (existsSelCard() && existsSelBox()) {
					cardFunctionality(classesCards[getSelCard()]);
				}
			} else if (modes == 2){
				if (!existsSelCard())
					slider.render(sliGapX, sliGapY, sliWidth, sliHeight, sliThickness, totalXSpace);

				if (existsSelCard() && getSelCard() != -1)
					classesCards[getSelCard()].render(cardSizeB / 32, cardIniGapXB, cardYB,false);
				else
					for (int i = 0; i < classesCards.length; i++)
						classesCards[i].render(cardsSize / 32, cardsIniGapX + i * (cardsGapX + cardsSize) - slider.xCursor * totalXSpace / slider.realWidth, cardsY, counter <= 0);

				if(existsSelCard()){
					if(items == null){
						items = new ItemsList(classesCards[getSelCard()].classs,character);
					}
					meleeHolder.render(holderSize/32,meleeGapX,holderGapY,false);
					shieldHolder.render(holderSize/32,shieldGapX,holderGapY,false);
					items.render(itemsSize/32,itemsIniGapX,itemsGapX,itemsY,true);
				} else if(items != null)
					items = null;

				if (existsSelCard() && existsSelBox()) {
					cardFunctionality(classesCards[getSelCard()]);
				}
			}
			fixatedDrawables.add(new DrawableObject("GUIBackgroundBorder", Gdx.graphics.getWidth() - (sizeX * globalSize() + globalSize())/2f, Gdx.graphics.getHeight() - (sizeY * globalSize()*.5625f - globalSize())/2f , 1f, 0, sizeX, sizeY));
			close.render(endSY/globalSize(),endSX,endSY,counter <= 0);


			if(delete)
				delete(this);
		}
	}

	private void dehover(){
		slider.selected = false;
		close.hovered = false;
		for(SelectionButton s : selButtons)
			s.hovered = false;
		for(ClassesCards c : classesCards)
			c.hovered = false;
		if (items != null) items.canHover = false;
	}

	int counterR = 0, counterL = 0, counterU, counterD;
	final int[] times = new int[]{80, 60, 50, 40,30};
	int counterStateR = -1, counterStateL = -1, counterStateU, counterStateD;

	private void hoverCheck(){
		if(modes == 0){
			if(elementHovered == -1){
				if(downJustPressed()){
					elementHovered = lastMode != -1 ? lastMode : 0;
					modeSelector[0].hovered = elementHovered == 0;
					modeSelector[1].hovered = elementHovered != 0;

					close.hovered = false;
				}
			}
			else {
				if (rightJustPressed()) {
					elementHovered = 1;
					lastMode = 1;
					close.hovered = false;
					modeSelector[1].hovered = true;
					modeSelector[0].hovered = false;
				} else if (leftJustPressed()) {
					elementHovered = 0;
					lastMode = 1;
					close.hovered = false;
					modeSelector[0].hovered = true;
					modeSelector[1].hovered = false;
				}
				else if (upJustPressed()){
					lastMode = elementHovered;
					elementHovered = -1;
					modeSelector[1].hovered = false;
					modeSelector[0].hovered = false;
					close.hovered = true;
				}
			}

		}
		if(modes == 1) {
			if (elementHovered != -2) {
				if (upJustPressed()) {
					if (elementHovered > -1 && elementHovered < classSlots.length) {
						lastBox = elementHovered;
						elementHovered = -1;
					} else if (elementHovered >= classSlots.length) {
						lastCard = elementHovered;
						elementHovered = lastBox != -1 ? lastBox : 1;
					} else if (elementHovered == -3) {
						slider.selected = false;
						elementHovered = (byte) (getElement() + selButtons.length);
					}
					persevereHover = 1;
				}
				if (downJustPressed()) {
					if (elementHovered == -1) {
						elementHovered = lastBox != -1 ? lastBox : (byte) (classSlots.length - 1);
					} else if (elementHovered < classSlots.length && !existsSelCard() && elementHovered != -3) {
						lastBox = elementHovered;
						elementHovered = lastCard != -1 ? lastCard : (byte) classSlots.length;
					} else if (!existsSelCard() && elementHovered != -3) {
						lastCard = elementHovered;
						elementHovered = -3;
					}
					persevereHover = 1;
				}
				if (leftJustPressed()) {
					if (elementHovered > 0 && elementHovered < classSlots.length) {
						lastBox = --elementHovered;
					} else if (elementHovered > classSlots.length) {
						lastCard = --elementHovered;
						counterStateL = 0;
					}
					persevereHover = 1;
				} else if (leftPressed() && elementHovered > classSlots.length && counterStateL != -1) {
					if (counterL++ > times[counterStateL]) {
						lastCard = --elementHovered;
						counterStateL += counterStateL < times.length - 1 ? 1 : 0;
						counterL = 0;
					}
				} else {
					counterL = 0;
					counterStateL = -1;
				}
				if (rightJustPressed()) {
					if (elementHovered > -1 && elementHovered < classSlots.length - 1)
						lastBox = ++elementHovered;
					else if (elementHovered >= classSlots.length && elementHovered < (classesCards.length + classSlots.length - 1)) {
						lastCard = ++elementHovered;
						counterStateR = 0;
					}
					persevereHover = 1;
				} else if (rightPressed() && elementHovered >= classSlots.length && elementHovered < (classesCards.length + classSlots.length - 1) && counterStateR != -1) {
					if (counterR++ > times[counterStateR]) {
						lastCard = ++elementHovered;
						counterStateR += counterStateR < times.length - 1 ? 1 : 0;
						counterR = 0;
					}
				} else {
					counterR = 0;
					counterStateR = -1;
				}
			} else {
				if (upJustPressed()) {
					elementHovered = -1;
					persevereHover = 1;
				}
				if (downJustPressed()) {
					elementHovered = lastCard != -1 ? lastCard : (byte) classSlots.length;
					persevereHover = 1;
				}
				if (leftJustPressed()) {
					elementHovered = lastBox != -1 ? lastBox : (byte) (floor(classSlots.length / 2d) - 1);
					persevereHover = 1;
				}
				if (rightJustPressed()) {
					elementHovered = lastBox != -1 ? lastBox : (byte) ((classSlots.length / 2) + 1);
					persevereHover = 1;
				}
			}
			if (persevereHover != 1) {
				dehover();
			}
			if (cursorX() >= endSX && cursorX() <= endSX + endSY &&
					cursorY() >= 0 && cursorY() <= endSY) {
				elementHovered = -1;
				persevereHover = -1;
			}
			for (int i = 0; i < selButtons.length; i++)
				if (cursorX() >= selButtons[i].x && cursorX() <= selButtons[i].x + selButtons[i].size &&
						cursorY() >= selButtons[i].y - selButtons[i].size && cursorY() <= selButtons[i].y) {
					elementHovered = (byte) i;
					persevereHover = -1;
					break;
				}
			if (!existsSelCard())
				for (int i = 0; i < classesCards.length; i++) {
					if (cursorX() >= classesCards[i].x && cursorX()
							<= classesCards[i].x + cardsSize &&
							cursorY() >= classesCards[i].y - cardsSize && cursorY() <= classesCards[i].y && cursorMoved()) {
						dehover();
						elementHovered = (byte) (i + selButtons.length);
						persevereHover = 1;
						break;
					}
				}
			if (persevereHover != 0) {
				if (elementHovered == -1) {
					dehover();
					close.hovered = true;
					persevereHover = persevereHover == 1 ? persevereHover : 0;
				} else if (elementHovered != -2 && elementHovered != -3) {
					dehover();
					for (int i = 0; i < selButtons.length; i++)
						if (i == elementHovered) {
							selButtons[i].hovered = true;
							persevereHover = persevereHover == 1 ? persevereHover : 0;
						}
					for (int i = 0; i < classesCards.length; i++)
						if (i + selButtons.length == elementHovered) {
							classesCards[i].hovered = true;
							persevereHover = persevereHover == 1 ? persevereHover : 0;
							if (classesCards[i].x + cardsSize + cardsIniGapX > Gdx.graphics.getWidth()) {
								slider.xCursor += Gdx.graphics.getWidth() / 640f;

							} else if (classesCards[i].x - cardsIniGapX < 0)
								slider.xCursor -= Gdx.graphics.getWidth() / 640f;
						}
				} else if (elementHovered == -3) {
					dehover();
					slider.selected = true;
				}
			}
		} else if (modes == 2){
			if(!existsSelCard()) {
				if (elementHovered != -2) {
					if (upJustPressed()) {
						if (elementHovered > -1 && elementHovered < classesCards.length) {
							lastCard = elementHovered;
							elementHovered = -1;
						} else if (elementHovered == -3) {
							slider.selected = false;
							elementHovered = (byte) (getElement());
							lastCard = elementHovered;
						}
						persevereHover = 1;
					}
					if (downJustPressed()) {
						if (elementHovered == -1) {
							elementHovered = (byte) getElement();
							lastCard = elementHovered;
						} else if (!existsSelCard() && elementHovered != -3) {
							lastCard = elementHovered;
							elementHovered = -3;
						}
						persevereHover = 1;
					}
					if (leftJustPressed()) {
						if (elementHovered > 0) {
							lastCard = --elementHovered;
							counterStateL = 0;
						}
						persevereHover = 1;
					} else if (leftPressed() && elementHovered > 0 && counterStateL != -1) {
						if (counterL++ > times[counterStateL]) {
							lastCard = --elementHovered;
							counterStateL += counterStateL < times.length - 1 ? 1 : 0;
							counterL = 0;
						}
					} else {
						counterL = 0;
						counterStateL = -1;
					}
					if (rightJustPressed()) {
						if (elementHovered > -1 && elementHovered < classesCards.length - 1) {
							lastCard = ++elementHovered;
							counterStateR = 0;
						}
						persevereHover = 1;
					} else if (rightPressed() && elementHovered >= 0 && elementHovered < classesCards.length - 1 && counterStateR != -1) {
						if (counterR++ > times[counterStateR]) {
							lastCard = ++elementHovered;
							counterStateR += counterStateR < times.length - 1 ? 1 : 0;
							counterR = 0;
						}
					} else {
						counterR = 0;
						counterStateR = -1;
					}
				} else {
					if (upJustPressed()) {
						elementHovered = -1;
						persevereHover = 1;
					}
					if (downJustPressed()) {
						elementHovered = lastCard != -1 ? lastCard : (byte) classSlots.length;
						persevereHover = 1;
					}
					if (leftJustPressed()) {
						elementHovered = (byte) max((getElement() - 1), 0);
						persevereHover = 1;
					}
					if (rightJustPressed()) {
						elementHovered = (byte) (getElement() + 1);
						persevereHover = 1;
					}
				}
				if (persevereHover != 1) {
					dehover();
				}
				if (cursorX() >= endSX && cursorX() <= endSX + endSY &&
						cursorY() >= 0 && cursorY() <= endSY) {
					elementHovered = -1;
					persevereHover = -1;
				}
				if (!existsSelCard())
					for (int i = 0; i < classesCards.length; i++) {
						if (cursorX() >= classesCards[i].x && cursorX()
								<= classesCards[i].x + cardsSize &&
								cursorY() >= classesCards[i].y - cardsSize && cursorY() <= classesCards[i].y && cursorMoved()) {
							dehover();
							elementHovered = (byte) i;
							persevereHover = 1;
							break;
						}
					}
				if (persevereHover != 0) {
					if (elementHovered == -1) {
						dehover();
						close.hovered = true;
						persevereHover = persevereHover == 1 ? persevereHover : 0;
					} else if (elementHovered != -2 && elementHovered != -3) {
						dehover();
						for (int i = 0; i < classesCards.length; i++)
							if (i == elementHovered) {
								classesCards[i].hovered = true;
								persevereHover = persevereHover == 1 ? persevereHover : 0;
								if (classesCards[i].x + cardsSize + cardsIniGapX > Gdx.graphics.getWidth()) {
									slider.xCursor += Gdx.graphics.getWidth() / 640f;

								} else if (classesCards[i].x - cardsIniGapX < 0)
									slider.xCursor -= Gdx.graphics.getWidth() / 640f;
							}
					} else if (elementHovered == -3) {
						dehover();
						slider.selected = true;
					}
				}
			} else {
				if (elementHovered != -2) {
					if (upJustPressed()) {
						if (elementHovered == 0) {
							if(items.processUp())
								elementHovered = -1;
							else
								counterStateU = 0;
						}
						persevereHover = 1;
					} else if (upPressed() && elementHovered == 0 && counterStateU != -1) {
						if (counterU++ > times[counterStateU]) {
							counterStateU += counterStateU < times.length - 1 ? 1 : 0;
							counterU = 0;
							if (items.processUp()) {
								elementHovered = -1;
								counterStateU = -1;
							}
						}
					} else {
						counterU = 0;
						counterStateU = -1;
					}
					if (downJustPressed()) {
						elementHovered = 0;
						items.processDown();
						counterStateD = 0;
						persevereHover = 1;
					} else if (downPressed() && elementHovered == 0 && counterStateD != -1) {
						if (counterD++ > times[counterStateD]) {
							counterStateD += counterStateD < times.length - 1 ? 1 : 0;
							counterD = 0;
							if (items.processDown()) {
								counterStateD = -1;
							}
						}
					} else {
						counterD = 0;
						counterStateD = -1;
					}

					if (leftJustPressed()) {
						if (elementHovered == 0) {
							items.processLeft();
						}
						persevereHover = 1;
					}
					if (rightJustPressed()) {
						if (elementHovered == 0) {
							items.processRight();
						}
						persevereHover = 1;
					}
				} else {
					if (upJustPressed()) {
						if(items.processUp())
							elementHovered = -1;
						persevereHover = 1;
					}
					if (downJustPressed()) {
						items.processDown();
						elementHovered = 0;
						persevereHover = 1;
					}
					if (leftJustPressed()) {
						items.processLeft();
						persevereHover = 1;
					}
					if (rightJustPressed()) {
						items.processRight();
						persevereHover = 1;
					}
				}
				if (persevereHover != 1) {
					dehover();
				}
				if (cursorX() >= endSX && cursorX() <= endSX + endSY &&
						cursorY() >= 0 && cursorY() <= endSY) {
					elementHovered = -1;
					persevereHover = -1;
				}
				if(items.processCursor() && cursorMoved()){
					items.saveCursor();
					elementHovered = 0;
					persevereHover = 1;
				}
				if (persevereHover != 0) {
					if (elementHovered == -1) {
						dehover();
						close.hovered = true;
						persevereHover = persevereHover == 1 ? persevereHover : 0;
					} else if (elementHovered == 0) {
						dehover();
						items.processHover();
						persevereHover = persevereHover == 1 ? persevereHover : 0;
					}
				}

			}
		}

	}

	@SuppressWarnings("all")
	private int getElement() {
		float closestToCenter = 1f / 0f;
		int element = -1;
		for (int i = 0; i < classesCards.length; i++) {
			if (closestToCenter > abs(classesCards[i].x - Gdx.graphics.getWidth() / 2f)) {
				closestToCenter = abs(classesCards[i].x - Gdx.graphics.getWidth() / 2f);
				element = i;
			} else if (closestToCenter > abs(classesCards[i].x + classesCards[i].size - Gdx.graphics.getWidth() / 2f)) {
				closestToCenter = abs(classesCards[i].x + classesCards[i].size - Gdx.graphics.getWidth() / 2f);
				element = i;
			}
		}
		return element;
	}

	private void calculateMath(){
		sizeX = Gdx.graphics.getWidth() /(globalSize()*1f);
		sizeY = Gdx.graphics.getHeight() /(globalSize()*.5625f);
		spaceX = 8*sizeX;
		spaceY = Gdx.graphics.getHeight() - 8*sizeY;
		endSX = Gdx.graphics.getWidth() - 8*sizeX;
		endSY = 8*sizeY;

		if(modes == 0) {
			modeSize = sizeY * .5f;
			modeGapX = (endSX - spaceX) / (8);
			modeIniGapX = ((endSX - spaceX) - (modeGapX + 2 * modeSize * globalSize() * .25f)) / 2f + spaceX;
			modeGapY = endSY + Gdx.graphics.getHeight() * .5f;
		}

		else if (modes == 1) {
			selSize = sizeY * .25f;
			selGapX = (endSX - spaceX) / (classSlots.length + 4);
			selIniGapX = ((endSX - spaceX) - (selGapX * (classSlots.length - 1) + classSlots.length * selSize * globalSize() * .25f)) / 2f + spaceX;
			selGapY = endSY + Gdx.graphics.getHeight() * .12f;

			selSizeB = sizeY * .4f;
			selGapXB = (endSX - spaceX) / (classSlots.length + 3);
			selIniGapXB = ((endSX - spaceX) - (selGapXB * (classSlots.length - 1) + classSlots.length * selSizeB * globalSize() * .25f)) / 2f + spaceX;
			selGapYB = endSY + Gdx.graphics.getHeight() * .22f;

			sliWidth = (sizeX * globalSize() - spaceX * 2) * .7f;
			sliGapX = ((sizeX * globalSize() - spaceX * 2) - sliWidth) / 2f + spaceX;
			sliHeight = (sizeY * globalSize() - endSY * 2) * .1f;
			sliGapY = ((sizeY * globalSize() - endSY * 2) * .5f) + endSY;
			sliThickness = min(sizeX, sizeY);

			cardsSize = (sliGapY - sliHeight - selGapY - selSize) * .9f;
			cardsY = sliGapY - sliHeight - cardsSize * .05f;
			cardsIniGapX = spaceX + sizeX * .12f;
			cardsGapX = cardsSize * .2f;

			cardSizeB = sizeY * 32 * 1.1f;
			cardYB = endSY + Gdx.graphics.getHeight() * .78f;
			cardIniGapXB = (Gdx.graphics.getWidth() - cardSizeB) / 2f;

			totalXSpace = cardsGapX * (classesCards.length - 1) + cardsIniGapX * 2 + cardsSize * classesCards.length;
		}

		else if (modes == 2){
			sliWidth = (sizeX * globalSize() - spaceX * 2) * .7f;
			sliGapX = ((sizeX * globalSize() - spaceX * 2) - sliWidth) / 2f + spaceX;
			sliHeight = (sizeY * globalSize() - endSY * 2) * .1f;
			sliGapY = ((sizeY * globalSize() - endSY * 2) * .5f) + endSY;
			sliThickness = min(sizeX, sizeY);

			cardsSize = (sliGapY - sliHeight ) * .7f;
			cardsY = sliGapY - sliHeight - cardsSize * .05f;
			cardsIniGapX = spaceX + sizeX * .12f;
			cardsGapX = cardsSize * .2f;

			cardSizeB = sizeY * 32 * 1.1f;
			cardYB = endSY + Gdx.graphics.getHeight() * .50f;
			cardIniGapXB = spaceX + Gdx.graphics.getWidth()*.01f;

			holderSize = cardSizeB *.35f;
			meleeGapX =  Gdx.graphics.getWidth() * .50f;
			shieldGapX = Gdx.graphics.getWidth() * .76f;
			holderGapY = cardYB - (holderSize)/(2*.3f);

			totalXSpace = cardsGapX * (classesCards.length - 1) + cardsIniGapX * 2 + cardsSize * classesCards.length;

			itemsGapX = shieldGapX;
			itemsIniGapX = meleeGapX;
			itemsY = Gdx.graphics.getHeight() * .6f;
			itemsSize = holderSize * 1.3f;

		}

	}




//*	public void drawer(){
//		b.begin();
//		sprite = new Sprite(atlas.findRegion("anima"));
//		b.draw(sprite,200,200,0,0,128,128,1,1,0,false);
//
//		b.end();
//	}


}
