package com.example.apps.twitterlogindemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CallbackActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		Runnable handleResponse_runnable = new Runnable() {
			@Override
			public void run() {
				try {
					MainActivity.twitterLoginTool.handleLoginResponse(intent); 
				} catch (Exception e) { 
					Log.e("TLD", "Problem handling Twitter login response", e); 
				}
				
			}
		}; 
		new Thread(handleResponse_runnable).start(); 
		finish(); 
	}

}
