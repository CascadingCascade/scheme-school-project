package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doBegin extends SpecialFormDispatch {
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        if (Pair.isNil(args.cdr))
            return Eval.eval((Pair) args.getCar(), context, true, 0, false, shell);
        Pair ap = args;
        while (!Pair.isNil(ap.cdr)) {
            Eval.eval((Pair) ap.getCar(), context, false, 0, false, shell);
            ap = ap.cdr;
        }
        return Eval.eval((Pair) ap.getCar(), context, true, 0, false, shell);
    }
}
