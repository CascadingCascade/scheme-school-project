package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.Frame;
import scheme.lang.Pair;
import scheme.lang.SchemeException;
import scheme.lang.SpecialFormDispatch;

public class doLogicalForms extends SpecialFormDispatch {
    private final String type;

    public doLogicalForms(String type) {
        this.type = type;
    }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        if (Pair.isNil(args)) {
            return switch (type) {
                case "and" -> new Pair(true);
                case "or" -> new Pair(false);
                default -> throw new SchemeException("This shouldn't happen @ logical special forms");
            };
        }
        if (Pair.isBox(args)) {
            return Eval.eval(args, context, true, 0, false, shell);
        }
        Pair argsc = args, p = null;
        while (!Pair.isNil(argsc)) {
            boolean hasNext = Pair.isNil(argsc.cdr);
            p = Eval.eval((Pair) argsc.getCar(), context, !hasNext, 0, false, shell);
            boolean bcv = Pair.canBeTrue(p);
            switch (type) {
                case "and": {
                    if (!bcv)
                        return p;
                    break;
                }
                case "or": {
                    if (bcv)
                        return p;
                    break;
                }
                default:
                    throw new SchemeException("This shouldn't happen @ logical special forms");
            }
            argsc = argsc.cdr;
        }
        return p;
    }
}
