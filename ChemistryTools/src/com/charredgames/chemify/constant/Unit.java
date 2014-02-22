package com.charredgames.chemify.constant;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 15, 2014
 */
public enum Unit {

	GRAM("gram", 1.00, UnitType.MASS, true, new ArrayList<String>(Arrays.asList("g", "gram", "grams"))),
	KILOGRAM("kilogram", 0.001, UnitType.MASS, false, new ArrayList<String>(Arrays.asList("kg", "kilogram", "kilograms"))),
	MILLIGRAM("milligram", 1000, UnitType.MASS, false, new ArrayList<String>(Arrays.asList("mg", "milligram", "milligrams"))),
	
	;
	
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	public String name;
	public ArrayList<String> abbreviations;
	public double factor;
	public UnitType type;
	public boolean base;
	
	private Unit(String name, double factor, UnitType type, boolean base, ArrayList<String> abbr){
		this.factor = factor;
		this.type = type;
		this.base = base;
		this.name = name;
		this.abbreviations = abbr;
	}
	
	public String getCommonAbbreviation(){
		return abbreviations.get(0);
	}
	
}
