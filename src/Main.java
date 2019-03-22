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
  public static ArrayList<Character> facts = new ArrayList<>();
  public static ArrayList<Character> queries = new ArrayList<>();
  public static ArrayList<String> leftPart = new ArrayList<>();
  public static ArrayList<String> rightPart = new ArrayList<>();
  public static Stack<String> table = new Stack<>();
  public static Stack<String> tableRight = new Stack<>();
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

        outputRes();
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

//    try {
//      Parser.solve();
//    } catch (IOException ex) {
//      System.out.println(ex.getMessage());
//      Parser.usage(formatter, options);
//    }

    System.out.println("End!");
  }
}
