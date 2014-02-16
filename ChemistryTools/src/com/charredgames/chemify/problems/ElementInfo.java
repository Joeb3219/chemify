package com.charredgames.chemify.problems;


import com.charredgames.chemify.Controller;
import com.charredgames.chemify.R;
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
		
		if(element == null) output += Controller.resources.getString(R.string.element_info_none_found);
		else{
			output += Controller.resources.getString(R.string.element_info_name) + ": " + element.getName() + "<br>";
			output += Controller.resources.getString(R.string.element_info_symbol) + ": " + element.getSymbol() + "<br>";
			output += Controller.resources.getString(R.string.element_info_atomic) + ": " + element.getAtomicNumber() + "<br>";
			output += Controller.resources.getString(R.string.element_info_mass) + ": " + element.getAtomicMass() + "g/mole<br>";
			output += Controller.resources.getString(R.string.element_info_period_group) + ": " + element.getPeriod() + ", " + element.getGroup() + "<br>";
			output += Controller.resources.getString(R.string.element_info_protons) + ": " + element.getProtons() + "<br>";
			output += Controller.resources.getString(R.string.element_info_neutrons) + ": " + element.getNeutrons() + "<br>";
			output += Controller.resources.getString(R.string.element_info_electrons) + ": " + element.getElectrons() + "<br>";
			output += Controller.resources.getString(R.string.element_info_valence_electrons) + ": " + element.getValence() + "<br>";
			output += Controller.resources.getString(R.string.element_info_charge) + ": " + element.getCharge() + "<br>";
			output += Controller.resources.getString(R.string.element_info_electron_config) + ": " + element.getElectronConfig();
		}
		
		if(element == Element.HYDROGEN && !input.equalsIgnoreCase("H") && !input.equalsIgnoreCase("hydrogen") && !input.equals("1") && !input.equals("1.01")) output = Controller.resources.getString(R.string.element_info_multiple_entered);
		if(isPrimary){
			response.addLine(input, ResponseType.input);
			response.addLine(output, ResponseType.answer);
		}else{
			response.addLine(output, ResponseType.element_info);
		}
		
	}
	
	
}
