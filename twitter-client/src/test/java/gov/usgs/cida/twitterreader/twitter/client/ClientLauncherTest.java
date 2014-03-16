package gov.usgs.cida.twitterreader.twitter.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.Calendar;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

    private File tempWorkDirectory;
    private String buildDirectory = System.getProperty("props.directory.build", System.getProperty("java.io.tmpdir"));
    private Properties propertiesFile;

    public ClientLauncherTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        tempWorkDirectory = Files.createTempDirectory(new File(buildDirectory).toPath(), "").toFile();
        FileUtils.copyFileToDirectory(new File("src/main/resources/sample.properties"), tempWorkDirectory);
        tempWorkDirectory.deleteOnExit();
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.forceDelete(tempWorkDirectory);
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

    @Test(expected = FileNotFoundException.class)
    public void testRunWithInvalidPropertiesPath() throws FileNotFoundException {
        System.out.println("testRunWithInvalidPropertiesPath");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "not-a-real.properties"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.run(args);
    }

    @Test
    public void testRunWithValidPropertiesPath() throws FileNotFoundException {
        System.out.println("testRunWithValidPropertiesPath");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "sample.properties"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.run(args);
    }

}
