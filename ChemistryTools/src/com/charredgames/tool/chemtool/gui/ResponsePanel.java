package com.charredgames.tool.chemtool.gui;

import java.util.ArrayList;

import com.charredgames.tool.chemtool.problems.ResponseType;

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
		
		//finalLine += "}";
		System.out.println(finalLine);
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
			System.out.println(str);
			master += str + "<br>";
		}
		master += "</center></html>";
		return master;
	}
	
}
