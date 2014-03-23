package gov.usgs.cida.twitterreader.twitter.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import gov.usgs.cida.twitter.reader.data.client.TwitterClient;
import gov.usgs.cida.twitterreader.commons.logging.LoggerType;
import gov.usgs.cida.twitterreader.commons.logging.TwitterAppenderFactory;
import gov.usgs.cida.twitterreader.commons.logging.TwitterLoggerContext;
import gov.usgs.cida.twitterreader.commons.observer.LoggingEventObserver;
import gov.usgs.cida.twitterreader.commons.observer.LoggingMessageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isuftin
 */
public class ClientLauncher {

    private static final Logger packageLogger = (Logger) LoggerFactory.getLogger("gov.usgs.cida.twitterreader.twitter.client");
    private static Logger logger;
    private static CmdLineParser parser;
    private File baseDirectory = null;
    private String propertiesFiles = null;
    private File logDirectory = null;
    private final Properties properties = new Properties();
    private final LoggingEventObserver consoleEventLogger = new LoggingEventObserver();
    private final LoggingMessageObserver consoleMessageLogger = new LoggingMessageObserver();
    private static TwitterClient client;
    private String oauthConsumerKey;
    private String oauthConsumerSecret;
    private String oauthToken;
    private String oauthSecret;
    private String simpleUsername;
    private String simplePassword;
    private boolean oauth;
    private boolean simple;

    @Option(name = "-h",
            aliases = {"--help"},
            usage = "Print this documentation",
            required = false,
            hidden = false)
    private boolean help;

    @Option(name = "-d",
            aliases = {"--directory.base"},
            usage = "Application Base Directory (Required)",
            metaVar = "String",
            required = true)
    public void setBaseDirectory(File file) {
        this.baseDirectory = file;
    }

    @Option(name = "-p",
            aliases = {"--property.files"},
            usage = "One or more comma separated properties filenames. Minimally contains authentication credentials. (Required)",
            metaVar = "String",
            depends = {"-d"},
            required = true)
    public void setPropertiesFile(String files) {
        this.propertiesFiles = files;
    }

    @Option(name = "-l",
            aliases = {"--logging.use"},
            usage = "Use file-based logging to log Twitter messages and events (default: false)")
    private boolean useLoggers = false;

    @Option(name = "--debug",
            usage = "Enable debug logging. (default: false)")
    private boolean debugLogging = false;

    private void printUsage(PrintStream stream) {
        stream.println("java Client [options...] arguments...");
        stream.println("-------------------------------------");
        parser.printUsage(stream);
    }

    public void run(String... args) throws FileNotFoundException, IOException, CmdLineException {
        // Process the flags coming in on the command line 
        processCommandLine(args);

        if (client == null || TwitterClient.isStopped()) {
            consoleEventLogger.register();
            consoleMessageLogger.register();
        }

    }

    private void processCommandLine(String[] args) throws FileNotFoundException, IOException, CmdLineException {
        parser = new CmdLineParser(this);
        parser.setUsageWidth(80);
        try {
            parser.parseArgument(args);

            // Check to make sure that the base directory exists
            if (!baseDirectory.exists()) {
                throw new FileNotFoundException(String.format("Directory at %s does not exist", baseDirectory.getAbsolutePath()));
            }
            
            logger = (Logger) LoggerFactory.getLogger(ClientLauncher.class);

            // If a properties file exists, use that
            if (propertiesFiles != null) {
                processPropertiesFile();
            }
            
            // Set logging level and appenders for the package and instantiate 
            // the logger for this class
            Level logLevel = debugLogging ? Level.DEBUG : Level.INFO;
            packageLogger.setLevel(logLevel);
            logger.info("Logger Set To {}", logger.getEffectiveLevel());

            // If I am using file based logging, set up logging for files
            if (useLoggers) {
                logDirectory = new File(baseDirectory, "logs");
                prepareLoggingDirectory();

                // We are using loggers so I need to create a file logger for 
                // this application, get its appender and attach it to the current logger
                // so all logs will go to console and file
                TwitterLoggerContext tlc = new TwitterLoggerContext(logger.getName());
                tlc.setOutputDirectory(logDirectory);
                tlc.setLoggerType(LoggerType.FILE);

                logger.addAppender(new TwitterAppenderFactory(tlc).createAppender());
                logger.debug("File appender added");
            }
        } catch (CmdLineException ex) {
            // User may not have passed in any arguments
            boolean missingArgmentList = "no argument is allowed:".equals(ex.getMessage().trim().toLowerCase());

            if (help || missingArgmentList) {
                printUsage(System.out);
            } else {
                throw ex;
            }
        }
    }

    private void processPropertiesFile() throws FileNotFoundException, IOException {
        String[] propertiesFilesArr = this.propertiesFiles.split(",");
        for (String prtopertyFile : propertiesFilesArr) {
            File propertiesPath = new File(baseDirectory, prtopertyFile);
            if (!propertiesPath.exists()) {
                throw new FileNotFoundException(String.format("Properties file at %s does not exist", propertiesPath.getAbsolutePath()));
            }
            this.properties.load(new FileInputStream(propertiesPath));
            logger.info(String.format("Properties file at %s has been loaded", propertiesPath.getPath()));
        }

        this.oauthConsumerKey = properties.getProperty("auth.oauth.consumerkey");
        this.oauthConsumerSecret = properties.getProperty("auth.oauth.consumersecret");
        this.oauthToken = properties.getProperty("auth.oauth.token");
        this.oauthSecret = properties.getProperty("auth.oauth.secret");
        this.simpleUsername = properties.getProperty("auth.simple.username");
        this.simplePassword = properties.getProperty("auth.simple.password");

        this.oauth = StringUtils.isNotBlank(this.oauthConsumerKey) && StringUtils.isNotBlank(this.oauthConsumerSecret)
                && StringUtils.isNotBlank(this.oauthToken) && StringUtils.isNotBlank(this.oauthSecret);
        this.simple = StringUtils.isNotBlank(this.simpleUsername) && StringUtils.isNotBlank(this.simplePassword);
        if (!oauth && !simple) {
            throw new IllegalArgumentException("Required: OAuth credentials or Simple Authentication credentials");
        }

        if (properties.containsKey("logging.use")) {
            useLoggers = true;
        }
        
        if (properties.containsKey("debugging")) {
            debugLogging = true;
        }
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
