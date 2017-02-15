package com.example.apps.twitterlogindemo; // TODO Move to appropriate package name. Remember to change "example" where-ever needed, including in the manifest. 

import twitter4j.TwitterException;
import twitter4j.User;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	static TwitterLoginTool twitterLoginTool ;
	
	TwitterServer twitterServer; 
	
	TextView textView ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.twitterServer = new TwitterServer(getApplicationContext()); 
		this.textView = new TextView(getBaseContext()); 
		textView.setText("Just starting..."); 
		setContentView(textView); 
		/* Initialization flow: both branches of this if() will call method runNormalInitialisation(), directly or indirectly. 
		 * Well, unless there's an exception thrown. */
		if (twitterServer.hasAccessToken()) { 
			Log.d("TLD", "twitterServer.hasAccessToken() is true. Straight to normal initialization."); 
			runNormalInitialization(); 
		} else { 
			Log.d("TLD", "twitterServer.hasAccessToken() is FALSE. Run login tool."); 
			twitterLoginTool = new TwitterLoginTool(twitterServer, this); 
			new AsyncTwitterLogin().execute(); 
		}
	}
	
	private class AsyncTwitterLogin extends AsyncTask<Void,Void,Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				twitterLoginTool.startLoginActivity(MainActivity.this);
			} catch (TwitterException e) { 
				Log.e("TLD", "Failed to start Twitter login activity", e); 
			} 
			return null;
		} 
	}
	
	/**
	 * Normal initialization for app. 
	 * Checking for, and ensuring we have, a Twitter access-token, should occur before this method is invoked. 
	 */
	void runNormalInitialization() {
		Log.d("TLD", "Starting  runNormalInitialization()"); 
		if (!twitterServer.hasAccessToken()) { // Check we have an access-token. 
			textView.setText("We don't have an access-token for Twitter."); 
			return ; 
		}
		Runnable asyncInitialization_runnable = new Runnable() { 
			@Override
			public void run() {
				new AsyncInitializer().execute(); 
			}
		};
		runOnUiThread(asyncInitialization_runnable);
	}
	
	private class AsyncInitializer extends AsyncTask<Void, Double, User> { 

		private AsyncInitializer() {}

		@Override
		protected void onPreExecute() { 
		}
		
		/**
		 * Ensures the domain has a <code>TriageInfo</code>, building it if necessary. 
		 */
		@Override
		protected User doInBackground(Void... params) {
			try { 
				Context context = getBaseContext();
				long userId = twitterServer.getAccessToken().getUserId(); 
				User user = twitterServer.fetchUser(userId); 
				return user ; 
			} catch (Exception e) { 
				Log.e("INIT", "AsyncInitializer.doInBackground() threw exception", e); 
				return null ; 
			}
		}
		
		@Override
		protected void onPostExecute(User user) { 
			if (user==null) { 
				textView.setText("Failed to retrieve user info"); 
				return ; 
			}
			textView.setText("User id is "+user.getId()+". Name is "+user.getName()+". Screen name is "+user.getScreenName()+"."); 
		}
	}

}
