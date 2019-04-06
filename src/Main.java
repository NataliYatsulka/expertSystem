import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;

public class Main {
//    public static String[] facts = new String[32];
//    public static List<String> rules =  Arrays.asList("(", ")", "!", "+", "|", "^", "=>", "<=>");
//    public static String[] queries = new String[32];
    public static HashMap<Character, Integer> mapOfFacts = new HashMap<Character, Integer>();
    public static HashMap<Character, Integer> mapOfWords = new HashMap<Character, Integer>();
    public static ArrayList<Character> facts = new ArrayList<>();
    public static ArrayList<Character> queries = new ArrayList<>();
    public static ArrayList<String> leftPart = new ArrayList<>();
    public static ArrayList<String> rightPart = new ArrayList<>();
    public static LinkedList<String> table = new LinkedList<>();
    public static LinkedList<LinkedList<String>> tableList = new LinkedList<>();//nik
    public static List<String> tableRight = new LinkedList<>();
    public static int countRaw;
    public static Integer[][] tableGrid;
//  public static boolean[] beingInThisRaw;

    public static final Integer UNDEFINED = -1;
    public static final Integer FALSE = 0;
    public static final Integer TRUE = 1;

    public static void outputRes() {
        System.out.println("\u001B[32m" + "The Result is next: " + "\u001B[0m");
        for (int i = 0; i < queries.size(); i++) {
            if (mapOfFacts.get(queries.get(i)) == null) {
                System.out.println("\u001B[32m" + queries.get(i) + " = " + false + "\u001B[0m");}
                else
                System.out.println("\u001B[32m" + queries.get(i) + " = " + mapOfFacts.get(queries.get(i)) + "\u001B[0m");
        }
    }

    public static void setTableGrid(){
        tableGrid = new Integer[Main.countRaw][26];
        System.out.println("leftPart = " + leftPart);

        for (Map.Entry<Character, Integer> oneEntry : mapOfFacts.entrySet()) {
            for (int j = 0; j < countRaw; j++) {
                tableGrid[j][(oneEntry.getKey() - 'A')] = 0;
                if ((leftPart.get(j)).indexOf(oneEntry.getKey()) >= 0) {
                    tableGrid[j][(oneEntry.getKey() - 'A')] |= 0b0001;
                }
                if ((rightPart.get(j)).indexOf(oneEntry.getKey()) >= 0) {
                    tableGrid[j][(oneEntry.getKey() - 'A')] |= 0b0010;
                }
            }
        }

        for (int i = 0; i < Main.countRaw; i++) {
            for (int j = 0; j < 26; j++) {
                if (tableGrid[i][j] == null)
                    tableGrid[i][j] = 0;
            }
        }
    }

    public static void main(String[] args) {
        String path = null;
        String goal = null;
        String fact = null;

        Options options = Parser.parsingInputArgs(args);
        List<String> list;

        HelpFormatter formatter = new HelpFormatter();
        facts.contains(queries);
        try {
            if (args.length < 2)
                Parser.usage(formatter, options);
            CommandLine cmd = new BasicParser().parse(options, args);
            path = cmd.getOptionValue("path");
            goal = cmd.getOptionValue("goals");
            fact = cmd.getOptionValue("facts");

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
            LinkedList<String> tmp = Algo.parse(leftPart.get(i));
            table.add(tmp.toString());
            tableList.add(tmp);
            tableRight.add(Algo.parse(rightPart.get(i)).toString());
            System.out.println("table = " + table.get(i));
            System.out.println("tableRight = " + tableRight.get(i));

        }

//        tableGrid = new Integer[Main.countRaw][26];
//        System.out.println("leftPart = " + leftPart);
//
//        for (Map.Entry<Character, Boolean> oneEntry : mapOfFacts.entrySet()) {
//            for (int j = 0; j < countRaw; j++) {
//                tableGrid[j][(oneEntry.getKey() - 'A')] = 0;
//                if ((leftPart.get(j)).indexOf(oneEntry.getKey()) >= 0) {
//                    tableGrid[j][(oneEntry.getKey() - 'A')] = 1;
//                }
//                if ((rightPart.get(j)).indexOf(oneEntry.getKey()) >= 0) {
//                    tableGrid[j][(oneEntry.getKey() - 'A')] = 2;
//                }
//            }
//        }
//
//        for (int i = 0; i < Main.countRaw; i++) {
//            for (int j = 0; j < 26; j++) {
//                if (tableGrid[i][j] == null)
//                    tableGrid[i][j] = 0;
//            }
//        }
        setTableGrid();

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

        if (goal != null || fact != null) {
            if (goal != null) {
                try {
                    for (int l = Main.queries.size() - 1; l >= 0; l--) {
                        Main.queries.remove(l);
                    }
                    System.out.println("IS= " + Main.queries);
                    System.out.println("New goal(s) added");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Enter new goal(s), which you want to find");
                    String string = br.readLine();
                    System.out.println("Read string from input: " + string);
                    if (string.matches("[A-Z]+")) {
                        for (int k = 0; k < string.length(); k++)
                            Main.queries.add(string.charAt(k));
                        outputRes();
                    } else Message.exception("ERROR:  Bad bonus input");
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Parser.usage(formatter, options);
                }
            }

            if (fact != null) {
                try {
                    for (int l = Main.facts.size() - 1; l >= 0; l--) {
                        Main.facts.remove(l);
                    }
                    System.out.println("facts= " + Main.facts);
                    System.out.println("New fact(s) added");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Enter new fact(s), which you want to find");
                    String string = br.readLine();
                    System.out.println("Read string from input: " + string);
                    if (string.matches("[A-Z]+")) {
                        for (int k = 0; k < string.length(); k++)
                            Main.facts.add(string.charAt(k));
                    } else Message.exception("ERROR:  Bad bonus input");
                    Parser.addFactsToMap();
                    System.out.println("bonus facts = " + facts);
                    setTableGrid();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Parser.usage(formatter, options);
                }
            }

            for (Character query : queries) {
                Parser.bc(query);
            }
            outputRes();
        }

        System.out.println("End!");
    }
}
