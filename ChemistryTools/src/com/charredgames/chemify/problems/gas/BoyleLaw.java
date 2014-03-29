package com.charredgames.chemify.problems.gas;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Unit;
import com.charredgames.chemify.constant.UnitType;
import com.charredgames.chemify.util.Controller;

public class BoyleLaw extends Activity{

	private Spinner p1Unit, p2Unit, v1Unit, v2Unit;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Controller.resetIfNeeded(this);
		setTitle(Controller.resources.getString(R.string.problem_boyle_law));
		setContentView(R.layout.boylelaw);
		
		if(savedInstanceState == null) reset();
		
	}
	
	private void reset(){
		p1Unit = (Spinner) findViewById(R.id.unit_pressure1);
		p2Unit = (Spinner) findViewById(R.id.unit_pressure2);
		v1Unit = (Spinner) findViewById(R.id.unit_volume1);
		v2Unit = (Spinner) findViewById(R.id.unit_volume2);
		setUnits();
	}
	
	private void setUnits(){
		ArrayList<String> units = new ArrayList<String>();
		for(Unit unit : UnitType.PRESSURE.getUnits()) units.add(unit.name);
		p1Unit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, units));
		p2Unit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, units));
		units = new ArrayList<String>();
		for(Unit unit : UnitType.VOLUME.getUnits()) units.add(unit.name);
		v1Unit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, units));
		v2Unit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, units));
	}
	
}
