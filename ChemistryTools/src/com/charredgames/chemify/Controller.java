package com.charredgames.chemify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.SparseArray;

import com.charredgames.chemify.constant.Ion;
import com.charredgames.chemify.problems.Prefix;
import com.charredgames.chemify.problems.ResponseType;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */
public class Controller {

	public static final String _VERSION = "1.2.6";
	private static AssetManager assets = null;
	public static ArrayList<ResponseType> types = new ArrayList<ResponseType>();
	public static ArrayList<Prefix> prefixes = new ArrayList<Prefix>();
	public static SparseArray<String> romanNumerals = new SparseArray<String>();
	public static final Locale _LOCALE = Locale.getDefault();
	public static boolean autoFormat = true, calculateReasoning = true, sendUsage = false;
	public static Map<String, String> reactionSymbols = new HashMap<String, String>();
	
	public static void reset(AssetManager aManager){
		assets = aManager;
		setElements("/default/elements.cgf");
		setIons("/default/polyions.cgf");
		
		//Only needs to be run once by app.
		if(assets != null) return;
		if(ResponseType.answer instanceof ResponseType);
		if(Prefix.mono instanceof Prefix);
		
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
		
		reactionSymbols.put(" +", "+");
		reactionSymbols.put("+ ", "+");
		reactionSymbols.put(" + ", "+");
		reactionSymbols.put(" >", ">");
		reactionSymbols.put("> ", ">");
		reactionSymbols.put(" > ", ">");
		reactionSymbols.put(" ->", ">");
		reactionSymbols.put("-> ", ">");
		reactionSymbols.put(" -> ", ">");
		reactionSymbols.put(" =>", ">");
		reactionSymbols.put("=> ", ">");
		reactionSymbols.put(" => ", ">");
		reactionSymbols.put(" &#8652;", ">");
		reactionSymbols.put("&#8652; ", ">");
		reactionSymbols.put(" &#8652; ", ">");
	}

	public static String replaceReactionSymbols(String str){
		for(Entry<String, String> entry : reactionSymbols.entrySet()){
			String key = entry.getKey();
			if(str.contains(key)) str = str.replace(key, entry.getValue());
		}
		
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
	
	private static File getFileFromStream(String prefix, InputStream in){
		File file = null;
		try{
			file = File.createTempFile(prefix, ".cgf");
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
		}catch(Exception e){}
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
	
}
