package gov.usgs.cida.twitterreader.twitter.client;

import gov.usgs.cida.twitter.reader.data.client.ClientContext;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitter.reader.data.client.TwitterClientBuilder;

/**
 *
 * @author isuftin
 */
public abstract class AbstractAuthTypeClientBuilder implements IAuthTypeClientBuilder {

    private ClientContext context;

    @Override
    public TwitterClient buildTwitterClient() {
        TwitterClientBuilder tcb = new TwitterClientBuilder().setContext(getContext());
        return tcb.build();
    }

    /**
     * @return the context
     */
    public ClientContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(ClientContext context) {
        this.context = context;
    }

}
