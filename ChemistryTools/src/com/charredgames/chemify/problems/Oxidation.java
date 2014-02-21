package com.charredgames.chemify.problems;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;
import com.charredgames.chemify.constant.Equation;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Jan 12, 2014
 */
public class Oxidation extends Problem{

	protected ResponseType type = ResponseType.oxidation;
	
	public Oxidation(String input){
		super(input);
	}
	
	public Oxidation(Equation equation){
		super(equation);
	}
	
	public void solve(boolean isPrimary){
		String collectiveInput = input, answer = "";
		if(equation == null){
			if(input.matches("[0-9]+")){
				if(isPrimary){
					answer += Controller.resources.getString(R.string.weight_invalid_input_numbers) + "<br>";
					answer += Controller.resources.getString(R.string.weight_invalid_input_finding_element) + "<br>";
				}
				input = Element.getElement(input).getSymbol();
			}
			equation = getEquationFromString(input);
		}
		if(Controller.autoFormat) collectiveInput = equation.getDrawString(false);
		if(input == null) input = equation.getDrawString(false);
		
		for(Compound c : equation.getAllCompounds()){
			ArrayList<ElementGroup> groups = new ArrayList<ElementGroup>();
			for(ElementGroup g : c.getElementGroups()) groups.add(g);
			
			reason += "<b>" + c.getDrawStringWithAllCharges() + "<b>:<br>";
			
			if(groups.size() == 1 && groups.get(0).getElementCount() == 1){
				reason += Controller.resources.getString(R.string.oxidation_elemental_form) + "<br>";
				groups.get(0).getElementSets().get(0).setOxidationNumber(0);
			}else{
				for(ElementGroup group : groups){
					boolean scanNeeded = false;
					for(ElementSet set : group.getElementSets()){
						Element element = set.getElement();
						if(element == Element.FLUORINE || element == Element.BROMINE || element == Element.IODINE){
							reason += element.getSymbol() + " " + Controller.resources.getString(R.string.oxidation_monatomic_ion) + " -1.<br>";
							set.setOxidationNumber(-1);
						}
						else if(element == Element.OXYGEN){
							reason += element.getSymbol() + " " + Controller.resources.getString(R.string.oxidation_monatomic_ion) + " -2.<br>";
							set.setOxidationNumber(-2);
						}
						else if(element.getGroup() == 1){
							reason += element.getSymbol() + " " + Controller.resources.getString(R.string.oxidation_is_in_group) + " 1: " + Controller.resources.getString(R.string.oxidation_charge_of) + " 1.<br>";
							set.setOxidationNumber(1);
						}
						else if(element.getGroup() == 2){
							reason += element.getSymbol() + " " + Controller.resources.getString(R.string.oxidation_is_in_group) + " 2: " + Controller.resources.getString(R.string.oxidation_charge_of) + " 2.<br>";
							set.setOxidationNumber(1);
						}
						else scanNeeded = true;
					}
					if(scanNeeded){
						//Assign values in a for loop.
						reason += Controller.resources.getString(R.string.oxidation_predicting_via_algebra) + "<br>";
						ArrayList<ElementSet> sets = new ArrayList<ElementSet>();
						for(ElementSet set : group.getElementSets()){
							if(set.getOxidationNumber() == -6) sets.add(set); //-6 is the default for an unknown.
						}
						
						predictViaScan(sets, group);
					}
				}
			}
			
		}
		
		for(Compound c : equation.getAllCompounds()){
			if(equation.getAllCompounds().size() > 1) answer += "<b>" + c.getDrawStringWithAllCharges() + "</b>:<br>";
			for(ElementGroup g : c.getElementGroups()){
				for(ElementSet set : g.getElementSets()){
					answer += set.getDrawString() + ": ";
					if(set.getOxidationNumber() > 0) answer += "+";
					answer += set.getOxidationNumber();
					if(g.getElementSets().get(g.getElementSets().size() - 1) != set) answer += "<br>";
				}
				if(c.getElementGroups().get(c.getElementGroups().size() - 1) != g) answer += "<br>";
			}
			if(equation.getAllCompounds().get(equation.getAllCompounds().size() - 1) != c) answer += "<br>";
		}
		
		answer += reason;
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
			addProblemToPanel(response, new Nomenclature(input));
			addProblemToPanel(response, new Weight(equation));
			addProblemToPanel(response, new Solubility(equation));
		}else{
			response.addLine(answer, ResponseType.oxidation);
		}
		
	}
	
	private void predictViaScan(ArrayList<ElementSet> sets, ElementGroup group){
		int maxLoop = 10;
		int smallestNumber = -5;
		int charge = 0;//group.getCharge();
		if(group.isPolyatomic()) charge = group.getCharge();
		int gQuantity = group.getQuantity();
		
		for(ElementSet set : group.getElementSets()){
			if(!sets.contains(set)) charge -= (set.getOxidationNumber() * set.getQuantity() * group.getQuantity());
		}
		
		if(sets.size() == 1){
			int a = charge / (sets.get(0).getQuantity() * group.getQuantity());
			sets.get(0).setOxidationNumber(a);
			return;
		}
		
		if(sets.size() == 2){
			for(int a = smallestNumber; a <= maxLoop; a++){
				sets.get(0).setOxidationNumber(a);
				for(int b = smallestNumber; b <= maxLoop; b++){
					sets.get(1).setOxidationNumber(b);
					if(numbersEqualCharge(sets, gQuantity, charge)) return;
				}
			}
		}
		
		if(sets.size() == 3){
			for(int a = smallestNumber; a <= maxLoop; a++){
				sets.get(0).setOxidationNumber(a);
				for(int b = smallestNumber; b <= maxLoop; b++){
					sets.get(1).setOxidationNumber(b);
					for(int c = smallestNumber; c <= maxLoop; c++){
						sets.get(2).setOxidationNumber(c);
						if(numbersEqualCharge(sets, gQuantity, charge)) return;
					}
				}
			}
		}
		
		if(sets.size() == 4){
			for(int a = smallestNumber; a <= maxLoop; a++){
				sets.get(0).setOxidationNumber(a);
				for(int b = smallestNumber; b <= maxLoop; b++){
					sets.get(1).setOxidationNumber(b);
					for(int c = smallestNumber; c <= maxLoop; c++){
						sets.get(2).setOxidationNumber(c);
						for(int d = smallestNumber; d <= maxLoop; d++){
							sets.get(3).setOxidationNumber(d);
							if(numbersEqualCharge(sets, gQuantity, charge)) return;
						}
					}
				}
			}
		}
		
		if(sets.size() == 5){
			for(int a = smallestNumber; a <= maxLoop; a++){
				sets.get(0).setOxidationNumber(a);
				for(int b = smallestNumber; b <= maxLoop; b++){
					sets.get(1).setOxidationNumber(b);
					for(int c = smallestNumber; c <= maxLoop; c++){
						sets.get(2).setOxidationNumber(c);
						for(int d = smallestNumber; d <= maxLoop; d++){
							sets.get(3).setOxidationNumber(d);
							for(int e = smallestNumber; e <= maxLoop; e++){
								sets.get(4).setOxidationNumber(e);
								if(numbersEqualCharge(sets, gQuantity, charge)) return;
							}
						}
					}
				}
			}
		}
		
		if(sets.size() == 6){
			for(int a = smallestNumber; a <= maxLoop; a++){
				sets.get(0).setOxidationNumber(a);
				for(int b = smallestNumber; b <= maxLoop; b++){
					sets.get(1).setOxidationNumber(b);
					for(int c = smallestNumber; c <= maxLoop; c++){
						sets.get(2).setOxidationNumber(c);
						for(int d = smallestNumber; d <= maxLoop; d++){
							sets.get(3).setOxidationNumber(d);
							for(int e = smallestNumber; e <= maxLoop; e++){
								sets.get(4).setOxidationNumber(e);
								for(int f = smallestNumber; f <= maxLoop; f++){
									sets.get(5).setOxidationNumber(f);
									if(numbersEqualCharge(sets, gQuantity, charge)) return;
								}
							}
						}
					}
				}
			}
		}
		
	}
	
	private boolean numbersEqualCharge(ArrayList<ElementSet> sets, int gQuantity, int charge){
		int current = 0;
		for(ElementSet set : sets) current += (set.getOxidationNumber() * set.getQuantity() * gQuantity);
		
		if(charge - current == 0) return true;
		return false;
	}
	
}
