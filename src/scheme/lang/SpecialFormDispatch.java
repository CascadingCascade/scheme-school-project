package scheme.lang;

import scheme.REPL;

public abstract class SpecialFormDispatch extends EvalDispatch{
    //Used to distinguish otherwise similar things, nothing much to see here
    public abstract Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException;
}
