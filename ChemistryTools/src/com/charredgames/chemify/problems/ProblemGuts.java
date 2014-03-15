package com.charredgames.chemify.problems;

import android.content.Context;
import android.view.View;

public interface ProblemGuts{
	public void openActivity(Context context);
	public void getInputView(Context context, View view);
	public boolean getSubmit(Context context, View view); //Return true if should use default implementation.
	public boolean fitsCategory(String cat);
}
