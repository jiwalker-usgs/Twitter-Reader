package gov.usgs.cida.twitter.data.model;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author isuftin
 */
public class TwitterEvent implements Serializable {

    private static final long serialVersionUID = 5140178149793574673L;

    private BigDecimal eventId;
    private TwitterEventType eventType;
    private String eventMessage;
    private Timestamp timestamp;

    public TwitterEvent() {
    }

    public TwitterEvent(TwitterEventType eventType, String eventMessage) {
        this(eventType, eventMessage, new Timestamp(new Date().getTime()));
    }

    public TwitterEvent(TwitterEventType eventType, String eventMessage, Timestamp timestamp) {
        this.eventType = eventType;
        this.eventMessage = eventMessage;
    }

    /**
     * Provides a String representation of this object
     *
     * @return
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }

    /**
     * @return the eventTypeId
     */
    public BigDecimal getEventId() {
        return eventId;
    }

    /**
     * @return the eventType
     */
    public TwitterEventType getEventType() {
        return eventType;
    }

    /**
     * @return the eventDescription
     */
    public String getEventMessage() {
        return eventMessage;
    }

    /**
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

}
