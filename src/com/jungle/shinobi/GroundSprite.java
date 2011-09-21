package com.jungle.shinobi;

import com.jungle.sprite.HitArea;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class GroundSprite {

	private Bitmap bitmap;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	//private int[] hitarea = new int[4];
	private HitArea hitarea;
	private boolean destroyable;
	private boolean moveable;
	
	private int floor;
	Canvas wideBmpCanvas;
	private float scale;
	
	public GroundSprite(Bitmap bitmap, int x, int y, int width, int height, float scale, int floor, boolean destroyable, boolean moveable, int type){
		
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.width = (int)(width*scale);
		this.height = (int)(height*scale);
		//this.height = 64;
		this.destroyable = destroyable;
		this.moveable = moveable;
		this.floor = floor;
		this.scale = scale;
		
		int bitmapWidth = (int)(bitmap.getWidth()*scale);
		int bitmapHeight = (int)(bitmap.getHeight()*scale);
		
		int tileWidth = (int)(128*scale);
		int tileHeight = (int)(64*scale);
		
		this.bitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, true);
		
		switch(type){
			case 0:
				Log.d("GroundSprite", "Creating sprite "+floor+" Type: "+type+" Width: "+this.width);
				/*hitarea[0] = this.x;
				hitarea[1] = this.y;
				hitarea[2] = this.x+this.width;
				hitarea[3] = this.y+this.height;*/
				hitarea = new HitArea(this.x, this.y+(int)(36*scale), this.x+this.width, this.y+this.height);
				
				if(this.width>tileWidth){
					//Log.d("GroundSprite", "Creating sprite "+floor+". Width: "+width);
					int objects = (int) Math.floor(this.width/tileWidth);
					Bitmap[] bmp = new Bitmap[objects+1];
					//Log.d("GroundSprite", "Objects: "+objects);
					
					for(int i=0; i<objects; i++){
						
						bmp[i] = Bitmap.createBitmap(this.bitmap, 0, 0, tileWidth, tileHeight);
					}
					
					Log.d("GroundSprite", "Last Object: "+(this.width - (objects*tileWidth)));
					bmp[objects] = Bitmap.createBitmap(this.bitmap, 0, 0, (this.width - (objects*tileWidth)), tileHeight);
					
					Log.d("GroundSprite", "Assuming bitmaps...");
					this.bitmap = TiledBitmap(bmp);
				}else{
					Log.d("GroundSprite", "Cutting sprite, bmp height: "+bitmapHeight+" sprite: "+this.height);
					this.bitmap = Bitmap.createBitmap(this.bitmap, 0, 0, this.width, this.height);
					
				}
				
				this.bitmap = roundedCorners(this.bitmap);
				break;
		}
	}
	
	public Bitmap roundedCorners(Bitmap bitmap) {
		
		Bitmap output = Bitmap.createBitmap(width,
		        height, Config.ARGB_8888);
		    Canvas canvas = new Canvas(output);

		    final int color = 0xff424242;
		    final Paint paint = new Paint();
		    final Rect rect = new Rect(hitarea.getX1()-this.x, hitarea.getY1()-this.y-(int)(12*scale), width, hitarea.getY2()-this.y);
		    final RectF rectF = new RectF(rect);
		    final float roundPx = 8;

		    paint.setAntiAlias(true);
		    canvas.drawARGB(0, 0, 0, 0);
		    paint.setColor(color);
		    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, rect, rect, paint);

		    return output;
	}
	
	public Bitmap TiledBitmap(Bitmap srcBmps[]) {
	    Bitmap wideBmp;
	    Canvas wideBmpCanvas;
	    Rect src, dest;

		// assume all of the src bitmaps are the same height & width
	    wideBmp = Bitmap.createBitmap(srcBmps[0].getWidth() * srcBmps.length-1 + srcBmps[srcBmps.length-1].getWidth(), 
		        srcBmps[0].getHeight(), srcBmps[0].getConfig());

	    
	    wideBmpCanvas = new Canvas(wideBmp); 

	    int offset = 0;
	    for (int i = 0; i < srcBmps.length; i++) {
	         src = new Rect(0, 0, srcBmps[i].getWidth(), srcBmps[i].getHeight());
	         dest = new Rect(src); 
	         
	         //dest.offset(i * srcBmps[i].getWidth(), 0); 
	         dest.offset(offset, 0);
	         offset += srcBmps[i].getWidth();
	         wideBmpCanvas.drawBitmap(srcBmps[i], src, dest, null); 
	    }

	    return wideBmp;
	}

	
	public void draw(Canvas canvas) {
		/*Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.RED);*/
		
		canvas.drawBitmap(bitmap, x, y, null);
		
		
		//draw red rectangle as hitarea
		
		/*canvas.drawRect(getHitarea().getX1()
				, getHitarea().getY1()
				, getHitarea().getX2()
				, getHitarea().getY2()
				, paint);*/
	}
	
	public boolean collides(int x1, int y1, int x2, int y2) {
		
		if(y2 > hitarea.getY1() && y2 < hitarea.getY2() ) {
			if( (x1 > hitarea.getX1() && x1< hitarea.getX2()) || (x2 > hitarea.getX1() && x2< hitarea.getX2()) || (x1 < hitarea.getX1() && x2 > hitarea.getX2()) ){
				return true;
			}
		}
		return false;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isDestroyable() {
		return destroyable;
	}

	public void setDestroyable(boolean destroyable) {
		this.destroyable = destroyable;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public HitArea getHitarea() {
		return hitarea;
	}
		
}
