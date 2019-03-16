import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static String[] facts = new String[32];
    public static List<String> rules =  Arrays.asList("(", ")", "!", "+", "|", "^", "=>", "<=>");
    public static String[] queries = new String[32];
    public static ArrayList<String> leftPart = new ArrayList<>();
    public static ArrayList<String> rightPart = new ArrayList<>();


    public static void main(String[] args) {
        String path = null;
        Options options = Parser.parsingInputArgs(args);
        List<String> list;

        HelpFormatter formatter = new HelpFormatter();

        try {
            if (args.length != 2)
                Message.exception("Bad length of args");
            CommandLine cmd = new BasicParser().parse(options, args);
            path = cmd.getOptionValue("path");
            System.out.println("path = " + path);
            if (path != null) {
                File f = new File(path);
                if (!f.exists() || f.isDirectory()) {
                    throw new FileNotFoundException("ERROR:   File not found");
                }
                list = Parser.readTextFileByLines(path);
                if (!list.isEmpty())
                    Parser.deleteComments(list);
                else
                    Message.exception("ERROR:  The list is empty");
                Parser.checkFileOnFactQueries(list);
                Parser.checkFile(list);
            }
        } catch (ParseException | IOException ex) {
            System.out.println(ex.getMessage());
            Parser.usage(formatter, options);
        }
        System.out.println("End!");
    }
}
