package com.charredgames.chemify.constant;

import java.text.DecimalFormat;

public class Measurement {

	private double value = 0.00;
	private Unit unit;
	private int sigFigs = 3;
	
	public Measurement(Unit unit, double value){
		this.unit = unit;
		this.value = value;
	}
	
	public Measurement(Unit unit){
		this.unit = unit;
	}
	
	public double getValue(){
		return value;
	}
	
	public Unit getUnit(){
		return unit;
	}
	
	public void setUnit(Unit unit){
		this.unit = unit;
	}
	
	public void setValue(double value){
		this.value = value;
	}
	
	public String getDrawString(){
		DecimalFormat f = new DecimalFormat("##.00000");
		
		return f.format(value) + " " + unit.getCommonAbbreviation();
	}
	
	public Measurement convertUnit(Unit desired){
		if(unit == desired) return this;
		if(unit.type != desired.type) return this;
		
		Measurement measure = new Measurement(desired);
		double val = this.value;
		if(unit.factor < 1) val /= unit.factor;
		else val *= unit.factor;
		val *= desired.factor;
		measure.setValue(val);
		
		return measure;
	}
	
}
