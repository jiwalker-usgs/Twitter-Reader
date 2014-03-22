package gov.usgs.cida.twitterreader.commons.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.slf4j.LoggerFactory;

/**
 * Used to programmatically create ch.qos.logback.classic.Logger instances
 *
 * @author isuftin
 */
public class TwitterLoggerFactory {

    private final TwitterLoggerContext twitterLoggerContext;
    private final Class<?> loggerClass;
    private final String loggerName;

    public TwitterLoggerFactory(TwitterLoggerContext context) {
        if (context == null) {
            throw new NullPointerException("LoggerContext can not be null");
        }

        this.twitterLoggerContext = context;
        this.loggerClass = twitterLoggerContext.getLoggerClass();
        this.loggerName = twitterLoggerContext.getLoggerName();
    }

    public Logger createLogger() {
        Logger result;

        if (this.loggerClass != null) {
            result = (Logger) LoggerFactory.getLogger(this.loggerClass);
        } else {
            result = (Logger) LoggerFactory.getLogger(this.loggerName);
        }

        Appender<ILoggingEvent> appender;
        appender = new TwitterAppenderFactory(twitterLoggerContext).createAppender();
        result.addAppender(appender);

        return result;
    }
}
