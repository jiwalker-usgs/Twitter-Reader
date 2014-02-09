package gov.usgs.cida.twitter.reader.data.access;

import com.google.common.eventbus.EventBus;
import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitter.reader.data.observer.impl.LoggingMessageObserver;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 *
 * @author isuftin
 */
public class CIDATwitterClientTest {

    private static String user = "";
    private static String pass = "";
    private static String consumerKey = "";
    private static String consumerSecret = "";
    private static String token = "";
    private static String secret = "";
    private static List<Long> userIds = null;
    private static List<String> terms = null;
    private static List<Location> locations = null;

    public CIDATwitterClientTest() {
    }

    @org.junit.BeforeClass
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

    @org.junit.Test
    public void testClientCreationUsingUsernameAndPass() {
        System.out.println("testClientCreation");
        CIDATwitterClient test = new CIDATwitterClient(user, pass, userIds, terms, locations);
        assertNotNull(test);
    }

    @org.junit.Test
    public void testGetValidEventBus() {
        System.out.println("testGetValidEventBus");
        CIDATwitterClient client = new CIDATwitterClient(user, pass, userIds, terms, locations);
        EventBus test = CIDATwitterClient.getEventBus();
        assertNotNull(test);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testTryConnectionWithNormalAuth() {
        System.out.println("testTryConnectionWithNormalAuth");
        CIDATwitterClient client = new CIDATwitterClient(user, pass, userIds, terms, locations);
        client.connect();
        client.stop(0);
        assertTrue(true);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testTryConnectionWithOAuth() {
        System.out.println("testTryConnectionWithOAuth");
        CIDATwitterClient client = new CIDATwitterClient(consumerKey, consumerSecret, token, secret, userIds, terms, locations);
        client.connect();
        client.stop(0);
        assertTrue(true);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testTryCreatingLogger() throws InterruptedException {
        System.out.println("testTryConnectionWithOAuth");
        CIDATwitterClient client = new CIDATwitterClient(consumerKey, consumerSecret, token, secret, userIds, terms, locations);
        client.connect();
        LoggingMessageObserver lmo = new LoggingMessageObserver();
        lmo.register();
        client.startMessageQueueing();
        synchronized(this) {
            wait(30000l);
        }
        client.stop(0);
        assertTrue(true);
    }

}
