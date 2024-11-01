package scheme.lang;

import scheme.REPL;

public abstract class EvalDispatch {
    //args will always be expecting a pair/list
    public abstract Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException;
}

