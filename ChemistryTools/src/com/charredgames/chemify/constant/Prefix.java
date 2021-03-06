package com.charredgames.chemify.constant;

import com.charredgames.chemify.util.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Jan 20, 2014
 */
public enum Prefix {

	mono("mono", "mon", 1), di("di", "di", 2), tri("tri", "tri", 3), tetra("tetra", "tetr", 4), penta("penta", "pent", 5),
	hexa("hexa", "hex", 6), hepta("hepta", "hept", 7), octa("octa", "oct", 8), nona("nona", "non", 9), deca("deca", "dec", 10);
	
	private int value;
	private String printed, secondaryPrint;
	
	private Prefix(String printed, String secondaryPrint, int value){
		this.printed = printed;
		this.secondaryPrint = secondaryPrint;
		this.value = value;
		Controller.prefixes.add(this);
	}
	
	public String getPrinted(){
		return printed;
	}
	
	public String getSecondary(){
		return secondaryPrint;
	}
	
	public int getValue(){
		return value;
	}
	
	public boolean stringMatchesPrefix(String input){
		if(input.contains(printed)) return true;
		if(input.contains(secondaryPrint)) return true;
		return false;
	}
	
	public String emitPrefix(String string){
		if(string.contains(printed)) return string.replace(printed, "");
		if(string.contains(secondaryPrint)) return string.replace(secondaryPrint, "");
		return string;
	}
	
}
