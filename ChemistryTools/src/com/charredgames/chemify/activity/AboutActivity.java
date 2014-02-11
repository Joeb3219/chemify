package com.charredgames.chemify.activity;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.charredgames.chemify.R;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Feb 5, 2014
 */
public class AboutActivity extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		int versionCode = 0;
		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {e.printStackTrace();}
		((TextView)findViewById(R.id.about_app_version)).setText("Version " + versionName + " (" + versionCode + ")");
	}
	
	public void responseClicked(View view){
		//Silence is golden.
	}
	
}
