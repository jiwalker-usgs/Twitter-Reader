package gov.usgs.cida.twitterreader.twitter.client;

import gov.usgs.cida.twitter.reader.data.client.TwitterClient;

/**
 * A singleton Twitter client
 *
 * @author isuftin
 */
public class TwitterClientSingleton {

    private static TwitterClient twitterClient = null;

    private TwitterClientSingleton() {
    }

    final static TwitterClient getTwitterClient(IAuthTypeClientBuilder builder) {
        if (twitterClient == null) {
            twitterClient = builder.buildTwitterClient();
        }

        return twitterClient;
    }
}
