package com.charredgames.chemify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.R;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.charredgames.chemify.MESSAGE";
	public static Spinner problem_type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		problem_type = (Spinner) findViewById(R.id.problem_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.problem_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		problem_type.setAdapter(adapter);

		
		Controller.reset(this.getAssets());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void submit(View view){
		Intent intent = new Intent(this, Response.class);
		EditText input = (EditText) findViewById(R.id.edit_input);
		intent.putExtra(EXTRA_MESSAGE, input.getText().toString());
		startActivity(intent);
	}
	
	
	public void onPause(){
		super.onPause();
	}
	
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}
	
	public void onResume(){
		super.onResume();
	}
	
}
