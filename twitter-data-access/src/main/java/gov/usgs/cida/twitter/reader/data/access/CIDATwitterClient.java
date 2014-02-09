package gov.usgs.cida.twitter.reader.data.access;

import com.google.common.eventbus.EventBus;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.BasicAuth;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.util.List;
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
    private List<Long> userIds = null;
    private List<String> terms = null;
    private List<Location> locations = null;
    
    /**
     * Thread runnable object that checks the message queue and posts new messages
     * to observers
     */
    private static class CIDATwitterMessageRunner implements Runnable {
        private final static Logger RUNNER_LOGGER = LoggerFactory.getLogger(CIDATwitterMessageRunner.class);
        @Override
        public void run() {
            getEventBus().post("TEST");
            if (isConnected) {
                RUNNER_LOGGER.debug("CIDATwitterMessageRunner checking queue");
                while (!messageQueue.isEmpty()) {
                    try {
                        String message = messageQueue.take();
                        getEventBus().post(message);
                    } catch (InterruptedException ex) {
                        RUNNER_LOGGER.warn("Message queue thread interrupted: " + ex);
                    }
                }
                RUNNER_LOGGER.debug("CIDATwitterMessageRunner done checking queue");
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
                RUNNER_LOGGER.debug("CIDATwitterEventRunner checking queue");
                while (!eventQueue.isEmpty()) {
                    try {
                        Event event = eventQueue.take();
                        getEventBus().post(event);
                    } catch (InterruptedException ex) {
                        RUNNER_LOGGER.warn("Event queue thread interrupted: " + ex);
                    }
                }
                RUNNER_LOGGER.debug("CIDATwitterEventRunner done checking queue");
            }
        }
    }
    
    /**
     * Create a Twitter client using OAuth
     * @param consumerKey Twitter API key
     * @param consumerSecret Twitter API secret
     * @param token Twitter Access token
     * @param secret Twitter Access token secret
     * @param userIds List of user IDs to track
     * @param terms List of terms IDs to track
     * @param locations List of locations IDs to track
     */
    public CIDATwitterClient(String consumerKey, String consumerSecret, String token, String secret, List<Long> userIds, List<String> terms, List<Location> locations) {
        if (client == null) {
            client = this.buildClient(new OAuth1(consumerKey, consumerSecret, token, secret));
            this.userIds = userIds;
            this.terms = terms;
            this.locations = locations;
            LOGGER.debug("New Twitter client created");
        }
    }
    
    /**
     * Create a Twitter client using basic login (using this method is discouraged
     * in favor of OAuth)
     * @param username Twitter user name
     * @param password Twitter password
     * @param userIds List of user IDs to track
     * @param terms List of terms IDs to track
     * @param locations List of locations IDs to track
     */
    public CIDATwitterClient(String username, String password, List<Long> userIds, List<String> terms, List<Location> locations) {
        if (client == null) {
            client = this.buildClient(new BasicAuth(username, password));
            this.userIds = userIds;
            this.terms = terms;
            this.locations = locations;
            LOGGER.debug("New Twitter client created");
        }
    }
    
    /**
     * Connects the Twitter client to Twitter
     */
    public synchronized void connect() {
        if (!isConnected) {
            client.connect();
            isConnected = true;
            LOGGER.info("Twitter client connecting...");
        }
    }
    
    /**
     * Starts the message queuing for the Twitter client
     */
    public void startMessageQueueing() {
        if (null == messageFuture || messageFuture.isDone() || messageFuture.isCancelled()) {
            messageSes.scheduleAtFixedRate(new CIDATwitterMessageRunner(), 0, 1, TimeUnit.MINUTES);
            LOGGER.info("Message queueing started");
        }
    }
    
    /**
     * Stops the message queuing for the Twitter client
     */
    public void stopMessageQueueing() {
        if (null != messageFuture && !messageFuture.isDone() && !messageFuture.isCancelled()) {
           messageFuture.cancel(false);
           LOGGER.info("Message queueing stopped");
        }
    }
    
    /**
     * Starts the event queuing for the Twitter client
     */
    public void startEventQueueing() {
        if (null == eventFuture || eventFuture.isDone() || eventFuture.isCancelled()) {
            eventSes.scheduleAtFixedRate(new CIDATwitterEventRunner(), 0, 1, TimeUnit.MINUTES);
            LOGGER.info("Event queueing started");
        }
    }
    
    /** 
     * Stops the event queuing for the Twitter client
     */
    public void stopEventQueueing() {
        if (null != eventFuture && !eventFuture.isDone() && !eventFuture.isCancelled()) {
            eventFuture.cancel(false);
            LOGGER.info("Event queueing stopped");
        }
    }

    public synchronized void stop(Integer waitMillis) {
        LOGGER.info("Twitter client stopping");
        this.stopEventQueueing();
        this.stopMessageQueueing();
        if (client != null) {
            client.stop(waitMillis);
            client = null;
        }
        LOGGER.info("Twitter client stopped");
    }
    
    private Client buildClient(Authentication auth) {
        messageQueue = new LinkedBlockingQueue<>(100000);
        eventQueue = new LinkedBlockingQueue<>(1000);
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        
        if (this.userIds != null) {
            endpoint.followings(this.userIds);
        }
        
        if (this.terms != null) {
            endpoint.trackTerms(this.terms);
        }
        
        if (this.locations != null) {
            endpoint.locations(this.locations);
        }
        
        ClientBuilder cb = new ClientBuilder().
                name("CIDA-Twitter-Client").
                hosts(Constants.SITESTREAM_HOST).
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
