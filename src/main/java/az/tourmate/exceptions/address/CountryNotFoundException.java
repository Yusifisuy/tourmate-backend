package az.tourmate.exceptions.address;

public class CountryNotFoundException extends RuntimeException{

    public CountryNotFoundException(String s) {
        super(s);
    }
}
