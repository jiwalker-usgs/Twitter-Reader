package gov.usgs.cida.twitterreader.commons.observer;

import com.twitter.hbc.core.event.Event;

/**
 * Sets an interface for handling Messages
 *
 * @author isuftin
 */
public abstract class MessageObserver extends ClientObserver {

    @Override
    public final void handleEvent(Event message) {
        throw new IllegalArgumentException("MessageObserver does not handle Event objects");
    }

    /**
     * Handles a new incoming message
     *
     * @param message
     */
    @Override
    public abstract void handleEvent(String message);

}
