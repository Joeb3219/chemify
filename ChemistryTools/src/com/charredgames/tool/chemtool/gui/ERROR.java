package com.charredgames.tool.chemtool.gui;

public enum ERROR {

	ILLEGAL_IDE_ITE_ENDING(11, "Illegal IDE/ITE Ending"),
	ILLEGAL_ACID_ENDING(12, "Illegal Acid Ending");
	
	private int id;
	private String name;
	
	private ERROR(int id, String name){
		
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
}
