package gov.usgs.cida.twitterreader.main;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitterreader.twitter.client.ClientLauncher;
import java.io.IOException;
import java.util.Arrays;
import org.kohsuke.args4j.CmdLineException;

/**
 * Entry point to the Twitter Client application
 *
 * @author isuftin
 */
public class ClientMain {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(ClientMain.class);
    private static TwitterClient client;
    private static String[] incomingArgs;
    private volatile boolean running = true;

    /**
     * Launches a TwitterClient instance using commandline-provided flags
     *
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        incomingArgs = Arrays.copyOf(args, args.length);
        new ClientMain().run();
    }

    /**
     * Waits for ctrl-c to exit out of console
     *
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {

        try {
            client = new ClientLauncher().buildClient(incomingArgs);

            if (client != null) {
                Runtime.getRuntime().addShutdownHook(new ShutdownHook());
                
                client.start();
                
                while (running && !TwitterClient.isStopped()) {
                    Thread.sleep(1000);
                }
                
                logger.info("Twitter Client has shut down cleanly...");
            }

        } catch (IllegalArgumentException | CmdLineException | IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

    private class ShutdownHook extends Thread {

        @Override
        public void run() {
            logger.info("Twitter Client shutting down...");
            running = false;
            client.stop(5000);
        }
    }

}
