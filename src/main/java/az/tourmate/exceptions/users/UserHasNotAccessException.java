package az.tourmate.exceptions.users;

public class UserHasNotAccessException extends RuntimeException{

    public UserHasNotAccessException(String s) {
        super(s);
    }
}
