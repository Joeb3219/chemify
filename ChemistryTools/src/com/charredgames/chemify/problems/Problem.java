package com.charredgames.chemify.problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;
import com.charredgames.chemify.constant.Ion;
import com.charredgames.chemify.gui.ResponsePanel;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */
public abstract class Problem {

	protected String input = null;
	protected ResponsePanel response;
	protected ArrayList<ElementGroup> elementGroups;
	protected String leftSide, rightSide;
	protected ResponseType type = ResponseType.nomenclature;
	
	public Problem(String input){
		this.input = input;
		response = new ResponsePanel();
		if(Ion.ions.size() == 0);
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
	
	public Compound correctAtomCount(Compound compound){
		int maxLoop = 30;
		ArrayList<ElementGroup> finalGroups = compound.getElementGroups();
		
		
		//TODO: Make recursive later. Will allow for infinite sized groups.
		
		if(compound.containsOnlyNM() || compound.containsOnlyM()){
			int fCharge = finalGroups.get(0).getCharge();
			int sCharge = finalGroups.get(1).getCharge();
			finalGroups.get(0).setQuantity(sCharge);
			finalGroups.get(1).setQuantity(fCharge);
			
			return compound;
		}
		
		if(finalGroups.size() == 2){
			for(int a = 0; a <= maxLoop; a++){
				finalGroups.get(0).setQuantity(a);
				for(int b = 0; b <= maxLoop; b++){
					finalGroups.get(1).setQuantity(b);
					if(compound.getOverallCharge() == 0) return compound;
				}
			}
		}
		
		return compound;
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
		
	public ArrayList<Compound> getCompoundsFromString(String string){
		ArrayList<Compound> compounds = new ArrayList<Compound>();
		if(string.contains(" + ")) string = string.replace(" + ", "+");
		String[] compoundStrings = string.split("\\+");
		for(String str : compoundStrings){
			ArrayList<ElementGroup> elementGroups;
			elementGroups = convertNameToElementGroups(str);

			compounds.add(new Compound(elementGroups));
		}
		
		return compounds;
	}
	
	public ArrayList<ElementGroup> convertNameToElementGroups(String input){
		if(!input.contains(" ")) return getElementGroups(input);
		String answer = "";
		ArrayList<ElementGroup> groups = new ArrayList<ElementGroup>();
		groups.add(new ElementGroup(new ElementSet(Element.HYDROGEN, 1)));
		
		input = input.toLowerCase();
		String[] inputGroups = input.split(" ");
		//Binary acid
		if(inputGroups[1].equalsIgnoreCase("acid")){
			if(inputGroups[0].contains("hydro")){
				inputGroups[0] = inputGroups[0].replace("hydro", "");
				ElementGroup acidSet = revertEnding(inputGroups[0], true);
				ElementGroup hydrogen = new ElementGroup(new ElementSet(Element.HYDROGEN, 1));
				
				if((acidSet.getCharge() + hydrogen.getCharge()) == 0) answer = hydrogen.getDrawString() + acidSet.getDrawString();
				else{
					int acidElementCharge = acidSet.getCharge();
					int hydrogenCharge = hydrogen.getCharge();
					hydrogen.setQuantity(acidElementCharge);
					acidSet.setQuantity(hydrogenCharge);
					answer = hydrogen.getDrawString() + acidSet.getDrawString();
				}
			//Non-binary acid.
			}else{
				ElementGroup acidGroup = revertEnding(inputGroups[0], false);
				ElementGroup hydrogen = new ElementGroup(new ElementSet(Element.HYDROGEN, 1));
				
				if((acidGroup.getCharge() + hydrogen.getCharge()) == 0){
					answer = hydrogen.getDrawString() + acidGroup.getDrawString();
				}else{
					int acidGroupCharge = acidGroup.getCharge();
					int hydrogenCharge = hydrogen.getCharge();
					
					hydrogen.setQuantity(acidGroupCharge);
					acidGroup.setQuantity(hydrogenCharge);
					
					answer = hydrogen.getDrawString() + acidGroup.getDrawString();
				}
			}
		//Not an acid.
		}else{
			//Contains roman-numerals
			if(input.contains("(")){
				input = input.replace(" ", "");
				String[] split = input.split("[()]");
				int anionCharge = Controller.convertNumeralToInt(split[1]);
				
				ElementGroup anion = new ElementGroup(new ElementSet(Element.getElement(split[0]), 1));
				ElementGroup cation;
				if(split[2].contains("ide")) cation = revertEnding(split[2], true);
				else cation = revertEnding(split[2], false);
				
				anion.setCharge(anionCharge);
				
				if((anion.getCharge() + cation.getCharge()) == 0) answer = anion.getDrawString() + cation.getDrawString();
				else{
					int cationCharge = cation.getCharge();
					
					anion.setQuantity(cationCharge / anion.getQuantity());
					cation.setQuantity(anionCharge / cation.getQuantity());
					
					answer = anion.getDrawString() + cation.getDrawString();
					
				}
				
			}else{
				boolean usesPrefixes = false;
				for(Prefix prefix : Controller.prefixes){
					if(input.contains(prefix.getPrinted()) || input.contains(prefix.getSecondary())){
						if(!(input.contains("ide") && prefix == Prefix.di) && !(input.contains("sodi") && prefix == Prefix.di)){
							System.out.println(prefix);
							usesPrefixes = true;
							break; 
						}
					}
				}
				
				//Really easy, just decipher prefixes.
				if(usesPrefixes){
					String[] split = input.split(" ");
					ElementGroup anion = revertEnding(split[0], true);
					ElementGroup cation = revertEnding(split[1], true);
					
					answer = anion.getDrawString() + cation.getDrawString();
					
				}else{

					String[] split = input.split(" ");
					ElementGroup anion = new ElementGroup(new ElementSet(Element.getElement(split[0]), 1));
					ElementGroup cation;
					if(split[1].contains("ide")) cation = revertEnding(split[1], true);
					else cation = revertEnding(split[1], false);
					
					if((anion.getCharge() + cation.getCharge()) == 0) answer = anion.getDrawString() + cation.getDrawString();
					else{
						int anionCharge = anion.getCharge();
						int cationCharge = cation.getCharge();
						
						anion.setQuantity(cationCharge / anion.getQuantity());
						cation.setQuantity(anionCharge / cation.getQuantity());
						
						answer = anion.getDrawString() + cation.getDrawString();
					}
					
				}
				
			}
		}
		
		if(!answer.equalsIgnoreCase("")) groups = getElementGroups(stripHtmlTags(answer));
		
		return groups;
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
		//if(string.contains("ate")) string = string.replace("ate", "");
		//if(string.contains("ite")) string = string.replace("ite", "");
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
			ElementSet elementSet = null;
			int quantity = 1;
			for(Prefix prefix : Controller.prefixes){
				if(string.contains(prefix.getPrinted())) {
					quantity = prefix.getValue();
					string = string.replace(prefix.getPrinted(), "");
					break;
				}
				else if(string.contains(prefix.getSecondary())) {
					quantity = prefix.getValue();
					string = string.replace(prefix.getSecondary(), "");
					break;
				}
			}
			
			for(Element element : Element.elements){
				if(element.getName().toLowerCase().contains(string)){
					elementSet = new ElementSet(element, quantity);
					break;
				}
			}
			
			if(elementSet != null){
				group.addElementSet(elementSet);
				return group;
			}
		}
		if(group.getElementCount() == 0) group.addElementSet(new ElementSet(Element.HYDROGEN, 1));
		return group;
	}
	
	public static String stripHtmlTags(String str){
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
	
	public int getGCD(int a, int b){
		if(a == 0 || b == 0) return a+b;
		return getGCD(b, a%b);
	}
	
	public int getGCD(ArrayList<Integer> list){
		int result = list.get(0);
		
		for(int i = 1; i < list.size(); i ++) result = getGCD(result, list.get(i));
		
		return result;
	}
}