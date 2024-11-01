package scheme;

import scheme.lang.*;
import scheme.lang.builtins.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Function;

public class REPL {
    private final Frame gFrame = new Frame(null);

    private static final String introMsg = "CS61A Scheme Interpreter";
    private static final String prompt = "\nscm>";
    private final PrintStream printer;
    private final Scanner scanner;
    private boolean hasPrinted = false;

    public REPL(InputStream inputStream, OutputStream outputStream) {
        printer = new PrintStream(outputStream);
        scanner = new Scanner(inputStream);
        registerBuiltin();
    }

    private String getExp(Scanner scanner) throws SchemeException {
        String line = scanner.nextLine().strip();
        if (line.startsWith(";"))
            return line;
        if (line.isEmpty())
            return null;
        StringBuilder builder = new StringBuilder(line);
        Function<String, Integer> countDepth = (String s) -> {
            int d = 0;
            for (char c : s.toCharArray()) {
                if (c == '(')
                    d++;
                if (c == ')')
                    d--;
            }
            return d;
        };
        int depth = countDepth.apply(line);
        while (depth != 0) {
            line = scanner.nextLine();
            builder.append(line);
            depth += countDepth.apply(line);
            if (depth < 0)
                throw new SchemeException("Invalid Expression");
        }
        return builder.toString();
    }

    // Kinda ugly but I don't want to make it read a config file or something
    private void registerBuiltin() {
        // arithmetics
        gFrame.bindings.put("+", new doArithmetics('+'));
        gFrame.bindings.put("-", new doArithmetics('-'));
        gFrame.bindings.put("*", new doArithmetics('*'));
        gFrame.bindings.put("/", new doArithmetics('/'));
        gFrame.bindings.put("abs", new doAbs());
        gFrame.bindings.put("modulo", new doArithmetics('%'));
        gFrame.bindings.put("expt", new doArithmetics('^'));
        gFrame.bindings.put("quotient", new doArithmetics('?'));
        gFrame.bindings.put("remainder", new doArithmetics('5'));

        // comparisons
        gFrame.bindings.put("<", new doArithComp('<'));
        gFrame.bindings.put(">", new doArithComp('>'));
        gFrame.bindings.put("=", new doArithComp('='));
        gFrame.bindings.put("<=", new doArithComp(','));
        gFrame.bindings.put(">=", new doArithComp('.'));

        // special forms
        gFrame.bindings.put("quote", new doQuote());
        gFrame.bindings.put("exit", new doExit());
        gFrame.bindings.put("define", new doDefine());
        gFrame.bindings.put("lambda", new doLambda());
        gFrame.bindings.put("mu", new doMu());
        gFrame.bindings.put("begin", new doBegin());
        gFrame.bindings.put("cond", new doCond());
        gFrame.bindings.put("if", new doIf());
        gFrame.bindings.put("let", new doLet());
        gFrame.bindings.put("display", new doDisplay("display"));
        gFrame.bindings.put("displayln", new doDisplay("displayln"));
        gFrame.bindings.put("newline", new doDisplay("newline"));
        gFrame.bindings.put("and", new doLogicalForms("and"));
        gFrame.bindings.put("or", new doLogicalForms("or"));

        // pair/list operations
        gFrame.bindings.put("list", new doList());
        gFrame.bindings.put("cons", new doPairOps("cons"));
        gFrame.bindings.put("car", new doPairOps("car"));
        gFrame.bindings.put("cdr", new doPairOps("cdr"));
        gFrame.bindings.put("length", new doLength());
        gFrame.bindings.put("append", new doAppend());
        gFrame.bindings.put("map", new doListOps("map"));
        gFrame.bindings.put("filter", new doListOps("filter"));
        gFrame.bindings.put("reduce", new doListOps("reduce"));

        // type checks
        gFrame.bindings.put("null?", new doTypeChecks("null"));
        gFrame.bindings.put("atom?", new doTypeChecks("atom"));
        gFrame.bindings.put("boolean?", new doTypeChecks("bool"));
        gFrame.bindings.put("integer?", new doTypeChecks("int"));
        gFrame.bindings.put("list?", new doTypeChecks("list"));
        gFrame.bindings.put("pair?", new doTypeChecks("pair"));
        gFrame.bindings.put("number?", new doTypeChecks("number"));
        gFrame.bindings.put("procedure?", new doTypeChecks("procedure"));
        gFrame.bindings.put("string?", new doTypeChecks("string"));
        gFrame.bindings.put("symbol?", new doTypeChecks("symbol"));
        gFrame.bindings.put("even?", new doTypeChecks("even"));
        gFrame.bindings.put("odd?", new doTypeChecks("odd"));
        gFrame.bindings.put("zero?", new doTypeChecks("zero"));

        // others
        gFrame.bindings.put("eq?", new doEqual(false));
        gFrame.bindings.put("equal?", new doEqual(true));
        gFrame.bindings.put("not", new doNot());
        gFrame.bindings.put("apply", new doApply());
    }

    public void print(String s) {
        printer.print(s);
        hasPrinted = true;
    }

    public void runREPL(boolean interactiveF, boolean clrF) {
        if (clrF) {
            gFrame.bindings.clear();
            registerBuiltin();
        }

        if (interactiveF) {
            printer.println(introMsg);
        }
        while (true) {
            if (interactiveF)
                printer.print(prompt);
            String exp;
            try {
                exp = getExp(scanner);
            } catch (SchemeException e) {
                printer.println(e);
                continue;
            }
            if (exp == null) {
                continue;
            }
            if (exp.startsWith(";")){
                printer.println(exp);
                continue;
            }
            Pair parsedExp = Parser.parse(exp);
            try {
                Pair res = Eval.eval(parsedExp, gFrame, true, 0, false, this);
                if (hasPrinted && res == null)
                    continue;
                if (res == null)
                    printer.print("undefined");
                else
                    printer.print(res);
            } catch (SchemeException e) {
                if (e.getMessage().equals("exit"))
                    break;
                printer.println(e);
            }
        }
    }
}
