package com.charredgames.chemify.constant;

import android.content.Context;
import android.view.View;

public interface ProblemGuts{
	public void openActivity(Context context);
	public View getInputView(Context context);
	public boolean getSubmit(Context context, View view); //Return true if should use default implementation.
	public boolean fitsCategory(String cat);
}
