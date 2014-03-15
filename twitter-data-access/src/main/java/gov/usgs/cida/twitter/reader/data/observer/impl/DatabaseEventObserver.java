package gov.usgs.cida.twitter.reader.data.observer.impl;

import com.google.common.eventbus.Subscribe;
import com.twitter.hbc.core.event.Event;
import gov.usgs.cida.twitter.data.dao.TwitterEventDAO;
import gov.usgs.cida.twitter.data.model.TwitterEvent;
import gov.usgs.cida.twitter.data.model.TwitterEventType;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs events to the database
 *
 * @author isuftin
 */
public class DatabaseEventObserver extends EventObserver {

    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseEventObserver.class);

    @Subscribe
    @Override
    public void handleEvent(Event event) {
        TwitterEventType eventType = new TwitterEventType(event.getEventType());
        TwitterEvent twitterEvent = new TwitterEvent(eventType, event.getMessage());
        TwitterEventDAO dao = new TwitterEventDAO();
        dao.insertEvent(twitterEvent);
    }

    @Override
    public void register() {
        TwitterClient.getEventBus().register(this);
        LOGGER.info("LoggingEventObserver registered");
    }

    @Override
    public void unregister() {
        TwitterClient.getEventBus().unregister(this);
        LOGGER.info("LoggingEventObserver unregistered");
    }

}
