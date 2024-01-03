package az.tourmate.utils;

import az.tourmate.models.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public class UserUtil {

    public static User getConnectedUser(Principal connectedUser){
        return (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    }
}
