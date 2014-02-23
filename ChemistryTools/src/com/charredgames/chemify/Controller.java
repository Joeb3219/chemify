package com.charredgames.chemify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.SparseArray;

import com.charredgames.chemify.activity.SendPost;
import com.charredgames.chemify.constant.Definition;
import com.charredgames.chemify.constant.Ion;
import com.charredgames.chemify.constant.Unit;
import com.charredgames.chemify.constant.UnitPrefix;
import com.charredgames.chemify.problems.Prefix;
import com.charredgames.chemify.problems.Problem;
import com.charredgames.chemify.problems.ResponseType;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */

public class Controller {

	public static final String _VERSION = "1.4.3";
	private static AssetManager assets = null;
	public static ArrayList<ResponseType> types = new ArrayList<ResponseType>();
	public static ArrayList<Prefix> prefixes = new ArrayList<Prefix>();
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	public static ArrayList<String> conversionSymbols = new ArrayList<String>(Arrays.asList("converted to", "equals"));
	public static SparseArray<String> romanNumerals = new SparseArray<String>();
	public static ArrayList<UnitPrefix> unitPrefixes = new ArrayList<UnitPrefix>();
	public static final Locale _LOCALE = Locale.getDefault();
	public static boolean autoFormat = true, calculateReasoning = true, sendUsage = false;
	public static Map<String, String> reactionSymbols = new HashMap<String, String>();
	public static final String _GENERIC_PLUS_SIGN = "+", _GENERIC_YIELDS_SIGN = ">";
	public static Context context;
	public static Resources resources;
	
	public static void reset(AssetManager aManager){
		//if(assets != null) return;
		assets = aManager;
		resources = context.getResources();
		setElements("/default/elements.cgf");
		Ion.ions = new ArrayList<Ion>();
		setIons("/default/polyions.cgf");
		Definition.definitions = new ArrayList<Definition>();
		setDefinitions("/default/definitions.cgf");
		
		//Only needs to be run once by app.
		//if(!firstLoad) return;
		if(ResponseType.answer instanceof ResponseType);
		if(UnitPrefix.ATTO instanceof UnitPrefix);
		if(Prefix.mono instanceof Prefix);
		if(Ion.ions.size() > 4);
		if(Controller.units.size() > 1);
		if(com.charredgames.chemify.constant.Element.elements.size() > 3);
		
		romanNumerals.put(1, "I");
		romanNumerals.put(2, "II");
		romanNumerals.put(3, "III");
		romanNumerals.put(4, "IV");
		romanNumerals.put(5, "V");
		romanNumerals.put(6, "VI");
		romanNumerals.put(7, "VII");
		romanNumerals.put(8, "VIII");
		romanNumerals.put(9, "IX");
		romanNumerals.put(10, "X");
		romanNumerals.put(11, "XI");
		romanNumerals.put(12, "XII");
		romanNumerals.put(13, "XIII");
		romanNumerals.put(14, "XIV");
		romanNumerals.put(15, "XV");
		
		reactionSymbols.put("+", _GENERIC_PLUS_SIGN);
		//reactionSymbols.put("with", "");
		//reactionSymbols.put("reacts", _GENERIC_PLUS_SIGN);
		reactionSymbols.put(">", _GENERIC_YIELDS_SIGN);
		reactionSymbols.put("->", _GENERIC_YIELDS_SIGN);
		reactionSymbols.put("=>", _GENERIC_YIELDS_SIGN);
		reactionSymbols.put("&#8652;", _GENERIC_YIELDS_SIGN);
		reactionSymbols.put("yields", _GENERIC_YIELDS_SIGN);

	}

	public static String replaceReactionSymbols(String str){
		for(Entry<String, String> entry : reactionSymbols.entrySet()){
			String key = entry.getKey();
			if(str.contains(key)) str = str.replace(key, entry.getValue());
			if(str.contains(" " + key)) str = str.replace(" " + key, entry.getValue());
			if(str.contains(key + " ")) str = str.replace(key + " ", entry.getValue());
			if(str.contains(" " + key + " ")) str = str.replace(" " + key + " ", entry.getValue());
		}
		
		return str;
	}
	
	public static String replaceConversionSymbols(String str){
		for(String s : conversionSymbols){
			if(str.contains(s)) str = str.replace(s, "to");
		}
		str = str.replace("of", "");
		return str;
	}
	
	public static String convertIntToNumeral(int num){
		if(romanNumerals.get(num) != null) return romanNumerals.get(num);
		return "I";
	}
	
	public static int convertNumeralToInt(String str){
		if(romanNumerals.indexOfValue(str) > 0){
			return romanNumerals.keyAt(romanNumerals.indexOfValue(str));
		}else{
			try{
				Integer.parseInt(str);
			}catch(Exception e){
				return 1;
			}
			return Integer.parseInt(str);
		}
	}
	
