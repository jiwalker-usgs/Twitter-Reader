package gov.usgs.cida.twitterreader.twitter.client;

import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitter.reader.data.client.ClientContext;
import java.util.List;

/**
 *
 * @author isuftin
 */
public class SingleSignonClientBuilder extends AbstractAuthTypeClientBuilder {

    private SingleSignonClientBuilder() {
        this.setContext(null);
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
        this.setContext(new ClientContext(consumerKey, consumerSecret, token, secret));
    }

    /**
     * @param locations the locations to set
     * @return
     */
    @Override
    public SingleSignonClientBuilder setLocations(List<Location> locations) {
        this.getContext().setLocations(locations);
        return this;
    }

    /**
     * @param terms the terms to set
     * @return
     */
    @Override
    public SingleSignonClientBuilder setTerms(List<String> terms) {
        this.getContext().setTerms(terms);
        return this;
    }

    /**
     * @param userIds the userIds to set
     * @return
     */
    @Override
    public SingleSignonClientBuilder setUserIds(List<Long> userIds) {
        this.getContext().setUserIds(userIds);
        return this;
    }
}
