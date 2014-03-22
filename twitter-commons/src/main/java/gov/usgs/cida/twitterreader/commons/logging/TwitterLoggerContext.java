package gov.usgs.cida.twitterreader.commons.logging;

import ch.qos.logback.classic.LoggerContext;
import java.io.File;

/**
 * Used to define the type and capabilities of Logger that LoggerFactory builds
 *
 * @author isuftin
 */
public class TwitterLoggerContext {

    private Class<?> loggerClass;
    private String loggerName;
    private LoggerType loggerType = LoggerType.CONSOLE;
    private LoggerLevel loggerLevel = LoggerLevel.INFO;
    private LoggerContext loggerContext = new LoggerContext();
    private String encoderPattern = "%d{HH:mm:ss.SSS} [%t] %5p %logger{20} - %m%n";

    // File output specific
    private boolean isRolling = true;
    private File outputDirectory = new File(System.getProperty("user.home", ""), "logs");

    private TwitterLoggerContext() {
        // Require a class or logger name to be passed in
    }

    public TwitterLoggerContext(String loggerName) {
        this.loggerName = loggerName;
    }

    public TwitterLoggerContext(Class<?> loggerClass) {
        this.loggerClass = loggerClass;
        this.loggerName = loggerClass.getName();
    }

    /**
     * @return the loggerClass
     */
    public Class<?> getLoggerClass() {
        return loggerClass;
    }

    /**
     * @return the loggerType
     */
    public LoggerType getLoggerType() {
        return loggerType;
    }

    /**
     * @param loggerType the loggerType to set
     */
    public void setLoggerType(LoggerType loggerType) {
        this.loggerType = loggerType;
    }

    /**
     * @return the loggerLevel
     */
    public LoggerLevel getLoggerLevel() {
        return loggerLevel;
    }

    /**
     * @param loggerLevel the loggerLevel to set
     */
    public void setLoggerLevel(LoggerLevel loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    /**
     * @return the loggerContext
     */
    public LoggerContext getLoggerContext() {
        return loggerContext;
    }

    /**
     * @param loggerContext the loggerContext to set
     */
    public void setLoggerContext(LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
    }

    /**
     * @return the outputDirectory
     */
    public File getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * @param outputDirectory the outputDirectory to set
     */
    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * @return the encoderPattern
     */
    public String getEncoderPattern() {
        return encoderPattern;
    }

    /**
     * @param encoderPattern the encoderPattern to set
     */
    public void setEncoderPattern(String encoderPattern) {
        this.encoderPattern = encoderPattern;
    }

    /**
     * @return the loggerName
     */
    public String getLoggerName() {
        return loggerName;
    }

    /**
     * @return the isRolling
     */
    public boolean isIsRolling() {
        return isRolling;
    }

    /**
     * @param isRolling the isRolling to set
     */
    public void setIsRolling(boolean isRolling) {
        this.isRolling = isRolling;
    }

}
