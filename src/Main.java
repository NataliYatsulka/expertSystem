

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        String path = null;

        HelpFormatter formatter = new HelpFormatter();

        try {
            if (args.length != 2)
                Message.exception("Bad length of args");
            Options options = Parser.parsingInputArgs(args);
            CommandLine cmd = new BasicParser().parse(options, args);
            path = cmd.getOptionValue("path");
            System.out.println("path = " + path);
        } catch (ParseException ex) {
            Message.exception(ex.getMessage());
        }
        System.out.println("End!");
    }
}
