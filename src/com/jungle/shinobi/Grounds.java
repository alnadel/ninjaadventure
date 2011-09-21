package com.jungle.shinobi;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Grounds {

	
	private int floors = 1;
	private int minWidth;
	private int maxWidth;
	private int minStepY;
	private int maxStepY;
	private int maxGroundPerFloor;
	private int height = 64;
	private int level;
	private float scale;
	private static int ALL_LEVELS = 25;
	private int stageWidth;
	private int stageHeight;
	private ArrayList<GroundSprite> groundsArray = new ArrayList<GroundSprite>();
	private Bitmap bitmap;

	
	public Grounds(Bitmap bitmap, int x, int y, int lvl, float scale, int stageWidth, int stageHeight) {
		
		this.bitmap = bitmap;
		
		if(lvl < 5){
			//this.bitmap = Bitmap.createBitmap(bitmap, 0, (int)(0*scale), (int)(128*scale), (int)(height*scale));
			this.bitmap = Bitmap.createBitmap(bitmap, 0, (int)(0), (int)(128), (int)(height));
		}
		this.minWidth = (int) (200*scale);
		this.maxWidth = (int) (400*scale);
		
		this.minStepY = (int) (150*scale);
		this.maxStepY = (int) (300*scale);
		
		this.maxGroundPerFloor = 1;
		
		this.level = lvl;
		this.stageWidth = stageWidth;
		this.stageHeight = stageHeight;
		
		this.scale = scale;
		//GroundSprite object = new GroundSprite(bitmap, (int)(x*scale), (int)(y-height*scale), stageWidth, stageHeight, false, false, 0);
		GroundSprite object = new GroundSprite(bitmap, (int)(x*scale), (int)(y-height*scale), (int)(stageWidth/scale), stageHeight, scale, floors++, false, false, 0);
		groundsArray.add(object );
		
		update();
	}
	
	public GroundSprite collisionDetection(int x, int y, int width, int height){
		
		int i=0;
		
		try{
			while( groundsArray.get(i).getY()+64 >= y+height) {
		
			
				Log.d("Grounds", "Player Y: "+(y+height));
				Log.d("Grounds", "Testing ground "+i+" Y: "+groundsArray.get(i).getY());
				if( groundsArray.get(i).collides(x, y, x+width, y+height) ){
					this.update();
					Log.d("Grounds", "Collides with Y: "+groundsArray.get(i).getY());
					return (GroundSprite) groundsArray.get(i);
				}
				i++;
				/*if(groundsArray.size() > i) {
					i++;
				}else{
					break;
				}*/
			}
		}catch(Error e){};
		return null;
		
	}
	
	public void update() {
		
		//removing
		try{
			while( groundsArray.get(0).getY() > stageHeight) {
				groundsArray.remove(0);
			}
		}catch(Error e){};
		
		//creating new
		while(groundsArray.get(groundsArray.size()-1).getY() > -stageHeight ) {
			Log.d("Grounds", "Creating floor..."+floors);
			int width = (int)( maxWidth-((maxWidth-minWidth)*Math.random()*(level+1)));//ALL_LEVELS));
			
			Log.d("Grounds", "Width: "+width);
			//int height = height;
			
			int x = (int) ( (groundsArray.get(groundsArray.size()-1).getX() + (level+1)*Math.random()*ALL_LEVELS))%(stageWidth-width);
			Log.d("Grounds", "X: "+x);
			int y = groundsArray.get(groundsArray.size()-1).getY()-minStepY;
			
			boolean destroyable = false;
			boolean moveable = false;
			int type = 0;
			
			GroundSprite object = new GroundSprite(bitmap, x, y, (int)(width), (int)(height), scale, floors++, destroyable, moveable, type);
			groundsArray.add(object );
		}
	}
	
	public void moveGrounds(int y) {
		for(int i=0; i<groundsArray.size(); i++) {
			((GroundSprite) groundsArray.get(i)).setY(groundsArray.get(i).getY()+y);
			//move hitarea
			//((GroundSprite) groundsArray.get(i)).getHitarea().update(0, y);
			groundsArray.get(i).getHitarea().setY1(groundsArray.get(i).getHitarea().getY1()+y);
			groundsArray.get(i).getHitarea().setY2(groundsArray.get(i).getHitarea().getY2()+y);
		}
		
	}
	
	
	public void draw(Canvas canvas){
		
		
		for(int i=0; i<groundsArray.size(); i++) {
			((GroundSprite) groundsArray.get(i)).draw(canvas);

		}
		
	}
}
