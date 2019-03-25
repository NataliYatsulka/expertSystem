import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
//    public static String[] facts = new String[32];
//    public static List<String> rules =  Arrays.asList("(", ")", "!", "+", "|", "^", "=>", "<=>");
//    public static String[] queries = new String[32];

    public static HashMap<Character, Boolean> mapOfFacts = new HashMap<Character, Boolean>();
    public static HashMap<Character, Integer> mapOfWords = new HashMap<Character, Integer>();
    public static ArrayList<Character> facts = new ArrayList<>();
    public static ArrayList<Character> queries = new ArrayList<>();
    public static ArrayList<String> leftPart = new ArrayList<>();
    public static ArrayList<String> rightPart = new ArrayList<>();
    public static Stack<String> table = new Stack<>();
    public static Stack<String> tableRight = new Stack<>();
    public static int countRaw;
    public static Integer[][] tableGrid;
//  public static boolean[] beingInThisRaw;

    public static void outputRes() {
        System.out.println("\u001B[32m" + "The Result is next: " + "\u001B[0m");
        for (int i = 0; i < queries.size(); i++) {
            System.out.println("\u001B[32m" + queries.get(i) + " = " + mapOfFacts.get(queries.get(i)) + "\u001B[0m");
        }
    }

    public static void main(String[] args) {
        String path = null;
        Options options = Parser.parsingInputArgs(args);
        List<String> list;

        HelpFormatter formatter = new HelpFormatter();
        facts.contains(queries);
        try {
            if (args.length < 2)
                Parser.usage(formatter, options);
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

        for (int i = 0; i < leftPart.size(); i++) {
            table.push(Algo.parse(leftPart.get(i)).toString());
            tableRight.push(Algo.parse(rightPart.get(i)).toString());
            System.out.println("table = " + table.get(i));
            System.out.println("tableRight = " + tableRight.get(i));

        }

        tableGrid = new Integer[Main.countRaw][26];
        System.out.println("leftPart = " + leftPart);

        for (Map.Entry<Character, Boolean> oneEntry : mapOfFacts.entrySet()) {
            for (int j = 0; j < countRaw; j++) {
                tableGrid[j][(oneEntry.getKey() - 'A')] = 0;
                if ((leftPart.get(j)).indexOf(oneEntry.getKey()) >= 0) {
                    tableGrid[j][(oneEntry.getKey() - 'A')] = 1;
                }
                if ((rightPart.get(j)).indexOf(oneEntry.getKey()) >= 0) {
                    tableGrid[j][(oneEntry.getKey() - 'A')] = 2;
                }
            }
        }

        for (int i = 0; i < Main.countRaw; i++) {
            for (int j = 0; j < 26; j++) {
                if (tableGrid[i][j] == null)
                    tableGrid[i][j] = 0;
            }
        }

        //delete
        for (int i = 0; i < Main.countRaw; i++) {
            for (int j = 0; j < 26; j++) {
                System.out.print(String.format("%3d", tableGrid[i][j]));
            }
            System.out.println();
        }

        for (Character query : queries) {
            Parser.bc(query);
        }
        outputRes();
        System.out.println("End!");
    }
}
