package com.jungle.sample;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.batch.SpriteGroup;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * @author Marcin Œroda
 * @since 23:44:09 - 27.07.2011
 */
public class IcyTowerActivity extends BaseExample implements IAccelerometerListener, IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final int CAMERA_WIDTH = 480;
	protected static final int CAMERA_HEIGHT = 720;
	private static final String LOG_TAG = "Error";
	private static LevelObjects LEVEL_POOL;
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TimerHandler onEnterFrameTimer;
	//private Object mThis; 
	private Player mPlayer;
	private Body mPlayerBody;
	private Scene mScene;
	//private Level lvl;
	private LevelObjects lvlBody;
	public SpriteGroup ground;
	
	public float moveDistance = 0;
	
	protected TiledTextureRegion mBoxFaceTextureRegion;
	protected TiledTextureRegion mCircleFaceTextureRegion;

	protected PhysicsWorld mPhysicsWorld;
	
	private BitmapTextureAtlas mPlayerTexture;
	private TextureRegion mPlayerTextureRegion;

	
	private BitmapTextureAtlas mGround1Texture;
	private TextureRegion mGround1TextureRegion;
	
	
	private BitmapTextureAtlas mRacetrackTexture;
	private TextureRegion mRacetrackStraightTextureRegion;
	private TextureRegion mRacetrackCurveTextureRegion;

	private BitmapTextureAtlas mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;
	
	protected boolean gameIsStarted = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		//mThis = this;
		Toast.makeText(this, "Jump so high!", Toast.LENGTH_LONG).show();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		//return new Engine(engineOptions);
		Engine engine = new Engine(engineOptions);   
		// Attempt to set up multitouch support  
		if (MultiTouch.isSupported(this) && (MultiTouch.isSupported(this))) {  
		    try {  
		        engine.setTouchController(new MultiTouchController());  
		    } catch (MultiTouchException e) {  
		        Log.e(LOG_TAG, "Error with multitouch initialization", e);  
		    }  
		}  
		
		return engine;
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		

		this.mOnScreenControlTexture = new BitmapTextureAtlas(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);

		this.mPlayerTexture = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mPlayerTexture, this, "box.png", 0, 0);

		LEVEL_POOL = new LevelObjects(mPlayerTextureRegion, this);
		//this.mEngine.getTextureManager().loadTextures(this.mVehiclesTexture, this.mRacetrackTexture);
		this.mEngine.getTextureManager().loadTextures(this.mBitmapTextureAtlas, this.mOnScreenControlTexture, this.mPlayerTexture);
	}

	@Override
	public Scene onLoadScene() {
		
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setBackground(new ColorBackground(1, 1, 1));
		this.mScene.setOnSceneTouchListener(this);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		//PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		this.mScene.attachChild(ground);
		this.mScene.attachChild(roof);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);

		this.createLevel();
		this.initOnScreenControls();
		gameIsStarted = true;
		
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);

		return this.mScene;
	}

	@Override
	public void onLoadComplete() {
		
		/*this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	mPlayer.gravity();
            }
		});*/
		onEnterFrameTimer = new TimerHandler(1 / 24.0f, true, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				try {
					mPlayer.gravity();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//if(e.getMessage().equals("Game Over"))
					//{
					//	Toast.makeText(BasePhysicsJointExample.this, "Error unlocking achievement.", Toast.LENGTH_SHORT).show();
					//}
					//showMessage("Game Over!");
					gameIsStarted = false;
					mScene.unregisterUpdateHandler(onEnterFrameTimer);
				}
				
				//check collision
				if(mPlayer.getJumpHeight()<0)
				{
					LEVEL_POOL.collisionWithGround(mPlayer, ground.getY());
					
					if(gameIsStarted)
						moveDistance = LEVEL_POOL.moveDistance();
					else
						moveDistance = 0;
				}else{
					if(mPlayer.getY() < CAMERA_HEIGHT/4)
					{
						ground.setPosition(ground.getX(), ground.getY()+mPlayer.getJumpHeight());
						mPlayer.setPosition(mPlayer.getX(), mPlayer.getY()+mPlayer.getJumpHeight());
						//mPlayer.jump();
					}
				}
				
				/*
				 * Moving platform
				 */
				
				ground.setPosition(ground.getX(), ground.getY()+moveDistance);
				mPlayer.setPosition(mPlayer.getX(), mPlayer.getY()+moveDistance);
			}
    });
		this.mScene.registerUpdateHandler(onEnterFrameTimer);
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		mPlayer.jump();
		return true;
	}

	@Override
	public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerometerData.getX(), pAccelerometerData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();

		this.enableAccelerometerSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerometerSensor();
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	private void initOnScreenControls() {
		System.out.println("Analog Height: "+this.mOnScreenControlBaseTextureRegion.getHeight());
		//final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(60, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, new IAnalogOnScreenControlListener() {
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(20, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight()-20, this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				//final Body carBody = IcyTowerGameActivity.this.mCarBody;
				
				/*
				 * TODO: only 2 directions, pValueY returns value [-1:1]
				 */
				/*final Vector2 velocity = Vector2Pool.obtain(pValueX * 5, 0);
				//final Vector2 velocity = Vector2Pool.obtain(pValueX * 5, 0);
				mPlayerBody.setLinearVelocity(velocity);
				Vector2Pool.recycle(velocity);*/
				if(pValueX > 0)
				{
					mPlayer.moveRight();
				}else if(pValueX < 0){
					mPlayer.moveLeft();
				}else{
					mPlayer.stand();
				}
				
				if(pValueY<-0.25f)
				{
					mPlayer.jump();					
				}

			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
				/* Nothing. */
			}
		});
		analogOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.getControlBase().setScale(1.5f);
		analogOnScreenControl.getControlKnob().setScale(1.5f);
		analogOnScreenControl.refreshControlKnobPosition();
		
		this.mScene.setChildScene(analogOnScreenControl);
	}
	
	private void createLevel() {

		this.ground = new SpriteGroup(mPlayerTexture, 50);
		
		for(int i=0; i<5; i++)
		{
			this.ground.attachChild(getGroundElementFromPool());
		}
		this.mScene.attachChild(this.ground);
		
		this.addPlayer(CAMERA_WIDTH/2, CAMERA_HEIGHT-64-120);
	}
	
	private void addPlayer(final float pX, final float pY) {
		this.mPlayer = new Player(pX, pY, 32, 64, this.mPlayerTextureRegion, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mScene.attachChild(mPlayer);
		
	}

		 /**
		 * Called because the bullet can be recycled
		 */
	public void sendGroundElementToPool(GroundSprite pGroundSprite) {
		LEVEL_POOL.recyclePoolItem(pGroundSprite);
	}

		 /**
		 * The player fired, we need a bullet to display
		 */
	public GroundSprite getGroundElementFromPool() {
		 return LEVEL_POOL.obtainPoolItem();
	}
		
	private void showMessage(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
