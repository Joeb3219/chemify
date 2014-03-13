package com.charredgames.chemify;

import java.util.ArrayList;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Mar 12, 2014
 */
public class ExpandedListGroup {

	public String name;
	public ArrayList<String> items;
	
	public ExpandedListGroup(String name, ArrayList<String> items){
		this.name = name;
		this.items = items;
	}
	
}
