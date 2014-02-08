package com.charredgames.chemify.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 5, 2014
 */
public class Equation {

	private ArrayList<Compound> left = new ArrayList<Compound>();
	private ArrayList<Compound> right = new ArrayList<Compound>();
	private int balanceAttempts = 0;
	
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
	
	public String getDrawString(){
		String answer = "";
		
		for(Compound c : left){
			answer += c.getDrawString();
			if(left.get(left.size() - 1) != c) answer += " + ";
			else if(hasProducts()) answer += " &#8652; ";
		}

		for(Compound c : right){
			answer += c.getDrawString();
			if(right.get(right.size() - 1) != c) answer += " + ";
		}
		
		return answer;
	}
	
	public double getMass(){
		double mass = 0.00;
		for(Compound c : left) mass += c.getMass();
		return mass;
	}

	public void balance(){
		if(!hasProducts() || isBalanced()) return;
		Map<Element, Integer> l = getElementQuantityMap(left);
		Map<Element, Integer> r = getElementQuantityMap(right);
		int iterations = 0;
		
		balanceLoop:
		while(!isBalanced() || iterations <= 35){
			iterations ++;
			for(Entry<Element, Integer> entry : l.entrySet()){
				Element element = entry.getKey();
				int quantity = entry.getValue();
				if(!r.containsKey(element)) return;
				if(r.get(element) == quantity) continue;
				
				if(r.get(element) > quantity){
					for(Compound c : left){
						if(c.containsElement(element)){
							c.setMoles(c.getMoles() * (int)((r.get(element) / quantity) + 0.5));
						}
					}
				}
				else{
					for(Compound c : right){
						if(c.containsElement(element)) c.setMoles(c.getMoles() * (int)((quantity / r.get(element)) + 0.5));
					}
				}
				
				l = getElementQuantityMap(left);
				r = getElementQuantityMap(right);
				break balanceLoop;	
			}
		}
		
		balanceAttempts++;
		if(balanceAttempts < 5) balance();
		else{
			for(Compound c : getAllCompounds()) c.setMoles(1);
		}
		
	}
	
	public boolean isBalanced(){
		Map<Element, Integer> l = getElementQuantityMap(left);
		Map<Element, Integer> r = getElementQuantityMap(right);
		
		for(Entry<Element, Integer> entry : l.entrySet()){
			if(entry.getValue() != r.get(entry.getKey())) return false;
		}
		return true;
	}
	
	private Map<Element, Integer> getElementQuantityMap(ArrayList<Compound> compounds){
		Map<Element, Integer> map = new HashMap<Element, Integer>();
		for(Compound compound : compounds){
			for(ElementGroup group : compound.getElementGroups()){
				for(ElementSet set : group.getElementSets()){
					Element element = set.getElement();
					int quantity = set.getQuantity() * group.getQuantity() * (int) (compound.getMoles());
					if(map.containsKey(element)) map.put(element, map.get(element) + quantity);
					else map.put(element, quantity);
				}
			}
		}
		return map;
	}
	
}
