package gov.usgs.cida.twitter.data.model;

import com.google.common.base.Objects;
import java.io.Serializable;

/**
 *
 * @author isuftin
 */
public class EventType implements Serializable {

    private static final long serialVersionUID = 680172890678713337L;

    private Integer eventTypeId;
    private String eventType;
    private String eventDescription;

    public EventType() {}
    
    public EventType(EventTypeType type) {
        switch(type) {
            case CONNECTED:
                this.eventTypeId = 1;
                break;
            case CONNECTION_ATTEMPT:
                this.eventTypeId = 2;
                break;
            case CONNECTION_ERROR:
                this.eventTypeId = 3;
                break;
            case DISCONNECTED:
                this.eventTypeId = 4;
                break;
            case HTTP_ERROR:
                this.eventTypeId = 5;
                break;
            case PROCESSING:
                this.eventTypeId = 6;
                break;
            case STOPPED_BY_ERROR:
                this.eventTypeId = 7;
                break;
            case STOPPED_BY_USER:
                this.eventTypeId = 8;
                break;
        }
    }
    
    public EventType(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
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
    public Integer getEventTypeId() {
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
