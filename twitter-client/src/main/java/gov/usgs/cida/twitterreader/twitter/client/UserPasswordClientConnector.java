package gov.usgs.cida.twitterreader.twitter.client;

import gov.usgs.cida.twitter.reader.data.client.TwitterClient;

/**
 *
 * @author isuftin
 */
public class UserPasswordClientConnector implements IAuthTypeClientBuilder{

    private final String username;
    private final String password;
    private TwitterClient twitterClient;
    
    private UserPasswordClientConnector() {
        this.username = null;
        this.password = null;
    }

    /**
     * Creates a User/Password based Twitter IClient
     *
     * @param username Twitter user name
     * @param password Twitter password
     * {@link TwitterClient()}
     */
    public UserPasswordClientConnector(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public TwitterClient buildTwitterClient() {
       return new TwitterClient(username, password);
    }

}
