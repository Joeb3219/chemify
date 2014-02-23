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
	public static Unit JOULE = new Unit("joule", 1.00, UnitType.ENERGY, true, new ArrayList<String>(Arrays.asList("J","joule", "joules")));
	public static Unit PASCAL = new Unit("pascal", 1.00, UnitType.PRESSURE, true, new ArrayList<String>(Arrays.asList("Pa", "pascal", "pascals")));
	public static Unit WATT = new Unit("watt", 1.00, UnitType.POWER, true, new ArrayList<String>(Arrays.asList("W", "watt", "watts")));
	public static Unit AMPERE = new Unit("A", 1.00, UnitType.ELECTRICAL_CHARGE, true, new ArrayList<String>(Arrays.asList("A", "ampere", "amps", "amperes")));
	public static Unit VOLT = new Unit("V", 1.00, UnitType.ELECTRICAL_POTENTIAL, true, new ArrayList<String>(Arrays.asList("V", "volt", "volts")));
	
	
	public static Unit CELSIUS = new Unit("celsius", 1.00, UnitType.TEMPERATURE, false, new ArrayList<String>(Arrays.asList("C", "celsius", "celsius")));
	public static Unit FAHRENHEIT = new Unit("fahrenheit", 1.00, UnitType.TEMPERATURE, false, new ArrayList<String>(Arrays.asList("F", "fahrenheit", "fahrenheit")));
	
	public static Unit ATOMS = new Unit("atoms", 6.02 * Math.pow(10, 23), UnitType.AMOUNT_SUBSTANCE, false, new ArrayList<String>(Arrays.asList("atom", "atom", "atoms")));
	public static Unit MOLECULES = new Unit("molecules", 6.02 * Math.pow(10, 23), UnitType.AMOUNT_SUBSTANCE, false, new ArrayList<String>(Arrays.asList("molecules", "molecule", "molecules")));
	
	public static Unit TORR = new Unit("torr", 1 / 133.325, UnitType.PRESSURE, false, new ArrayList<String>(Arrays.asList("torr", "torr", "torr")));
	public static Unit ATOMOSPHERES = new Unit("atmosphere", 0.0000098692, UnitType.PRESSURE, false, new ArrayList<String>(Arrays.asList("atm", "atmosphere", "atmospheres")));
	public static Unit MM_HG = new Unit("mmHg", 1 / 133.325, UnitType.PRESSURE, false, new ArrayList<String>(Arrays.asList("mmHg", "mmHg", "mmHg")));
	
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
		for(Unit unit : Controller.units){
			for(String symbol : unit.abbreviations){
				if(symbol.equalsIgnoreCase(str)) return unit;
			}
		}
		return Unit.GRAM;
	}
	
}
