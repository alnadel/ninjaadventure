package com.jungle.shinobi;

import com.jungle.shinobi.MainGamePanel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
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
        //setContentView(new MainGamePanel(this, display.getWidth(), display.getHeight()));
        //setContentView(new MainMenuPanel(this, display.getWidth(), display.getHeight()));
        View view = new MainGamePanel(this, display.getWidth(), display.getHeight());
        setContentView(view);
        
        //Intent myIntent = new Intent(view.getContext(), MainMenuPanel.class);
        //startActivityForResult(myIntent, 0);
    }
    
    public void changeContentView(View content) {
    	setContentView(content);
    }
}