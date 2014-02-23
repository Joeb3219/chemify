package com.charredgames.chemify.constant;

import java.util.ArrayList;
import java.util.Arrays;

import com.charredgames.chemify.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 15, 2014
 */
public class Unit {

	//Express base units, then allow for program to generate the rest as needed.
	public static Unit GRAM = new Unit("gram", 1.00, UnitType.MASS, true, new ArrayList<String>(Arrays.asList("g", "gram", "grams")));
	public static Unit SECOND = new Unit("second", 1.00, UnitType.TIME, true, new ArrayList<String>(Arrays.asList("s", "second", "seconds")));
	public static Unit METER = new Unit("meter", 1.00, UnitType.LENGTH, true, new ArrayList<String>(Arrays.asList("m", "meter", "meters")));
	public static Unit KELVIN = new Unit("kelvin", 1.00, UnitType.TEMPERATURE, true, new ArrayList<String>(Arrays.asList("K", "kelvin", "kelvin")));
	public static Unit MOLE = new Unit("mole", 1.00, UnitType.AMOUNT_SUBSTANCE, true, new ArrayList<String>(Arrays.asList("mol", "mole", "moles")));
	public static Unit COULOMB = new Unit("coulomb", 1.00, UnitType.ELECTRICAL_CHARGE, true, new ArrayList<String>(Arrays.asList("C", "coulomb", "coulombs")));
	public static Unit NEWTON = new Unit("newton", 1.00, UnitType.FORCE, true, new ArrayList<String>(Arrays.asList("N", "newton", "newtons")));
	
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
		if(base && type.usesPrefixes){
			for(UnitPrefix prefix : Controller.unitPrefixes){
				new Unit(prefix.name + name, prefix.value * factor, type, 
						false, new ArrayList<String>(Arrays.asList(prefix.abbreviation + abbr.get(0), prefix.name + abbr.get(1), prefix.name + abbr.get(2))));
			}
		}
	}
	
	public String getCommonAbbreviation(){
		return abbreviations.get(0);
	}
	
	public static Unit getUnitFromString(String str){
		str = str.replace(" ", "");
		
		for(Unit unit : Controller.units){
			for(String symbol : unit.abbreviations){
				if(symbol.equals(str)) return unit;
			}
		}
		return Unit.GRAM;
	}
	
}
