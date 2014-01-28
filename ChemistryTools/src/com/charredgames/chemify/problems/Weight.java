package com.charredgames.chemify.problems;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;

public class Weight extends Problem{

	protected ResponseType type = ResponseType.weight;

	public Weight(String input) {
		super(input);
		//elementGroups = getElementGroups(input);
	}
	
	public Weight(ArrayList<ElementGroup> elementGroups){
		super(elementGroups);
	}

	public void solve(boolean isPrimary){
		double weight = 0.00;
		String collectiveInput = "";
		
		if(input != null) {
			elementGroups = convertNameToElementGroups(input);
			collectiveInput = input;
		}else{
			for(ElementGroup group : elementGroups){
				collectiveInput += group.getDrawString();
			}
		}
		
		for(ElementGroup group : elementGroups){
			weight += group.getOverallWeight();
		}
		
		DecimalFormat f = new DecimalFormat("##.00");
		
		if(isPrimary) {
			String output = "<u>" + weight + " g/mol</u><br>";
			for(ElementGroup group : elementGroups){
				String generalGroupMarkup = "[ " + group.getDrawString() + " -> " + group.getOverallWeight() + " g/mol (" + f.format((group.getOverallWeight() / weight) * 100) + "%) ]";
				output += "<b>" + generalGroupMarkup + "</b>:<br>";
				for(ElementSet set : group.getElementSets()){
					output += set.getDrawString() + " -> " + set.getWeight() + " g/mol (" + f.format((set.getWeight() / group.getOverallWeight()) * 100) + "% of group, " + f.format((set.getWeight() / weight) * 100) + "% of total) <br>";
				}
			}
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(output, ResponseType.answer);
		}
		else response.addLine("<b>Weight</b>:" + f.format(weight) + " g/mol", ResponseType.weight);
	}
	
}
