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
		String collectiveInput = "";
		String answer = "Oops, something went wrong.";
		ArrayList<ElementSet> sets = new ArrayList<ElementSet>();
		ArrayList<ElementGroup> groups = new ArrayList<ElementGroup>();
		//Convert name to formula
		if(input.contains(" ")){
			collectiveInput = input;
			input = input.toLowerCase();
			String[] inputGroups = input.split(" ");
			//Binary acid
			if(inputGroups[1].equalsIgnoreCase("acid")){
				if(inputGroups[0].contains("hydro")){
					inputGroups[0] = inputGroups[0].replace("hydro", "");
					ElementGroup acidSet = revertEnding(inputGroups[0], true);
					ElementGroup hydrogen = new ElementGroup(new ElementSet(Element.HYDROGEN, 1));
					
					if((acidSet.getCharge() + hydrogen.getCharge()) == 0) answer = hydrogen.getDrawString() + acidSet.getDrawString();
					else{
						int acidElementCharge = acidSet.getCharge();
						int hydrogenCharge = hydrogen.getCharge();
						hydrogen.setQuantity(acidElementCharge);
						acidSet.setQuantity(hydrogenCharge);
						answer = hydrogen.getDrawString() + acidSet.getDrawString();
					}
				//Non-binary acid.
				}else{
					ElementGroup acidGroup = revertEnding(inputGroups[0], false);
					ElementGroup hydrogen = new ElementGroup(new ElementSet(Element.HYDROGEN, 1));
					
					if((acidGroup.getCharge() + hydrogen.getCharge()) == 0){
						answer = hydrogen.getDrawString() + acidGroup.getDrawString();
					}else{
						int acidGroupCharge = acidGroup.getCharge();
						int hydrogenCharge = hydrogen.getCharge();
						
						hydrogen.setQuantity(acidGroupCharge);
						acidGroup.setQuantity(hydrogenCharge);
						
						answer = hydrogen.getDrawString() + acidGroup.getDrawString();
					}
				}
			//Not an acid.
			}else{
				//Contains roman-numerals
				if(input.contains("(")){
					input = input.replace(" ", "");
					String[] split = input.split("[()]");
					int anionCharge = Controller.convertNumeralToInt(split[1]);
					
					ElementGroup anion = new ElementGroup(new ElementSet(Element.getElement(split[0]), 1));
					ElementGroup cation;
					if(split[2].contains("ide")) cation = revertEnding(split[2], true);
					else cation = revertEnding(split[2], false);
					
					anion.setCharge(anionCharge);
					
					if((anion.getCharge() + cation.getCharge()) == 0) answer = anion.getDrawString() + cation.getDrawString();
					else{
						int cationCharge = cation.getCharge();
						
						anion.setQuantity(cationCharge / anion.getQuantity());
						cation.setQuantity(anionCharge / cation.getQuantity());
						
						answer = anion.getDrawString() + cation.getDrawString();
						
					}
					
				}else{
					boolean usesPrefixes = false;
					for(Prefix prefix : Controller.prefixes){
						if(input.contains(prefix.getPrinted()) || input.contains(prefix.getSecondary())){
							if(!(input.contains("ide") && prefix == Prefix.di) && !(input.contains("sodi") && prefix == Prefix.di)){
								System.out.println(prefix);
								usesPrefixes = true;
								break; 
							}
						}
					}
					
					//Really easy, just decipher prefixes.
					if(usesPrefixes){
						String[] split = input.split(" ");
						ElementGroup anion = revertEnding(split[0], true);
						ElementGroup cation = revertEnding(split[1], true);
						
						answer = anion.getDrawString() + cation.getDrawString();
						
					}else{

						String[] split = input.split(" ");
						ElementGroup anion = new ElementGroup(new ElementSet(Element.getElement(split[0]), 1));
						ElementGroup cation;
						if(split[1].contains("ide")) cation = revertEnding(split[1], true);
						else cation = revertEnding(split[1], false);
						
						if((anion.getCharge() + cation.getCharge()) == 0) answer = anion.getDrawString() + cation.getDrawString();
						else{
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
			groups = getElementGroups(input);
			for(ElementGroup group : groups){
				collectiveInput += group.getDrawString();
				for(ElementSet set : group.getElementSets()) sets.add(set);
			}
			
	
			if(sets.size() == 1 && groups.size() == 1) answer = normalizeString(sets.get(0).getElement().getName(), true);
			else if(sets.size() == 2 && groups.size() < 2){
				if(sets.get(0).getElement().getName().equalsIgnoreCase("Hydrogen")){
					type = BondType.IONIC_ACID;
					answer =  "Hydro" + getAcidEnding(sets.get(1).getElement().getName().toLowerCase(), true) + " Acid";
				}else{
					
					if(sets.get(0).getElement().getMetalType() == MetalType.METAL){
						
						if(sets.get(0).getElement().getGroup() <= 2){
							answer = normalizeString(sets.get(0).getElement().getName(), true) + " " + changeEnding(sets.get(1).getElement().getName(), "ide");
						}else{
							int anionCharge = Math.abs(sets.get(1).getTotalCharge());
							int cationCharge = anionCharge / sets.get(0).getQuantity();
							answer = normalizeString(sets.get(0).getElement().getName(), true) + " (" + Controller.convertIntToNumeral(cationCharge) + ") " + changeEnding(sets.get(1).getElement().getName(), "ide");
						}
						
					}else{
						answer = normalizeString(sets.get(0).getElement().getName(), true) + " " + changeEnding(sets.get(1).getElement().getName(), "ide");
					}
					
				}
			}else{
				if(sets.get(0).getElement().getName().equalsIgnoreCase("Hydrogen")){
					type = BondType.IONIC_ACID;
					if(groups.size() > 1 && groups.get(1).getIon() != null){
						answer = getAcidEnding(groups.get(1).getIon().getName().toLowerCase(), false) + " Acid";
					}else{
						answer = getAcidEnding(sets.get(1).getElement().getName().toLowerCase(), false) + " Acid";
					}
				}
				else if((groups.size() == 2 || groups.size() == 3) && groups.get(0).getElementCount() == 1){
					ElementSet groupOneElement = groups.get(0).getElementSets().get(0);
					String output = normalizeString(groupOneElement.getElement().getName() + " ", true);
					
					if(groupOneElement.getElement().getGroup() > 2){
						int anionCharge = Math.abs(groups.get(1).getCharge());
						int cationCharge = anionCharge / groupOneElement.getQuantity();
						output += "(" + cationCharge + ")";
					}
					
					if(groups.get(1).getIon() != null) output += " " + normalizeString(groups.get(1).getIon().getName(), true);
					else output += "Oops, something went wrong!";
					
					answer = output;
				}
			}
		}
		
		
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