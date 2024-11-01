package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doList extends ProcedureDispatch {
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair car = (Pair) args.getCar();
        Pair list = new Pair(car, Pair.nilP);
        if (Pair.isNil(args.cdr))
            return list;
        Pair lp = list;
        Pair ap = args.cdr;
        while (!Pair.isNil(ap)) {
            Pair car1 = (Pair) ap.getCar();
            lp.cdr = new Pair(car1,
                    Pair.nilP);
            lp = lp.cdr;
            ap = ap.cdr;
        }
        return list;
    }
}
