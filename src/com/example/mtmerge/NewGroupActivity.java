package com.example.mtmerge;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class NewGroupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newgroup);
	}
	
	public void go_btn_newgroup_finish (View v) {

		String groupnameStr = ((EditText)findViewById(R.id.et_newgroup_groupname)).getText().toString();
		String boyStr = ((EditText)findViewById(R.id.et_newgroup_boy)).getText().toString();
		String girlStr = ((EditText)findViewById(R.id.et_newgroup_girl)).getText().toString();
		String placeStr = ((EditText)findViewById(R.id.et_newgroup_place)).getText().toString();

		Intent intent_grouplist = new Intent();
	
		intent_grouplist.putExtra("groupname", groupnameStr);
	    intent_grouplist.putExtra("boy", boyStr);
	    intent_grouplist.putExtra("girl", girlStr);
	    intent_grouplist.putExtra("place", placeStr);
	    setResult(RESULT_OK, intent_grouplist);
	    
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_group, menu);
		return true;
	}
	

	public void keyboard_off_layout (View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}

}
