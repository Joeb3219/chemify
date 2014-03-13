package com.charredgames.chemify.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.charredgames.chemify.ExpandedListAdapter;
import com.charredgames.chemify.ExpandedListGroup;
import com.charredgames.chemify.R;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Mar 12, 2014
 */
public class TestActivity extends Activity{

	public void onCreate(Bundle savedInstanceBundle){
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.test_activity);
		ExpandableListView expandable = (ExpandableListView) findViewById(R.id.ExpList);
		
		ArrayList<ExpandedListGroup> groups = new ArrayList<ExpandedListGroup>();
		ArrayList<String> items = new ArrayList<String>();
		
		ExpandedListGroup g = new ExpandedListGroup("Basic Chemistry Tools", items);
		items.add("Nomenclature");
		items.add("Molar Mass");
		items.add("Predict Reactions");
		items.add("Element Info");
		items.add("Predict Solubility");
		items.add("Oxidation Numbers");
		items.add("Dimensional Analysis");
		items.add("Formula Balancer");
		groups.add(g);
		items = new ArrayList<String>();
		g = new ExpandedListGroup("Converters", items);
		items.add("Stoichiometry");
		items.add("Unit Converter");
		items.add("pH/pOH Calculator");
		items.add("Half Life Calculator");
		items.add("Wavelength/Amplitude/Energy Calculator");
		groups.add(g);
		items = new ArrayList<String>();
		g = new ExpandedListGroup("Gas Laws Tools", items);
		groups.add(g);
		items.add("Boyle's Law");
		items.add("Charles's Law");
		items.add("Gay-Lussac Law");
		items.add("Ideal Gas Law");
		items.add("Combined Gas Law");
		items = new ArrayList<String>();
		g = new ExpandedListGroup("Thermodynamics Tools", items);
		groups.add(g);
		items.add("Entropy/Etholpy/Enthropy Calculator");
		items.add("Calorimetry Calculator");
		items.add("Gibbs Free Energy Calculator");
		items.add("Specific Heat Capacity");
		items = new ArrayList<String>();
		g = new ExpandedListGroup("Kinetics Tools", items);
		groups.add(g);
		items.add("Rate/K Calculator");
		items.add("Order of Reaction");
		items.add("Activation Energy Calculator");
		items = new ArrayList<String>();
		g = new ExpandedListGroup("Equilibrium Tools", items);
		groups.add(g);
		items.add("K Calculator");
		items.add("Buffer Capacity");
		items = new ArrayList<String>();
		g = new ExpandedListGroup("Electrochemistry Tools", items);
		groups.add(g);
		items.add("Half-Cell");
		items.add("Cell Notation");
		items.add("Cell Potential");
		items = new ArrayList<String>();
		g = new ExpandedListGroup("Nuclear Chemistry Tools", items);
		groups.add(g);
		items.add("Nuclear missiles!");
		items = new ArrayList<String>();
		g = new ExpandedListGroup("Organic Chemistry Tools", items);
		groups.add(g);
		items.add("Stuff");
		items = new ArrayList<String>();
		g = new ExpandedListGroup("Reference", items);
		groups.add(g);
		items.add("Quizzes");
		items.add("Dictionary");
		items.add("Periodic Table");
		items.add("Activity Series");
		items.add("Standard Reduction Series");
		items.add("Solubility Rules");
		items.add("Constants");
		items.add("Polyatomic Ions");
		items.add("Formulas");
		items.add("Units");
		
		ExpandedListAdapter adapter = new ExpandedListAdapter(this, groups);
		expandable.setAdapter(adapter);
		expandable.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				System.out.println(parent.getAdapter().getItem(childPosition));
				return true;
			}
			
		});
		
	}
	
}
