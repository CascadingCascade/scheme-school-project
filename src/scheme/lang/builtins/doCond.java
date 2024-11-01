package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doCond extends SpecialFormDispatch {
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair iter = args;

        while (!Pair.isNil(iter)) {
            Pair clause = (Pair) iter.getCar();
            boolean caseTaken = Pair.canBeTrue(Eval.eval((Pair) clause.getCar(), context, false, 0, false, shell));
            if (caseTaken)
                return Eval.eval(Pair.boxIfNecessary(clause.cdr.getCar()), context, true, 0, false, shell);
            iter = iter.cdr;
        }

        return null;
    }
}
