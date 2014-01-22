package com.charredgames.chemistrytools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.charredgames.tool.chemtool.Controller;
import com.charredgames.tool.chemtool.problems.Problem;

public class BugReport extends DialogFragment{

	private String[] report = new String[2];
	
	public BugReport(){
		
	}
	
	public BugReport(Problem problem){
		report[0] = problem.getInput();
		if(problem.getResponse() != null){
			report[1] = problem.getResponse().getResponse();
		}
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.action_bug);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.bug_report, null);
        
        builder.setView(layout);

        
        builder.setPositiveButton(R.string.bugReport_positive, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
	                       
	               			
	               		 HttpClient httpclient = new DefaultHttpClient();
	               		 HttpPost httppost = new HttpPost("http://www.charredgames.com/bugreport.php");
	               		    
	               		 try {
	               			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	               			nameValuePairs.add(new BasicNameValuePair("version", Controller._VERSION));
	               			nameValuePairs.add(new BasicNameValuePair("input", report[0]));
	               			nameValuePairs.add(new BasicNameValuePair("output", report[1]));
	               			nameValuePairs.add(new BasicNameValuePair("email", ((EditText)layout.findViewById(R.id.bugReport_email)).getText().toString()));
	               			nameValuePairs.add(new BasicNameValuePair("notes", ((EditText) layout.findViewById(R.id.bugReport_notes)).getText().toString()));
	               			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	               			
	               			new SendPost(httpclient, httppost).execute();
	
	               		} catch (IOException e) {
	               	        // TODO Auto-generated catch block
	               	    }
                   }
               });
        builder.setNegativeButton(R.string.bugReport_negative, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dismiss();
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();

	}
	
}
