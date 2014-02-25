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
	public static Unit AMPERE = new Unit("ampere", 1.00, UnitType.ELECTRICAL_CHARGE, true, new ArrayList<String>(Arrays.asList("A", "ampere", "amps", "amperes")));
	public static Unit VOLT = new Unit("volt", 1.00, UnitType.ELECTRICAL_POTENTIAL, true, new ArrayList<String>(Arrays.asList("V", "volt", "volts")));
	public static Unit LITRE = new Unit("litre", 1.00, UnitType.VOLUME, true, new ArrayList<String>(Arrays.asList("L", "litre", "litres", "liter", "liters")));
	
	public static Unit CELSIUS = new Unit("celsius", 1.00, UnitType.TEMPERATURE, false, new ArrayList<String>(Arrays.asList("C", "celsius", "celsius")));
	public static Unit FAHRENHEIT = new Unit("fahrenheit", 1.00, UnitType.TEMPERATURE, false, new ArrayList<String>(Arrays.asList("F", "fahrenheit", "fahrenheit")));
	
	public static Unit ATOMS = new Unit("atom", 6.02 * Math.pow(10, 23), UnitType.AMOUNT_SUBSTANCE, false, new ArrayList<String>(Arrays.asList("atom", "atom", "atoms")));
	public static Unit MOLECULES = new Unit("molecule", 6.02 * Math.pow(10, 23), UnitType.AMOUNT_SUBSTANCE, false, new ArrayList<String>(Arrays.asList("molecules", "molecule", "molecules")));
	
	public static Unit TORR = new Unit("torr", 1 / 133.325, UnitType.PRESSURE, false, new ArrayList<String>(Arrays.asList("torr", "torr", "torr")));
	public static Unit ATOMOSPHERES = new Unit("atmosphere", 0.0000098692, UnitType.PRESSURE, false, new ArrayList<String>(Arrays.asList("atm", "atmosphere", "atmospheres")));
	public static Unit MM_HG = new Unit("mmHg", 1 / 133.325, UnitType.PRESSURE, false, new ArrayList<String>(Arrays.asList("mmHg", "mmHg", "mmHg")));
	
	public static Unit MINUTE = new Unit("minute", 1.0 / 60.00, UnitType.TIME, false, new ArrayList<String>(Arrays.asList("min", "minute", "minutes", "mins")));
	public static Unit HOUR = new Unit("hour", 1.0 / (60.0 * 60.0), UnitType.TIME, false, new ArrayList<String>(Arrays.asList("hr", "hour", "hours", "hrs")));
	public static Unit DAY = new Unit("day", 1.0 / (60.0 * 60.0 * 24.0), UnitType.TIME, false, new ArrayList<String>(Arrays.asList("day", "days", "days")));
	public static Unit WEEK = new Unit("week", 1.0 / (60.0 * 60.0 * 24.0 * 7.0), UnitType.TIME, false, new ArrayList<String>(Arrays.asList("wk", "week", "weeks", "wks")));
	public static Unit MONTH = new Unit("month", 1.0 / (60.0 * 60.0 * 24.0 * 7.0 * 4), UnitType.TIME, false, new ArrayList<String>(Arrays.asList("mnth", "month", "months")));
	public static Unit YEAR = new Unit("year", 1.0 / (3.15569 * Math.pow(10, 7)), UnitType.TIME, false, new ArrayList<String>(Arrays.asList("yr", "year", "years")));
	public static Unit DECADE = new Unit("decade", 1.0 / (3.15569 * Math.pow(10, 8)), UnitType.TIME, false, new ArrayList<String>(Arrays.asList("decade", "decade", "decades")));
	public static Unit CENTURY = new Unit("century", 1.0 / (3.15569 * Math.pow(10, 9)), UnitType.TIME, false, new ArrayList<String>(Arrays.asList("century", "century", "centuries")));
	
	public static Unit INCH = new Unit("inch", 39.3701, UnitType.LENGTH, false, new ArrayList<String>(Arrays.asList("in", "inch", "inches")));
	public static Unit FOOT = new Unit("foot", 3.28084, UnitType.LENGTH, false, new ArrayList<String>(Arrays.asList("ft", "foot", "feet")));
	public static Unit YARD = new Unit("yard", 1.09361, UnitType.LENGTH, false, new ArrayList<String>(Arrays.asList("yd", "yard", "yards")));
	public static Unit MILE = new Unit("mile", 0.000621371, UnitType.LENGTH, false, new ArrayList<String>(Arrays.asList("mile", "mile", "miles")));
	public static Unit ACRE = new Unit("acre", (0.000621371) * 640.0, UnitType.LENGTH, false, new ArrayList<String>(Arrays.asList("acre", "acre", "acres")));

	public static Unit GALLON = new Unit("gallon", 1.0 / 3.78541, UnitType.VOLUME, false, new ArrayList<String>(Arrays.asList("gal", "gallon", "gallons")));
	public static Unit QUART = new Unit("quart", 1.0 / 0.946353, UnitType.VOLUME, false, new ArrayList<String>(Arrays.asList("qt", "quart", "quarts")));
	public static Unit PINT = new Unit("pint", 1.0 / 0.473176, UnitType.VOLUME, false, new ArrayList<String>(Arrays.asList("pt", "pint", "pints")));
	public static Unit CUP = new Unit("cup", 1.0 / 0.236588, UnitType.VOLUME, false, new ArrayList<String>(Arrays.asList("cup", "cup", "cups")));
	public static Unit FL_OUNCE = new Unit("fluid ounce", 1.0 / 0.0295735, UnitType.VOLUME, false, new ArrayList<String>(Arrays.asList("floz", "flounce", "flounces")));
	public static Unit TABLESPOON = new Unit("tablespoon", 1.0 / 0.0147868, UnitType.VOLUME, false, new ArrayList<String>(Arrays.asList("tbsp", "tablespoon", "tablespoons")));
	public static Unit TEASPOON = new Unit("teaspoon", 1.0 / 0.00492892, UnitType.VOLUME, false, new ArrayList<String>(Arrays.asList("tsp", "teaspoon", "teaspoons")));
	
	public static Unit OUNCE = new Unit("ounce", 0.0353, UnitType.MASS, false, new ArrayList<String>(Arrays.asList("oz", "ounce", "ounces")));
	public static Unit POUND = new Unit("pound", 0.00220, UnitType.MASS, false, new ArrayList<String>(Arrays.asList("lb", "pound", "pounds")));
	
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
		if(abbreviations.size() != 0) return abbreviations.get(0);
		return name;
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
	
	public String getScientificFactor(){
		return Controller.doubleToScientific(factor);
	}
	
}
