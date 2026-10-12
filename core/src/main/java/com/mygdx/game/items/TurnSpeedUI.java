package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import static com.mygdx.game.Settings.*;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.TurnManager.*;

public class TurnSpeedUI extends GUI {
	static final float FIXD_SZ_CNT = 3f;
	static final int BOX_SIZE = 32;
	static final float PX_PER_FRAME = 8f;
	static final float SCREEN_CNT = 1920f;
	static final String BOX_TEXTURE = "Frame1";
	static final String BOX_TEXTURE_ACTIVE = "Frame1Active";
	static final String BOX_TR_L = "Frame1STL";
	static final String BOX_TR_R = "Frame1STR";
	static final String BOX_TR_LR= "Frame1SRRL";
	public float frameWidth;
	public float height;
	float startPos;
	float sizeMultipX = 3;
	float sizeMultipY = 3;
	float alpha = 0.5f;
	float alphaP = 1f;
	String textureBox = BOX_TEXTURE;
	ArrayList<GettingRendered> elements = new ArrayList<>();
	ArrayList<GettingAdded> gettingAdded = new ArrayList<>();
	ArrayList<GettingRemoved> dead = new ArrayList<>();

	boolean permittedTocheckOrder = false;

	TurnSpeedUI(){queuedForRemoval = true;}


	public void render(){
		if(turnMode){
			math();
			renderList();
		}
	}



	public void renderList(){
		getList();
		processList();
		for(int i = 0; i < elements.size(); i++){
			if(elements.get(i).getIsDead())
				removeElement(elements.get(i),i);
			else
				renderElement(i);
		} elements.removeIf(m -> (m != null && (m.getIsDead() || elementExistsInDeath(m.turnable))));
		processDead();
	}


	public void math(){
		frameWidth = Gdx.graphics.getWidth();
		sizeMultipY = Gdx.graphics.getHeight()/1080f * FIXD_SZ_CNT;
		sizeMultipX = sizeMultipY;//frameWidth/1920f * FIXD_SZ_CNT;
		height = (8 + BOX_SIZE)*Gdx.graphics.getHeight()/1080f*sizeMultipY;
		startPos = frameWidth - ((elements.size()+gettingAdded.size()) * sizeMultipX * BOX_SIZE);

	}

	public void renderElement(int i){
		GettingRendered t = elements.get(i);
		float x = startPos + xIndex(i) + xNewElement(i);
		textureBox  = BOX_TEXTURE;

		if (t != null) {
			t.active = elements.get(i).isPermittedToAct();
			float[] colors = new float[]{t.turnable.portraitColor()[0],t.turnable.portraitColor()[1],t.turnable.portraitColor()[2]};

			if (!t.active && elements.get(i).z <= 0){
				byte speedTiesState = 0;
				if(i > 0)
					speedTiesState += elements.get(i).speedTiesLeft ? 1 : (byte) 0;
				if(i < elements.size()-1)
					speedTiesState += elements.get(i+1).speedTiesLeft ? 2 : (byte) 0;

				switch (speedTiesState){
					case 0: textureBox = BOX_TEXTURE; break;
					case 1: textureBox = BOX_TR_L; break;
					case 2: textureBox = BOX_TR_R; break;
					case 3: textureBox = BOX_TR_LR; break;
					default:
				}


				renderElement(t.getPortrait(), x,colors);
			}
			else if (!t.active) {
				textureBox  = BOX_TEXTURE_ACTIVE;
				float realCenterX, realCenterY, size, expectedCenterX, expectedCenterY, realTopY, expectedTopY;
				size = BOX_SIZE * sizeMultipY * (t.z * 0.2f + 1);
				realCenterX = x + size / 2;
				expectedCenterX = x + BOX_SIZE / 2f * sizeMultipX;
				realCenterY = height + size / 2;
				expectedCenterY = height + BOX_SIZE / 2f * sizeMultipY;
				realTopY = height + size;
				expectedTopY = height + BOX_SIZE * sizeMultipY;

				float fixedX, fixedY;

				fixedX = x - (realCenterX - expectedCenterX);
				fixedY = height + (turnSpeedCapY() ? (realTopY - expectedTopY) : (realCenterY - expectedCenterY));


				renderElement(t.turnable.getPortrait(),fixedX,fixedY,t.z,t.opacity,colors);
				t.z -= t.z > 0 ? 0.20f : 0;
				if(t.z < 0)
					t.z = 0f;
				t.opacity -= t.opacity > 1 ? 0.25f : 0;
				if (t.opacity < 1)
					t.opacity = 1;

			} else {
				textureBox  = BOX_TEXTURE_ACTIVE;
				float realCenterX, realCenterY, size, expectedCenterX, expectedCenterY, realTopY, expectedTopY;
				size = BOX_SIZE * sizeMultipY * (t.z * 0.2f + 1);
				realCenterX = x + size / 2;
				expectedCenterX = x + BOX_SIZE / 2f * sizeMultipX;
				realCenterY = height + size / 2;
				expectedCenterY = height + BOX_SIZE / 2f * sizeMultipY;
				realTopY = height + size;
				expectedTopY = height + BOX_SIZE * sizeMultipY;

				float fixedX, fixedY;

				fixedX = x - (realCenterX - expectedCenterX);
				fixedY = height + (turnSpeedCapY() ? (realTopY - expectedTopY) : (realCenterY - expectedCenterY));


				renderElement(t.turnable.getPortrait(),fixedX,fixedY,t.z,t.opacity,colors);
				t.z += t.z < 1 ? 0.20f : 0;
				t.opacity += t.opacity < 4 ? 0.25f : 0;
			}
		}
	}

