package com.jungle.shinobi;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BackgroundBitmap {

	private BitmapSprite[] bitmaps;
	
	public BackgroundBitmap( BitmapSprite[] arg ) {
		
		this.bitmaps = arg;
		
	}
	
	
	public void draw(Canvas canvas) {
		
		for(int i=0; i<this.bitmaps.length; i++) {
			canvas.drawBitmap(bitmaps[i].getBitmap(), bitmaps[i].getX(), bitmaps[i].getY(), null);
		}
	}
	
	public void scroll(int move) {
		
		for(int i=0; i<this.bitmaps.length; i++) {
			bitmaps[i].setY( bitmaps[i].getY()+move/(this.bitmaps.length-i) );
		}
	}
}
