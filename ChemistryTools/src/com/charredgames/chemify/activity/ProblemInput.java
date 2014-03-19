package com.charredgames.chemify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.charredgames.chemify.R;
import com.charredgames.chemify.util.Controller;

public class ProblemInput extends Activity{

	public String problemType;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		if(TestActivity.problemType == null && problemType == null) startActivity(new Intent(this, MainActivity.class));
		if(problemType == null){
			if(TestActivity.problemType == null) startActivity(new Intent(this, TestActivity.class));
			else problemType = TestActivity.problemType;
		}
		setTitle(problemType);

		setContentView(Controller.problemTypes.get(problemType).getInputView(this));
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		
	}
	
	public void submit(View view){
		if(Controller.problemTypes.get(problemType).getSubmit(this, view)){
			Intent intent = new Intent(this, Response.class);
			EditText input = (EditText) findViewById(R.id.edit_input);
			if(input.getText() == null || input.getText().toString() == null || input.getText().toString().equals("") || 
					input.getText().toString().equals(" ")) return;
			intent.putExtra("input", input.getText().toString());
			intent.putExtra("problem_type", problemType);
			startActivity(intent);
		}
	}
	
	
}