	public void renderElement(String texture, float x,float... colors){
		renderElement(texture,x,height,0,1, colors);
	}

	public void renderElement(String texture, float x, float height,float z, float opacity, float... color){
		float pMultX = pAdjustX(texture), pMultY = pAdjustY(texture);
		fixatedDrawables.add(getDrawable(textureBox,x,height,z,alpha*opacity,0,false,false,sizeMultipX,sizeMultipY,true,color));
		fixatedDrawables.add(getDrawable(texture,x,height,z,alphaP*opacity,0,false,false,sizeMultipX*pMultX,sizeMultipY*pMultY,true,color));

	}

	private Turnable tToCheck;
	public void permitToCheck(Turnable t, boolean speedTiesLeft){
		permittedTocheckOrder = true;
		tToCheck = t;
		int pos = findElementPosition(t);
		if(pos != -1)
			elements.get(pos).speedTiesLeft = speedTiesLeft;
	}


	public void getList(){
		for(TurnManager.Turnable t : finalList)
			if(!elementExistsInRender(t) && !elementExistsInList(t) && (t.didItAct() || t.isPermittedToAct()) && !t.getIsDead())
				addElement(t);
		if(isTurnRunning() && willRunTurn() && permittedTocheckOrder)
			if(finalList.size() == elements.size()) {
				permittedTocheckOrder = false;
				if(tToCheck != null) {
					int element = findElementPosition(tToCheck);
					if (element != -1) {
						print("element on " + finalList.indexOf(tToCheck) + " on final is " + tToCheck + " while on element its " + elements.get(element).turnable);
						moveElement(element, finalList.indexOf(tToCheck));
					}
				}
				elements.removeIf(e -> e == null || e.queueRemoval);
			}
	}

	public void moveElement(int elementToMove, int newPosition){
		if(elementToMove == newPosition)
			return;
		print("Called to move element from " + elementToMove + " to " + newPosition);
		if(newPosition < elementToMove){
			int lastElementToMove = elementToMove-1;
			GettingRendered mobile = elements.get(elementToMove);
			for(int i = newPosition ; i <= lastElementToMove; i++){
					elements.get(i).mobileFactor = -BOX_SIZE;
			}
			elements.remove(elementToMove);
			elements.add(newPosition,mobile);
			mobile.mobileFactor = BOX_SIZE * (elementToMove - newPosition);
			mobile.moving =true;
		}
	}

	float[] posDelta;
	public void processList(){
		ArrayList<Integer> newPos = new ArrayList<>();
		float minX = frameWidth, maxX = frameWidth;
		updateMovables();
		for (GettingAdded g : gettingAdded){
			if(g != null && g.turnable != null) {
				while (elements.size() > g.indexToReach && g.turnable.getSpeed() < elements.get(g.indexToReach).getSpeed())
					g.indexToReach++;
				if ((g.x - PX_PER_FRAME) * frameWidth / SCREEN_CNT > startPos + xIndex(g.indexToReach)) {
					g.x -= PX_PER_FRAME;
					g.realX = g.x * frameWidth / SCREEN_CNT;
					maxX = Math.max(g.realX + BOX_SIZE * sizeMultipY, maxX);
					minX = Math.min(g.realX, minX);
					float[] colors = new float[]{g.turnable.portraitColor()[0],g.turnable.portraitColor()[1],g.turnable.portraitColor()[2]};
					renderElement(g.turnable.getPortrait(), g.realX,colors);
				} else {
					newPos.sort(Integer::compare);
					for (Integer i : newPos)
						if (g.indexToReach == i) {
							while (g.turnable.getSpeed() < elements.get(i).getSpeed())
								g.indexToReach++;
						}
					if(elements.size() > g.indexToReach && elements.get(g.indexToReach) == null)
						elements.remove(g.indexToReach);
					elements.add(g.indexToReach, new GettingRendered(g.turnable));
					newPos.add(g.indexToReach);
				}
			}
		} gettingAdded.removeIf(g -> elementExistsInRender(g.turnable));
		if(!gettingAdded.isEmpty()) {
			if (posDelta == null || posDelta.length < elements.size()) {
				int elementsOnI = 0;
				float[] newDelta = new float[elements.size()];
				for (Integer in : newPos) {
					for (int i = 0; i < newDelta.length; i++) {
						if (in == i && i != 0) {
							newDelta[i] = newDelta[i - 1];
							elementsOnI++;
						} else if (in == i) {
							newDelta[0] = gettingAdded.size() * BOX_SIZE;
							elementsOnI++;
						} else if (posDelta != null && posDelta.length > i - elementsOnI && i != 0)
							newDelta[i] = posDelta[i - elementsOnI];
					}
				}
				posDelta = newDelta;
			}
			for (int i = 0; i < posDelta.length; i++) {
				if (maxX > frameWidth && minX < frameWidth) {
					posDelta[i] -= PX_PER_FRAME;
				}
				if (indexOverlaps(i)) {
					posDelta[i] += PX_PER_FRAME;
				}
			}
		} else
			posDelta = null;
	}

