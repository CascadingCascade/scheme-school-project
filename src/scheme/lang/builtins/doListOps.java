package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.Frame;
import scheme.lang.Pair;
import scheme.lang.ProcedureDispatch;
import scheme.lang.SchemeException;

public class doListOps extends ProcedureDispatch {
    private final String type;

    public doListOps(String type) {
        this.type = type;
    }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {

        Pair proc = (Pair) args.getCar();
        Pair list = ((Pair) args.cdr.getCar());
        Pair res = new Pair(), rp = res, rtp = null;
        if (proc.getCar() instanceof ProcedureDispatch p) {
            switch (type) {
                case "map", "filter": {
                    if (p.argC != 1)
                        throw new SchemeException(type + " expected procedure with one argument");
                    while (!Pair.isNil(list)) {
                        Pair toEval = new Pair(proc, new Pair(Pair.boxIfNecessary(list.getCar()), Pair.nilP));
                        Pair evaled = Eval.eval(toEval,
                                context, Pair.isNil(list.cdr), 0, false, shell);
                        if (type.equals("map")) {
                            rp.setCar(evaled);
                            rp.cdr = new Pair();
                            list = list.cdr;
                            rtp = rp; rp = rp.cdr;
                            continue;
                        }
                        if (Pair.canBeTrue(evaled)) {
                            rp.setCar(list.getCar());
                            rp.cdr = new Pair();
                            list = list.cdr;
                            rtp = rp;
                            rp = rp.cdr;
                        } else {
                            list = list.cdr;
                        }
                    }
                    if (rtp != null)
                        rtp.cdr = Pair.nilP;
                    return res;
                }
                case "reduce": {
                    if (p.argC != 2)
                        throw new SchemeException("reduce expected procedure with two arguments");
                    if (Pair.isNil(list))
                        throw new SchemeException("reduce can't work on empty lists");
                    Pair base = (Pair) list.getCar();
                    Pair toEval = new Pair(proc, new Pair(base, new Pair()));
                    list = list.cdr;
                    while (!Pair.isNil(list)) {
                        toEval.cdr.cdr.setCar(list.getCar());
                        base = Eval.eval(toEval, context, Pair.isNil(list.cdr), 0, false, shell);
                        toEval.cdr.setCar(base);
                    }
                    return base;
                }
                default: {
                    throw new SchemeException("This shouldn't happen @ list operations");
                }
            }
        } else {
            throw new SchemeException(proc + " is not a procedure");
        }
    }
}
