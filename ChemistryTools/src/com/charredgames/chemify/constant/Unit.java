package com.charredgames.chemify.constant;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 15, 2014
 */
public enum Unit {

	GRAM("gram", 1.00, UnitType.MASS, true, new ArrayList<String>(Arrays.asList("g", "gram", "grams"))),
	
	
	;
	
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	public String name;
	public ArrayList<String> abbreviations;
	public double value;
	public UnitType type;
	public boolean base;
	
	private Unit(String name, double value, UnitType type, boolean base, ArrayList<String> abbr){
		this.value = value;
		this.type = type;
		this.base = base;
		this.name = name;
		this.abbreviations = abbr;
	}
	
}
