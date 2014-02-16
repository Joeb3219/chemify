package com.charredgames.chemify.problems;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.Equation;
import com.charredgames.chemify.constant.MatterState;

public class Solubility extends Problem{

	public Solubility(String input) {
		super(input);
	}

	public Solubility(Equation equation){
		super(equation);
	}
	
	public void solve(boolean isPrimary){
		String collectiveInput, answer = "";
		
		if(equation == null) equation = getEquationFromString(input);
		if(input == null) input = equation.getDrawString(false);
		
		if(Controller.autoFormat) collectiveInput = equation.getDrawString(false);
		else collectiveInput = input;
		
		ArrayList<Compound> compounds = equation.getAllCompounds();
		
		for(Compound compound : compounds){
			boolean isLast = false;
			if(compounds.get(compounds.size() - 1) == compound) isLast = true;
			answer += "<b>" + compound.getDrawString(false) + ":</b>";
			reason += "<b>" + compound.getDrawString(false) + ":</b><br>";
			
			if(compound.getMatterState() != MatterState.SOLID && compound.getMatterState() != MatterState.AQUEOUS){
				answer += " " + Controller.resources.getString(R.string.solubility_insoluble);
				if(!isLast) answer += "<br>";
				reason += Controller.resources.getString(R.string.solubility_is_liquid_or_gas) + "<br>";
				continue;
			}
			reason += Controller.resources.getString(R.string.solubility_not_liquid_or_gas) + "<br>";
			if(compound.allElementsInGroup(1) || compound.containsOnlyG1AndIon(Controller.getIon("OH")) || compound.containsPolyatomic(Controller.getIon("NH4")) || compound.containsOnlyG1AndIon(Controller.getIon("PO4"))){
				answer += " " + Controller.resources.getString(R.string.solubility_soluble);
				if(!isLast) answer += "<br>";
				reason += Controller.resources.getString(R.string.solubility_all_group_one) + "<br>";
				continue;
			}
			reason += Controller.resources.getString(R.string.solubility_not_group_one) + "<br>";
			if(compound.containsPolyatomic(Controller.getIon("NO3")) || compound.containsPolyatomic(Controller.getIon("ClO3")) || 
					compound.containsPolyatomic(Controller.getIon("ClO4")) || compound.containsPolyatomic(Controller.getIon("CH3COO"))){
				answer += " " + Controller.resources.getString(R.string.solubility_soluble);
				if(!isLast) answer += "<br>";
				reason += Controller.resources.getString(R.string.solubility_has_nitrate_or_chlorite) + "<br>";
				continue;
			}
			reason += Controller.resources.getString(R.string.solubility_not_has_nitrate_or_chlorite) + "<br>";
			if((compound.containsElement(Element.BROMINE) || compound.containsElement(Element.CHLORINE) || compound.containsElement(Element.IODINE))
					&& !(compound.containsElement(Element.SILVER) || compound.containsElement(Element.LEAD) || compound.containsElement(Element.MERCURY))){
				answer += " " + Controller.resources.getString(R.string.solubility_soluble);
				if(!isLast) answer += "<br>";
				reason += Controller.resources.getString(R.string.solubility_has_br_cl_i) + "<br>";
				continue;
			}
			reason += Controller.resources.getString(R.string.solubility_has_br_cl_i_exception) + "<br>";
			if (compound.containsPolyatomic(Controller.getIon("SO4"))){
				if(!compound.containsElement(Element.BARIUM) && !compound.containsElement(Element.STRONTIUM) && !compound.containsElement(Element.CALCIUM) &&
					!compound.containsElement(Element.LEAD) && !compound.containsElement(Element.MERCURY)){
					answer += " " + Controller.resources.getString(R.string.solubility_soluble);
				if(!isLast) answer += "<br>";
					reason += Controller.resources.getString(R.string.solubility_so4_no_exceptions) + "<br>";
					continue;
					}
			}
			reason += Controller.resources.getString(R.string.solubility_so4_exceptions) + "<br>";
			if(compound.containsOnlyGroupAndElement(1, Element.SULFUR) || compound.containsOnlyGroupAndElement(2, Element.SULFUR)){
				answer += " " + Controller.resources.getString(R.string.solubility_soluble);
				if(!isLast) answer += "<br>";
				reason += Controller.resources.getString(R.string.solubility_s_g1g2) + "<br>";
				continue;
			}
			reason += Controller.resources.getString(R.string.solubility_no_s_g1g2) + "<br>";
			if(compound.containsOnlyG1AndIon(Controller.getIon("SO3")) || compound.containsOnlyG1AndIon(Controller.getIon("CO3")) ||
					compound.containsOnlyG1AndIon(Controller.getIon("CrO3")) || compound.containsOnlyG1AndIon(Controller.getIon("PO3"))){
				answer += " " + Controller.resources.getString(R.string.solubility_soluble);
				if(!isLast) answer += "<br>";
				reason += Controller.resources.getString(R.string.solubility_so3_co3) + "<br>";
				continue;
			}
			reason += Controller.resources.getString(R.string.solubility_no_so3_co3) + "<br>";
			answer += " " + Controller.resources.getString(R.string.solubility_insoluble);
			if(!isLast) answer += "<br>";
			reason += Controller.resources.getString(R.string.solubility_failed_all) + "<br>";
		}
		
		answer += reason;
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
			addProblemToPanel(response, new Weight(equation));
			addProblemToPanel(response, new Oxidation(equation));
			addProblemToPanel(response, new Nomenclature(input));
		}else{
			response.addLine(answer, ResponseType.solubility);
		}
	}
	
}
