package gov.usgs.cida.twitter.reader.data.client.auth;

import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.BasicAuth;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author isuftin
 */
public class UserPasswordAuth implements IAuthType {

    private String username;
    private String password;

    private UserPasswordAuth() {
        this.username = null;
        this.password = null;
    }

    /**
     * Creates a User/Password based Twitter IClient
     *
     * @param username Twitter user name
     * @param password Twitter password {@link TwitterClient()}
     */
    public UserPasswordAuth(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Required: username, password");
        }
        this.username = username;
        this.password = password;
    }

    @Override
    public Authentication createAuthentication() {
        return new BasicAuth(this.username, this.password);
    }

}
