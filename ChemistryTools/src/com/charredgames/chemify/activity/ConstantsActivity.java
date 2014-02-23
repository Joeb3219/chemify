package com.charredgames.chemify.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Constant;

public class ConstantsActivity extends Activity{

	private String search = "";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.resources_search_activity);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		EditText textBox = (EditText)findViewById(R.id.search_box);
		textBox.setHint("speed of light");
		textBox.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				if(s != null){
					search = s.toString();
					fillResults();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
			
		});
		
		fillResults();
	}
	
	private void fillResults(){
		LinearLayout container = (LinearLayout)this.findViewById(R.id.search_results);
		container.removeAllViews();
		boolean firstBox = true;
		
		for(Constant constant : Constant.constants){
			if(!constant.matchesSearch(search)) continue;
			TextView constantBox;
			if(firstBox) constantBox = (TextView)getLayoutInflater().inflate(R.layout.firstboxtemplate, null);
			else constantBox = (TextView)getLayoutInflater().inflate(R.layout.secondboxtemplate, null);
				
			constantBox.setText(constant.getDrawString());
			
			container.addView(constantBox);
			
			firstBox = !firstBox;
		}
	}
	
	public void responseClicked(View view){
	}	
}
