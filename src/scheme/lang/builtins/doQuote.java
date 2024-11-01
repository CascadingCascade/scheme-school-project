package scheme.lang.builtins;

import scheme.REPL;
import scheme.lang.EvalDispatch;
import scheme.lang.Frame;
import scheme.lang.Pair;
import scheme.lang.SpecialFormDispatch;

public class doQuote extends SpecialFormDispatch {
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) {
        return (Pair) args.getCar();
    }
}
