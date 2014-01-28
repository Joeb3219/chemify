package com.charredgames.chemify.problems;

import com.charredgames.chemify.Controller;

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
	
}
