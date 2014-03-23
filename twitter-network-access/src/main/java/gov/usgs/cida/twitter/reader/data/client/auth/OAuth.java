package gov.usgs.cida.twitter.reader.data.client.auth;

import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author isuftin
 */
public class OAuth implements IAuthType {

    private final String consumerKey;
    private final String consumerSecret;
    private final String token;
    private final String secret;

    private OAuth() {
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
    public OAuth(String consumerKey, String consumerSecret, String token, String secret) {
        if (StringUtils.isBlank(consumerKey) || StringUtils.isBlank(consumerSecret)
                || StringUtils.isBlank(token) || StringUtils.isBlank(secret)) {
            throw new IllegalArgumentException("Required: consumerKey, consumerSecret, token, secret");
        }
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.secret = secret;
    }

    @Override
    public Authentication createAuthentication() {
        return new OAuth1(this.consumerKey, this.consumerSecret, this.secret, this.token);
    }
}
