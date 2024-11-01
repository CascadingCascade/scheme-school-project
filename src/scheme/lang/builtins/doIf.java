package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doIf extends SpecialFormDispatch {
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair pred = (Pair) args.getCar();
        Pair conseq = (Pair) args.cdr.getCar();
        boolean hasAlt = !Pair.isNil(args.cdr.cdr);
        boolean taken = Pair.canBeTrue(Eval.eval(pred, context, false, 0, false, shell));
        if (taken) {
            return Eval.eval(conseq, context, true, 0, false, shell);
        } else if (hasAlt) {
            Pair alt = (Pair) args.cdr.cdr.getCar();
            return Eval.eval(alt, context, true, 0, false, shell);
        }

        return null;
    }
}
