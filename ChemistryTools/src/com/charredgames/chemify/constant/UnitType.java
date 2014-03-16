package com.charredgames.chemify.constant;

import java.util.ArrayList;

import com.charredgames.chemify.util.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 15, 2014
 */
public enum UnitType {

	LENGTH("Length", true), TIME("Time", true), MASS("Mass", true), TEMPERATURE("Temperature", false), AMOUNT_SUBSTANCE("Amount of Substance", false), ELECTRICAL_CHARGE("Electrical Charge", true), 
	FORCE("Force", true), ENERGY("Energy", true), PRESSURE("Pressure", true), POWER("Power", true), ELECTRICAL_CURRENT("Electrical Current", true), ELECTRICAL_POTENTIAL("Electrical Potential", true), VOLUME("Volume", true);
	
	public Unit baseUnit = null;
	public String name;
	public boolean usesPrefixes = false;
	
	private UnitType(String name, boolean usesPrefixes){
		this.name = name;
		this.usesPrefixes = usesPrefixes;
		Controller.unitTypes.add(this);
	}
	
	public static Unit getBaseUnit(UnitType type){
		if(type.baseUnit != null) return type.baseUnit;
		for(Unit unit : Controller.units){
			if(unit.type == type && unit.base) {
				type.baseUnit = unit;
				return type.baseUnit;
			}
		}
		return Unit.GRAM;
	}
	
	public ArrayList<Unit> getUnits(){
		ArrayList<Unit> units = new ArrayList<Unit>();
		
		for(Unit unit : Controller.units){
			if(unit.type == this) units.add(unit);
		}
		
		return units;
	}
	
}
