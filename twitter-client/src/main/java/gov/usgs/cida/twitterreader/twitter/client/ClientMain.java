package gov.usgs.cida.twitterreader.twitter.client;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Entry point to the Twitter Client application
 *
 * @author isuftin
 */
public class ClientMain {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        new ClientLauncher().run(args);
    }
}
