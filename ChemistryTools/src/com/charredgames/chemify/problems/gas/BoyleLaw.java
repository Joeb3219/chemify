package com.charredgames.chemify.problems.gas;

import android.app.Activity;
import android.os.Bundle;

import com.charredgames.chemify.R;
import com.charredgames.chemify.util.Controller;

public class BoyleLaw extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Controller.resetIfNeeded(this);
		setTitle(Controller.resources.getString(R.string.problem_boyle_law));
		setContentView(R.layout.boylelaw);
	}
	
	
	
}
