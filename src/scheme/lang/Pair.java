package scheme.lang;


import java.util.Objects;

public class Pair {
    // Should be just symbols/self-eval-able-strings or other pairs at the beginning of evaluation
    // We have the constraint that if this Pair is not a box then car would be another Pair, maybe box
    // And if it is then car would be a string
    private Object car;
    public Pair cdr;

    public Pair(Object car, Pair cdr) {
        this.setCar(car);
        this.cdr = cdr;
    }

    // wrapper constructor
    public Pair(Object car) {
        this(car, wrapperS);
    }
    // I'll-put-data-in-later constructor
    public Pair() {
        this(nilP, nilP);
    }

    private static final Pair wrapperS = new Pair("\"If you're seeing this something's wrong\"");
    public static final Pair nilP = new Pair("nil");

    public static boolean isNil(Pair p) {
        return p == nilP;
    }

    public static boolean isBox(Pair p) {
        return !(p == null) && p.cdr == wrapperS && !isNil(p);
    }

    public static Pair boxIfNecessary(Object o) {
        return (o instanceof Pair p) ? p : new Pair(o);
    }

    public static String getBoxedCarHelper(Pair p) {
        return (String) ((Pair) p.getCar()).getCar();
    }

    public int listLen() {
        if (this == nilP || this == wrapperS)
            return 0;
        if (cdr == nilP || cdr == wrapperS) {
            return 1;
        }
        return cdr.listLen() + 1;
    }
    public static boolean canBeTrue(Pair p) {
        return !(p.getCar() instanceof Boolean b && !b);
    }

    private static String toStringHelper(Object o) {
        switch (o) {
            case null -> {
                return "undefined";
            }
            case Boolean b -> {
                if (b)
                    return "#t";
                return "#f";
            }
            case String s when s.startsWith("\"") -> {
                return s.substring(1, s.length() - 1);
            }
            default -> {
                return o.toString();
            }
        }
    }

    @Override
    public String toString() {
        if (cdr == wrapperS)
            return toStringHelper(getCar());
        if (cdr == nilP) {
            return "(" + toStringHelper(getCar()) + ")";
        }
        StringBuilder builder = new StringBuilder("(");
        builder.append(toStringHelper(getCar()));
        Pair p = cdr;
        while (!isNil(p)) {
            builder.append(" ");
            builder.append(toStringHelper(p.car));
            p = p.cdr;
        }
        builder.append(")");
        return builder.toString();
    }

    public Object getCar() {
        return car;
    }

    public void setCar(Object car) {
        assert car == null
//                || car instanceof Integer
//                || car instanceof Double
                || car instanceof String
                || car instanceof Pair;
        this.car = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair pair)) return false;
        return Objects.equals(getCar(), pair.getCar()) && Objects.equals(cdr, pair.cdr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCar(), cdr);
    }
}
