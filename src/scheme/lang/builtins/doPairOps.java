package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doPairOps extends ProcedureDispatch {
    private final String type;

    public doPairOps(String type) {
        this.type = type;
        if (type.equals("cons"))
            argC = 2;
        else
            argC = 1;
    }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        return switch (type) {
            case "cons" -> {
                Pair cdr = (Pair) args.cdr.getCar();
                Pair car = (Pair) args.getCar();
                yield new Pair(car, cdr);
            }
            case "car", "cdr" -> {
                Pair res = (Pair) args.getCar();
                if (Pair.isBox(res) && res.getCar() instanceof Pair p)
                    res = p;
                if (type.equals("car"))
                    yield (Pair) res.getCar();
                yield res.cdr;
            }
            default -> throw new SchemeException("This shouldn't happen @ pair operations");
        };
    }
}
