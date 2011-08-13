package com.jungle.sample;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Player extends AnimatedSprite {

	private static float speedStep = 2f;
	private static float jumpStep = 1f;
	private static float maxSpeed = 35;
	
	
	private boolean isJumping = false;
	private float speed = 0;
	private float jumpHeight = 0;
	private float jumpScale = 0.2f;
	
	private int sceneWidth = 0;
	private int sceneHeight = 0;
	
	public Player(float pX, float pY,
					TiledTextureRegion pTextureRegion, int pSceneWidth, int pSceneHeight) {
		
		super(pX, pY, pTextureRegion);
		// TODO Auto-generated constructor stub
		this.sceneWidth = pSceneWidth;
		this.sceneHeight = pSceneHeight;
		
		//this.animate(new long[] { 100, 100 }, 1, 8, true);
		this.animate(new long[] { 1, 1, 1, 1, 1, 1, 1, 1 }, 1, 8, true);
		
		System.out.println("Player W: "+this.getWidth()+":"+this.getWidthScaled()+" H: "+this.getHeight()+":"+this.getHeightScaled());
		//final AnimatedSprite helicopter = new AnimatedSprite(pX, pY, this.mPlayerTR);
		//helicopter.animate(new long[] { 100, 100 }, 1, 8, true);
		//helicopter.animate(new long[] { 1, 1, 1, 1, 1, 1, 1, 1 }, 1, 8, true);
		//this.mScene.attachChild(helicopter);
	}

	public void moveLeft()
	{
		//this.stopAnimation(); 
		//this.setCurrentTileIndex(1, 3);
		this.animate(new long[] { 1, 1, 1, 1, 1, 1, 1, 1 }, 8, 15, true);
		this.move(-1);
		this.speed = (this.speed > 0)?(0):(this.speed);
		this.speed = (this.speed > -(Player.maxSpeed - Player.speedStep))?(this.speed - Player.speedStep):(-Player.maxSpeed);
		//this.speed += speedStep;		
	}
	
	public void moveRight()
	{
		this.move(1);
		//this.stopAnimation();
		//this.setCurrentTileIndex(1, 2);
		//this.animate()
		this.animate(new long[] { 1, 1, 1, 1, 1, 1, 1, 1 }, 16, 23, true);
		this.speed = (this.speed < 0)?(0):(this.speed);
		this.speed = (this.speed < (Player.maxSpeed - Player.speedStep))?(this.speed + Player.speedStep):(this.maxSpeed);
		//this.speed += speedStep;
	}
	
	public void stand()
	{
		this.speed = 0;
		this.animate(new long[] { 1, 1, 1, 1, 1, 1, 1, 1 }, 1, 8, true);
	}
	
	public void move(int direction)
	{
		final float distanceX = (this.getX()+this.speed);
		
//		final float jumpDistance = this.jumpHeight -= jumpStep;
//		float distanceY = (isJumping)?(this.getY()+jumpDistance):(this.getY());
		
		this.setPosition((this.sceneWidth+distanceX)%this.sceneWidth, this.getY());
		
	}
	
	public void jump()
	{
		if(!isJumping())
		{
			isJumping = true;
			this.jumpHeight = Math.round(this.speed*this.jumpScale + 20);
			//System.out.println("Jump!");
		}
	}
	
	public void gravity() throws Exception
	{
		//System.out.println("Gravity is working! Current position: "+this.getX());
		if(this.getY() > this.sceneHeight)
		{
			this.setPosition(this.getX(), this.sceneHeight); 
			stopFalling();
			throw new Exception("Game Over");
			
		}else if(isJumping()){
			final float jumpDistance = this.jumpHeight -= jumpStep;
			float distanceY = (isJumping)?(this.getY()-jumpDistance):(this.getY());
			this.setPosition(this.getX(), distanceY);
			//System.out.println(">>> Jumping: "+this.jumpHeight);
		}/*else{
			isJumping = s.
		}*/
	}
	
	public void startFalling()
	{
		isJumping = true;
		//this.jumpHeight = 0;
	}
	
	public void stopFalling()
	{
		isJumping = false;
		this.jumpHeight = 0;
	}
	
	public boolean isJumping() {
		return isJumping;
	}

	private void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public float getSpeed() {
		return speed;
	}

	private void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getJumpHeight() {
		return jumpHeight;
	}

	private void setJumpHeight(float jumpHeight) {
		this.jumpHeight = jumpHeight;
	}

}
