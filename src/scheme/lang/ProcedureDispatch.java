package scheme.lang;

import scheme.REPL;

public abstract class ProcedureDispatch extends EvalDispatch{
    public int argC = -1; // -1 for variadic
    public abstract Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException;
}
