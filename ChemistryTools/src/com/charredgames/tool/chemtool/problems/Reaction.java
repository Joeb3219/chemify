package com.charredgames.tool.chemtool.problems;

import java.util.ArrayList;

import com.charredgames.tool.chemtool.constant.Compound;

public class Reaction extends Problem{

	public Reaction(String input) {
		super(input);
	}

	public void solve(boolean isPrimary){
		String answer = "", collectiveInput = input;
		ArrayList<Compound> compounds = getCompoundsFromString(input);
		
		//Decomposition
		if(compounds.size() == 1){
			
		}
		//Other reactions, makes sure not 0 compounds.
		else if(compounds.size() > 1){
			Compound firstCompound = compounds.get(0);
			Compound secondCompound = compounds.get(1);
			if(compounds.size() == 2){
				if(firstCompound.isWater()){
					Compound temp = secondCompound;
					secondCompound = firstCompound;
					firstCompound = temp;
				}
			}
			//Synthesis
			if(compounds.size() == 2 && (firstCompound.getNumberOfElements() == 1) && (secondCompound.getNumberOfElements() == 1 || secondCompound.isWater())){
				Compound finalCompound;
				//NM + water == acid, M + water == metallic hydroxide
				if(secondCompound.isWater()){
					
				}else{
					
				}
			}
			//Redox (single)
			else if(compounds.size() == 2 && ( (firstCompound.getNumberOfElements() == 1 && secondCompound.getNumberOfElements() == 2) || (firstCompound.getNumberOfElements() == 2 && secondCompound.getNumberOfElements() == 1))){
				
			}
		}
		
		if(isPrimary){
			response.addLine(collectiveInput, ResponseType.input);
			response.addLine(answer, ResponseType.answer);
		}else{
			response.addLine(answer, ResponseType.reactions);
		}
	}
	
}
