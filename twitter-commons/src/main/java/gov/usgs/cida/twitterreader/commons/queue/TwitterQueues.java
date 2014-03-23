package gov.usgs.cida.twitterreader.commons.queue;

import ch.qos.logback.classic.Logger;
import com.google.common.eventbus.EventBus;
import com.twitter.hbc.core.event.Event;
import gov.usgs.cida.twitterreader.commons.observer.ClientObserver;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isuftin
 */
public class TwitterQueues {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(TwitterQueues.class);
    private final static EventBus eventBus;
    private final static BlockingQueue<String> messageQueue;
    private final static BlockingQueue<Event> eventQueue;
    private final static ScheduledExecutorService eventSes;
    private final static ScheduledExecutorService messageSes;
    private static ScheduledFuture<TwitterEventRunner> eventFuture = null;
    private static ScheduledFuture<TwitterMessageRunner> messageFuture = null;
    private QueueParams eventQueueParams = new QueueParams(0l, 1l, TimeUnit.MINUTES);
    private QueueParams messageQueueParams = new QueueParams(0l, 1l, TimeUnit.MINUTES);

    static {
        messageQueue = new LinkedBlockingQueue<>(100000);
        eventQueue = new LinkedBlockingQueue<>(1000);
        eventSes = Executors.newScheduledThreadPool(1);
        messageSes = Executors.newScheduledThreadPool(1);
        eventBus = new EventBus("Twitter-Eventbus");
    }

    /**
     * Starts both event and message queueing
     */
    public void startQueueing() {
        startMessageQueueing(messageQueueParams);
        startEventQueueing(eventQueueParams);
    }

    /**
     * Stops both message and event queueing
     */
    public void stopQueueing() {
        TwitterQueues.stopEventQueueing();
        TwitterQueues.stopMessageQueueing();
    }

    /**
     * Starts message queuing for the Twitter client using default start time
     * and run time
     */
    public void startMessageQueueing() {
        startMessageQueueing(messageQueueParams);
    }

    /**
     * Starts the event queuing for the Twitter client using default start time
     * and run time
     */
    public void startEventQueueing() {
        startEventQueueing(eventQueueParams);
    }

    /**
     * Stops the message queuing for the Twitter client
     */
    public static void stopMessageQueueing() {
        if (null != messageFuture && !messageFuture.isDone() && !messageFuture.isCancelled()) {
            messageFuture.cancel(false);
            logger.info("Message queueing stopped");
        }
    }

    /**
     * Stops the event queuing for the Twitter client
     */
    public static void stopEventQueueing() {
        if (null != eventFuture && !eventFuture.isDone() && !eventFuture.isCancelled()) {
            eventFuture.cancel(false);
            logger.info("Event queueing stopped");
        }
    }

    /**
     * Starts message queuing for the Twitter client
     *
     * @param initialDelay the time to delay first execution
     * @param period the period between successive executions
     * @param timeUnit the time unit of the initialDelay and period parameters
     */
    public void startMessageQueueing(QueueParams params) {
        Long initialDelay = params.getInitialDelay();
        Long period = params.getPeriod();
        TimeUnit timeUnit = params.getTimeUnit();
        if (null == messageFuture || messageFuture.isDone() || messageFuture.isCancelled()) {
            messageFuture = (ScheduledFuture<TwitterMessageRunner>) messageSes.scheduleAtFixedRate(new TwitterMessageRunner(), initialDelay, period, timeUnit);
            logger.info("Message queueing started");
        }
    }

    /**
     * Starts event queuing for the Twitter client
     *
     * @param initialDelay the time to delay first execution
     * @param period the period between successive executions
     * @param timeUnit the time unit of the initialDelay and period parameters
     */
    public void startEventQueueing(QueueParams params) {
        Long initialDelay = params.getInitialDelay();
        Long period = params.getPeriod();
        TimeUnit timeUnit = params.getTimeUnit();
        if (null == eventFuture || eventFuture.isDone() || eventFuture.isCancelled()) {
            eventFuture = (ScheduledFuture<TwitterEventRunner>) eventSes.scheduleAtFixedRate(new TwitterEventRunner(), initialDelay, period, timeUnit);
            logger.info("Event queueing started");
        }
    }

    /**
     * Thread runnable object that checks the message queue and posts new
     * messages to observers
     */
    private static class TwitterMessageRunner implements Runnable {

        private final static Logger RUNNER_LOGGER = (Logger) LoggerFactory.getLogger(TwitterMessageRunner.class);

        @Override
        public void run() {
            RUNNER_LOGGER.debug("TwitterMessageRunner checking queue");
            while (!messageQueue.isEmpty()) {
                try {
                    String message = messageQueue.take();
                    eventBus.post(message);
                } catch (InterruptedException ex) {
                    RUNNER_LOGGER.warn("Message queue thread interrupted: " + ex);
                }
            }
            RUNNER_LOGGER.debug("TwitterMessageRunner done checking queue");
        }
    }

    /**
     * Thread runnable object that checks the event queue and posts new events
     * to observers
     */
    private static class TwitterEventRunner implements Runnable {

        private final static Logger RUNNER_LOGGER = (Logger) LoggerFactory.getLogger(TwitterEventRunner.class);

        @Override
        public void run() {
            RUNNER_LOGGER.debug("TwitterEventRunner checking queue");
            while (!TwitterQueues.eventQueue.isEmpty()) {
                try {
                    Event event = TwitterQueues.eventQueue.take();
                    eventBus.post(event);
                } catch (InterruptedException ex) {
                    RUNNER_LOGGER.warn("Event queue thread interrupted: " + ex);
                }
            }
            RUNNER_LOGGER.debug("TwitterEventRunner done checking queue");
        }
    }

    public static BlockingQueue<String> getMessageQueue() {
        return TwitterQueues.messageQueue;
    }

    public static BlockingQueue<Event> getEventQueue() {
        return TwitterQueues.eventQueue;
    }

    /**
     * @param params
     */
    public void setEventQueueParams(QueueParams params) {
        eventQueueParams = params;
    }

    /**
     * @param params
     */
    public void setMessageQueueParams(QueueParams params) {
        messageQueueParams = params;
    }

    public static void registerObserver(ClientObserver observer) {
        // Try to unregister observer. If not registered, no biggie
        unregisterObserver(observer);
        eventBus.register(observer);
    }

    public static void unregisterObserver(ClientObserver observer) {
        try {
            eventBus.unregister(observer);
        } catch (IllegalArgumentException iae) {
            logger.debug("Attempted to unregister observer that was not registered. This is usually not a problem.");
        }
    }
}
