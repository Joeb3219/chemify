package com.charredgames.chemify.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;

import com.charredgames.chemify.Controller;
import com.charredgames.chemify.R;

public class SettingsActivity extends PreferenceActivity{

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		//menu.add(Menu.NONE, 0, 0, "Show current settings");
		return super.onCreateOptionsMenu(menu);
	}

	public void onStop(){
		super.onStop();
		Controller.reloadSettings(this);
	}
	
}
