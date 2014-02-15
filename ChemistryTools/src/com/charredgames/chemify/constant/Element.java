package com.charredgames.chemify.constant;

import java.util.ArrayList;

import com.charredgames.chemify.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */
public class Element {

	private String name, symbol, electronConfig = null;
	private int atomicNumber, period, group, protons, electrons, neutrons, charge, valence, activityNum;
	private double atomicMass;
	private MetalType metalType;
	public static ArrayList<Element> elements = new ArrayList<Element>();
	
	public static final Element HYDROGEN = new Element("HYDROGEN");
	public static final Element HELIUM = new Element("HELIUM");
	public static final Element LITHIUM = new Element("LITHIUM");
	public static final Element BERYLLIUM = new Element("BERYLLIUM");
	public static final Element BORON = new Element("BORON");
	public static final Element CARBON = new Element("CARBON");
	public static final Element NITROGEN = new Element("NITROGEN");
	public static final Element OXYGEN = new Element("OXYGEN");
	public static final Element FLUORINE = new Element("FLUORINE");
	public static final Element NEON = new Element("NEON");
	public static final Element SODIUM = new Element("SODIUM");
	public static final Element MAGNESIUM = new Element("MAGNESIUM");
	public static final Element ALUMINIUM = new Element("ALUMINIUM");
	public static final Element SILICON = new Element("SILICON");
	public static final Element PHOSPHORUS = new Element("PHOSPHORUS");
	public static final Element SULFUR = new Element("SULFUR");
	public static final Element CHLORINE = new Element("CHLORINE");
	public static final Element ARGON = new Element("ARGON");
	public static final Element POTASSIUM = new Element("POTASSIUM");
	public static final Element CALCIUM = new Element("CALCIUM");
	public static final Element SCANDIUM = new Element("SCANDIUM");
	public static final Element TITANIUM = new Element("TITANIUM");
	public static final Element VANADIUM = new Element("VANADIUM");
	public static final Element CHROMIUM = new Element("CHROMIUM");
	public static final Element MANGANESE = new Element("MANGANESE");
	public static final Element IRON = new Element("IRON");
	public static final Element COBALT = new Element("COBALT");
	public static final Element NICKEL = new Element("NICKEL");
	public static final Element COPPER = new Element("COPPER");
	public static final Element ZINC = new Element("ZINC");
	public static final Element GALLIUM = new Element("GALLIUM");
	public static final Element GERMANIUM = new Element("GERMANIUM");
	public static final Element ARSENIC = new Element("ARSENIC");
	public static final Element SELENIUM = new Element("SELENIUM");
	public static final Element BROMINE = new Element("BROMINE");
	public static final Element KRYPTON = new Element("KRYPTON");
	public static final Element RUBIDIUM = new Element("RUBIDIUM");
	public static final Element STRONTIUM = new Element("STRONTIUM");
	public static final Element YTTRIUM = new Element("YTTRIUM");
	public static final Element ZIRCONIUM = new Element("ZIRCONIUM");
	public static final Element NIOBIUM = new Element("NIOBIUM");
	public static final Element MOLYBDENUM = new Element("MOLYBDENUM");
	public static final Element TECHNETIUM = new Element("TECHNETIUM");
	public static final Element RUTHENIUM = new Element("RUTHENIUM");
	public static final Element RHODIUM = new Element("RHODIUM");
	public static final Element PALLADIUM = new Element("PALLADIUM");
	public static final Element SILVER = new Element("SILVER");
	public static final Element CADMIUM = new Element("CADMIUM");
	public static final Element INDIUM = new Element("INDIUM");
	public static final Element TIN = new Element("TIN");
	public static final Element ANTIMONY = new Element("ANTIMONY");
	public static final Element TELLURIUM = new Element("TELLURIUM");
	public static final Element IODINE = new Element("IODINE");
	public static final Element XENON = new Element("XENON");
	public static final Element CESIUM = new Element("CESIUM");
	public static final Element BARIUM = new Element("BARIUM");
	public static final Element LANTHANUM = new Element("LANTHANUM");
	public static final Element CERIUM = new Element("CERIUM");
	public static final Element PRASEODYMIUM = new Element("PRASEODYMIUM");
	public static final Element NEODYMIUM = new Element("NEODYMIUM");
	public static final Element PROMETHIUM = new Element("PROMETHIUM");
	public static final Element SAMARIUM = new Element("SAMARIUM");
	public static final Element EUROPIUM = new Element("EUROPIUM");
	public static final Element GADOLINIUM = new Element("GADOLINIUM");
	public static final Element TERBIUM = new Element("TERBIUM");
	public static final Element DYSPROSIUM = new Element("DYSPROSIUM");
	public static final Element HOLMIUM = new Element("HOLMIUM");
	public static final Element ERBIUM = new Element("ERBIUM");
	public static final Element THULIUM = new Element("THULIUM");
	public static final Element YTTERBIUM = new Element("YTTERBIUM");
	public static final Element LUTETIUM = new Element("LUTETIUM");
	public static final Element HAFNIUM = new Element("HAFNIUM");
	public static final Element TANTALUM = new Element("TANTALUM");
	public static final Element TUNGSTEN = new Element("TUNGSTEN");
	public static final Element RHENIUM = new Element("RHENIUM");
	public static final Element OSMIUM = new Element("OSMIUM");
	public static final Element IRIDIUM = new Element("IRIDIUM");
	public static final Element PLATINUM = new Element("PLATINUM");
	public static final Element GOLD = new Element("GOLD");
	public static final Element MERCURY = new Element("MERCURY");
	public static final Element THALLIUM = new Element("THALLIUM");
	public static final Element LEAD = new Element("LEAD");
	public static final Element BISMUTH = new Element("BISMUTH");
	public static final Element POLONIUM = new Element("POLONIUM");
	public static final Element ASTATINE = new Element("ASTATINE");
	public static final Element RADON = new Element("RADON");
	public static final Element FRANCIUM = new Element("FRANCIUM");
	public static final Element RADIUM = new Element("RADIUM");
	public static final Element ACTINIUM = new Element("ACTINIUM");
	public static final Element THORIUM = new Element("THORIUM");
	public static final Element PROTACTINIUM = new Element("PROTACTINIUM");
	public static final Element URANIUM = new Element("URANIUM");
	public static final Element NEPTUNIUM = new Element("NEPTUNIUM");
	public static final Element PLUTONIUM = new Element("PLUTONIUM");
	public static final Element AMERICIUM = new Element("AMERICIUM");
	public static final Element CURIUM = new Element("CURIUM");
	public static final Element BERKELIUM = new Element("BERKELIUM");
	public static final Element CALIFORNIUM = new Element("CALIFORNIUM");
	public static final Element EINSTEINIUM = new Element("EINSTEINIUM");
	public static final Element FERMIUM = new Element("FERMIUM");
	public static final Element MENDELEVIUM = new Element("MENDELEVIUM");
	public static final Element NOBELIUM = new Element("NOBELIUM");
	public static final Element LAWRENCIUM = new Element("LAWRENCIUM");
	public static final Element RUTHERFORDIUM = new Element("RUTHERFORDIUM");
	public static final Element DUBNIUM = new Element("DUBNIUM");
	public static final Element SEABORGIUM = new Element("SEABORGIUM");
	public static final Element BOHRIUM = new Element("BOHRIUM");
	public static final Element HASSIUM = new Element("HASSIUM");
	public static final Element MEITNERIUM = new Element("MEITNERIUM");
	public static final Element DARMSTADTIUM = new Element("DARMSTADTIUM");
	public static final Element ROENTGENIUM = new Element("ROENTGENIUM");
	public static final Element COPERNICIUM = new Element("COPERNICIUM");
	public static final Element UNUNTRIUM = new Element("UNUNTRIUM");
	public static final Element UNUNQUADIUM = new Element("UNUNQUADIUM");
	public static final Element UNUNPENTIUM = new Element("UNUNPENTIUM");
	public static final Element UNUNHEXIUM = new Element("UNUNHEXIUM");
	public static final Element UNUNSEPTIUM = new Element("UNUNSEPTIUM");
	public static final Element UNUNOCTIUM = new Element("UNUNOCTIUM");
	
