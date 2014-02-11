package com.charredgames.chemify.constant;

public enum MatterState {

	GAS("g"), LIQUID("l"), SOLID("s"), AQUEOUS("aq"), UNKNOWN("?"); 
	
	private String symbol;
	
	private MatterState(String symbol){
		this.symbol = symbol;
	}
	
	public String getSymbol(){
		return "(" + symbol + ")";
	}
	
	public String getDrawSymbol(){
		return "<sub>(" + symbol + ")</sub>";
	}
	
}
