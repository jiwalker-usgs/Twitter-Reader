package gov.usgs.cida.twitter.reader.data.observer.impl;

import com.google.common.eventbus.Subscribe;
import com.twitter.hbc.core.event.Event;
import gov.usgs.cida.twitter.reader.data.observer.interfaces.IClientObserver;

/**
 * Sets an interface for handling Events
 *
 * @author isuftin
 */
public abstract class EventObserver implements IClientObserver {

    /**
     * Handles a new incoming event
     *
     * @param event
     */
    @Subscribe
    @Override
    public abstract void handleEvent(Event event);

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
