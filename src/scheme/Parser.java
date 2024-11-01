package scheme;

import scheme.lang.*;

import java.util.ArrayList;

public class Parser {
    public static Pair parse(String input) {
        input = preprocess(input);
        return parseImpl(input);
    }
    private static Pair parseImpl(String input) {
        assert input != null;
        assert !input.isBlank();
        Pair p = new Pair();
        if (!input.startsWith("(")) {
            if (input.equals("nil"))
                return Pair.nilP;
            return new Pair(input);
        }
        String[] strings = getSubExps(input);
        p.setCar(parseImpl(strings[0]));
        p.cdr = parseImpl(strings[1].strip());
        return p;
    }

    public static String preprocess(String raw) {
        String t = raw.strip();
        if (t.equals("()"))
            return "nil";
        switch (t.charAt(0)) {
            case '\'' -> {
                return "(quote (" + preprocess(t.substring(1)) + " nil))";
            }
            case '`' -> {
                return "(quasiquote (" + preprocess(t.substring(1)) + " nil))";
            }
            case ',' -> {
                return "(unquote (" + preprocess(t.substring(1)) + " nil))";
            }
            case '(' -> {
                StringBuilder builder = new StringBuilder("(");
                String[] subs = getSubExps(t);
                if (subs.length == 1 || (subs.length == 2 && subs[1].equals("nil"))) {
                    builder.append(preprocess(subs[0]));
                    builder.append(" nil)");
                    return builder.toString();
                }
                for (String sub : subs) {
                    builder.append(preprocess(sub));
                    // Ignore that warning, I AM trying to compare index
                    if (sub != subs[subs.length - 1])
                        builder.append(" (");
                }
                builder.append(" nil)");
                builder.append(")".repeat(subs.length - 1));
                return builder.toString();
            }
            default -> {
                if(t.equals("else")) {
                    return "#t";
                }
                return t;
            }
        }
    }

    private static String[] getSubExps(String in) {
        ArrayList<String> strings = new ArrayList<>();
        in = in.substring(1, in.length() - 1);

        if (in.contains("(")) {
            in = in.concat(" ");
            int depth = 0, dex1 = 0;
            for (int i = 0; i < in.length(); i++) {
                if (in.charAt(i) == '(')
                    depth++;
                if (in.charAt(i) == ')')
                    depth--;
                if (depth == 0 && in.charAt(i) == ' ') {
                    String t = in.substring(dex1, i);
                    if (!t.isEmpty())
                        strings.add(t);
                    dex1 = i + 1;
                }
            }
            return strings.toArray(new String[0]);
        }
        return in.split(" ");
        }
}
