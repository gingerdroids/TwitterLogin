package com.example.apps.twitterlogindemo;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Knows how to get authorization from Twitter for this app to access Twitter. 
 */
public class TwitterLoginTool { 
	
	private RequestToken requestToken = null ; // Is set when sending login request
	
	private final TwitterServer twitterServer ; 
	
	private final MainActivity mainActivity; 
	
	TwitterLoginTool(TwitterServer twitterServer, MainActivity mainActivity) { 
		this.twitterServer = twitterServer ; 
		this.mainActivity = mainActivity ; 
	}
	
	/**
	 * Send the request to Twitter. 
	 */
	void startLoginActivity(Activity activity) throws TwitterException { 
		Twitter twitter = twitterServer.getTwitterInstance(); 
		this.requestToken = twitter.getOAuthRequestToken(TwitterServer.CALLBACK_URI); // This goes to internet! 
		Uri requestUri = Uri.parse(requestToken.getAuthenticationURL());
		Log.d("TLD", "startLoginActivity(): requestURI is   "+requestUri); 
		final Intent intent = new Intent(Intent.ACTION_VIEW, requestUri); 
		activity.startActivity(intent); 
	}
	
	/**
	 * Handle Twitter's response. 
	 */
	void handleLoginResponse(Intent intent) throws TwitterException { 
		/* See "https://dev.twitter.com/web/sign-in/implementing"
		 * Note that the oauth_token received in step 1 is different to the oauth_token received in Step 3. Likewise the oauth_token_secret. 
		 */ 
		Log.d("TLD", "Starting  TwitterLoginTool.handleLoginResponse()"); 
		//////  Dissect RequestToken and its response
		Uri responseUri = intent.getData(); 
		String oauthToken = responseUri.getQueryParameter("oauth_token"); 
		String oauthVerifier = responseUri.getQueryParameter("oauth_verifier"); 
		Log.d("TLD", "handleLoginResponse(): URI is   "+responseUri); 
		Log.d("TLD", "handleLoginResponse(): oauth_token from URI is   "+oauthToken); 
		{
		}
		/* From page "https://dev.twitter.com/web/sign-in/implementing" - Your application should examine the HTTP status of the response. Any value other than 200 indicates a failure. */ 
		// TODO Handle login failure 
		//////  Construct AccessToken
		Twitter twitter = twitterServer.getTwitterInstance(); 
		AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
		//////  Store AccessToken for use in this invocation of app, and info for building AccessToken in future invocations of app
		{
			twitterServer.setAccessToken(accessToken); 
			Log.d("TLD", "handleLoginResponse(): getToken() from accessToken is   "+accessToken.getToken()); 
		}
		//////  Notify main activity we have a Twitter AccessToken. 
		mainActivity.runNormalInitialization(); 
	}

}
