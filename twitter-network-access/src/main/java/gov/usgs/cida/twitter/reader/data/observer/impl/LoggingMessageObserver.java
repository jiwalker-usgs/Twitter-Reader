package gov.usgs.cida.twitter.reader.data.observer.impl;

import com.google.common.eventbus.Subscribe;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a message observer that receives and logs messages
 *
 * @author isuftin
 */
public class LoggingMessageObserver extends MessageObserver {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingMessageObserver.class);
    
    /**
     * @see MessageObserver#register() 
     */
    @Override
    public void register() {
        TwitterClient.getEventBus().register(this);
        LOGGER.info("LoggingMessageObserver registered");
    }
    
    /**
     * @see MessageObserver#unregister() 
     */
    @Override
    public void unregister() {
        TwitterClient.getEventBus().unregister(this);
        LOGGER.info("LoggingMessageObserver unregistered");
    }
    
    /**
     * Logs incoming Twitter messages
     * @see MessageObserver#handleIncomingMessage(java.lang.String) 
     * @param message 
     */
    @Subscribe
    @Override
    public void handleEvent(String message) {
        LOGGER.info("Incoming Message: " + message);
    }
    
}
