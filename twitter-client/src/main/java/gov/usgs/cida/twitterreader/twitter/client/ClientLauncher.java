package gov.usgs.cida.twitterreader.twitter.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;

/**
 *
 * @author isuftin
 */
public class ClientLauncher {

    private static CmdLineParser parser;
    private File baseDirectory;

    @Option(name = "-d", usage = "Application Base Directory", metaVar = "String", required = true)
    public void setBaseDirectory(File file) {
        baseDirectory = file;
    };

    @Option(name = "-p", usage = "Properties File Name", metaVar = "String", depends = {"-d"})
    private String propertiesFileName;

    @Option(name = "-h", usage = "Print this documentation", required = false, hidden = false)
    private boolean help;

    public void run(String... args) throws FileNotFoundException {
        parser = new CmdLineParser(this);
        parser.setUsageWidth(80);
        try {
            parser.parseArgument(args);
            
            if (!baseDirectory.exists()) {
                throw new FileNotFoundException(String.format("Directory at %s does not exist", baseDirectory.getAbsolutePath()));
            }
            
        } catch (CmdLineException | NullPointerException ex) {
            if (help) {
                printUsage(System.out);
            } else {
                if (ex instanceof NullPointerException) {
                    System.err.println("Null was passed into the launcher");
                } else {
                    System.err.println(ex.getMessage());
                }
                printUsage(System.err);
            }

        }
    }

    private void printUsage(PrintStream stream) {
        stream.println("java Client [options...] arguments...");
        parser.printUsage(stream);

        // print option sample. This is useful some time
        stream.println();
        stream.println(" Example: java Client" + parser.printExample(OptionHandlerFilter.REQUIRED));
    }
}
