package com.charredgames.chemify.problems;

import java.util.ArrayList;
import java.util.Collections;

import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.Element;
import com.charredgames.chemify.constant.ElementGroup;
import com.charredgames.chemify.constant.ElementSet;
import com.charredgames.chemify.constant.Equation;
import com.charredgames.chemify.constant.Ion;
import com.charredgames.chemify.constant.Prefix;
import com.charredgames.chemify.gui.ResponsePanel;
import com.charredgames.chemify.util.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */
public abstract class Problem {

	protected String input = null;
	protected ResponsePanel response;
	protected ArrayList<ElementGroup> elementGroups;
	protected String leftSide, rightSide, reason = new String("{reason}");
	protected ResponseType type = ResponseType.nomenclature;
	protected Equation equation = null;
	
	public Problem(String input){
		this.input = input;
		response = new ResponsePanel();
		if(Ion.ions.size() == 0);
	}
	
	public Problem(ArrayList<ElementGroup> elementGroups){
		this.elementGroups = elementGroups;
		response = new ResponsePanel();
	}
	
	public Problem(Equation equation){
		this.equation = equation;
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
	
	public String getFormattedDisplay(String input){
		String answer = "";
		input = Controller.replaceReactionSymbols(input);
		String[] sides = input.split("\\>");
		for(int i = 0; i < sides.length; i++){
			String side = sides[i];
			side = Controller.replaceReactionSymbols(side);
			String[] compoundStrings = side.split("\\+");
			for(String str : compoundStrings){

				if(str.contains(" ")) answer += str;
				else{
					ArrayList<Compound> cmps = getCompoundsFromString(str);
					for(Compound c : cmps) answer += c.getDrawStringWithoutCharges();
				}
				
				if(!sides[sides.length - 1].equals(side) && compoundStrings[compoundStrings.length - 1].equals(str)) answer += " &#8652; ";
				if(!compoundStrings[compoundStrings.length - 1].equals(str)) answer += " + ";
				
			}
		}
		return answer;
	}
	
	public static Compound correctAtomCount(Compound compound){
		int maxLoop = 30;
		ArrayList<ElementGroup> finalGroups = compound.getElementGroups();
		//TODO: Make recursive later. Will allow for infinite sized groups.
		
		if(compound.getOverallCharge() == 0) return compound;
		
		for(ElementGroup g : compound.getElementGroups()){
			if(g.isPolyatomic()) continue;
			for(ElementSet set : g.getElementSets()) set.setQuantity(1);
		}
		
		if(finalGroups.size() == 1){
			if(finalGroups.get(0).getElementCount() == 1){
				Element element = finalGroups.get(0).getElementSets().get(0).getElement();
				if(element == Element.HYDROGEN || element == Element.NITROGEN || element == Element.OXYGEN || element == Element.FLUORINE || 
						element == Element.CHLORINE || element == Element.IODINE || element == Element.BROMINE) finalGroups.get(0).getElementSets().get(0).setQuantity(2);
			}
			return compound;
		}
		
		if((compound.containsOnlyNMAndPoly() || compound.containsOnlyMAndPoly())){
			int fCharge = finalGroups.get(0).getCharge();
			int sCharge = finalGroups.get(1).getCharge();
			if(sCharge != fCharge){
				finalGroups.get(0).setQuantity(sCharge);
				finalGroups.get(1).setQuantity(fCharge);
			}else{
				finalGroups.get(0).setQuantity(1);
				finalGroups.get(1).setQuantity(1);
			}
			
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
		else if(finalGroups.size() == 3){
			for(int a = 0; a <= maxLoop; a++){
				finalGroups.get(0).setQuantity(a);
				for(int b = 0; b <= maxLoop; b++){
					finalGroups.get(1).setQuantity(b);
					for(int c = 0; c <= maxLoop; c++){
						finalGroups.get(2).setQuantity(c);
						if(compound.getOverallCharge() == 0) return compound;
					}
				}
			}
		}
		else if(finalGroups.size() == 4){
			for(int a = 0; a <= maxLoop; a++){
				finalGroups.get(0).setQuantity(a);
				for(int b = 0; b <= maxLoop; b++){
					finalGroups.get(1).setQuantity(b);
					for(int c = 0; c <= maxLoop; c++){
						finalGroups.get(2).setQuantity(c);
						for(int d = 0; d <= maxLoop; d++){
							finalGroups.get(3).setQuantity(d);
							if(compound.getOverallCharge() == 0) return compound;
						}
					}
				}
			}
		}
		
		return compound;
	}
		
	public Equation getEquationFromString(String str){
		Equation equation = new Equation();
		str = Controller.replaceReactionSymbols(str);
		String[] sides = str.split("\\>");
		
		for(int i = 0; i < sides.length; i++){
			equation.addCompounds(getCompoundsFromString(sides[i]), i);
		}
		
		return equation;
	}
	
	public ArrayList<Compound> getCompoundsFromString(String string){
		ArrayList<Compound> compounds = new ArrayList<Compound>();
		string = Controller.replaceReactionSymbols(string);
		String[] compoundStrings = string.split("\\+");
		for(String str : compoundStrings){
			ArrayList<ElementGroup> elementGroups;
			elementGroups = Nomenclature.convertNameToElementGroups(str, null);
			Compound c = new Compound(elementGroups);
			c = correctAtomCount(c);
			compounds.add(c);
		}
		
		return compounds;
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
						group = group.replace(ion.getElementString(), "");
					}
				}
				if(!containsPolyatomics) elementGroups.add(new ElementGroup(getElementSets(group)));
				string = string.replace(group, ""); //Allows for the quantity approach to work if same thing is repeated in input.
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
					string = string.replace(ion.getElementString(), "");
				}
			}
			if(containsPolyatomics && (!string.equals("") || !string.equals(" "))) elementGroups.add(new ElementGroup(getElementSets(string)));
			if(!containsPolyatomics) elementGroups.add(new ElementGroup(getElementSets(string)));
		}
		
		for(ElementGroup group : elementGroups) if(group.getElementSets().size() == 0) elementGroups.remove(group);
		//Organize groups so that the polyatomics always follow non polyatomics
		Collections.sort(elementGroups);
		
		//Create a compound so that correctAtomCount can be ran, updated groups.
		correctAtomCount(new Compound(elementGroups));
		
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
	
	public String changeEnding(String string, String ending){
		String str = string.toLowerCase(Controller._LOCALE);
		string = str.substring(0,1).toUpperCase(Controller._LOCALE) + str.substring(1);
		if(string.contains("orus")) return string.replace("orus", ending);
		if(string.contains("ygen")) return string.replace("ygen", ending);
		if(string.contains("ogen")) return string.replace("ogen", ending);
		if(string.contains("ium")) return string.replace("ium", ending);
		if(string.contains("ine")) return string.replace("ine", ending);
		if(string.contains("ur")) return string.replace("ur", ending);
		if(string.contains("on")) return string.replace("on", ending);
		return string + ending;
	}

	public static ElementGroup revertEnding(String string, boolean binary){
		ElementGroup group = new ElementGroup();
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
				if(ion.getName().toLowerCase(Controller._LOCALE).contains(string)){
					group = new ElementGroup(ion.getElementSet());
					group.setIon(ion);
					return group;
				}
			}
		}else{
			ElementSet elementSet = null;
			int quantity = 1;
			for(Prefix prefix : Controller.prefixes){
				if(prefix.stringMatchesPrefix(string)){
					string = prefix.emitPrefix(string);
					quantity = prefix.getValue();
					break;
				}
			}
			
			for(Element element : Element.elements){
				if(element.getName().toLowerCase(Controller._LOCALE).contains(string)){
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
	
	public String getAcidEnding(String string, boolean binary){
		String str = string.toLowerCase(Controller._LOCALE);
		string = str.substring(0,1).toUpperCase(Controller._LOCALE) + str.substring(1);
		if(!binary){
			if(string.contains("ate")) return string.replace("ate", "ic");
			else if(string.contains("ite")) return string.replace("ate", "ous");
		}else return changeEnding(string, "ic").toLowerCase(Controller._LOCALE);
		return string;
	}
	
	public void addProblemToPanel(ResponsePanel panel, Problem problem){
		problem.solve(false);
		for(String str : problem.getResponse().getResponses()) panel.addLine(str);
	}

	public ResponseType getType(){
		return type;
	}
	
}