package com.charredgames.chemify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.charredgames.chemify.ExpandedListAdapter;
import com.charredgames.chemify.R;
import com.charredgames.chemify.constant.Ion;
import com.charredgames.chemify.constant.ProblemGuts;
import com.charredgames.chemify.util.Controller;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Mar 12, 2014
 */
public class TestActivity extends Activity{

	public static String problemType = null;
	
	public void onCreate(Bundle savedInstanceBundle){
		super.onCreate(savedInstanceBundle);
		
		Controller.context = this;
		
		if(Ion.ions.size() == 0) Controller.reset(this.getAssets());
		
		setContentView(R.layout.test_activity);
		ExpandableListView expandable = (ExpandableListView) findViewById(R.id.ExpList);
		
		final ExpandedListAdapter adapter = Controller.getMainGroupsAdapter(this);
		expandable.setAdapter(adapter);
		expandable.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				checkClick(adapter.groups.get(groupPosition).items.get(childPosition));
				return true;
			}
			
		});
		
	}
	
	private void checkClick(String child){
		problemType = child;
		ProblemGuts problemGuts = Controller.problemTypes.get(child);
		if(problemGuts == null) return;
		problemGuts.openActivity(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
        	startActivity(new Intent(this, SettingsActivity.class));
            return true;
		case R.id.action_definitions:
        	startActivity(new Intent(this, DefinitionsActivity.class));
            return true;
		case R.id.action_polyions:
        	startActivity(new Intent(this, PolyIonsActivity.class));
            return true;
		case R.id.action_constants:
        	startActivity(new Intent(this, ConstantsActivity.class));
            return true;
		case R.id.action_activitySeries:
        	startActivity(new Intent(this, ActivitySeriesActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}
	
}
