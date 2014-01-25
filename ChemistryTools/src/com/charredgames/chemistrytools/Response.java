package com.charredgames.chemistrytools;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.charredgames.tool.chemtool.gui.ResponsePanel;
import com.charredgames.tool.chemtool.problems.ElementInfo;
import com.charredgames.tool.chemtool.problems.Nomenclature;
import com.charredgames.tool.chemtool.problems.Problem;
import com.charredgames.tool.chemtool.problems.Reaction;
import com.charredgames.tool.chemtool.problems.ResponseType;
import com.charredgames.tool.chemtool.problems.Weight;

public class Response extends Activity {

	private Problem problem;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(MainActivity.problem_type.getSelectedItem().toString());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }else getActionBar().setDisplayHomeAsUpEnabled(true);

		
		Intent intent = getIntent();
		String input = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		//Spanned answer;
		
		String selectedOperation = MainActivity.problem_type.getSelectedItem().toString();
		
		//Set the problem
		if(selectedOperation.equalsIgnoreCase("element info")) problem = new ElementInfo(input);
		else if(selectedOperation.equalsIgnoreCase("nomenclature")) problem = new Nomenclature(input);
		else if(selectedOperation.equalsIgnoreCase("weight")) problem = new Weight(input);
		else if(selectedOperation.equalsIgnoreCase("Predict Reactions")) problem = new Reaction(input);
		else problem = new Nomenclature(input);
		
		problem.solve(true);
		ArrayList<String> responses = new ArrayList<String>();
		if(problem != null && problem.getResponse() != null){
			ResponsePanel response = problem.getResponse();
			responses = response.getResponses();
			
			//String responseHTML = response.getResponse();
			//answer = android.text.Html.fromHtml(responseHTML);
		}//else answer = new SpannedString("Problem or response returned null.");
		
		/*TextView txtView = new TextView(this);
		txtView.setTextSize(15);
		txtView.setText(answer);
		
		ScrollView scroll = new ScrollView(this);
		scroll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		RelativeLayout layout = new RelativeLayout(this);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)); 
		layout.addView(txtView);
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) txtView.getLayoutParams();
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		txtView.setLayoutParams(layoutParams);
		
		scroll.addView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		setContentView(scroll);*/
		setContentView(R.layout.problem_nomenclature);
		
		//Get each string that the problem returned.
		for(String str : responses){
			if(!str.contains("{")) continue;
			String[] groups = str.split("[{}]");
			String responseTypeString = groups[1];
			Spanned responseString = Html.fromHtml(groups[2]);
			ResponseType responseType = ResponseType.getTypeByString(responseTypeString);
			switch(responseType){
				case input:
					((TextView) findViewById(R.id.problem_input)).setText(responseString);
					break;
				case answer:
					((TextView) findViewById(R.id.problem_answer)).setText(responseString);
					break;
				/*case nomenclature:
					((TextView) findViewById(R.id.problem_nomenclature)).setText(responseString);
					break;*/
				case weight:
					((TextView) findViewById(R.id.problem_weight)).setText(responseString);
					break;
				case oxidation:
					((TextView) findViewById(R.id.problem_oxidation)).setText(responseString);
					break;
				default:
					
					break;
			}
		}
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.response, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.action_bug:
	            sendBugReport();
	            return true;
	        case R.id.action_settings:
	        	//Do something
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	private void sendBugReport(){
		BugReport report = new BugReport(problem);
		report.show(getFragmentManager(), "Send Bug Report");
	}

	public void responseClicked(View view){
		//TextView v = (TextView) view;
		//v.setText(view.getId());
	}
	
}
