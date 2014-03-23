package gov.usgs.cida.twitter.reader.data.access;

import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitter.reader.data.client.ClientContext;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitter.reader.data.client.TwitterClientBuilder;
import java.util.ArrayList;
import java.util.List;
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
        ClientContext context = new ClientContext(user, pass);
        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingNullUserNameAndPass() {
        System.out.println("testBuildClientUsingNullUserNameAndPass");
        ClientContext context = new ClientContext(null, null);
        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingBlankOAuthCredentials() {
        System.out.println("testBuildClientUsingBlankOAuthCredentials");
        ClientContext context = new ClientContext(consumerKey, consumerSecret, token, secret);
        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        builder.build();
        fail("Expecting IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildClientUsingNullOAuthCredentials() {
        System.out.println("testBuildClientUsingNullOAuthCredentials");
        ClientContext context = new ClientContext(null, null, null, null);
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
        ClientContext context = new ClientContext(user, pass);
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

        ClientContext context = new ClientContext(user, pass);
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

        ClientContext context = new ClientContext(consumerKey, consumerSecret, token, secret);
        context.setUserIds(userIds);

        TwitterClientBuilder builder = new TwitterClientBuilder().
                setContext(context);
        TwitterClient client = builder.build();

        assertThat(client, is(notNullValue()));
    }
}
