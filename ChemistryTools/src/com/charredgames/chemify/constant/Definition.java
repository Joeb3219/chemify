package com.charredgames.chemify.constant;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;

public class Definition {

	public String word, definition, example;
	public static ArrayList<Definition> definitions = new ArrayList<Definition>();
	
	public Definition(String word, String definition){
		this.word = word;
		this.definition = definition;
		definitions.add(this);
	}
	
	public void setExample(String example){
		this.example = example;
	}
	
	public String getWord(){
		return word;
	}
	
	public String getDefinition(){
		return definition;
	}
	
	public String getExample(){
		if(example != null) return "<b>Example</b>: <br>" + example;
		return "";
	}
	
	public String getDrawString(){
		String output = "";
		
		output += "<b>" + getWord() + "</b><br>";
		output += getDefinition();
		if(example != null){
			output += "<br><br>";
			output += getExample();
		}
		
		return output;
	}
	
	public static Definition getDefinitionByWord(String word){
		for(Definition d : definitions){
			if(d.getWord().equals(word)) return d;
		}
		return definitions.get(0);
	}
	
	public boolean containsWord(String w){
		w = w.toLowerCase(Controller._LOCALE);
		if(w == null || w.equals("") || w.equals(" ") || getWord().toLowerCase(Controller._LOCALE).contains(w)) return true;
		return false;
	}
	
}
