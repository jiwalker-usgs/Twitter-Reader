package gov.usgs.cida.twitterreader.twitter.client;

import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitterreader.commons.queue.QueueParams;
import gov.usgs.cida.twitterreader.commons.observer.interfaces.IClientObserver;
import java.util.List;

/**
 * Base client interface
 *
 * @author isuftin
 */
public interface IClient {

    public List<Long> getUserIds();

    public void addUserId(Long userId);

    public List<String> getTerms();

    public void addTerm(String term);

    public List<Location> getLocations();

    public void addLocation(Location location);

    /**
     * @return known observers
     */
    public List<IClientObserver> getObservers();

    /**
     * Adds the observer to the list of internal observers. Registration is left
     * to upstream objects
     *
     * @param observer observer to listen
     * @return added observer
     */
    public IClientObserver addObserver(IClientObserver observer);

    /**
     * Connects the Twitter client to Twitter
     */
    public void start();

    /**
     * Connects the Twitter client to Twitter with the option to automatically
     * start queueing
     *
     * @param autoStartQueue automatically begin queueing when connected
     */
    public void start(Boolean autoStartQueue);

    /**
     * Disconnect client and ready it for disposal. Also stops all queued jobs
     *
     * @param waitMillis milliseconds to wait for the client to stop
     */
    public void stop(Integer waitMillis);

    /**
     * Registers all available observers to listen to TwitterClient
     */
    public void registerAllObservers();

    /**
     * Unregisters all available observers from listening to TwitterClient
     */
    public void unregisterAllObservers();

    /**
     * @param messageQueueParams the messageQueueParams to set
     */
    public void setMessageQueueParams(QueueParams messageQueueParams);

    /**
     * @param eventQueueParams the eventQueueParams to set
     */
    public void setEventQueueParams(QueueParams eventQueueParams);
}
