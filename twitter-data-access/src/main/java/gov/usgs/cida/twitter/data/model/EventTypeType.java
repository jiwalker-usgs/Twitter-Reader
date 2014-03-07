package gov.usgs.cida.twitter.data.model;

/**
 *
 * @author isuftin
 */
public enum EventTypeType {

    /**
     * When an http request is made
     */
    CONNECTION_ATTEMPT, /**
     * When a connection is established w/ a 200 response
     */
    CONNECTED, /**
     * When we begin receiving/processing messages
     */
    PROCESSING, /**
     * When an established connection gets disconnected for any reason
     */
    DISCONNECTED, /**
     * When a connection fails due to either a bad request (invalid host,
     * invalid requests)
     */
    CONNECTION_ERROR, /**
     * When a connection fails due to a 400/500 response
     */
    HTTP_ERROR, /**
     * When the client is explicitly stopped by the user. No more connections
     * will be attempted
     */
    STOPPED_BY_USER, /**
     * When the client is stopped due to an error. No more connections will be
     * attempted
     */
    STOPPED_BY_ERROR

}
