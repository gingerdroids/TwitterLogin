package com.example.apps.twitterlogindemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Stores Twitter access info in a {@link SharedPreferences}. 
 */
public class TwitterPreferences { 
	
	private static final String TWITTER_PREFERENCES = "twitter_preferences" ; 
	private static final String ACCESS_TOKEN_KEY = "access_token" ; 
	private static final String ACCESS_SECRET_KEY = "access_secret" ; 
	
	private final SharedPreferences sharedPreferences ; 

	TwitterPreferences(Context context) { 
		this.sharedPreferences = context.getSharedPreferences(TWITTER_PREFERENCES, Context.MODE_PRIVATE); 
	}
	
	String getAccessToken() { 
		return sharedPreferences.getString(ACCESS_TOKEN_KEY, null); 
	}
	
	String getAccessSecret() { 
		return sharedPreferences.getString(ACCESS_SECRET_KEY, null); 
	}
	
	void setAccessTokenAndSecret(String accessToken, String accessSecret) { 
		Editor editor = sharedPreferences.edit(); 
		editor.putString(ACCESS_TOKEN_KEY, accessToken); 
		editor.putString(ACCESS_SECRET_KEY, accessSecret); 
		editor.commit(); 
	}

}
