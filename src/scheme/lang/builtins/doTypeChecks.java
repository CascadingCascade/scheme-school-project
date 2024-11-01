package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doTypeChecks extends ProcedureDispatch {
    private final String type;

    public doTypeChecks(String type) {
        this.type = type;
        argC = 1;
    }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair evaledArgs = ((Pair) args.getCar());
        switch (type) {
            case "null": {
                // It sucks that it's possible to see both nil and boxed nil last time I checked
                // it may or may not be fixed now, can't be bother to check everywhere for it
                return new Pair(Pair.isNil(evaledArgs) || (Pair.isBox(evaledArgs) && evaledArgs.getCar() instanceof Pair p && Pair.isNil(p)));
            }
            case "atom": {
                return new Pair(Pair.isNil(evaledArgs) || Pair.isBox(evaledArgs)
                        || (Pair.isBox(evaledArgs) && evaledArgs.getCar() instanceof Pair p && Pair.isNil(p)));
            }
            case "bool": {
                return new Pair(evaledArgs.getCar() instanceof Boolean);
            }
            case "int": {
                return new Pair(evaledArgs.getCar() instanceof Integer);
            }
            case "list", "pair": {
                //todo: change if we add streams
                return new Pair(!Pair.isBox(evaledArgs));
            }
            case "number": {
                return new Pair(evaledArgs.getCar() instanceof Number);
            }
            case "procedure": {
                return new Pair(evaledArgs.getCar() instanceof ProcedureDispatch);
            }
            case "string": {
                return new Pair(evaledArgs.getCar() instanceof String s && s.startsWith("\""));
            }
            case "symbol": {
                return new Pair(evaledArgs.getCar() instanceof String s && Eval.identifierValidation(s));
            }
            case "odd": {
                return new Pair(evaledArgs.getCar() instanceof Integer i && i % 2 != 0);
            }
            case "even": {
                return new Pair(evaledArgs.getCar() instanceof Integer i && i % 2 == 0);
            }
            case "zero": {
                double precision = 0.0000001;
                return new Pair(evaledArgs.getCar() instanceof Number n &&
                        n.intValue() == 0 && Math.abs(n.doubleValue()) < precision);
            }
            default: {
                throw new SchemeException("This shouldn't happen @ type checks");
            }
        }
    }
}
