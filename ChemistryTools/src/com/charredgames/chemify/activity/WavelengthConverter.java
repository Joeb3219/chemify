package com.charredgames.chemify.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.charredgames.chemify.R;
import com.charredgames.chemify.util.Controller;

public class WavelengthConverter extends Activity{

	private boolean canCalculate = true;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wavelengthconverter);
		setTitle(Controller.resources.getString(R.string.problem_waves));
	}

	public void onStart(){
		super.onStart();
		((EditText) findViewById(R.id.value_wavelength)).addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable arg0) {
				calculate(((EditText) findViewById(R.id.value_wavelength)).getText().toString(), "wavelength");
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		((EditText) findViewById(R.id.value_frequency)).addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable arg0) {
				calculate(((EditText) findViewById(R.id.value_frequency)).getText().toString(), "frequency");
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		((EditText) findViewById(R.id.value_energy)).addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable arg0) {
				calculate(((EditText) findViewById(R.id.value_energy)).getText().toString(), "energy");
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
		double planck = 6.626 * Math.pow(10, -34);
		double light = 3.0 * Math.pow(10, 8);
		
		double value = 1.00;
		try{
			if(!val.equals("Infinity") && !val.equals("-Infinity") && !val.equals("") && !val.equals(" ") && !val.equals(".") && !val.equals("-") && !val.equals("-.") && !val.equals(".-")) value = Double.parseDouble(val);
		}catch(NumberFormatException e){
			value = 1.00;
		}
		double wavelength = 0.00;
		if(edited.equalsIgnoreCase("wavelength")) wavelength = value;
		else if(edited.equalsIgnoreCase("frequency")) wavelength = light / value;
		else if(edited.equalsIgnoreCase("energy")) wavelength = 1 / ((value) / (light * planck));
		
		if(!edited.equalsIgnoreCase("wavelength")) ((EditText) findViewById(R.id.value_wavelength)).setText(Controller.checkInfinityValues(wavelength) + "");
		if(!edited.equalsIgnoreCase("frequency")) ((EditText) findViewById(R.id.value_frequency)).setText(Controller.checkInfinityValues(light / wavelength) + "");
		if(!edited.equalsIgnoreCase("energy")) ((EditText) findViewById(R.id.value_energy)).setText(Controller.checkInfinityValues( (planck * light) / (wavelength)) + "");
		
		canCalculate = true;
	}
	
}
