public class Message {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    
    public Message() {
    }

    public static void exception(String mess){
        System.out.println(ANSI_RED + mess + ANSI_RESET);
        System.exit(1);
    }

}
