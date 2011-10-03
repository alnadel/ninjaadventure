package com.jungle.shinobi;

import com.jungle.buttons.TwoSidedPad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainMenuPanel extends SurfaceView implements
		SurfaceHolder.Callback, MainPanelInterface {

	private static final String TAG = MainGamePanel.class.getSimpleName();

	private MainThread thread;
	private Activity activity;
	public Bitmap menuBitmap;

	public int stageWidth = 0;
	public int stageHeight = 0;
	public float scale = 1;

	private boolean isTouched = false;
	private float lastX = 0;
	private float lastY = 0;

	// the fps to be displayed
	private String avgFps;

	private Game game;

	public void setAvgFps(String avgFps) {
		this.avgFps = avgFps;
	}

	public MainMenuPanel(Activity context, int width, int height) {
		super(context);

		this.activity = context;
		this.stageWidth = width;
		this.stageHeight = height;

		double scaleP = 1;
		int bwidth = 0, bheight = 0;

		/*scale = (float) stageWidth
				/ (BitmapFactory.decodeResource(getResources(),
						R.drawable.top_bar)).getWidth();
*/

		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.menu);
		
		scale = (float) stageWidth/bmp.getWidth();
		menuBitmap = Bitmap.createScaledBitmap(bmp, stageWidth, stageHeight,true);
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

		/*if (event.getAction() == MotionEvent.ACTION_DOWN) {
			lastX = event.getX();
			lastY = event.getY();
			isTouched = true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			lastX = event.getX();
			lastY = event.getY();
		} else */
		if (event.getAction() == MotionEvent.ACTION_UP) {
			isTouched = false;
			lastX = event.getX();
			lastY = event.getY();
			return collides(lastX, lastY);
			
		}
		//invalidate();
		return true;
	}

	public boolean collides(float x, float y) {

		/*if (y > stageHeight - 100 - 10) {
			if (x < stageWidth / 2) {
				player.moveLeft();
				pad.activeDir = 1;
			} else {
				player.moveRight();
				pad.activeDir = 2;
			}
		} else {
			pad.activeDir = 0;
		}*/
		int __x = (int)(x/scale);
		int __y = (int)(y/scale);
		
		Log.d(TAG, "Current Y: "+__y);
		if(__y > 320 && __y < 420) {
			//Play
			//activity.setContentView(new MainGamePanel(activity, this.stageWidth, this.stageHeight) );
			Intent myIntent = new Intent(this.getContext(), GameActivity.class);
	        activity.startActivityForResult(myIntent, 0);
	        thread.setRunning(false);
	        return false;
			 //thread.setGamePanel(new MainGamePanel(activity, this.stageWidth, this.stageHeight));
			//new MainGamePanel(activity, this.stageWidth, this.stageHeight);
		}else if(__y > 440 && __y < 540) {
			//Settings
		}else if(__y > 560 && __y < 660) {
			//Exit
			activity.finish();
		}
		
		return true;
	}

	/*@Override
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
	}*/

	public void render(Canvas canvas) {

		canvas.drawColor(Color.rgb(250, 205, 25));
		
		canvas.drawBitmap(menuBitmap, 0, 0, null);
		displayFps(canvas, avgFps);
	}

	public void update() {
		Log.d(TAG, TAG);
		/*if (isTouched) {
			collides(lastX, lastY);
		}

		player.update(System.currentTimeMillis());*/
	}

	private void displayFps(Canvas canvas, String fps) {

		if (canvas != null && fps != null) {
			Paint paint = new Paint();
			paint.setARGB(255, 0, 0, 0);
			canvas.drawText(fps, this.getWidth() - 50, 20, paint);
		}
	}


}
