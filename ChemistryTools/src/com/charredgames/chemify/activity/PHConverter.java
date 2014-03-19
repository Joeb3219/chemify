package com.charredgames.chemify.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.charredgames.chemify.R;
import com.charredgames.chemify.util.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Mar 16, 2014
 */
public class PHConverter extends Activity{

	private boolean canCalculate = true;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setTitle(Controller.resources.getString(R.string.problem_ph_converter));
		setContentView(R.layout.phconverter);
	}
	
	public void onStart(){
		super.onStart();
		((EditText) findViewById(R.id.value_ph)).addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable arg0) {
				calculate(((EditText) findViewById(R.id.value_ph)).getText().toString(), "ph");
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		((EditText) findViewById(R.id.value_ph_concentration)).addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable arg0) {
				calculate(((EditText) findViewById(R.id.value_ph_concentration)).getText().toString(), "[h]");
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		((EditText) findViewById(R.id.value_poh)).addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable arg0) {
				calculate(((EditText) findViewById(R.id.value_poh)).getText().toString(), "poh");
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		((EditText) findViewById(R.id.value_poh_concentration)).addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable arg0) {
				calculate(((EditText) findViewById(R.id.value_poh_concentration)).getText().toString(), "[poh]");
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}
	
	private void calculate(String val, String edited){
		if(!canCalculate) return;
		canCalculate = false;
		double value = 1.00;
		try{
			if(!val.equals("Infinity") && !val.equals("-Infinity") && !val.equals("") && !val.equals(" ") && !val.equals(".") && !val.equals("-") && !val.equals("-.") && !val.equals(".-")) value = Double.parseDouble(val);
		}catch(NumberFormatException e){
			value = 1.00;
		}
		double phValue = 0.00;
		if(edited.equalsIgnoreCase("ph")) phValue = value;
		else if(edited.equalsIgnoreCase("[h]")){
			phValue = -Math.log(value);
		}else if(edited.equalsIgnoreCase("poh")){
			phValue = 14 - value;
		}else{
			phValue = 14 - (-Math.log(value));
		}
		
		
		
		if(!edited.equalsIgnoreCase("ph")) ((EditText) findViewById(R.id.value_ph)).setText(Controller.checkInfinityValues(phValue) + "");
		if(!edited.equalsIgnoreCase("[h]")) ((EditText) findViewById(R.id.value_ph_concentration)).setText(Controller.checkInfinityValues(Math.pow(10, -1 * phValue)) + "");
		if(!edited.equalsIgnoreCase("poh")) ((EditText) findViewById(R.id.value_poh)).setText(Controller.checkInfinityValues((14.00 - phValue)) + "");
		if(!edited.equalsIgnoreCase("[oh]")) ((EditText) findViewById(R.id.value_poh_concentration)).setText(Controller.checkInfinityValues(Math.pow(10, -1 * (14.00 - phValue))) + "");
		
		canCalculate = true;
	}
	
}
