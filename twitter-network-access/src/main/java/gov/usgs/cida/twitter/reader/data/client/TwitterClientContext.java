package gov.usgs.cida.twitter.reader.data.client;

import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.httpclient.auth.Authentication;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the context used in building the client
 *
 * @author isuftin
 */
public class TwitterClientContext {

    private Authentication auth = null;
    private List<Long> userIds = new ArrayList<>();
    private List<String> terms = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();

    private TwitterClientContext() {
    }

    public TwitterClientContext(Authentication auth) {
        if (auth == null) {
            throw new IllegalArgumentException("Authentication object not provided");
        }
        this.auth = auth;
    }

    Authentication getAuth() {
        return auth;
    }

    /**
     * @return the userIds
     */
    public List<Long> getUserIds() {
        return new ArrayList<>(userIds);
    }

    /**
     * @param userIds the userIds to set
     */
    public void setUserIds(List<Long> userIds) {
        if (userIds != null) {
            this.userIds = new ArrayList<>(userIds);
        }
    }

    /**
     * @return the terms
     */
    public List<String> getTerms() {
        return new ArrayList<>(terms);
    }

    /**
     * @param terms the terms to set
     */
    public void setTerms(List<String> terms) {
        if (terms != null) {
            this.terms = new ArrayList<>(terms);
        }
    }

    /**
     * @return the locations
     */
    public List<Location> getLocations() {
        return new ArrayList<>(locations);
    }

    /**
     * @param locations the locations to set
     */
    public void setLocations(List<Location> locations) {
        if (locations != null) {
            this.locations = new ArrayList<>(locations);
        }
    }

    boolean isEmpty() {
        int population = 0;
        boolean empty;

        population += userIds.size();
        population += terms.size();
        population += locations.size();

        empty = population == 0;

        return empty;
    }

}
