package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doArithmetics extends ProcedureDispatch {
    private final char type;

    public doArithmetics(char type) {
        this.type = type;
    }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        if (Pair.isNil(args)) {
            return switch (type) {
                case '+' -> new Pair(0);
                case '*' -> new Pair(1);
                default -> throw new SchemeException("Not enough arguments for" + type);
            };
        }
        Object lhs = Eval.eval((Pair) args.getCar(), context, false ,0, false, shell).getCar();
        if (!(lhs instanceof Number))
            throw new SchemeException("Can't do " + type + " on " + lhs);
        if (Pair.isNil(args.cdr)) {
            return switch (type) {
                case '+','*' -> new Pair(lhs);
                // Might actually want to wrap arithmetic stuff into another class actually
                case '-' -> (lhs instanceof Integer) ? new Pair(-(int)lhs) : new Pair(-(double)lhs);
                case '/' -> (lhs instanceof Integer i && i == 1) ? new Pair(1) : new Pair(1/(double)lhs);
                default -> throw new SchemeException("This shouldn't happen @ calc0");
            };
        }
        int sum = ((Number) lhs).intValue(); double sump = ((Number) lhs).doubleValue(); boolean pF = lhs instanceof Double;
        Pair argsc = args.cdr;
        do {
            Pair evaled = Eval.eval((Pair) argsc.getCar(), context, false, 0, false, shell);
            Object operand = evaled.getCar();
            if (!(operand instanceof Number))
                throw new SchemeException("Can't do " + type + " on " + operand);
            if (operand instanceof Double)
                pF = true;
            if (!pF) {
                switch (type) {
                    case '+': {
                        sum += (int)operand;
                        break;
                    }
                    case '-': {
                        sum -= (int)operand;
                        break;
                    }
                    case '*': {
                        sum *= (int)operand;
                        break;
                    }
                    case '/': {
                        if ((int)operand == 0)
                            throw new SchemeException("Division by zero");
                        if (sum % (int)operand == 0) {
                            sum /= (int)operand;
                            break;
                        }
                        pF = true;
                        break;
                    }
                    case '%': {
                        sum = (int) Math.copySign(sum %= (int)operand, sum);
                        break;
                    }
                    case '^': {
                        sum = (int) Math.pow(sum, (int)operand);
                        break;
                    }
                    case '?': {
                        sum /= (int) operand;
                        break;
                    }
                    case '5': {
                        sum =  (int) Math.copySign(sum %= (int)operand, (int)operand);
                        break;
                    }
                    default: {
                        throw new SchemeException("This shouldn't happen @ calc1");
                    }
                }
            }
            switch (type) {
                case '+': {
                    sump += ((Number) operand).doubleValue();
                    break;
                }
                case '-': {
                    sump -= ((Number) operand).doubleValue();
                    break;
                }
                case '*': {
                    sump *= ((Number) operand).doubleValue();
                    break;
                }
                case '/': {
                    if (((Number) operand).doubleValue() == 0)
                        throw new SchemeException("Division by zero");
                    sump /= ((Number) operand).doubleValue();
                    break;
                }
                case '%': {
                    sump = Math.copySign(sump %= ((Number) operand).doubleValue(), sum);
                    break;
                }
                case '^': {
                    sump = Math.pow(sump, ((Number) operand).doubleValue());
                    break;
                }
                case '?': {
                    // Do nothing
//                    sump /= ((Number) operand).doubleValue();
                    break;
                }
                case '5': {
                    sump =  Math.copySign(sump %= ((Number) operand).doubleValue(), ((Number) operand).doubleValue());
                    break;
                }
                default: {
                    throw new SchemeException("This shouldn't happen @ calc2");
                }
            }
            argsc = argsc.cdr;
        }   while (!Pair.isNil(argsc));
        if (pF && type != '?')
            return new Pair(sump);
        return new Pair(sum);
    }
}