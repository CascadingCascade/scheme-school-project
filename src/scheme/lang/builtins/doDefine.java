package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

import java.util.ArrayList;

public class doDefine extends SpecialFormDispatch {
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        if (Pair.isBox(args))
            throw new SchemeException("Not enough argument for define");
        Pair defArgs = (Pair) args.getCar();
        Pair body = (!Pair.isNil(args.cdr.cdr))? args.cdr : (Pair) args.cdr.getCar();
        if (Pair.isBox(defArgs)) {
            String name = (String) defArgs.getCar();
            if (!Eval.identifierValidation(name))
                throw new SchemeException(name + " is not a valid/available identifier.");
            Pair val = Eval.eval(body, context, false, 0, false, shell);
            if (Pair.isBox(val))
                context.bindings.put(name, val.getCar());
            else
                context.bindings.put(name, val);
            if (val.getCar() instanceof LambdaProcedure l)
                l.name = name;
            return new Pair(name);
        }
        // We have a lambda then
        String name = Pair.getBoxedCarHelper(defArgs);
        if (!Eval.identifierValidation(name))
            throw new SchemeException(name + " is not a valid/available identifier.");
        ArrayList<String> lambdaArgs = new ArrayList<>();
        Pair iter = defArgs.cdr;
        while (!Pair.isNil(iter)) {
            lambdaArgs.add(Pair.getBoxedCarHelper(iter));
            iter = iter.cdr;
        }
        LambdaProcedure lambda = new LambdaProcedure(false, context, lambdaArgs.toArray(new String[0]), body);
        lambda.name = name;
        context.bindings.put(name, lambda);
        return new Pair(name);
    }
}
