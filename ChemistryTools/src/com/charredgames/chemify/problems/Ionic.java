package com.charredgames.chemify.problems;

import java.util.ArrayList;

import com.charredgames.chemify.constant.Compound;
import com.charredgames.chemify.constant.Equation;

public class Ionic extends Problem{

	public Ionic(Equation equation) {
		super(equation);
	}

	public Ionic(String input){
		super(input);
	}
	
	public void solve(boolean isPrimary){
		String answer = "", collectiveInput = "";
		Equation complete = new Equation();
		
		if(equation == null){
			equation = getEquationFromString(input);
		}
		
		if(equation.hasProducts()){
		
			for(Compound c : equation.getReactants()){
				complete.addCompounds(c.breakIntoIons(), 0);
			}
			
			for(Compound c : equation.getProducts()){
				complete.addCompounds(c.breakIntoIons(), 1);
			}
			
			reason += "<b>Complete Ionic</b><br>";
			reason += "Breaking compounds into ions.<br><br>";
			answer += "<u>Complete Ionic</u>:<br>";
			answer += complete.getDrawStringWithAllCharges(false) + "<br>";
			
			reason += "<b>Net Ionic</b><br>";
			answer += "<u>Net Ionic</u>:<br>";
			
			ArrayList<Compound> forRemoval = new ArrayList<Compound>();
			
			for(Compound c : complete.getReactants()){
				if(complete.compoundOnBothSides(c)){
					reason += c.getDrawStringWithAllCharges() + " exists on both sides: spectator ion.<br>";
					forRemoval.add(c);
				}
			}
			for(Compound c : forRemoval) complete.removeAllInstancesOfCompound(c);
			complete.balance();
			
			if(complete.getAllCompounds().size() != 0) answer += complete.getDrawStringWithAllCharges(false);
			else answer = "No Reaction/All Compounds Soluble.";
			
		}else{
			reason = "";
			answer = "Please enter a complete reaction.";
		}
		
		answer += reason;
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
		}else{
			response.addLine(answer, ResponseType.ionic);
		}
		
	}
	
}
