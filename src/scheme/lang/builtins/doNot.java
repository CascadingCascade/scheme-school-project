package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.Frame;
import scheme.lang.Pair;
import scheme.lang.ProcedureDispatch;
import scheme.lang.SchemeException;

public class doNot extends ProcedureDispatch {
    public doNot() { argC = 1; }
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair evaled = Eval.eval((Pair) args.getCar(), context, false, 0, false, shell);
        return new Pair(!Pair.canBeTrue(evaled));
    }
}
