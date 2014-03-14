package com.charredgames.chemify.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.charredgames.chemify.R;
import com.charredgames.chemify.util.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Mar 12, 2014
 */
public class TestActivity extends Activity{

	public void onCreate(Bundle savedInstanceBundle){
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.test_activity);
		ExpandableListView expandable = (ExpandableListView) findViewById(R.id.ExpList);
		
		expandable.setAdapter(Controller.getMainGroupsAdapter(this));
		expandable.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				System.out.println(parent.getAdapter().getItem(childPosition));
				return true;
			}
			
		});
		
	}
	
}
