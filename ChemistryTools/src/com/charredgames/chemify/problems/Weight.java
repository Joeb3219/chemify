package com.charredgames.chemify.problems;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;
import com.charredgames.chemify.constant.Equation;
import com.charredgames.chemify.util.Controller;

public class Weight extends Problem{

	protected ResponseType type = ResponseType.weight;

	public Weight(String input) {
		super(input);
	}
	
	public Weight(ArrayList<ElementGroup> elementGroups){
		super(elementGroups);
	}

	public Weight(Equation equation){
		super(equation);
	}
	
	public void solve(boolean isPrimary){
		double weight = 0.00;
		String collectiveInput, answer = "";
		
		
		if(equation == null){
			if(input != null){
				if(input.matches("[0-9]+")){
					equation = new Equation();
					equation.addCompound(new Compound(new ElementGroup(new ElementSet(Element.getElement(input), 1))), 0);
					if(isPrimary){
						answer += Controller.resources.getString(R.string.weight_invalid_input_numbers) + "<br>";
						answer += Controller.resources.getString(R.string.weight_invalid_input_finding_element) + "<br>";
					}
				}else equation = getEquationFromString(input);
			}
			else{
				Compound c = new Compound(elementGroups);
				equation = new Equation();
				equation.addCompound(c, 0);
			}
		}else if(input == null) input = equation.getDrawString(false);
		
		if(Controller.autoFormat) collectiveInput = equation.getDrawString(false);
		else collectiveInput = input;
		
		equation.balance();
		
		ArrayList<Compound> compounds = new ArrayList<Compound>();
		compounds.addAll(equation.getReactants());
		compounds.addAll(equation.getProducts());
		
		DecimalFormat f = new DecimalFormat("##.00");

		weight = equation.getMass();
		
		for(Compound c : compounds){
			reason += "<b>[" + c.getDrawString() + ": " + f.format(c.getMass()) + " g (" + f.format((c.getMass() / weight) * 100) + "%) ]</b>:<br>";
			for(ElementGroup g : c.getElementGroups()){
				for(ElementSet set : g.getElementSets()){
					reason += set.getDrawString() + " -> " + f.format(set.getWeight()) + " g/mol (" + f.format((set.getWeight() / c.getMass()) * 100) + "% of group, " + f.format((set.getWeight() / weight) * 100) + "% of total) <br>";
				}
			}
			if(compounds.size() > 1) answer += c.getDrawString() + ": ";
			answer += f.format(c.getMass());
			if(compounds.size() == 1) answer += " g/mol";
			else answer += " grams<br>";
		}
		
		if(compounds.size() > 1) answer += Controller.resources.getString(R.string.weight_overall_weight) + " " + f.format(weight) + " grams";
		
		answer += reason;
		
		if(isPrimary) {
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
			addProblemToPanel(response, new Solubility(equation));
			addProblemToPanel(response, new Oxidation(equation));
		}
		else response.addLine(answer, ResponseType.weight);
	}
	
}
