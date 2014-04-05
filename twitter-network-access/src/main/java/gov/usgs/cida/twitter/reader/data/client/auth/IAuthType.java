package gov.usgs.cida.twitter.reader.data.client.auth;

import com.twitter.hbc.httpclient.auth.Authentication;

/**
 * Represents a builder for a client based on the authentication method used
 *
 * @author isuftin
 */
public interface IAuthType {
    public Authentication createAuthentication();
}
