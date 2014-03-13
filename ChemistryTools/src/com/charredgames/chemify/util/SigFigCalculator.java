package com.charredgames.chemify.util;

import java.text.DecimalFormat;

public class SigFigCalculator {

	public double value, significantValue = 0.00;
	public String significantString = "";
	public int sigFigs = 0;
	
	public SigFigCalculator(double value){
		this.value = value;
		calculate(0);
	}
	
	private void calculate(int figures){
		int sigNumber = figures;
		if(figures == 0){
			String val = String.valueOf(value);
			String sigString = "";
			for(int i = 0; i < val.length(); i++){
				Character a = val.charAt(i);
				sigString += a.toString();
				if(a.equals('.')) continue;
				if(!a.equals('0')){
					sigNumber ++;
				}else{
					String test = val.replace(sigString, "");
					if(test.contains("[1-9]") || test.contains(".")) sigNumber ++;
					else if(val.contains(".") && !test.contains(".")) sigNumber ++; 
				}
				
			}
		}
		this.sigFigs = sigNumber;
		
		significantString = "";
		String format = "0.";
		DecimalFormat f;
		for(int i = 0; i < sigNumber; i ++){
			if(i > 0) format += "0";
		}
		if(value <= 9999 && value >= -9999 && (value >= 0.001 || value <= -0.001 || value == 0)){
			f = new DecimalFormat(format);
			significantString = f.format(value);
		}else{
			f = new DecimalFormat(format + "E0");
			significantString = f.format(value);
			significantString = significantString.replace("E", " * 10<sup>");
			significantString += "</sup>";
		}

	}
	
	public double getSignificantValue(){
		calculate(0);
		return significantValue;
	}
	
	public String getDisplayString(){
		calculate(0);
		
		return significantString;
	}
	
	public double getSignificantValue(int sigFigs){
		calculate(sigFigs);

		return significantValue;
	}
	
	public String getDisplayString(int sigFigs){
		calculate(sigFigs);
		
		return significantString;
	}
	
}
