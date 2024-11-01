package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doAbs extends ProcedureDispatch {
    public doAbs() { argC = 1; }
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Object num = Eval.eval((Pair) args.getCar(), context, false, 0, false, shell).getCar();
        if (!(num instanceof Number number))
            throw new SchemeException("Can't do abs on " + num);
        else if (number instanceof Integer)
            return new Pair(Math.abs(number.intValue()));
        return new Pair(Math.abs(number.doubleValue()));
    }
}
