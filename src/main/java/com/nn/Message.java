package com.nn;

public class Message {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Message() {
    }

    public static void exception(String mess){
        System.out.println(ANSI_RED + mess + ANSI_RESET);
        System.exit(1);
    }

    public static void errorMsg(String msg){
        System.err.println(ANSI_RED + msg + ANSI_RESET);
        System.exit(1);
    }

    public static void infoMsg(String msg){

        if (Main.expandedPrint)System.out.println(ANSI_GREEN + msg + ANSI_RESET);
    }
}
