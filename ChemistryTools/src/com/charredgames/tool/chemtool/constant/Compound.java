package com.charredgames.tool.chemtool.constant;

import java.util.ArrayList;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Jan 22, 2014
 */
public class Compound {

	private ArrayList<ElementGroup> elementGroups = new ArrayList<ElementGroup>();
	private int moles = 1;
	
	public Compound(ArrayList <ElementGroup> groups, int moles){
		this.elementGroups = groups;
		this.moles = moles;
	}
	
	public Compound(ArrayList<ElementGroup> groups){
		this.elementGroups = groups;
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
	
	public boolean isHydrocarbon(){
		if(elementGroups.size() != 1) return false;
		if(elementGroups.get(0).getElementCount() != 2) return false;
		ArrayList<ElementSet> sets = elementGroups.get(0).getElementSets();
		if(sets.get(0).getElement() == Element.HYDROGEN && sets.get(1).getElement() == Element.CARBON) return true;
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
	
	public String getDrawString(){
		String str = "";
		if(moles != 1) str += moles;
		for(ElementGroup group : elementGroups){
			str += group.getDrawString();
		}
		
		return str;
	}
	
}
