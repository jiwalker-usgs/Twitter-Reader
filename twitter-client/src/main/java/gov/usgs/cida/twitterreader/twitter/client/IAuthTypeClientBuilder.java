package gov.usgs.cida.twitterreader.twitter.client;

import gov.usgs.cida.twitter.reader.data.client.TwitterClient;

/**
 * Represents a builder for a client based on the authentication method used
 *
 * @author isuftin
 */
public interface IAuthTypeClientBuilder {
    
    /**
     * Creates a new Twitter Client based on the authentical method of the builder
     * @return 
     */
    public TwitterClient  buildTwitterClient();
}
