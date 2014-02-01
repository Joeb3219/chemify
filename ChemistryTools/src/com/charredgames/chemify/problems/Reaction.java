package com.charredgames.chemify.problems;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;

public class Reaction extends Problem{

	public Reaction(String input) {
		super(input);
	}

	public void solve(boolean isPrimary){
		String answer = "", reason = "{reason}", collectiveInput = input;
		ArrayList<Compound> compounds = getCompoundsFromString(input);
		ArrayList<Compound> answerCompounds = new ArrayList<Compound>();
		ArrayList<ElementGroup> allElementGroups = new ArrayList<ElementGroup>();
		ArrayList<Element> allElements = new ArrayList<Element>();
		for(Compound c : compounds) allElementGroups.addAll(c.getElementGroups());
		for(ElementGroup group : allElementGroups) for(ElementSet set : group.getElementSets()) allElements.add(set.getElement());
		
		//Decomposition
		if(compounds.size() == 1){
			reason += "Only one reactant -> decomposition. <br>";
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
				reason += "Two elements: (Synthesis) A + B -> AB<br>";
				Compound finalCompound;
				//NM + water == acid, M + water == metallic hydroxide
				if(secondCompound.isWater()){
					ArrayList<ElementGroup> finalGroups = new ArrayList<ElementGroup>();
					if(firstCompound.isMetal()){
						reason += "Metal + Water -> Metalic Hydroxide<br>";
						ElementGroup hydroxide = new ElementGroup(Controller.getIon("OH").getElementSet());
						hydroxide.setIon(Controller.getIon("OH"));
						ElementGroup metal = new ElementGroup(new ElementSet(firstCompound.getElementGroups().get(0).getElementSets().get(0).getElement(), 1));
						finalGroups.add(metal);
						finalGroups.add(hydroxide);
					}else{
						reason += "Nonmetal + Water -> Acid<br>";
						ElementGroup hydrogen = new ElementGroup(new ElementSet(Element.HYDROGEN, 1));
						ElementGroup secondary = new ElementGroup();
						secondary.addElementSet(firstCompound.getElementGroups().get(0).getElementSets().get(0));
						secondary.addElementSet(new ElementSet(Element.OXYGEN, 1));
						finalGroups.add(hydrogen);
						finalGroups.add(secondary);
					}
					finalCompound = new Compound(finalGroups);
					
					if(finalCompound.getOverallCharge() != 0) finalCompound = correctAtomCount(finalCompound);
					
					answerCompounds.add(finalCompound);
					
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
					
					answerCompounds.add(finalCompound);
					
				}
			}
			//Redox (single)
			else if(compounds.size() == 2 && ( (firstCompound.getNumberOfElements() == 1 && secondCompound.getNumberOfElements() == 2) || (firstCompound.getNumberOfElements() == 2 && secondCompound.getNumberOfElements() == 1))){
				
			}
		}
		
		balanceEquation(compounds, answerCompounds);
		
		for(int i = 0; i < compounds.size(); i ++){
			answer += compounds.get(i).getDrawString();
			if(i < compounds.size() - 1) answer += " + ";
			else answer += " -> ";
		}
		
		for(int i = 0; i < answerCompounds.size(); i ++){
			answer += answerCompounds.get(i).getDrawString();
			if(i < answerCompounds.size() - 1) answer += " + ";
		}
		
		answer += reason;
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
		}else{
			response.addLine(answer, ResponseType.reactions);
		}
	}
	
}
