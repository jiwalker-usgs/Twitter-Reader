package gov.usgs.cida.twitter.data.observer;

import com.google.common.eventbus.Subscribe;
import com.twitter.hbc.core.event.Event;
import gov.usgs.cida.twitter.data.dao.TwitterEventDAO;
import gov.usgs.cida.twitter.data.model.TwitterEvent;
import gov.usgs.cida.twitter.data.model.TwitterEventType;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitterreader.commons.queue.TwitterQueues;
import gov.usgs.cida.twitterreader.commons.observer.EventObserver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs events to the database
 *
 * @author isuftin
 */
public class DatabaseEventObserver extends EventObserver {

    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseEventObserver.class);
    private SqlSessionFactory sessionFactory = null;

    public DatabaseEventObserver() {
    }

    public DatabaseEventObserver(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Subscribe
    @Override
    public void handleEvent(Event event) {
        TwitterEventType eventType = new TwitterEventType(event.getEventType());
        TwitterEvent twitterEvent = new TwitterEvent(eventType, event.getMessage());
        TwitterEventDAO dao;
        if (this.sessionFactory != null) {
            dao = new TwitterEventDAO(this.sessionFactory);
        } else {
            dao = new TwitterEventDAO();
        }

        dao.insertEvent(twitterEvent);
    }

    /**
     * @see ClientObserver#register()
     */
    @Override
    public void register() {
        TwitterQueues.registerObserver(this);
    }

    /**
     * @see ClientObserver#unregister()
     */
    @Override
    public void unregister() {
        TwitterQueues.unregisterObserver(this);
    }

}
