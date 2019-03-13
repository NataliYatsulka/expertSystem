import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Parser {

    public static Options parsingInputArgs(String[] args) {
        Options options = new Options();

        Option path = new Option("p", "path", true, "path to your file");
        path.setRequired(false);
        options.addOption(path);

        return options;
    }

    public static void usage(HelpFormatter formatter, Options options) {
        formatter.printHelp("expert-system", options);
        System.exit(1);
    }

    public static List<String> readTextFileByLines(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName));
    }

    public static void deleteComments(List<String> list) {

        for (int i = 0; i < list.size(); i++) {
            String[] res = list.get(i).split("#");
            list.set(i, res[0]);
            if (list.get(i).isEmpty()) {
                list.remove(i);
                i--;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            String[] res = list.get(i).split(" ");
            String row = "";
            for (String re : res) {
                row = row + re;
            }
            list.set(i, row);
            System.out.println(list.get(i));
        }
    }

    public static void checkFileOnFact(List<String> list) {
        int count = 0;
        for (String s : list) {
            if (s.matches("=[A-Z]+")) {
                System.out.println("dano: " + s);
                count++;
            }
            if (s.matches("\\?[A-Z]+")) {
                System.out.println("should find: " + s);
                count++;
            }
        }
        if (count != 2)
            Message.exception("ERROR:  more than one time = or ?");
    }

    public static boolean checkFile(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).matches("[A-Z]");
        }
        return true;
    }
}
