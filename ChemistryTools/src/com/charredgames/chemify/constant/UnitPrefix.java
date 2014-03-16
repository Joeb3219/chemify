package com.charredgames.chemify.constant;

import com.charredgames.chemify.util.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 22, 2014
 */
public enum UnitPrefix {

	YOTTA("yotta", "Y", Math.pow(10, -24)),
	ZETTA("zetta", "Z", Math.pow(10, -21)),
	EXA("exa", "E", Math.pow(10, -18)),
	PETA("peta", "P", Math.pow(10, -15)),
	TERA("tera", "T", Math.pow(10, -12)),
	GIGA("giga", "G", Math.pow(10, -9)),
	MEGA("mega", "M", Math.pow(10, -6)),
	KILO("kilo", "k", Math.pow(10, -3)),
	HECTO("hecto", "h", Math.pow(10, -2)),
	DECA("deca", "da", Math.pow(10, -1)),
	//BASE("", "", Math.pow(10, 0)),
	DECI("deci", "d", Math.pow(10, 1)),
	CENTI("centi", "c", Math.pow(10, 2)),
	MILLI("milli", "m", Math.pow(10, 3)),
	MICRO("micro", "µ", Math.pow(10, 6)),
	MICRO_U("micro", "u", Math.pow(10, 6)),
	NANO("nano", "n", Math.pow(10, 9)),
	PICO("pico", "p", Math.pow(10, 12)),
	FEMTO("femto", "f", Math.pow(10, 15)),
	ATTO("atto", "a", Math.pow(10, 18)),
	ZEPTO("zepto", "z", Math.pow(10, 21)),
	YOCTO("yocto", "y", Math.pow(10, 24));
	
	public double value;
	public String name, abbreviation;
	
	private UnitPrefix(String name, String abbreviation, double val){
		this.name = name;
		this.abbreviation = abbreviation;
		this.value = val;
		Controller.unitPrefixes.add(this);
	}
	
	public static boolean containsPrefix(String str){
		for(UnitPrefix prefix : Controller.unitPrefixes){
			//if(prefix == UnitPrefix.BASE) continue;
			if(str.toLowerCase(Controller._LOCALE).contains(prefix.name)) return true;
		}
		return false;
	}
	
}
