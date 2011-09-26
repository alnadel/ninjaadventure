package com.jungle.shinobi;

import com.jungle.shinobi.sprite.AnimatedSprite;

import android.graphics.Bitmap;
import android.util.Log;


public class Player extends AnimatedSprite {

	private static float speedStep = 1f;
	private static float jumpStep = 1f;
	private static float maxSpeed = 10;
	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	private boolean isJumping = false;
	private float speed = 0;
	private float jumpHeight = 0;
	private float jumpScale = 0.1f;
	
	private int sceneWidth = 0;
	private int sceneHeight = 0;
	
	
	public Player(Bitmap bitmap, int x, int y, int width, int height, int row, int fps, int frameCount, int pSceneWidth, int pSceneHeight) {
		
		super(bitmap, x, y, width, height, row, fps, frameCount);
		this.sceneWidth = pSceneWidth;
		this.sceneHeight = pSceneHeight;
	}

	@Override
	public void stopAnimation() {
		
	}
	
	public void moveLeft()
	{
		this.move(-1);
		
		if(!isJumping())
			this.setRow(1);
		else{
			this.setRow(3);
		}
		
		this.speed = (this.speed > 0)?(0):(this.speed);
		this.speed = (this.speed > -(Player.maxSpeed - Player.speedStep))?(this.speed - Player.speedStep):(-Player.maxSpeed);

	}
	
	public void moveRight()
	{
		this.move(1);
		
		if(!isJumping())
			this.setRow(2);
		else{
			this.setRow(4);
		}
		this.speed = (this.speed < 0)?(0):(this.speed);
		this.speed = (this.speed < (Player.maxSpeed - Player.speedStep))?(this.speed + Player.speedStep):(this.maxSpeed);
	}
	
	public void stand()
	{
		this.speed = 0;
		this.setRow(0);
	}
	
	public void move(int direction)
	{
		final float distanceX = (this.getX()+this.speed);

//		final float jumpDistance = this.jumpHeight -= jumpStep;
//		float distanceY = (isJumping)?(this.getY()+jumpDistance):(this.getY());

		this.setX((int) ( (this.sceneWidth+distanceX+32)%(this.sceneWidth)) -32);
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
			this.setY((int) this.sceneHeight); 
			stopFalling();
			throw new Exception("Game Over");
			
		}else if(isJumping()){
			final float jumpDistance = this.jumpHeight -= jumpStep;
			float distanceY = (isJumping)?(this.getY()-jumpDistance):(this.getY());
			this.setY((int) distanceY);
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
		
		//jump();
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
