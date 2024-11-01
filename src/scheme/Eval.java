package scheme;

import scheme.lang.*;
import scheme.lang.builtins.LambdaProcedure;
import scheme.lang.builtins.doLet;

public class Eval {

    public static boolean identifierValidation(String string) {
        if (Keywords.keywords.contains(string))
            return false;
        try {
            Integer.parseInt(string);
            return false;
        } catch (NumberFormatException _) {}
        try {
            Double.parseDouble(string);
            return false;
        } catch (NumberFormatException _) {}
        return !string.contains("\"");
    }

    // I probably shouldn't have made it so stateless in retrospect
    // Actually I didn't test the quasiquote
    public static Pair eval(Pair exp, Frame context,
                            boolean tailFlag, int quoteCount, boolean listEvalAllowed,
                            REPL shell) throws SchemeException {
        int nqc = quoteCount;
        Object quoteT;
        if (exp.getCar() instanceof Pair pair && Pair.isBox(pair)) {
            quoteT = pair.getCar();
        } else
            quoteT = exp.getCar();

        if (quoteT.equals("quasiquote"))
            nqc++;
        if (quoteT.equals("unquote")) {
            if (quoteCount == 0)
                throw new SchemeException("Can't unquote no quotes");
            nqc--;
        }

        if (quoteCount > 0 && nqc == 0 ||
            quoteCount == 0 && nqc > 0) {
            return eval(exp.cdr, context, tailFlag, quoteCount, false, shell);
        }
        if (quoteCount > 0 && nqc == quoteCount) {
            if (exp.getCar() instanceof Pair pcar) {
                return new Pair(eval(pcar, context, tailFlag, nqc, false, shell),
                            eval(exp.cdr, context, tailFlag, nqc, false, shell));
            }
            return new Pair(exp.getCar(), eval(exp.cdr, context, tailFlag, nqc, false, shell));
        }
        if (Pair.isBox(exp)) {
            if (exp.getCar() instanceof String name) {
                Object val = context.resolve(name);
                if (val instanceof Pair pv)
                    return pv;
                return new Pair(val);
            }
            return exp;
        }
        if (Pair.isNil(exp))
            return Pair.nilP;

        Pair resolvedName = eval((Pair) exp.getCar(), context, false, 0, false, shell);
        // it comes back boxed, let's check and unbox

        if (!Pair.isBox(resolvedName) && !listEvalAllowed)
            throw new SchemeException("Call expression expected");
        if (Pair.isBox(resolvedName) && resolvedName.getCar() == null)
            throw new SchemeException("Can't resolve: " + exp.getCar());

        //We're looking at a call expression then
        if (Pair.isBox(resolvedName) && resolvedName.getCar() instanceof EvalDispatch dispatch) {
            if (dispatch instanceof ProcedureDispatch procedure) {


                Frame evalF;
                Pair toEvalArgList = exp.cdr;
                Pair evaledArgList = new Pair();
                Pair tp = evaledArgList;
                Pair rtp = null;
                while (!Pair.isNil(toEvalArgList)) {
                    Object t = Eval.eval((Pair) toEvalArgList.getCar(), context, false, 0, false, shell);
                    tp.setCar(t);
                    tp.cdr = new Pair();
                    rtp = tp;
                    tp = tp.cdr;
                    toEvalArgList = toEvalArgList.cdr;
                }
                if (rtp != null)
                    rtp.cdr = Pair.nilP;

                //If there's not enough arguments, let's assume that we're trying to get the procedure itself
                int givenArgC = evaledArgList.listLen();
                if (givenArgC < procedure.argC)
                    return resolvedName;

                if (procedure instanceof LambdaProcedure l) {
                    // We don't want to contaminate global frame
                    if (tailFlag && context.getParent() != null)
                        evalF = context;
                    else
                        evalF = (l.muflag) ? new Frame(context) : new Frame(l.defEnv);
                    evalF.bindings.putAll(l.defEnv.bindings);
                    return l.doEval(evaledArgList, evalF, shell);
                } else {
                    if (tailFlag && context.getParent() != null)
                        evalF = context;
                    else
                        evalF = new Frame(context);
                    return procedure.doEval(evaledArgList, evalF, shell);
                }
            }
            if (dispatch instanceof SpecialFormDispatch specialForm) {
                Frame evalF;
                if (specialForm instanceof doLet) {
                    if (tailFlag && context.getParent() != null)
                        evalF = context;
                    else
                        evalF = new Frame(context);
                } else
                    evalF = context;
                Pair argList = (Pair.isBox(exp.cdr) || Pair.isNil(exp.cdr)) ? new Pair(exp.cdr, Pair.nilP) : exp.cdr;
                return specialForm.doEval(argList, evalF, shell);
            }
        }


        //We're looking at a list of expression to be evaluated in order then
        if (listEvalAllowed && Pair.isNil(exp.cdr))
            return resolvedName;

        if (listEvalAllowed) {
            Pair iter = exp.cdr;
            while (!Pair.isNil(iter.cdr)) {
                Eval.eval((Pair) iter.getCar(), context, false, 0 ,false, shell);
                iter= iter.cdr;
            }
            return Eval.eval((Pair) iter.getCar(), context, tailFlag, 0, false, shell);
        }

        return exp;
    }
}
