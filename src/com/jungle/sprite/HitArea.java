package com.jungle.sprite;

public class HitArea {

	private int x1;
	private int x2;
	private int y1;
	private int y2;
	
	private int startx1;
	private int startx2;
	private int starty1;
	private int starty2;
	//private int move;
	
	public HitArea(int x1, int y1, int x2, int y2, int movex, int movey){
		this.x1 = startx1 = x1;
		this.x2 = startx2 = x2;
		this.y1 = starty1 = y1;
		this.y2 = starty2 = y2;	
		
		this.x1 += movex;
		this.x2 += movex;
		this.y1 += movey;
		this.y2 += movey;
	}
	
	public void update(int moveX, int moveY) {
		this.x1 = startx1+moveX;
		this.x2 = startx2+moveX;
		this.y1 = starty1+moveY;
		this.y2 = starty2+moveY;
	}
	
	public int getX1() {
		return x1;
	}

	public int getX2() {
		return x2;
	}

	public int getY1() {
		return y1;
	}

	public int getY2() {
		return y2;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}
}
