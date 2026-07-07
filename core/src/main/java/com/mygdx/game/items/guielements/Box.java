package com.mygdx.game.items.guielements;

import com.mygdx.game.items.GUI;
import com.mygdx.game.items.TextureManager;

import static com.mygdx.game.items.TextureManager.fixatedDrawables;

public class Box extends GUI {

	byte r = (byte) 255;
	byte g = (byte) 255;
	byte b = (byte) 255;

	byte rBg = (byte) 255;
	byte gBg = (byte) 255;
	byte bBg = (byte) 255;


	public String defaultTx = "BackgroundTextbox";

	public String cornerA = "CornerA";
	public String sideAB = "SideAB";
	public String cornerB = "CornerB";

	public String sideBC = "SideBC";

	public String cornerC = "CornerC";
	public String sideCD = "SideCD";
	public String cornerD = "CornerD";

	public String sideDA = "SideDA";

	Box(){}

	public Box(boolean changeToTextboxConfiguration){
		if(changeToTextboxConfiguration){
			sideAB = "BackgroundTextbox";
			sideBC = "BackgroundTextbox";
			sideCD = "BackgroundTextbox";
			sideDA = "BackgroundTextbox";

			cornerA = "BackgroundTextbox";
			cornerB = "BackgroundTextbox";
			cornerC = "BackgroundTextbox";
			cornerD = "BackgroundTextbox";

			defaultTx = "BackgroundTextbox";

			r = (byte) 255;
			g = (byte) 255;
			b = (byte) 255;

			rBg  = (byte) 0;
			gBg  = (byte) 0;
			bBg  = (byte) 0;
		}
	}

	Box(int rBg, int gBg, int bBg){
		this.rBg =  (byte) rBg;
		this.gBg =  (byte) gBg;
		this.bBg =  (byte) bBg;
	}

	public void colorBg(byte... color) {
		if (color.length == 1)
			rBg = color[0];
		else if (color.length == 2) {
			rBg = color[0];
			gBg = color[1];
		}
		else if (color.length == 3) {
			rBg = color[0];
			gBg = color[1];
			bBg = color[2];
		}

	}

	public void color(byte... color) {
		if (color.length == 1)
			r = color[0];
		if (color.length == 2) {
			r = color[0];
			g = color[1];
		}
		if (color.length == 3) {
			r = color[0];
			g = color[1];
			b = color[2];
		}

	}

	public void render(float x, float y, float width, float height, float size){
		render(x,y,width,height,size,r,g,b,rBg,gBg,bBg);
	}


	public void render(float x, float y, float width, float height, float size,byte r,byte g, byte b, byte rBg, byte gBg, byte bBg){
		float sideStartingX = x + size *szTxtr;
		float sideFinalY = height - size *szTxtr;

		float widthSide = (width - sideStartingX)/szTxtr;
		float heightSide = (sideFinalY - y)/szTxtr;
/*		fixatedDrawables.add(new TextureManager.DrawableObject(defaultTx,x,y,1,false,false,(width/32-size*2),(height/32-size*2),true,(float) (rBg & 0xFF),(float) (gBg & 0xFF), (float) (bBg & 0xFF)));

		fixatedDrawables.add(new TextureManager.DrawableObject(cornerA,x,y,1,false,false,size,size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideAB,x,y-size*32,1,false,false,size,(height/32-size*2),true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerB,x,y-size*32-(height/32-size*2)*32,1,false,false,size,size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));

		fixatedDrawables.add(new TextureManager.DrawableObject(sideBC,x+size*32,y-size*32-(height/32-size*2)*32,1,false,false,(width/32-size*2),size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));

		fixatedDrawables.add(new TextureManager.DrawableObject(cornerC,x+size*32+(width/32-size*2)*32,y-size*32-(height/32-size*2)*32,1,false,false,size,size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideCD,x+size*32+(width/32-size*2)*32,y-size*32,1,false,false,size,(height/32-size*2),true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerD,x+size*32+(width/32-size*2)*32,y,1,false,false,size,size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));

		fixatedDrawables.add(new TextureManager.DrawableObject(sideDA,x+size*32,y,1,false,false,(width/32-size*2),size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
*/
		fixatedDrawables.add(new TextureManager.DrawableObject(defaultTx, sideStartingX,sideFinalY,1,false,false,widthSide,heightSide,true,(float) (rBg & 0xFF),(float) (gBg & 0xFF), (float) (bBg & 0xFF)));

		fixatedDrawables.add(new TextureManager.DrawableObject(cornerA, x, y,1,false,false, size, size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerD, width, y,1,!true,false, size, size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerB, x, height,1,false,!true, size, size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(cornerC, width, height,1,!true,!true, size, size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));

		fixatedDrawables.add(new TextureManager.DrawableObject(sideDA, sideStartingX, y,1,false,false,widthSide, size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideBC, sideStartingX, height,1,false,!true,widthSide, size,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideAB, x,sideFinalY,1,false,false, size,heightSide,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));
		fixatedDrawables.add(new TextureManager.DrawableObject(sideCD, width,sideFinalY,1,!true,false, size,heightSide,true,(float) (r & 0xFF),(float) (g & 0xFF), (float) (b & 0xFF)));



	}




	static float szTxtr = 32;

}
