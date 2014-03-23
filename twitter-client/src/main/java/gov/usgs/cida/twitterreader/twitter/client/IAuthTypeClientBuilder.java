package gov.usgs.cida.twitterreader.twitter.client;

import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import java.util.List;

/**
 * Represents a builder for a client based on the authentication method used
 *
 * @author isuftin
 */
public interface IAuthTypeClientBuilder {

    /**
     * Creates a new Twitter Client based on the authentical method of the
     * builder
     *
     * @return
     */
    public TwitterClient buildTwitterClient();

    /**
     * @param locations the locations to set
     * @return
     */
    public IAuthTypeClientBuilder setLocations(List<Location> locations);

    /**
     * @param terms the terms to set
     * @return
     */
    public IAuthTypeClientBuilder setTerms(List<String> terms);

    /**
     * @param userIds the userIds to set
     * @return
     */
    public IAuthTypeClientBuilder setUserIds(List<Long> userIds);
}
