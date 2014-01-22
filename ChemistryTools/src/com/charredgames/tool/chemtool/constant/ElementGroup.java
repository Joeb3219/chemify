package com.charredgames.tool.chemtool.constant;

import java.util.ArrayList;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Jan 8, 2014
 */
public class ElementGroup implements Comparable<ElementGroup>{

	private ArrayList<ElementSet> elementSets;
	private int quantity = 1, charge = 0; //Mostly used if is a polyatomic ion.
	private Ion ion = null;
	
	public ElementGroup(){
		elementSets = new ArrayList<ElementSet>();
	}
	
	public ElementGroup(ArrayList<ElementSet> eGroup){
		this.elementSets = eGroup;
	}
	
	public ElementGroup(ElementSet set){
		elementSets = new ArrayList<ElementSet>();
		elementSets.add(set);
	}
	
	public ArrayList<ElementSet> getElementSets(){
		return elementSets;
	}
	
	public void addElementSet(ElementSet elementSet){
		elementSets.add(elementSet);
	}
	
	public double getOverallWeight(){
		double weight = 0.00;
		for(ElementSet set : elementSets){
			weight += set.getWeight();
		}
		return weight * quantity;
	}
	
	public int getAdditiveCharge(){
		int c = 0;
		
		for(ElementSet set : elementSets) c += set.getTotalCharge();
		
		return c;
	}
	
	public void setCharge(int charge){
		this.charge = charge;
	}
	
	public int getCharge(){
		if(ion != null) return ion.getCharge();
		if(charge != 0) return charge;
		return getAdditiveCharge();
	}
	
	public Ion getIon(){
		return ion;
	}
	
	public void setIon(Ion ion){
		this.ion = ion;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public void setQuantity(int num){
		this.quantity = Math.abs(num);
	}
	
	public int getElementCount(){
		return elementSets.size();
	}
	
	public String getDrawString(){
		String str = "";
		if(ion != null) str += "(";
		for(ElementSet set : elementSets){
			int drawQuantity = set.getQuantity();
			if(ion == null) drawQuantity *= quantity;
			str += set.getElement().getSymbol();
			if(drawQuantity > 1) str += "<sub>" + drawQuantity + "</sub>";
		}
		if(ion != null) str += ")";
		if(ion != null & quantity > 1) str += "<sub>" + quantity + "</sub>";
		return str;
	}


	public int compareTo(ElementGroup another) {
		
		if((this.getIon() == null && another.getIon() == null) || (this.getIon() != null && another.getIon() != null)) return 0;
		if(this.getIon() == null && another.getIon() != null) return -1;
		if(this.getIon() != null && another.getIon() == null) return 1;
		
		return 0;
	}
	
}
