package gov.usgs.cida.twitter.data.model;

import com.twitter.hbc.core.event.EventType;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author isuftin
 */
public class TwitterEventTypeTest {

    @Test
    public void testToStringOnEmptyTwitterEventType() {
        System.out.println("testToStringOnEmptyTwitterEventType");
        TwitterEventType instance = new TwitterEventType();
        String startsWith = "gov.usgs.cida.twitter.data.model.TwitterEventType";
        String endsWith = "[eventTypeId=<null>,eventType=<null>,eventDescription=<null>]";
        String result = instance.toString();
        assertThat(result, startsWith(startsWith));
        assertThat(result, containsString(endsWith));
    }

    @Test
    public void testToStringOnNonEmptyTwitterEventType() {
        System.out.println("testToStringOnNonEmptyTwitterEventType");
        TwitterEventType instance = new TwitterEventType(TwitterEventType.Type.CONNECTED);
        String expResult = "[eventTypeId=2,eventType=CONNECTED,eventDescription=When a connection is established w/ a 200 response]";
        String result = instance.toString();
        assertThat(result, containsString(expResult));
    }

    @Test
    public void testCreateTwitterEventTypeUsingEventType() {
        System.out.println("testToStringOnNonEmptyTwitterEventType");
        TwitterEventType instance = new TwitterEventType(EventType.CONNECTED);
        String expResult = "[eventTypeId=2,eventType=CONNECTED,eventDescription=When a connection is established w/ a 200 response]";
        String result = instance.toString();
        assertThat(result, containsString(expResult));
    }

}
