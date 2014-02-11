package com.charredgames.chemify.problems;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;
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
		collectiveInput = input;
		
		if(equation == null) equation = getEquationFromString(input);
		ArrayList<Compound> compounds = equation.getAllCompounds();
		
		for(Compound compound : compounds){
			answer += "<b>" + compound.getDrawString(false) + ":</b>";
			reason += "<b>" + compound.getDrawString(false) + ":</b><br>";
			
			if(compound.getMatterState() != MatterState.SOLID && compound.getMatterState() != MatterState.AQUEOUS){
				answer += "insoluble.<br>";
				reason += "Compound is liquid or gas.<br>";
				continue;
			}
			reason += "Compound is not liquid or gas.<br>";
			if(compound.allElementsInGroup(1) || compound.containsOnlyG1AndIon(Controller.getIon("OH")) || compound.containsPolyatomic(Controller.getIon("NH4")) || compound.containsOnlyG1AndIon(Controller.getIon("PO4"))){
				answer += " soluble.<br>";
				reason += "All elements in " + compound.getDrawString(false) + " are in group 1, or are hydroxide/ammonium ion.<br>";
				continue;
			}
			reason += "All elements in " + compound.getDrawString(false) + " are either not in group 1, or aren't hydroxide ion.<br>";
			if(compound.containsPolyatomic(Controller.getIon("NO3")) || compound.containsPolyatomic(Controller.getIon("ClO3")) || 
					compound.containsPolyatomic(Controller.getIon("ClO4")) || compound.containsPolyatomic(Controller.getIon("CH3COO"))){
				answer += " soluble.<br>";
				reason += "Compound contains nitrate, chlorite, perchlorate, or acetate ion.<br>";
				continue;
			}
			reason += "Compound does not contain nitrate, chlorite, perchlorate, or acetate ion.<br>";
			if((compound.containsElement(Element.BROMINE) || compound.containsElement(Element.CHLORINE) || compound.containsElement(Element.IODINE))
					&& !(compound.containsElement(Element.SILVER) || compound.containsElement(Element.LEAD) || compound.containsElement(Element.MERCURY))){
				answer += " soluble.<br>";
				reason += "Compound contains Br, Cl, or I, but not Ag, Pb, or Hg.<br>";
				continue;
			}
			reason += "Compound doesn't contain Br, Cl, or I, or has Ag, Pb, or Hg.<br>";
			if (compound.containsPolyatomic(Controller.getIon("SO4"))){
				if(!compound.containsElement(Element.BARIUM) && !compound.containsElement(Element.STRONTIUM) && !compound.containsElement(Element.CALCIUM) &&
					!compound.containsElement(Element.LEAD) && !compound.containsElement(Element.MERCURY)){
					answer += " soluble.<br>";
					reason += "Compound contains SO4 and no exceptions.<br>";
					continue;
					}
			}
			reason += "Compound does not contains SO4, or has exceptions.<br>";
			if(compound.containsOnlyGroupAndElement(1, Element.SULFUR) || compound.containsOnlyGroupAndElement(2, Element.SULFUR)){
				answer += " soluble.<br>";
				reason += "Compound contains Sulfur and G1 or G2 metals.<br>";
				continue;
			}
			reason += "Compound eitehr contains no Sulfur, or no G1/G2 metals.<br>";
			if(compound.containsOnlyG1AndIon(Controller.getIon("SO3")) || compound.containsOnlyG1AndIon(Controller.getIon("CO3")) ||
					compound.containsOnlyG1AndIon(Controller.getIon("CrO3")) || compound.containsOnlyG1AndIon(Controller.getIon("PO3"))){
				answer += " soluble.<br>";
				reason += "Compound contains SO3, CO3, CrO4, or PO4, and G1 metals.<br>";
				continue;
			}
			reason += "Compound doesn't contain SO3, CO3, CrO4, or PO4, or has no G1 metals.<br>";
			answer += " insoluble.";
			if(compounds.get(compounds.size() - 1) != compound) answer += "<br>";
			reason += "Compound failed all checks.<br>";
		}
		
		answer += reason;
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
		}else{
			response.addLine(answer, ResponseType.solubility);
		}
	}
	
}
