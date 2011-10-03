package com.jungle.shinobi;

import android.graphics.Canvas;

public interface MainPanelInterface {

	public void render(Canvas canvas);
	public void update();
	public void setAvgFps(String avgFps);
}
