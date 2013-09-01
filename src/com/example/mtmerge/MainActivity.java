package com.example.mtmerge;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window win = getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);	
		setContentView(R.layout.activity_main);
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_titlebar);
       
        
		
		ImageButton gogrouplist = (ImageButton)findViewById(R.id.btn_main_grouplist);
		gogrouplist.setOnClickListener(new ImageButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				// go to grouplist
				Intent intent_grouplist = new Intent(MainActivity.this, GroupListActivity.class);
    			startActivity(intent_grouplist);
    		
			}	
			
		}
		);
		
		ImageButton goinfo = (ImageButton)findViewById(R.id.btn_main_info);
		goinfo.setOnClickListener(new ImageButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				// go to info
				Intent intent = new Intent(MainActivity.this, InfoActivity.class);
    			startActivity(intent);
    		
			}	
			
		}
		);
    
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
