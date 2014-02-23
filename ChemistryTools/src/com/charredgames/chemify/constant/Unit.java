package com.charredgames.chemify.constant;

import java.util.ArrayList;
import java.util.Arrays;

import com.charredgames.chemify.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 15, 2014
 */
public enum Unit {

	//Base units first.
	GRAM("gram", 1.00, UnitType.MASS, true, new ArrayList<String>(Arrays.asList("g", "gram", "grams"))),
	SECOND("second", 1.00, UnitType.TIME, true, new ArrayList<String>(Arrays.asList("s", "second", "seconds"))),
	METER("meter", 1.00, UnitType.LENGTH, true, new ArrayList<String>(Arrays.asList("m", "meter", "meters"))),
	KELVIN("kelvin", 1.00, UnitType.TEMPERATURE, true, new ArrayList<String>(Arrays.asList("K", "kelvin", "kelvin"))),
	MOLE("mole", 1.00, UnitType.AMOUNT_SUBSTANCE, true, new ArrayList<String>(Arrays.asList("mol", "mole", "moles"))),
	COULOMB("coulomb", 1.00, UnitType.ELECTRICAL_CHARGE, true, new ArrayList<String>(Arrays.asList("C", "coulomb", "coulombs"))),
	
	
	KILOGRAM("kilogram", 0.001, UnitType.MASS, false, new ArrayList<String>(Arrays.asList("kg", "kilogram", "kilograms"))),
	MILLIGRAM("milligram", 1000, UnitType.MASS, false, new ArrayList<String>(Arrays.asList("mg", "milligram", "milligrams"))),
	
	
	
	
	;
	
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
		Controller.units.add(this);
	}
	
	public String getCommonAbbreviation(){
		return abbreviations.get(0);
	}
	
	public static Unit getUnitFromString(String str){
		str = str.replace(" ", "");
		
		System.out.println(str);
		
		for(Unit unit : Controller.units){
			for(String symbol : unit.abbreviations){
				if(symbol.equalsIgnoreCase(str)) return unit;
			}
		}
		return Unit.GRAM;
	}
	
}
