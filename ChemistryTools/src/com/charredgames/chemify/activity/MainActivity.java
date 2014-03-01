package com.charredgames.chemify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.R;
import com.charredgames.chemify.SigFigCalculator;
import com.charredgames.chemify.constant.Ion;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.charredgames.chemify.MESSAGE";
	public static Spinner problem_type;
	private static final String STATE_INPUT = "state_input";
	private static final String STATE_PTYPE = "ptype_value";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Controller.context = this;
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		setContentView(R.layout.activity_main);

		problem_type = (Spinner) findViewById(R.id.problem_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.problem_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		problem_type.setAdapter(adapter);
		
		if(Ion.ions.size() == 0) Controller.reset(this.getAssets());
		
		AdView adView = (AdView) findViewById(R.id.ad);
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
		adView.loadAd(request);
		AdView.LayoutParams params = new AdView.LayoutParams(AdView.LayoutParams.WRAP_CONTENT, AdView.LayoutParams.WRAP_CONTENT);
		params.addRule(AdView.ALIGN_PARENT_BOTTOM);
		adView.setLayoutParams(params);
		
		SigFigCalculator sigFigs = new SigFigCalculator(15.00);
		System.out.println(sigFigs.sigFigs);
		sigFigs = new SigFigCalculator(234.3);
		System.out.println(sigFigs.sigFigs);
		sigFigs = new SigFigCalculator(234.300);
		System.out.println(sigFigs.sigFigs);
		sigFigs = new SigFigCalculator(20001.00);
		System.out.println(sigFigs.sigFigs);
		sigFigs = new SigFigCalculator(20001);
		System.out.println(sigFigs.sigFigs);
	}
	
	public void onStart(){
		super.onStart();
		if(problem_type == null) problem_type = (Spinner) findViewById(R.id.problem_type);
		problem_type.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	TextView txt = ((TextView)findViewById(R.id.edit_input));
		    	String pType = problem_type.getSelectedItem().toString();
		    	if(pType.equalsIgnoreCase("nomenclature")) txt.setHint(R.string.nomenclature_hint);
		    	else if(pType.equalsIgnoreCase("predict reactions")) txt.setHint(R.string.reactions_hint);
		    	else if(pType.equalsIgnoreCase("Weight")) txt.setHint(R.string.weight_hint);
		    	else if(pType.equalsIgnoreCase("Solubility")) txt.setHint(R.string.solubility_hint);
		    	else if(pType.equalsIgnoreCase("Oxidation")) txt.setHint(R.string.oxidation_hint);
		    	else if(pType.equalsIgnoreCase("Element Info")) txt.setHint(R.string.element_info_hint);
		    	else if(pType.equalsIgnoreCase("dimensional analysis")) txt.setHint(R.string.dimensional_analysis_hint);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }

		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void submit(View view){
		Intent intent = new Intent(this, Response.class);
		EditText input = (EditText) findViewById(R.id.edit_input);
		if(input.getText() == null || input.getText().toString() == null || input.getText().toString().equals("") || 
				input.getText().toString().equals(" ")) return;
		intent.putExtra(EXTRA_MESSAGE, input.getText().toString());
		startActivity(intent);
	}
	
	public void onPause(){
		super.onPause();
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		
		((TextView)findViewById(R.id.edit_input)).setText(savedInstanceState.getString(STATE_INPUT));
		problem_type.setSelection(savedInstanceState.getInt(STATE_PTYPE));
	}
	
	public void onSaveInstanceState(Bundle outState){
		outState.putString(STATE_INPUT, ((TextView)findViewById(R.id.edit_input)).getText().toString());
		outState.putInt(STATE_PTYPE, problem_type.getSelectedItemPosition());

		super.onSaveInstanceState(outState);
	}
	
	public void onResume(){
		super.onResume();
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
