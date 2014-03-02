package com.charredgames.chemify.constant;

import java.util.ArrayList;

import com.charredgames.chemify.problems.Problem;
import com.charredgames.chemify.util.Controller;

public class Ion{

	public static ArrayList<Ion> ions = new ArrayList<Ion>();
	private ArrayList<ElementSet> elements;
	private String name, elementString;
	private Compound compound;
	private int charge;
	
	public Ion(String elements, String name, int charge){
		this.name = name;
		this.charge = charge;
		this.elementString = elements;
		
		this.elements = Problem.getElementSets(elements);
		
		ElementGroup g = new ElementGroup(this.elements);
		g.setIon(this);
		compound = new Compound(g);
		
		ions.add(this);
	}
	
	public boolean compareElements(String elementInput){
		if(elementInput.equals(elementString)) return true;
		
		return false;
	}

	public String getElementString(){
		return elementString;
	}
	
	public String getName(){
		return name;
	}
	
	public int getCharge(){
		return charge;
	}
	
	public ArrayList<ElementSet> getElementSet(){
		return elements;
	}

	public boolean compoundMatches(Compound compound){
		if(compound.getOverallCharge() != charge) return false;
		
		String str = "";
		for(ElementGroup group : compound.getElementGroups()){
			str += Controller.stripHtmlTags(group.getDrawString());
		}

		if(elementString.equals(str)) return true;
		
		return false;
	}
	
	public boolean containsString(String str){
		if(str == null || str.equals("") || str.equals(" ")) return true;
		if(name.toLowerCase(Controller._LOCALE).contains(str.toLowerCase())) return true;
		if(elementString.toLowerCase(Controller._LOCALE).contains(str.toLowerCase())) return true;
		
		return false;
	}
	
	public String getDrawString(){
		String output = "";
		
		output += "<b>" + Controller.normalizeString(name, true) + "</b><br>";
		String drawString = compound.getDrawStringWithoutCharges();
		drawString = drawString.replace("(", "");
		drawString = drawString.replace(")", "");
		output += "<u>Formula</u>: " + drawString + "<br>";
		output += "<u>Charge</u>:" + charge;
		
		return output;
	}

}
