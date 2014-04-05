package gov.usgs.cida.twitter.reader.data.client;

import ch.qos.logback.classic.Logger;
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.httpclient.auth.Authentication;
import gov.usgs.cida.twitterreader.commons.queue.QueueParams;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Represents the context used in building the client
 *
 * @author isuftin
 */
public class TwitterClientContext {

    private Authentication auth = null;
    private List<Long> userIds = new ArrayList<>();
    private List<String> terms = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();
    private QueueParams eventQueueParams = new QueueParams(0l, 0l, TimeUnit.MINUTES);
    private QueueParams messageQueueParams = new QueueParams(0l, 0l, TimeUnit.MINUTES);
    private Logger logger;

    private TwitterClientContext() {
    }

    public TwitterClientContext(Authentication auth) {
        if (auth == null) {
            throw new IllegalArgumentException("Authentication object not provided");
        }
        this.auth = auth;
    }

    Authentication getAuth() {
        return auth;
    }

    /**
     * @return the userIds
     */
    public List<Long> getUserIds() {
        return new ArrayList<>(userIds);
    }

    /**
     * @param userIds the userIds to set
     */
    public void setUserIds(List<Long> userIds) {
        if (userIds != null) {
            this.userIds = new ArrayList<>(userIds);
        }
    }

    /**
     * @return the terms
     */
    public List<String> getTerms() {
        return new ArrayList<>(terms);
    }

    /**
     * @param terms the terms to set
     */
    public void setTerms(List<String> terms) {
        if (terms != null) {
            this.terms = new ArrayList<>(terms);
        }
    }

    /**
     * @return the locations
     */
    public List<Location> getLocations() {
        return new ArrayList<>(locations);
    }

    /**
     * @param locations the locations to set
     */
    public void setLocations(List<Location> locations) {
        if (locations != null) {
            this.locations = new ArrayList<>(locations);
        }
    }

    /**
     * @return the eventQueueParams
     */
    public QueueParams getEventQueueParams() {
        return eventQueueParams;
    }

    /**
     * @param eventQueueParams the eventQueueParams to set
     */
    public void setEventQueueParams(QueueParams eventQueueParams) {
        this.eventQueueParams = eventQueueParams;
    }

    /**
     * @return the messageQueueParams
     */
    public QueueParams getMessageQueueParams() {
        return messageQueueParams;
    }

    /**
     * @param messageQueueParams the messageQueueParams to set
     */
    public void setMessageQueueParams(QueueParams messageQueueParams) {
        this.messageQueueParams = messageQueueParams;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }
    
    boolean isEmpty() {
        int population = 0;
        boolean empty;

        population += userIds.size();
        population += terms.size();
        population += locations.size();

        empty = population == 0;

        return empty;
    }


}
