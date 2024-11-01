package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class LambdaProcedure extends ProcedureDispatch {
    public boolean muflag;
    public final Frame defEnv;
    private final String[] formals;
    private final Pair body;
    // real lambda is nameless, but this helps with debugging
    public String name = "lambda";

    public LambdaProcedure(boolean muflag, Frame defEnv, String[] formals, Pair body) {
        this.muflag = muflag;
        this.defEnv = defEnv;
        this.formals = formals;
        this.body = body;
        argC = formals.length;
    }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair argsc = args;
        if (args.listLen() != formals.length)
            throw new SchemeException("Expected " + formals.length + " arguments, got " + args.listLen());
        for (String name : formals) {
            Object value = argsc.getCar();
            if (value instanceof Pair p && Pair.isBox(p))
                value = p.getCar();
            context.bindings.put(name, value);
            argsc = argsc.cdr;
        }
        if (Pair.isNil(body.cdr)) {
            return Eval.eval((Pair) body.getCar(),context, false, 0, false, shell);
        }
        return Eval.eval(body, context, false, 0, true, shell);
    }

    @Override
    public String toString() {
        return name;
    }
}
