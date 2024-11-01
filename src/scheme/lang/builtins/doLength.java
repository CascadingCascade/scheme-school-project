package scheme.lang.builtins;

import scheme.REPL;
import scheme.lang.Frame;
import scheme.lang.Pair;
import scheme.lang.ProcedureDispatch;
import scheme.lang.SchemeException;

public class doLength extends ProcedureDispatch {
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        return new Pair(((Pair)args.getCar()).listLen());
    }
}
