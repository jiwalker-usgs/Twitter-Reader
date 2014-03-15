package gov.usgs.cida.twitterreader.twitter.client;

import gov.usgs.cida.twitter.reader.data.client.TwitterClient;

/**
 *
 * @author isuftin
 */
public class SingleSignonClientBuilder implements IAuthTypeClientBuilder{

    private final String consumerKey;
    private final String consumerSecret;
    private final String token;
    private final String secret;
    
    private SingleSignonClientBuilder() {
        this.consumerKey = null;
        this.consumerSecret = null;
        this.token = null;
        this.secret = null;
    }

    /**
     * Creates a Single Signon Twitter IClient
     *
     * @param consumerKey Twitter API key
     * @param consumerSecret Twitter API secret
     * @param token Twitter Access token
     * @param secret Twitter Access token secret
     */
    public SingleSignonClientBuilder(String consumerKey, String consumerSecret, String token, String secret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.secret = secret;
    }

    @Override
    public TwitterClient buildTwitterClient() {
       return new TwitterClient(consumerKey, consumerSecret, token, secret);
    }
}
