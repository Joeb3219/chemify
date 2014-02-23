package com.charredgames.chemify.problems;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.constant.Measurement;
import com.charredgames.chemify.constant.Unit;

public class DimensionalAnalysis extends Problem{

	public DimensionalAnalysis(String input) {
		super(input);
	}

	public void solve(boolean isPrimary){
		String collectiveInput = input, answer = "";
		input = Controller.replaceConversionSymbols(input);
		String[] sides = input.split("to");
		
		if(sides.length == 0){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine("Invalid input", ResponseType.answer);
			return;
		}
		
		Measurement current = Measurement.getMeasurementFromString(sides[0]);
		answer += current.getDrawString() + " &#8594; ";
		
		Unit desiredUnit = null;
		if(sides.length > 1) desiredUnit = Unit.getUnitFromString(sides[1]);
		else desiredUnit = Unit.GRAM;
		Measurement converted = current.convertUnit(desiredUnit);
		answer += converted.getDrawString();
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
		}else{
			
		}
	}
	
}