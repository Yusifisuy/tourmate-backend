package az.tourmate.exceptions.users;

public class PasswordIsWrongException extends RuntimeException{

    public PasswordIsWrongException(String s) {
        super(s);
    }
}
