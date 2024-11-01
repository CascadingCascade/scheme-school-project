package scheme.lang;

import java.util.HashSet;

public class Keywords {
    public static final HashSet<String> keywords = new HashSet<>();
    static {
        keywords.add("define");
        keywords.add("if");
        keywords.add("cond");
        keywords.add("exit");
        keywords.add("quote");
        keywords.add("quasiquote");
        keywords.add("unquote");
        keywords.add("lambda");
        keywords.add("and");
        keywords.add("or");
        keywords.add("let");
        keywords.add("display");
        keywords.add("displayln");
        keywords.add("newline");
        keywords.add("mu");
    }
}
