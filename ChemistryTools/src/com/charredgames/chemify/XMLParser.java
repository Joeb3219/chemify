package com.charredgames.chemify;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class XMLParser {

	public static final String _NAMESPACE = null;
	
	public List parse(File file, String type){
		InputStream stream = null;
		try{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			stream =  new FileInputStream(file);
			parser.setInput(stream, null);
			return readFeed(parser, type);
		}catch(Exception e){}finally{
			if(stream != null){
				try{stream.close();}catch(Exception e){}
			}
			file.delete();
		}
		return new ArrayList();
	}
	
	private List readFeed(XmlPullParser parser, String type){
		List entries = new ArrayList();
		try{
			
			parser.require(XmlPullParser.START_TAG, _NAMESPACE, type);
			while(parser.next() != XmlPullParser.END_TAG){
				if(parser.getEventType() != XmlPullParser.START_TAG) continue;
				if(type.equals("elements")){
					//if(parser.getName().equals(""))
				}
			}
			
		}catch(Exception e){}
		
		return entries;
	}
	
}
