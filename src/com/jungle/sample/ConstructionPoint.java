package com.jungle.sample;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import com.badlogic.gdx.physics.box2d.Body;


public class ConstructionPoint extends Object{

	//private static ConstructionPoint instance;
	private AnimatedSprite face;
	private Body body;
	private boolean gravity;
	private AnimatedSprite connectionFace;
	
	ConstructionPoint(AnimatedSprite __face, Body __body, boolean __gravity, AnimatedSprite __connectionFace)
	{
		this.face = __face;
		this.body = __body;
		this.gravity = __gravity;
		this.connectionFace = __connectionFace;	
	}

	public AnimatedSprite getFace() {
		return face;
	}

	public void setFace(AnimatedSprite face) {
		this.face = face;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public boolean isGravity() {
		return gravity;
	}

	public void setGravity(boolean gravity) {
		this.gravity = gravity;
	}

	public AnimatedSprite getConnectionFace() {
		return connectionFace;
	}

	public void setConnectionFace(AnimatedSprite connectionFace) {
		this.connectionFace = connectionFace;
	}

	    
}
