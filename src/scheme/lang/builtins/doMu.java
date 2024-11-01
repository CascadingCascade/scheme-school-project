package scheme.lang.builtins;

import scheme.REPL;
import scheme.lang.Frame;
import scheme.lang.Pair;
import scheme.lang.SchemeException;
import scheme.lang.SpecialFormDispatch;

public class doMu extends SpecialFormDispatch {
    private static final doLambda dl = new doLambda();
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        LambdaProcedure l = (LambdaProcedure) dl.doEval(args, context, shell).getCar();
        l.muflag = true;
        return new Pair(l);
    }
}
