package gov.usgs.cida.twitter.reader.data.observer.interfaces;

import com.google.common.eventbus.Subscribe;
import com.twitter.hbc.core.event.Event;

/**
 * Sets an interface for handling Events
 *
 * @author isuftin
 */
public interface EventObserver {

    /**
     * Handles a new incoming event
     *
     * @param event
     */
    @Subscribe
    public void handleIncomingMessage(Event event);

    /**
     * Register this object to the event bus
     */
    public void register();

    /**
     * Unregister this object from the event bus
     */
    public void unregister();

}
