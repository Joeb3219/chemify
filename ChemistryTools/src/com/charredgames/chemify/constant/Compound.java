package com.charredgames.chemify.constant;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Jan 22, 2014
 */
public class Compound {

	private ArrayList<ElementGroup> elementGroups = new ArrayList<ElementGroup>();
	private int moles = 1, soluble = -1;
	
	public Compound(ArrayList <ElementGroup> groups, int moles){
		this.elementGroups = groups;
		this.moles = moles;
	}
	
	public Compound(ArrayList<ElementGroup> groups){
		ArrayList<ElementGroup> newGroups = new ArrayList<ElementGroup>();
		for(ElementGroup group : groups) {
			ElementGroup g = new ElementGroup();
			ArrayList<ElementSet> sets = new ArrayList<ElementSet>();
			if(group.isPolyatomic()) {
				sets.addAll(group.getIon().getElementSet());
				g.setIon(group.getIon());
			}
			else{
				for(ElementSet set : group.getElementSets()){
					sets.add(new ElementSet(set.getElement(), set.getQuantity()));
				}
			}
			for(ElementSet set : sets) g.addElementSet(set);
			newGroups.add(g);
		}
		this.elementGroups = newGroups;
	}

	public Compound(ElementGroup group){
		elementGroups.add(group);
	}
	
	public int getMoles(){
		return moles;
	}
	
	public ArrayList<ElementGroup> getElementGroups(){
		return elementGroups;
	}
	
	public double getMass(){
		double mass = 0.00;
		
		for(ElementGroup group : elementGroups) mass += group.getOverallWeight();
		
		return mass * moles;
	}
	
	public void setMoles(int num){
		this.moles = num;
	}
	
	public void addElementGroup(ElementGroup group){
		elementGroups.add(group);
	}
	
	public int getNumberOfElements(){
		int num = 0;
		
		for(ElementGroup group : elementGroups) num += group.getElementSets().size();
		
		return num;
	}
	
	public int getNumberOfMolecules(){
		int num = 0;
		
		for(ElementGroup group : elementGroups){
			if(group.isPolyatomic()) num += 1;
			else num += group.getElementSets().size();
		}
		
		return num;
	}
	
	public int getNumberOfElementGroups(){
		return elementGroups.size();
	}
	
	public boolean containsElement(Element element){
		for(ElementGroup group : elementGroups){
			for(ElementSet set : group.getElementSets()){
				if(set.getElement() == element) return true;
			}
		}
		return false;
	}
	
	public boolean isHydrocarbon(){
		if(elementGroups.size() != 1) return false;
		if(elementGroups.get(0).getElementCount() != 2) return false;
		ArrayList<ElementSet> sets = elementGroups.get(0).getElementSets();
		if(sets.get(0).getElement() == Element.HYDROGEN && sets.get(1).getElement() == Element.CARBON) return true;
		if(sets.get(0).getElement() == Element.CARBON && sets.get(1).getElement() == Element.HYDROGEN) return true;
		return false;
	}
	
	public boolean isWater(){
		if(elementGroups.size() != 1) return false;
		if(elementGroups.get(0).getElementCount() != 2) return false;
		ArrayList<ElementSet> sets = elementGroups.get(0).getElementSets();
		if(sets.get(0).getElement() == Element.HYDROGEN && sets.get(0).getQuantity() == 2 && sets.get(1).getElement() == Element.OXYGEN && sets.get(1).getQuantity() == 1) return true;
		return false;
	}
	
	public boolean isMetal(){
		if(elementGroups.size() != 1) return false;
		if(elementGroups.get(0).getElementSets().size() > 1) return false;
		ArrayList<ElementSet> sets = elementGroups.get(0).getElementSets();
		if(sets.get(0).getElement().getMetalType() == MetalType.METAL) return true;
		return false;
	}
	
	public boolean isNonMetal(){
		if(elementGroups.size() != 1) return false;
		if(elementGroups.get(0).getElementSets().size() > 1) return false;
		ArrayList<ElementSet> sets = elementGroups.get(0).getElementSets();
		if(sets.get(0).getElement().getMetalType() == MetalType.NONMETAL) return true;
		return false;
	}
	
	public boolean containsOnlyNMAndPoly(){
		for(ElementGroup group : elementGroups) if(!group.containsOnlyNM() && !group.isPolyatomic()) return false;
		return true;
	}
	
	public boolean containsOnlyMAndPoly(){
		for(ElementGroup group : elementGroups) if(!group.containsOnlyM() && !group.isPolyatomic()) return false;
		return true;
	}
	
	public boolean containsOnlyG1AndIon(Ion ion){
		for(ElementGroup group : elementGroups){
			if(group.getIon() == null){
				for(ElementSet set : group.getElementSets()){
					if(set.getElement().getGroup() != 1) return false;
				}
			}
			else if(group.getIon() != ion) return false;
		}
		return true;
	}

	public boolean containsOnlyGroupAndElement(int group, Element element){
		for(ElementGroup g : elementGroups){
			for(ElementSet set : g.getElementSets()){
				if(set.getElement() != element && set.getElement().getGroup() != group) return false;
			}
		}
		return true;
	}
	
	public boolean containsOnlyNM(){
		for(ElementGroup group : elementGroups) if(!group.containsOnlyNM()) return false;
		return true;
	}
	
	public boolean containsOnlyM(){
		for(ElementGroup group : elementGroups) if(!group.containsOnlyM()) return false;
		return true;
	}
	
	public String getDrawString(){
		String str = "";
		if(moles != 1) str += moles;
		for(ElementGroup group : elementGroups){
			str += group.getDrawString();
		}
		
		int charge = getOverallCharge();
		if(charge != 0 && (!containsOnlyM() && !containsOnlyNM())) str += "<sup>" + charge + "</sup>";
		
		return str;
	}
	
	public int getOverallCharge(){
		int charge = 0;
		
		for(ElementGroup group : elementGroups){
			charge += group.getCharge();
		}
		
		return charge;
	}
	
	public boolean containsPolyatomic(Ion ion){
		for(ElementGroup group : elementGroups){
			if(group.getIon() != null && group.getIon() == ion) return true;
		}
		return false;
	}
	
	public boolean allElementsInGroup(int group){
		for(ElementGroup g : elementGroups){
			for(ElementSet set : g.getElementSets()){
				if(set.getElement().getGroup() != group) return false;
			}
		}
		return true;
	}

	public boolean isSoluble(){
		if(soluble != -1){
			if(soluble == 1) return true;
			return false;
		}

		
		
		return false;
	}
	
}
