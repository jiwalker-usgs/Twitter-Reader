package gov.usgs.cida.twitterreader.twitter.client;

import gov.usgs.cida.twitter.reader.data.client.QueueParams;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitter.reader.data.observer.interfaces.IClientObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceConfigurationError;

/**
 * Builder class to help create clients
 *
 * @author isuftin
 */
public class ClientBuilder {

    private TwitterClient twitterClient = null;
    private IAuthTypeClientBuilder builder = null;
    private IClient client;
    private QueueParams eventQueueParams = null;
    private QueueParams messageQueueParams = null;
    private final List<IClientObserver> clientObservers = new ArrayList<>();

    private ClientBuilder() {
    }

    public ClientBuilder(IAuthTypeClientBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("IAuthTypeClientBuilder cannot be null");
        }
        this.builder = builder;
    }
    
    public ClientBuilder setBuilder(IAuthTypeClientBuilder builder) {
        if (builder != null) {
            this.builder = builder;
        }
        
        return this;
    }

    public ClientBuilder addClientObserver(IClientObserver clientObserver) {
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

    public IClient build() {
        if (client == null) {
            twitterClient = builder.buildTwitterClient();
            client = new ClientImpl(twitterClient);
            for (IClientObserver clientObserver : clientObservers) {
                client.addObserver(clientObserver).register();
            }
            if (this.messageQueueParams != null) {
                client.setMessageQueueParams(this.messageQueueParams);
            }
            if (this.eventQueueParams != null) {
                client.setEventQueueParams(this.eventQueueParams);
            }
        }
        return client;
    }
}
