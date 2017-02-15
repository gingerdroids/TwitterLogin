package com.example.apps.twitterlogindemo;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.api.UsersResources;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.util.Log;

public class TwitterServer {
	
	static final String CONSUMER_KEY = "REPLACE ME" ; // TODO Fill with consumer key for your app. See "https://apps.twitter.com/app/new". 
	
	static final String CONSUMER_SECRET= "REPLACE ME" ; // TODO Fill with consumer secret for your app. See "https://apps.twitter.com/app/new". 
	
    static final String CALLBACK_URI = "myapp://twitterlogindemo.apps.example.com" ; // Must be consistent with CallbackActivity in manifest. 
    
    /*
     * CALLBACK_URI: Ideally, the URI should not have a non-standard schema (ie, "myapp"). 
     * However, if we use "http" etc, the user will be offered the dialogue which-app-do-you-want-to-use. 
     * This is annoying to the user, and will break the login processing if they don't choose this app.  
     */
    
    static TwitterPreferences twitterPreferences ; 
	
	private final TwitterFactory twitterFactory ; 
	
	private AccessToken accessToken = null ; 

	public TwitterServer(Context applicationContext) { 
		if (twitterPreferences==null) twitterPreferences = new TwitterPreferences(applicationContext); 
		ConfigurationBuilder builder = new ConfigurationBuilder();
		if (CONSUMER_KEY==null || CONSUMER_SECRET==null) throw new NullPointerException("These hard-coded constants need to be edited."); 
		builder.setOAuthConsumerKey(CONSUMER_KEY);
		builder.setOAuthConsumerSecret(CONSUMER_SECRET);
		twitterFactory = new TwitterFactory(builder.build()); 
		String accessTokenString = twitterPreferences.getAccessToken(); 
		String accessSecretString = twitterPreferences.getAccessSecret(); 
		if (accessTokenString!=null && accessSecretString!=null) { 
			Log.d("TLD", "TwitterPreferences has access info"); 
			accessToken = new AccessToken(accessTokenString, accessSecretString); 
		} else { 
			Log.d("TLD", "TwitterPreferences is missing access info"); 
		}
	}

	/**
	 * Returns a {@link Twitter} instance from the factory. 
	 */
	public Twitter getTwitterInstance() {
		return twitterFactory.getInstance(accessToken);
	}
	
	/**
	 * Whether we currently have an {@link AccessToken} instance. 
	 */
	public boolean hasAccessToken() { 
		return accessToken != null ; 
	}
	
	/**
	 * Getter, may return null. 
	 */
	public AccessToken getAccessToken() { 
		return accessToken ; 
	}

	/**
	 * Setter for {@link #accessToken} field. 
	 * Also saves token info in {@link TwitterPreferences}. 
	 */
	void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken ; 
		twitterPreferences.setAccessTokenAndSecret(accessToken.getToken(), accessToken.getTokenSecret()); 
	}

	/**
	 * Fetches the requested {@link Status} from Twitter's server. 
	 * Assumes we have an access-token. 
	 */
	public Status fetchStatus(long id) throws TwitterException { 
		if (accessToken==null) throw new IllegalStateException("Do not have an AccessToken"); // Theoretically optional(?), but allocates usage to user rather than app. 
		Twitter twitter = twitterFactory.getInstance(accessToken); 
		Status status = twitter.showStatus(id);
		return status;
	} 

	/**
	 * Fetches the requested {@link User} from Twitter's server. 
	 * Assumes we have an access-token. 
	 */
	public User fetchUser(long id) throws TwitterException { 
		if (accessToken==null) throw new IllegalStateException("Do not have an AccessToken"); // Theoretically optional(?), but allocates usage to user rather than app. 
		Twitter twitter = twitterFactory.getInstance(accessToken); 
		return twitter.showUser(id);
	}

	/**
	 * Fetches the requested {@link User} from Twitter's server. 
	 * Assumes we have an access-token. 
	 */
	public User getUser(String posterName) throws TwitterException { 
		if (accessToken==null) throw new IllegalStateException("Do not have an AccessToken"); // Theoretically optional(?), but allocates usage to user rather than app. 
		Twitter twitter = twitterFactory.getInstance(accessToken); 
		UsersResources usersResources = twitter.users(); 
		return usersResources.showUser(posterName); 
	} 

}
