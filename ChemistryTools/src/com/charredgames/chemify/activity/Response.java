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
import com.charredgames.chemify.problems.DimensionalAnalysis;
import com.charredgames.chemify.problems.ElementInfo;
import com.charredgames.chemify.problems.Ionic;
import com.charredgames.chemify.problems.Nomenclature;
import com.charredgames.chemify.problems.Oxidation;
import com.charredgames.chemify.problems.Problem;
import com.charredgames.chemify.problems.Reaction;
import com.charredgames.chemify.problems.ResponseType;
import com.charredgames.chemify.problems.Solubility;
import com.charredgames.chemify.problems.Weight;
import com.charredgames.chemify.util.Controller;

public class Response extends Activity {

	private Problem problem;
	private ArrayList<Integer> expandedViews = new ArrayList<Integer>();
	private Map<Integer, ResponseBlock> answers = new HashMap<Integer, ResponseBlock>();
	private String input, selectedOperation = null;
	private static final String STATE_INPUT = "state_input";
	private static final String STATE_OPERATION = "state_operation";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null){
			input = savedInstanceState.getString(STATE_INPUT);
			selectedOperation = savedInstanceState.getString(STATE_OPERATION);
			Controller.context = this;
			Controller.reset(this.getAssets());
		}else{
			if(!getIntent().hasExtra("problem_type")) startActivity(new Intent(this, OldMainActivity.class));
		}
		
		if(selectedOperation == null) selectedOperation = getIntent().getStringExtra("problem_type");
		setTitle(selectedOperation);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

		
		Intent intent = getIntent();
		if(input == null) input = intent.getStringExtra("input");
		//Spanned answer;
		
		//Set the problem
		if(selectedOperation.equalsIgnoreCase(Controller.resources.getString(R.string.problem_element_info))) {
			problem = new ElementInfo(input);
			setContentView(R.layout.problem_elementinfo);
		}
		else if(selectedOperation.equalsIgnoreCase(Controller.resources.getString(R.string.problem_nomenclature))){
			problem = new Nomenclature(input);
			setContentView(R.layout.problem_nomenclature);
		}
		else if(selectedOperation.equalsIgnoreCase(Controller.resources.getString(R.string.problem_molar_mass))){
			problem = new Weight(input);
			setContentView(R.layout.problem_weight);
		}
		else if(selectedOperation.equalsIgnoreCase(Controller.resources.getString(R.string.problem_oxdidation))){
			problem = new Oxidation(input);
			setContentView(R.layout.problem_oxidation);
		}
		else if(selectedOperation.equalsIgnoreCase(Controller.resources.getString(R.string.problem_reactions))){
			problem = new Reaction(input);
			setContentView(R.layout.problem_reactions);
		}
		else if(selectedOperation.equalsIgnoreCase(Controller.resources.getString(R.string.problem_solubility))){
			problem = new Solubility(input);
			setContentView(R.layout.problem_solubility);
		}
		else{
			problem = new Nomenclature(input);
			setContentView(R.layout.problem_nomenclature);
		}
		
		problem.solve(true);
		ArrayList<String> responses = new ArrayList<String>();
		if(problem != null && problem.getResponse() != null){
			ResponsePanel response = problem.getResponse();
			responses = response.getResponses();
			
		}
		
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
				case nomenclature:
					((TextView) findViewById(R.id.problem_nomenclature)).setText(responseString);
					answers.put(R.id.problem_nomenclature, block);
					break;
				case weight:
					((TextView) findViewById(R.id.problem_weight)).setText(responseString);
					answers.put(R.id.problem_weight, block);
					break;
				case solubility:
					((TextView) findViewById(R.id.problem_solubility)).setText(responseString);
					answers.put(R.id.problem_solubility, block);
					break;
				case ionic:
					((TextView) findViewById(R.id.problem_ionic)).setText(responseString);
					answers.put(R.id.problem_ionic, block);
					break;
				case oxidation:
					((TextView) findViewById(R.id.problem_oxidation)).setText(responseString);
					answers.put(R.id.problem_oxidation, block);
					break;
				default:
					
					break;
			}
		}
		
		if(Controller.sendUsage) Controller.sendUsageReport(selectedOperation, problem);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.response, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.action_bug:
	            sendBugReport();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	public void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putString(STATE_INPUT, input);
		savedInstanceState.putString(STATE_OPERATION, selectedOperation);

		super.onSaveInstanceState(savedInstanceState);
	}
	
	private void sendBugReport(){
		BugReport report = new BugReport(problem);
		report.show(getFragmentManager(), "Send Bug Report");
	}

	public void responseClicked(View view){
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
