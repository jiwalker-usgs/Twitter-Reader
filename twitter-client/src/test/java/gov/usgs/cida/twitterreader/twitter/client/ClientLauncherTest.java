package gov.usgs.cida.twitterreader.twitter.client;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

/**
 *
 * @author isuftin
 */
public class ClientLauncherTest {

    private File tempWorkDirectory;
    private final String buildDirectory = System.getProperty("props.directory.build", System.getProperty("java.io.tmpdir"));

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
        FileUtils.copyFileToDirectory(new File("src/test/resources/valid.properties"), tempWorkDirectory);
        FileUtils.copyFileToDirectory(new File("src/test/resources/simpleauth.properties"), tempWorkDirectory);
        FileUtils.copyFileToDirectory(new File("src/test/resources/oauth.properties"), tempWorkDirectory);
        tempWorkDirectory.deleteOnExit();
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.forceDelete(tempWorkDirectory);
    }

    @Test(expected = NullPointerException.class)
    public void testRunWithNullArguments() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testRunWithNullArguments");
        String[] args = null;
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        fail("Expected a NullPointerException");
    }

    @Test
    public void testRunWithNoArguments() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testRunWithNoArguments");
        String[] args = new String[]{""};
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        assertTrue("No exception was generated", true);
    }

    @Test(expected = FileNotFoundException.class)
    public void testRunWithInvalidDirectory() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testRunWithInvalidDirectory");
        String[] args = new String[]{
            "-d", "/directory/does/not/exist",
            "-p", "required.input"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        fail("Expected a FileNotFoundException");
    }

    @Test(expected = FileNotFoundException.class)
    public void testRunWithInvalidPropertiesPath() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testRunWithInvalidPropertiesPath");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "not-a-real.properties"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        fail("Expected a FileNotFoundException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRunWithValidPropertiesFileWithoutAuthenticationCredentials() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testRunWithValidPropertiesFileWithoutAuthenticationCredentials");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "valid.properties"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        fail("Expected a IllegalArgumentException");
    }

    @Test
    public void testRunWithValidPropertiesFileWithValidOAuthCredentials() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testRunWithValidPropertiesFileWithValidOAuthCredentials");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "oauth.properties,valid.properties"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        assertTrue("No exception was generated", true);
    }

    @Test
    public void testRunWithValidPropertiesFileWithValidSimpleAuthCredentials() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testRunWithValidPropertiesFileWithValidSimpleAuthCredentials");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "simpleauth.properties,valid.properties"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        assertTrue("No exception was generated", true);
    }

    @Test
    public void testRunWithLoggers() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testRunWithLoggers");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "simpleauth.properties",
            "--logging.use"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        File logPath = new File(tempWorkDirectory, "logs");
        assertTrue(logPath.exists());
        assertTrue(logPath.isDirectory());
    }

    @Test
    public void testUseFilebasedLogging() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testUseFilebasedLogging");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "simpleauth.properties",
            "-l"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        File logDirectory = new File(tempWorkDirectory, "logs");
        FileFilter ff = new SuffixFileFilter("log");
        File[] files = logDirectory.listFiles(ff);
        assertThat(files.length, is(1));
        List<String> fileLines = FileUtils.readLines(files[0]);
        assertThat(fileLines.isEmpty(), is(Boolean.FALSE));
    }

    @Test
    public void testUseFilebasedDebugLogging() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testUseFilebasedDebugLogging");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "simpleauth.properties",
            "-l",
            "--track.userids", "1,2,3,4",
            "--debug"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        File logDirectory = new File(tempWorkDirectory, "logs");
        FileFilter ff = new SuffixFileFilter("log");
        File[] files = logDirectory.listFiles(ff);
        assertThat(files.length, is(1));
        List<String> fileLines = FileUtils.readLines(files[0]);
        assertThat(fileLines.isEmpty(), is(Boolean.FALSE));
        assertThat(fileLines.get(0), containsString("DEBUG"));
    }

    @Test
    public void testUseFilebasedDebugLoggingUsingPropertiesFileToDefineDebugging() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testUseFilebasedDebugLoggingUsingPropertiesFileToDefineDebugging");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "simpleauth.properties,valid.properties",
            "-l"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        File logDirectory = new File(tempWorkDirectory, "logs");
        FileFilter ff = new SuffixFileFilter("log");
        File[] files = logDirectory.listFiles(ff);
        assertThat(files.length, is(1));
        List<String> fileLines = FileUtils.readLines(files[0]);
        assertThat(fileLines.isEmpty(), is(Boolean.FALSE));
        assertThat(fileLines.get(0), containsString("DEBUG"));
    }

    @Test
    public void testUseFilebasedLoggingUsingPropertiesFileToDefineLogging() throws FileNotFoundException, IOException, CmdLineException {
        System.out.println("testUseFilebasedLoggingUsingPropertiesFileToDefineLogging");
        String[] args = new String[]{
            "-d", tempWorkDirectory.getAbsolutePath(),
            "-p", "simpleauth.properties,valid.properties"
        };
        ClientLauncher instance = new ClientLauncher();
        instance.buildClient(args);
        File logDirectory = new File(tempWorkDirectory, "logs");
        FileFilter ff = new SuffixFileFilter("log");
        File[] files = logDirectory.listFiles(ff);
        assertThat(files.length, is(1));
        List<String> fileLines = FileUtils.readLines(files[0]);
        assertThat(fileLines.isEmpty(), is(Boolean.FALSE));
        assertThat(fileLines.get(0), containsString("DEBUG"));
    }
}
