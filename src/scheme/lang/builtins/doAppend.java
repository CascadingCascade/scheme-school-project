package scheme.lang.builtins;

import scheme.REPL;
import scheme.lang.Frame;
import scheme.lang.Pair;
import scheme.lang.ProcedureDispatch;
import scheme.lang.SchemeException;

public class doAppend extends ProcedureDispatch {
    private Pair traverseToLast(Pair l) {
        Pair p = l;
        while (!Pair.isNil(p.cdr))
            p = p.cdr;
        return p;
    }
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair begin = (Pair) args.getCar();
        Pair p1 = traverseToLast(begin);
        Pair ap = args.cdr;
        while (!Pair.isNil(ap)) {
            p1.cdr = (Pair) ap.getCar();
            p1 = traverseToLast(p1);
            ap = ap.cdr;
        }
        return begin;
    }
}
