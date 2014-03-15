package gov.usgs.cida.twitterreader.twitter.client;

import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitter.reader.data.observer.interfaces.IClientObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder class to help create clients
 * @author isuftin
 */
public class ClientBuilder {
    private TwitterClient twitterClient = null;
    private IAuthTypeClientBuilder builder = null;
    private IClient client;
    private List<IClientObserver> clientObservers = new ArrayList<>();
    
    public ClientBuilder() {}
    
    public ClientBuilder addConnector(IAuthTypeClientBuilder builder) {
        this.builder = builder;
        return this;
    }
    
    public ClientBuilder addClientObserver(IClientObserver clientObserver) {
        if (clientObserver != null) {
            clientObservers.add(clientObserver);
        }
        return this;
    }
    
    
    public IClient build() {
        if (builder != null && client == null) {
            twitterClient = builder.buildTwitterClient();
            client = new ClientImpl(twitterClient);
            for (IClientObserver clientObserver : clientObservers) {
                client.addObserver(clientObserver).register();
            }
        }
        return client;
    }
}
