package com.charredgames.chemify.constant;

import java.util.ArrayList;

import android.text.Html;
import android.text.Spanned;

import com.charredgames.chemify.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */
public class Constant {

	public static ArrayList<Constant> constants = new ArrayList<Constant>();
	public String name, symbol, units;
	public double value;
	public Spanned drawString = null;
	
	public static Constant SPEED_OF_LIGHT = new Constant("Speed of Light", "C", 2.998 * Math.pow(10, 8), "m/s");
	public static Constant PLANCK_CONSTANT = new Constant("Planck Constant", "h", 6.626 * Math.pow(10, -34), "J*s");
	public static Constant BOLTZMANN_CONSTANT = new Constant("Boltzmann Constant", "k", 1.381 * Math.pow(10, -23), "J*K");
	public static Constant AVOGADROS_NUMBER = new Constant("Avogadro's Number", "N<sub>A</sub>", 6.022 * Math.pow(10, 23), "");
	public static Constant ELECTRON_MASS = new Constant("Electron Mass", "m<sub>e</sub>", 9.109 * Math.pow(10, -31), "kg");
	public static Constant PROTON_MASS = new Constant("Proton Mass", "m<sub>p</sub>", 1.673 * Math.pow(10, -27), "kg");
	public static Constant NEUTRON_MASS = new Constant("Neutron Mass", "m<sub>n</sub>", 1.675 * Math.pow(10, -27), "kg");
	public static Constant BOHR_RADIUS = new Constant("Bohr Radius", "a<sub>0</sub>", 5.292 * Math.pow(10, -11), "J/molK");
	public static Constant GAS_CONSTANT = new Constant("Gas Constant", "R", 8.314, "kg");
	public static Constant GRAVITY_ACCELERATION = new Constant("Gravity Acceleration", "g", 8.9, "m/s<sup>2</sup>");
	public static Constant ELEMENTARY_CHARGE = new Constant("Elementary Charge", "e", 1.6 * Math.pow(10, -19), "Coulombs");
	public static Constant FARADY_CONSTANT = new Constant("Farady Constant", "F", 9.65 * Math.pow(10, 4), "Coulombs/mol");
	public static Constant GRAVITY_CONSTANT = new Constant("Gravity Constant", "G", 6.673 * Math.pow(10, -11), "m<sup>3</sup>*kg*s<sup>2</sup>");
	public static Constant RYDBERG_CONSTANT = new Constant("Rydberg Constant", "R<sub>&#8734;</sub>", 1.0973 * Math.pow(10, 7), "1/m");
	
	public Constant(String name, String symbol, double value, String units){
		this.name = name;
		this.symbol = symbol;
		this.value = value;
		this.units = units;
		constants.add(this);
	}
	
	public static Constant getConstantByString(String str){
		String stripped = "";
		for(Constant c : constants){
			stripped = c.symbol.replace("<sub>", "").replace("</sub>", "");
			if(c.symbol.equals(str) || stripped.equals(str)) return c;
			if(c.name.equals(str)) return c;
		}
		for(Constant c : constants){
			stripped = c.symbol.replace("<sub>", "").replace("</sub>", "");
			if(c.symbol.equalsIgnoreCase(str) || stripped.equalsIgnoreCase(str)) return c;
			if(c.name.equalsIgnoreCase(str)) return c;
		}
		return constants.get(0);
	}

	public Spanned getDrawString(){
		if(drawString != null) return drawString;
		
		String str = "<b>" + name + "</b> (" + symbol + ")<br>";
		str += "<u>Value</u>: " + Controller.doubleToScientific(value) + " " + units;
		
		drawString = Html.fromHtml(str);
		return drawString;
	}
	
	public boolean matchesSearch(String str){
		str = str.toLowerCase();
		if(str == null || str.equals(" ") || str.equals("")) return true;
		if(symbol.toLowerCase().contains(str) || name.toLowerCase().contains(str) || symbol.toLowerCase().replace("<sub>", "").replace("</sub>", "").contains(str)) return true;
		return false;
	}
	
}
