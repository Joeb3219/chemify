package com.charredgames.tool.chemtool.problems;

import java.util.ArrayList;

import com.charredgames.tool.chemtool.constant.Compound;
import com.charredgames.tool.chemtool.constant.ElementGroup;
import com.charredgames.tool.chemtool.constant.ElementSet;

public class Reaction extends Problem{

	public Reaction(String input) {
		super(input);
	}

	public void solve(boolean isPrimary){
		String answer = "", collectiveInput = input;
		ArrayList<Compound> compounds = getCompoundsFromString(input);
		ArrayList<ElementGroup> allElementGroups = new ArrayList<ElementGroup>();
		for(Compound c : compounds) allElementGroups.addAll(c.getElementGroups());
		
		//Decomposition
		if(compounds.size() == 1){
			
		}
		//Other reactions, makes sure not 0 compounds.
		else if(compounds.size() > 1){
			Compound firstCompound = compounds.get(0);
			Compound secondCompound = compounds.get(1);
			if(compounds.size() == 2){
				if(firstCompound.isWater()){
					Compound temp = secondCompound;
					secondCompound = firstCompound;
					firstCompound = temp;
				}
			}
			//Synthesis
			if(compounds.size() == 2 && (firstCompound.getNumberOfElements() == 1) && (secondCompound.getNumberOfElements() == 1 || secondCompound.isWater())){
				Compound finalCompound;
				//NM + water == acid, M + water == metallic hydroxide
				if(secondCompound.isWater()){
					
				}else{
					
					int fcCharge = firstCompound.getOverallCharge();
					int scCharge = secondCompound.getOverallCharge();
					
					if(fcCharge + scCharge == 0){
						finalCompound = new Compound(allElementGroups);
					}else{
						int fcQuantity = scCharge / firstCompound.getElementGroups().get(0).getQuantity();
						int scQuantity = fcCharge / secondCompound.getElementGroups().get(0).getQuantity();
						
						/*ElementGroup firstGroup = firstCompound.getElementGroups().get(0);
						ElementGroup secondGroup = secondCompound.getElementGroups().get(0);
						for(ElementSet set : firstGroup.getElementSets()) set.setQuantity(scCharge / (set.getTotalCharge() * firstGroup.getQuantity()));
						for(ElementSet set : secondGroup.getElementSets()) set.setQuantity(fcCharge / (set.getTotalCharge() * secondGroup.getQuantity()));
						*/
						
						for(ElementGroup group : firstCompound.getElementGroups()) group.setQuantity(fcQuantity);
						for(ElementGroup group : secondCompound.getElementGroups()) group.setQuantity(scQuantity);
						
						ArrayList<Integer> quantities = new ArrayList<Integer>();
						for(ElementGroup group : allElementGroups){
							for(ElementSet set : group.getElementSets()){
								quantities.add(set.getQuantity() * group.getQuantity());
							}
						}
						
						int gcd = getGCD(quantities);
						System.out.println(gcd);
						if(gcd != 1 && gcd != 0){
							for(ElementGroup group : allElementGroups){
								for(ElementSet set : group.getElementSets()){
									System.out.println(set.getQuantity() + " " + gcd + " " + set.getQuantity() / gcd + "::" + group.getQuantity());
									set.setQuantity(set.getQuantity() / gcd);
								}
								group.setQuantity(1);
							}
						}

						finalCompound = new Compound(allElementGroups);
					}
					
					answer = finalCompound.getDrawString();
					
				}
			}
			//Redox (single)
			else if(compounds.size() == 2 && ( (firstCompound.getNumberOfElements() == 1 && secondCompound.getNumberOfElements() == 2) || (firstCompound.getNumberOfElements() == 2 && secondCompound.getNumberOfElements() == 1))){
				
			}
		}
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
		}else{
			response.addLine(answer, ResponseType.reactions);
		}
	}
	
}
