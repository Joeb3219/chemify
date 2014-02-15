package com.charredgames.chemify.problems;

import com.charredgames.chemify.constant.Element;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */
public class ElementInfo extends Problem{

	protected ResponseType type = ResponseType.element_info;
	
	public ElementInfo(String input) {
		super(input);
	}

	public void solve(boolean isPrimary){
		Element element = null;
		element = Element.getElement(input);

		String output = "";
		
		if(element == null) output += "No element found.";
		else{
			output += "Name: " + element.getName() + "<br>";
			output += "Symbol: " + element.getSymbol() + "<br>";
			output += "Atomic #: " + element.getAtomicNumber() + "<br>";
			output += "Mass: " + element.getAtomicMass() + "g/mole<br>";
			output += "Period, Group: " + element.getPeriod() + ", " + element.getGroup() + "<br>";
			output += "Protons: " + element.getProtons() + "<br>";
			output += "Neutrons: " + element.getNeutrons() + "<br>";
			output += "Electrons: " + element.getElectrons() + "<br>";
			output += "Valence Electrons: " + element.getValence() + "<br>";
			output += "Charge: " + element.getCharge() + "<br>";
			output += "Electron Config: " + element.getElectronConfig();
		}
		
		if(getElements(input).size() != 1) output = "Multiple elements entered.";

		if(isPrimary){
			response.addLine(input, ResponseType.input);
			response.addLine(output, ResponseType.answer);
		}else{
			response.addLine(output, ResponseType.element_info);
		}
		
	}
	
	
}
