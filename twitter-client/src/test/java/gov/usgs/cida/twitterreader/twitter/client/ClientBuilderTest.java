package gov.usgs.cida.twitterreader.twitter.client;

import gov.usgs.cida.twitter.reader.data.observer.impl.LoggingEventObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author isuftin
 */
public class ClientBuilderTest {
    
    private IAuthTypeClientBuilder userPassBuilder;
    private IAuthTypeClientBuilder ssoBuilder;
    
    public ClientBuilderTest() {
    }
    
    @Before
    public void setUp() {
        userPassBuilder = new UserPasswordClientConnector("test", "test");
        ssoBuilder = new SingleSignonClientBuilder("consumerKeyHere", "consumerSecretHere", "tokenHere", "secretHere");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAddConnector() {
        System.out.println("addConnector");
        ClientBuilder instance = new ClientBuilder()
                .addConnector(ssoBuilder);
        assertThat(instance, is(notNullValue()));
    }

    @Test
    public void testBuild() {
        System.out.println("testBuild");
        ClientBuilder instance = new ClientBuilder()
                .addConnector(ssoBuilder);
        IClient test = instance.build();
        assertThat(test, is(notNullValue()));
    }
    
    @Test
    public void testBuildWithObservers() {
        System.out.println("testBuildWithObservers");
        ClientBuilder instance = new ClientBuilder()
                .addConnector(ssoBuilder)
                .addClientObserver(new LoggingEventObserver());
        IClient test = instance.build();
        assertThat(test, is(notNullValue()));
        assertThat(test.getObservers().size(), is(1));
        assertThat(test.getObservers().get(0), is(instanceOf(LoggingEventObserver.class)));
    }
    
}
