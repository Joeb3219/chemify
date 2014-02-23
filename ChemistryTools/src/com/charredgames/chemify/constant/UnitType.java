package com.charredgames.chemify.constant;

import com.charredgames.chemify.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 15, 2014
 */
public enum UnitType {

	LENGTH, TIME, MASS, TEMPERATURE, AMOUNT_SUBSTANCE, ELECTRICAL_CHARGE, 
	FORCE, ENERGY, PRESSURE, POWER, ELECTRICAL_CURRENT, ELECTRICAL_POTENTIAL;
	
	public Unit baseUnit = null;
	
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
	
}
