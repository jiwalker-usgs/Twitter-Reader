package gov.usgs.cida.twitterreader.twitter.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.Properties;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isuftin
 */
public class ClientLauncher {

    private static final Logger logger = LoggerFactory.getLogger(ClientLauncher.class);
    private static CmdLineParser parser;
    private File baseDirectory = null;
    private String propertiesFile = null;
    private File propertiesPath = null;
    private File logDirectory = null;
    private boolean useLoggers = false;
    private Properties properties = new Properties();

    @Option(name = "-d", usage = "Application Base Directory", metaVar = "String", required = true)
    public void setBaseDirectory(File file) {
        this.baseDirectory = file;
    }

    @Option(name = "-p", usage = "Properties File Name", metaVar = "String", depends = {"-d"})
    public void setPropertiesFile(String file) {
        this.propertiesFile = file;
    }

    @Option(name = "-loggers.use",
            usage = "Use file-based logging to log Twitter messages and events",
            metaVar = "Boolean")
    public void setUseLoggers(boolean use) {
        this.useLoggers = use;
    }

    @Option(name = "-h", usage = "Print this documentation", required = false, hidden = false)
    private boolean help;

    public void run(String... args) throws FileNotFoundException, IOException {
        parser = new CmdLineParser(this);
        parser.setUsageWidth(80);
        try {
            parser.parseArgument(args);

            // Check to make sure that the base directory exists
            if (!baseDirectory.exists()) {
                throw new FileNotFoundException(String.format("Directory at %s does not exist", baseDirectory.getAbsolutePath()));
            }
            
            if (propertiesFile != null) {
                processPropertiesFile();
            }
            
            if (useLoggers) {
                logDirectory = new File(baseDirectory, "logs");
                prepareLoggingDirectory();
            }

        } catch (CmdLineException | NullPointerException ex) {
            if (help) {
                printUsage(System.out);
            } else {
                if (ex instanceof NullPointerException) {
                    System.err.println("Null was passed into the launcher");
                } else {
                    System.err.println(ex.getMessage());
                }
                printUsage(System.err);
            }

        }
    }

    private void printUsage(PrintStream stream) {
        stream.println("java Client [options...] arguments...");
        parser.printUsage(stream);

        // print option sample. This is useful some time
        stream.println();
        stream.println(" Example: java Client" + parser.printExample(OptionHandlerFilter.REQUIRED));
    }

    private void processPropertiesFile() throws FileNotFoundException, IOException {
        propertiesPath = new File(baseDirectory, propertiesFile);
        if (!propertiesPath.exists()) {
            throw new FileNotFoundException(String.format("Properties file at %s does not exist", baseDirectory.getAbsolutePath()));
        }
        properties.load(new FileInputStream(propertiesPath));
        logger.debug(String.format("Properties file at %s has been loaded", propertiesPath.getPath()));

        useLoggers = Boolean.parseBoolean(properties.getProperty("loggers.use", "false"));
    }
    
    private void prepareLoggingDirectory() throws IOException {
        if (!baseDirectory.exists()) {
            Files.createDirectory(baseDirectory.toPath());
        }

        if (!logDirectory.exists()) {
            Files.createDirectory(logDirectory.toPath());
        }
    }
}
