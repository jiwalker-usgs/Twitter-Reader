package gov.usgs.cida.twitterreader.twitter.client;

import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitter.reader.data.client.ClientContext;
import java.util.List;

/**
 *
 * @author isuftin
 */
public class UserPasswordClientBuilder extends AbstractAuthTypeClientBuilder {

    private UserPasswordClientBuilder() {
        this.setContext(null);
    }

    /**
     * Creates a User/Password based Twitter IClient
     *
     * @param username Twitter user name
     * @param password Twitter password {@link TwitterClient()}
     */
    public UserPasswordClientBuilder(String username, String password) {
        this.setContext(new ClientContext(username, password));
    }

    /**
     * @param locations the locations to set
     * @return
     */
    @Override
    public UserPasswordClientBuilder setLocations(List<Location> locations) {
        this.getContext().setLocations(locations);
        return this;
    }

    /**
     * @param terms the terms to set
     * @return
     */
    @Override
    public UserPasswordClientBuilder setTerms(List<String> terms) {
        this.getContext().setTerms(terms);
        return this;
    }

    /**
     * @param userIds the userIds to set
     * @return
     */
    @Override
    public UserPasswordClientBuilder setUserIds(List<Long> userIds) {
        this.getContext().setUserIds(userIds);
        return this;
    }

}
