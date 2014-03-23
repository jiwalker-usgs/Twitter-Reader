package gov.usgs.cida.twitterreader.twitter.client;

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

    private IAuthTypeClientBuilder userPassBuilder;
    private IAuthTypeClientBuilder ssoBuilder;

    public ClientBuilderTest() {
    }

    @Before
    public void setUp() {
        userPassBuilder = new UserPasswordClientBuilder("test", "test");
        ssoBuilder = new SingleSignonClientBuilder("consumerKeyHere", "consumerSecretHere", "tokenHere", "secretHere");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddSingleSignonConnector() {
        System.out.println("testAddSingleSignonConnector");
        ClientBuilder instance = new ClientBuilder(ssoBuilder);
        assertThat(instance, is(notNullValue()));
    }

    @Test
    public void testAddUserPasswordConnector() {
        System.out.println("testAddUserPasswordConnector");
        ClientBuilder instance = new ClientBuilder(userPassBuilder);
        assertThat(instance, is(notNullValue()));
    }

    @Test
    public void testBuild() {
        System.out.println("testBuild");
        ssoBuilder.setTerms(Lists.asList("term", new String[]{"test2"}));
        ClientBuilder instance = new ClientBuilder(ssoBuilder);
        IClient test = instance.build();
        assertThat(test, is(notNullValue()));
    }

    @Test
    public void testBuildWithObservers() {
        System.out.println("testBuildWithObservers");
        ssoBuilder.setTerms(Lists.asList("term", new String[]{"test2"}));
        ClientBuilder instance = new ClientBuilder(ssoBuilder)
                .addClientObserver(new LoggingEventObserver());
        IClient test = instance.build();
        assertThat(test, is(notNullValue()));
        assertThat(test.getObservers().size(), is(1));
        assertThat(test.getObservers().get(0), is(instanceOf(LoggingEventObserver.class)));
    }

    @Test
    public void testBuildWithQueueParams() {
        System.out.println("testBuildWithQueueParams");
        ssoBuilder.setTerms(Lists.asList("term", new String[]{"test2"}));
        ClientBuilder builder = new ClientBuilder(ssoBuilder);
        builder.setEventQueueParams(new QueueParams(0l, 0l, TimeUnit.MINUTES));
        builder.setMessageQueueParams(new QueueParams(0l, 0l, TimeUnit.MINUTES));
        IClient client = builder.build();
        assertThat(client, is(notNullValue()));
    }

}
