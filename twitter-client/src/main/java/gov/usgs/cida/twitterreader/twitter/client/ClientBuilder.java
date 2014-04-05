package gov.usgs.cida.twitterreader.twitter.client;

import ch.qos.logback.classic.Logger;
import com.twitter.hbc.core.endpoint.Location;
import gov.usgs.cida.twitter.reader.data.client.TwitterClientContext;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitter.reader.data.client.TwitterClientBuilder;
import gov.usgs.cida.twitter.reader.data.client.auth.IAuthType;
import gov.usgs.cida.twitterreader.commons.observer.interfaces.IClientObserver;
import gov.usgs.cida.twitterreader.commons.queue.QueueParams;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Builder class to help create clients
 *
 * @author isuftin
 */
public class ClientBuilder {
    private Logger logger;
    private IAuthType authType = null;
    private TwitterClient client;
    private QueueParams eventQueueParams = new QueueParams(0l, 0l, TimeUnit.MINUTES);
    private QueueParams messageQueueParams = new QueueParams(0l, 0l, TimeUnit.MINUTES);
    private final List<IClientObserver> clientObservers = new ArrayList<>();
    private List<Long> userIds;
    private List<String> terms;
    private List<Location> locations;

    private ClientBuilder() {
    }

    public ClientBuilder(IAuthType authType) {
        if (authType == null) {
            throw new NullPointerException("IAuthTypeClientBuilder cannot be null");
        }
        this.authType = authType;
    }

    public TwitterClient build() {
        if (client == null) {
            TwitterClientBuilder twitterClientBuilder = new TwitterClientBuilder();
            
            TwitterClientContext context = new TwitterClientContext(authType.createAuthentication());
            context.setUserIds(userIds);
            context.setTerms(terms);
            context.setLocations(locations);
            context.setEventQueueParams(this.eventQueueParams);
            context.setMessageQueueParams(this.messageQueueParams);
            context.setLogger(logger);
            
            twitterClientBuilder.setContext(context);
            
            client = twitterClientBuilder.build();
            
            for (IClientObserver clientObserver : clientObservers) {
                client.addObserver(clientObserver);
                clientObserver.register();
            }
        }
        return client;
    }

    public ClientBuilder addObserver(IClientObserver clientObserver) {
        if (clientObserver != null) {
            clientObservers.add(clientObserver);
        }
        return this;
    }

    public ClientBuilder setEventQueueParams(QueueParams params) {
        this.eventQueueParams = params;
        return this;
    }

    public ClientBuilder setMessageQueueParams(QueueParams params) {
        this.messageQueueParams = params;
        return this;
    }

    /**
     * @param userIds the userIds to set
     * @return 
     */
    public ClientBuilder setUserIds(List<Long> userIds) {
        this.userIds = new ArrayList<>(userIds);
        return this;
    }

    /**
     * @param terms the terms to set
     * @return 
     */
    public ClientBuilder setTerms(List<String> terms) {
        this.terms = new ArrayList<>(terms);
        return this;
    }

    /**
     * @param locations the locations to set
     * @return 
     */
    public ClientBuilder setLocations(List<Location> locations) {
        this.locations = new ArrayList<>(locations);
        return this;
    }

    public ClientBuilder setLogger(Logger logger) {
       this.logger = logger;
       return this;
    }
}
