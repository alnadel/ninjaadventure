package com.jungle.shinobi;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Game {

	private int lvl = 0;
	private int gold = 0;
	private int time = 0;
	private int floor = 0;
	
	private Bitmap topbar;
	private int x;
	private int y;
	private float scale;
	
	private Bitmap groundbmp;

	private Paint paint = new Paint();
	private Grounds grounds;
	
	
	public Game(Bitmap topbar, int x, int y, Bitmap groundbmp, int stageWidth, int stageHeight) {

		scale = (float)stageWidth/topbar.getWidth();
		
		//Log.d("Game", "Stage: "+stageWidth+" Width: "+bitmap.getWidth()+" Scale: "+scale);
		this.topbar = Bitmap.createScaledBitmap(topbar, (int)(scale*topbar.getWidth()), (int)(scale*topbar.getHeight()), true);
		this.topbar.setDensity(stageWidth);
		
		this.groundbmp = Bitmap.createScaledBitmap(groundbmp, (int)(scale*groundbmp.getWidth()), (int)(scale*groundbmp.getHeight()), true);
		this.groundbmp.setDensity(stageWidth);
		
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);  
        paint.setFilterBitmap(true);  
        paint.setTextSize(20);
        
        this.x = x;
        this.y = y;
        
        grounds = new Grounds(groundbmp, 0, stageHeight, lvl, scale, stageWidth, stageHeight);
        
		setGold(0);
		setTime(120);
				
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public Grounds getGrounds() {
		return grounds;
	}

	public void draw(Canvas canvas) {
		grounds.draw(canvas);
		
		canvas.drawBitmap(topbar, x, y, null);
		canvas.drawText(Integer.toString(time), (int)(100*scale)+x, (int)(55*scale)+y, paint);
		canvas.drawText(Integer.toString(gold), (int)(400*scale)+x, (int)(55*scale)+y, paint);
		
	}
}
