package com.charredgames.tool.chemtool.constant;

import java.util.ArrayList;

import com.charredgames.tool.chemtool.problems.Problem;

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


}
