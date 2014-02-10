package com.charredgames.chemify.constant;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.problems.Problem;

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
	
	public String getDrawStringWithAllCharges(){
		normalizeCompound();
		String str = "";
		if(moles != 1) str += moles;
		for(ElementGroup group : elementGroups){
			str += group.getDrawString();
		}
		
		int charge = getOverallCharge();
		if(charge != 0){
			String c = "";
			if(charge == 1) c = "+";
			else if(charge == -1) c = "-";
			else if(charge > 1) c = "+" + charge;
			else c = "" + charge;
			str += "<sup>" + c + "</sup>";
		}
		
		return str;
	}
	
	public String getDrawString(){
		normalizeCompound();
		String str = "";
		if(moles != 1) str += moles;
		for(ElementGroup group : elementGroups){
			str += group.getDrawString();
		}
		
		int charge = getOverallCharge();
		if(charge != 0 && (!containsOnlyM() && !containsOnlyNM())) str += "<sup>" + charge + "</sup>";
		
		return str;
	}
	
	public String getDrawString(boolean showMoles){
		if(showMoles) return getDrawString();
		String str = "";
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
		
		if(allElementsInGroup(1) || containsOnlyG1AndIon(Controller.getIon("OH")) || containsPolyatomic(Controller.getIon("NH4")) || containsOnlyG1AndIon(Controller.getIon("PO4"))){
			soluble = 1;
			return true;
		}
		if(containsPolyatomic(Controller.getIon("NO3")) || containsPolyatomic(Controller.getIon("ClO3")) || 
				containsPolyatomic(Controller.getIon("ClO4")) || containsPolyatomic(Controller.getIon("CH3COO"))){
			soluble = 1;
			return true;
		}
		if((containsElement(Element.BROMINE) || containsElement(Element.CHLORINE) || containsElement(Element.IODINE))
				&& !(containsElement(Element.SILVER) || containsElement(Element.LEAD) || containsElement(Element.MERCURY))){
			soluble = 1;
			return true;
		}
		if (containsPolyatomic(Controller.getIon("SO4"))){
			if(!containsElement(Element.BARIUM) && !containsElement(Element.STRONTIUM) && !containsElement(Element.CALCIUM) &&
				!containsElement(Element.LEAD) && !containsElement(Element.MERCURY)){
				soluble = 1;
				return true;
				}
		}
		if(containsOnlyGroupAndElement(1, Element.SULFUR) || containsOnlyGroupAndElement(2, Element.SULFUR)){
			soluble = 1;
			return true;
		}
		if(containsOnlyG1AndIon(Controller.getIon("SO3")) || containsOnlyG1AndIon(Controller.getIon("CO3")) ||
				containsOnlyG1AndIon(Controller.getIon("CrO3")) || containsOnlyG1AndIon(Controller.getIon("PO3"))){
			soluble = 1;
			return true;
		}
		soluble = 0;
		return false;
	}

	public boolean containsOnlyElementsInCompound(Compound c){
		ArrayList<Element> these = new ArrayList<Element>();
		ArrayList<Element> those = new ArrayList<Element>();
		
		for(ElementGroup g : getElementGroups()){
			for(ElementSet set : g.getElementSets()){
				if(!these.contains(set.getElement())) these.add(set.getElement());
			}
		}
		
		for(ElementGroup g : getElementGroups()){
			for(ElementSet set : g.getElementSets()){
				if(!those.contains(set.getElement())) those.add(set.getElement());
			}
		}
		
		if(these.size() > those.size()) return false;
		
		for(Element element : these){
			if(!those.contains(element)) return false;
		}
		
		return true;
	}
	
	public Compound cloneCompound(){
		ArrayList<ElementGroup> grps = new ArrayList<ElementGroup>();
		for(ElementGroup g : elementGroups){
			ArrayList<ElementSet> sets = new ArrayList<ElementSet>();
			for(ElementSet set : g.getElementSets()){
				sets.add(new ElementSet(set.getElement(), set.getQuantity()));
			}
			grps.add(new ElementGroup(sets));
		}
		return new Compound(grps);
	}

	public ArrayList<Compound> breakIntoIons(){
		normalizeCompound();
		ArrayList<Compound> compounds = new ArrayList<Compound>();
		if(!isSoluble()) compounds.add(this);
		else{
			for(ElementGroup g : elementGroups){
				if(g.isPolyatomic()) {
					ElementGroup grp = new ElementGroup(g.getIon().getElementSet());
					grp.setIon(g.getIon());
					Compound cmp = new Compound(grp);
					cmp.setMoles(getMoles() * g.getQuantity());
					compounds.add(cmp);
				}
				else{
					for(ElementSet set : g.getElementSets()){
						Compound cmp = new Compound(new ElementGroup(new ElementSet(set.getElement(), 1)));
						cmp.setMoles(set.getQuantity() * g.getQuantity() * getMoles());
						compounds.add(cmp);
					}
				}
			}
		}
		return compounds;
	}
	
	private void normalizeCompound(){
		String drawString = "";
		
		for(ElementGroup group : elementGroups){
			drawString += group.getDrawString();
		}
		
		if(drawString.equals("(OH)<sub>2</sub>H<sub>2</sub>")){
			elementGroups = new ArrayList<ElementGroup>();
			
			elementGroups.add(new ElementGroup(new ElementSet(Element.HYDROGEN, 2)));
			elementGroups.add(new ElementGroup(new ElementSet(Element.OXYGEN, 1)));
		}
	}
	
}
