package gov.usgs.cida.twitter.reader.data.observer.impl;

import com.google.common.eventbus.Subscribe;
import com.twitter.hbc.core.event.Event;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a message observer that receives and logs messages
 *
 * @author isuftin
 */
public class LoggingEventObserver extends EventObserver {

    private Logger logger;
    
    public LoggingEventObserver() {
        logger = LoggerFactory.getLogger(LoggingEventObserver.class);
    }
    
    public LoggingEventObserver(Logger logger) {
        this.logger = logger;
    }
    
    /**
     * @see EventObserver#register() 
     */
    @Override
    public void register() {
        TwitterClient.getEventBus().register(this);
        logger.info("LoggingEventObserver registered");
    }
    
    /**
     * @see EventObserver#unregister() 
     */
    @Override
    public void unregister() {
        TwitterClient.getEventBus().unregister(this);
        logger.info("LoggingEventObserver unregistered");
    }
    
    /**
     * @see EventObserver#handleIncomingMessage(com.twitter.hbc.core.event.Event) 
     * @param event 
     */
    @Subscribe
    @Override
    public void handleEvent(Event event) {
        logger.info("New incoming Twitter event: " + event.getMessage());
    }
    
}
