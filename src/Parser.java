import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Parser {

    public static Options parsingInputArgs(String[] args) {
        Options options = new Options();

        Option path = new Option("p", "path", true, "path to your file");
        path.setRequired(false);
        options.addOption(path);

        return options;
    }
}
