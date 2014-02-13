package com.charredgames.chemify.constant;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 18, 2013
 */
public class ElementSet{

	private Element element;
	private int quantity;
	private int oxidationNumber = -6;
	
	public ElementSet(Element element, int quantity){
		this.element = element;
		this.quantity = quantity;
	}
	
	public Element getElement(){
		return element;
	}
	
	public int getQuantity(){
		return Math.abs(quantity);
	}
	
	public double getWeight(){
		return element.getAtomicMass() * quantity;
	}
	
	public int getTotalCharge(){
		return element.getCharge() * quantity;
	}
	
	public String getDrawString(){
		String str = element.getSymbol();
		if(quantity > 1) str += "<sub>" + quantity + "</sub>";
		return str;
	}
	
	public void setQuantity(int num){
		if(num == 0) num = 1;
		this.quantity = Math.abs(num);
	}

	public int getOxidationNumber(){
		return oxidationNumber;
	}
	
	public void setOxidationNumber(int num){
		this.oxidationNumber = num;
	}

}
