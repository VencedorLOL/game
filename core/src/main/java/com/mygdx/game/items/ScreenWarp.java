package com.mygdx.game.items;

import static com.mygdx.game.Settings.*;

//TODO: COMPLETELY CHANGE HOW THESE WORK:
// Size: either minimum size (1px) or standard (globalSize() multiples). Will have a center to calculate the offset and
// to tell the spawner from the next stage how much to offset Anima.
// The coordinate is minimum size on the same coordinate that this offset is ignored (e.g.: if walking horizontally,
// the offset is vertical, thus the 1xp size will have to be on the horizontal coordinate, AKA a 1px base.)
public class ScreenWarp {
	protected Stage stage;
	public float base;
	public float height;
	public float x;
	public float y;
	protected String screenWarpTexture = "selectionIndicator";
	public int ID;
	public static int IDState = 0;
	public int endOfColor = 0;
	//only for stage creation
	public int destination;

	public ScreenWarp(float x, float y, float base, float height) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		ID = IDState + 1;
		IDState++;
	}

	public ScreenWarp(float x, float y, float base, float height, int endOfColor) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		ID = IDState + 1;
		IDState++;
		this.endOfColor = endOfColor;

	}


	public ScreenWarp(float x, float y, float base, float height, int destination, int... endOfColor) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		this.destination = destination;
		ID = IDState + 1;
		IDState++;
		if (endOfColor.length == 1)
			this.endOfColor = endOfColor[0];
		else if (endOfColor.length == 0)
			printErr("ERROR: END_OF_COLOR REQUESTED YET NOT SET. ERROR AT ScreenWarp 6-arg CONSTRUCTOR.");
	}

	public ScreenWarp(float x, float y, int destination) {
		this.x = x;
		this.y = y;
		this.base = globalSize();
		this.height = globalSize();
		this.destination = destination;
		ID = IDState + 1;
		IDState++;
	}

	public boolean doesCharInteractWithMe(Character chara){
		if (!turnMode)
			return x < chara.x + chara.base && x + base > chara.x && y < chara.y + chara.height && y + height > chara.y;
		return chara.x == x && chara.y == y;
	}


	/**
	Usage:
	 @param x: SIMPLIFIED x coordinate.
	 @param y: SIMPLIFIED y coordinate.
	 @param width: SIMPLIFIED width of the object.
	 @param isTop: Whether the object should be attached at the top of the grid. False if it should be set at the bottom.
	 @param color: Color of the object.
	 @param endOfColor: In case of the object being colorless, set the number of the object.
	*/
	public static ScreenWarp createNewHorizontalSW(float x, float y, float width, boolean isTop, int destination, int... endOfColor){
		return new ScreenWarp(x*globalSize(),y*globalSize() + (isTop ? globalSize() - 1 : 0),width * globalSize(),1,destination,endOfColor);
	}
	/**
	 Usage:
	 @param x: SIMPLIFIED x coordinate.
	 @param y: SIMPLIFIED y coordinate.
	 @param height: SIMPLIFIED height of the object.
	 @param isLeft: Whether the object should be attached at the left of the grid. False if it should be set at the right.
	 @param color: Color of the object.
	 @param endOfColor: In case of the object being colorless, set the number of the object.
	 */
	public static ScreenWarp createNewVerticalSW(float x, float y, float height,boolean isLeft,int destination, int... endOfColor){
		return new ScreenWarp(x*globalSize() + (isLeft ? 0 : globalSize() - 1),y*globalSize(),1,height*globalSize(),destination,endOfColor);
	}


	public enum Type{
		RED(189,35,35),ORANGE(220,110,20),YELLOW(250,250,0),GREEN(35,189,35),CYAN(87,197,236),DARK_BLUE(35,35,189),PURPLE(150,50,200)
		,PINK(231,173,173),BROWN(140,90,20), BLACK(20,20,20),WHITE(255,255,255),GREY(150,150,150),END_OF_COLOR(15,30,60);

		public final int r,g,b;
		Type(int r,int g, int b){
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}



















}
