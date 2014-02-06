package com.charredgames.chemify.constant;

import java.util.ArrayList;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 5, 2014
 */
public class Equation {

	private ArrayList<Compound> left = new ArrayList<Compound>();
	private ArrayList<Compound> right = new ArrayList<Compound>();
	
	public Equation(ArrayList<Compound> left, ArrayList<Compound> right){
		this.left = left;
		this.right = right;
	}
	
	public Equation(){
		
	}
	
	public boolean hasProducts(){
		if(right.size() == 0) return false;
		return true;
	}
	
	public boolean hasReactants(){
		if(left.size() == 0) return false;
		return true;
	}
	
	public ArrayList<Compound> getReactants(){
		return left;
	}
	
	public ArrayList<Compound> getProducts(){
		return right;
	}
	
	public ArrayList<Compound> getCompounds(int side){
		if(side <= 0) return left;
		return right;
	}
	
	public ArrayList<Compound> getAllCompounds(){
		ArrayList<Compound> all = new ArrayList<Compound>();
		
		for(Compound c : left) all.add(c);
		for(Compound c : right) all.add(c);
		
		return all;
	}
	
	public void addCompound(Compound c, int side){
		if(side <= 0) left.add(c);
		else right.add(c);
	}
	
	public void addCompounds(ArrayList<Compound> cmps, int side){
		if(side <= 0) left.addAll(cmps);
		else right.addAll(cmps);
	}
	
}
