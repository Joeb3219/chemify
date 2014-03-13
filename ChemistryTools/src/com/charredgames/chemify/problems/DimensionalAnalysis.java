package com.charredgames.chemify.problems;

import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.Equation;
import com.charredgames.chemify.constant.Measurement;
import com.charredgames.chemify.constant.Unit;
import com.charredgames.chemify.constant.UnitType;
import com.charredgames.chemify.util.Controller;

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
			response.addLine(Controller.resources.getString(R.string.dimensional_analysis_invalid_input), ResponseType.answer);
			return;
		}
		
		String[] fParts = sides[0].split(" ");
		if(fParts.length >= 3 || (fParts.length == 2 && Measurement.stringContainsMeasurement(fParts[0]))){
			//Converting something dealing with a formula
			if(Measurement.stringContainsMeasurement(fParts[0])){
				String[] parts = new String[fParts.length + 1];
				for(int i = 0; i < fParts.length; i++){
					if(i == 0){
						Measurement m = Measurement.getMeasurementFromString(fParts[0]);
						parts[0] = m.getValue() + "";
						parts[1] = m.getUnit().getCommonAbbreviation();
					}else{
						parts[i + 1] = fParts[i];
					}
				}
				fParts = parts;
			}
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
			
			reason += Controller.resources.getString(R.string.dimensional_analysis_unit_equals).replace("[unit]", current.getUnit().name).replace("[value]", current.getUnit().getScientificFactor() + "").replace("[base]", UnitType.getBaseUnit(current.getUnit().type).name) + "<br>";
			reason += Controller.resources.getString(R.string.dimensional_analysis_unit_equals).replace("[unit]", desiredUnit.name).replace("[value]", desiredUnit.getScientificFactor() + "").replace("[base]", UnitType.getBaseUnit(desiredUnit.type).name) + "<br>";
			
			if(current.getUnit().type == UnitType.MASS){
				reason += Controller.resources.getString(R.string.dimensional_analysis_converting_unit).replace("[unit]", current.getUnit().name).replace("[unit2]", Unit.GRAM.name) + "<br>";
				Measurement asGrams = current.convertUnit(Unit.GRAM);
				if(desiredUnit.type == UnitType.AMOUNT_SUBSTANCE){
					reason += Controller.resources.getString(R.string.dimensional_analysis_converting_unit).replace("[unit]", asGrams.getUnit().name).replace("[unit2]", Unit.MOLE.name) + "<br>";
					Measurement asMoles = new Measurement(Unit.MOLE, asGrams.getValue() / compound.getMass());
					answerMeasure = asMoles.convertUnit(desiredUnit);
				}else answerMeasure = asGrams.convertUnit(desiredUnit);
			}else{
				reason += Controller.resources.getString(R.string.dimensional_analysis_converting_unit).replace("[unit]", current.getUnit().name).replace("[unit2]", Unit.MOLE.name) + "<br>";
				Measurement asMoles = current.convertUnit(Unit.MOLE);
				if(desiredUnit.type == UnitType.MASS){
					reason += Controller.resources.getString(R.string.dimensional_analysis_converting_unit).replace("[unit]", asMoles.getUnit().name).replace("[unit2]", Unit.GRAM.name) + "<br>";
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

			Measurement converted;
			if((desiredUnit != current.getUnit()) && desiredUnit.type == UnitType.TEMPERATURE && current.getUnit().type == UnitType.TEMPERATURE){
				converted = new Measurement(desiredUnit);
				converted.setValue(current.getValue());
				if(desiredUnit == Unit.KELVIN){
					if(current.getUnit() == Unit.FAHRENHEIT) converted.setValue((((current.getValue() - 32.0) * 5.0) / 9.0) );
					converted.setValue(converted.getValue() + 273.15);
				}else if(desiredUnit == Unit.FAHRENHEIT){
					if(current.getUnit() == Unit.KELVIN) converted.setValue(current.getValue() - 273.15);
					converted.setValue( (((converted.getValue() * 9.0) / 5.0) + 32.0) );
				}else{
					if(current.getUnit() == Unit.KELVIN) converted.setValue(current.getValue() - 273.15);
					else converted.setValue( (((converted.getValue() - 32.0) * 5.0) / 9.0) );
				}
				answer += converted.getDrawString();
			}else if(desiredUnit.type != current.getUnit().type){
				reason = "";
				answer = Controller.resources.getString(R.string.dimensional_analysis_incomparable_types).replace("[unit]", current.getUnit().name).replace("[unit2]", desiredUnit.name);
			}else {
				reason += Controller.resources.getString(R.string.dimensional_analysis_unit_equals).replace("[unit]", current.getUnit().name).replace("[value]", current.getUnit().getScientificFactor() + "").replace("[base]", UnitType.getBaseUnit(current.getUnit().type).name) + "<br>";
				reason += Controller.resources.getString(R.string.dimensional_analysis_unit_equals).replace("[unit]", desiredUnit.name).replace("[value]", desiredUnit.getScientificFactor() + "").replace("[base]", UnitType.getBaseUnit(desiredUnit.type).name) + "<br>";
				converted = current.convertUnit(desiredUnit);
				answer += converted.getDrawString();
			}
			
		}
		
		answer += reason;
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
		}else{
			
		}
	}
	
}