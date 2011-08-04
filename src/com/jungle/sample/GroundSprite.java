package com.jungle.sample;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class GroundSprite extends Sprite {

	private Boolean destroyable;
	
	public GroundSprite(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion, Boolean pDestroyable) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		destroyable = pDestroyable;
		// TODO Auto-generated constructor stub
	}

	public Boolean getDestroyable() {
		return destroyable;
	}

}

	