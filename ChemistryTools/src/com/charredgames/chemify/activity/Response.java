package com.charredgames.chemify.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import com.charredgames.chemify.R;
import com.charredgames.chemify.gui.ResponseBlock;
import com.charredgames.chemify.gui.ResponsePanel;
import com.charredgames.chemify.problems.ElementInfo;
import com.charredgames.chemify.problems.Nomenclature;
import com.charredgames.chemify.problems.Problem;
import com.charredgames.chemify.problems.Reaction;
import com.charredgames.chemify.problems.ResponseType;
import com.charredgames.chemify.problems.Weight;

public class Response extends Activity {

	private Problem problem;
	private ArrayList<Integer> expandedViews = new ArrayList<Integer>();
	private Map<Integer, ResponseBlock> answers = new HashMap<Integer, ResponseBlock>();
	
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
			
		}
		
		setContentView(R.layout.problem_nomenclature);
		
		//Get each string that the problem returned.
		for(String str : responses){
			if(!str.contains("{")) continue;
			ResponseBlock block;
			String[] groups = str.split("[{}]");
			String responseTypeString = groups[1];
			Spanned responseString = Html.fromHtml(groups[2]);
			if(groups.length == 5){
				block = new ResponseBlock(responseString, Html.fromHtml(groups[4]));
			}else block = new ResponseBlock(responseString);
			ResponseType responseType = ResponseType.getTypeByString(responseTypeString);
			switch(responseType){
				case input:
					((TextView) findViewById(R.id.problem_input)).setText(responseString);
					break;
				case answer:
					((TextView) findViewById(R.id.problem_answer)).setText(responseString);
					answers.put(R.id.problem_answer, block);
					break;
				/*case nomenclature:
					((TextView) findViewById(R.id.problem_nomenclature)).setText(responseString);
					break;*/
				case weight:
					((TextView) findViewById(R.id.problem_weight)).setText(responseString);
					answers.put(R.id.problem_weight, block);
					break;
				case oxidation:
					((TextView) findViewById(R.id.problem_oxidation)).setText(responseString);
					answers.put(R.id.problem_oxidation, block);
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
		int id = view.getId();
		if(!answers.containsKey(id)) return;
		ResponseBlock block = answers.get(id);
		if(expandedViews.contains(id)){
			expandedViews.remove(expandedViews.indexOf(id));
			((TextView) (view)).setText(block.getAnswer());
		}
		else{
			expandedViews.add(id);
			((TextView) (view)).setText(block.getExpanded());
		}
		
	}
	
}