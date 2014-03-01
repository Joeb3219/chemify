package com.charredgames.chemify;

public class SigFigCalculator {

	public double value, significantValue;
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
		
		
	}
	
	public double getSignificantValue(){
		calculate(0);
		return significantValue;
	}
	
	public String getDisplayString(){
		calculate(0);
		
		return Controller.doubleToScientific(significantValue);
	}
	
	public double getSignificantValue(int sigFigs){
		calculate(sigFigs);

		return significantValue;
	}
	
	public String getDisplayString(int sigFigs){
		calculate(sigFigs);
		
		return Controller.doubleToScientific(significantValue);
	}
	
}
