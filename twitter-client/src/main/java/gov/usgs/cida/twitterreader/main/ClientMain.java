package gov.usgs.cida.twitterreader.main;

import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitterreader.twitter.client.ClientLauncher;
import java.io.IOException;
import org.kohsuke.args4j.CmdLineException;

/**
 * Entry point to the Twitter Client application
 *
 * @author isuftin
 */
public class ClientMain {

    private static ClientLauncher launcher;
    private static TwitterClient client;

    /**
     * Launches a TwitterClient instance using commandline-provided flags
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launcher = new ClientLauncher();
        try {
            client = launcher.buildClient(args);
            
            if (client != null) {
                client.start();
            }
            
        } catch (IllegalArgumentException | CmdLineException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
