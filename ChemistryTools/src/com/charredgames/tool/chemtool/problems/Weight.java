package com.charredgames.tool.chemtool.problems;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.charredgames.tool.chemtool.constant.ElementGroup;
import com.charredgames.tool.chemtool.constant.ElementSet;

public class Weight extends Problem{

	protected ResponseType type = ResponseType.weight;

	public Weight(String input) {
		super(input);
		elementGroups = getElementGroups(input);
	}
	
	public Weight(ArrayList<ElementGroup> elementGroups){
		super(elementGroups);
	}

	public void solve(boolean isPrimary){
		double weight = 0.00;
		
		for(ElementGroup group : elementGroups){
			weight += group.getOverallWeight();
		}
		
		String collectiveInput = "";
		for(ElementGroup group : elementGroups){
			collectiveInput += group.getDrawString();
		}
		
		DecimalFormat f = new DecimalFormat("##.00");
		
		if(isPrimary) {
			String output = "";
			for(ElementGroup group : elementGroups){
				String generalGroupMarkup = "[ " + group.getDrawString() + " -> " + group.getOverallWeight() + " g/mol (" + f.format((group.getOverallWeight() / weight) * 100) + "%) ]";
				output += "<b>" + generalGroupMarkup + "</b>:<br>";
				for(ElementSet set : group.getElementSets()){
					output += set.getDrawString() + " -> " + set.getWeight() + " g/mol (" + f.format((set.getWeight() / group.getOverallWeight()) * 100) + "% of group, " + f.format((set.getWeight() / weight) * 100) + "% of total) <br>";
				}
			}
			response.addLine(collectiveInput + ": " + f.format(weight) + " g/mol", ResponseType.input);
			response.addLine(output, ResponseType.answer);
		}
		else response.addLine("<b>Weight</b>:" + f.format(weight) + " g/mol", ResponseType.weight);
	}
	
}
