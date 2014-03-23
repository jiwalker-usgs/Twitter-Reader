package gov.usgs.cida.twitter.reader.data.access;

import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.httpclient.auth.Authentication;
import gov.usgs.cida.twitter.reader.data.client.TwitterClientContext;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitter.reader.data.client.TwitterClientBuilder;
import gov.usgs.cida.twitter.reader.data.client.auth.OAuth;
import gov.usgs.cida.twitter.reader.data.client.auth.UserPasswordAuth;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author isuftin
 */
public class CIDATwitterClientTest {

    private static String user = "";
    private static String pass = "";
    private static String consumerKey = "test";
    private static String consumerSecret = "test";
    private static String token = "test";
    private static String secret = "test";
    private static List<Long> userIds = new ArrayList<>();
    private static List<String> terms = null;
    private static List<Location> locations = null;

    public CIDATwitterClientTest() {
    }

    @Before
    public void before() {
        user = "";
        pass = "";
        consumerKey = "";
        consumerSecret = "";
        token = "";
        secret = "";
        userIds = new ArrayList<>();
        terms = new ArrayList<>();
        locations = new ArrayList<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingNoContext() {
        System.out.println("testBuildClientUsingNoContext");
        TwitterClientBuilder builder = new TwitterClientBuilder();
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingNullContext() {
        System.out.println("testBuildClientUsingNullContext");
        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(null);
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingBlankUserNameAndPass() {
        System.out.println("testBuildClientUsingBlankUserNameAndPass");
        Authentication auth = new UserPasswordAuth(user, pass).createAuthentication();
        TwitterClientContext context = new TwitterClientContext(auth);
        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingNullUserNameAndPass() {
        System.out.println("testBuildClientUsingNullUserNameAndPass");
        Authentication auth = new UserPasswordAuth(null, null).createAuthentication();
        TwitterClientContext context = new TwitterClientContext(auth);
        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingBlankOAuthCredentials() {
        System.out.println("testBuildClientUsingBlankOAuthCredentials");
        Authentication auth = new OAuth(consumerKey, consumerSecret, token, secret).createAuthentication();
        TwitterClientContext context = new TwitterClientContext(auth);
        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingNullOAuthCredentials() {
        System.out.println("testBuildClientUsingNullOAuthCredentials");
        Authentication auth = new OAuth(null, null, null, null).createAuthentication();
        TwitterClientContext context = new TwitterClientContext(auth);
        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingUsernameAndPassUsingUnpopulatedContext() {
        System.out.println("testBuildClientUsingNullOAuthCredentials");
        user = "test";
        pass = "test";
        Authentication auth = new UserPasswordAuth(user, pass).createAuthentication();
        TwitterClientContext context = new TwitterClientContext(auth);
        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test
    public void successfullyBuildClientUsingUsernameAndPassUsingPopulatedContext() {
        System.out.println("testBuildClientUsingNullOAuthCredentials");
        user = "test";
        pass = "test";
        userIds.add(Long.MIN_VALUE);

        Authentication auth = new UserPasswordAuth(user, pass).createAuthentication();
        TwitterClientContext context = new TwitterClientContext(auth);
        context.setUserIds(userIds);

        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        TwitterClient client = builder.build();

        assertThat(client, is(notNullValue()));
    }

    @Test
    public void successfullyBuildClientUsingOAuthUsingPopulatedContext() {
        System.out.println("successfullyBuildClientUsingOAuthUsingPopulatedContext");
        consumerKey = "test";
        consumerSecret = "test";
        token = "test";
        secret = "test";
        userIds.add(Long.MIN_VALUE);

        Authentication auth = new OAuth(consumerKey, consumerSecret, token, secret).createAuthentication();
        TwitterClientContext context = new TwitterClientContext(auth);
        context.setUserIds(userIds);

        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        TwitterClient client = builder.build();

        assertThat(client, is(notNullValue()));
    }
}
