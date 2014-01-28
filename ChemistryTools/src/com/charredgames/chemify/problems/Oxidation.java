package com.charredgames.chemify.problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Jan 12, 2014
 */
public class Oxidation extends Problem{

	protected ResponseType type = ResponseType.oxidation;
	
	public Oxidation(ArrayList<ElementGroup> elementGroups) {
		super(elementGroups);
	}

	public void solve(boolean isPrimary){
		ArrayList<ElementSet> sets = new ArrayList<ElementSet>();
		for(ElementGroup group : elementGroups){
			for(ElementSet set : group.getElementSets()){
				sets.add(set);
			}
		}
		
		String collectiveInput = "";
		for(ElementGroup group : elementGroups){
			collectiveInput += group.getDrawString();
			for(ElementSet set : group.getElementSets()) sets.add(set);
		}
		
		String output = "";
		
		if(sets.size() == 1) {
			output += sets.get(0).getElement().getSymbol() + ": 0";
			if(isPrimary) response.addLine(output, ResponseType.answer);
			else response.addLine(output, type);
			return;
		}
		for(ElementGroup group : elementGroups){
			Map<Element, Integer> oxidations = new HashMap<Element, Integer>();
			for(ElementSet set : group.getElementSets()){
				Element element = set.getElement();
				if(element == Element.FLUORINE) oxidations.put(element, -1);
				else if (element == Element.OXYGEN) oxidations.put(element, -2);
				else if(element.getGroup() == 1) oxidations.put(element, 1);
				else if(element.getGroup() == 1) oxidations.put(element, 2);
				else oxidations.put(element, -9);
			}
			while(mapContainsInteger(oxidations, -9)){
				for(Entry<Element, Integer> entry : oxidations.entrySet()){
					Element element = entry.getKey();
					Integer value = entry.getValue();
					
					if(value == -9){
						int totalCharge = 0;
						for(ElementSet set : sets){
							Element ele = set.getElement();
							if(oxidations.get(ele) == null) oxidations.put(ele, -9);
							if(oxidations.get(ele) != -9){
								totalCharge += Math.abs(oxidations.get(ele) * set.getQuantity());
							}
						}
						for(ElementSet set : sets){
							if(set.getElement() == element){
								int quantity = set.getQuantity();
								if(totalCharge == 0){
									if(element.getGroup() == 1) oxidations.put(element, 1);
									else if(element.getGroup() == 2) oxidations.put(element, 2);
									else if(element.getGroup() == 3) oxidations.put(element, 3);
									else if(element.getGroup() == 13) oxidations.put(element, 3);
									else if(element.getGroup() == 14) oxidations.put(element, 4);
									else if(element.getGroup() == 15) oxidations.put(element, -3);
									else if(element.getGroup() == 16) oxidations.put(element, -2);
									else if(element.getGroup() == 17) oxidations.put(element, -1);
									else if(element.getGroup() == 18) oxidations.put(element, 1);
								}else{
									try{
										entry.setValue((totalCharge / quantity));
									}catch(Exception e){}
									//oxidations.put(element, (totalCharge / quantity));
								}
							}
						}
					}
				}
			}
			
			for(Entry<Element, Integer> entry : oxidations.entrySet()){
				output += entry.getKey().getSymbol() + ": " + entry.getValue() + "<br>";
			}
			
		}
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(output, ResponseType.answer);
		}else{
			response.addLine(output, ResponseType.oxidation);
		}
		
	}
	
}
