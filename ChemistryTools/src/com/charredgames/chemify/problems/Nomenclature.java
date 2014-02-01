package com.charredgames.chemify.problems;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.constant.BondType;
import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;
import com.charredgames.chemify.constant.MetalType;

public class Nomenclature extends Problem{

	protected ResponseType type = ResponseType.nomenclature;

	public Nomenclature(String input) {
		super(input);
	}

	public void solve(boolean isPrimary){
		BondType type = BondType.IONIC;
		String collectiveInput = "", reason = "{reason}";
		String answer = "Oops, something went wrong.";
		ArrayList<ElementSet> sets = new ArrayList<ElementSet>();
		ArrayList<ElementGroup> groups = new ArrayList<ElementGroup>();
		//Convert name to formula
		if(input.contains(" ")){
			reason += "Does the input contain a space? Yes.<br>";
			collectiveInput = input;
			input = input.toLowerCase();
			String[] inputGroups = input.split(" ");
			//Binary acid
			if(inputGroups[1].equalsIgnoreCase("acid")){
				reason += "Does input contain the word 'acid'? Yes.<br>";
				if(inputGroups[0].contains("hydro")){
					reason += "The word 'hydro' was found: Binary Acid.<br>";
					inputGroups[0] = inputGroups[0].replace("hydro", "");
					ElementGroup acidSet = revertEnding(inputGroups[0], true);
					reason += "The first element must be hydrogen.<br>";
					reason += "Converted " + inputGroups[0] + " to " + acidSet.getDrawString() + ".<br>";
					ElementGroup hydrogen = new ElementGroup(new ElementSet(Element.HYDROGEN, 1));
					
					if((acidSet.getCharge() + hydrogen.getCharge()) == 0){
						reason += "Charges of " + hydrogen.getDrawString() + " and " + acidSet.getDrawString() + " equal zero when added.<br>";
						answer = hydrogen.getDrawString() + acidSet.getDrawString();
					}
					else{
						reason += "Charges of " + hydrogen.getDrawString() + " and " + acidSet.getDrawString() + " do not equal zero when added.<br>";
						reason += "Setting the quantities of each to the opposite's charge.<br>";
						int acidElementCharge = acidSet.getCharge();
						int hydrogenCharge = hydrogen.getCharge();
						hydrogen.setQuantity(acidElementCharge);
						acidSet.setQuantity(hydrogenCharge);
						answer = hydrogen.getDrawString() + acidSet.getDrawString();
					}
				//Non-binary acid.
				}else{
					reason += "The word 'hydro' wasn't found: non-binary acid.<br>";
					ElementGroup acidGroup = revertEnding(inputGroups[0], false);
					ElementGroup hydrogen = new ElementGroup(new ElementSet(Element.HYDROGEN, 1));
					reason += "First element must be Hydrogen.<br>";
					reason += "Converted " + inputGroups[0] + " to " + acidGroup.getDrawString() + ".<br>";
					
					if((acidGroup.getCharge() + hydrogen.getCharge()) == 0){
						reason += "Charges of " + hydrogen.getDrawString() + " and " + acidGroup.getDrawString() + " equal zero when added.";
						answer = hydrogen.getDrawString() + acidGroup.getDrawString();
					}else{
						reason += "Charges of " + hydrogen.getDrawString() + " and " + acidGroup.getDrawString() + " do not equal zero when added.<br>";
						reason += "Setting the quantities of each to the opposite's charge.<br>";
						int acidGroupCharge = acidGroup.getCharge();
						int hydrogenCharge = hydrogen.getCharge();
						
						hydrogen.setQuantity(acidGroupCharge);
						acidGroup.setQuantity(hydrogenCharge);
						
						answer = hydrogen.getDrawString() + acidGroup.getDrawString();
					}
				}
			//Not an acid.
			}else{
				reason += "Does input contain the word 'acid'? No.<br>";
				//Contains roman-numerals
				if(input.contains("(")){
					reason += "Input contains roman numerals? No.<br>";
					input = input.replace(" ", "");
					String[] split = input.split("[()]");
					int anionCharge = Controller.convertNumeralToInt(split[1]);
					reason += "Roman numeral " + split[1] + " recognized as " + anionCharge + ": anion's charge.<br>";
					
					ElementGroup anion = new ElementGroup(new ElementSet(Element.getElement(split[0]), 1));
					reason += "Converted input string " + split[0] + " to " + anion.getDrawString() + " (anion).<br>";
					ElementGroup cation;
					if(split[2].contains("ide")) cation = revertEnding(split[2], true);
					else cation = revertEnding(split[2], false);
					
					reason += "Converted input string " + split[2] + " to " + cation.getDrawString() + " (cation).<br>";
					
					anion.setCharge(anionCharge);
					
					if((anion.getCharge() + cation.getCharge()) == 0){
						reason += "Charges of " + anion.getDrawString() + " and " + cation.getDrawString() + " equal zero when added.<br>";
						answer = anion.getDrawString() + cation.getDrawString();
					}
					else{
						reason += "Charges of " + anion.getDrawString() + " and " + cation.getDrawString() + " do not equal zero when added.<br>";
						reason += "Setting the quantities of each to the opposite's charge.<br>";
						int cationCharge = cation.getCharge();
						
						anion.setQuantity(cationCharge / anion.getQuantity());
						cation.setQuantity(anionCharge / cation.getQuantity());
						
						answer = anion.getDrawString() + cation.getDrawString();
						
					}
					
				}else{
					reason += "Input contains roman numerals? No.<br>";
					boolean usesPrefixes = false;
					for(Prefix prefix : Controller.prefixes){
						if(input.contains(prefix.getPrinted()) || input.contains(prefix.getSecondary())){
							if(!(input.contains("ide") && prefix == Prefix.di) && !(input.contains("sodi") && prefix == Prefix.di)){
								usesPrefixes = true;
								break; 
							}
						}
					}
					
					//Really easy, just decipher prefixes.
					if(usesPrefixes){
						reason += "Input contains at least one prefix (mon, dec, etc).<br>";
						String[] split = input.split(" ");
						ElementGroup anion = revertEnding(split[0], true);
						ElementGroup cation = revertEnding(split[1], true);
						
						reason += "Converted input string  " + split[0] + " to " + anion.getDrawString() + " (anion).<br>";
						reason += "Converted input string  " + split[1] + " to " + cation.getDrawString() + " (cation).<br>";
						
						answer = anion.getDrawString() + cation.getDrawString();
						
					}else{
						reason += "Input does not contain at least one prefix.<br>";
						String[] split = input.split(" ");
						ElementGroup anion = new ElementGroup(new ElementSet(Element.getElement(split[0]), 1));
						reason += "Converted input string " + split[0] + " to " + anion.getDrawString() + " (anion).<br>";
						ElementGroup cation;
						if(split[1].contains("ide")) cation = revertEnding(split[1], true);
						else cation = revertEnding(split[1], false);
						reason += "Converted input string " + split[1] + " to " + cation.getDrawString() + " (cation).<br>";
						
						if((anion.getCharge() + cation.getCharge()) == 0) {
							reason += "Charges of " + anion.getDrawString() + " and " + cation.getDrawString() + " equal zero when added.<br>";
							answer = anion.getDrawString() + cation.getDrawString();
						}
						else{
							reason += "Charges of " + anion.getDrawString() + " and " + cation.getDrawString() + " do not equal zero when added.<br>";
							reason += "Setting the quantities of each to the opposite's charge.<br>";
							int anionCharge = anion.getCharge();
							int cationCharge = cation.getCharge();
							
							anion.setQuantity(cationCharge / anion.getQuantity());
							cation.setQuantity(anionCharge / cation.getQuantity());
							
							answer = anion.getDrawString() + cation.getDrawString();
						}
						
					}
					
				}
			}
			
			if(!answer.equalsIgnoreCase("Oops, something went wrong.")){
				groups = getElementGroups(stripHtmlTags(answer));
				for(ElementGroup group : groups){
					for(ElementSet set : group.getElementSets()) sets.add(set);
				}
			}
		//Convert formula to name.
		}else{
			reason += "Does the input contain a space? No.<br>";
			groups = getElementGroups(input);
			for(ElementGroup group : groups){
				collectiveInput += group.getDrawString();
				for(ElementSet set : group.getElementSets()) sets.add(set);
			}
			
	
			if(sets.size() == 1 && groups.size() == 1){
				reason += "Input contains only one element.<br>";
				answer = normalizeString(sets.get(0).getElement().getName(), true);
			}
			else if(sets.size() == 2 && groups.size() < 2){
				reason += "Input contains no polyatomic ions.<br>";
				if(sets.get(0).getElement().getName().equalsIgnoreCase("Hydrogen")){
					type = BondType.IONIC_ACID;
					answer =  "Hydro" + getAcidEnding(sets.get(1).getElement().getName().toLowerCase(), true) + " Acid";
					reason += "Contains hydrogen: must be an acid.<br>";
					reason += "Binary acid - only Hydrogen and " + sets.get(1).getDrawString() + ".<br>";
				}else{
					
					if(sets.get(0).getElement().getMetalType() == MetalType.METAL){
						reason += "Is the first element a metal? Yes.<br>";
						if(sets.get(0).getElement().getGroup() <= 2){
							reason += "First element's group is 1 or 2: no roman numerals needed.<br>";
							reason += "Adding 'ide' to end of nonmetal's name.<br>";
							answer = normalizeString(sets.get(0).getElement().getName(), true) + " " + changeEnding(sets.get(1).getElement().getName(), "ide");
						}else{
							reason += "First element is a transition metal: roman numerals needed.<br>";
							int anionCharge = Math.abs(sets.get(1).getTotalCharge());
							int cationCharge = anionCharge / sets.get(0).getQuantity();
							reason += "Using numeral " + Controller.convertIntToNumeral(cationCharge) + " in place of needed cation charge: " + cationCharge + ".<br>";
							answer = normalizeString(sets.get(0).getElement().getName(), true) + " (" + Controller.convertIntToNumeral(cationCharge) + ") " + changeEnding(sets.get(1).getElement().getName(), "ide");
						}
						
					}else{
						reason += "Is the first element a metal? No.<br>";
						reason += "Adding 'ide' to second element's name.<br>";
						answer = normalizeString(sets.get(0).getElement().getName(), true) + " " + changeEnding(sets.get(1).getElement().getName(), "ide");
					}
					
				}
			}else{
				reason += "Input may contain polyatomic ions.<br>";
				if(sets.get(0).getElement().getName().equalsIgnoreCase("Hydrogen")){
					reason += "Input begins with Hydrogen: acid.<br>";
					type = BondType.IONIC_ACID;
					if(groups.size() > 1 && groups.get(1).getIon() != null){
						reason += "Input contains polyatomic ions: " + groups.get(1).getIon().getName() + ".<br>";
						reason += "Converting " + groups.get(1).getIon().getName() + " to " + getAcidEnding(groups.get(1).getIon().getName().toLowerCase(), false) + " and apending 'acid' to name.<br>";
						answer = getAcidEnding(groups.get(1).getIon().getName().toLowerCase(), false) + " Acid";
					}else{
						reason += "Input does not contain polyatomic ions. Adding 'acid' to end of name.<br>";
						answer = getAcidEnding(sets.get(1).getElement().getName().toLowerCase(), false) + " Acid";
					}
				}
				else if((groups.size() == 2 || groups.size() == 3) && groups.get(0).getElementCount() == 1){
					reason += "Input contains one or more polyatomic ions.<br>";
					ElementSet groupOneElement = groups.get(0).getElementSets().get(0);
					String output = normalizeString(groupOneElement.getElement().getName() + " ", true);
					reason += "Converting " + groupOneElement.getDrawString() + " to " + output + ".<br>";
					
					if(groupOneElement.getElement().getGroup() > 2){
						reason += "First element is a transition metal: roman numerals needed.<br>";
						int anionCharge = Math.abs(groups.get(1).getCharge());
						int cationCharge = anionCharge / groupOneElement.getQuantity();
						output += "(" + Controller.convertIntToNumeral(cationCharge) + ")";
						reason += "Setting roman roman numeral equal to the needed cation charge: " + cationCharge + ".<br>";
					}
					
					if(groups.get(1).getIon() != null){
						output += " " + normalizeString(groups.get(1).getIon().getName(), true);
						reason += "Converting " + groups.get(1).getIon().getName() + " to " + normalizeString(groups.get(1).getIon().getName(), true) + ".<br>";
					}
					else output += "Oops, something went wrong!";
					
					answer = output;
				}
			}
		}
		
		answer += reason;
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
			addProblemToPanel(response, new Weight(groups));
			//addProblemToPanel(response, new Oxidation(groups));
		}else{
			response.addLine(answer, ResponseType.nomenclature);
		}
	}
	
}