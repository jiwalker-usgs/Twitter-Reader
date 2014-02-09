package gov.usgs.cida.twitter.reader.data.observer.impl;

import com.google.common.eventbus.Subscribe;
import com.twitter.hbc.core.event.Event;
import gov.usgs.cida.twitter.reader.data.access.TwitterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a message observer that receives and logs messages
 *
 * @author isuftin
 */
public class LoggingEventObserver extends EventObserver {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingEventObserver.class);
    
    /**
     * @see EventObserver#register() 
     */
    @Override
    public void register() {
        TwitterClient.getEventBus().register(this);
        LOGGER.info("LoggingEventObserver registered");
    }
    
    /**
     * @see EventObserver#unregister() 
     */
    @Override
    public void unregister() {
        TwitterClient.getEventBus().unregister(this);
        LOGGER.info("LoggingEventObserver unregistered");
    }
    
    /**
     * @see EventObserver#handleIncomingMessage(com.twitter.hbc.core.event.Event) 
     * @param event 
     */
    @Subscribe
    @Override
    public void handleEvent(Event event) {
        LOGGER.info("New incoming Twitter event: " + event.getMessage());
    }
    
}
