package com.charredgames.chemify.constant;

import java.util.ArrayList;

import com.charredgames.chemify.problems.Problem;

public class Ion{

	public static ArrayList<Ion> ions = new ArrayList<Ion>();
	private ArrayList<ElementSet> elements;
	private String name, elementString;
	private int charge;
	
	public Ion(String elements, String name, int charge){
		this.name = name;
		this.charge = charge;
		this.elementString = elements;
		
		this.elements = Problem.getElementSets(elements);
		
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
		System.out.println(compound.getOverallCharge() + " " + charge);
		if(compound.getOverallCharge() != charge) return false;
		
		String str = "";
		for(ElementGroup group : compound.getElementGroups()){
			str += Problem.stripHtmlTags(group.getDrawString());
		}

		System.out.println(elementString + " " + str);
		
		if(elementString.equals(str)) return true;
		
		return false;
	}

}
