package com.charredgames.chemify.activity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import android.os.AsyncTask;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Jan 20, 2014
 */
public class SendPost extends AsyncTask<String, Void, HttpResponse>{

	private HttpClient client;
	private HttpPost post;
	protected Exception exception;
	
	public SendPost(HttpClient client, HttpPost post){
		this.client = client;
		this.post = post;
	}
	
	protected HttpResponse doInBackground(String... params) {
		try {
			HttpResponse response = client.execute(post);
            return response;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
	}

}
