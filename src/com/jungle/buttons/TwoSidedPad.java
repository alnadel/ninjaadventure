package com.jungle.buttons;

import com.jungle.shinobi.sprite.AnimatedSprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class TwoSidedPad {
	
	private static final String TAG = AnimatedSprite.class.getSimpleName();

	private Bitmap bitmap;
	private int bitmapX;
	private int bitmapY;

	public int activeDir = 0;

	
	public TwoSidedPad(Bitmap bitmap, int x, int y)
	{
		this.bitmap = bitmap;

		this.bitmapX = x;
		this.bitmapY = y;
	}

	public void draw(Canvas canvas) {
		
		canvas.drawBitmap(bitmap, bitmapX, bitmapY, null);
		
		switch(activeDir){
			case 1:
				drawLeft(canvas);
				break;
			case 2:
				drawRight(canvas);
				break;
		}
		
	}
	
	public void drawLeft(Canvas canvas) {

		canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth()/2, bitmap.getHeight()), new Rect(bitmapX, bitmapY, bitmapX+bitmap.getWidth()/2, bitmapY+bitmap.getHeight()), null);	
	}
	
	public void drawRight(Canvas canvas) {

		canvas.drawBitmap(bitmap, new Rect(bitmap.getWidth()/2, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(bitmapX+bitmap.getWidth()/2, bitmapY, bitmapX+bitmap.getWidth(), bitmapY+bitmap.getHeight()), null);	
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}