	public boolean isMovement(){
		for (GettingRendered g : elements)
			if(g.moving)
				return true;
		return false;
	}

	public boolean indexOverlaps(int index){
		for(GettingAdded g : gettingAdded)
			if(g.x*frameWidth/SCREEN_CNT < startPos+xIndex(index)+xNewElement(index)+BOX_SIZE*sizeMultipY && BOX_SIZE*sizeMultipY+g.x*frameWidth/SCREEN_CNT > startPos+xIndex(index)+xNewElement(index)
					&& (g.x*frameWidth/SCREEN_CNT+BOX_SIZE*sizeMultipY < frameWidth))
				return true;
		return false;
	}

	public boolean indexOverlapsMobile(int index, float mobileX){
		return mobileX < startPos+xIndex(index)+xNewElement(index)+BOX_SIZE*sizeMultipY && BOX_SIZE*sizeMultipY+mobileX > startPos+xIndex(index)+xNewElement(index);
	}

	public float xNewElement(int index){
		float returnNum = 0;
		if (posDelta != null && index < posDelta.length && !gettingAdded.isEmpty())
			returnNum += posDelta[index]*frameWidth/SCREEN_CNT + gettingAdded.size()*BOX_SIZE*sizeMultipY;
		if(!dead.isEmpty())
			returnNum += deathOffset(index);
		return returnNum + theMobileFactor(index);
	}

	private float theMobileFactor(int index){
		if(!isMovement())
			return 0;
		return elements.get(index).mobileFactor * sizeMultipY;
	}

	private void updateMovables() {
		if (!isMovement())
			return;
		GettingRendered mobile = null;
		int movIndex = -1;
		for (int i = 0; i < elements.size(); i++)
			if (elements.get(i).moving) {
				mobile = elements.get(i);
				movIndex = i;
				break;
			}
		if (mobile == null) {
			printErr("Literally how [AT: TurnSpeedUI -> updateMovables()]");
			return;
		}
		if (mobile.mobileFactor == 0)
			mobile.moving = false;
		if (mobile.mobileFactor > 0) {
			mobile.mobileFactor -= PX_PER_FRAME;
			if (mobile.mobileFactor < 0)
				mobile.mobileFactor = 0;
		} else if (mobile.mobileFactor < 0) {
			mobile.mobileFactor += PX_PER_FRAME;
			if (mobile.mobileFactor > 0)
				mobile.mobileFactor = 0;
		}

		for(int i = 0; i < elements.size(); i++){
			GettingRendered g = elements.get(i);
			if(g != mobile)
				if(indexOverlapsMobile(i, startPos+xIndex(movIndex)+xNewElement(movIndex)+theMobileFactor(movIndex))) {
					if (g.mobileFactor > 0) {
						g.mobileFactor -= PX_PER_FRAME;
						if (g.mobileFactor < 0)
							g.mobileFactor = 0;
					} else if (g.mobileFactor < 0) {
						g.mobileFactor += PX_PER_FRAME;
						if (g.mobileFactor > 0)
							g.mobileFactor = 0;
					}
				}
		}
	}


	public void addElement(TurnManager.Turnable t) {
		float lastX = 1920f;
		for(GettingAdded g : gettingAdded){
			if (g.x > lastX)
				lastX = g.x;
		}
		lastX += BOX_SIZE*sizeMultipY/SCREEN_CNT*frameWidth;
		int indexObjective = elements.size();
		for (int i = 0; i < elements.size(); i++)
			if(elements.get(i).getSpeed() < t.getSpeed()) {
				indexObjective = i;
				break;
			}
		gettingAdded.add(new GettingAdded(t,lastX,indexObjective));
	}

