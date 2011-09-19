package com.jungle.shinobi;

import com.jungle.shinobi.MainGamePanel;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class ShinobiTowerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        Display display = getWindowManager().getDefaultDisplay(); 
		//int width = display.getWidth();
		//int height = display.getHeight();
        setContentView(new MainGamePanel(this, display.getWidth(), display.getHeight()));
    }
}