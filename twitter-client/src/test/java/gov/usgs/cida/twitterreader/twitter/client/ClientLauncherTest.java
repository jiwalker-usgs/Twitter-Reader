package gov.usgs.cida.twitterreader.twitter.client;

import java.io.FileNotFoundException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author isuftin
 */
public class ClientLauncherTest {
    
    public ClientLauncherTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testRunWithNullArguments() throws FileNotFoundException {
        System.out.println("testRunWithNullArguments");
        String[] args = null;
        ClientLauncher instance = new ClientLauncher();
        instance.run(args);
        assertTrue("Client launcher did not hit NPE with null arguments", true);
    }
    
    @Test
    public void testRunWithNoArguments() throws FileNotFoundException {
        System.out.println("testRunWithNoArguments");
        String[] args = new String[]{""};
        ClientLauncher instance = new ClientLauncher();
        instance.run(args);
        assertTrue("Client launcher did not throw back exception", true);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testRunWithInvalidDirectory() throws FileNotFoundException {
        System.out.println("testRunWithInvalidDirectory");
        String[] args = new String[]{"-d", "/directory/does/not/exist"};
        ClientLauncher instance = new ClientLauncher();
        instance.run(args);
    }
    
}
