package com.mygdx.game.items;

import static com.mygdx.game.Settings.*;

//TODO: COMPLETELY CHANGE HOW THESE WORK:
// Size: either minimum size (1px) or standard (globalSize() multiples). Will have a center to calculate the offset and
// to tell the spawner from the next stage how much to offset Anima.
// The coordinate is minimum size on the same coordinate that this offset is ignored (e.g.: if walking horizontally,
// the offset is vertical, thus the 1xp size will have to be on the horizontal coordinate, AKA a 1px base.)
public class ScreenWarp {
	protected Stage stage;
	protected float base, height;
	public float x;
	public float y;
	protected String screenWarpTexture = "ScreenWarp";
	public int ID;
	public static int IDState = 0;
	public Type color;
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


	public ScreenWarp(float x, float y, float base, float height, int destination,Type type, int... endOfColor) {
		this.x = x;
		this.y = y;
		this.base = base;
		this.height = height;
		this.destination = destination;
		ID = IDState + 1;
		IDState++;
		color = type;
		if (color == Type.END_OF_COLOR && endOfColor.length == 1)
			this.endOfColor = endOfColor[0];
		else if (color == Type.END_OF_COLOR && endOfColor.length == 0)
			printErr("ERROR: END_OF_COLOR REQUESTED YET NOT SET. ERROR AT ScreenWarp 7-arg CONSTRUCTOR.");
		else if (color == Type.END_OF_COLOR) {
			printErr("ERROR: END_OF_COLOR HAS SOMEHOW MORE THAN ONE VALUE. PROCEEDING WITH THE FIRST VALUE. ERROR AT ScreenWarp 7-arg CONSTRUCTOR.");
			this.endOfColor = endOfColor[0];
		}
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
	public static ScreenWarp createNewHorizontalSW(float x, float y, float width, boolean isTop, int destination, Type color, int... endOfColor){
		return new ScreenWarp(x*globalSize(),y*globalSize() + (isTop ? globalSize() - 1 : 0),width * globalSize(),1,destination,color,endOfColor);
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
	public static ScreenWarp createNewVerticalSW(float x, float y, float height,boolean isLeft,int destination, Type color, int... endOfColor){
		return new ScreenWarp(x*globalSize() + (isLeft ? 0 : globalSize() - 1),y*globalSize(),1,height*globalSize(),destination,color,endOfColor);
	}


	public enum Type{
		RED(1),BLUE(2),GREEN(3),CYAN(4),PINK(5),PURPLE(6),WHITE(7),
		BLACK(8),GREY(9),BROWN(10),ORANGE(11),DARK_BLUE(12),END_OF_COLOR(0);

		int id;
		Type(int id){
			this.id = id;
		}
	}


















}
