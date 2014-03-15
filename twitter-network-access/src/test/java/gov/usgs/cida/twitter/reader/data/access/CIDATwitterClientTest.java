package gov.usgs.cida.twitter.reader.data.access;

import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitter.reader.data.observer.impl.LoggingEventObserver;
import gov.usgs.cida.twitter.reader.data.observer.impl.LoggingMessageObserver;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
    private static List<Long> userIds = Lists.newArrayList(467664169l);
    private static List<String> terms = null;
    private static List<Location> locations = null;

    public CIDATwitterClientTest() {
    }

    @BeforeClass
    public static void beforeClass() {
        user = "";
        pass = "";
        consumerKey = "";
        consumerSecret = "";
        token = "";
        secret = "";
        userIds = new ArrayList<>();
        terms = new ArrayList<>();
        locations = new ArrayList<>();;
    }

    @Test
    public void testClientCreationUsingUsernameAndPass() {
        System.out.println("testClientCreation");
        TwitterClient test = new TwitterClient(user, pass);
        assertNotNull(test);
    }

    @Test
    public void testGetValidEventBus() {
        System.out.println("testGetValidEventBus");
        TwitterClient client = new TwitterClient(user, pass);
        EventBus test = TwitterClient.getEventBus();
        assertNotNull(test);
    }

    @Test
    @Ignore
    public void testTryConnectionWithNormalAuth() {
        System.out.println("testTryConnectionWithNormalAuth");
        TwitterClient client = new TwitterClient(user, pass);
        client.connect();
        client.stop(0);
        assertTrue(true);
    }

    @Test
    @Ignore
    public void testTryConnectionWithOAuth() {
        System.out.println("testTryConnectionWithOAuth");
        TwitterClient client = new TwitterClient(consumerKey, consumerSecret, token, secret);
        client.connect();
        client.stop(0);
        assertTrue(true);
    }

    @Test
    @Ignore
    public void testTryCreatingMessageLogger() throws InterruptedException {
        System.out.println("testTryCreatingMessageLogger");
        TwitterClient client = new TwitterClient(consumerKey, consumerSecret, token, secret);
        client.connect();
        LoggingMessageObserver lmo = new LoggingMessageObserver();
        lmo.register();
        client.startMessageQueueing();
        synchronized (this) {
            wait(30000l);
        }
        client.stop(0);
        assertTrue(true);
    }

    @Test
    @Ignore
    public void testTryCreatingEventLogger() throws InterruptedException {
        System.out.println("testTryCreatingMessageLogger");
        TwitterClient client = new TwitterClient(consumerKey, consumerSecret, token, secret);
        
        LoggingEventObserver leo = new LoggingEventObserver();
        leo.register();
        client.startEventQueueing();
        
        client.connect();
        
        synchronized (this) {
            wait(30000l);
        }
        client.stop(0);
        assertTrue(true);
    }

}
