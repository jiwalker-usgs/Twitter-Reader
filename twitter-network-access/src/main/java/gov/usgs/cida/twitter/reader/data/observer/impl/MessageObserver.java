package gov.usgs.cida.twitter.reader.data.observer.impl;

import com.google.common.eventbus.Subscribe;
import gov.usgs.cida.twitter.reader.data.observer.interfaces.IClientObserver;

/**
 * Sets an interface for handling Messages
 *
 * @author isuftin
 */
public abstract class MessageObserver implements IClientObserver {

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
    @Override
    public abstract void register();

    /**
     * Unregister this object from the event bus
     */
    @Override
    public abstract void unregister();

}
