package gov.usgs.cida.twitterreader.main;

import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitterreader.twitter.client.ClientLauncher;
import java.io.FileNotFoundException;
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
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, CmdLineException {
        launcher = new ClientLauncher();
        client = launcher.buildClient(args);
    }
}
