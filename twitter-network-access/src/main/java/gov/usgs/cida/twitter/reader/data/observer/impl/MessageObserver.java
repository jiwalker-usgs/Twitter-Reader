package gov.usgs.cida.twitter.reader.data.observer.impl;

import com.google.common.eventbus.Subscribe;

/**
 * Sets an interface for handling Messages
 *
 * @author isuftin
 */
public abstract class MessageObserver {

    /**
     * Handles a new incoming message
     *
     * @param message
     */
    @Subscribe
    public abstract void handleEvent(String message);

    /**
     * Register this object to the event bus
     */
    public abstract void register();

    /**
     * Unregister this object from the event bus
     */
    public abstract void unregister();

}
