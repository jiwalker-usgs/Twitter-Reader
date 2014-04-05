package gov.usgs.cida.twitterreader.commons.observer;

import com.twitter.hbc.core.event.Event;
import gov.usgs.cida.twitterreader.commons.observer.interfaces.IClientObserver;

/**
 *
 * @author isuftin
 */
public abstract class ClientObserver implements IClientObserver {

    /**
     * Register this object to the event bus
     */
    @Override
    public abstract void register();

    /**
     * @see MessageObserver#unregister()
     */
    @Override
    public abstract void unregister();

    /**
     * Handle an incoming Event object
     *
     * @param eventObject
     */
    @Override
    public abstract void handleEvent(Event eventObject);

    /**
     * Handle an incoming Message
     *
     * @param messageObject
     */
    @Override
    public abstract void handleEvent(String messageObject);

}
