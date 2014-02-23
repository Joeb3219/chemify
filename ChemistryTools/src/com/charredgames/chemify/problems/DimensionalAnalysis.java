package com.charredgames.chemify.problems;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.Equation;
import com.charredgames.chemify.constant.Measurement;
import com.charredgames.chemify.constant.Unit;
import com.charredgames.chemify.constant.UnitType;

public class DimensionalAnalysis extends Problem{

	public DimensionalAnalysis(String input) {
		super(input);
	}

	public void solve(boolean isPrimary){
		String collectiveInput = input, answer = "";
		input = Controller.replaceConversionSymbols(input);
		String[] sides = input.split(" to ");
		
		if(sides.length == 0){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine("Invalid input", ResponseType.answer);
			return;
		}
		
		String[] fParts = sides[0].split(" ");
		if(fParts.length >= 3){
			//Converting something dealing with a formula
			String eString = fParts[2];
			if(fParts.length > 3) {
				for(int i = 3; i < fParts.length; i ++) eString += " " + fParts[i];
			}
			Equation equation = getEquationFromString(eString);
			Compound compound = new Compound(new ElementGroup());
			if(equation.hasReactants()) compound = equation.getReactants().get(0);
			
			sides[0] = sides[0].replace(eString, "");
			
			Measurement current = Measurement.getMeasurementFromString(sides[0]);
			Measurement answerMeasure;
			answer += current.getDrawString() + " " + compound.getDrawString() + " &#8594; ";
			Unit desiredUnit = null;
			if(sides.length > 1) desiredUnit = Unit.getUnitFromString(sides[1]);
			else desiredUnit = Unit.GRAM;
			
			if(current.getUnit().type == UnitType.MASS){
				Measurement asGrams = current.convertUnit(Unit.GRAM);
				if(desiredUnit.type == UnitType.AMOUNT_SUBSTANCE){
					Measurement asMoles = new Measurement(Unit.MOLE, asGrams.getValue() / compound.getMass());
					answerMeasure = asMoles.convertUnit(desiredUnit);
				}else answerMeasure = asGrams.convertUnit(desiredUnit);
			}else{
				Measurement asMoles = current.convertUnit(Unit.MOLE);
				if(desiredUnit.type == UnitType.MASS){
					Measurement asGrams = new Measurement(Unit.GRAM, asMoles.getValue() * compound.getMass());
					answerMeasure = asGrams.convertUnit(desiredUnit);
				}else answerMeasure = asMoles.convertUnit(desiredUnit);
			}
			
			answer += answerMeasure.getDrawString() + " " + compound.getDrawStringWithoutCharges();
			
		}else{
			//Simple unit conversion
			Measurement current = Measurement.getMeasurementFromString(sides[0]);
			answer += current.getDrawString() + " &#8594; ";
			
			Unit desiredUnit = null;
			if(sides.length > 1) desiredUnit = Unit.getUnitFromString(sides[1]);
			else desiredUnit = Unit.GRAM;
			Measurement converted = current.convertUnit(desiredUnit);
			answer += converted.getDrawString();
		}
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
		}else{
			
		}
	}
	
}