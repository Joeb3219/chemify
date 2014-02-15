package com.charredgames.chemify.activity;

import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Element;

public class ActivitySeriesActivity extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.resources_activity);
		
		TreeMap<Integer, Element> elements = new TreeMap<Integer, Element>();
		for(Element element : Element.elements){
			if(element.getActivityNumber() < 2) continue;
			elements.put(element.getActivityNumber(), element);
		}
		
		boolean firstBox = true;
		LinearLayout container = (LinearLayout)this.findViewById(R.id.resources_linear_parent);

		for(Entry<Integer, Element> entry : elements.descendingMap().entrySet()){
			Element element = entry.getValue();
			TextView eBox;
			if(firstBox) eBox = (TextView)getLayoutInflater().inflate(R.layout.firstboxtemplate, null);
			else eBox = (TextView)getLayoutInflater().inflate(R.layout.secondboxtemplate, null);
				
			eBox.setText(Html.fromHtml(element.getDrawString()));
			
			container.addView(eBox);
			
			firstBox = !firstBox;
		}
	
	}
	
	public void responseClicked(View view){
	}
}
