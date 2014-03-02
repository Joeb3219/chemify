package com.charredgames.chemify.problems;

import com.charredgames.chemify.util.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Jan 20, 2014
 */
public enum ResponseType {

	input("input"), answer("answer"), element_info("Element Info"), nomenclature("Nomenclature"), weight("Weight"), 
	dimensional_analysis("Dimensional Analysis"), reactions("Predict Reactions"), oxidation("Oxidation"), thermo("Thermochem"), 
	stoichiometry("Stoichiometry"), hess("Hess Law"), ionic("Complete Ionic"), solubility("Solubility");
	
	private String name;
	
	private ResponseType(String name){
		this.name = name;
		Controller.types.add(this);
	}
	
	public String getName(){
		return name;
	}
	
	public static ResponseType getTypeByString(String str){
		for(ResponseType type : Controller.types){
			if(type.getName().equalsIgnoreCase(str)) return type;
		}
		return ResponseType.input;
	}
	
}
