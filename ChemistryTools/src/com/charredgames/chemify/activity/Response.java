package com.charredgames.chemify.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.R;
import com.charredgames.chemify.gui.ResponseBlock;
import com.charredgames.chemify.gui.ResponsePanel;
import com.charredgames.chemify.problems.ElementInfo;
import com.charredgames.chemify.problems.Ionic;
import com.charredgames.chemify.problems.Nomenclature;
import com.charredgames.chemify.problems.Oxidation;
import com.charredgames.chemify.problems.Problem;
import com.charredgames.chemify.problems.Reaction;
import com.charredgames.chemify.problems.ResponseType;
import com.charredgames.chemify.problems.Solubility;
import com.charredgames.chemify.problems.Weight;

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
			if(!getIntent().hasExtra(MainActivity.EXTRA_MESSAGE)) startActivity(new Intent(this, MainActivity.class));
		}
		
		if(selectedOperation == null) selectedOperation = MainActivity.problem_type.getSelectedItem().toString();
		setTitle(selectedOperation);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }//else getActionBar().setDisplayHomeAsUpEnabled(true);

		
		Intent intent = getIntent();
		if(input == null) input = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		//Spanned answer;
		
		//Set the problem
		if(selectedOperation.equalsIgnoreCase("element info")) problem = new ElementInfo(input);
		else if(selectedOperation.equalsIgnoreCase("nomenclature")) problem = new Nomenclature(input);
		else if(selectedOperation.equalsIgnoreCase("weight")) problem = new Weight(input);
		else if(selectedOperation.equalsIgnoreCase("oxidation")) problem = new Oxidation(input);
		else if(selectedOperation.equalsIgnoreCase("Predict Reactions")) problem = new Reaction(input);
		else if(selectedOperation.equalsIgnoreCase("Solubility")) problem = new Solubility(input);
		else if(selectedOperation.equalsIgnoreCase("Complete/Net Ionic")) problem = new Ionic(input);
		else problem = new Nomenclature(input);
		
		problem.solve(true);
		ArrayList<String> responses = new ArrayList<String>();
		if(problem != null && problem.getResponse() != null){
			ResponsePanel response = problem.getResponse();
			responses = response.getResponses();
			
		}
		
		if(selectedOperation.equalsIgnoreCase("element info")) setContentView(R.layout.problem_elementinfo);
		else if(selectedOperation.equalsIgnoreCase("nomenclature")) setContentView(R.layout.problem_nomenclature);
		else if(selectedOperation.equalsIgnoreCase("weight")) setContentView(R.layout.problem_weight);
		else if(selectedOperation.equalsIgnoreCase("oxidation")) setContentView(R.layout.problem_oxidation);
		else if(selectedOperation.equalsIgnoreCase("solubility")) setContentView(R.layout.problem_solubility);
		else if(selectedOperation.equalsIgnoreCase("predict reactions")) setContentView(R.layout.problem_reactions);
		else if(selectedOperation.equalsIgnoreCase("Complete/Net Ionic")) setContentView(R.layout.problem_reactions);
		else setContentView(R.layout.problem_nomenclature);
		
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
