package com.charredgames.tool.chemtool.problems;

import java.util.ArrayList;

import com.charredgames.tool.chemtool.constant.Compound;
import com.charredgames.tool.chemtool.constant.Element;
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
		ArrayList<Element> allElements = new ArrayList<Element>();
		for(Compound c : compounds) allElementGroups.addAll(c.getElementGroups());
		for(ElementGroup group : allElementGroups) for(ElementSet set : group.getElementSets()) allElements.add(set.getElement());
		
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
					
					if(firstCompound.isMetal()){
						
					}else{
						
					}
					
				}else{

					ArrayList<ElementGroup> newGroups = new ArrayList<ElementGroup>();
					for(ElementGroup group : allElementGroups){
						if(group.isPolyatomic()) {
							newGroups.add(new ElementGroup(group.getElementSets()));
							continue;
						}
						ArrayList<ElementSet> newSets = new ArrayList<ElementSet>();
						for(ElementSet set : group.getElementSets()) newSets.add(new ElementSet(set.getElement(), 1));
						newGroups.add(new ElementGroup(newSets));
					}
					
					finalCompound = new Compound(newGroups);
					
					if(finalCompound.getOverallCharge() != 0) finalCompound = correctAtomCount(finalCompound);
					
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
