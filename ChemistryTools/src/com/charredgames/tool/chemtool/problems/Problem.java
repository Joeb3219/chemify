package com.charredgames.tool.chemtool.problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import com.charredgames.tool.chemtool.Controller;
import com.charredgames.tool.chemtool.constant.Element;
import com.charredgames.tool.chemtool.constant.ElementGroup;
import com.charredgames.tool.chemtool.constant.ElementSet;
import com.charredgames.tool.chemtool.constant.Ion;
import com.charredgames.tool.chemtool.gui.ResponsePanel;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */
public abstract class Problem {

	protected String input;
	protected ResponsePanel response;
	protected ArrayList<ElementGroup> elementGroups;
	protected String leftSide, rightSide;
	protected ResponseType type = ResponseType.nomenclature;
	
	public Problem(String input){
		this.input = input;
		response = new ResponsePanel();
	}
	
	public Problem(ArrayList<ElementGroup> elementGroups){
		this.elementGroups = elementGroups;
		response = new ResponsePanel();
	}
	
	public String getInput(){
		return input;
	}
	
	public void solve(boolean isPrimary){
		
	}
	
	public ResponsePanel getResponse(){
		return response;
	}
	
	public ArrayList<Element> getElements(String string){
		ArrayList<Element> elements = new ArrayList<Element>();
		String current = null;
		for(int i = 0; i < string.length(); i++){
			Character cha = string.charAt(i);
			if(Character.isUpperCase(cha)) {
				if(current != null){
					elements.add(Element.getElement(current));
				}
				current = cha.toString();
			}
			else if(Character.isLowerCase(cha)) {
				if(current != null) current = current + cha.toString();
				else current = cha.toString();
			}
		}
		elements.add(Element.getElement(current));
		return elements;
	}
	
	public double getMolecularWeight(ArrayList<ElementSet> elementSet){
		double weight = 0;
		for(ElementSet set : elementSet){
			weight += set.getWeight();
		}
		return weight;
	}
		
	public static ArrayList<ElementGroup> getElementGroups(String string){
		ArrayList<ElementGroup> elementGroups = new ArrayList<ElementGroup>();
		
		if(string.contains("(")){
			String[] groups = string.split("[()]");
			for(String group : groups){
				if(group.equals("") || group.equals(" ")) continue;
				
				boolean containsPolyatomics = false;
				for(Ion ion : Ion.ions){
					if(group.contains(ion.getElementString())) {
						
						//Let's get the total number of polys in this group
						String quantity = ""; //The same whimpy way of calculating a quantity based on Integer.parse.
						int quan = 1;
						int position = string.indexOf(group) + group.length() + 1;
						if(position < string.length()){
							for(int i = position; i < string.length(); i ++){
								Character cha = string.charAt(i);
								if(cha >= '0' && cha <= '9') quantity = quantity + cha.toString();
								else break;
							}
							
							if(!quantity.equals("") && !quantity.equals(" ") && !quantity.equalsIgnoreCase("0")){
								quan = Integer.parseInt(quantity);
							}
						}else quan = 1;
						
						containsPolyatomics = true;
						ElementGroup g = new ElementGroup(getElementSets(ion.getElementString()));
						g.setCharge(ion.getCharge());
						g.setIon(ion);
						g.setQuantity(quan);
						elementGroups.add(g);
						group.replace(ion.getElementString(), "");
					}
				}
				if(!containsPolyatomics) elementGroups.add(new ElementGroup(getElementSets(group)));
				string.replace(group, ""); //Allows for the quantity approach to work if same thing is repeated in input.
			}
		}else{
			boolean containsPolyatomics = false;
			for(Ion ion : Ion.ions){
				if(string.contains(ion.getElementString())) {
					containsPolyatomics = true;
					ElementGroup group = new ElementGroup(getElementSets(ion.getElementString()));
					group.setCharge(ion.getCharge());
					group.setIon(ion);
					elementGroups.add(group);
					string.replace(ion.getElementString(), "");
				}
			}
			
			if(!containsPolyatomics) elementGroups.add(new ElementGroup(getElementSets(string)));
		}
		
		//Organize groups so that the polyatomics always follow non polyatomics
		Collections.sort(elementGroups);
		
		return elementGroups;
	}
	
