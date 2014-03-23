package gov.usgs.cida.twitterreader.commons.queue;

import java.util.concurrent.TimeUnit;

/**
 * Parameter object to configure queueing start
 *
 * @author isuftin
 */
public class QueueParams {

    private Long initialDelay;
    private Long period;
    private TimeUnit timeUnit;

    private QueueParams() {
    }

    /**
     * @param initialDelay the time to delay first execution
     * @param period the period between successive executions
     * @param timeUnit the time unit of the initialDelay and period parameters
     */
    public QueueParams(Long initialDelay, Long period, TimeUnit timeUnit) {
        this.initialDelay = initialDelay;
        this.period = period;
        this.timeUnit = timeUnit;
    }

    /**
     * @return the initialDelay
     */
    public Long getInitialDelay() {
        return initialDelay;
    }

    /**
     * @return the period
     */
    public Long getPeriod() {
        return period;
    }

    /**
     * @return the timeUnit
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
