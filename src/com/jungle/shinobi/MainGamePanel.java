/**
 * 
 */
package com.jungle.shinobi;

import com.jungle.shinobi.R;
import com.jungle.shinobi.R.drawable;

import com.jungle.buttons.TwoSidedPad;
import com.jungle.shinobi.sprite.AnimatedSprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;

/**
 * @author impaler
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	private MainThread thread;
	public 	Player player;
	private TwoSidedPad pad;

	public 	int stageWidth = 320;
	public 	int stageHeight = 470;
	public float scale = 1;
	
	private boolean isTouched = false;
	private float lastX = 0;
	private float lastY = 0;
	
	private int pwidth = 0; 
	private int	pheight = 0;
	// the fps to be displayed
	private String avgFps;
	
	private Game game;
	
	
	public void setAvgFps(String avgFps) {
		this.avgFps = avgFps;
	}

	public int getPwidth() {
		return pwidth;
	}

	public void setPwidth(int pwidth) {
		this.pwidth = pwidth;
	}

	public int getPheight() {
		return pheight;
	}

	public void setPheight(int pheight) {
		this.pheight = pheight;
	}

	public MainGamePanel(Context context, int width, int height) {
		super(context);
		
		this.stageWidth = width;
		this.stageHeight = height;
		
		double scaleP = 1;
		int bwidth = 0, bheight = 0;
		
		
		scale = (float)stageWidth/(BitmapFactory.decodeResource(getResources(), R.drawable.top_bar)).getWidth();
		
		if(scale >= 1/3) {
			//scaleP = 0.75;
			pwidth 		= (int)(64*.9);
			pheight 	= (int)(128*.9);
			bwidth 		= 8*pwidth; 
			bheight 	= 5*pheight;
		}
		
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.player);
		player = new Player(
				Bitmap.createScaledBitmap(bmp, bwidth, bheight, true)
				, stageWidth/2, 200					// initial position
				, pwidth, pheight	// width and height of sprite
				, 0									// current row of animation
				, 5, 8								// FPS and number of frames in the animation
				, stageWidth, stageHeight);// Scene width and height
		
		pad = new TwoSidedPad(
				BitmapFactory.decodeResource(getResources(), R.drawable.vpad)
				, stageWidth/2 - 115, stageHeight-100-10 );
		

		game = new Game(BitmapFactory.decodeResource(getResources(), R.drawable.top_bar), 0, 0, BitmapFactory.decodeResource(getResources(), R.drawable.terrain), stageWidth, stageHeight);
		
		thread = new MainThread(getHolder(), this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");

		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {

			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			lastX = event.getX();
			lastY = event.getY();
			isTouched = true;
		}else if (event.getAction() == MotionEvent.ACTION_MOVE){
			lastX = event.getX();
			lastY = event.getY();
		}else if (event.getAction() == MotionEvent.ACTION_UP){
			isTouched = false;
			pad.activeDir = 0;
			player.stand();
		}
		return true;
	}
	
	
	public void collides(float x, float y) {
		
		if(y > stageHeight-100-10) {
			if(x < stageWidth/2) {
			    player.moveLeft();
			    pad.activeDir = 1;
			}else{
		    	player.moveRight();
		    	pad.activeDir = 2;
			}
		}else{
			pad.activeDir = 0;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     switch (keyCode) {
	     case KeyEvent.KEYCODE_DPAD_LEFT:
	        player.moveLeft();
	        return true;
	     case KeyEvent.KEYCODE_DPAD_RIGHT:
	    	 player.moveRight();
	    	 return true;
	     }
	     
	     Log.d(TAG, "Another key pressed");
	     return false;
	} 

	public void render(Canvas canvas) {

		canvas.drawColor(Color.rgb(250, 205, 25));
		
		game.draw(canvas);
		if(player.isJumping() && player.getJumpHeight() < 0){
			
			
			GroundSprite collidedWith = game.getGrounds().collisionDetection(player.getX(), player.getY(), pwidth, pheight);
		
			if(collidedWith != null) {
				player.stopFalling();
				//zmienic na hitarea
				player.setY(collidedWith.getY()-pheight);
				player.jump();
			}
		}
		
		player.draw(canvas);
		try {
			player.gravity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		pad.draw(canvas);
		displayFps(canvas, avgFps);
	}

	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {
		
		if(isTouched) {
			collides(lastX, lastY);
		}

		player.update(System.currentTimeMillis());
	}

	private void displayFps(Canvas canvas, String fps) {
		
		if (canvas != null && fps != null) {
			Paint paint = new Paint();
			paint.setARGB(255, 255, 255, 255);
			canvas.drawText(fps, this.getWidth() - 50, 20, paint);
		}
	}

}
