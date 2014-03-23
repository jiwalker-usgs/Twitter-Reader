package gov.usgs.cida.twitterreader.commons.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import java.io.File;
import java.io.IOException;
import org.slf4j.LoggerFactory;

/**
 * Used to programmatically create Logback Appender instances
 *
 * @author isuftin
 */
public class TwitterAppenderFactory {

    private final TwitterLoggerContext twitterLoggerContext;
    private final LoggerType type;
    private final String loggerName;
    private final String encoderPattern;
    private final LoggerContext context;
    private final File outputDirectory;

    public TwitterAppenderFactory(TwitterLoggerContext context) throws IOException {
        if (context == null) {
            throw new NullPointerException("LoggerContext can not be null");
        }

        this.twitterLoggerContext = context;
        this.type = this.twitterLoggerContext.getLoggerType();
        this.loggerName = twitterLoggerContext.getLoggerName();
        this.encoderPattern = twitterLoggerContext.getEncoderPattern();
        this.outputDirectory = twitterLoggerContext.getOutputDirectory();

        if (twitterLoggerContext.getLoggerContext() == null) {
            this.context = new LoggerContext();
        } else {
            this.context = twitterLoggerContext.getLoggerContext();
        }
        
        if (this.type == LoggerType.FILE && !this.outputDirectory.exists()) {
            throw new IOException(String.format("Output directory %s does not exist", outputDirectory.getPath()));
        }
    }

    public Appender<ILoggingEvent> createAppender() {
        Appender<ILoggingEvent> result = null;
        if (this.type == LoggerType.CONSOLE) {
            result = createConsoleLogger();
        } else if (this.type == LoggerType.FILE) {
            result = createFileLogger();
        }
        return result;
    }

    private ConsoleAppender<ILoggingEvent> createConsoleLogger() {
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();

        PatternLayout layout = new PatternLayout();

        // Set console appender
        consoleAppender.setContext(this.context);
        consoleAppender.setName(String.format("%s-ConsoleAppender", this.loggerName));
        consoleAppender.setLayout(createPatternLayout());
        consoleAppender.setTarget("System.out");
        consoleAppender.setEncoder(null);
        consoleAppender.start();

        return consoleAppender;
    }

    private RollingFileAppender<ILoggingEvent> createFileLogger() {
        RollingFileAppender<ILoggingEvent> result = new RollingFileAppender<>();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        TimeBasedRollingPolicy<ILoggingEvent> rPolicy = new TimeBasedRollingPolicy<>();
        
        // Set the pattern layout encoder
        ple.setPattern(this.encoderPattern);
        ple.setContext(this.context);
        ple.start();

        rPolicy.setContext(this.context);
        rPolicy.setFileNamePattern(this.loggerName + ".%d{yyyy-MM-dd}.log.zip");
        rPolicy.setCleanHistoryOnStart(false);
        rPolicy.setParent(result);
        rPolicy.start();

        result.setRollingPolicy(rPolicy);
        result.setEncoder(ple);
        result.setName(String.format("%s-FileAppender", this.loggerName));
        result.setContext(this.context);
        result.setLayout(createPatternLayout());
        result.setFile(this.outputDirectory.getAbsolutePath() + File.separatorChar + rPolicy.getActiveFileName());
        result.start();

        return result;
    }
    
    private PatternLayout createPatternLayout() {
        PatternLayout layout = new PatternLayout();
        // Set encoder pattern
        layout.setPattern(this.encoderPattern);
        layout.setContext(this.context);
        layout.start();
        return layout;
    }

}