	public Element(String name){
		this.name = name;
		elements.add(this);
	}
	
	public void setInfo(String symbol, int atomicNumber, double atomicMass, int period, int group, int charge, int valence, int activityNum){
		this.symbol = symbol;
		this.atomicNumber = atomicNumber;
		this.period = period;
		this.group = group;
		this.charge = charge;
		this.valence = valence;
		this.atomicMass = atomicMass;
		this.activityNum = activityNum;
		int mass = (int) Math.round(atomicMass);
		this.protons = atomicNumber;
		this.neutrons = (mass - protons);
		this.electrons = atomicNumber;
		if(symbol.equalsIgnoreCase("B") || symbol.equalsIgnoreCase("Si") || 
				symbol.equalsIgnoreCase("Ge") || symbol.equalsIgnoreCase("As") || 
				symbol.equalsIgnoreCase("Sb") || symbol.equalsIgnoreCase("Te")) metalType = MetalType.METALLOID;
		else if((period == 2 && group >= 14) || (period == 3 && group >= 15) || 
				(period == 4 && group >= 16) || (period == 5 && group >= 17) || 
				(period == 6 && group >= 17) || (period == 7 && group == 18)) metalType = MetalType.NONMETAL;
		else metalType = MetalType.METAL;
	}
	
	public static Element getElement(String name){
		for(Element element : elements){
			if(element.getSymbol() != null && element.getSymbol().equals(name)) return element;
			if(element.getName().equalsIgnoreCase(name)) return element;
			try{
				int num = Integer.parseInt(name);
				if(element.getAtomicNumber() == num) return element;
				if(element.getAtomicMass() == num) return element;
			}catch(Exception e){}
			
		}
		return Element.HYDROGEN;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public String getName(){
		return name;
	}
	
	public int getAtomicNumber(){
		return atomicNumber;
	}
	
	public double getAtomicMass(){
		return atomicMass;
	}
	
	public int getPeriod(){
		return period;
	}
	
	public int getGroup(){
		return group;
	}
	
	public int getCharge(){
		return charge;
	}
	
	public int getValence(){
		return valence;
	}
	
	public int getActivityNumber(){
		return activityNum;
	}
	
	public int getProtons(){
		return protons;
	}
	
	public int getNeutrons(){
		return neutrons;
	}
	
	public int getElectrons(){
		return electrons;
	}
	
	public MetalType getMetalType(){
		return metalType;
	}
	
	public String getElectronConfig(){
		if(electronConfig != null) return electronConfig;
		String config = "";
		
		int elec = electrons;
		
		config += getFilledPortion("1S", 2, 2, electrons);
		config += getFilledPortion("2S", 4, 2, electrons);
		config += getFilledPortion("2P", 10, 6, electrons);
		config += getFilledPortion("3S", 12, 2, electrons);
		config += getFilledPortion("3P", 18, 6, electrons);
		config += getFilledPortion("4S", 20, 2, electrons);
		config += getFilledPortion("3D", 30, 10, electrons);
		config += getFilledPortion("4P", 36, 6, electrons);
		config += getFilledPortion("5S", 38, 2, electrons);
		config += getFilledPortion("4D", 48, 10, electrons);
		config += getFilledPortion("5P", 54, 6, electrons);
		config += getFilledPortion("6S", 56, 2, electrons);
		config += getFilledPortion("4F", 70, 14, electrons);
		config += getFilledPortion("5D", 80, 10, electrons);
		config += getFilledPortion("6P", 86, 6, electrons);
		config += getFilledPortion("7S", 88, 2, electrons);
		config += getFilledPortion("5F", 102, 14, electrons);
		config += getFilledPortion("6D", 112, 10, electrons);
		config += getFilledPortion("7P", 118, 6, electrons);
		
		
		electrons = elec;
		electronConfig = config;
		return electronConfig;
	}
	
	private String getFilledPortion(String orbital, int filled, int size, int actual){
		if(actual <= 0) return "";
		orbital += "<sup>";
		if(actual >= size){
			orbital += size;
			electrons -= size;
		}
		if(actual < size){
			orbital += actual;
			electrons -= actual;
		}
		
		return orbital + "</sup>";
	}
	
	
	public String getDrawString(){
		String output = "";
		
		output += "<b>" + Controller.normalizeString(name, true) + "</b> (" + symbol + ")";
		
		return output;
	}
	
}
