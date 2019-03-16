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


  public static void checkFileOnFactQueries(List<String> list) {
    int count = 0;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).matches("=[A-Z]+")) {
        String[] s = list.get(i).split("=");
        Main.facts[0] = s[1];
        System.out.println("Main.fact: " + Main.facts[0]);
        list.remove(i);
        i--;
        count++;
      }
      if (list.get(i).matches("\\?[A-Z]+")) {
        String[] s = list.get(i).split("\\?");
        Main.queries[0] = s[1];
        System.out.println("Main.queries: " + Main.queries[0]);
        list.remove(i);
        i--;
        count++;
      }
    }
    System.out.println(list);
    if (count != 2)
      Message.exception("ERROR:  more than one time = or ?");
  }

  public static void checkLeftRightSiteOfRow(ArrayList<String> left, ArrayList<String> right) {
    int couunt = 0;
    for (int i = 0; i < left.size(); i++) {
      System.out.print(couunt++ + " ");
      if (left.get(i).matches("(!*\\(*!*[A-Z][|+^]?[A-Z]?\\)*)+([[|+^]?\\(*!*[A-Z][|+^]?[A-Z]?\\)*])*")) {
        System.out.print("YES");
      } else {
        System.out.println("NO");
      }
      if (right.get(i).matches("(!*\\(*!?[A-Z][|+^]?(!?[A-Z])?\\)*)+([[|+^]?\\(*!*[A-Z][|+^]?[A-Z]?\\)*])*"))
        System.out.print("YESss");
      else
        System.out.println("NOoo");
      System.out.println();
    }
  }

  public static boolean checkFile(List<String> list) {
    for (int i = 0; i < list.size(); i++) {
      String[] tmp = list.get(i).split("<=>");
      if (list.get(i).matches(".+<=>.+")) {
        Main.leftPart.add(tmp[0]);
        Main.rightPart.add(tmp[1]);
      } else if (list.get(i).matches(".+=>.+")) {
        tmp = list.get(i).split("=>");
        Main.leftPart.add(tmp[0]);
        Main.rightPart.add(tmp[1]);
      } else
        Message.exception("ERROR:  One of the line have bad initialization in row  " + list.get(i));
      if ((StringUtils.countMatches(tmp[0], "(") != StringUtils.countMatches(tmp[0], ")")) ||
              (StringUtils.countMatches(tmp[1], "(") != StringUtils.countMatches(tmp[1], ")")))
        Message.exception("ERROR:  You forgot () in the row");
//      if (tmp[0].matches("([A-Z][A-Z]+[+|^][+|^]+[A-Z][A-Z]+)+"))
//        Message.exception("ERROR:  Bad row " + i);
    }
    System.out.println(Main.leftPart);
    System.out.println(Main.rightPart);
    checkLeftRightSiteOfRow(Main.leftPart, Main.rightPart);
    return true;
  }
}
