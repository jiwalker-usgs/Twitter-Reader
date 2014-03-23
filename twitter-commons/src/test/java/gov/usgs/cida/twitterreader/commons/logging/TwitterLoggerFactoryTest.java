package gov.usgs.cida.twitterreader.commons.logging;

import ch.qos.logback.classic.Logger;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author isuftin
 */
public class TwitterLoggerFactoryTest {

    private File tempWorkDirectory;
    private final String buildDirectory = System.getProperty("props.directory.build", System.getProperty("java.io.tmpdir"));

    public TwitterLoggerFactoryTest() {
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
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.forceDelete(tempWorkDirectory);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateLoggerUsingNullLoggerContext() throws IOException {
        System.out.println("testCreateLoggerUsingNullLoggerContext");
        Logger result = new TwitterLoggerFactory(null).createLogger();
        result.info("Test");
    }

    @Test
    public void testCreateConsoleLoggerUsingBaseLoggerContext() throws IOException {
        System.out.println("testCreateConsoleLoggerUsingBaseLoggerContext");
        TwitterLoggerContext lc = new TwitterLoggerContext("Test Console Logger");
        Logger result = new TwitterLoggerFactory(lc).createLogger();

        assertNotNull(result);
        assertThat(result.getName(), is("Test Console Logger"));
    }

    @Test
    public void testCreateFileLogger() throws IOException {
        System.out.println("testCreateFileLogger");
        TwitterLoggerContext lc = new TwitterLoggerContext(this.getClass());
        lc.setLoggerType(LoggerType.FILE);
        lc.setOutputDirectory(tempWorkDirectory);
        Logger result = new TwitterLoggerFactory(lc).createLogger();

        assertNotNull(result);
        assertThat(result.getName(), is(this.getClass().getName()));
    }

    @Test
    public void testCreateFileLoggerAndTryLogging() throws IOException {
        System.out.println("testCreateFileLoggerAndTryLogging");
        TwitterLoggerContext lc = new TwitterLoggerContext(this.getClass());
        lc.setLoggerType(LoggerType.FILE);
        lc.setOutputDirectory(tempWorkDirectory);
        Logger result = new TwitterLoggerFactory(lc).createLogger();

        assertNotNull(result);
        assertThat(result.getName(), is(this.getClass().getName()));

        result.info("Test Log!");
        FileFilter ff = new SuffixFileFilter("log");
        File[] files = tempWorkDirectory.listFiles(ff);
        assertThat(files.length, is(1));
        List<String> fileLines = FileUtils.readLines(files[0]);
        assertThat(fileLines.isEmpty(), is(Boolean.FALSE));
        assertThat(fileLines.get(0), containsString("INFO"));
        assertThat(fileLines.get(0), containsString("Test Log!"));
    }
    
    @Test(expected = IOException.class)
    public void testCreateFileLoggerWithBogusOutputDir() throws IOException {
        System.out.println("testCreateFileLoggerWithBogusOutputDir");
        File bogusOutputDirectory = new File("/wouldnt/it/be/funny/if/this/did/exist");
        TwitterLoggerContext lc = new TwitterLoggerContext(this.getClass());
        lc.setLoggerType(LoggerType.FILE);
        lc.setOutputDirectory(bogusOutputDirectory);
        Logger result = new TwitterLoggerFactory(lc).createLogger();
        result.info("Test Log!");
    }

}
