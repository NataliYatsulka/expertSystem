package com.nn;

import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    public static int[][] mas;

    public static Options parsingInputArgs(String[] args) {
        Options options = new Options();

        Option path = new Option("p", "path", true, "path to your file");
        path.setRequired(false);
        options.addOption(path);

        Option goals = new Option("g", "goals", true, "new goals you want to find");
        goals.setRequired(false);
        options.addOption(goals);

        Option facts = new Option("f", "facts", true, "new facts");
        facts.setRequired(false);
        options.addOption(facts);

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
            list.set(i, list.get(i).replaceAll("\\s", ""));
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
            } else if (list.get(i).equals("=")) {
                count++;
                list.remove(i);
                i--;
            }else if (list.get(i).matches("\\?[A-Z]+")) {
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
        if (Main.debugOn)System.out.println("ooo =" + list);
//    Main.countRaw = countRaw;
        if (Main.debugOn)System.out.println("Main.countRaw = " + Main.countRaw);
        if (Main.debugOn) System.out.println(Main.facts + "     " + Main.queries);
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
                    Message.exception("ERROR:  Not right row " + (i + 1));
                char c = left.get(i).charAt(j);
                char prev = c;

                if (Character.isLetter(prev))
                    Main.mapOfFacts.put(prev, Main.UNDEFINED);

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
                } else if (j == left.get(i).length() - 1 && (prev == '|' || prev == '^' || prev == '+'))
                    Message.errorMsg("ERROR:  Bad raw " + (i + 1));
            }
        }
    }

    public static void addFactsToMap() {
        for (int i = 0; i < Main.facts.size(); i++) {
            Main.mapOfFacts.put(Main.facts.get(i), Main.TRUE);
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
        if (Main.debugOn)System.out.println("mapOfFacts = " + Main.mapOfFacts);
        if (Main.debugOn)System.out.println("facts =" + Main.facts);
        for (int z = 0; z < structures.size(); z++) {
            if (Main.debugOn)System.out.println("qqq    = " + structures.get(z).toString());
        }
        return true;
    }


    public static ArrayList<Integer> findAllQuery(int indexQuery) {
        ArrayList<Integer> mas = new ArrayList<>();
        for (int i = 0; i < Main.countRaw; i++) {
            if ((Main.tableGrid[i][indexQuery] & 0b0010) == 2) {
                mas.add(i);
            }
        }
        return mas;
    }

    public static ArrayList<Integer> findAllCondition(int indexQuery) {
        ArrayList<Integer> mas = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            if ((Main.tableGrid[indexQuery][i] & 0b001) == 1) {
                mas.add(i);
            }
        }
        return mas;
    }

    public static boolean isInLeft(char letter) {
        if (letter >= 'A' && letter <= 'Z') {
            Message.errorMsg("Wrong letter to search!");
        }
        for (int i = 0; i < 26; i++) {
            if ((Main.tableGrid[letter - 'A'][i] & 0b001) == 1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInRight(char letter) {
        if (letter >= 'A' && letter <= 'Z') {
            Message.errorMsg("Wrong letter to search!");
            System.exit(-1);
        }
        for (int i = 0; i < 26; i++) {
            if ((Main.tableGrid[letter - 'A'][i] & 0b010) == 2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInBoth(char letter) {
        if (letter >= 'A' && letter <= 'Z') {
            Message.errorMsg("Wrong letter to search!");
        }
        for (int i = 0; i < 26; i++) {
            if ((Main.tableGrid[letter - 'A'][i] & 0b011) == 3) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInFacts(char letter){
        if (letter <= 'A' && letter >= 'Z') {
            Message.errorMsg("Wrong letter to search!");
        }
        if (Main.mapOfFacts.get(letter).equals(Main.TRUE))
            return true;
        return false;
    }

    public static boolean isDefined(String block){
        if (block == null)
            Message.errorMsg("ERROR: NULL was passed to isUndefined()!");
        if (block.equals("true") || block.equals("false"))
            return true;
        if (block.length() == 1 && block.charAt(0) >= 'A' && block.charAt(0) <= 'Z' && !(Main.mapOfFacts.get(block.charAt(0)).equals(Main.UNDEFINED)))
            return isInFacts(block.charAt(0));
        if (block.length() == 2 && block.charAt(0) >= '!' && block.charAt(1) >= 'A' && block.charAt(1) <= 'Z' && !(Main.mapOfFacts.get(block.charAt(1)).equals(Main.UNDEFINED)))
            return isInFacts(block.charAt(1));
        return false;
    }

    public static boolean isInFacts(String block){
        if (block == null)
            Message.errorMsg("ERROR: NULL was passed to isCorrect()!");
        if (block.equals("true") || block.equals("false"))
            return true;
        if (block.length() > 2 || block.length() < 1)
            return false;
        if (block.length() == 1 && block.charAt(0) >= 'A' && block.charAt(0) <= 'Z')
            return isInFacts(block.charAt(0));
        if (block.length() == 2 && block.charAt(0) >= '!' && block.charAt(1) >= 'A' && block.charAt(1) <= 'Z')
            return isInFacts(block.charAt(1));
        Message.errorMsg("Wrong block to search in facts: " + block);
        return false;
    }

    public static boolean getValueFromFacts(String block){
        if (block == null)
            Message.errorMsg("ERROR: NULL was passed to getValueFromFacts()!");
        if (block.equals("true"))
            return true;
        if (block.equals("false"))
            return false;
        if (!(block.length() > 2 || block.length() < 1)) {
            if (block.length() == 1)
                return isInFacts(block.charAt(0));
            if (block.length() == 2 && block.charAt(0) >= '!')
                return !(isInFacts(block.charAt(1)));
        }
        Message.errorMsg("Not in facts or Wrong block to get fact: " + block);
        return false;
    }

    public static  boolean isCorrect(String block){
        return isCorrect(block, false);
    }

    public static boolean isCorrect(String block, boolean silent){
        if (block == null)
            Message.errorMsg("ERROR: NULL was passed to isCorrect()!");
        if (block.equals("true") || block.equals("false"))
            return true;
        if (block.length() > 2 || block.length() < 1)
            return false;
        if (block.length() == 1 && block.charAt(0) >= 'A' && block.charAt(0) <= 'Z')
            return true;
        if (block.length() == 2 && block.charAt(0) >= '!' && block.charAt(1) >= 'A' && block.charAt(1) <= 'Z')
            return true;
        if (!silent) Message.errorMsg("Incorrect block: " + block);
        return false;
    }



    public static boolean isOperand(String block){
        if (block != null && block.length() == 1
                && (block.charAt(0) == '+' || block.charAt(0) == '^' || block.charAt(0) == '|')
           )
            return true;
        return false;
    }

    public static LinkedList<String> tryToFixNots(LinkedList<String> raw, int posToFixFrom) {
        return tryToFixNots(raw, posToFixFrom, false);
    }
    public static LinkedList<String> tryToFixNots(LinkedList<String> raw, int posToFixFrom, boolean disablePreviousOperandCheck){
        if (!disablePreviousOperandCheck && posToFixFrom == 1) {
            Message.errorMsg("Unfixable raw!");
        }

        int count_nots = 0;
        int i;

        for (i = posToFixFrom - 1; i >= 0; i--){
            if (raw.get(i).equals("!"))
                count_nots++;
            else
                break;
        }
        if (count_nots == 0)
            Message.errorMsg("Wrong data passed to fixer!");
        if (i < 0 && !disablePreviousOperandCheck)
            Message.errorMsg("ERROR: no previous operand found by fixer!");
        if (!disablePreviousOperandCheck && !isCorrect(raw.get(i)))
            Message.errorMsg("ERROR: wrong previous operand found by fixer: " + raw.get(i));
        if (isCorrect(raw.get(posToFixFrom)) && raw.get(posToFixFrom).length() == 2) {
            raw.set(posToFixFrom, Character.toString(raw.get(posToFixFrom).charAt(1)));
            count_nots++;
        }

        if (count_nots % 2 == 1)
            raw.set(posToFixFrom, "!" + raw.get(posToFixFrom));
        for (int z = posToFixFrom - 1; z > i; z--){
            if (z < 0)
                break;
            raw.remove(z);
        }

        return raw;
    }

    public static LinkedList<String> solveBlock(LinkedList<String> raw, int i){
        boolean result=false;
        if (Main.debugOn)System.out.println("\nI need to: " + raw.get(i - 2) + raw.get(i) + raw.get(i - 1));
        if (isCorrect(raw.get(i - 1))){
            if(isCorrect(raw.get(i - 2), true))
            {
                boolean operandA = getValueFromFacts(raw.get(i - 2));
                boolean operandB = getValueFromFacts(raw.get(i - 1));

                if (isDefined(raw.get(i - 2))) {
                    if (Main.debugOn)
                        System.out.println(raw.get(i - 2) + " is defined and is " + (operandA ? "TRUE" : "FALSE"));
                } else {
                    if (Main.debugOn) System.out.println(raw.get(i - 2) + " is UNDEFINED, so assume as FALSE");
                }

                if (isDefined(raw.get(i - 1))) {
                    if (Main.debugOn)
                        System.out.println(raw.get(i - 1) + " is defined and is " + (operandB ? "TRUE" : "FALSE"));
                } else {
                    if (Main.debugOn) System.out.println(raw.get(i - 1) + " is UNDEFINED, so assume as FALSE");
                }

                switch (raw.get(i).charAt(0)) {
                    case '+'://AND &
                        result = operandA && operandB;
                        break;

                    case '^'://XOR ^
                        result = operandA ^ operandB;
                        break;

                    case '|'://OR |
                        result = operandA || operandB;
                        break;

                    default:
                        Message.errorMsg("Incorrect operand: " + raw.get(i));
                        break;
                }

                if (i >= 3 && raw.get(i - 3).length() == 1 && raw.get(i - 3).charAt(0) == '!') {
                    result = !result;
                    raw.set(i, result ? "true" : "false");
                    raw.remove(i - 1);
                    raw.remove(i - 2);
                    raw.remove(i - 3);
                } else {
                    raw.set(i, result ? "true" : "false");
                    raw.remove(i - 1);
                    raw.remove(i - 2);
                }

                Message.infoMsg("Result of calculation: " + (result ? "TRUE" : "FALSE"));
            }
            else
            {
                raw = tryToFixNots(raw, i - 1);
            }
        }
        else
            Message.errorMsg("Right operand is incorrect: " + raw.get(i - 1));
        return raw;
    }

    public static int findFirstOperand(LinkedList<String> raw){
        for (int i = 0; i < raw.size(); i++){
            if (!raw.get(i).equals("!"))
                return i;
        }
        Message.errorMsg("ERROR: No first operand found");
        return -1;
    }

    public static boolean solve(int j){
        if (Main.debugOn)System.out.println("\nNow:" + Main.tableList.get(j).toString());
        return solveRaw(Main.tableList.get(j));
    }

    public static boolean solveRaw(LinkedList<String> raw) {
        if (Main.debugOn)System.out.println();
        if (raw.size() >= 3) {
            int  i;
            for (i = 0; i < raw.size(); i++) {
                if (isOperand(raw.get(i))) {
                    if (i >= 2) {//якщо зліва є два символи
                        raw = solveBlock(raw, i);
                        if (Main.debugOn)System.out.println("Polska Notation after solving block: " + raw.toString());
                        i = -1;
                        break;
                    } else
                        break;
                }
            }
            if (i >= raw.size())
            {
                raw = tryToFixNots(raw, findFirstOperand(raw), true);
//                Message.errorMsg("it is a trap!");
            }
            return solveRaw(raw);
        }else if (raw.size() == 1) {
            Message.infoMsg("Lasted ONE:" + raw.toString());
            return (getValueFromFacts(raw.get(0)));
        }else if (raw.size() == 2){
            Message.infoMsg("Lasted TWO:" + raw.toString());
            if (raw.get(0).equals("!") && isCorrect(raw.get(1)))
            {
                return !(getValueFromFacts(raw.get(1)));
            }
            else
                Message.errorMsg("ERROR: this is not handled");
            return false;
        }else {
            Message.errorMsg("ERROR: empty input in solveRaw()");
            return false;
        }

    }

    public static void bc(char query) {// throws IOException {
        ArrayList<Integer> masQuery = new ArrayList<>();
        ArrayList<Integer> masCondit = new ArrayList<>();

        if (!Main.facts.contains(query)) {
            if (Main.debugOn)System.out.println("\u001B[33m" + "Query to evaluate: " + query + "\u001B[0m");
            int indexQuery = query - 'A';
            masQuery = findAllQuery(indexQuery);
            if (Main.debugOn)System.out.println("masQuery " + masQuery.size());
            if (masQuery.size() < 1) {//якщо немає в стовпці 2 (справа)
                Main.mapOfFacts.put(query, Main.UNDEFINED);
            }
            for (int j = 0; j < masQuery.size(); j++) {
                masCondit = findAllCondition(masQuery.get(j));
                if (Main.debugOn)System.out.println("masCond" + masCondit);
                for (int k = 0; k < masCondit.size(); k++) {
                    bc((char) (masCondit.get(k) + 'A'));
                }
                boolean nikResult =  solve(masQuery.get(j));
                Message.infoMsg("\nNik's magic result is: " + nikResult);
                if (nikResult)
                {
                    if (!Main.facts.contains(query))
                    {
                        Main.facts.add(query);
                        if (Main.debugOn)System.out.println("Adding \'" + query + "\' to facts");
                    }
                    Main.mapOfFacts.replace(query, Main.TRUE);
                    if (Main.debugOn)System.out.println("Setting \'" + query + "\' in mapOfFacts  to TRUE");
                }else{
                    if (Main.mapOfFacts.replace(query, Main.UNDEFINED, Main.FALSE)) {
                        if (Main.debugOn)System.out.println("Setting \'" + query + "\' in mapOfFacts from UNDEFINED to FALSE");
                    }

                }
                Message.infoMsg("Task was to find: " + query + "\n\n\n\n\n");
            }
        }
    }
}
