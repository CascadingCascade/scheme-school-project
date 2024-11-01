package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doLet extends SpecialFormDispatch {

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair bindingsList = (Pair) args.getCar();
        Pair body = args.cdr;
        while (!Pair.isNil(bindingsList)) {
            Pair nvp = (Pair) bindingsList.getCar();
            String name = (String) ((Pair) nvp.getCar()).getCar();
            Pair expv = Eval.eval((Pair) nvp.cdr.getCar(), context, false, 0, false, shell);
            Object val = (Pair.isBox(expv)) ? expv.getCar() : expv;
            context.bindings.put(name, val);
            bindingsList = bindingsList.cdr;
        }
        return Eval.eval(body, context, false, 0, true, shell);
    }
}
