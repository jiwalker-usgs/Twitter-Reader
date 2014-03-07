package gov.usgs.cida.twitter.data.model;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author isuftin
 */
public class Event implements Serializable {
    
    private static final long serialVersionUID = 5140178149793574673L;

    private BigDecimal eventId;
    private EventType eventType;
    private String eventMessage;

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
    public EventType getEventType() {
        return eventType;
    }

    /**
     * @return the eventDescription
     */
    public String getEventMessage() {
        return eventMessage;
    }

}
