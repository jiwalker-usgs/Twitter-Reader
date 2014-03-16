package gov.usgs.cida.twitterreader.twitter.client;

import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitter.reader.data.client.QueueParams;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitter.reader.data.observer.interfaces.IClientObserver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author isuftin
 */
public class ClientImpl implements IClient {

    private TwitterClient client;
    private List<Long> userIds;
    private List<String> terms;
    private List<Location> locations;
    private List<IClientObserver> observers = new ArrayList<>();
    private QueueParams eventQueueParams = null;
    private QueueParams messageQueueParams = null;

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
    public void connect() {
        client.connect();
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
}
