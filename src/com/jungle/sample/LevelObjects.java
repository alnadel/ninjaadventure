package com.jungle.sample;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.pool.GenericPool;

public class LevelObjects extends GenericPool<GroundSprite> {

	protected static final int CAMERA_WIDTH = 480;
	protected static final int CAMERA_HEIGHT = 600;
	
	private int currentGround = 0;
	private int currentLevel = 30;
	private static int ALL_LEVELS = 30;
	private int platformStep = 150;
	
	
	private int lastPositionX = (int) (Math.random()*CAMERA_WIDTH);
	private int lastPositionY = CAMERA_HEIGHT;
	
	private TextureRegion mTextureRegion;
	private ArrayList objects = new ArrayList<GroundSprite>();

	private IcyTowerActivity mScene;
	
	 public LevelObjects(TextureRegion pTextureRegion, IcyTowerActivity scene) {
		  if (pTextureRegion == null) {
		   // Need to be able to create a Sprite so the Pool needs to have a TextureRegion
		   throw new IllegalArgumentException("The texture region must not be NULL");
		  }
		  mScene = scene;
		  this.mTextureRegion = pTextureRegion;
	 }

		 /**
		 * Called when a Bullet is required but there isn't one in the pool
		 */
		 @Override
		 protected GroundSprite onAllocatePoolItem() {

			 float spriteWidth;
			 float posX;
			 
			 if(currentGround % 10 == 0)
			 {
				 spriteWidth = CAMERA_WIDTH*2;
				 
				 posX = (-CAMERA_WIDTH);
				 
			 }else{
				 spriteWidth = (float) ((CAMERA_WIDTH*0.4f) - currentLevel*(Math.random()*7.5f));
				 
				 posX = (float) (lastPositionX + currentLevel*(Math.random()*ALL_LEVELS));
				 
				 posX = posX % (CAMERA_WIDTH-spriteWidth);
				 
				 lastPositionX = (int) posX;
			 }
			 
			 Boolean destroyable;
			 if((-15+100*currentLevel/ALL_LEVELS) < (Math.random()*100))
			 {
				 destroyable = false;
			 }else{
				 destroyable = true;
			 }
			 
			 final GroundSprite sprite = new GroundSprite(posX, lastPositionY, spriteWidth, mTextureRegion.getHeight(), mTextureRegion, destroyable);
			 objects.add(sprite);
			 
			 currentGround++;
			 lastPositionY -= platformStep+randomRange(currentLevel/3, currentLevel/3);
			 return sprite;
			 //return new GroundSprite(posX, lastPositionY, spriteWidth, mTextureRegion.getHeight(), mTextureRegion);
		 }

		 /**
		  * Called when a Bullet is sent to the pool
		 */
		 @Override
		 protected void onHandleRecycleItem(final GroundSprite pObject) {
			 pObject.setIgnoreUpdate(true);
			 pObject.setVisible(false);
		 }

		 /**
		  * Called just before a Bullet is returned to the caller, this is where you write your initialize code
		  * i.e. set location, rotation, etc.
		 */
		 @Override
		 protected void onHandleObtainItem(final GroundSprite pObject) {
			 pObject.reset();
		 }

		public ArrayList getObjects() {
			return objects;
		}
		 
		 
		public boolean collisionWithGround(final Player player, float ground_move)
		{
			// trzymanie obiektów na wysokoœæ 2x CAMERA_HEIGHT
			
			for(int i=0; i<objects.size(); i++)
			{
				if(player.collidesWith((IShape) objects.get(i)))
				{
					final float collisionY = ((GroundSprite) objects.get(i)).getY()+ground_move;
					if((player.getY() + player.getHeightScaled() - ((GroundSprite) objects.get(i)).getHeightScaled()) <= collisionY )
					{
						player.stopFalling();
						player.setPosition(player.getX(), collisionY-player.getHeightScaled());
						
						
						
						/* DESTROYABLE */
						if(( (GroundSprite) objects.get(i)).getDestroyable() )
						{
							
							Timer timer = new Timer();
							timer.schedule(new java.util.TimerTask() {
							            @Override
							            public void run() {
							            	//destroy
							            	onHandleRecycleItem((GroundSprite) objects.get(0));
							            	//fall player
							            	player.startFalling();
							            }
							        }, 500);
							
						
						}
						
						int count = 0;
						while( ((GroundSprite) (objects.get(0))).getY()+ground_move > CAMERA_HEIGHT )
						{
							//System.out.println("Removing Ground Y: ");
							onHandleRecycleItem((GroundSprite) objects.get(0));
							objects.remove(0);
							
							
							mScene.ground.attachChild(mScene.getGroundElementFromPool());
						}
					
						return true;
					}
				}
			}
			return false;
		}
		 
		public double randomRange(float min, float max)
		{
			return (Math.random()*(max - min)) + min;
		}
		 
		public float moveDistance()
		{
			return currentLevel * 0.1f;
		}

		public int getCurrentLevel() {
			return currentLevel;
		}

		public void setCurrentLevel(int currentLevel) {
			this.currentLevel = currentLevel;
		}
}
