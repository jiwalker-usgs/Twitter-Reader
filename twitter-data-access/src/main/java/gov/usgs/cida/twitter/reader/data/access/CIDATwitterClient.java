package gov.usgs.cida.twitter.reader.data.access;

import com.google.common.eventbus.EventBus;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.BasicAuth;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client to asynchronously pull messages from CIDA's Twitter queue
 * 
 * @author isuftin
 */
public class CIDATwitterClient extends Observable {
    private static Client client = null;
    private static Boolean isConnected = false;
    private static BlockingQueue<String> messageQueue = null;
    private static BlockingQueue<Event> eventQueue = null;
    private static EventBus eventBus;
    private final static Logger LOGGER = LoggerFactory.getLogger(CIDATwitterClient.class);
    private final static ScheduledExecutorService eventSes = Executors.newScheduledThreadPool(1);
    private final static ScheduledExecutorService messageSes = Executors.newScheduledThreadPool(1);
    private final static ScheduledFuture<CIDATwitterEventRunner> eventFuture = null;
    private final static ScheduledFuture<CIDATwitterMessageRunner> messageFuture = null;
    
    /**
     * Thread runnable object that checks the message queue and posts new messages
     * to observers
     */
    private static class CIDATwitterMessageRunner implements Runnable {
        private final static Logger RUNNER_LOGGER = LoggerFactory.getLogger(CIDATwitterMessageRunner.class);
        @Override
        public void run() {
            if (isConnected) {
                while (!messageQueue.isEmpty()) {
                    try {
                        String message = messageQueue.take();
                        getEventBus().post(message);
                    } catch (InterruptedException ex) {
                        RUNNER_LOGGER.warn("Message queue thread interrupted: " + ex);
                    }
                }
            }
        }
    }
    
    /**
     * Thread runnable object that checks the event queue and posts new events
     * to observers
     */
    private static class CIDATwitterEventRunner implements Runnable {
        private final static Logger RUNNER_LOGGER = LoggerFactory.getLogger(CIDATwitterEventRunner.class);
        @Override
        public void run() {
            if (isConnected) {
                while (!eventQueue.isEmpty()) {
                    try {
                        Event event = eventQueue.take();
                        getEventBus().post(event);
                    } catch (InterruptedException ex) {
                        RUNNER_LOGGER.warn("Event queue thread interrupted: " + ex);
                    }
                }
            }
        }
    }
    
    /**
     * Create a Twitter client using OAuth
     * @param consumerKey
     * @param consumerSecret
     * @param token
     * @param secret 
     */
    public CIDATwitterClient(String consumerKey, String consumerSecret, String token, String secret) {
        if (client == null) {
            client = this.buildClient(new OAuth1(consumerKey, consumerSecret, token, secret));
            LOGGER.debug("New Twitter client created");
        }
    }
    
    public CIDATwitterClient(String username, String password) {
        if (client == null) {
            client = this.buildClient(new BasicAuth(username, password));
            LOGGER.debug("New Twitter client created");
        }
    }
    
    /**
     * Connects the Twitter client to Twitter
     */
    public void connect() {
        if (!isConnected) {
            client.connect();
            isConnected = true;
            LOGGER.info("Twitter client connected");
        }
    }
    
    /**
     * Starts the message queuing for the Twitter client
     */
    public void startMessageQueueing() {
        if (null == messageFuture || messageFuture.isDone() || messageFuture.isCancelled()) {
            messageSes.scheduleAtFixedRate(new CIDATwitterMessageRunner(), 500, 1, TimeUnit.MINUTES);
        }
    }
    
    /**
     * Stops the message queuing for the Twitter client
     */
    public void stopMessageQueueing() {
        if (null != messageFuture && !messageFuture.isDone() && !messageFuture.isCancelled()) {
           messageFuture.cancel(false);
        }
    }
    
    /**
     * Starts the event queuing for the Twitter client
     */
    public void startEventQueueing() {
        if (null == eventFuture || eventFuture.isDone() || eventFuture.isCancelled()) {
            eventSes.scheduleAtFixedRate(new CIDATwitterEventRunner(), 500, 1, TimeUnit.MINUTES);
        }
    }
    
    /** 
     * Stops the event queuing for the Twitter client
     */
    public void stopEventQueueing() {
        if (null != eventFuture && !eventFuture.isDone() && !eventFuture.isCancelled()) {
            eventFuture.cancel(false);
        }
    }

    public void stop(Integer waitMillis) {
        LOGGER.info("Twitter client stopping");
        this.stopEventQueueing();
        this.stopMessageQueueing();
        client.stop(waitMillis);
        LOGGER.info("Twitter client stopped");
    }
    
    private Client buildClient(Authentication auth) {
        messageQueue = new LinkedBlockingQueue<>(100000);
        eventQueue = new LinkedBlockingQueue<>(1000);
        Hosts cidaHosts = new HttpHosts(Constants.STREAM_HOST);
        StreamingEndpoint endpoint = new StatusesFilterEndpoint();
        
        ClientBuilder cb = new ClientBuilder().
                name("CIDA-Twitter-Client").
                hosts(cidaHosts).
                authentication(auth).
                endpoint(endpoint).
                processor(new StringDelimitedProcessor(messageQueue)).
                eventMessageQueue(eventQueue);

        return cb.build();
    }
    
    /**
     * During finalize, shuts down the Twitter client
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            if (null != client) {
                this.stop(10000);
            }
        } finally {
            super.finalize();
        }
    }

    /**
     * Gets the singleton event bus to subscribe to
     * @return 
     */
    public static EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = new EventBus("Twitter-Eventbus");
        }
        return eventBus;
    }
}
