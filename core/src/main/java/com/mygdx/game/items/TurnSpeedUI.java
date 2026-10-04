package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.Utils;

import java.util.ArrayList;

import static com.mygdx.game.Settings.turnMode;
import static com.mygdx.game.items.TextureManager.*;
import static com.mygdx.game.items.TurnManager.finalList;

public class TurnSpeedUI extends GUI {
	public float frameWidth;
	public float height;
	float startPos;
	static final float FIXD_SZ_CNT = 3f;
	static final float PX_PER_FRAME = 8f;
	float sizeMultipX = 3;
	float sizeMultipY = 3;
	float alpha = 0.5f;
	float alphaP = 1f;
	String textureBox = "Frame1";
	ArrayList<TurnManager.Turnable> elements = new ArrayList<>();
	ArrayList<GettingAdded> gettingAdded = new ArrayList<>();
	ArrayList<GettingRemoved> dead = new ArrayList<>();

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
		} elements.removeIf(TurnManager.Turnable::getIsDead);
		processDead();
	}



	public void math(){
		frameWidth = Gdx.graphics.getWidth();
		sizeMultipY = Gdx.graphics.getHeight()/1080f * FIXD_SZ_CNT;
		sizeMultipX = sizeMultipY;//frameWidth/1920f * FIXD_SZ_CNT;
		height = (8 + 32)*Gdx.graphics.getHeight()/1080f*sizeMultipY;
		startPos = frameWidth - (finalList.size() * sizeMultipX * 32);

	}

	public void renderElement(int i){
		renderElement(elements.get(i).getPortrait(),startPos+xIndex(i)+xNewElement(i),0);
	}

	public void renderElement(String texture, float x,float z){
		float pMultX = pAdjustX(texture), pMultY = pAdjustY(texture);
		fixatedDrawables.add(getDrawable(texture,x,height,z,alphaP,0,false,false,sizeMultipX*pMultX,sizeMultipY*pMultY,true));
		fixatedDrawables.add(getDrawable(textureBox,x,height,z,alpha,0,false,false,sizeMultipX,sizeMultipY,true));

	}


	public void getList(){
		for(TurnManager.Turnable t : finalList){
			if(!Utils.elementExistsInList(elements,t) && !elementExistsInList(gettingAdded,t))
				addElement(t);
		}

	}

	float[] posDelta;
	public void processList(){
		ArrayList<Integer> newPos = new ArrayList<>();
		float maxX = frameWidth;
		for (GettingAdded g : gettingAdded){
			while (elements.size() > g.indexToReach && g.turnable.getSpeed() < elements.get(g.indexToReach).getSpeed())
				g.indexToReach++;
			if(sizeMultipX*(g.x + PX_PER_FRAME) > startPos+xIndex(g.indexToReach)+xNewElement(g.indexToReach)) {
				maxX = Math.max(g.x, maxX);
				g.x -=PX_PER_FRAME;
				g.realX = g.x * sizeMultipX;
				renderElement(g.turnable.getPortrait(),g.realX,0);
			}
			else {
				newPos.sort(Integer::compare);
				for(Integer i : newPos)
					if (g.indexToReach == i) {
						while (g.turnable.getSpeed() < elements.get(i).getSpeed())
							g.indexToReach++;
					}
				elements.add(g.indexToReach, g.turnable);
				newPos.add(g.indexToReach);
			}
		} gettingAdded.removeIf(g -> Utils.elementExistsInList(elements,g.turnable));
		if(posDelta == null || posDelta.length < elements.size()){
			int elementsOnI = 0;
			float[] newDelta = new float[elements.size()];
			for(Integer in : newPos){
				for (int i = 0; i < newDelta.length; i++) {
					if(in == i && i != 0) {
						newDelta[i] = newDelta[i - 1];
						elementsOnI++;
					}
					else if (in == i){
						newDelta[0] = gettingAdded.size()*32;
						elementsOnI++;
					}
					else if (posDelta.length > i-elementsOnI && i != 0)
						newDelta[i] = posDelta[i-elementsOnI];
				}
			}
			posDelta = newDelta;
		}
		for (int i = 0; i < posDelta.length; i++){
			if(maxX > frameWidth) {
				posDelta[i]-=PX_PER_FRAME;
			}
			if (indexOverlaps(i)) {
				posDelta[i]+=PX_PER_FRAME;
			}
		}
	}

	public boolean indexOverlaps(int index){
		for(GettingAdded g : gettingAdded)
			if(g.x*sizeMultipY < startPos+xIndex(index)+xNewElement(index)+32*sizeMultipY && (g.x+32)*sizeMultipY > startPos+xIndex(index)+xNewElement(index))
				return true;
		return false;
	}

	public float xNewElement(int index){
		if(gettingAdded.isEmpty() || index >= elements.size() || index >= posDelta.length)
			return 0;
		return posDelta[index]*32*sizeMultipX;
	}

	public void addElement(TurnManager.Turnable t) {
		float lastX = frameWidth;
		for(GettingAdded g : gettingAdded){
			if (g.x > lastX)
				lastX = g.x;
		}
		int indexObjective = elements.size();
		for (int i = 0; i < elements.size(); i++)
			if(elements.get(i).getSpeed() < t.getSpeed()) {
				indexObjective = i;
				break;
			}
		gettingAdded.add(new GettingAdded(t,lastX,indexObjective));
	}

	public void removeElement(TurnManager.Turnable t, int index){
		dead.add(new GettingRemoved(t,startPos+xIndex(index)+xNewElement(index)));

	}

	public void processDead(){
		for(GettingRemoved d : dead){
			renderElement(d.turnable.getPortrait(),d.x*sizeMultipY,d.z);
			d.z += 0.05f;
		} dead.removeIf(d -> d.z >= 50);
	}

	public float pAdjustX(String texture){
		return 32f/getTextureWidth(texture);
	}

	public float pAdjustY(String texture){
		return 32f/getTextureHeight(texture);
	}

	public float xIndex(int index){
		return 32*sizeMultipX*index;
	}

	private boolean elementExistsInList(ArrayList<GettingAdded> list, TurnManager.Turnable element){
		for(GettingAdded o : list)
			if(element == o.turnable)
				return true;
		return false;
	}

	private static class GettingAdded{
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
		float z = 0;

		GettingRemoved(TurnManager.Turnable t, float x){
			turnable = t;
			this.x = x;
		}
	}


}
