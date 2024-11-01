package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.Frame;
import scheme.lang.Pair;
import scheme.lang.ProcedureDispatch;
import scheme.lang.SchemeException;

public class doEqual extends ProcedureDispatch {
    private final boolean compareValue;

    public doEqual(boolean compareValue) {
        this.compareValue = compareValue;
        argC = 2;
    }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair lhs = (Pair) args.getCar();
        Pair rhs = (Pair) args.cdr.getCar();
        if (!Pair.isBox(lhs) && !Pair.isBox(rhs)) {
            if (compareValue)
                return new Pair(lhs.equals(rhs));
            return new Pair(lhs == rhs);
        }
        return new Pair(lhs.getCar().equals(rhs.getCar()));
    }
}
