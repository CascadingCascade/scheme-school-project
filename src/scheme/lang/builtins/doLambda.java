package scheme.lang.builtins;

import scheme.REPL;
import scheme.lang.*;

import java.util.ArrayList;

public class doLambda extends SpecialFormDispatch {
    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        Pair argsList = (Pair) args.getCar();
        Pair body = args.cdr;
        ArrayList<String> lambdaArgs = new ArrayList<>();
        while (!Pair.isNil(argsList)) {
            lambdaArgs.add(Pair.getBoxedCarHelper(argsList));
            argsList = argsList.cdr;
        }
        LambdaProcedure lambda = new LambdaProcedure(false, context, lambdaArgs.toArray(new String[0]), body);
        return new Pair(lambda);
    }
}
