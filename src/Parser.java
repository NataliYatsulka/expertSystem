import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static int[][] mas;

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
//      System.out.println("1 " + list.get(i));
        }
    }


    public static void checkFileOnFactQueries(List<String> list) {
        int count = 0;
//    int countRaw = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).matches("=[A-Z]+")) {
                String[] s = list.get(i).split("=");
                for (int k = 0; k < s[1].length(); k++)
                    Main.facts.add(s[1].charAt(k));
                list.remove(i);
                i--;
                count++;
            }
            if (list.get(i).matches("\\?[A-Z]+")) {
                String[] s = list.get(i).split("\\?");
                for (int k = 0; k < s[1].length(); k++)
                    Main.queries.add(s[1].charAt(k));
                list.remove(i);
                i--;
                count++;
            }
            Main.countRaw = i;
        }
        Main.countRaw++;
//    Main.beingInThisRaw = new boolean[++countRaw];
//    System.out.println("booleanMass   = " + Main.beingInThisRaw);
        System.out.println("ooo =" + list);
//    Main.countRaw = countRaw;
        System.out.println("Main.countRaw = " + Main.countRaw);
        System.out.println(Main.facts + "     " + Main.queries);
        if (count != 2)
            Message.exception("ERROR:  more than one time = or ?");
    }

    public static boolean isLetterOrSign(char c) {
        return (c >= 'A' && c <= 'Z') || c == '(' || c == ')' || c == '+' || c == '^' || c == '|' || c == '!';
    }

    public static void checkLeftRightSiteOfRow(ArrayList<String> left) {

        for (int i = 0; i < left.size(); i++) {
            for (int j = 0; j < left.get(i).length(); j++) {
                if (!isLetterOrSign(left.get(i).charAt(j)))
                    Message.exception("ERROR:  Not right row " + i);
                char c = left.get(i).charAt(j);
                char prev = c;

                if (Character.isLetter(prev))
                    Main.mapOfFacts.put(prev, false);

                if (j == 0 && left.get(i).length() == 1) {
                    if (!Character.isLetter(prev))
                        Message.exception("ERROR:  Row can be only A-Z in row " + i);
                } else if (j != left.get(i).length() - 1) {

                    char next = left.get(i).charAt(j + 1);

                    if (j == 0 && (c == ')' || c == '+' || c == '^' || c == '|')) {
                        Message.exception("ERROR:  Row can not start by " + c);
                    } else if (j == left.get(i).length() - 1 && (c == '(' || c == '+' || c == '^' || c == '|')) {
                        Message.exception("ERROR:  Row can not end " + c);
                    } else if ((prev == '!' && (next == '+' || next == '^' || next == '|' || next == '!'))
                            || (prev == '(' && (next == '+' || next == '^' || next == '|' || next == ')'))
                            || ((Character.isLetter(prev) || prev == ')') && (next == '(' || Character.isLetter(next) || next == '!'))
                            || ((prev == '+' || prev == '^' || prev == '|') && (next == '+' || next == '^' || next == '|' || next == ')')))
                        Message.exception("ERROR:  Row can not have after " + prev + " - " + next);
                }
            }
        }
    }

    public static void addFactsToMap() {
        for (int i = 0; i < Main.facts.size(); i++) {
            Main.mapOfFacts.put(Main.facts.get(i), true);
        }
    }

    public static boolean checkFile(List<String> list) {
        List<Structure> structures = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String[] tmp = list.get(i).split("<=>");
            if (list.get(i).matches(".+<=>.+")) {
                structures.add(new Structure(tmp[0], tmp[1], true, false));
            } else if (list.get(i).matches(".+=>.+")) {
                tmp = list.get(i).split("=>");
                structures.add(new Structure(tmp[0], tmp[1], false, false));
            } else
                Message.exception("ERROR:  One of the line have bad initialization in row  " + list.get(i));
            Main.leftPart.add(tmp[0]);
            Main.rightPart.add(tmp[1]);
            if ((StringUtils.countMatches(tmp[0], "(") != StringUtils.countMatches(tmp[0], ")")) ||
                    (StringUtils.countMatches(tmp[1], "(") != StringUtils.countMatches(tmp[1], ")")))
                Message.exception("ERROR:  You forgot () in the row");
        }
        checkLeftRightSiteOfRow(Main.leftPart);
        checkLeftRightSiteOfRow(Main.rightPart);
        addFactsToMap();
        System.out.println("mapOfFacts = " + Main.mapOfFacts);
        System.out.println("facts =" + Main.facts);
        for (int z = 0; z < structures.size(); z++) {
            System.out.println("qqq    = " + structures.get(z).toString());
        }
        return true;
    }


    public static ArrayList<Integer> findAllQuery(int indexQuery) {
        ArrayList<Integer> mas = new ArrayList<>();
        for (int i = 0; i < Main.countRaw; i++) {
            if (Main.tableGrid[i][indexQuery] == 2) {
                mas.add(i);
            }
        }
        return mas;
    }

    public static ArrayList<Integer> findAllCondition(int indexQuery) {
        ArrayList<Integer> mas = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            if (Main.tableGrid[indexQuery][i] == 1) {
                mas.add(i);
            }
        }
        return mas;
    }

    public static void bc(char query) {// throws IOException {
        ArrayList<Integer> masQuery = new ArrayList<>();
        ArrayList<Integer> masCondit = new ArrayList<>();

        if (!Main.facts.contains(query)) {
            System.out.println("\u001B[33m" + "Querie to evaluete: " + query + "\u001B[0m");
            int indexQuery = query - 'A';
            masQuery = findAllQuery(indexQuery);
            System.out.println("masQuery " + masQuery.size());
            if (masQuery.size() < 1) {
                Main.mapOfFacts.put(query, false);
            }
            for (int j = 0; j < masQuery.size(); j++) {
                masCondit = findAllCondition(masQuery.get(j));
                System.out.println("masCond" + masCondit);
                for (int k = 0; k < masCondit.size(); k++) {
                    bc((char) (masCondit.get(k) + 'A'));
                }
            }
        }
    }
}
