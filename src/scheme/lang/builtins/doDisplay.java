package scheme.lang.builtins;

import scheme.Eval;
import scheme.REPL;
import scheme.lang.*;

public class doDisplay extends SpecialFormDispatch {
    private final String type;

    public doDisplay(String type) {
        this.type = type;
    }

    @Override
    public Pair doEval(Pair args, Frame context, REPL shell) throws SchemeException {
        switch (type) {
            case "display", "displayln": {
                if (Pair.isNil(args))
                    return null;
                Pair toPrint = Eval.eval((Pair) args.getCar(), context, false, 0 ,false, shell);
                shell.print(toPrint.toString() + ((type.equals("displayln")) ? "\n" : ""));
                return null;
            }
            case "newline": {
                shell.print("\n");
                return null;
            }
            default: {
                throw new SchemeException("This shouldn't happen @ display");
            }
        }
    }
}
