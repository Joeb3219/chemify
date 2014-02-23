package com.charredgames.chemify.constant;

import com.charredgames.chemify.Controller;

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
		return Controller.doubleToScientific(value) + " " + unit.getCommonAbbreviation();
	}
	
	public Measurement convertUnit(Unit desired){
		//Only really meant to be used for mass, time, length, etc. (not moles, etc)
		if(unit == desired) return this;
		if(unit.type != desired.type) return this;
		
		Measurement measure = new Measurement(desired);
		double val = this.value;
		//if(unit.factor < 1) val /= unit.factor;
		val /= unit.factor;
		val *= desired.factor;
		measure.setValue(val);
		
		return measure;
	}
	
	public static Measurement getMeasurementFromString(String str){
		str = str.replace(" ", "");
		double val = 0.00;
		Unit unit = null;
		String[] pieces = str.split("(?<=\\d)(?=[a-zA-Z])");
		for(String s : pieces){
			try{
				val += Double.parseDouble(s);
			}catch(Exception e){
				unit = Unit.getUnitFromString(s);
			}
		}
		
		
		return new Measurement(unit, val);
	}
	
}
