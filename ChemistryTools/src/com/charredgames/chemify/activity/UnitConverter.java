package com.charredgames.chemify.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Measurement;
import com.charredgames.chemify.constant.Unit;
import com.charredgames.chemify.constant.UnitPrefix;
import com.charredgames.chemify.constant.UnitType;
import com.charredgames.chemify.util.Controller;

public class UnitConverter extends Activity{

	private Spinner unitType, fromUnit, toUnit;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(Controller.resources.getString(R.string.problem_unit_converter));
		
		if(unitType == null) {
			setContentView(R.layout.unitconverter);
			reset();
		}
		
		ArrayList<String> types = new ArrayList<String>();
		for(UnitType type : Controller.unitTypes) types.add(type.name);
		unitType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types));
		resetUnits(unitType.getSelectedItem().toString(), this);
	}
	
	public void onStart(){
		super.onStart();
		unitType.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	String item = unitType.getSelectedItem().toString();
				resetUnits(item, parentView.getContext());
		    	calculate();
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		fromUnit.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				calculate();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		toUnit.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				calculate();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		((EditText) findViewById(R.id.value_from)).addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				calculate();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
		});
	}
	
	private void reset(){
		unitType = (Spinner) findViewById(R.id.unit_type);
		fromUnit = (Spinner) findViewById(R.id.unit_from);
		toUnit = (Spinner) findViewById(R.id.unit_to);
		ArrayList<String> types = new ArrayList<String>();
		for(UnitType type : Controller.unitTypes) types.add(type.name);
		unitType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types));
	}
	
	private void resetUnits(String item, Context context){
		ArrayList<String> mainUnits = new ArrayList<String>();
    	ArrayList<String> prefixedUnits = new ArrayList<String>();
    	for(Unit unit : Controller.units){
    		if(unit.type.name.equalsIgnoreCase(item)){
    			if(UnitPrefix.containsPrefix(unit.name)) prefixedUnits.add(unit.name);
    			else mainUnits.add(unit.name);
    		}
    	}
    	
    	for(String str : prefixedUnits)  mainUnits.add(str);
    	
    	fromUnit.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, mainUnits));
    	toUnit.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, mainUnits));
	}
	
	private void calculate(){
		String valueFrom = ((EditText) findViewById(R.id.value_from)).getText().toString();
		double val = 1.00;
		if(!valueFrom.equals("") && !valueFrom.equals(" ") && !valueFrom.equals(".") && !valueFrom.equals("-") && !valueFrom.equals("-.") && !valueFrom.equals(".-")) val = Double.parseDouble(valueFrom);
		Measurement m = new Measurement(getUnitFromSpinner(fromUnit), val);
		Measurement result = m.convertUnit(getUnitFromSpinner(toUnit));
		((EditText) findViewById(R.id.value_to)).setText(Html.fromHtml(result.getDrawStringWithoutAbbrev()));
		if((result.getUnit() != m.getUnit()) && result.getUnit().type == UnitType.TEMPERATURE && m.getUnit().type == UnitType.TEMPERATURE){
			result = new Measurement(result.getUnit());
			result.setValue(m.getValue());
			if(result.getUnit() == Unit.KELVIN){
				if(m.getUnit() == Unit.FAHRENHEIT) result.setValue((((m.getValue() - 32.0) * 5.0) / 9.0) );
				result.setValue(result.getValue() + 273.15);
			}else if(result.getUnit() == Unit.FAHRENHEIT){
				if(m.getUnit() == Unit.KELVIN) result.setValue(result.getValue() - 273.15);
				result.setValue( (((result.getValue() * 9.0) / 5.0) + 32.0) );
			}else{
				if(m.getUnit() == Unit.KELVIN) result.setValue(result.getValue() - 273.15);
				else result.setValue( (((result.getValue() - 32.0) * 5.0) / 9.0) );
			}
			((EditText) findViewById(R.id.value_to)).setText(Html.fromHtml(result.getDrawStringWithoutAbbrev()));
		}
	}
	
	private Unit getUnitFromSpinner(Spinner spinner){
		return Unit.getUnitFromString((String)spinner.getSelectedItem());
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		reset();
		unitType.setSelection(savedInstanceState.getInt("type"));
		resetUnits(unitType.getSelectedItem().toString(), this);
		fromUnit.setSelection(savedInstanceState.getInt("fromUnit"));
		toUnit.setSelection(savedInstanceState.getInt("toUnit"));
		((EditText) findViewById(R.id.value_from)).setText(savedInstanceState.getDouble("valueFrom") + "");
		calculate();
	}
	
	public void onSaveInstanceState(Bundle outState){
		outState.putInt("type", unitType.getSelectedItemPosition());
		outState.putInt("toUnit", toUnit.getSelectedItemPosition());
		outState.putInt("fromUnit", fromUnit.getSelectedItemPosition());
		String valueFrom = ((EditText) findViewById(R.id.value_from)).getText().toString();
		double val = 1.00;
		if(!valueFrom.equals("") && !valueFrom.equals(" ") && !valueFrom.equals(".") && !valueFrom.equals("-") && !valueFrom.equals("-.") && !valueFrom.equals(".-")) val = Double.parseDouble(valueFrom);
		outState.putDouble("valueFrom", val);
		
		super.onSaveInstanceState(outState);
	}
	
	
}
