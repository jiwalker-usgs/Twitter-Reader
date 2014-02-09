package gov.usgs.cida.twitter.reader.data.observer.impl;

import com.google.common.eventbus.Subscribe;
import gov.usgs.cida.twitter.reader.data.access.CIDATwitterClient;
import gov.usgs.cida.twitter.reader.data.observer.interfaces.MessageObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a message observer that receives and logs messages
 *
 * @author isuftin
 */
public class LoggingMessageObserver implements MessageObserver {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingMessageObserver.class);
    
    /**
     * @see MessageObserver#register() 
     */
    @Override
    public void register() {
        CIDATwitterClient.getEventBus().register(this);
    }
    
    /**
     * @see MessageObserver#unregister() 
     */
    @Override
    public void unregister() {
        CIDATwitterClient.getEventBus().unregister(this);
    }
    
    /**
     * Logs incoming Twitter messages
     * @see MessageObserver#handleIncomingMessage(java.lang.String) 
     * @param message 
     */
    @Subscribe
    @Override
    public void handleIncomingMessage(String message) {
        LOGGER.info("Incoming Message: " + message);
    }
    
}
