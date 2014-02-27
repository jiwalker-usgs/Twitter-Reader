package gov.usgs.cida.twitter.reader.data.observer.impl;

import com.google.common.eventbus.Subscribe;
import com.twitter.hbc.core.event.Event;

/**
 * Sets an interface for handling Events
 *
 * @author isuftin
 */
public abstract class EventObserver {

    /**
     * Handles a new incoming event
     *
     * @param event
     */
    @Subscribe
    public abstract void handleEvent(Event event);

    /**
     * Register this object to the event bus
     */
    public abstract void register();

    /**
     * Unregister this object from the event bus
     */
    public abstract void unregister();

}
