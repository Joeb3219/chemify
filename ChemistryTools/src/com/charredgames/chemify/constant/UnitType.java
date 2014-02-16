package com.charredgames.chemify.constant;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 15, 2014
 */
public enum UnitType {

	LENGTH, MASS, TIME, TEMPERATURE, AMOUNT_SUBSTANCE, FORCE, PRESSURE, ;
	
	public Unit baseUnit = null;
	
	public static Unit getBaseUnit(UnitType type){
		if(type.baseUnit != null) return type.baseUnit;
		for(Unit unit : Unit.units){
			if(unit.type == type && unit.base) {
				type.baseUnit = unit;
				return type.baseUnit;
			}
		}
		return Unit.GRAM;
	}
	
}
