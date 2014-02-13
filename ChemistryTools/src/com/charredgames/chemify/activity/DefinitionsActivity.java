package com.charredgames.chemify.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Definition;

public class DefinitionsActivity extends Activity{

	private String search = "";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		setContentView(R.layout.activity_definitions);
		
		EditText textBox = (EditText)findViewById(R.id.search_box);
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
		LinearLayout container = (LinearLayout)this.findViewById(R.id.definitions_results);
		container.removeAllViews();
		boolean firstBox = true;
		
		for(Definition def : Definition.definitions){
			if(!def.containsWord(search)) continue;
			TextView defBox;
			if(firstBox) defBox = (TextView)getLayoutInflater().inflate(R.layout.firstboxtemplate, null);
			else defBox = (TextView)getLayoutInflater().inflate(R.layout.secondboxtemplate, null);
				
			defBox.setText(Html.fromHtml(def.getDrawString()));
			
			container.addView(defBox);
			
			firstBox = !firstBox;
		}
	}
	
	
	
	public void responseClicked(View view){
	}
	
}
