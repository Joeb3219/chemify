package com.charredgames.chemify.problems;

import java.util.ArrayList;

import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;
import com.charredgames.chemify.constant.Equation;
import com.charredgames.chemify.constant.MetalType;
import com.charredgames.chemify.constant.Prefix;
import com.charredgames.chemify.util.Controller;

public class Nomenclature extends Problem{

	protected ResponseType type = ResponseType.nomenclature;

	public Nomenclature(String input) {
		super(input);
	}

	public void solve(boolean isPrimary){
		String collectiveInput = "";
		String answer = "";
		ArrayList<ElementGroup> groups = new ArrayList<ElementGroup>();
		equation = new Equation();
		
		if(input.matches("[0-9]+")){
			if(isPrimary){
				answer += Controller.resources.getString(R.string.weight_invalid_input_numbers) + "<br>";
				answer += Controller.resources.getString(R.string.weight_invalid_input_finding_element) + "<br>";
			}
			input = Element.getElement(input).getSymbol();
		}
		
		if(Controller.autoFormat) collectiveInput = getFormattedDisplay(input);
		else collectiveInput = input;
		
		//Same code as in Problem method's getEquation && getCompound, but modified.
		input = Controller.replaceReactionSymbols(input);
		String[] sides = input.split("\\>");
		
		for(int i = 0; i < sides.length; i++){
			String side = sides[i];
			equation.addCompounds(getCompoundsFromString(side), i); //Used to build an equation
			side = Controller.replaceReactionSymbols(side);
			String[] compoundStrings = side.split("\\+");
			int iteration = 0;
			for(String str : compoundStrings){
				iteration ++;

				if(compoundStrings.length > 1 || sides.length > 1){
					answer += "<u>" + str + "</u>: ";
					reason += "<b>" + str + "</b><br>";
				}
				//Convert name to formula
				if(str.contains(" ")){
					groups = convertNameToElementGroups(str, this);
					for(ElementGroup group : groups) answer += group.getDrawString();
				//Convert formula to name.
				}else{
					groups = getElementGroups(str);
					answer += convertGroupsToName(groups, this);
				}
				
				if((compoundStrings.length > 1 || sides.length > 1) && !((i == (sides.length - 1)) && compoundStrings.length == iteration)){
					answer += "<br>";
					reason += "<br>";
				}
			
			}
		}
		
		//collectiveInput = equation.getDrawString(false);
		
		answer += reason.intern();
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
			addProblemToPanel(response, new Weight(equation));
			addProblemToPanel(response, new Oxidation(equation));
		}else{
			response.addLine(answer, ResponseType.nomenclature);
		}
	}
	
	public String convertGroupsToName(ArrayList<ElementGroup> groups, Problem problem){
		String reason = "", answer = "";
		ArrayList<ElementSet> sets = new ArrayList<ElementSet>();
		
		reason += Controller.resources.getString(R.string.nomenclature_contains_space) + " " + Controller.resources.getString(R.string.no) + ".<br>";
		for(ElementGroup group : groups){
			for(ElementSet set : group.getElementSets()) sets.add(set);
		}
		

		if(sets.size() == 1 && groups.size() == 1){
			reason += Controller.resources.getString(R.string.nomenclature_one_element) + "<br>";
			answer = Controller.normalizeString(sets.get(0).getElement().getName(), true);
		}
		else if(sets.size() == 2 && groups.size() < 2){
			reason += Controller.resources.getString(R.string.nomenclature_no_polyatomics) + "<br>";
			if(sets.get(0).getElement().getName().equalsIgnoreCase("Hydrogen")){
				answer =  "Hydro" + getAcidEnding(sets.get(1).getElement().getName().toLowerCase(Controller._LOCALE), true) + " Acid";
				reason += Controller.resources.getString(R.string.nomenclature_must_be_acid) + "<br>";
				reason += Controller.resources.getString(R.string.nomenclature_binary_acid) + " " + sets.get(1).getDrawString() + ".<br>";
			}else{
				
				if(sets.get(0).getElement().getMetalType() == MetalType.METAL){
					reason += Controller.resources.getString(R.string.nomenclature_first_is_metal) + " " + Controller.resources.getString(R.string.yes) + ".<br>";
					if(sets.get(0).getElement().getGroup() <= 2){
						reason += Controller.resources.getString(R.string.nomenclature_no_numerals_needed) + "<br>";
						reason += Controller.resources.getString(R.string.nomenclature_adding_ide) + "<br>";
						answer = Controller.normalizeString(sets.get(0).getElement().getName(), true) + " " + changeEnding(sets.get(1).getElement().getName(), "ide");
					}else{
						reason += Controller.resources.getString(R.string.nomenclature_numerals_needed) + "<br>";
						int anionCharge = Math.abs(sets.get(1).getTotalCharge());
						int cationCharge = anionCharge / sets.get(0).getQuantity();
						reason += Controller.resources.getString(R.string.nomenclature_using_numeral) + " " + Controller.convertIntToNumeral(cationCharge) + " " + Controller.resources.getString(R.string.nomenclature_numeral_in_place_of) + " " + cationCharge + ".<br>";
						answer = Controller.normalizeString(sets.get(0).getElement().getName(), true) + " (" + Controller.convertIntToNumeral(cationCharge) + ") " + changeEnding(sets.get(1).getElement().getName(), "ide");
					}
					
				}else{
					reason += Controller.resources.getString(R.string.nomenclature_first_is_metal) + " " + Controller.resources.getString(R.string.no) + ".<br>";
					reason += Controller.resources.getString(R.string.nomenclature_adding_ide) + "<br>";
					answer = Controller.normalizeString(sets.get(0).getElement().getName(), true) + " " + changeEnding(sets.get(1).getElement().getName(), "ide");
				}
				
			}
		}else{
			reason += Controller.resources.getString(R.string.nomenclature_may_contain_polyatomics) + "<br>";
			if(sets.get(0).getElement().getName().equalsIgnoreCase("Hydrogen")){
				reason += Controller.resources.getString(R.string.nomenclature_must_be_acid) + "<br>";
				if(groups.size() > 1 && groups.get(1).getIon() != null){
					reason += Controller.resources.getString(R.string.nomenclature_contains_polyatomic) + " " + groups.get(1).getIon().getName() + ".<br>";
					reason += Controller.resources.getString(R.string.converting) + " " + groups.get(1).getIon().getName() + " " + Controller.resources.getString(R.string.to) + " " + getAcidEnding(groups.get(1).getIon().getName().toLowerCase(Controller._LOCALE), false) + " " + Controller.resources.getString(R.string.nomenclature_appending_acid) + "<br>";
					answer = getAcidEnding(groups.get(1).getIon().getName().toLowerCase(Controller._LOCALE), false) + " Acid";
				}else{
					reason += Controller.resources.getString(R.string.nomenclature_no_polyatomics) + " " + Controller.resources.getString(R.string.nomenclature_appending_acid) + "<br>";
					answer = getAcidEnding(sets.get(1).getElement().getName().toLowerCase(Controller._LOCALE), false) + " Acid";
				}
			}
			else if((groups.size() == 2 || groups.size() == 3) && groups.get(0).getElementCount() == 1){
				reason += Controller.resources.getString(R.string.nomenclature_many_polyatomics) + "<br>";
				ElementSet groupOneElement = groups.get(0).getElementSets().get(0);
				String output = Controller.normalizeString(groupOneElement.getElement().getName() + " ", true);
				reason += Controller.resources.getString(R.string.converting) + " " + groupOneElement.getDrawString() + " " + Controller.resources.getString(R.string.to) + " " + output + ".<br>";
				
				if(groupOneElement.getElement().getGroup() > 2){
					reason += Controller.resources.getString(R.string.nomenclature_transition_metal_numerals_needed)+ "<br>";
					int anionCharge = Math.abs(groups.get(1).getCharge());
					int cationCharge = anionCharge / groupOneElement.getQuantity();
					output += "(" + Controller.convertIntToNumeral(cationCharge) + ")";
					reason += Controller.resources.getString(R.string.nomenclature_setting_numeral_to_charge) +" " + cationCharge + ".<br>";
				}
				
				if(groups.get(1).getIon() != null){
					output += " " + Controller.normalizeString(groups.get(1).getIon().getName(), true);
					reason += Controller.resources.getString(R.string.converting) + " " + groups.get(1).getIon().getName() + " " + Controller.resources.getString(R.string.to) + " " + Controller.normalizeString(groups.get(1).getIon().getName(), true) + ".<br>";
				}
				
				answer = output;
			}
		}
		
		if(problem != null && problem instanceof Nomenclature) problem.reason += reason;
		return answer;
	}
	
	public static ArrayList<ElementGroup> convertNameToElementGroups(String input, Problem problem){
		boolean addReason = false;
		if(problem != null && problem instanceof Nomenclature)addReason = true;
		if(!input.contains(" ")) return Problem.getElementGroups(input);
		String answer = "";
		ArrayList<ElementGroup> groups = new ArrayList<ElementGroup>();
		groups.add(new ElementGroup(new ElementSet(Element.HYDROGEN, 1)));
		
		input = input.toLowerCase(Controller._LOCALE);
		if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_contains_space) + " " + Controller.resources.getString(R.string.yes) + ".<br>";
		String[] inputGroups = input.split(" ");
		//Binary acid
		if(inputGroups[1].equalsIgnoreCase("acid")){
			if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_contains_word_acid) + " " + Controller.resources.getString(R.string.yes) + ".<br>";
			if(inputGroups[0].contains("hydro")){
				if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_word_hydro_found) + "<br>";
				inputGroups[0] = inputGroups[0].replace("hydro", "");
				ElementGroup acidSet = revertEnding(inputGroups[0], true);
				if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_first_element_must_be_hydrogen) + "<br>";
				if(addReason) problem.reason += Controller.resources.getString(R.string.converting) + inputGroups[0] + " " + Controller.resources.getString(R.string.to) + " " + acidSet.getDrawString() + ".<br>";
				ElementGroup hydrogen = new ElementGroup(new ElementSet(Element.HYDROGEN, 1));
				
				if((acidSet.getCharge() + hydrogen.getCharge()) == 0){
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_charges_of) + " " + hydrogen.getDrawString() + " " + Controller.resources.getString(R.string.and) +" " + acidSet.getDrawString() + " " +  Controller.resources.getString(R.string.nomenclature_equals_zero) + "<br>";
					answer = hydrogen.getDrawString() + acidSet.getDrawString();
				}
				else{
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_charges_of) + " " + hydrogen.getDrawString() + " " + Controller.resources.getString(R.string.and) + " " + acidSet.getDrawString() + Controller.resources.getString(R.string.nomenclature_not_equals_zero) + "<br>";
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_setting_charges_opposite) + "<br>";
					int acidElementCharge = acidSet.getCharge();
					int hydrogenCharge = hydrogen.getCharge();
					hydrogen.setQuantity(acidElementCharge);
					acidSet.setQuantity(hydrogenCharge);
					answer = hydrogen.getDrawString() + acidSet.getDrawString();
				}
			//Non-binary acid.
			}else{
				if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_word_hydro_not_found) + "<br>";
				ElementGroup acidGroup = revertEnding(inputGroups[0], false);
				ElementGroup hydrogen = new ElementGroup(new ElementSet(Element.HYDROGEN, 1));
				if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_first_element_must_be_hydrogen) + "<br>";
				if(addReason) problem.reason += Controller.resources.getString(R.string.converting) + " " + inputGroups[0] + " " + Controller.resources.getString(R.string.to) + " " + acidGroup.getDrawString() + ".<br>";
				
				if((acidGroup.getCharge() + hydrogen.getCharge()) == 0){
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_charges_of) + " " + hydrogen.getDrawString() + " " + Controller.resources.getString(R.string.and) + " " + acidGroup.getDrawString() + " " + Controller.resources.getString(R.string.nomenclature_equals_zero);
					answer = hydrogen.getDrawString() + acidGroup.getDrawString();
				}else{
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_charges_of) + " " + hydrogen.getDrawString() + " " + Controller.resources.getString(R.string.and) + " " + acidGroup.getDrawString() + " " + Controller.resources.getString(R.string.nomenclature_not_equals_zero) + "<br>";
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_setting_charges_opposite) + "<br>";
					int acidGroupCharge = acidGroup.getCharge();
					int hydrogenCharge = hydrogen.getCharge();
					
					hydrogen.setQuantity(acidGroupCharge);
					acidGroup.setQuantity(hydrogenCharge);
					
					answer = hydrogen.getDrawString() + acidGroup.getDrawString();
				}
			}
		//Not an acid.
		}else{
			if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_contains_word_acid) + " " + Controller.resources.getString(R.string.no) +".<br>";
			//Contains roman-numerals
			if(input.contains("(")){
				if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_contains_numerals) + " " + Controller.resources.getString(R.string.no) + ".<br>";
				input = input.replace(" ", "");
				String[] split = input.split("[()]");
				int anionCharge = Controller.convertNumeralToInt(split[1]);
				if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_roman_numeral) + " " + split[1] + " " + Controller.resources.getString(R.string.nomenclature_recognized_as) + " " + anionCharge + ": " + Controller.resources.getString(R.string.nomenclature_anion_charge) + ".<br>";
				
				ElementGroup anion = new ElementGroup(new ElementSet(Element.getElement(split[0]), 1));
				if(addReason) problem.reason += Controller.resources.getString(R.string.converting) + " " + split[0] + " " + Controller.resources.getString(R.string.to) + " " + anion.getDrawString() + " (" + Controller.resources.getString(R.string.nomenclature_anion) + ").<br>";
				ElementGroup cation;
				if(split[2].contains("ide")) cation = revertEnding(split[2], true);
				else cation = revertEnding(split[2], false);
				
				if(addReason) problem.reason += Controller.resources.getString(R.string.converting) + " " + split[2] + " " + Controller.resources.getString(R.string.to) + " " + cation.getDrawString() + " (" + Controller.resources.getString(R.string.nomenclature_cation) + ").<br>";
				
				anion.setCharge(anionCharge);
				
				if((anion.getCharge() + cation.getCharge()) == 0){
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_charges_of) + " " + anion.getDrawString() + " " + Controller.resources.getString(R.string.and) + " " + cation.getDrawString() + " " + Controller.resources.getString(R.string.nomenclature_equals_zero) + "<br>";
					answer = anion.getDrawString() + cation.getDrawString();
				}
				else{
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_charges_of) + " " + anion.getDrawString() + " " + Controller.resources.getString(R.string.and) + " " + cation.getDrawString() + " " + Controller.resources.getString(R.string.nomenclature_not_equals_zero) + "<br>";
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_setting_charges_opposite) + "<br>";
					int cationCharge = cation.getCharge();
					
					anion.setQuantity(cationCharge / anion.getQuantity());
					cation.setQuantity(anionCharge / cation.getQuantity());
					
					answer = anion.getDrawString() + cation.getDrawString();
					
				}
				
			}else{
				if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_contains_numerals) + " " + Controller.resources.getString(R.string.no) + ".<br>";
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
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_contains_prefixes) + " " + Controller.resources.getString(R.string.yes) + ".<br>";
					String[] split = input.split(" ");
					ElementGroup anion = revertEnding(split[0], true);
					ElementGroup cation = revertEnding(split[1], true);
					
					if(addReason) problem.reason += Controller.resources.getString(R.string.converting) + " " + split[0] + " " + Controller.resources.getString(R.string.to) + " " + anion.getDrawString() + " (" + Controller.resources.getString(R.string.nomenclature_anion) + ").<br>";
					if(addReason) problem.reason += Controller.resources.getString(R.string.converting) + " " + split[1] + " " + Controller.resources.getString(R.string.to) + " " + cation.getDrawString() + " (" + Controller.resources.getString(R.string.nomenclature_cation) + ").<br>";
					
					answer = anion.getDrawString() + cation.getDrawString();
					
				}else{
					if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_contains_prefixes) + " " + Controller.resources.getString(R.string.yes) + ".<br>";
					String[] split = input.split(" ");
					ElementGroup anion = new ElementGroup(new ElementSet(Element.getElement(split[0]), 1));
					if(addReason) problem.reason += Controller.resources.getString(R.string.converting) + " " + split[0] + " " + Controller.resources.getString(R.string.to) + " " + anion.getDrawString() + " (" + Controller.resources.getString(R.string.nomenclature_anion) + ").<br>";
					ElementGroup cation;
					if(split[1].contains("ide")) cation = revertEnding(split[1], true);
					else cation = revertEnding(split[1], false);
					if(addReason) problem.reason += Controller.resources.getString(R.string.converting) + " " + split[1] + Controller.resources.getString(R.string.to) + cation.getDrawString() + " (" + Controller.resources.getString(R.string.nomenclature_cation) + ").<br>";
					
					if((anion.getCharge() + cation.getCharge()) == 0) {
						if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_charges_of) + " " + anion.getDrawString() + " " + Controller.resources.getString(R.string.and) + " " + cation.getDrawString() + " " + Controller.resources.getString(R.string.nomenclature_equals_zero) + "<br>";
						answer = anion.getDrawString() + cation.getDrawString();
					}
					else{
						if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_charges_of) + " " + anion.getDrawString() + " " + Controller.resources.getString(R.string.and) + " " + cation.getDrawString() + " " + Controller.resources.getString(R.string.nomenclature_not_equals_zero) + "<br>";
						if(addReason) problem.reason += Controller.resources.getString(R.string.nomenclature_setting_charges_opposite) + "<br>";
						int anionCharge = anion.getCharge();
						int cationCharge = cation.getCharge();
						
						anion.setQuantity(cationCharge / anion.getQuantity());
						cation.setQuantity(anionCharge / cation.getQuantity());
						
						answer = anion.getDrawString() + cation.getDrawString();
					}
					
				}
				
			}
		}
		
		if(!answer.equalsIgnoreCase("")) groups = Problem.getElementGroups(Controller.stripHtmlTags(answer));
		
		return groups;
	}
	
}