	private void removeElement(GettingRendered t, int index){
		xDeathOffset = 0;
		dead.add(new GettingRemoved(t.turnable,(startPos+xIndex(index)+xNewElement(index))/frameWidth,index));

	}

	float xDeathOffset = -1;
	private float deathOffset(int index){
		if(xDeathOffset == -1)
			return 0;
		if(xDeathOffset - deadAffectedBy(index)*BOX_SIZE*sizeMultipY >= 0) {
			if(dead.isEmpty())
				xDeathOffset = -1;
			return 0;
		}
		float returnNumber = (xDeathOffset)*frameWidth/SCREEN_CNT - deadAffectedBy(index)*BOX_SIZE*sizeMultipY;
		xDeathOffset += PX_PER_FRAME;
		return returnNumber;
	}

	private int deadAffectedBy(int index){
		int num = 0;
		for (GettingRemoved d : dead)
			if(index < d.index)
				num++;
		return num;
	}

	public void processDead(){
		dead.removeIf(d -> d.z >= 20);
		for(GettingRemoved d : dead){
			float realCenterX, realCenterY, size, sizePortX, sizePortY, expectedCenterX, expectedCenterY, realTopY, expectedTopY;
			size = BOX_SIZE*sizeMultipY*(d.z*0.2f + 1);
			sizePortX = size/pAdjustX(d.turnable.getPortrait());
			sizePortY = size/pAdjustY(d.turnable.getPortrait());

			realCenterX = d.x*frameWidth + size/2;
			expectedCenterX = d.x*frameWidth + BOX_SIZE/2f*sizeMultipX;
			realCenterY = height + size/2;
			expectedCenterY = height + BOX_SIZE/2f*sizeMultipY;
			realTopY = height + size;
			expectedTopY = height + BOX_SIZE*sizeMultipY;

			float fixedX, fixedY;

			fixedX = d.x*frameWidth - ( realCenterX - expectedCenterX );
			fixedY = height + (turnSpeedCapY() ? ( realTopY - expectedTopY ) : ( realCenterY - expectedCenterY ));


			renderElement(d.turnable.getPortrait(),fixedX,fixedY,d.z,d.opacity
					,new float[]{d.turnable.portraitColor()[0],d.turnable.portraitColor()[1],d.turnable.portraitColor()[2]});
			d.opacity -= .02f;
			d.z += 0.20f;
		}
	}

	public float pAdjustX(String texture){
		return (float) BOX_SIZE /getTextureWidth(texture);
	}

	public float pAdjustY(String texture){
		return (float) BOX_SIZE /getTextureHeight(texture);
	}

	public float xIndex(int index){
		return BOX_SIZE*sizeMultipX*index;
	}

	private boolean elementExistsInRender(TurnManager.Turnable element){
		for(GettingRendered o : elements)
			if(o != null && element == o.turnable)
				return true;
		return false;
	}

	private boolean elementExistsInList(TurnManager.Turnable element){
		for(GettingAdded o : gettingAdded)
			if(element == o.turnable)
				return true;
		return false;
	}

	private boolean elementExistsInDeath(TurnManager.Turnable element){
		for(GettingRemoved o : dead)
			if(element == o.turnable)
				return true;
		return false;
	}

	private int findElementPosition(TurnManager.Turnable t){
		for(int i = 0; i < elements.size();i++)
			if (elements.get(i) != null && t == elements.get(i).turnable)
				return i;
		return -1;
	}

	private static class GettingAdded {
		TurnManager.Turnable turnable;
		float x;
		float realX;
		int indexToReach;

		GettingAdded(TurnManager.Turnable t, float x, int objective){
			turnable = t;
			this.x = x;
			indexToReach = objective;
		}

	}

	private static class GettingRemoved{
		TurnManager.Turnable turnable;
		float x;
		int index;
		float z = 0;
		float opacity = 1f;

		GettingRemoved(TurnManager.Turnable t, float x,int index){
			turnable = t;
			this.x = x;
			this.index = index;
		}
	}

	private static class GettingRendered{
		TurnManager.Turnable turnable;
		boolean active;
		float z;
		float opacity = 1f;
		boolean queueRemoval = false;
		boolean moving = false;
		float mobileFactor = 0f;
		boolean speedTiesLeft = false;

		GettingRendered(TurnManager.Turnable t){
			turnable = t;
		}

		public boolean getIsDead(){return turnable.getIsDead();}

		public boolean isPermittedToAct(){return turnable.isPermittedToAct();}

		public float getSpeed(){return turnable.getSpeed();}

		public String getPortrait(){return turnable.getPortrait();}
	}

}
