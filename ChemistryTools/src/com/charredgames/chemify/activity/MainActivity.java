package com.charredgames.chemify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
public class MainActivity extends Activity{

	public static String problemType = null;
	
	public void onCreate(Bundle savedInstanceBundle){
		super.onCreate(savedInstanceBundle);
		
		Controller.resetIfNeeded(this);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
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
		
		/*AdView adView = (AdView) findViewById(R.id.ad);
		adView.setAdListener(new AdListener() {
			
			@Override
			public void onReceiveAd(Ad arg0) {
			}
			
			@Override
			public void onPresentScreen(Ad arg0) {
			}
			
			@Override
			public void onLeaveApplication(Ad arg0) {
				
			}
			
			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
			}
			
			@Override
			public void onDismissScreen(Ad arg0) {
			}
		});
		AdRequest request = new AdRequest();
		request.addKeyword("education");
		//adView.loadAd(request);
		AdView.LayoutParams params = new AdView.LayoutParams(AdView.LayoutParams.WRAP_CONTENT, AdView.LayoutParams.WRAP_CONTENT);
		params.addRule(AdView.ALIGN_PARENT_BOTTOM);
		adView.setLayoutParams(params);*/
		
	}
	
	private void checkClick(String child){
		problemType = child;
		ProblemGuts problemGuts = Controller.problemTypes.get(child);
		if(problemGuts == null) return;
		problemGuts.openActivity(this);
	}

	public void onResume(){
		super.onResume();
	}

	public void onPause(){
		super.onPause();
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		String s = savedInstanceState.getString("problemType");
		if(s != null) problemType = s;
	}
	
	public void onSaveInstanceState(Bundle outState){
		if(problemType != null) outState.putString("problemType", problemType);

		super.onSaveInstanceState(outState);
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
