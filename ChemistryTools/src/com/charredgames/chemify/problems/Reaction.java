package com.charredgames.chemify.problems;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;
import com.charredgames.chemify.constant.Equation;
import com.charredgames.chemify.constant.Ion;
import com.charredgames.chemify.constant.MetalType;

public class Reaction extends Problem{

	public Reaction(String input) {
		super(input);
	}

	public void solve(boolean isPrimary){
		String answer = "", collectiveInput = input;
		if(Controller.autoFormat) collectiveInput = getFormattedDisplay(input);
		
		ArrayList<Compound> compounds = getCompoundsFromString(input);
		ArrayList<Compound> answerCompounds = new ArrayList<Compound>();
		ArrayList<ElementGroup> allElementGroups = new ArrayList<ElementGroup>();
		ArrayList<Element> allElements = new ArrayList<Element>();
		for(Compound c : compounds) allElementGroups.addAll(c.getElementGroups());
		for(ElementGroup group : allElementGroups) for(ElementSet set : group.getElementSets()) allElements.add(set.getElement());

		for(Compound cmp : compounds){
			if(cmp.getOverallCharge() != 0) cmp = correctAtomCount(cmp);
			//else cmp = reduceAtomCount(cmp);
		}
		
		//Decomposition
		if(compounds.size() == 1){
			reason += "Only one reactant -> decomposition. <br>";
			Compound compound = compounds.get(0);
			boolean solved = false;
			if(compound.getElementGroups().get(0).containsOnlyM() && compound.getElementGroups().get(0).getElementCount() == 1){
				reason += "Is the first element a metal? Yes.<br>";
				if(compound.getElementGroups().get(1).isPolyatomic()){
					//ElementSet metal = new ElementSet(compound.getElementGroups().get(0).getElementSets().get(0).getElement(), 1);
					ElementGroup group = compound.getElementGroups().get(1);
					if(Controller.getIon("CO2") == group.getIon() || Controller.getIon("CO3") == group.getIon()){
						reason += "Polyatomic Ion " + group.getIon().getName() + " found.<br>";
						reason += "A metal carbonate yields a metal oxide + CO2.<br>";
						ArrayList<ElementGroup> metalOxide = new ArrayList<ElementGroup>();
						metalOxide.add(new ElementGroup(new ElementSet(compound.getElementGroups().get(0).getElementSets().get(0).getElement(), 1)));
						metalOxide.add(new ElementGroup(new ElementSet(Element.OXYGEN, 1)));
						answerCompounds.add(new Compound(metalOxide));
						if(answerCompounds.get(0).getOverallCharge() == 0){
							//reduceAtomCount(answerCompounds.get(0));
							reason += "Charge of " + answerCompounds.get(0).getDrawString() + " equals zero.<br>";
						}
						else{
							reason += "Charge of " + answerCompounds.get(0).getDrawString() + " does not equal zero.<br>";
							correctAtomCount(answerCompounds.get(0));
						}

						Ion ion = Controller.getIon("CO2");
						ElementGroup co2 = new ElementGroup(ion.getElementSet());
						co2.setIon(ion);
						
						answerCompounds.add(new Compound(co2));
						solved = true;
					}
					else if(Controller.getIon("ClO3") == group.getIon()){
						reason += "Polyatomic Ion " + group.getIon().getName() + " found.<br>";
						reason += "A metal chlorate yields a metal chloride + O2.<br>";
						ArrayList<ElementGroup> metalChlorate = new ArrayList<ElementGroup>();
						metalChlorate.add(new ElementGroup(new ElementSet(compound.getElementGroups().get(0).getElementSets().get(0).getElement(), 1)));
						metalChlorate.add(new ElementGroup(new ElementSet(Element.CHLORINE, 1)));
						answerCompounds.add(new Compound(metalChlorate));
						if(answerCompounds.get(0).getOverallCharge() == 0){
							//reduceAtomCount(answerCompounds.get(0));
							reason += "Charge of " + answerCompounds.get(0).getDrawString() + " equals zero.<br>";
						}
						else{
							reason += "Charge of " + answerCompounds.get(0).getDrawString() + " does not equal zero.<br>";
							correctAtomCount(answerCompounds.get(0));
						}

						ElementGroup oxygen = new ElementGroup(new ElementSet(Element.OXYGEN, 2));
						
						answerCompounds.add(new Compound(oxygen));
						solved = true;
					}
					else if(Controller.getIon("OH") == group.getIon()){
						reason += "Polyatomic Ion " + group.getIon().getName() + " found.<br>";
						reason += "A metal hydroxide yields a metal oxide + H2O.<br>";
						ArrayList<ElementGroup> metalHydroxide = new ArrayList<ElementGroup>();
						metalHydroxide.add(new ElementGroup(new ElementSet(compound.getElementGroups().get(0).getElementSets().get(0).getElement(), 1)));
						metalHydroxide.add(new ElementGroup(new ElementSet(Element.OXYGEN, 1)));
						answerCompounds.add(new Compound(metalHydroxide));
						if(answerCompounds.get(0).getOverallCharge() == 0){
							//reduceAtomCount(answerCompounds.get(0));
							reason += "Charge of " + answerCompounds.get(0).getDrawString() + " equals zero.<br>";
						}
						else{
							reason += "Charge of " + answerCompounds.get(0).getDrawString() + " does not equal zero.<br>";
							correctAtomCount(answerCompounds.get(0));
						}

						ElementGroup water = new ElementGroup();
						water.addElementSet(new ElementSet(Element.HYDROGEN, 2));
						water.addElementSet(new ElementSet(Element.OXYGEN, 1));
						
						answerCompounds.add(new Compound(water));
						solved = true;
					}
				}
			}
			if(!solved){
				reason += "Either no metal or no CO3/ClO3/OH found.<br>";
				for(ElementSet set : compound.getElementGroups().get(0).getElementSets()){
					answerCompounds.add(new Compound(new ElementGroup(new ElementSet(set.getElement(), 1))));
				}
				
				for(Compound c : answerCompounds){
					correctAtomCount(c);
					//reduceAtomCount(c);
				}
			}
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
					reason += "Does the input contain water? Yes.<br>";
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
					//else finalCompound = reduceAtomCount(finalCompound);
					
					answerCompounds.add(finalCompound);
					
				}else{
					reason += "Does the input contain water? No.<br>";
					ArrayList<ElementGroup> newGroups = new ArrayList<ElementGroup>();
					for(ElementGroup group : allElementGroups){
						if(group.isPolyatomic()) {
							ElementGroup grp = new ElementGroup(group.getElementSets());
							newGroups.add(grp);
							reason += "Polyatomic ion " + grp.getDrawString() + " found.<br>";
							continue;
						}
						ArrayList<ElementSet> newSets = new ArrayList<ElementSet>();
						for(ElementSet set : group.getElementSets()) newSets.add(new ElementSet(set.getElement(), 1));
						ElementGroup grp = new ElementGroup(newSets);
						newGroups.add(grp);
						reason += "Converting input string to " + grp.getDrawString() + ".<br>";
					}
					
					finalCompound = new Compound(newGroups);
					
					if(finalCompound.getOverallCharge() == 0) {
						//reduceAtomCount(finalCompound);
						reason += "Charge of " + finalCompound.getDrawString() + " equals zero.<br>";
					}
					else {
						reason += "Charge of " + finalCompound.getDrawString() + " does not equal zero (" + finalCompound.getOverallCharge() + "). Correcting atom counts.<br>";
						finalCompound = correctAtomCount(finalCompound);
					}
					
					answerCompounds.add(finalCompound);
					
				}
			}
			//Combustion
			else if(compounds.size() == 2 && (compounds.get(0).isHydrocarbon() && (compounds.get(1).getNumberOfElements() == 1 && compounds.get(1).getElementGroups().get(0).getElementSets().get(0).getElement() == Element.OXYGEN))){
				reason += "Hydrocarbon + Oxygen yields CO2 + H2O: combustion reaction.<br>";
				ElementGroup co2 = new ElementGroup(Controller.getIon("CO2").getElementSet());
				co2.setIon(Controller.getIon("CO2"));
				answerCompounds.add(new Compound(co2));
				ElementGroup water = new ElementGroup();
				water.addElementSet(new ElementSet(Element.HYDROGEN, 2));
				water.addElementSet(new ElementSet(Element.OXYGEN, 1));
				answerCompounds.add(new Compound(water));
			}
			//Redox (single)
			else if(compounds.size() == 2 && ( (firstCompound.getNumberOfMolecules() == 1 && secondCompound.getNumberOfMolecules() == 2) || (firstCompound.getNumberOfMolecules() == 2 && secondCompound.getNumberOfMolecules() == 1))){
				reason += "One compound has two groups, the other has one: Redox.<br>";
				if(firstCompound.getNumberOfElements() == 1){
					Compound tmp = firstCompound;
					secondCompound = firstCompound;
					firstCompound = tmp;
				}
				Element replacer = secondCompound.getElementGroups().get(0).getElementSets().get(0).getElement();
				Element replaced = firstCompound.getElementGroups().get(0).getElementSets().get(0).getElement();
				if((replacer.getMetalType() == MetalType.NONMETAL || replaced.getMetalType() == MetalType.NONMETAL) || replacer.getActivityNumber() > replaced.getActivityNumber()){
					reason += "Replacing element is either a nonmetal or has a higher activity number.<br>";
					reason += replacer.getName() + " can replace " + replaced.getName() + ".<br>";
					ArrayList<ElementGroup> newGroups = new ArrayList<ElementGroup>();
					newGroups.add(new ElementGroup(new ElementSet(replacer, 1)));
					if(firstCompound.getElementGroups().size() == 1) newGroups.add(new ElementGroup(new ElementSet(firstCompound.getElementGroups().get(0).getElementSets().get(1).getElement(), 1)));
					else{
						ElementGroup g = new ElementGroup();
						if(firstCompound.getElementGroups().get(1).isPolyatomic()) {
							for(ElementSet set : firstCompound.getElementGroups().get(1).getIon().getElementSet()) g.addElementSet(set);
							g.setIon(firstCompound.getElementGroups().get(1).getIon());
						}
						else for(ElementSet set : firstCompound.getElementGroups().get(1).getElementSets()) g.addElementSet(new ElementSet(set.getElement(), 1));
						newGroups.add(g);
					}
					answerCompounds.add(new Compound(newGroups));
					if(answerCompounds.get(0).getOverallCharge() != 0) correctAtomCount(answerCompounds.get(0));
					//else reduceAtomCount(answerCompounds.get(0));
					answerCompounds.add(new Compound(new ElementGroup(new ElementSet(replaced, 1))));
				}else reason += "No reaction: " + replacer.getName() + "'s activity number is less than " + replaced.getName() + "'s.<br>";
			}
			//Double Replacement
			else if(compounds.size() == 2 && firstCompound.getNumberOfMolecules() > 1 && secondCompound.getNumberOfMolecules() > 1){
				reason += "All other checks failed: must be double replacement.<br>";
				
				ArrayList<ElementGroup> c1 = new ArrayList<ElementGroup>();
				ArrayList<ElementGroup> c2 = new ArrayList<ElementGroup>();
				if(firstCompound.getElementGroups().size() == 1){
					ElementGroup fGroup = firstCompound.getElementGroups().get(0);
					c1.add(new ElementGroup(new ElementSet(fGroup.getElementSets().get(1).getElement(), 1)));
					c2.add(new ElementGroup(new ElementSet(fGroup.getElementSets().get(0).getElement(), 1)));
				}else{
					ElementGroup fGroup = firstCompound.getElementGroups().get(0);
					ElementGroup sGroup = firstCompound.getElementGroups().get(1);
					c1.add(sGroup);
					c2.add(fGroup);
				}
				
				if(secondCompound.getElementGroups().size() == 1){
					ElementGroup fGroup = secondCompound.getElementGroups().get(0);
					c1.add(new ElementGroup(new ElementSet(fGroup.getElementSets().get(0).getElement(), 1)));
					c2.add(new ElementGroup(new ElementSet(fGroup.getElementSets().get(1).getElement(), 1)));
				}else{
					ElementGroup fGroup = secondCompound.getElementGroups().get(0);
					ElementGroup sGroup = secondCompound.getElementGroups().get(1);
					c1.add(fGroup);
					c2.add(sGroup);
				}
				
				answerCompounds.add(new Compound(c1));
				answerCompounds.add(new Compound(c2));
				
				correctAtomCount(answerCompounds.get(0));
				//else reduceAtomCount(answerCompounds.get(0));
				correctAtomCount(answerCompounds.get(1));
				//else reduceAtomCount(answerCompounds.get(1));
				
			}
			else reason += "Unknown reaction type.<br>";
		}
		
		equation = new Equation();
		equation.addCompounds(compounds, 0);
		equation.addCompounds(answerCompounds, 1);
		equation.balance();
		
		if(answerCompounds.size() == 0){
			answer += "No reaction.";
		}else{
			//balanceEquation(compounds, answerCompounds);
			
			answer += equation.getDrawString(true);
		}
		
		answer += reason;
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
			addProblemToPanel(response, new Weight(equation));
			addProblemToPanel(response, new Solubility(equation));
			addProblemToPanel(response, new Ionic(equation));
		}else{
			response.addLine(answer, ResponseType.reactions);
		}
	}
	
}
