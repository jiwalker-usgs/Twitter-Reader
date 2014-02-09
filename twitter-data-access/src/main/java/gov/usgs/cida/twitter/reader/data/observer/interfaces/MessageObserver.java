package gov.usgs.cida.twitter.reader.data.observer.interfaces;

import com.google.common.eventbus.Subscribe;

/**
 * Sets an interface for handling Messages
 *
 * @author isuftin
 */
public interface MessageObserver {

    /**
     * Handles a new incoming message
     *
     * @param message
     */
    @Subscribe
    public void handleIncomingMessage(String message);

    /**
     * Register this object to the event bus
     */
    public void register();

    /**
     * Unregister this object from the event bus
     */
    public void unregister();

}
