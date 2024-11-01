package scheme.lang;

public class SchemeException extends Exception{
    public SchemeException(String s) {
        super(s);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