	private static void setElements(String path){
		if(assets == null) return;
		SAXBuilder builder = new SAXBuilder();
		File xmlFile;
		try {
			xmlFile = getFileFromStream("elements", assets.open("default/elements.cgf"));
			
		try {
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			//Handles loading world chests
			List<Element> list = rootNode.getChildren("element");
			for (int i = 0; i < list.size(); i++) {				
				Element ele = (Element) list.get(i);
				com.charredgames.chemify.constant.Element element = com.charredgames.chemify.constant.Element.getElement(ele.getAttributeValue("name"));
				element.setInfo(ele.getChild("string").getAttributeValue("symbol"), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("number")), 
						Double.parseDouble(ele.getChild("int").getAttributeValue("mass")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("period")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("group")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("charge")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("valence")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("activity")));
				
			}
		  } catch (IOException e) {e.printStackTrace();} catch (JDOMException e) {e.printStackTrace();  }} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void setIons(String path){
		SAXBuilder builder = new SAXBuilder();
		File xmlFile;
		try {
			xmlFile = getFileFromStream("polyions", assets.open("default/polyions.cgf"));
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			List<Element> list = rootNode.getChildren("ion");
			for (int i = 0; i < list.size(); i++) {				
				Element ionItem = (Element) list.get(i);
				new Ion(
						ionItem.getChild("elements").getAttributeValue("list"),
						ionItem.getChild("string").getAttributeValue("name"),
						Integer.parseInt(ionItem.getChild("int").getAttributeValue("charge"))
						);
			}
		} catch (IOException e) {e.printStackTrace();} catch (JDOMException e) {e.printStackTrace();  }
	}
	
	private static void setDefinitions(String path){
		SAXBuilder builder = new SAXBuilder();
		File xmlFile;
		try {
			xmlFile = getFileFromStream("definitions", assets.open("default/definitions.cgf"));
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			List<Element> list = rootNode.getChildren("entry");
			for (int i = 0; i < list.size(); i++) {				
				Element definition = (Element) list.get(i);
				Definition d = new Definition(
						definition.getChildText("word"),
						definition.getChildText("definition"));
				if(definition.getChildText("example") != null) d.setExample(definition.getChildText("example"));
			}
		} catch (IOException e) {e.printStackTrace();} catch (JDOMException e) {e.printStackTrace();  }
	}
	
	private static File getFileFromStream(String prefix, InputStream in){
		File file = null;
		try{
			file = File.createTempFile(prefix, ".cgf", context.getCacheDir());
			file.deleteOnExit();
			OutputStream os = new FileOutputStream(file);
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = in.read(buffer)) !=-1){
                os.write(buffer, 0, bytesRead);
            }
            in.close();
            os.flush();
            os.close();
		}catch(Exception e){e.printStackTrace();}
		if(file == null) file = new File("p");
		return file;
	}
	
	public static Ion getIon(String str){
		for(Ion ion : Ion.ions){
			if(ion.getElementString().equals(str)) return ion;
		}
		return Ion.ions.get(0);
	}

	public static String stripHtmlTags(String str){
		if(str.contains("<sub>")) str = str.replace("<sub>", "");
		if(str.contains("</sub>")) str = str.replace("</sub>", "");
		if(str.contains("<sup>")) str = str.replace("<sup>", "");
		if(str.contains("</sup>")) str = str.replace("</sup>", "");
		if(str.contains("<br>")) str = str.replace("<br>", "");
		return str;
	}
	
	public static void reloadSettings(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		autoFormat = prefs.getBoolean("pref_auto-format", true);
		calculateReasoning = prefs.getBoolean("pref_calculate-reasoning", true);
		sendUsage = prefs.getBoolean("pref_send-data", false);
	}
	
	public static int getGCD(int a, int b){
		if(a == 0 || b == 0) return a+b;
		return getGCD(b, a%b);
	}
	
	public static int getGCD(ArrayList<Integer> list){
		if(list.size() == 1) return 1;
		int result = list.get(0);
		
		for(int i = 1; i < list.size(); i ++) result = getGCD(result, list.get(i));
		
		return result;
	}

	public static String normalizeString(String string, boolean upper){
		String str = string.toLowerCase(_LOCALE);
		if(upper) return str.substring(0,1).toUpperCase(_LOCALE) + str.substring(1);
		return str;
	}
	
	public static void sendUsageReport(String operation, Problem problem){
		HttpClient httpclient = new DefaultHttpClient();
  		 HttpPost httppost = new HttpPost("http://www.charredgames.com/usagedata.php");
  		    
  		 try {
  			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
  			nameValuePairs.add(new BasicNameValuePair("version", Controller._VERSION));
  			nameValuePairs.add(new BasicNameValuePair("problem", operation));
  			nameValuePairs.add(new BasicNameValuePair("input", problem.getInput()));
  			String r = "";
  			if(problem.getResponse() != null) r = problem.getResponse().getResponse();
  			nameValuePairs.add(new BasicNameValuePair("output", r));
  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
  			
  			new SendPost(httpclient, httppost).execute();

  		} catch (IOException e) {}
	}
	
	public static String doubleToScientific(Double num){
		DecimalFormat f = new DecimalFormat("0.###E0");
		String formatted = "";
		if(num <= 9999 && num >= -9999 && (num >= 0.001 || num <= -0.001)){
			f = new DecimalFormat("0.000");
			formatted = f.format(num);
		}else{
			formatted = f.format(num);
			formatted = formatted.replace("E", " * 10<sup>");
			formatted += "</sup>";
		}
		return formatted;
	}
	
}
