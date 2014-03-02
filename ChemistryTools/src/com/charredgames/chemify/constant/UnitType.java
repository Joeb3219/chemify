package com.charredgames.chemify.constant;

import com.charredgames.chemify.util.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 15, 2014
 */
public enum UnitType {

	LENGTH(true), TIME(true), MASS(true), TEMPERATURE(false), AMOUNT_SUBSTANCE(false), ELECTRICAL_CHARGE(true), 
	FORCE(true), ENERGY(true), PRESSURE(true), POWER(true), ELECTRICAL_CURRENT(true), ELECTRICAL_POTENTIAL(true), VOLUME(true);
	
	public Unit baseUnit = null;
	public boolean usesPrefixes = false;
	
	private UnitType(boolean usesPrefixes){
		this.usesPrefixes = usesPrefixes;
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
	
}
