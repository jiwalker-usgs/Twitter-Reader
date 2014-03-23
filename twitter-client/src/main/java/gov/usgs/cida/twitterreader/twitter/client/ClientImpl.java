package gov.usgs.cida.twitterreader.twitter.client;

import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitterreader.commons.queue.QueueParams;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitterreader.commons.observer.interfaces.IClientObserver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author isuftin
 */
public class ClientImpl implements IClient {

    private final TwitterClient client;
    private final List<IClientObserver> observers = new ArrayList<>();
    private List<Long> userIds;
    private List<String> terms;
    private List<Location> locations;
    private QueueParams eventQueueParams = new QueueParams(0l, 0l, TimeUnit.MINUTES);
    private QueueParams messageQueueParams = new QueueParams(0l, 0l, TimeUnit.MINUTES);

    protected ClientImpl(TwitterClient client) {
        this.client = client;
    }

    @Override
    public List<Long> getUserIds() {
        return Collections.unmodifiableList(userIds);
    }

    @Override
    public void addUserId(Long userId) {
        if (!this.userIds.contains(userId)) {
            this.userIds.add(userId);
        }
    }

    @Override
    public List<String> getTerms() {
        return Collections.unmodifiableList(terms);
    }

    @Override
    public void addTerm(String term) {
        if (!this.terms.contains(term)) {
            this.terms.add(term);
        }
    }

    @Override
    public List<Location> getLocations() {
        return Collections.unmodifiableList(locations);
    }

    @Override
    public void addLocation(Location location) {
        if (!this.locations.contains(location)) {
            this.locations.add(location);
        }
    }

    @Override
    public void start() {
        client.start();
    }

    @Override
    public void start(Boolean autoStartQueue) {
        client.start(autoStartQueue);
    }

    @Override
    public void stop(Integer waitMillis) {
        client.stop(waitMillis);
    }

    @Override
    public List<IClientObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }

    @Override
    public IClientObserver addObserver(IClientObserver observer) {
        if (observer != null) {
            this.observers.add(observer);
        }
        return observer;
    }

    @Override
    public void registerAllObservers() {
        for (IClientObserver observer : observers) {
            observer.register();
        }
    }

    @Override
    public void unregisterAllObservers() {
        for (IClientObserver observer : observers) {
            observer.unregister();
        }
    }

    @Override
    public void setEventQueueParams(QueueParams eventQueueParams) {
        this.eventQueueParams = eventQueueParams;
    }

    @Override
    public void setMessageQueueParams(QueueParams messageQueueParams) {
        this.messageQueueParams = messageQueueParams;
    }

    /**
     * @return the eventQueueParams
     */
    public QueueParams getEventQueueParams() {
        return eventQueueParams;
    }

    /**
     * @return the messageQueueParams
     */
    public QueueParams getMessageQueueParams() {
        return messageQueueParams;
    }
}
