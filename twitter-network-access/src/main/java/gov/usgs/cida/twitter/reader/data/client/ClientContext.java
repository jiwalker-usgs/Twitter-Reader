package gov.usgs.cida.twitter.reader.data.client;

import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.BasicAuth;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents the context used in building the client
 *
 * @author isuftin
 */
public class ClientContext {

    private Authentication auth = null;
    private List<Long> userIds = new ArrayList<>();
    private List<String> terms = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();

    private ClientContext(){};
    
    public ClientContext(String consumerKey, String consumerSecret, String token, String secret) {
        if (StringUtils.isNotBlank(consumerKey) && StringUtils.isNotBlank(consumerSecret)
                && StringUtils.isNotBlank(token) && StringUtils.isNotBlank(secret)) {
            auth = new OAuth1(consumerKey, consumerSecret, token, secret);
        } else {
            throw new IllegalArgumentException("consumerKey, consumerSecret, token and secret must be populated");
        }
    }
    
    public ClientContext(String userName, String password) {
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            auth = new BasicAuth(userName, password);
        } else {
            throw new IllegalArgumentException("Username and password must be populated");
        }
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
        this.userIds = new ArrayList<>(userIds);
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
        this.terms = new ArrayList<>(terms);
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
        this.locations = new ArrayList<>(locations);
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
