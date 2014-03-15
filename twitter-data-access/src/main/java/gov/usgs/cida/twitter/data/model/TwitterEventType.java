package gov.usgs.cida.twitter.data.model;

import com.google.common.base.Objects;
import com.twitter.hbc.core.event.EventType;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author isuftin
 */
public class TwitterEventType implements Serializable {

    private static final long serialVersionUID = 680172890678713337L;

    private Integer eventTypeId;
    private String eventType;
    private String eventDescription;

    public TwitterEventType() {
    }

    public TwitterEventType(EventType eventType) {
        this(Type.valueOf(eventType.name()));
    }

    public TwitterEventType(Type type) {
        this.eventTypeId = type.getEventType();
        this.eventType = type.getEventTypeName();
        this.eventDescription = type.getEventDescription();
    }

    /**
     * Provides a String representation of this object
     *
     * @return
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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

    /**
     * Represents a finite set of Event types that may exist
     */
    public static enum Type {

        /**
         * When an http request is made
         */
        CONNECTION_ATTEMPT(1, "CONNECTION_ATTEMPT", "When an http request is made"),
        /**
         * When a connection is established w/ a 200 response
         */
        CONNECTED(2, "CONNECTED", "When a connection is established w/ a 200 response"),
        /**
         * When we begin receiving/processing messages
         */
        PROCESSING(3, "PROCESSING", "When we begin receiving/processing messages"),
        /**
         * When an established connection gets disconnected for any reason
         */
        DISCONNECTED(4, "DISCONNECTED", "When an established connection gets disconnected for any reason"),
        /**
         * When a connection fails due to either a bad request (invalid host,
         * invalid requests)
         */
        CONNECTION_ERROR(5, "CONNECTION_ERROR", "When a connection fails due to either a bad request (invalid host, invalid requests)"),
        /**
         * When a connection fails due to a 400/500 response
         */
        HTTP_ERROR(6, "HTTP_ERROR", "When a connection fails due to a 400/500 response"),
        /**
         * When the client is explicitly stopped by the user. No more
         * connections will be attempted
         */
        STOPPED_BY_USER(7, "STOPPED_BY_USER", "When the client is explicitly stopped by the user. No more connections will be attempted"),
        /**
         * When the client is stopped due to an error. No more connections will
         * be attempted
         */
        STOPPED_BY_ERROR(8, "STOPPED_BY_ERROR", "When the client is stopped due to an error. No more connections will be attempted");

        private final int eventType;
        private final String eventTypeName;
        private final String eventDescription;

        private Type(int type, String name, String description) {
            eventType = type;
            eventTypeName = name;
            eventDescription = description;
        }

        public int getEventType() {
            return eventType;
        }

        public String getEventTypeName() {
            return eventTypeName;
        }

        public String getEventDescription() {
            return eventDescription;
        }
    }

}
