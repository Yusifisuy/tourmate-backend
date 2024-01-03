package az.tourmate.exceptions.users;

public class UserIsNotFoundException extends RuntimeException{

    public UserIsNotFoundException(String s) {
        super(s);
    }
}
