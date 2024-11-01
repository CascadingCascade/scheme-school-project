package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.Frame;
import scheme.lang.Pair;
import scheme.lang.ProcedureDispatch;
import scheme.lang.SchemeException;

public class doApply extends ProcedureDispatch {
    public doApply() { argC = 2; }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair toEval = new Pair(null, args.cdr);
        Pair proc = Eval.eval((Pair) args.getCar(), context, false, 0, false, shell);
        if (!(proc.getCar() instanceof ProcedureDispatch))
            throw new SchemeException(proc + "can't be applied");
        toEval.setCar(proc);
        return Eval.eval(toEval, context, true, 0, false, shell);
    }
}