	public static ArrayList<ElementSet> getElementSets(String string){
		ArrayList<ElementSet> elementSets = new ArrayList<ElementSet>();
		String[] upperSplit = string.split("(?=\\p{Upper})");
		for(String str : upperSplit){
			String current = "", quantity = "0"; //Quantity is a sloppy way of converting string to int.
			int quan = 1;
			for(int i = 0; i < str.length(); i++){
				Character cha = str.charAt(i);
				if(cha >= '0' && cha <= '9') quantity = quantity + cha.toString();
				else current = current + cha.toString();
			}
			if(current.equals("") || current.equals(" ") || current == null) continue;
			if(!quantity.equalsIgnoreCase("0")){
				quan = Integer.parseInt(quantity);
			}
				elementSets.add(new ElementSet(Element.getElement(current), quan));
			
		}
		
		return elementSets;
	}
	
	public String normalizeString(String string, boolean upper){
		String str = string.toLowerCase();
		if(upper) return str.substring(0,1).toUpperCase() + str.substring(1);
		return str;
	}
	
	public String changeEnding(String string, String ending){
		String str = string.toLowerCase();
		string = str.substring(0,1).toUpperCase() + str.substring(1);
		if(string.contains("orus")) return string.replace("orus", ending);
		else if(string.contains("ygen")) return string.replace("ygen", ending);
		else if(string.contains("ogen")) return string.replace("ogen", ending);
		else if(string.contains("ium")) return string.replace("ium", ending);
		else if(string.contains("ine")) return string.replace("ine", ending);
		else if(string.contains("ur")) return string.replace("ur", ending);
		else if(string.contains("on")) return string.replace("on", ending);
		return "Error 11";
	}

	public ElementGroup revertEnding(String string, boolean binary){
		ElementGroup group = new ElementGroup();
		if(string.contains("ate")) string = string.replace("ate", "");
		if(string.contains("ite")) string = string.replace("ite", "");
		if(!binary){
			if(string.contains("ous")) string = string.replace("ous", "ite");
			if(string.contains("ic")) {
				if(string.contains("ur")) string = string.replace("ur", "");
				if(string.contains("or")) string = string.replace("or", "");
				string = string.replace("ic", "ate");
			}
		}else{
			if(string.contains("ous")) string = string.replace("ous", "");
			if(string.contains("ic")) string = string.replace("ic", "");
		}
		if(string.contains("ide")) string = string.replace("ide", "");
		
		if(!binary){
			for(Ion ion : Ion.ions){
				if(ion.getName().toLowerCase().contains(string)){
					group = new ElementGroup(ion.getElementSet());
					group.setIon(ion);
					return group;
				}
			}
		}else{
			for(Element element : Element.elements){
				if(element.getName().toLowerCase().contains(string)){
					for(Prefix prefix : Controller.prefixes){
						if(string.contains(prefix.getPrinted()) || string.contains(prefix.getSecondary())){
							group.addElementSet(new ElementSet(element, prefix.getValue()));
							return group;
						}
					}
					group.addElementSet(new ElementSet(element, 1));
					return group;
				}
			}
		}
		if(group.getElementCount() == 0) group.addElementSet(new ElementSet(Element.HYDROGEN, 1));
		return group;
	}
	
	public String stripHtmlTags(String str){
		if(str.contains("<sub>")) str = str.replace("<sub>", "");
		if(str.contains("</sub>")) str = str.replace("</sub>", "");
		if(str.contains("<sup>")) str = str.replace("<sup>", "");
		if(str.contains("</sup>")) str = str.replace("</sup>", "");
		if(str.contains("<br>")) str = str.replace("<br>", "");
		return str;
	}
	
	public String getAcidEnding(String string, boolean binary){
		String str = string.toLowerCase();
		string = str.substring(0,1).toUpperCase() + str.substring(1);
		if(!binary){
			if(string.contains("ate")) return string.replace("ate", "ic");
			else if(string.contains("ite")) return string.replace("ate", "ous");
		}else{
			return changeEnding(string, "ic").toLowerCase();
		}
		return "Error 12";
	}
	
	public boolean mapContainsInteger(Map<Element, Integer> map, int num){
		for(Entry<Element, Integer> e : map.entrySet()){
			if(e.getValue() == num) return true;
		}
		return false;
	}
	
	public void addProblemToPanel(ResponsePanel panel, Problem problem){
		problem.solve(false);
		for(String str : problem.getResponse().getResponses()) panel.addLine(str);
	}

	public ResponseType getType(){
		return type;
	}
}