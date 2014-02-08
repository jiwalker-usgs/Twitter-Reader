package gov.usgs.cida;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.BasicAuth;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A client to asynchronously pull messages from CIDA's Twitter queue
 * 
 * @author isuftin
 */
public class CIDATwitterClient {
    private Client client = null;
    private Boolean isConnected = false;
    
    /**
     * Create a Twitter client using OAuth
     * @param consumerKey
     * @param consumerSecret
     * @param token
     * @param secret 
     */
    public CIDATwitterClient(String consumerKey, String consumerSecret, String token, String secret) {
        this.client = this.buildClient(new OAuth1(consumerKey, consumerSecret, token, secret));
    }
    
    public CIDATwitterClient(String username, String password) {
        this.client = this.buildClient(new BasicAuth(username, password));
    }
    
    public void start() {
        if (!this.isConnected) {
            this.client.connect();
            this.isConnected = true;
        }
    }

    public void stop(Integer waitMillis) {
        this.getClient().stop(waitMillis);
    }
    
    private Client buildClient(Authentication auth) {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(100000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(1000);
        Hosts cidaHosts = new HttpHosts(Constants.STREAM_HOST);
        StreamingEndpoint endpoint = new StatusesFilterEndpoint();
        ClientBuilder cb = new ClientBuilder().
                name("CIDA-Twitter-Client").
                hosts(cidaHosts).
                authentication(auth).
                endpoint(endpoint).
                processor(new StringDelimitedProcessor(msgQueue)).
                eventMessageQueue(eventQueue);
        return cb.build();
    }
    
    /**
     * During finalize, shuts down the Twitter client
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            if (null != this.getClient()) {
                this.stop(10000);
            }
        } finally {
            super.finalize();
        }
    }

    public Client getClient() {
        return client;
    }
}
