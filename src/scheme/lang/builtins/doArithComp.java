package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doArithComp extends ProcedureDispatch {
    private final char type;

    public doArithComp(char type) {
        this.type = type;
        argC = 2;
    }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        if (args.listLen() != 2)
            throw new SchemeException("Expected 2 arguments, got" + args.listLen());
        Pair lhs = (Pair) args.getCar();
        Pair rhs = (Pair) args.cdr.getCar();
        Object lhso = Eval.eval(lhs, context, false, 0, false, shell).getCar();
        Object rhso = Eval.eval(rhs, context, false, 0, false, shell).getCar();
        boolean result;
        boolean pF = false;
        switch (type) {
            case '=': {
                if (lhso.getClass() != rhso.getClass()) {
                    result = false;
                    break;
                }
                if (lhso instanceof Integer) {
                    result = ((Integer) lhso).intValue() == ((Integer) rhso).intValue();
                    break;
                }
                if (lhso instanceof Double) {
                    result = ((Double) lhso).doubleValue() == ((Double) rhso).doubleValue();
                    break;
                }
            }
            case '>': {
                if (lhso instanceof Integer a && rhso instanceof Integer b) {
                    result = a > b;
                    break;
                }
                result = ((Number) lhso).doubleValue() > ((Number) rhso).doubleValue();
                break;
            }
            case '<': {
                if (lhso instanceof Integer a && rhso instanceof Integer b) {
                    result = a < b;
                    break;
                }
                result = ((Number) lhso).doubleValue() < ((Number) rhso).doubleValue();
                break;
            }
            case ',': {
                if (lhso instanceof Integer a && rhso instanceof Integer b) {
                    result = a <= b;
                    break;
                }
                result = ((Number) lhso).doubleValue() <= ((Number) rhso).doubleValue();
                break;
            }
            case '.': {
                if (lhso instanceof Integer a && rhso instanceof Integer b) {
                    result = a >= b;
                    break;
                }
                result = ((Number) lhso).doubleValue() >= ((Number) rhso).doubleValue();
                break;
            }
            default:
                throw new SchemeException("This shouldn't happen @ comp");
        }
        return new Pair(result);
    }
}
