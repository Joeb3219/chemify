package com.charredgames.tool.chemtool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import android.content.res.AssetManager;

import com.charredgames.tool.chemtool.constant.Ion;
import com.charredgames.tool.chemtool.problems.Prefix;
import com.charredgames.tool.chemtool.problems.ResponseType;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */
public class Controller {

	public static final String _VERSION = "1.0";
	private static AssetManager assets;
	public static ArrayList<ResponseType> types = new ArrayList<ResponseType>();
	public static ArrayList<Prefix> prefixes = new ArrayList<Prefix>();
	public static Map<Integer, String> romanNumerals = new HashMap<Integer, String>();

	
	public static void reset(AssetManager aManager){
		assets = aManager;
		setElements("/default/elements.cgf");
		setIons("/default/polyions.cgf");
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
	}
	
	public static String convertIntToNumeral(int num){
		if(romanNumerals.containsKey(num)) return romanNumerals.get(num);
		return "I";
	}
	
	public static int convertNumeralToInt(String str){
		for(Entry<Integer, String> entry : romanNumerals.entrySet()){
			if(entry.getValue().equalsIgnoreCase(str)) return entry.getKey();
		}
		
		try{
			Integer.parseInt(str);
		}catch(Exception e){
			return 1;
		}
		
		return Integer.parseInt(str);
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
				com.charredgames.tool.chemtool.constant.Element element = com.charredgames.tool.chemtool.constant.Element.getElement(ele.getAttributeValue("name"));
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
	
}
