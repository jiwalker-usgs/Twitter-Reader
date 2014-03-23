package gov.usgs.cida.twitterreader.twitter.client;

import gov.usgs.cida.twitter.reader.data.client.auth.OAuth;
import gov.usgs.cida.twitter.reader.data.client.auth.IAuthType;
import gov.usgs.cida.twitter.reader.data.client.auth.UserPasswordAuth;
import com.google.common.collect.Lists;
import gov.usgs.cida.twitterreader.commons.queue.QueueParams;
import gov.usgs.cida.twitterreader.commons.observer.LoggingEventObserver;
import java.util.concurrent.TimeUnit;
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

    private IAuthType userPassAuth;
    private IAuthType ssoAuth;

    public ClientBuilderTest() {
    }

    @Before
    public void setUp() {
        userPassAuth = new UserPasswordAuth("test", "test");
        ssoAuth = new OAuth("consumerKeyHere", "consumerSecretHere", "tokenHere", "secretHere");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitializationUsingSingleSignonAuth() {
        System.out.println("testInitializationUsingSingleSignonAuth");
        ClientBuilder instance = new ClientBuilder(ssoAuth);
        assertThat(instance, is(notNullValue()));
    }

    @Test
    public void testInitializationUsingUserAndPasswordAuth() {
        System.out.println("testInitializationUsingUserAndPasswordAuth");
        ClientBuilder instance = new ClientBuilder(userPassAuth);
        assertThat(instance, is(notNullValue()));
    }

    @Test
    public void testBuildClient() {
        System.out.println("testBuildClient");
        ClientBuilder instance = new ClientBuilder(ssoAuth).
            setTerms(Lists.asList("term", new String[]{"test2"}));
        IClient test = instance.build();
        assertThat(test, is(notNullValue()));
    }

    @Test
    public void testBuildWithObservers() {
        System.out.println("testBuildWithObservers");
        ClientBuilder instance = new ClientBuilder(ssoAuth).
                setTerms(Lists.asList("term", new String[]{"test2"})).
                addObserver(new LoggingEventObserver());
        IClient test = instance.build();
        assertThat(test, is(notNullValue()));
        assertThat(test.getObservers().size(), is(1));
        assertThat(test.getObservers().get(0), is(instanceOf(LoggingEventObserver.class)));
    }

    @Test
    public void testBuildWithQueueParams() {
        System.out.println("testBuildWithQueueParams");
        ClientBuilder builder = new ClientBuilder(ssoAuth).
                setTerms(Lists.asList("term", new String[]{"test2"})).
                setEventQueueParams(new QueueParams(0l, 0l, TimeUnit.MINUTES)).
                setMessageQueueParams(new QueueParams(0l, 0l, TimeUnit.MINUTES));
        IClient client = builder.build();
        assertThat(client, is(notNullValue()));
    }

}
