package com.jungle.shinobi;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BitmapSprite {

	private Bitmap bmp;
	private int x = 0;
	private int y = 0;
	
	public void BitmapSprite( Bitmap bmp, int x, int y) {
	
		this.bmp =  bmp;
		this.x = x;
		this.y = y;
	}

	public void draw(Canvas canvas) {
		
		canvas.drawBitmap(bmp, x, y, null);
	}
	
	public Bitmap getBitmap() {
		return bmp;
	}

	public void setBitmap(Bitmap bmp) {
		this.bmp = bmp;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
