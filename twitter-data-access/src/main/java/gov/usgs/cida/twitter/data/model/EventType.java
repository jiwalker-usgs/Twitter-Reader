package gov.usgs.cida.twitter.data.model;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author isuftin
 */
public class EventType implements Serializable {

    private static final long serialVersionUID = 680172890678713337L;

    private BigDecimal eventTypeId;
    private String eventType;
    private String eventDescription;

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
    public BigDecimal getEventTypeId() {
        return eventTypeId;
    }

    /**
     * @return the eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * @return the eventDescription
     */
    public String getEventDescription() {
        return eventDescription;
    }

}
