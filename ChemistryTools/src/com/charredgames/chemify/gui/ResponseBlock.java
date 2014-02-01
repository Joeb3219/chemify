package com.charredgames.chemify.gui;

import android.text.Html;
import android.text.Spanned;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Jan 30, 2014
 */
public class ResponseBlock {

	private Spanned answer, expanded = null;
	
	public ResponseBlock(Spanned answer, Spanned expanded){
		this.answer = answer;
		this.expanded = expanded;
	}
	
	public ResponseBlock(Spanned answer){
		this.answer = answer;
	}

	public Spanned getAnswer(){
		return answer;
	}
	
	public CharSequence getExpanded(){
		if(expanded == null) return removeWhiteSpace(answer);
		return removeWhiteSpace(Html.fromHtml(Html.toHtml(expanded) + "<b>" + Html.toHtml(answer) + "</b>"));
	}
	
	private CharSequence removeWhiteSpace(CharSequence text) {
        while (text.charAt(text.length() - 1) == '\n') text = text.subSequence(0, text.length() - 1);
        return text;
    }
	
}
