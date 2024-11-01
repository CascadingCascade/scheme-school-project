package scheme.lang.builtins;

import scheme.REPL;
import scheme.lang.*;

public class doExit extends SpecialFormDispatch {
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        throw new SchemeException("exit");
    }
}
