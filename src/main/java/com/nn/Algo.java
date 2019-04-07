package com.nn;

import java.util.*;

public class Algo {

    public static String operators = "+^|";
    public static String delimiters = "() " + operators;
    public static boolean flag = true;

    public static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i))
                return true;
        }
        return false;
    }

    public static boolean isOperator(String token) {
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i))
                return true;
        }
        return false;
    }

    public static int priority(String token) {
        if (token.equals("(")) return 1;
        if (token.equals("!")) return 2;
        if (token.equals("+")) return 5;
        if (token.equals("|")) return 4;
        if (token.equals("^")) return 3;
        return 1;
    }


    public static LinkedList<String> parse(String infix) {
        LinkedList<String> postfix = new LinkedList<>();
        Deque<String> stack = new ArrayDeque<String>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (curr.equals(" "))
                continue;
            else if (isDelimiter(curr)) {
                if (curr.equals("("))
                    stack.push(curr);
                else if (curr.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                    }
                    stack.pop();
                } else {
                    while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                        postfix.add(stack.pop());
                    }

                    stack.push(curr);
                }

            } else {
                postfix.add(curr);//????
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek()))
                postfix.add(stack.pop());
        }
//    if (Main.debugOn)System.out.println(postfix);
        return postfix;
    }

//    public static ArrayList<String> solve(ArrayList<String> list) {
////        Stack<String> stack = new Stack<>();
//        List<String> stack = new LinkedList<>();
//
////    for ()
//
//
//        return list;
//    }
}

