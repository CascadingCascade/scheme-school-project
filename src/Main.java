import scheme.*;

public class Main {
    public static void main(String[] args) {
        REPL repl = new REPL(System.in, System.out);
        repl.runREPL(true, false);
    }
}
