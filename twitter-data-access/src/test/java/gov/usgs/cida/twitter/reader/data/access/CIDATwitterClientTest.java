package gov.usgs.cida.twitter.reader.data.access;

import com.google.common.eventbus.EventBus;
import static org.junit.Assert.*;

/**
 *
 * @author isuftin
 */
public class CIDATwitterClientTest {

    private String user = "";
    private String pass = "";
    
    public CIDATwitterClientTest() {
    }
    
    @org.junit.BeforeClass
    public void beforeClass() {
        this.user = "test";
        this.pass = "test";
    }

    @org.junit.Test
    public void testClientCreationUsingUsernameAndPass() {
        System.out.println("testClientCreation");
        CIDATwitterClient test = new CIDATwitterClient(this.user, this.pass);
        assertNotNull(test);
    }

    @org.junit.Test
    public void testGetValidEventBus() {
        System.out.println("testGetValidEventBus");
        CIDATwitterClient client = new CIDATwitterClient(this.user, this.pass);
        EventBus test = CIDATwitterClient.getEventBus();
        assertNotNull(test);
    }

    @org.junit.Test
    public void testTryConnection() {
        System.out.println("testTryConnection");
        CIDATwitterClient client = new CIDATwitterClient(this.user, this.pass);
        client.connect();
        client.stop(100);
        assertTrue(true);
    }

}
