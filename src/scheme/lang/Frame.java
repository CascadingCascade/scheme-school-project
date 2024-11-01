package scheme.lang;

import java.util.HashMap;

public class Frame {
    private final Frame parent;
    public HashMap<String, Object> bindings;

    public Frame(Frame parent) {
        this.parent = parent;
        bindings = new HashMap<>();
    }

    public Object resolve(String name) {
        return resolveImpl(name, true);
    }

    private Object resolveImpl(String name, boolean sef) {
        if (sef) {
            try {
                return Integer.parseInt(name);
            } catch (NumberFormatException _) {}
            try {
                return Double.parseDouble(name);
            } catch (NumberFormatException _) {}
            switch (name) {
                case "true", "#t" -> {
                    return true;
                }
                case "false", "#f" -> {
                    return false;
                }
                case "nil" -> {
                    return Pair.nilP;
                }
            }
            if (name.startsWith("\""))
                return name.substring(1, name.length() - 1);
        }

        if (bindings.containsKey(name))
            return bindings.get(name);
        if (parent == null)
            return null;
        return parent.resolveImpl(name, false);
    }

    public Frame getParent() {
        return parent;
    }
}
