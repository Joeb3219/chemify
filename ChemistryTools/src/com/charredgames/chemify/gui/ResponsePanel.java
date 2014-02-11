package com.charredgames.chemify.gui;

import java.util.ArrayList;

import com.charredgames.chemify.problems.ResponseType;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */
public class ResponsePanel{

	private ArrayList<String> response = new ArrayList<String>();
	
	public ResponsePanel(){
		super();
	}
	
	public void addLine(String line, ResponseType type){
		String finalLine = "";//"{";
		finalLine += "{" + type.getName() + "}" + line;
		response.add(finalLine);
	}
	
	public void addLine(String line){
		response.add(line);
	}

	public ArrayList<String> getResponses(){
		return response;
	}

	public String getResponse(){
		String master = "<html><center>";
		for(String str : response){
			master += str + "<br>";
		}
		master += "</center></html>";
		return master;
	}
	
}
