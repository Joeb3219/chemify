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
	private MatterState state = MatterState.UNKNOWN;
	
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
		sortElements();
	}

	public Compound(ElementGroup group){
		elementGroups.add(group);
		sortElements();
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
		sortElements();
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
	
	private boolean isCommonlySolid(){
		if(elementGroups.size() == 1 && elementGroups.get(0).getElementSets().size() == 1){
			Element element = elementGroups.get(0).getElementSets().get(0).getElement();
			if(element.getMetalType() != MetalType.NONMETAL && element != Element.MERCURY) return true;
		}
		
		if(elementGroups.size() == 1 && elementGroups.get(0).getElementSets().size() == 2){
			if(!isMetal() && !isNonMetal()) return true; //Probably Ionic bonding -> generally solid.
		}
		
		return false;
	}
	
	private boolean isCommonlyGas(){
		if(elementGroups.size() == 1 && elementGroups.get(0).getElementSets().size() == 1){
			Element element = elementGroups.get(0).getElementSets().get(0).getElement();
			int quantity = elementGroups.get(0).getQuantity() * elementGroups.get(0).getElementSets().get(0).getQuantity();
			if(element.getGroup() == 18) return true;
			if(quantity == 2 && (element == Element.HYDROGEN || element == Element.NITROGEN || element == Element.OXYGEN || element == Element.FLUORINE || element == Element.CHLORINE || element == Element.BROMINE || element == Element.IODINE)) return true;
		}
		
		return false;
	}
	
	private boolean isCommonlyLiquid(){
		if(isWater()) return true;
		
		return false;
	}
	
	public MatterState getMatterState(){
		normalizeCompound();
		if(state == MatterState.UNKNOWN){
			if(isCommonlyLiquid()) state = MatterState.LIQUID;
			else if(isCommonlyGas()) state = MatterState.GAS;
			else if(isCommonlySolid()){
				state = MatterState.SOLID;
				if(isSoluble()) state = MatterState.AQUEOUS;
				else state = MatterState.SOLID;
			}
			if(state == MatterState.UNKNOWN){
				state = MatterState.SOLID;
				if(isSoluble()) state = MatterState.AQUEOUS;
				else state = MatterState.SOLID;
			}
		}
		
		return state;
	}
	
	public void setMatterState(MatterState state){
		this.state = state;
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
	
	public String getDrawStringWithoutCharges(){
		normalizeCompound();
		String str = "";
		if(moles != 1) str += moles;
		for(ElementGroup group : elementGroups){
			str += group.getDrawString();
		}
		
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
		if(state == MatterState.UNKNOWN) state = getMatterState();
		if(state == MatterState.AQUEOUS) return true;
		if(soluble != -1){
			if(soluble == 1) return true;
			return false;
		}
		
		if(state != MatterState.SOLID){
			soluble = -1;
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
	
	public boolean containsPolyatomics(){
		for(ElementGroup group : elementGroups){
			if(group.isPolyatomic()) return true;
		}
		return false;
	}
	
	public void reduceAtomCount(){
		ArrayList<Integer> atoms = new ArrayList<Integer>();
		
		for(ElementGroup g : elementGroups) atoms.add(g.getQuantity());
		
		int gcd = Controller.getGCD(atoms);
		if(gcd != 0 && gcd != -1 && gcd != 1){
			for(ElementGroup g : elementGroups) g.setQuantity(g.getQuantity() / gcd);
		}
		
	}
	
	private void sortElements(){
		ArrayList<ElementGroup> newGroups = new ArrayList<ElementGroup>();
		for(ElementGroup group : elementGroups){
			if(newGroups.size() == 0) newGroups.add(group);
			else{
				for(ElementGroup compared : newGroups){
					if(group.isPolyatomic() && !compared.isPolyatomic()) newGroups.add(newGroups.indexOf(compared) + 1, group);
					else if(group.getCharge() > compared.getCharge()) newGroups.add(newGroups.indexOf(compared), group);
					else{
						if(group.getDrawString().compareToIgnoreCase(compared.getDrawString()) < 0) newGroups.add(newGroups.indexOf(compared), group);
						else newGroups.add(newGroups.indexOf(compared) + 1, group);
					}
				}
			}
		}
		
		elementGroups = newGroups;
	}
	
	public void normalizeCompound(){
		sortElements();
		String drawString = "";
		
		for(ElementGroup group : elementGroups){
			drawString += group.getDrawString();
		}
		
		if(drawString.equals("(OH)<sub>2</sub>H<sub>2</sub>") || drawString.equals("H(OH)")){
			elementGroups = new ArrayList<ElementGroup>();
			ElementGroup g = new ElementGroup();
			g.addElementSet(new ElementSet(Element.HYDROGEN, 2));
			g.addElementSet(new ElementSet(Element.OXYGEN, 1));
			elementGroups.add(g);
		}
		else if(drawString.equals("Na<sub>3</sub>Cl<sub>3</sub>")){
			elementGroups = new ArrayList<ElementGroup>();
			ElementGroup g = new ElementGroup();
			g.addElementSet(new ElementSet(Element.SODIUM, 1));
			g.addElementSet(new ElementSet(Element.CHLORINE, 1));
			elementGroups.add(g);
		}
	}
	
}
